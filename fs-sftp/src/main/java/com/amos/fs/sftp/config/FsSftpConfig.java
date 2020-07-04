package com.amos.fs.sftp.config;

import com.amos.fs.sftp.common.base.sftp.config.SftpConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DESCRIPTION: FsSftpConfig
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/7/4
 */
@Configuration
@ConfigurationProperties("fs")
public class FsSftpConfig {

    private String baseDir;

    private SftpConfig sftp;

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public SftpConfig getSftp() {
        return sftp;
    }

    public void setSftp(SftpConfig sftp) {
        this.sftp = sftp;
    }
}
