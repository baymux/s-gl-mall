package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.CategoryEntity;
import com.bay.mall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查出所有分类以及子分类,以树形结构组装
     *
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 批量删除菜单
     * @param asList
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId的完整路径
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 级联更新所有数据
     * @param category
     */
    void updateCascade(CategoryEntity category);

    /**
     * 查询所有的一级分类
     * @return
     */
    List<CategoryEntity> getLevel1Categorys();

    /**
     * 查出所有的分类信息
     * @return
     */
    Map<String, List<Catelog2Vo>> getCatalogJson();


}

