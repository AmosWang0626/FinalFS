package com.amos.fs.sftp.common.base.sftp.adapter;

import com.amos.fs.sftp.common.base.pool.adapter.BasePoolAdapter;
import com.amos.fs.sftp.common.base.sftp.config.SftpConfig;
import com.amos.fs.sftp.common.base.sftp.core.SftpConnection;
import com.amos.fs.sftp.common.base.sftp.core.SftpPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sftp 连接池适配器
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public class SftpPoolAdapter extends BasePoolAdapter<SftpConnection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpPoolAdapter.class);

    private final SftpConfig sftpConfig;

    /**
     * Sftp 连接池对象
     */
    private SftpPool sftpPool = null;

    public SftpPoolAdapter(SftpConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

    /**
     * 初始化 Sftp 连接池
     */
    @Override
    public void init() {
        if (sftpConfig == null) {
            LOGGER.error("Sftp 配置为空，终止初始化");
            return;
        }

        if (StringUtils.isAnyBlank(sftpConfig.getHost(), sftpConfig.getUsername(), sftpConfig.getPassword())) {
            LOGGER.error("Sftp 配置错误，终止初始化");
            return;
        }

        LOGGER.info("开始初始化 Sftp 连接池");

        Integer maxTotal = sftpConfig.getMaxTotal();
        Integer minIdle = sftpConfig.getMinIdle();
        Integer maxIdle = sftpConfig.getMaxIdle();

        sftpPool = new SftpPool(objectPoolConfig(maxTotal, minIdle, maxIdle), sftpConfig);

        LOGGER.info("Sftp 连接池初始化完成");
    }

    /**
     * 从 Sftp 连接池中获取连接
     */
    @Override
    public SftpConnection getResource() {
        return sftpPool.getResource();
    }

    /**
     * 将连接还回 Sftp 连接池
     */
    @Override
    public void returnResource(SftpConnection sftpConnection) {
        sftpPool.returnResource(sftpConnection);
    }

    /**
     * 销毁 Sftp 连接池
     */
    @Override
    public void destroy() {
        sftpPool.close();
        LOGGER.info("销毁 Sftp 连接池");
    }

}
