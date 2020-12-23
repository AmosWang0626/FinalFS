package com.amos.fs.minio.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 模块名称: FinalFS
 * 模块描述: IndexController
 *
 * @author amos.wang
 * @date 2020/12/23 9:50
 */
@Controller
public class IndexController {

    /**
     * 跳转到 index页面
     *
     * @return index页面
     */
    @GetMapping
    public String index() {
        return "index";
    }

}
