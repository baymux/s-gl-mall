package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:31
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存Spu 描述图片信息
     * @param spuInfoDescEntity
     */
    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

