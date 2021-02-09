package com.amos.fs.mongo.web;

import com.amos.common.dto.response.MultiResponse;
import com.amos.common.dto.response.Response;
import com.amos.common.dto.response.SingleResponse;
import com.amos.fs.api.enums.FsErrorCodeEnum;
import com.amos.fs.mongo.common.constant.MongoFsConstant;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.model.vo.MongoFsVO;
import com.amos.fs.mongo.service.MongoFsService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Optional;

/**
 * DESCRIPTION: MongoFsController
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Api(tags = "文件操作")
@Controller
@RequestMapping("mongo")
public class MongoFsController {

    @Resource
    private MongoFsService mongoFsService;
    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;


    @ResponseBody
    @PostMapping("uploadOne")
    @ApiOperation("单个文件上传")
    public Response uploadOne(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return SingleResponse.ofFail(FsErrorCodeEnum.FS_BASE_isEmpty);
        }

        String objectId = mongoFsService.upload(file);
        if (objectId == null) {
            return SingleResponse.ofFail();
        }

        return SingleResponse.ofSuccess();
    }

    @ResponseBody
    @PostMapping("uploadMany")
    @ApiOperation("批量文件上传")
    public SingleResponse<String> uploadMany(MultipartFile[] files) {
        if (files == null) {
            return SingleResponse.ofFail(FsErrorCodeEnum.FS_BASE_isEmpty);
        }

        mongoFsService.upload(files);

        return SingleResponse.ofSuccess();
    }

    /**
     * 读取文件
     *
     * @param id       文件ID
     * @param response HttpServletResponse
     * @throws Exception 读取文件异常
     */
    @GetMapping(value = "read/{id}")
    @ApiOperation("根据文件ID读取文件")
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

    @ResponseBody
    @GetMapping("findBy")
    @ApiOperation("根据文件信息查询")
    public MultiResponse<MongoFsVO> findBy(MongoFsForm form) {

        return MultiResponse.ofSuccess(mongoFsService.findBy(form));
    }

    @ResponseBody
    @DeleteMapping(value = "delete/{id}")
    @ApiOperation("根据文件ID删除文件")
    public SingleResponse<String> deleteById(@PathVariable String id) {
        mongoFsService.deleteById(id);

        return SingleResponse.ofSuccess();
    }

}
