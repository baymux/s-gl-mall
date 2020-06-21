package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spu的图片集
     * @param id
     * @param images
     */
    void saveImages(Long id, List<String> images);
}

