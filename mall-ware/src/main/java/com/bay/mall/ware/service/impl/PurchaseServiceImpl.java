package com.bay.mall.ware.service.impl;

import com.bay.common.constant.WareConstant;
import com.bay.mall.ware.entity.PurchaseDetailEntity;
import com.bay.mall.ware.service.PurchaseDetailService;
import com.bay.mall.ware.service.WareSkuService;
import com.bay.mall.ware.vo.MergeVo;
import com.bay.mall.ware.vo.PurchaseDoneVo;
import com.bay.mall.ware.vo.PurchaseItemDoneVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.Query;

import com.bay.mall.ware.dao.PurchaseDao;
import com.bay.mall.ware.entity.PurchaseEntity;
import com.bay.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;
    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0)
                        .or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }

        // TODO 确认采购单状态是0，1
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());

            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());

        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        // 1. 确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item ->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());

            return item;
        }).collect(Collectors.toList());
        // 2. 改变采购单的状态
        this.updateBatchById(collect);
        // 3. 改变采购项的状态
        collect.forEach(item ->{
            List<PurchaseDetailEntity> list = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> entities = list.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(entity.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(entities);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        // 改变采购但状态
        Long id = purchaseDoneVo.getId();
        // 改变采购项的状态
        boolean falg = true;
        List<PurchaseItemDoneVo> itemDoneVos = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> updateItems = new ArrayList<>();
        for (PurchaseItemDoneVo itemDoneVo : itemDoneVos) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (itemDoneVo.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                falg = false;
                purchaseDetailEntity.setStatus(itemDoneVo.getStatus());
            } else {
                // 将成功的采购进行入库
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                PurchaseDetailEntity byId = purchaseDetailService.getById(itemDoneVo.getItemId());


                wareSkuService.addStock(byId.getSkuId(), byId.getWareId(), byId.getSkuNum());

            }
            purchaseDetailEntity.setId(itemDoneVo.getItemId());

            updateItems.add(purchaseDetailEntity);
        }

        purchaseDetailService.updateBatchById(updateItems);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseDoneVo.setId(id);
        purchaseEntity.setStatus(falg ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());

        this.updateById(purchaseEntity);


    }

}