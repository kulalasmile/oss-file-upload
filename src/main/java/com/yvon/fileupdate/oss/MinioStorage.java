package com.yvon.fileupdate.oss;

import io.minio.*;
import io.minio.messages.DeleteObject;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * minio 存储
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
public class MinioStorage implements Storage {
    private final MinioClient minioClient;

    public MinioStorage(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return  minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        return this.minioClient.getObjectUrl(bucketName, objectName);
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @Override
    public void removeObjects(String bucketName, List<String> objectNames) throws Exception {
        List<DeleteObject> objects = new LinkedList<>();
        objectNames.forEach(objectName -> objects.add(new DeleteObject(objectName)));
        minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(objects)
                        .build());
    }
}