package com.yvon.fileupdate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件信息响应参数
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-04
 */
@Data
@ApiModel("文件信息响应参数")
public class FileInfoDTO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id")
    private String id;

    /**
     * 文件位置
     */
    @ApiModelProperty(name = "bucketName", value = "文件位置")
    private String bucketName;


    /**
     * 文件名
     */
    @ApiModelProperty(name = "fileName", value = "文件名")
    private String fileName;

    /**
     * 原文件名
     */
    @ApiModelProperty(name = "fileOriName", value = "原文件名")
    private String fileOriName;

    /**
     * 文件大小
     */
    @ApiModelProperty(name = "fileSize", value = "文件大小")
    private Long fileSize;

    /**
     * 文件类型
     */
    @ApiModelProperty(name = "contentType", value = "文件类型")
    private String contentType;

    /**
     * 关联编号
     */
    @ApiModelProperty(name = "relateId", value = "关联编号")
    private String relateId;

    /**
     * 存储方式
     */
    @ApiModelProperty(name = "storage", value = "存储方式")
    private String storage;
}
