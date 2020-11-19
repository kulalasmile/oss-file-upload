package com.yvon.fileupdate.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * oss 自动配置
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OSSProperties.class})
@Import({MinioOSSConfiguration.class, AliyunOSSConfiguration.class})
public class OSSAutoConfiguration {
    public OSSAutoConfiguration() {
    }
}