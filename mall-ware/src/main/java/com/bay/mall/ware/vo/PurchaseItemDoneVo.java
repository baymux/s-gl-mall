package com.bay.mall.ware.vo;

import lombok.Data;

/**
 * @ClassName PurchaseItemDoneVo
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/7 15:44
 * @Vsrsion 1.0
 **/
@Data
public class PurchaseItemDoneVo {
    //itemId:1,status:4,reason:""
    private Long itemId;
    private Integer status;
    private String reason;
}
