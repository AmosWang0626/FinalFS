package com.amos.fs.mongo.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DESCRIPTION: MongoFileVO
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Data
@Accessors(chain = true)
public class MongoFsVO {

    @ApiModelProperty("文件ID")
    private String id;

    @ApiModelProperty("文件真实名字")
    private String realName;

    @ApiModelProperty("文件别名")
    private String filename;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件编码类型")
    private String contentType;

    @ApiModelProperty("上传时间")
    private String uploadDate;

}
