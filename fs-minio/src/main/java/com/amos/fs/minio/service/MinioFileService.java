package com.amos.fs.minio.service;

import com.amos.fs.api.service.FileService;

import java.io.InputStream;
import java.util.List;

/**
 * DESCRIPTION: MinioService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/12/22
 */
public interface MinioFileService extends FileService {

    /**
     * 获取文件流
     *
     * @param bucketName bucket name
     * @param objectName object name
     * @return InputStream 可能为空
     */
    InputStream getFileInputStream(String bucketName, String objectName);

    /**
     * 批量删除
     *
     * @param bucketName     bucket name
     * @param objectNameList object name list
     * @return 删除数量
     */
    int batchDelete(String bucketName, List<String> objectNameList);

    /**
     * 文件是否存在
     *
     * @param bucketName bucket name
     * @param objectName object name
     * @return true-存在；false-不存在
     */
    boolean existFile(String bucketName, String objectName);

    /**
     * 获取包含指定名字的所有 BucketName
     *
     * @param baseBucketName BucketName
     * @return 筛选后的 BucketName
     */
    List<String> findBucketName(String baseBucketName);

    /**
     * 获取指定bucket下的所有文件名字
     *
     * @param bucketName BucketName
     * @return 所有文件名字
     */
    List<String> objectNames(String bucketName);

    /**
     * 设置指定bucket只读权限
     *
     * @param bucketName BucketName
     */
    void setReadOnlyPolicy(String bucketName);

}
