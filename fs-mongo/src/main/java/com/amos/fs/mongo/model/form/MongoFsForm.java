package com.amos.fs.mongo.model.form;

import com.amos.fs.mongo.common.enums.FileTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DESCRIPTION: MongoFile查询表单
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Data
public class MongoFsForm {

    @ApiModelProperty("文件名字")
    private String realName;

    @ApiModelProperty("文件类型")
    private FileTypeEnum fileTypeEnum;

}
