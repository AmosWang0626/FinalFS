package com.amos.fs.api.enums;

import com.amos.common.dto.base.IErrorCode;

/**
 * DESCRIPTION: 文件错误
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/2/9
 */
public enum FsErrorCodeEnum implements IErrorCode {

    /***/
    FS_BASE_isEmpty("200", "文件不能为空");

    private final String code;

    private final String message;

    FsErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
