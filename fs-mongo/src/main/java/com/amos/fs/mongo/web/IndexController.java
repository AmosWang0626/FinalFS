package com.amos.fs.mongo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * PROJECT: FinalFS
 * DESCRIPTION: note
 *
 * @author daoyuan
 * @date 2020/6/26 13:19
 */
@Controller
public class IndexController {

    @GetMapping
    public String index() {
        return "redirect:/mongo";
    }

}
