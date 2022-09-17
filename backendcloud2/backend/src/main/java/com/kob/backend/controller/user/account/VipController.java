package com.kob.backend.controller.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VipController {
    @Autowired
    VipService vipService;

    @GetMapping("/api/user/account/vip/")
    public List<User> gteAllUsers() {
        return vipService.SelectAllUsers();
    }
}
