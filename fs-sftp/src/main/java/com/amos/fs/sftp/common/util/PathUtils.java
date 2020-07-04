package com.amos.fs.sftp.common.util;

import com.amos.fs.sftp.common.constant.PathConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * DESCRIPTION: 路径处理工具类
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/7
 */
public class PathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathUtils.class);

    /**
     * 文件本地路径，从sftp下载后的存储目录
     */
    private static final String CACHE_PATH = "DATA" + PathConst.DIR_SPLIT;

    private static final String VIDEO_PATH;
    private static final String AUDIO_PATH;
    private static final String IMAGE_PATH;
    private static final String DOCUMENT_PATH;
    private static final String OTHER_PATH;

    static {
        VIDEO_PATH = CACHE_PATH + "video";
        AUDIO_PATH = CACHE_PATH + "audio";
        IMAGE_PATH = CACHE_PATH + "image";
        DOCUMENT_PATH = CACHE_PATH + "document";
        OTHER_PATH = CACHE_PATH + "other";

        LOGGER.debug("创建目录[{}], [{}]", VIDEO_PATH, mkdir(VIDEO_PATH));
        LOGGER.debug("创建目录[{}], [{}]", AUDIO_PATH, mkdir(AUDIO_PATH));
        LOGGER.debug("创建目录[{}], [{}]", IMAGE_PATH, mkdir(IMAGE_PATH));
        LOGGER.debug("创建目录[{}], [{}]", DOCUMENT_PATH, mkdir(DOCUMENT_PATH));
        LOGGER.debug("创建目录[{}], [{}]", OTHER_PATH, mkdir(OTHER_PATH));
    }

    private static String mkdir(String path) {
        return new File(path).mkdirs() ? "成功" : "失败";
    }

    public static String getLocalPath(String filename) {
        if (FileExtensionUtils.isAudio(filename)) {
            return AUDIO_PATH;
        }
        if (FileExtensionUtils.isImage(filename)) {
            return IMAGE_PATH;
        }
        if (FileExtensionUtils.isVideo(filename)) {
            return VIDEO_PATH;
        }
        if (FileExtensionUtils.isDocument(filename)) {
            return DOCUMENT_PATH;
        }
        return OTHER_PATH;
    }


    /**
     * 在路径末尾加上 /
     *
     * @param path 路径
     * @return 处理后的路径
     */
    public static String endWithSlash(String path) {
        path = replaceBackslash(path);

        if (StringUtils.endsWith(path, PathConst.DIR_SPLIT)) {
            return path;
        }

        return path + PathConst.DIR_SPLIT;
    }

    /**
     * 替换所有反斜杠
     *
     * @param path 路径
     * @return 处理后的路径
     * @author eko.zhan at Sep 11, 2018 1:55:14 PM
     */
    public static String replaceBackslash(String path) {
        return path.replace("\\", PathConst.DIR_SPLIT);
    }

}
