package com.amos.fs.mongo.common.response;

/**
 * DESCRIPTION: 系统状态码
 * 注意：1xxx 属于系统保留状态码；
 * 建议业务系统状态码从 2000 开始。
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/5/31
 * @apiNote https://github.com/AmosWang0626/common/tree/master/common-api
 */
public enum GeneralResponseCode implements IResponseCode {

    /***/
    SUCCESS("1000", "成功!"),
    FAILURE("1001", "失败!"),
    ERROR_PARAM("1002", "参数错误（%s）"),
    REQUEST_ILLEGAL("1003", "非法请求"),
    OPERATION_FREQUENTLY("1004", "操作过于频繁");


    GeneralResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
