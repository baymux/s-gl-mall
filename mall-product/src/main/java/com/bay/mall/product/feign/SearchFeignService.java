package com.bay.mall.product.feign;

import com.bay.common.to.es.SkuEsModel;
import com.bay.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName SearchFeignService
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/21 23:09
 * @Vsrsion 1.0
 **/
@FeignClient("mall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productUpStatus(@RequestBody List<SkuEsModel> skuEsModels);
}
