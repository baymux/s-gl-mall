package com.bay.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.bay.mall.product.entity.AttrEntity;
import com.bay.mall.product.service.AttrAttrgroupRelationService;
import com.bay.mall.product.service.AttrService;
import com.bay.mall.product.service.CategoryService;
import com.bay.mall.product.vo.AttrGroupRelationVo;
import com.bay.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bay.mall.product.entity.AttrGroupEntity;
import com.bay.mall.product.service.AttrGroupService;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.R;


/**
 * 属性分组
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 14:06:11
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 获取三级分类ID
     */
    @RequestMapping("/list/{catelogId}")
    public R listCatelogId(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {

        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }


    /**
     * 获取属性分组的关联的所有属性
     *
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntities = attrService.getRelation(attrgroupId);
        return R.ok().put("data", attrEntities);
    }


    /**
     *获取属性分组没有关联的其他属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noAttrRelation(@RequestParam Map<String, Object> params,
                            @PathVariable("attrgroupId") Long attrgroupId) {
        PageUtils page = attrService.getNoRelation(params,attrgroupId);
        return R.ok().put("page", page);
    }

    /**
     * 删除属性与分组的关联关系
     *
     * @param attrGroupRelationVos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] attrGroupRelationVos) {
        attrService.deleteRelation(attrGroupRelationVos);
        return R.ok();
    }


    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVos) {

        attrAttrgroupRelationService.saveBatch(attrGroupRelationVos);
        return R.ok();
    }

    @GetMapping("{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        // 1. 查出当前分类下的所有属性分组
        // 2. 查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data", attrGroupWithAttrsVos);
    }
}
