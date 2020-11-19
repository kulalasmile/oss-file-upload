package com.yvon.fileupdate.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 文件信息
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_info")
@TableName("file_info")
public class FileInfo implements Serializable {


    @Id
    @Column(length = 36)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件位置
     */
    private String bucketName;


    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原文件名
     */
    private String fileOriName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 关联编号
     */
    private String relateId;

    /**
     * 存储方式
     */
    private String storage;

}
