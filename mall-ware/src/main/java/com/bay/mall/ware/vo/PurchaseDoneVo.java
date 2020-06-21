package com.bay.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PurchaseDoneVo
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/7 15:43
 * @Vsrsion 1.0
 **/
@Data
public class PurchaseDoneVo {

    private Long id;

    private List<PurchaseItemDoneVo> items;
}
