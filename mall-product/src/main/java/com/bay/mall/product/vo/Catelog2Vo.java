package com.bay.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName Catelog2Vo
 * @Description 二级分类Vo
 * @Author baymux
 * @Date 2020/6/22 16:47
 * @Vsrsion 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    /**
     * 一级父分类id
     */
    private String catalog1Id;

    /**
     * 三级分类
     */
    private List<Catalog3Vo> catalog3List;

    private String id;
    private String name;


    /**
     * 三级分类VO 内部类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catalog3Vo {
        private String catalog2Id;
        private String id;
        private String name;
    }


}
