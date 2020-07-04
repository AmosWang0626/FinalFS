package com.amos.fs.sftp.common.base.sftp.core;

import com.amos.fs.sftp.common.base.pool.BaseObjectPool;
import com.amos.fs.sftp.common.base.sftp.config.SftpConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 初始化 Sftp 连接池
 * <p>
 * 1.接收连接池配置
 * 2.创建连接池工厂
 * 3.根据初始化连接数，创建多个 Sftp 连接
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public class SftpPool extends BaseObjectPool<SftpConnection> {

    /**
     * 初始化 sftp 连接池
     *
     * @param objectPoolConfig 对象池配置
     * @param sftpConfig       sftp配置信息
     */
    public SftpPool(GenericObjectPoolConfig<SftpConnection> objectPoolConfig, SftpConfig sftpConfig) {
        super(objectPoolConfig, new SftpPooledFactory(sftpConfig));

        // 初始化连接池，一次性创建多个 SftpConnection 对象
        if (sftpConfig.getInitConnections() != null && sftpConfig.getInitConnections() > 0) {
            this.addObjects(sftpConfig.getInitConnections());
        }
    }

}
