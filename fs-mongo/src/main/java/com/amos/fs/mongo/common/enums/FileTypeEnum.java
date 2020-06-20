package com.amos.fs.mongo.common.enums;


import com.amos.fs.mongo.common.util.ExtensionUtils;

import java.util.HashMap;
import java.util.Map;

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
        if (ExtensionUtils.isVideo(filename)) {
            fileTypeEnum = VIDEO;
        } else if (ExtensionUtils.isAudio(filename)) {
            fileTypeEnum = AUDIO;
        } else if (ExtensionUtils.isImage(filename)) {
            fileTypeEnum = IMAGE;
        } else if (ExtensionUtils.isDocument(filename)) {
            fileTypeEnum = DOCUMENT;
        }

        return fileTypeEnum.name();
    }

    public static Map<String, String> getAll() {
        Map<String, String> map = new HashMap<>(8);
        for (FileTypeEnum item : FileTypeEnum.values()) {
            map.put(item.name(), item.getName());
        }

        return map;
    }

    public String getName() {
        return name;
    }

}
