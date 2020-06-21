package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.BrandEntity;
import com.bay.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存分类详细信息
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新品牌名
     * @param brandId
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * 更新分类名
     * @param catId
     * @param name
     */
    void updateCategory(Long catId, String name);

    /**
     * 查询指定信息的品牌信息
     * @param catId
     * @return
     */
    List<BrandEntity> getBrandsByCatId(Long catId);
}

