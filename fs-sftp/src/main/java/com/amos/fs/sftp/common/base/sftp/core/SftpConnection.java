package com.amos.fs.sftp.common.base.sftp.core;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * JSch Sftp 连接
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public class SftpConnection {

    /**
     * JSch Session
     */
    private Session session;
    /**
     * JSch ChannelSftp
     */
    private ChannelSftp channelSftp;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }

    public void setChannelSftp(ChannelSftp channelSftp) {
        this.channelSftp = channelSftp;
    }
}
