package com.amos.fs.minio.web;

import com.amos.fs.minio.config.MinioProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 模块名称: FinalFS
 * 模块描述: MinIO
 *
 * @author amos.wang
 * @date 2020/12/17 10:46
 */
@RestController
public class MinioController {

    @Resource
    private MinioProperties minioProperties;

    @GetMapping
    public String minioConfig() {

        return minioProperties.toString();
    }

}
