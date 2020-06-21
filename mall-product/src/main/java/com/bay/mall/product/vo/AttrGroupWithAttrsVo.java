package com.bay.mall.product.vo;

import com.bay.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName AttrGroupWithAttrsVo
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/1 0:15
 * @Vsrsion 1.0
 **/
@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;


    private List<AttrEntity> attrs;

}
