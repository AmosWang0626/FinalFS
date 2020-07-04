package com.amos.fs.sftp.common.enums;

import com.amos.fs.sftp.common.util.FileExtensionUtils;

/**
 * DESCRIPTION: 文件类型枚举
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
public enum FileTypeEnum {

    /***/
    VIDEO("视频"),
    AUDIO("音频"),
    IMAGE("图片"),
    DOCUMENT("文档"),
    OTHER("其他");

    private final String name;

    FileTypeEnum(String name) {
        this.name = name;
    }

    /**
     * 根据文件名获取文件类型
     *
     * @param filename 文件名
     * @return 文件类型
     */
    public static String getFileType(String filename) {
        FileTypeEnum fileTypeEnum = OTHER;
        if (FileExtensionUtils.isVideo(filename)) {
            fileTypeEnum = VIDEO;
        } else if (FileExtensionUtils.isAudio(filename)) {
            fileTypeEnum = AUDIO;
        } else if (FileExtensionUtils.isImage(filename)) {
            fileTypeEnum = IMAGE;
        } else if (FileExtensionUtils.isDocument(filename)) {
            fileTypeEnum = DOCUMENT;
        }

        return fileTypeEnum.name();
    }

    /**
     * 根据文件名获取文件要存放的文件夹
     *
     * @param filename 文件名
     * @return 要存放的文件夹
     */
    public static String getUploadDir(String filename) {

        return getFileType(filename).toLowerCase();
    }

    public String getName() {
        return name;
    }

}
