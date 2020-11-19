package com.yvon.fileupdate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yvon.fileupdate.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * 文件信息 service
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-04
 */

public interface FileInfoService extends IService<FileInfo> {

    /**
     * 上传文件
     *
     * @param file     文件
     * @param relateId 关联编号
     * @return : FileInfo
     * @author : Yvon / 2020-11-03
     */
    FileInfo putFile(MultipartFile file, String relateId) ;

    /**
     * 通过关联编号查询url
     *
     * @param relateId 关联编号
     * @return : Set<String>
     * @author : Yvon / 2020-11-04
     */
    Set<String> getUrlByRelateId(String relateId);

    /**
     * 查询文件流
     *
     * @param fileInfo 文件信息
     * @return : InputStream
     * @author : Yvon / 2020-11-04
     */
    InputStream getFileStream(FileInfo fileInfo) throws Exception;

    /**
     * 通过关联编号查询文件信息
     *
     * @param relateId 关联编号
     * @return : List<FileInfo>
     * @author : Yvon / 2020-11-05
     */
    List<FileInfo> findFileInfoByRelateId(String relateId);

    /**
     * 通过编号查询文件信息(下载使用)
     *
     * @param id 编号
     * @return : FileInfo
     * @author : Yvon / 2020-11-04
     */
    FileInfo getFileInfoById(String id);

    /**
     * 批量上传文件
     *
     * @param files    文件
     * @param relateId 关联编号
     * @return : List<FileInfo>
     * @author : Yvon / 2020-11-05
     */
    List<FileInfo> putFiles(List<MultipartFile> files, String relateId);

    /**
     * 通过关联编号删除
     *
     * @param relateId 关联编号
     * @return : boolean
     * @author : Yvon / 2020-11-05
     */
    boolean removeByRelateIds(List<String> relateId);

    /**
     * 通过编号查询url
     *
     * @param id 编号
     * @return : String
     * @author : Yvon / 2020-11-17
     */
    String getUrlById(String id);
}
