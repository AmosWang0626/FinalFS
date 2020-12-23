package com.amos.fs.minio.web;

import com.amos.fs.minio.config.MinioProperties;
import com.amos.fs.minio.service.UploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
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
    private MinioProperties minioProperties;
    @Resource
    private UploadFileService minioUploadFile;


    @GetMapping
    public String minioConfig() {

        return minioProperties.toString();
    }

    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传的文件不能为空";
        }

        return minioUploadFile.uploadFile(file);
    }

    @GetMapping("getBucketPolicy/{bucketName}")
    public String getBucketPolicy(@PathVariable("bucketName") String bucketName) {
        if (StringUtils.isBlank(bucketName)) {
            return "Bucket name not blank";
        }

        return minioUploadFile.getBucketPolicy(bucketName);
    }

}
