package com.bay.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bay.common.utils.PageUtils;
import com.bay.mall.product.entity.AttrEntity;
import com.bay.mall.product.vo.AttrGroupRelationVo;
import com.bay.mall.product.vo.AttrRespVo;
import com.bay.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author baymux
 * @email baymux@gmail.com
 * @date 2020-05-21 13:27:32
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 级联保存属性信息
     *
     * @param attr
     */
    void saveAttr(AttrVo attr);

    /**
     * 查询分类属性
     *  @param params
     * @param catelogId
     * @param attrType
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    /**
     * 级联修改
     * @param attr
     */
    void updateAttr(AttrRespVo attr);

    /**
     * 获取属性分组的关联的所有属性
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getRelation(Long attrgroupId);

    /**
     * 删除属性与分组的关联关系
     * @param attrGroupRelationVos
     */
    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);

    PageUtils getNoRelation(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的集合中找出可用来检索的attrIds
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

