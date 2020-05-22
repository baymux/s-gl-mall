package com.bay.mall.member.feign;

import com.bay.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName CouponFeignService
 * @Description TODO
 * @Author baymux
 * @Date 2020/5/22 0:13
 * @Vsrsion 1.0
 **/
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
