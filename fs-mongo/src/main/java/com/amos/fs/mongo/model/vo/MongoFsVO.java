package com.amos.fs.mongo.model.vo;

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

    private String id;
    private String realName;
    private String filename;
    private String fileType;
    private String contentType;
    private String uploadDate;

}
