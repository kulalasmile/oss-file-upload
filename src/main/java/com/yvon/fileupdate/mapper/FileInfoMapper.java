package com.yvon.fileupdate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yvon.fileupdate.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件信息 mapper
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-04
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
}
