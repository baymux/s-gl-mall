package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void savaProductAttr(List<ProductAttrValueEntity> collect);

    List<ProductAttrValueEntity> baseAttrListforspu(Long spuId);
}

