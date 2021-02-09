# Fs-Minio

> 项目中使用时，经过技术选型后，选用了 Minio，简单好用才是王道。

### Windows 运行 Minio

- `minio server E:\study\minio\workspace`
- 详情：[run.bat](run.bat)

### 设置指定 bucket 公共读权限

- `mc policy set public minio/amos`
- 详情：[mc.bat](mc.bat)
