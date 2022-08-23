package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 前后端不分离
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index () {
        return "/pk/index.html";
    }
}
