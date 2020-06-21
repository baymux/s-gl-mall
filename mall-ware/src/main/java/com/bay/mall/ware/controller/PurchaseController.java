package com.bay.mall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.bay.mall.ware.vo.MergeVo;
import com.bay.mall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bay.mall.ware.entity.PurchaseEntity;
import com.bay.mall.ware.service.PurchaseService;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.R;


/**
 * 采购信息
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:34:41
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * /ware/purchase/unreceive/list
     *
     * @param params
     * @return
     */
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     * /ware/purchase/merge
     *
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R mergePurchase(@RequestBody MergeVo mergeVo) {

        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 完成采购单
     *
     * @param mergeVo
     * @return
     */
    @PostMapping("/done")
    public R purchaseDone(@RequestBody PurchaseDoneVo purchaseDoneVo) {

        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }

    /**
     * 领取采购单
     * /ware/purchase/received
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
