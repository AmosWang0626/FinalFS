package com.amos.fs.api.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * DESCRIPTION: FileService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/2/9
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file);

    /**
     * 上传多个文件
     * 如需使用上传文件返回值，可自行 forEach uploadFile(file)
     *
     * @param files 文件
     */
    default void uploadFile(MultipartFile[] files) {
        for (MultipartFile file : files) {
            uploadFile(file);
        }
    }

}
