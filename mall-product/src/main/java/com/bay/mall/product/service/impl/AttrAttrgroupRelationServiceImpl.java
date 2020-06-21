package com.bay.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bay.common.utils.PageUtils;
import com.bay.common.utils.Query;
import com.bay.mall.product.vo.AttrGroupRelationVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bay.mall.product.dao.AttrAttrgroupRelationDao;
import com.bay.mall.product.entity.AttrAttrgroupRelationEntity;
import com.bay.mall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeBatchRelation(List<AttrAttrgroupRelationEntity> relationEntities) {
        baseMapper.deleteBatchRelation(relationEntities);
    }

    @Override
    public void saveBatch(List<AttrGroupRelationVo> attrGroupRelationVos) {
        List<AttrAttrgroupRelationEntity> collect = attrGroupRelationVos.stream().map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtil.copyProperties(item, attrAttrgroupRelationEntity);

            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());

        saveBatch(collect);
    }

}