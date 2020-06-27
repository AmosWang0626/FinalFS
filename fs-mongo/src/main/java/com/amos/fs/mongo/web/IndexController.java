package com.amos.fs.mongo.web;

import com.amos.fs.mongo.common.enums.FileTypeEnum;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.service.MongoFsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT: FinalFS
 * DESCRIPTION: note
 *
 * @author daoyuan
 * @date 2020/6/26 13:19
 */
@ApiIgnore
@Controller
public class IndexController {

    @Resource
    private MongoFsService mongoFsService;

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
     * 批量文件上传
     *
     * @param files 批量文件上传
     * @return 重定向到 index页面
     */
    @ApiIgnore
    @PostMapping("upload")
    public String upload(MultipartFile[] files) {
        mongoFsService.upload(files);

        return "redirect:";
    }

}
