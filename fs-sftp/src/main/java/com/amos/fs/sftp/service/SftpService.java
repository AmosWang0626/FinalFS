package com.amos.fs.sftp.service;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Vector;

/**
 * Sftp 操作接口
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
public interface SftpService {

    /**
     * 上传 --- File方式上传
     *
     * @param file     文件
     * @param path     上传路径
     * @param fileName 文件名
     * @throws SftpException         SftpException
     * @throws FileNotFoundException 文件不存在
     */
    void upload(File file, String path, String fileName) throws SftpException, FileNotFoundException;

    /**
     * 上传 --- byte[]方式上传
     *
     * @param data     byte[]
     * @param path     上传路径
     * @param fileName 文件名字
     * @throws SftpException SftpException
     */
    void upload(byte[] data, String path, String fileName) throws SftpException;

    /**
     * 下载 --- 下载到指定文件
     *
     * @param path     远程文件路径
     * @param saveFile 下载到指定文件
     * @throws SftpException SftpException
     */
    void read(String path, File saveFile) throws SftpException;

    /**
     * 下载 --- 获取文件流
     *
     * @param path 文件路径
     * @return InputStream
     * @throws SftpException SftpException
     */
    byte[] read(String path) throws SftpException;

    /**
     * 创建文件夹
     *
     * @param path 路径
     * @throws SftpException SftpException
     */
    void mkdir(String path) throws SftpException;

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @throws SftpException SftpException
     */
    void delete(String path) throws SftpException;

    /**
     * 移动文件
     *
     * @param oldPath 原文件路径
     * @param newPath 移动到
     * @throws SftpException Exception
     */
    void rename(String oldPath, String newPath) throws SftpException;

    /**
     * 获取文件大小
     *
     * @param path 文件路径
     * @return 文件大小
     * @throws SftpException SftpException
     */
    long fileSize(String path) throws SftpException;

    /**
     * 获取文件所有信息
     *
     * @param path 文件路径
     * @return 文件所有信息
     * @throws SftpException SftpException
     */
    SftpATTRS fileAttrs(String path) throws SftpException;

    /**
     * 获取文件修改时间
     *
     * @param path 文件路径
     * @return 文件修改时间
     * @throws SftpException SftpException
     */
    String lastModify(String path) throws SftpException;

    /**
     * 查看指定目录下文件信息
     *
     * @param path 路径
     * @return 文件信息集合
     * @throws SftpException SftpException
     */
    Vector<LsEntry> ls(String path) throws SftpException;

    /**
     * 文件获取文件名/文件夹则获取下面所有文件名
     *
     * @param path 路径
     * @return 文件名集合
     * @throws SftpException SftpException
     */
    List<String> allFileName(String path) throws SftpException;

}
