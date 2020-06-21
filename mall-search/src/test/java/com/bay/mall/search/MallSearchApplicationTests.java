package com.bay.mall.search;

import cn.hutool.json.JSONUtil;
import com.bay.mall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class MallSearchApplicationTests {

    @Resource
    private RestHighLevelClient client;

    @Test
    public void searchData() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 请求DSL
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        // 按照年龄值分布聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);

        ageAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");

        searchSourceBuilder
                .aggregation(ageAgg)
                .aggregation(balanceAvg);

        System.out.println(searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(search.toString());
    }


    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        //indexRequest.source("username","zhangsan", "age", 18, "gender","M");
        User user = new User();
        user.setUserName("zhangsan");
        user.setGender("男");
        user.setAge(18);
        String jsonStr = JSONUtil.toJsonStr(user);

        indexRequest.source(jsonStr, XContentType.JSON);

        // 执行操作
        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(index);
    }


    @Test
    public void contextLoads() {
        System.out.println(client);
    }


    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }

}
