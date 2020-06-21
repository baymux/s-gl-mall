package com.bay.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.ware.entity.PurchaseEntity;
import com.bay.mall.ware.vo.MergeVo;
import com.bay.mall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 23:34:41
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo purchaseDoneVo);
}

