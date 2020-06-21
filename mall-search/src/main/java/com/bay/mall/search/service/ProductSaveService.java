package com.bay.mall.search.service;

import com.bay.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ProductSaveService
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/21 22:41
 * @Vsrsion 1.0
 **/
public interface ProductSaveService {

    boolean productUpStatus(List<SkuEsModel> skuEsModels) throws IOException;
}
