package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 前后端不分离
@Controller
@RequestMapping("/pk/")  // 表示父目录
public class IndexController {
    @RequestMapping("index/")
    public String index () {
        return "/pk/index.html";
    }
}
