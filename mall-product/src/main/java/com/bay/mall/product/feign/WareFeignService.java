package com.bay.mall.product.feign;

import com.bay.common.to.SkuHasStockTo;
import com.bay.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName WareFeignService
 * @Description 库存相关远程调用
 * @Author baymux
 * @Date 2020/6/21 22:10
 * @Vsrsion 1.0
 **/
@FeignClient("mall-ware")
public interface WareFeignService {

    /**
     * 1. 给R加上泛型
     * 2. 直接返回结果
     * 3. 自己封装解析结果
     *
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);


}
