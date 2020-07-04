package com.amos.fs.sftp.common.base.pool.exception;

/**
 * 连接池异常
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public class PoolException extends RuntimeException {

    private static final long serialVersionUID = -2946266495682282677L;

    public PoolException(String message) {
        super(message);
    }

    public PoolException(String message, Throwable cause) {
        super(message, cause);
    }

}


