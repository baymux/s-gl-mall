package com.bay.mall.order.dao;

import com.bay.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:26:59
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
