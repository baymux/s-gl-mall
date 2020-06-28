package com.bay.mall.product.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.Query;
import com.bay.mall.product.dao.CategoryDao;
import com.bay.mall.product.entity.CategoryEntity;
import com.bay.mall.product.service.CategoryBrandRelationService;
import com.bay.mall.product.service.CategoryService;
import com.bay.mall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1. 查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 2. 组装父子属性结构
        // 2.1 找到所有的一级分类
        List<CategoryEntity> level1Menus = categoryEntities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map(menu -> {
            menu.setChildren(getChildrens(menu, categoryEntities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        //TODO 检查当前删除的菜单,是否被别的地方引用

        // 逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * [2,25,225]
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentpath = findParentpath(catelogId, paths);

        Collections.reverse(parentpath);

        return (Long[]) parentpath.toArray(new Long[parentpath.size()]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());


    }

    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }

    /**
     * 先去查redis
     * TODO 产生堆外内存溢出
     * spring boot 2.0 以后使用 lettuce 操作redis客户端， 使用netty网络通信
     * lettuce 的bug 导致netty堆外内存溢出
     * <p>
     * 解决方案： 不能使用 虚拟机参数 解决
     * 1。升级lettuce客户端
     * 2. 使用jedis
     *
     * @return
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {

        // 1. 加入缓存逻辑,缓存中是json
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            // 2. 缓存中没有，查询数据库
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromWithRedissonLock();
            return catalogJsonFromDb;
        }

        // 转为指定的对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {

        });

        return result;
    }


    /**
     * 使用分布式锁
     * 缓存数据一致性问题
     * 1， 双写模式
     * 2， 失效模式
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromWithRedissonLock() {

        RLock catalogJsonLock = redissonClient.getLock("CatalogJson-lock");
        catalogJsonLock.lock();

        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            catalogJsonLock.unlock();

        }
        return dataFromDb;
    }


    /**
     * 使用分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromWithRedisLock() {

        // 1. 分布式锁
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            // 加锁成功
            // 设置过期时间，和加锁同步
            //stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
            } finally {
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";

                // 删除锁
                Long deleteLock = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                        Arrays.asList("lock"), uuid);
            }

            // 删除锁， 原子操作，使用lua脚本解锁
            //stringRedisTemplate.delete("lock");
            //String lockValue = stringRedisTemplate.opsForValue().get("lock");
            //if (uuid.equals(lockValue)) {
            //    stringRedisTemplate.delete("lock");
            //}

            return dataFromDb;
        } else {
            // 加锁失败，重试
            // 设置休眠时间
            return getCatalogJsonFromWithRedisLock(); // 自旋
        }

    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {

            });
            return result;
        }
        // 1. 查出所有1级分类
        List<CategoryEntity> level1Categorys = getLevel1Categorys();

        // 封装数据
        Map<String, List<Catelog2Vo>> level2CategoryMap = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 查询每个以及分类的所有二级分类
            List<CategoryEntity> level2Categorys = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<Catelog2Vo> catelog2Vos = null;
            if (level2Categorys != null) {
                catelog2Vos = level2Categorys.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());

                    // 查找二级分类下的所有三级分类
                    List<CategoryEntity> level3Categorys = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
                    if (level3Categorys != null) {
                        List<Catelog2Vo.Catalog3Vo> catalog3Vos = level3Categorys.stream().map(l3 -> {
                            Catelog2Vo.Catalog3Vo catalog3Vo = new Catelog2Vo.Catalog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catalog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        // 3. 查到的数据放入缓存
        String catalogJsonStr = JSONUtil.toJsonStr(level2CategoryMap);

        stringRedisTemplate.opsForValue().set("catalogJSON", catalogJsonStr, 1, TimeUnit.DAYS);

        return level2CategoryMap;
    }


    /**
     * 从数据库查询分类数据
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {

        // 加锁
        // TODO 本地锁synchronized， JUC(lock), 分布式情况下使用分布式锁
        synchronized (this) {
            return getDataFromDb();
        }
    }


    /**
     * 递归查找所有菜单的子菜单
     *
     * @param root 当前菜单
     * @param all  所有菜单
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> childern = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            // 找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // 排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return childern;
    }

    /**
     * 找到所有的父Id
     *
     * @param catelogId
     * @param paths
     * @return
     */
    private List<Long> findParentpath(Long catelogId, List<Long> paths) {
        // 先收集自己的id
        paths.add(catelogId);
        CategoryEntity byId = getById(catelogId);
        // 递归调用
        if (byId.getParentCid() != 0) {
            findParentpath(byId.getParentCid(), paths);
        }
        return paths;
    }
}