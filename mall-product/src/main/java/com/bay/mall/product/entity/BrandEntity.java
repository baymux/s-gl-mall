package com.bay.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bay.common.valid.AddGroup;
import com.bay.common.valid.ListValue;
import com.bay.common.valid.UpdateGroup;
import lombok.Data;

import org.hibernate.validator.constraints.URL;


import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改必须指定id", groups = UpdateGroup.class)
    @Null(message = "新增不能指定id", groups = AddGroup.class)
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空",groups = {AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(groups = AddGroup.class)
    @URL(message = "logo必须是合法的url地址", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ListValue(values = {0,1}, groups = AddGroup.class)
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(groups = AddGroup.class)
    @Pattern(regexp = "^[a-zA-z]$", message = "检索首字母必须是字母", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = AddGroup.class)
    @Min(value = 0, message = "排序必须大于0",groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
