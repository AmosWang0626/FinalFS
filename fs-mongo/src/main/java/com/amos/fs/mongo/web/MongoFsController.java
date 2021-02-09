package com.amos.fs.mongo.web;

import com.amos.common.dto.response.MultiResponse;
import com.amos.common.dto.response.Response;
import com.amos.common.dto.response.SingleResponse;
import com.amos.fs.api.enums.FsErrorCodeEnum;
import com.amos.fs.api.exception.FsException;
import com.amos.fs.mongo.common.constant.MongoFsConstant;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.model.vo.MongoFsVO;
import com.amos.fs.mongo.service.MongoFsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Objects;
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


    @ResponseBody
    @PostMapping("upload")
    @ApiOperation("单个文件上传")
    public Response upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return SingleResponse.ofFail(FsErrorCodeEnum.FS_BASE_isEmpty);
        }

        String objectId = mongoFsService.uploadFile(file);
        if (objectId == null) {
            return SingleResponse.ofFail();
        }

        return SingleResponse.ofSuccess();
    }

    @ResponseBody
    @PostMapping("multiUpload")
    @ApiOperation("批量文件上传")
    public SingleResponse<String> multiUpload(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return SingleResponse.ofFail(FsErrorCodeEnum.FS_BASE_isEmpty);
        }

        mongoFsService.uploadFile(files);

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
        GridFsResource resource = mongoFsService.findById(id);
        if (Objects.isNull(resource.getGridFSFile())) {
            throw new FsException("文件不存在");
        }

        Optional.ofNullable(resource.getGridFSFile().getMetadata())
                .ifPresent(document -> response.setContentType(document.getString(MongoFsConstant.CONTENT_TYPE)));
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
