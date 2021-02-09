# FinalFS

![FinalFS](doc/FinalFS.png)

> 项目中用到了文件管理，选了几个业界流行的方案，实践一下。

---

## 三种方案

- sftp
- mongo
- minio

---

## 概述

### 1. fs-sftp

> 项目中原有的方案，聊胜于无，较原始

基于 sftp & commons-pool2 实现。

**实现的功能**：文件上传、文件查询、文件读取

### 2. fs-mongo

> 实用性强，但是不适合存放大量小文件（也就是16MB以下的文件）

使用 Mongo GridFS 实现。详细说明：[doc/mongo](doc/mongo)

**实现的功能**：文件上传、文件检索、文件读取、文件删除

### 3. fs-minio

> 最终的技术选型，Minio 简单好用，权限管理比较方便

fs-minio 模块，运用 Minio 实现。详细说明：[doc/minio](doc/minio)

**实现的功能**：文件上传、文件读取、创建bucket、设置bucket公共读权限、批量删除、查找指定bucket、查看bucket下所有文件

---

[附录](doc/others.md)