package com.yvon.fileupdate.autoconfigure;

import com.yvon.fileupdate.oss.MinioStorage;
import com.yvon.fileupdate.oss.Storage;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio oss配置类
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MinioClient.class})
public class MinioOSSConfiguration {

    MinioOSSConfiguration() {
    }

    @Bean
    @ConditionalOnProperty(prefix = "oss", name = {"type"}, havingValue = "minio")
    Storage minioStorage(OSSProperties ossProperties) throws InvalidPortException, InvalidEndpointException {
        OSSProperties.Minio minio = ossProperties.getMinio();

        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(minio.getEndpoint())
                        .credentials(minio.getAccessKey(), minio.getSecretKey())
                        .build();
        return new MinioStorage(minioClient);
    }
}
