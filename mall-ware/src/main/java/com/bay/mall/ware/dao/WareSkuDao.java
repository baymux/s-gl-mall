package com.bay.mall.ware.dao;

import com.bay.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:34:40
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {


    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /**
     * 查询库存
     * @param skuId
     * @return
     */
    Long getSkuStock(@Param("skuId") Long skuId);
}
