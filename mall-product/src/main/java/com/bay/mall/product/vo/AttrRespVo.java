package com.bay.mall.product.vo;

import lombok.Data;

/**
 * @ClassName AttrRespVo
 * @Description TODO
 * @Author baymux
 * @Date 2020/5/31 11:33
 * @Vsrsion 1.0
 **/
@Data
public class AttrRespVo extends AttrVo {

    /*"catelogName": "手机/数码/手机", //所属分类名字
      "groupName": "主体", //所属分组名字*/

    private String catelogName;
    private String groupName;
    /**
     * 三级分类
     */
    private Long[] catelogPath;
}
