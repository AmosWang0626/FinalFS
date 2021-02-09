package com.amos.fs.sftp.service.impl;

import com.amos.fs.api.exception.FsException;
import com.amos.fs.sftp.common.base.pool.exception.PoolException;
import com.amos.fs.sftp.common.base.sftp.adapter.SftpPoolAdapter;
import com.amos.fs.sftp.common.base.sftp.core.SftpConnection;
import com.amos.fs.sftp.common.constant.PathConst;
import com.amos.fs.sftp.common.enums.FileTypeEnum;
import com.amos.fs.sftp.common.util.PathUtils;
import com.amos.fs.sftp.config.FsSftpConfig;
import com.amos.fs.sftp.service.SftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Standalone-SftpPool 操作实现类
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/4
 */
@Service
public class SftpServiceImpl implements SftpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpServiceImpl.class);

    @Resource
    private FsSftpConfig fsSftpConfig;
    @Resource
    private SftpPoolAdapter sftpPoolAdapter;


    @Override
    public String uploadFile(MultipartFile file) {
        String dir = fsSftpConfig.getBaseDir();
        String originalFilename = file.getOriginalFilename();
        // file final dir
        String uploadDir = FileTypeEnum.getUploadDir(originalFilename);
        String path = PathUtils.endWithSlash(dir) + uploadDir;
        // reset file name
        String extension = FilenameUtils.getExtension(originalFilename);
        String filename = System.currentTimeMillis() + "." + extension;

        try {
            upload(new ByteArrayInputStream(file.getBytes()), path, filename);
        } catch (SftpException | IOException e) {
            e.printStackTrace();
            throw new FsException("上传失败 !");
        }

        return path + PathConst.DIR_SPLIT + filename;
    }

    /**
     * 下载文件到指定文件
     *
     * @throws SftpException SftpException
     */
    @Override
    public void download(String path, File saveFile) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sc = null;
        FileOutputStream os = null;
        try {
            sc = sftpPoolAdapter.getResource();
            ChannelSftp channel = sc.getChannelSftp();
            // 文件所在目录
            String dirPath = path.substring(0, path.lastIndexOf(PathConst.DIR_SPLIT));
            if (!StringUtils.endsWith(path, PathConst.DIR_SPLIT)) {
                dirPath = dirPath + PathConst.DIR_SPLIT;
            }
            channel.cd(dirPath);

            if (!saveFile.exists()) {
                if (!saveFile.createNewFile()) {
                    LOGGER.error("文件下载失败 --- 创建缓存文件失败");
                }
            }

            os = new FileOutputStream(saveFile);

            channel.get(path, os);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
            sftpPoolAdapter.returnResource(sc);
        }
    }

    /**
     * 获取文件流
     *
     * @throws SftpException SftpException
     */
    @Override
    public InputStream getFileInputStream(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sc = null;
        try {
            sc = sftpPoolAdapter.getResource();
            ChannelSftp channel = sc.getChannelSftp();

            return channel.get(path);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sc);
        }
    }

    /**
     * 创建文件夹
     *
     * @throws PoolException PoolException
     * @throws SftpException SftpException
     */
    @Override
    public void mkdir(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sftpConnection = null;
        try {
            sftpConnection = sftpPoolAdapter.getResource();
            ChannelSftp channel = sftpConnection.getChannelSftp();

            try {
                channel.ls(path);
            } catch (Exception e) {
                channel.mkdir(path);
            }
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (PoolException e) {
            throw new PoolException("获取连接失败 !!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sftpConnection);
        }
    }

    /**
     * 删除文件
     *
     * @throws SftpException SftpException
     * @throws PoolException PoolException
     */
    @Override
    public void delete(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sftpConnection = null;
        try {
            sftpConnection = sftpPoolAdapter.getResource();
            ChannelSftp channel = sftpConnection.getChannelSftp();

            channel.rm(path);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (PoolException e) {
            throw new PoolException("获取连接失败 !!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sftpConnection);
        }
    }

    /**
     * 移动文件
     *
     * @throws SftpException SftpException
     * @throws PoolException PoolException
     */
    @Override
    public void rename(String oldPath, String newPath) throws SftpException {
        oldPath = PathUtils.replaceBackslash(oldPath);
        newPath = PathUtils.replaceBackslash(newPath);

        SftpConnection sftpConnection = null;
        try {
            sftpConnection = sftpPoolAdapter.getResource();
            ChannelSftp channel = sftpConnection.getChannelSftp();

            channel.rename(oldPath, newPath);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (PoolException e) {
            throw new PoolException("获取连接失败 !!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sftpConnection);
        }
    }

    /**
     * 获取文件大小
     *
     * @throws SftpException SftpException
     */
    @Override
    public long fileSize(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        return fileAttrs(path).getSize();
    }

    /**
     * 获取文件所有信息
     *
     * @throws SftpException SftpException
     */
    @Override
    public SftpATTRS fileAttrs(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sc = null;
        try {
            sc = sftpPoolAdapter.getResource();
            ChannelSftp channel = sc.getChannelSftp();

            return channel.lstat(path);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sc);
        }
    }

    /**
     * 获取文件修改时间(待测试)
     *
     * @throws SftpException SftpException
     */
    @Override
    public String lastModify(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);
        SftpATTRS lstat = this.fileAttrs(path);

        return lstat.getMTime() + "000";
    }

    /**
     * 获取文件夹下所有文件信息
     */

    @Override
    public Vector<LsEntry> ls(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        SftpConnection sc = null;
        try {
            sc = sftpPoolAdapter.getResource();
            ChannelSftp channel = sc.getChannelSftp();

            final Vector<LsEntry> lsEntries = new Vector<>();

            channel.ls(path, lsEntry -> {
                lsEntries.addElement(lsEntry);
                return 0;
            });

            return lsEntries;
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (PoolException e) {
            throw new PoolException("获取连接失败 !!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sftpPoolAdapter.returnResource(sc);
        }
    }

    /**
     * 文件获取文件名/文件夹则获取下面所有文件名
     *
     * @throws SftpException SftpException
     */
    @Override
    public List<String> allFileName(String path) throws SftpException {
        path = PathUtils.replaceBackslash(path);

        List<String> list = new ArrayList<>();
        Vector<LsEntry> v = ls(path);
        for (LsEntry lsEntry : v) {
            if (".".equals(lsEntry.getFilename()) || "..".equals(lsEntry.getFilename())) {
                continue;
            }

            list.add(lsEntry.getFilename());
        }

        return list;
    }

    /**
     * 文件上传实现
     *
     * @param inputStream 输入流
     * @param path        上传路径
     * @param fileName    文件名
     * @throws PoolException PoolException
     * @throws SftpException SftpException
     */
    private void upload(InputStream inputStream, String path, String fileName) throws SftpException {
        assert inputStream != null;

        path = PathUtils.replaceBackslash(path);

        SftpConnection sftpConnection = null;
        try {
            sftpConnection = sftpPoolAdapter.getResource();

            ChannelSftp channel = sftpConnection.getChannelSftp();
            try {
                channel.ls(path);
            } catch (Exception e) {
                channel.mkdir(path);
            }

            channel.put(inputStream, (path.endsWith(PathConst.DIR_SPLIT) ? path : path + PathConst.DIR_SPLIT) + fileName, ChannelSftp.OVERWRITE);
        } catch (SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "", e);
        } catch (PoolException e) {
            throw new PoolException("获取连接失败 !!!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            sftpPoolAdapter.returnResource(sftpConnection);
        }
    }

}
