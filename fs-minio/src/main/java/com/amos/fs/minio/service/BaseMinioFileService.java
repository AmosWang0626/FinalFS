package com.amos.fs.minio.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模块名称: filesystem
 * 模块描述: 上传文件公共方法
 *
 * @author amos
 * @date 2020/12/27
 */
public abstract class BaseMinioFileService implements MinioFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMinioFileService.class);

    /**
     * 获取 bucketName 可独立实现重复性校验
     *
     * @return bucketName
     * @implNote 如果使用上传文件，务必重写该方法
     */
    public String getBucketName() {
        return null;
    }

    /**
     * 获取 MinioClient 自己实现
     *
     * @return MinioClient
     */
    public abstract MinioClient getMinioClient();


    /**
     * 上传文件
     *
     * @param file 文件
     * @return 访问路径
     * @see #getBucketName() 务必实现该方法，指定要上传到哪个bucket
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String bucketName = getBucketName();

        try {
            getMinioClient().putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }

        return bucketName + "/" + file.getOriginalFilename();
    }

    @Override
    public int batchDelete(String bucketName, List<String> objectNameList) {
        if (CollectionUtils.isEmpty(objectNameList)) {
            return 0;
        }

        List<DeleteObject> list = new ArrayList<>();
        objectNameList.forEach(objectName -> list.add(new DeleteObject(objectName)));

        Iterable<Result<DeleteError>> results =
                getMinioClient().removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(list).build());
        results.forEach(deleteErrorResult -> {
            try {
                DeleteError deleteError = deleteErrorResult.get();
                LOGGER.error("【MinIO删除对象】删除失败！[{}/{}]，错误信息：[{}]", deleteError.bucketName(), deleteError.objectName(), deleteError.message());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
        });

        return objectNameList.size();
    }

    @Override
    public boolean existFile(String bucketName, String objectName) {
        return getFileInputStream(bucketName, objectName) != null;
    }

    @Override
    public InputStream getFileInputStream(String bucketName, String objectName) {
        InputStream inputStream = null;
        try {
            inputStream = getMinioClient().getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    @Override
    public List<String> findBucketName(String baseBucketName) {
        List<String> bucketNameList = new ArrayList<>();
        try {
            bucketNameList = getMinioClient().listBuckets().stream()
                    .map(Bucket::name)
                    .filter(bucketName -> bucketName.contains(baseBucketName))
                    .collect(Collectors.toList());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }

        return bucketNameList;
    }

    @Override
    public List<String> objectNames(String bucketName) {
        List<String> objectNameList = new ArrayList<>();
        Iterable<Result<Item>> results = getMinioClient().listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        results.forEach(itemResult -> {
            try {
                objectNameList.add(itemResult.get().objectName());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
        });

        return objectNameList;
    }

    @Override
    public void setReadOnlyPolicy(String bucketName) {
        try {
            // 设置权限
            String policyJson = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]}," +
                    "\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"]," +
                    "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]}," +
                    "{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]}," +
                    "\"Action\":[\"s3:GetObject\"]," +
                    "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            getMinioClient().setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bucket 无则创建
     *
     * @param bucketName bucketName
     */
    public void makeBucket(String bucketName) {
        try {
            boolean exists = getMinioClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (exists) {
                return;
            }

            getMinioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            // 设置只读权限
            setReadOnlyPolicy(bucketName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
    }

}
