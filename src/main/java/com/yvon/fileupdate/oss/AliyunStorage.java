package com.yvon.fileupdate.oss;

import cn.hutool.core.util.CharUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.InputStream;
import java.util.List;

/**
 * aliyun 存储
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
public class AliyunStorage implements Storage {
    private final OSSClient ossClient;

    public AliyunStorage(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream inputStream, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        this.ossClient.putObject(bucketName, objectName, inputStream, objectMetadata);
    }



    @Override
    public InputStream getObject(String bucketName, String objectName) {
        return this.ossClient.getObject(bucketName, objectName).getObjectContent();
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        return "https://".concat(bucketName).concat(String.valueOf(CharUtil.DOT)).concat(this.ossClient.getEndpoint().getHost()).concat(String.valueOf(CharUtil.SLASH)).concat(objectName);
    }

    @Override
    public void removeObject(String bucketName, String objectName) {
        this.ossClient.deleteObject(bucketName, objectName);
    }

    @Override
    public void removeObjects(String bucketName, List<String> objectNames) throws Exception {

    }
}