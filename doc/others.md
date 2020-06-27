## 附录
- spring boot 2.3 设置 `favicon.ico`

  将要设置的图标替换 `resources/public/favicon.ico` 即可

- 设置[自定义控制台输出](http://patorjk.com/software/taag/)

  将要设置的内容写入 `resources/banner.txt` 即可


---
### Windows 安装 MongoDB
- 下载安装包，傻瓜式安装
https://www.mongodb.com/try/download/community

- 进入安装目录下的 bin 目录

- 启动
  > 注意是 mongod，不是 mongo
   - 执行命令：`mongod -dbpath E:\app_res\mongodb\data`
   - 必须加上 `--dbpath`
   - [启动脚本](doc/mongo_start.bat)

- 测试启动
    - http://localhost:27017/

- 查看操作命令
    - 执行命令：`mongo`
    - 查看帮助：`help`

---
[好玩的网站](http://patorjk.com/)
```
                     ..._  ,-'``; 
                   ,`     \`-----'.. 
                   ,\          .~ ` - . 
                  ,'               o    |__ 
                _|                        (#) 
              _\  '`~-.                   ,' 
             ,\   ,.-~-.' -.,       .'--~` 
            /   /         }   ` -..,/ 
          /  ,'___    :/           \ 
        /'`-.|      `'-..'........      \ 
      ;      \                   )-....| 
     |         ' ---...........-'      ,' 
     ',    ,......                   ,' 
       ' ,/        `,              ,' 
         \           \       ,.- ' 
          ',          ',-~'`  ;                ,======, 
           |          ;      /__            ,' -------  ', 
          /          /__        )            \ ======== / 
          '-.             )----~'             \________/ 
             ' - .......-``
```