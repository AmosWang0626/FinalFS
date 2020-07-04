package com.amos.fs.sftp.config;

import com.amos.fs.sftp.common.base.sftp.adapter.SftpPoolAdapter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * DESCRIPTION: SftpPoolBean
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/7/4
 */
@Component("sftpPoolAdapter")
public class SftpPoolBean implements FactoryBean<SftpPoolAdapter> {

    @Resource
    private FsSftpConfig fsSftpConfig;

    private SftpPoolAdapter sftpPoolAdapter;

    @Override
    public SftpPoolAdapter getObject() throws Exception {
        return this.sftpPoolAdapter;
    }

    @Override
    public Class<?> getObjectType() {
        return SftpPoolAdapter.class;
    }

    @PostConstruct
    public void init() throws Exception {
        SftpPoolAdapter sftpPoolAdapter = new SftpPoolAdapter(fsSftpConfig.getSftp());
        sftpPoolAdapter.init();

        this.sftpPoolAdapter = sftpPoolAdapter;
    }

    @PreDestroy
    public void destroy() throws Exception {
        this.sftpPoolAdapter.destroy();
    }

}
