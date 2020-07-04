package com.amos.fs.sftp.common.base.sftp.core;

import com.amos.fs.sftp.common.base.sftp.config.SftpConfig;
import com.jcraft.jsch.*;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * Sftp连接池工厂
 * <p>
 * 包括连接池对象的创建、销毁等操作
 *
 * @author daoyuan
 * @date 2020/6/6 17:20
 */
public class SftpPooledFactory implements PooledObjectFactory<SftpConnection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpPooledFactory.class);

    private final SftpConfig sftpConfig;


    public SftpPooledFactory(SftpConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

    /**
     * 创建连接池对象
     */
    @Override
    public PooledObject<SftpConnection> makeObject() throws Exception {

        return new DefaultPooledObject<>(getSftpClient());
    }

    /**
     * 销毁连接池对象
     */
    @Override
    public void destroyObject(PooledObject<SftpConnection> pooledObject) throws Exception {
        SftpConnection object = pooledObject.getObject();
        if (Objects.isNull(object)) {
            return;
        }

        if (Objects.nonNull(object.getChannelSftp())) {
            object.getChannelSftp().disconnect();
        }
        if (Objects.nonNull(object.getSession())) {
            object.getSession().disconnect();
        }
    }

    /**
     * 校验连接池对象
     * 后台检测线程会周期性监测池中对象，如果无效，则会调用 destroyObject 释放连接
     */
    @Override
    public boolean validateObject(PooledObject<SftpConnection> pooledObject) {
        SftpConnection object = pooledObject.getObject();
        if (Objects.isNull(object)) {
            return false;
        }

        try {
            return object.getSession().isConnected()
                    && object.getChannelSftp().isConnected() && !object.getChannelSftp().isClosed();
        } catch (Exception e) {
            LOGGER.error("SftpConnection validate fail. [{}]", e.getMessage());
        }

        return false;
    }

    /**
     * 激活连接池对象，如果对象不为空，将重新初始化该对象
     */
    @Override
    public void activateObject(PooledObject<SftpConnection> pooledObject) throws Exception {
        SftpConnection object = pooledObject.getObject();
        if (Objects.isNull(object)) {
            LOGGER.error("SftpConnection activate fail !!!!!!");
            return;
        }

        activateSftpClient(object);
    }

    /**
     * 钝化连接池对象，让对象休息一下
     */
    @Override
    public void passivateObject(PooledObject<SftpConnection> pooledObject) throws Exception {
        SftpConnection object = pooledObject.getObject();
        if (Objects.isNull(object)) {
            return;
        }

        object.getSession().disconnect();
        object.getChannelSftp().disconnect();
    }

    /**
     * 获取一个 sftp 连接
     *
     * @return SftpConnection
     * @throws JSchException JSch异常
     */
    private SftpConnection getSftpClient() throws JSchException {
        SftpConnection sftpConnection = new SftpConnection();

        activateSftpClient(sftpConnection);

        return sftpConnection;
    }

    /**
     * 为 SftpConnection 对象装配 Session 和 Channel
     *
     * @throws JSchException JSch异常
     */
    private void activateSftpClient(SftpConnection sftpConnection) throws JSchException {
        Session session = new JSch().getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
        session.setPassword(sftpConfig.getPassword());
        // $ ssh -o StrictHostKeyChecking=no 关闭公钥确认
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(60000);
        session.connect();

        // 打开SFTP通道
        Channel chan = session.openChannel("sftp");
        // 建立SFTP通道的连接
        chan.connect();

        sftpConnection.setSession(session);
        sftpConnection.setChannelSftp((ChannelSftp) chan);
    }

}
