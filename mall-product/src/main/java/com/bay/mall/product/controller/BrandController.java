package com.bay.mall.product.controller;

import com.bay.common.utils.PageUtils;
import com.bay.common.utils.R;
import com.bay.common.valid.AddGroup;
import com.bay.common.valid.UpdateGroup;
import com.bay.mall.product.entity.BrandEntity;
import com.bay.mall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 品牌
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 14:06:11
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     *
     * @Valid 校验注解开启
     */
    @RequestMapping("/save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand/*, BindingResult result*/) {
        /*if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>(16);
            // 获取校验的错误结果
            result.getFieldErrors().forEach((item) ->{
                String message = item.getDefaultMessage();
                String field = item.getField();
                errorMap.put(field, message);
            });
            return R.error("提交的数据不合法").put("data", errorMap);
        } else {

        }
        */
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
