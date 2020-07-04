package com.amos.fs.sftp.common.base.pool.adapter;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Optional;

/**
 * DESCRIPTION: 通用连接池适配器抽象类
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/7
 */
public abstract class BasePoolAdapter<T> {

    private static final int DEFAULT_MAX_TOTAL = 10;
    private static final int DEFAULT_MAX_IDLE = 10;
    private static final int DEFAULT_MIN_IDLE = 2;
    private static final boolean DEFAULT_TEST_ON_BORROW = true;
    private static final boolean DEFAULT_TEST_ON_RETURN = true;
    private static final int DEFAULT_MAX_WAIT_MILLIS = 300 * 1000;

    /**
     * 初始化对象池
     */
    public abstract void init();

    /**
     * 销毁对象池
     */
    public abstract void destroy();

    /**
     * 从对象池中获取一个对象
     *
     * @return T
     */
    public abstract T getResource();

    /**
     * 将对象还回对象池
     *
     * @param resource T
     */
    public abstract void returnResource(T resource);

    /**
     * 对象池配置
     */
    public static <T> GenericObjectPoolConfig<T> objectPoolConfig(Integer maxTotal, Integer maxIdle, Integer minIdle) {
        GenericObjectPoolConfig<T> genericObjectPoolConfig = new GenericObjectPoolConfig<>();

        // 最大连接数
        genericObjectPoolConfig.setMaxTotal(Optional.ofNullable(maxTotal).orElse(DEFAULT_MAX_TOTAL));
        // 最大空闲连接数
        genericObjectPoolConfig.setMaxIdle(Optional.ofNullable(maxIdle).orElse(DEFAULT_MAX_IDLE));
        //最小空闲连接数
        genericObjectPoolConfig.setMinIdle(Optional.ofNullable(minIdle).orElse(DEFAULT_MIN_IDLE));
        // 池为空时取对象等待的最大毫秒数
        genericObjectPoolConfig.setMaxWaitMillis(DEFAULT_MAX_WAIT_MILLIS);
        // 取出对象时验证(此处设置成验证ftp是否处于连接状态).
        genericObjectPoolConfig.setTestOnBorrow(DEFAULT_TEST_ON_BORROW);
        // 还回对象时验证(此处设置成验证ftp是否处于连接状态).
        genericObjectPoolConfig.setTestOnReturn(DEFAULT_TEST_ON_RETURN);

        return genericObjectPoolConfig;
    }

}
