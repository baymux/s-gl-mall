package com.bay.common.to;

import lombok.Data;

/**
 * @ClassName SkuHasStockVo
 * @Description 是否有库存To
 * @Author baymux
 * @Date 2020/6/21 21:59
 * @Vsrsion 1.0
 **/
@Data
public class SkuHasStockTo {

    private Long skuId;
    private Boolean hasStock;

}
