package com.amos.fs.minio.service.impl;

import com.amos.common.util.date.DateUtils;
import com.amos.fs.minio.service.BaseFileService;
import io.minio.MinioClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * DESCRIPTION: 上传文件
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/12/22
 */
@Service("fileService")
public class FileServiceImpl extends BaseFileService {

    public static final String DOC_BUCKET_PREFIX = "doc-bucket-";

    @Resource
    private MinioClient minioClient;

    /**
     * 项目级缓存，避免每次上传都调用 minio 查询 bucket 是否存在
     * 如果 bucket 是按月划分的，1年 也就 12条，项目中途重启或更新的话会更小，这个 list 不用删除老数据；反之按需清理 EXIST_BUCKET
     */
    private static final Set<String> EXIST_BUCKET = new HashSet<>();


    @Override
    public String getBucketName() {
        String bucketName = DOC_BUCKET_PREFIX + DateUtils.getDate(LocalDate.now());
        // bucket 不存在则创建
        if (!EXIST_BUCKET.contains(bucketName)) {
            makeBucket(bucketName);
            EXIST_BUCKET.add(bucketName);
        }

        return bucketName;
    }

    @Override
    public MinioClient getMinioClient() {
        return minioClient;
    }

}
