package com.bay.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SkuReductionTo
 * @Description
 * @Author baymux
 * @Date 2020/6/1 22:12
 * @Vsrsion 1.0
 **/
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
