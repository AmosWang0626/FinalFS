package com.amos.fs.mongo.service.impl;

import com.amos.common.util.date.DateUtils;
import com.amos.fs.api.exception.FsException;
import com.amos.fs.mongo.common.constant.MongoFsConstant;
import com.amos.fs.mongo.common.enums.FileTypeEnum;
import com.amos.fs.mongo.common.util.ExtensionUtils;
import com.amos.fs.mongo.model.form.MongoFsForm;
import com.amos.fs.mongo.model.vo.MongoFsVO;
import com.amos.fs.mongo.service.MongoFsService;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * DESCRIPTION: MongoFileService
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/6/19
 */
@Service("mongoFileService")
public class MongoFsServiceImpl implements MongoFsService {

    @Resource
    private GridFsTemplate gridFsTemplate;
    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Override
    public String uploadFile(MultipartFile file) {
        // 文件原始名字
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        // 新的文件名字
        String filename = System.currentTimeMillis() + "." + extension;
        // 文件类型 (适配不支持类型)
        AtomicReference<String> contentType = new AtomicReference<>(file.getContentType());
        ExtensionUtils.fixContentType(extension).ifPresent(contentType::set);

        Document document = new Document()
                .append(MongoFsConstant.FILENAME, originalFilename)
                .append(MongoFsConstant.CONTENT_TYPE, contentType.get())
                .append(MongoFsConstant.FILE_TYPE, FileTypeEnum.getFileType(originalFilename));

        try {
            return gridFsTemplate.store(file.getInputStream(), filename, contentType.get(), document).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public GridFsResource findById(String id) {
        GridFSFile gridFsFile = gridFsTemplate.findOne(query(where("_id").is(id)));
        /// 根据 filename 查找：query(GridFsCriteria.whereFilename().is(filename))
        if (Objects.isNull(gridFsFile)) {
            throw new FsException("文件不存在");
        }

        GridFSBucket bucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());

        return new GridFsResource(gridFsFile, bucket.openDownloadStream(gridFsFile.getObjectId()));
    }

    @Override
    public List<MongoFsVO> findBy(MongoFsForm form) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("uploadDate")));
        if (form != null) {
            if (StringUtils.isNotBlank(form.getRealName())) {
                query.addCriteria(GridFsCriteria.whereMetaData(MongoFsConstant.FILENAME).regex(".*?" + form.getRealName() + ".*"));
            }
            if (form.getFileTypeEnum() != null) {
                query.addCriteria(GridFsCriteria.whereMetaData(MongoFsConstant.FILE_TYPE).is(form.getFileTypeEnum().name()));
            }
        }

        List<MongoFsVO> list = new ArrayList<>();

        MongoCursor<GridFSFile> iterator = gridFsTemplate.find(query).iterator();
        iterator.forEachRemaining(gridFsFile -> {
            // 基础信息
            MongoFsVO mongoFsVO = new MongoFsVO()
                    .setId(gridFsFile.getObjectId().toString())
                    .setFilename(gridFsFile.getFilename())
                    .setUploadDate(DateUtils.getDateTime(gridFsFile.getUploadDate()));

            // 附加信息
            Optional.ofNullable(gridFsFile.getMetadata()).ifPresent(document -> {
                String filename = document.getString(MongoFsConstant.FILENAME);
                String contentType = document.getString(MongoFsConstant.CONTENT_TYPE);
                String fileType = FileTypeEnum.valueOf(document.getString(MongoFsConstant.FILE_TYPE)).getName();

                mongoFsVO.setRealName(filename).setFileType(fileType).setContentType(contentType);
            });

            list.add(mongoFsVO);
        });

        return list;
    }

    @Override
    public void deleteById(String id) {
        gridFsTemplate.delete(query(where("_id").is(id)));
    }

}
