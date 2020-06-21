package com.bay.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bay.common.constant.ProductConstant;
import com.bay.common.to.SkuHasStockTo;
import com.bay.common.to.SkuReductionTo;
import com.bay.common.to.SpuBoundTo;
import com.bay.common.to.es.SkuEsModel;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.Query;
import com.bay.common.utils.R;
import com.bay.mall.product.dao.SpuInfoDao;
import com.bay.mall.product.entity.*;
import com.bay.mall.product.feign.CouponFeignService;
import com.bay.mall.product.feign.SearchFeignService;
import com.bay.mall.product.feign.WareFeignService;
import com.bay.mall.product.service.*;
import com.bay.mall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1保存Spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtil.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        log.info("保存Spu基本信息 success");
        // 2 保存spu描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        log.info("保存spu描述图片 success");
        // 3 保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        log.info("保存spu的图片集 success");
        // 4 保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(id.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());

            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.savaProductAttr(collect);
        log.info("保存spu的规格参数 success");
        // 5积分信息 sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtil.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());

        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu优惠信息失败");
        }

        // 6 保存spu对应的sku信息
        List<Skus> skus = spuSaveVo.getSkus();

        if (skus != null && skus.size() > 0) {
            skus.forEach(sku -> {
                String defaultImg = "";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                //sku基本信息 pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtil.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                log.info("sku基本信息 success");

                Long skuId = skuInfoEntity.getSkuId();

                //sku图片信息 pms_sku_images
                List<SkuImagesEntity> imagesEntities = sku.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());

                    return skuImagesEntity;
                }).filter(skuImagesEntity ->
                        !StringUtils.isEmpty(skuImagesEntity.getImgUrl())
                ).collect(Collectors.toList());

                // TODO 没有图片路径的无需保存
                skuImagesService.saveBatch(imagesEntities);
                log.info("sku图片信息 success");
                //sku销售属性 pms_sku_sale_attr_value
                List<Attr> attr = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(item -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtil.copyProperties(item, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());

                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                log.info("sku销售属性 success");

                // sms_sku_ladder，sms_sku_full_reduction
                SkuReductionTo skuReductionTo = new SkuReductionTo();

                BeanUtil.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() <= 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });


        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByConditton(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> spuInfoEntityQueryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String status = (String) params.get("status");
        //TODO 页面参数写错了  catelogId
        String catalogId = (String) params.get("catelogId");

        if (!StringUtils.isEmpty(key)) {
            spuInfoEntityQueryWrapper.and(wrapper -> {
                wrapper.eq("id", key).or().like("spu_name", key);
            });
        }

        if (!StringUtils.isEmpty(status)) {
            spuInfoEntityQueryWrapper.eq("publish_status", status);
        }

        if (!StringUtils.isEmpty(brandId)) {
            spuInfoEntityQueryWrapper.eq("brand_id", brandId);
        }

        if (!StringUtils.isEmpty(catalogId)) {
            spuInfoEntityQueryWrapper.eq("catalog_id", catalogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                spuInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        // 1 查出当前spuId对应的所有sku信息
        List<SkuInfoEntity> skus = skuInfoService.getSkuBySpuId(spuId);
        // 获取sku的所有id集合
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 4.查询sku所有可以用来检索的规格属性
        List<ProductAttrValueEntity> attrValues = productAttrValueService.baseAttrListforspu(spuId);
        List<Long> attrIds = attrValues.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());
        // 查询可用来检索的属性
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = attrValues.stream()
                .filter(item -> idSet.contains(item.getAttrId()))
                .map(item -> {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    BeanUtil.copyProperties(item, attrs);
                    return attrs;
                })
                .collect(Collectors.toList());

        //  1.远程调研库存服务，是否有库存
        Map<Long, Boolean> skuHasStockMap = null;
        try {
            R skuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            TypeReference<List<SkuHasStockTo>> typeReference = new TypeReference<List<SkuHasStockTo>>() {

            };
            skuHasStockMap = skuHasStock.getData(typeReference).stream()
                    .collect(Collectors.toMap(SkuHasStockTo::getSkuId, item -> item.getHasStock()));
        } catch (Exception e) {
            log.error("库存服务查询原因, {}", e);
        }

        // 1.2 封装每个sku的信息
        Map<Long, Boolean> finalSkuHasStockMap = skuHasStockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            // 组装需要的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            // 属性对拷
            BeanUtil.copyProperties(sku, skuEsModel);
            // skuPrice, skuImg,hasStock,hotScore
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            if (finalSkuHasStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalSkuHasStockMap.get(sku.getSkuId()));
            }
            // TODO 2.热度评分，默认0， 可扩展
            skuEsModel.setHotScore(0L);

            //  3.查询品牌和分类名称
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());

            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());

            skuEsModel.setCatalogName(categoryEntity.getName());

            // 设置检索属性
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());

        // TODO 5.将数据发送es保存， 远程检索服务
        R r = searchFeignService.productUpStatus(upProducts);
        if (r.getCode() == 0) {
            // 远程调用成功
            // TODO 6 修改商品spu状态
            baseMapper.updataSpuStatus(spuId, ProductConstant.StatusrEnum.UP_SPU.getCode());
        } else {
            //远程调用失败
            // TODO 7.接口幂等性: 重试机制
        }

    }


}