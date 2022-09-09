package com.kob.backend.service.impl.user.account;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class vipServiceImpl implements VipService {
    @Autowired
    UserMapper userMapper;
    @Override
    public List<User> SelectAllUsers() {
        return userMapper.selectList(null);
    }
}
