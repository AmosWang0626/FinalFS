package com.amos.fs.sftp.common.base.sftp.config;

/**
 * Sftp 连接初始化参数
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public class SftpConfig {

    /**
     * host
     */
    private String host;
    /**
     * port
     */
    private Integer port;
    /**
     * username
     */
    private String username;
    /**
     * password
     */
    private String password;
    /**
     * 初始化连接数
     */
    private Integer initConnections;
    /**
     * 最大连接数
     */
    private Integer maxTotal;
    /**
     * 最大空闲连接数
     */
    private Integer maxIdle;
    /**
     * 最小空闲连接数
     */
    private Integer minIdle;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitConnections() {
        return initConnections;
    }

    public void setInitConnections(Integer initConnections) {
        this.initConnections = initConnections;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
}
