package com.bay.mall.search.controller;

import com.bay.common.exception.ExCodeEnum;
import com.bay.common.to.es.SkuEsModel;
import com.bay.common.utils.R;
import com.bay.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName EsSaveController
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/21 22:37
 * @Vsrsion 1.0
 **/
@RestController
@RequestMapping("/search/save")
@Slf4j
public class EsSaveController {

    @Resource
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productUpStatus(@RequestBody List<SkuEsModel> skuEsModels) {

        boolean b = false;
        try {
            b = productSaveService.productUpStatus(skuEsModels);
        } catch (IOException e) {
            log.error("商品上架错误， {}", e);
            return R.error(ExCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), ExCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (!b) {
            return R.ok();
        } else {
            log.error("商品上架错误");
            return R.error(ExCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), ExCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
