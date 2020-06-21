package com.bay.mall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class MallThirdPartyApplicationTests {

    @Resource
    private OSSClient ossClient;

    @Test
    void contextLoads() {
    }



    @Test
    public void testUpload() throws FileNotFoundException {
        //// Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "oss-cn-beijing.aliyuncs.com";
        //// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        //String accessKeyId = "LTAI4G1ZqsLSx7oBwZFkLLxS";
        //String accessKeySecret = "7SrS6Aa3UeebdLMaWynjWXwvYD1rto";

        //// 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("E:\\照片\\北京\\IMG_20190420_140739.jpg");
        ossClient.putObject("mall-obj", "IMG_20190420_140739.jpg.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        System.out.println("上传成功");

    }

}
