package com.yvon.fileupdate.service.impl;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yvon.fileupdate.mapper.FileInfoMapper;
import com.yvon.fileupdate.oss.Storage;
import com.yvon.fileupdate.service.FileInfoService;
import com.yvon.fileupdate.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件信息 service impl
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-04
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService  {

    private static final String BUCKET_NAME = "kulalasmile";

    @Resource
    Storage storage;

    @Value("${oss.type}")
    private String storageType;

    @Override
    public FileInfo putFile(MultipartFile file, String relateId)  {
        FileInfo fileInfo = of(file, relateId);
        save(fileInfo);
        // 上传文件
        try {
            storage.putObject(fileInfo.getBucketName(), fileInfo.getFileName(), file.getInputStream(), fileInfo.getContentType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    @Override
    public List<FileInfo> putFiles(List<MultipartFile> files, String relateId) {
        List<FileInfo> fileInfos = new ArrayList<>();
        files.forEach(file -> {
            FileInfo fileInfo = of(file, relateId);
            save(fileInfo);
            // 上传文件
            try {
                storage.putObject(fileInfo.getBucketName(), fileInfo.getFileName(), file.getInputStream(), fileInfo.getContentType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileInfos.add(fileInfo);
        });
        return  fileInfos;
    }



    @Override
    public Set<String> getUrlByRelateId(String relateId) {
        Set<String> urls = new HashSet<>();
        List<FileInfo> fileInfos = getByRelateId(relateId);
        fileInfos.forEach(fileInfo -> {
            try {
                String objectUrl = storage.getObjectUrl(fileInfo.getBucketName(), fileInfo.getFileName());
                urls.add(objectUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return urls;
    }

    @Override
    public InputStream getFileStream(FileInfo fileInfo) throws Exception {
        return storage.getObject(fileInfo.getBucketName(), fileInfo.getFileName());
    }

    @Override
    public List<FileInfo> findFileInfoByRelateId(String relateId) {
        return lambdaQuery()
                .eq(FileInfo::getRelateId, relateId)
                .list();
    }

    @Override
    public FileInfo getFileInfoById(String id) {
        return lambdaQuery()
                .eq(FileInfo::getId, id)
                .eq(FileInfo::getStorage, storageType)
                .one();
    }

    @Override
    public boolean removeById(Serializable id) {
        FileInfo fileInfo = baseMapper.selectById(id);
        baseMapper.deleteById(id);
        try {
            storage.removeObject(BUCKET_NAME.toLowerCase(), fileInfo.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        this.baseMapper.deleteBatchIds(idList);
        List<FileInfo> fileInfos = baseMapper.selectBatchIds(idList);
        try {
            storage.removeObjects(BUCKET_NAME.toLowerCase(), fileInfos.stream().map(FileInfo::getFileName).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean removeByRelateIds(List<String> relateId) {
        List<FileInfo> fileInfoList = lambdaQuery().
                in(FileInfo::getRelateId, relateId)
                .list();
        List<String> idList = fileInfoList.stream().map(FileInfo::getFileName).collect(Collectors.toList());
        this.baseMapper.deleteBatchIds(idList);
        try {
            storage.removeObjects(BUCKET_NAME.toLowerCase(), idList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getUrlById(String id) {
        FileInfo fileInfo = baseMapper.selectById(id);
        try {
            return storage.getObjectUrl(fileInfo.getBucketName(), fileInfo.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建文件信息
     *
     * @param file     文件
     * @param relateId 关联编号
     * @return 文件信息
     * @author : Yvon / 2020-02-17
     */
    private FileInfo of(MultipartFile file, String relateId) {
        final String oriFileName = file.getOriginalFilename();



        String objectName = IdUtil.simpleUUID().substring(1,21);
        String fileName = objectName;
        String fileSuffix = oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
        if (!Strings.isNullOrEmpty(fileSuffix)) {
            fileName = objectName.concat(String.valueOf(CharUtil.DOT)).concat(fileSuffix);
        }

        return FileInfo.builder()
                .bucketName(BUCKET_NAME.toLowerCase())
                .fileName(fileName)
                .fileOriName(oriFileName)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .relateId(relateId)
                .storage(storageType)
                .build();
    }


    /**
     * 通过关联编号查询
     *
     * @param relateId 关联编号
     * @return : List<FileInfo>
     * @author : Yvon / 2020-11-04
     */
    private List<FileInfo> getByRelateId(String relateId) {
        return lambdaQuery().eq(FileInfo::getRelateId, relateId)
                .eq(FileInfo::getStorage, storageType)
                .list();
    }

}
