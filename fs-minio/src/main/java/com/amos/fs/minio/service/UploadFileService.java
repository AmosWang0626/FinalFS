package com.amos.fs.minio.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * DESCRIPTION: MinioService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/12/22
 */
public interface UploadFileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file);

    /**
     * get bucket policy
     *
     * @param bucketName bucketName
     * @return bucket policy
     */
    String getBucketPolicy(String bucketName);

}
