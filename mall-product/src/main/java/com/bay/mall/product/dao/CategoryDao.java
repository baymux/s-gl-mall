package com.bay.mall.product.dao;

import com.bay.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
