package com.yvon.fileupdate.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * oss 属性
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {

    private OSSProperties.Aliyun aliyun;
    private OSSProperties.Minio minio;

    public OSSProperties.Aliyun getAliyun() {
        return this.aliyun;
    }

    public OSSProperties.Minio getMinio() {
        return this.minio;
    }

    public void setAliyun(OSSProperties.Aliyun aliyun) {
        this.aliyun = aliyun;
    }

    public void setMinio(OSSProperties.Minio minio) {
        this.minio = minio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode()
    public static class Aliyun {
        private boolean active;
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode()
    public static class Minio {
        private boolean active;
        private String endpoint;
        private String accessKey;
        private String secretKey;
    }


}
