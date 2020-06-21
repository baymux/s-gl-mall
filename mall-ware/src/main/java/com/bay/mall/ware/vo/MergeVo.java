package com.bay.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MergeVo
 * @Description 合并VO
 * @Author baymux
 * @Date 2020/6/2 21:51
 * @Vsrsion 1.0
 **/
@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;
}
