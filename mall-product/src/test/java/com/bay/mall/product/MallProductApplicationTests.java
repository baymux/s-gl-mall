package com.bay.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bay.mall.product.entity.BrandEntity;
import com.bay.mall.product.service.BrandService;
import com.bay.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryService categoryService;


    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));

    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setName("haha");
        //brandService.save(brandEntity);
       /* brandEntity.setBrandId(1L);
        brandEntity.setName("华为");
        brandEntity.setDescript("修改");
        brandService.updateById(brandEntity);
        System.out.println("success");*/

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));

        list.forEach(item -> {
            System.out.println(item);
        });
    }




}
