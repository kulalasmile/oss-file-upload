package com.yvon.fileupdate.oss;

import java.io.InputStream;
import java.util.List;

/**
 * 存储
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
public interface Storage {
    /**
     * 存对象
     *
     * @param bucketName  bucket名称
     * @param objectName  对象名称
     * @param inputStream 输入流
     * @param contentType 内容类型
     * @author : Yvon / 2020-11-04
     */
    void putObject(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception;

    /**
     * 查询对象
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return : InputStream
     * @author : Yvon / 2020-11-03
     */
    InputStream getObject(String bucketName, String objectName) throws Exception;

    /**
     * 查询对象url
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return : String
     * @author : Yvon / 2020-11-03
     */
    String getObjectUrl(String bucketName, String objectName) throws Exception;

    /**
     * 删除对象
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @author : Yvon / 2020-11-03
     */
    void removeObject(String bucketName, String objectName) throws Exception;

    /**
     * 批量删除对象
     *
     * @param bucketName  桶名称
     * @param objectNames 对象名称
     * @author : Yvon / 2020-11-04
     */
    void removeObjects(String bucketName, List<String> objectNames) throws Exception;
}
