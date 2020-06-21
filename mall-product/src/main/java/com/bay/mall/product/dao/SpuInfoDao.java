package com.bay.mall.product.dao;

import com.bay.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:31
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updataSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
