package com.bay.mall.search.service.ipml;

import cn.hutool.json.JSONUtil;
import com.bay.common.to.es.SkuEsModel;
import com.bay.mall.search.config.ElasticSearchConfig;
import com.bay.mall.search.constant.EsConstant;
import com.bay.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ProductSaveServiceImpl
 * @Description TODO
 * @Author baymux
 * @Date 2020/6/21 22:42
 * @Vsrsion 1.0
 **/
@Slf4j
@Service("ProductSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productUpStatus(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存es
        // 1. 给Es 建立所有 product, 建立好映射关系
        // 2. 给Es中保存数据
        // BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // 构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String skuJson = JSONUtil.toJsonStr(skuEsModel);
            indexRequest.source(skuJson, XContentType.JSON);


            bulkRequest.add(indexRequest);
        }
        // TODO 如果批量错误
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> item.getId()).collect(Collectors.toList());
        log.error("商品上架成功{}", collect);

        return b;
    }


}



