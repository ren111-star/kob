package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("all")
    public List<User> getAll () {
        return userMapper.selectList(null);
    }

    @GetMapping("select/{userid}")
    public User getUser (@PathVariable int userid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userid);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("insert/{userid}/{username}/{password}")
    public String insertUser (@PathVariable int userid,
                              @PathVariable String username,
                              @PathVariable String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        userMapper.insert(new User(userid,username,encode));
        return "success";
    }
    @GetMapping("delete/{userid}")
    public String deleteUser (@PathVariable int userid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userid);
        userMapper.delete(queryWrapper);
        return "success";
    }
}
