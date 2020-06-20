package com.amos.fs.mongo.web;

import com.amos.fs.mongo.common.constant.MongoFsConstant;
import com.amos.fs.mongo.common.enums.FileTypeEnum;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.service.MongoFsService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DESCRIPTION: MongoFileController
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Controller
@RequestMapping("mongo")
public class MongoFsController {

    @Resource
    private MongoFsService mongoFsService;
    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;


    /**
     * 跳转到 index页面
     *
     * @return index页面
     */
    @GetMapping
    public ModelAndView index(MongoFsForm form) {
        Map<String, Object> response = new HashMap<>(4);
        response.put("data", mongoFsService.findBy(form));
        response.put("fileTypeEnums", FileTypeEnum.values());
        response.put("realName", form == null ? "" : form.getRealName());
        response.put("fileTypeEnum", form == null ? FileTypeEnum.AUDIO : form.getFileTypeEnum());

        return new ModelAndView("index", response);
    }

    /**
     * 文件上传
     *
     * @param files 批量文件上传
     * @return 重定向到 index页面
     */
    @PostMapping("upload")
    public String upload(MultipartFile[] files) {
        mongoFsService.upload(files);

        return "redirect:/mongo";
    }

    /**
     * 读取文件
     *
     * @param id       文件ID
     * @param response HttpServletResponse
     * @throws Exception 读取文件异常
     */
    @GetMapping(value = "read/{id}")
    public void readById(@PathVariable String id, HttpServletResponse response) throws Exception {
        GridFSFile gridFsFile = mongoFsService.findById(id);
        Optional.ofNullable(gridFsFile.getMetadata())
                .ifPresent(document -> response.setContentType(document.getString(MongoFsConstant.CONTENT_TYPE)));

        GridFSBucket bucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());
        GridFsResource resource = new GridFsResource(gridFsFile, bucket.openDownloadStream(gridFsFile.getObjectId()));

        OutputStream stream = response.getOutputStream();
        stream.write(IOUtils.toByteArray(resource.getInputStream()));
        stream.flush();
        stream.close();
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return index页面
     */
    @DeleteMapping(value = "delete/{id}")
    public ModelAndView deleteById(@PathVariable String id) {
        mongoFsService.deleteById(id);

        return index(null);
    }

}
