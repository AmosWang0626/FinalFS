package com.amos.fs.mongo.model.form;

import com.amos.fs.mongo.common.enums.FileTypeEnum;
import lombok.Data;

/**
 * DESCRIPTION: MongoFile查询表单
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Data
public class MongoFsForm {
    /**
     * 文件名字
     */
    private String realName;
    /**
     * 文件类型
     */
    private FileTypeEnum fileTypeEnum;

}
