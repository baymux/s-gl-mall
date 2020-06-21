package com.bay.mall.product.feign;

import com.bay.common.to.SkuReductionTo;
import com.bay.common.to.SpuBoundTo;
import com.bay.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName CouponFeignService
 * @Description
 * @Author baymux
 * @Date 2020/6/1 21:53
 * @Vsrsion 1.0
 **/
@FeignClient("mall-coupon")
public interface CouponFeignService {

    /**
     * 1. CouponFeignService.saveSpuBounds(spuBoundTo)
     *     1.1 @RequestBody 将这个对象转为json
     *     1.2 给mall-coupon的/coupon/spubounds/save发送请求，
     *     将上一步转的对象放在请求体的位置
     *     1.3 对方服务收到请求，请求体里的json数据
     *     json兼容
     * @param spuBoundTo
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    /**
     * 保存skuReduction
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}


