/*
 * Power by www.xiaoi.com
 */
package com.amos.fs.mongo.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.util.Optional;

/**
 * 获取文件类型工具类
 *
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2017年12月21日 下午3:10:34
 */
public class ExtensionUtils {

    private static final String[] VIDEO_ARRAY = new String[]{"mp4", "avi", "wma", "rmvb", "rm", "flash", "mid", "3gp", "wmv"};
    private static final String[] IMAGE_ARRAY = new String[]{"jpg", "jpeg", "png", "gif", "ico", "bmp"};
    private static final String[] AUDIO_ARRAY = new String[]{"mp3", "wav", "ogg", "m4a", "flac"};
    private static final String[] OFFICE_ARRAY = new String[]{"doc", "docx", "xls", "xlsx", "ppt", "pptx"};
    private static final String[] DOCUMENT_ARRAY = new String[]{"doc", "docx", "xls", "xlsx", "ppt", "pptx", "md", "pdf", "txt"};

    /**
     * 是否是视频文件
     *
     * @return 是否
     * @author eko.zhan at 2017年12月21日 下午3:38:28
     */
    public static Boolean isVideo(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(VIDEO_ARRAY, extension);
    }

    /**
     * 是否是图片
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at 2017年12月21日 下午3:47:18
     */
    public static Boolean isImage(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(IMAGE_ARRAY, extension);
    }

    /**
     * 是否是Office文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Jan 3, 2018 5:30:52 PM
     */
    public static Boolean isOffice(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(OFFICE_ARRAY, extension);
    }

    /**
     * 是否是音频文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Jan 5, 2018 3:41:36 PM
     */
    public static Boolean isAudio(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(AUDIO_ARRAY, extension);
    }

    /**
     * 是否是文档
     *
     * @param filename 文件名
     * @return 是否
     * @author amos.wang
     * @date 2020/6/9
     */
    public static Boolean isDocument(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(DOCUMENT_ARRAY, extension);
    }

    /**
     * 是否是Excel文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Aug 7, 2018 11:33:06 AM
     */
    public static Boolean isExcel(String filename) {
        String[] arr = new String[]{"xls", "xlsx"};
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(arr, extension);
    }

    /**
     * 是否是Word文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Aug 7, 2018 11:33:06 AM
     */
    public static Boolean isWord(String filename) {
        String[] arr = new String[]{"doc", "docx"};
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(arr, extension);
    }

    /**
     * 是否是Ppt文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Aug 7, 2018 11:33:06 AM
     */
    public static Boolean isPpt(String filename) {
        String[] arr = new String[]{"ppt", "pptx"};
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(arr, extension);
    }

    /**
     * 是否是 Word 或 Excel 文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Aug 7, 2018 11:37:50 AM
     */
    public static Boolean isWordOrExcel(String filename) {
        return isWord(filename) || isExcel(filename);
    }

    /**
     * 是否是 office 或 pdf 文件
     *
     * @param filename 文件名
     * @return 是否
     * @author eko.zhan at Aug 7, 2018 11:45:10 AM
     */
    public static Boolean isOfficeOrPdf(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        if (isOffice(filename) || "pdf".equals(extension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 是否是 html 文件
     */
    public static Boolean isHtml(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        if ("html".equals(extension) || "htm".equals(extension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 是否是 pdf 文件
     */
    public static Boolean isPdf(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        if ("pdf".equals(extension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 是否是 txt 文件
     */
    public static Boolean isTxt(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        if ("txt".equals(extension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 修复不支持的文件格式
     *
     * @param extension 文件后缀名
     * @return MediaType
     */
    public static Optional<String> fixContentType(String extension) {
        if (StringUtils.isBlank(extension)) {
            return Optional.empty();
        }

        // 文件名后缀转小写
        extension = extension.toLowerCase();

        String contentType = null;

        // 兼容 markdown 文件类型
        if ("md".equals(extension)) {
            contentType = MediaType.TEXT_MARKDOWN.toString();
        }

        return Optional.ofNullable(contentType);
    }

}
