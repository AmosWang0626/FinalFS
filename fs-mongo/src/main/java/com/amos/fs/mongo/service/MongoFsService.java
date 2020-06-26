package com.amos.fs.mongo.service;

import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.model.vo.MongoFsVO;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DESCRIPTION: MongoService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
public interface MongoFsService {

    /**
     * 上传
     *
     * @param files 文件
     */
    void upload(MultipartFile[] files);

    /**
     * 上传
     *
     * @param files 文件
     * @return 文件ID
     */
    String upload(MultipartFile files);

    /**
     * 根据文件ID获取唯一文件
     *
     * @param id 文件ID
     * @return file
     */
    GridFSFile findById(String id);

    /**
     * 根据名字获取唯一文件
     *
     * @param filename 文件名字
     * @return file
     */
    GridFSFile findByFilename(String filename);

    /**
     * 根据名字获取文件集合
     *
     * @param form 文件名字
     * @return file
     */
    List<MongoFsVO> findBy(MongoFsForm form);

    /**
     * 删除
     *
     * @param id 文件ID
     */
    void deleteById(String id);

}
