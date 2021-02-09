package com.amos.fs.mongo.service;

import com.amos.fs.api.service.FileService;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.model.vo.MongoFsVO;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.util.List;

/**
 * DESCRIPTION: MongoService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
public interface MongoFsService extends FileService {

    /**
     * 根据文件ID获取唯一文件
     *
     * @param id 文件ID
     * @return file
     */
    GridFsResource findById(String id);

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
