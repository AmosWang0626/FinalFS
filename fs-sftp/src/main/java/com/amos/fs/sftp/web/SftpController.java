package com.amos.fs.sftp.web;

import com.amos.fs.sftp.common.constant.PathConst;
import com.amos.fs.sftp.common.enums.FileTypeEnum;
import com.amos.fs.sftp.common.util.FileExtensionUtils;
import com.amos.fs.sftp.common.util.PathUtils;
import com.amos.fs.sftp.config.FsSftpConfig;
import com.amos.fs.sftp.model.vo.FileLsVO;
import com.amos.fs.sftp.service.SftpService;
import com.jcraft.jsch.SftpException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 模块名称: storage
 * 模块描述: FileController
 *
 * @author amos.wang
 * @date 2020/6/5 9:55
 */
@Controller
public class SftpController {

    @Resource
    private SftpService sftpService;
    @Resource
    private FsSftpConfig fsSftpConfig;

    @GetMapping
    public ModelAndView index(String path) {
        String dir = fsSftpConfig.getBaseDir();
        if (StringUtils.isNotBlank(path)) {
            dir = PathUtils.endWithSlash(dir) + path;
        }

        FileLsVO fileLsVO = new FileLsVO();
        fileLsVO.setPath(dir);
        fileLsVO.setRelativePath(path);
        try {
            fileLsVO.setFileNames(sftpService.allFileName(dir));
        } catch (SftpException e) {
            e.printStackTrace();
        }

        return new ModelAndView("index", "data", fileLsVO);
    }

    @PostMapping("upload")
    public ModelAndView upload(MultipartFile file) throws IOException, SftpException {
        if (file == null) {
            return index(null);
        }
        String dir = fsSftpConfig.getBaseDir();
        String originalFilename = file.getOriginalFilename();
        // file final dir
        String uploadDir = FileTypeEnum.getUploadDir(originalFilename);
        String path = PathUtils.endWithSlash(dir) + uploadDir;
        // reset file name
        String extension = FilenameUtils.getExtension(originalFilename);
        String filename = System.currentTimeMillis() + "." + extension;
        sftpService.upload(file.getBytes(), path, filename);

        return index(null);
    }

    /**
     * 读取文件（有本地缓存）
     *
     * @param filename 文件名
     * @param download 1 下载
     * @return 文件流
     * @throws Exception 读取文件异常
     */
    @GetMapping(value = "read")
    public ResponseEntity<byte[]> read(String filename, String download) throws Exception {
        if (Strings.isBlank(filename)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        String dir = fsSftpConfig.getBaseDir();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileExtensionUtils.getMediaType(filename));
        if (PathConst.DOWNLOAD_FLAG.equals(download)) {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(filename, "UTF-8"));
        }

        // 本地文件缓存地址
        String cachePath = PathUtils.getLocalPath(filename);
        cachePath = cachePath + PathConst.DIR_SPLIT + filename;
        File file = new File(cachePath);
        if (file.exists()) {
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        }

        // 远程文件地址
        String originPath = PathUtils.endWithSlash(dir) +
                FileTypeEnum.getUploadDir(filename) + PathConst.DIR_SPLIT + filename;
        sftpService.read(originPath, file);

        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 读取文件（无缓存）
     *
     * @param filename 文件名
     * @return 文件流
     * @throws Exception 读取文件异常
     */
    @GetMapping(value = "readOnly")
    public ResponseEntity<byte[]> readOnly(String filename) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileExtensionUtils.getMediaType(filename));

        String dir = fsSftpConfig.getBaseDir();
        // 远程文件地址
        String originPath = PathUtils.endWithSlash(dir) +
                FileTypeEnum.getUploadDir(filename) + PathConst.DIR_SPLIT + filename;

        return new ResponseEntity<>(sftpService.read(originPath), headers, HttpStatus.OK);
    }

}
