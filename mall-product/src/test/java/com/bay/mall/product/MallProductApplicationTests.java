package com.bay.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bay.mall.product.config.RedissonConfig;
import com.bay.mall.product.entity.BrandEntity;
import com.bay.mall.product.service.BrandService;
import com.bay.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class MallProductApplicationTests {

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;


    @Test
    public void testRedissonClient() {
        System.out.println(redissonClient);

    }


    @Test
    public void testRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world" + UUID.randomUUID().toString());

        // 查询
        String hello = ops.get("hello");
        System.out.println(hello);

    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));

    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setName("haha");
        //brandService.save(brandEntity);
       /* brandEntity.setBrandId(1L);
        brandEntity.setName("华为");
        brandEntity.setDescript("修改");
        brandService.updateById(brandEntity);
        System.out.println("success");*/

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));

        list.forEach(item -> {
            System.out.println(item);
        });
    }




}
