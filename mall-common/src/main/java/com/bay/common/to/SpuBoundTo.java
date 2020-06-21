package com.bay.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName SpuBoundTo
 * @Description 远程调用传数据
 * @Author baymux
 * @Date 2020/6/1 21:58
 * @Vsrsion 1.0
 **/
@Data
public class SpuBoundTo {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;
}
