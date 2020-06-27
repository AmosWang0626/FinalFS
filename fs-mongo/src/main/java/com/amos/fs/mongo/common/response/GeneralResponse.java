package com.amos.fs.mongo.common.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * DESCRIPTION: 通用返回结果
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/5/31
 * @apiNote https://github.com/AmosWang0626/common/tree/master/common-api
 */
public final class GeneralResponse<T> implements IResponseCode {

    private final String code;

    private final String message;

    private T body;

    private GeneralResponse(String message, String code) {
        this.code = code;
        this.message = message;
    }

    private GeneralResponse(IResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    private GeneralResponse(IResponseCode responseCode, T body) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.body = body;
    }

    public static <R> GeneralResponse<R> of(IResponseCode responseCode) {
        return new GeneralResponse<R>(responseCode);
    }

    public static <R> GeneralResponse<R> ofSuccess() {
        return new GeneralResponse<R>(GeneralResponseCode.SUCCESS);
    }

    public static <R> GeneralResponse<R> ofSuccess(R body) {
        return new GeneralResponse<>(GeneralResponseCode.SUCCESS, body);
    }

    public static <R> GeneralResponse<R> ofFailure() {
        return new GeneralResponse<>(GeneralResponseCode.FAILURE);
    }

    /**
     * 参数错误
     *
     * @param message 错误描述
     * @param <R>     泛型类型
     * @return GeneralResponse<R>
     */
    public static <R> GeneralResponse<R> ofErrorParam(String message) {
        return new GeneralResponse<>(
                GeneralResponseCode.ERROR_PARAM.getCode(),
                String.format(GeneralResponseCode.ERROR_PARAM.getMessage(), message)
        );
    }

    /**
     * 非法请求
     *
     * @param <R> 泛型类型
     * @return GeneralResponse<R>
     */
    public static <R> GeneralResponse<R> ofRequestIllegal() {
        return new GeneralResponse<>(GeneralResponseCode.REQUEST_ILLEGAL);
    }

    /**
     * 操作过于频繁
     *
     * @param <R> 泛型类型
     * @return GeneralResponse<R>
     */
    public static <R> GeneralResponse<R> ofOperationFrequently() {
        return new GeneralResponse<>(GeneralResponseCode.OPERATION_FREQUENTLY);
    }


    public boolean isSuccess() {
        return GeneralResponseCode.SUCCESS.getCode().equals(this.getCode());
    }


    public boolean isFailure() {
        return !isSuccess();
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
