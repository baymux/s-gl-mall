package com.bay.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.member.entity.MemberReceiveAddressEntity;

import java.util.Map;

/**
 * 会员收货地址
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:22:37
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

