package com.amos.fs.mongo.common.response;

/**
 * DESCRIPTION: 统一异常返回码接口
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/5/31
 * @apiNote https://github.com/AmosWang0626/common/tree/master/common-api
 */
public interface IResponseCode {

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    String getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMessage();

}
