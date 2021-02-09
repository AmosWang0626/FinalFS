package com.amos.fs.minio.web;

import com.amos.fs.minio.service.MinioFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 模块名称: FinalFS
 * 模块描述: MinIO
 *
 * @author amos.wang
 * @date 2020/12/17 10:46
 */
@RestController
@RequestMapping("minio")
public class MinioController {

    @Resource
    private MinioFileService minioUploadFile;


    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传的文件不能为空";
        }

        return minioUploadFile.uploadFile(file);
    }

}
