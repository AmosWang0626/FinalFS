package com.amos.fs.sftp.common.base.pool;

import com.amos.fs.sftp.common.base.pool.exception.PoolException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.NoSuchElementException;

/**
 * 对象池抽象类
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public abstract class BaseObjectPool<T> {

    /**
     * real object pool
     */
    private GenericObjectPool<T> genericObjectPool;


    public BaseObjectPool(GenericObjectPoolConfig<T> poolConfig, PooledObjectFactory<T> factory) {
        initPool(poolConfig, factory);
    }


    /**
     * 初始化对象池
     *
     * @param config  对象池配置
     * @param factory 对象池工厂
     */
    public void initPool(GenericObjectPoolConfig<T> config, PooledObjectFactory<T> factory) {
        if (this.genericObjectPool != null) {
            try {
                close();
            } catch (Exception ignored) {
            }
        }
        this.genericObjectPool = new GenericObjectPool<>(factory, config);
    }

    /**
     * 从对象池中获取一个对象
     *
     * @return T
     */
    public T getResource() {
        try {
            return this.genericObjectPool.borrowObject();
        } catch (NoSuchElementException nse) {
            if (null == nse.getCause()) {
                throw new PoolException("Could not get a resource since the pool is exhausted", nse);
            }
            throw new PoolException("Could not get a resource from the pool", nse);
        } catch (Exception e) {
            throw new PoolException("Could not get a resource from the pool", e);
        }
    }


    /**
     * 将对象还回对象池
     *
     * @param resource T
     */
    public void returnResource(T resource) {
        if (resource == null) {
            return;
        }

        try {
            this.genericObjectPool.returnObject(resource);
        } catch (Exception e) {
            throw new PoolException("Could not return the resource to the pool", e);
        }
    }


    /**
     * 关闭对象池
     */
    public void close() {
        try {
            this.genericObjectPool.close();
        } catch (Exception e) {
            throw new PoolException("Could not destroy the pool", e);
        }
    }


    /**
     * 判断对象池是否已关闭
     *
     * @return 已关闭返回 true
     */
    public boolean isClosed() {
        return this.genericObjectPool.isClosed();
    }


    /**
     * 一次性添加指定个数对象
     *
     * @param count 对象个数
     */
    public void addObjects(int count) {
        try {
            for (int i = 0; i < count; i++) {
                this.genericObjectPool.addObject();
            }
        } catch (Exception e) {
            throw new PoolException("Error trying to add idle objects", e);
        }
    }

}
