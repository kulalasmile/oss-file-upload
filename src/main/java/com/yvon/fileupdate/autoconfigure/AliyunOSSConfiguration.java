package com.yvon.fileupdate.autoconfigure;

import com.aliyun.oss.OSSClient;
import com.yvon.fileupdate.oss.AliyunStorage;
import com.yvon.fileupdate.oss.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * aliyun oss配置类
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({OSSClient.class})
public class AliyunOSSConfiguration {

    AliyunOSSConfiguration() {
    }

    @Bean
    @ConditionalOnProperty(prefix = "oss", name = {"type"}, havingValue = "aliyun")
    Storage aliyunStorage(OSSProperties ossProperties) {
        OSSProperties.Aliyun aliyun = ossProperties.getAliyun();
        OSSClient ossClient = new OSSClient(aliyun.getEndpoint(), aliyun.getAccessKeyId(), aliyun.getAccessKeySecret());
        return new AliyunStorage(ossClient);
    }
}
