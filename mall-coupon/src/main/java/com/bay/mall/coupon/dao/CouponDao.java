package com.bay.mall.coupon.dao;

import com.bay.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 22:52:20
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
