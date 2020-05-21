package com.bay.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:26:59
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

