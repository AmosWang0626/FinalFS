package com.amos.fs.minio.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIPTION: 上传文件
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/12/22
 */
@Service("minioUploadFile")
public class UploadFileServiceImpl implements UploadFileService {

    private static final String WAV_BUCKET_PREFIX = "wav-bucket-";
    private static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private MinioClient minioClient;

    /**
     * 项目级缓存，避免每次上传都调用 minio 查询 bucket 是否存在
     * 如果 bucket 是按月划分的，1年也就 12 条，项目中途重启或更新的话会更小，这个 list 不用删除老数据，反之按需清理 EXIST_BUCKET
     */
    private static final List<String> EXIST_BUCKET = new ArrayList<>();


    @Override
    public String uploadFile(MultipartFile file) {
        String bucketName = WAV_BUCKET_PREFIX + LocalDate.now().format(SIMPLE_DATE_FORMAT);
        // bucket 不存在则创建
        if (!EXIST_BUCKET.contains(bucketName)) {
            makeBucket(bucketName);
        }

        try {
            minioClient.putObject(PutObjectArgs.builder()
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
    public String getBucketPolicy(String bucketName) {
        try {
            return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException | BucketPolicyTooLargeException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Bucket 无则创建
     *
     * @param bucketName bucketName
     */
    private void makeBucket(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (exists) {
                return;
            }

            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            // 设置权限
            String policyJson = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]}," +
                    "\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"]," +
                    "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]}," +
                    "{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]}," +
                    "\"Action\":[\"s3:GetObject\"]," +
                    "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());

            EXIST_BUCKET.add(bucketName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | IOException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
    }

}
