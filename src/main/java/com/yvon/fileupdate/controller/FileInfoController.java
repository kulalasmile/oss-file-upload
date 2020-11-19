package com.yvon.fileupdate.controller;

import cn.hutool.core.lang.Assert;
import com.yvon.fileupdate.dto.FileInfoDTO;
import com.yvon.fileupdate.service.FileInfoService;
import com.yvon.fileupdate.entity.FileInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 文件信息 controller
 *
 * @author : Yvon
 * @version : 1.0
 * @since : 2020-11-03
 */
@RestController
@RequestMapping(value = "/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "文件管理", tags = "文件服务 - 文件管理API")
public class FileInfoController {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 上传单个文件
     *
     * @param file     文件
     * @param relateId 关联编号
     * @return : ResponseEntity<FileInfoDTO>
     * @author : Yvon / 2020-11-04
     */
    @PostMapping(value = "/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传单个文件", response = FileInfoDTO.class,
            notes = "#### 上传单个文件\n" +
                    "1. 文件信息(bucketName，存储方式...)保存在数据库\n" +
                    "2. 以文件信息的id作为objectName(OSS存储时的文件名)上传文件")
    public ResponseEntity<FileInfoDTO> add(@RequestPart("file") MultipartFile file, @RequestParam(value = "relateId", required = false) String relateId){
        checkFile(file);
        FileInfo fileInfo = fileInfoService.putFile(file, relateId);
        return ResponseEntity.ok(entityToDTO(fileInfo));
    }

    /**
     * 上传多个文件
     *
     * @param files    文件
     * @param relateId 关联编号
     * @return : ResponseEntity<List<FileInfoDTO>>
     * @author : Yvon / 2020-11-04
     */
    @PostMapping(value = "/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传多个文件", notes = "上传多个文件", response = FileInfoDTO.class)
    public ResponseEntity<List<FileInfoDTO>> add(@RequestParam("files") List<MultipartFile> files, @RequestParam(value = "relateId", required = false) String relateId){
        files.forEach(this::checkFile);
        List<FileInfo> fileInfos = fileInfoService.putFiles(files, relateId);
        return ResponseEntity.ok((entityToDTO(fileInfos)));
    }


    /**
     * 根据关联编号查询url
     *
     * @param relateId 关联编号
     * @return : ResponseEntity<Set<String>>
     * @author : Yvon / 2020-11-04
     */
    @GetMapping(value = "/url/relateId/{relateId}")
    @ApiOperation(value = "根据关联编号查询url", notes = "根据关联编号查询url API", response = Set.class)
    public ResponseEntity<Set<String>> getUrlByRelateId(@PathVariable(value = "relateId") String relateId) {
        return ResponseEntity.ok(fileInfoService.getUrlByRelateId(relateId));
    }

    /**
     * 根据编号查询url
     *
     * @param id 编号
     * @return : ResponseEntity<Set<String>>
     * @author : Yvon / 2020-11-04
     */
    @GetMapping(value = "/url/id/{id}")
    @ApiOperation(value = "根据编号查询url", notes = "根据编号查询url API", response = String.class)
    public ResponseEntity<String> getUrlById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(fileInfoService.getUrlById(id));
    }

    /**
     * 根据文件编号下载
     *
     * @param id       编号
     * @param response 响应
     * @author : Yvon / 2020-11-04
     */
    @GetMapping(value = "/download/{id}")
    @ApiOperation(value = "根据文件编号下载", notes = "根据文件编号下载")
    public void download(@PathVariable("id") String id, HttpServletResponse response){
        FileInfo fileInfo = fileInfoService.getFileInfoById(id);
        Assert.notNull(fileInfo, "文件信息不存在或存储方式错误!");
        try(final InputStream inputStream = fileInfoService.getFileStream(fileInfo)) {
            response.setContentType(fileInfo.getContentType());
            response.setContentLengthLong(fileInfo.getFileSize());
            response.setHeader("Content-Disposition", "attachment; filename=".concat(fileInfo.getFileOriName()));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过关联编号查询文件信息
     *
     * @param relateId 关联编号
     * @return : ResponseEntity<List<FileInfoDTO>>
     * @author : Yvon / 2020-11-04
     */
    @GetMapping(value = "/relateId/{relateId}")
    @ApiOperation(value = "通过关联编号查询文件信息", notes = "通过关联编号查询文件信息API", response = FileInfoDTO.class)
    public ResponseEntity<List<FileInfoDTO>> findFileInfoByRelateId(@PathVariable("relateId") String relateId){
        List<FileInfo> fileInfos = fileInfoService.findFileInfoByRelateId(relateId);
        return ResponseEntity.ok((entityToDTO(fileInfos)));
    }

    /**
     * 通过编号文件信息
     *
     * @param id 编号
     * @return : ResponseEntity<FileInfoDTO>
     * @author : Yvon / 2020-11-04
     */
    @GetMapping(value = "/id/{id}")
    @ApiOperation(value = "通过编号文件信息", notes = "通过编号文件信息API", response = FileInfoDTO.class)
    public ResponseEntity<FileInfoDTO> getById(@PathVariable("id") String id){
        FileInfo fileInfo = fileInfoService.getById(id);
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(fileInfo, fileInfoDTO);
        return ResponseEntity.ok((fileInfoDTO));
    }

    /**
     * 删除文件
     *
     * @param id id
     * @return : ResponseData<Boolean>
     * @author : Yvon / 2020-11-04
     */
    @DeleteMapping("/id/{id}")
    @ApiOperation(value = "删除文件", response = Boolean.class,
            notes = "删除文件")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") String id){
        return ResponseEntity.ok(fileInfoService.removeById(id));
    }

    /**
     * 批量删除文件
     *
     * @param ids id
     * @return : ResponseData<Boolean>
     * @author : Yvon / 2020-11-04
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除文件", response = Boolean.class, notes = "批量删除文件")
    public ResponseEntity<Boolean> deleteByIds(@RequestBody List<String> ids){
        return ResponseEntity.ok(fileInfoService.removeByIds(ids));
    }

    /**
     * 通过关联编号删除文件
     *
     * @param relateId id
     * @return : ResponseData<Boolean>
     * @author : Yvon / 2020-11-04
     */
    @DeleteMapping("/relateIds")
    @ApiOperation(value = "通过关联编号删除文件", notes = "通过关联编号删除文件API", response = Boolean.class)
    public ResponseEntity<Boolean> deleteByRelateIds(@RequestBody List<String> relateId){
        return ResponseEntity.ok(fileInfoService.removeByRelateIds(relateId));
    }



    /**
     * 检查文件
     *
     * @param file 文件
     * @author : Yvon / 2020-11-04
     */
    private void checkFile(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空!");
        }
    }

    private List<FileInfoDTO> entityToDTO(List<FileInfo> fileInfoList) {
        List<FileInfoDTO> fileInfoDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileInfoList)) {
            fileInfoList.forEach(fileInfo -> {
                FileInfoDTO fileInfoDTO = entityToDTO(fileInfo);
                fileInfoDTOList.add(fileInfoDTO);
            });
        }
        return fileInfoDTOList;
    }

    private FileInfoDTO entityToDTO(FileInfo fileInfo) {
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        if (Objects.nonNull(fileInfo)) {
            BeanUtils.copyProperties(fileInfo, fileInfoDTO);
        }
        return fileInfoDTO;
    }
}
