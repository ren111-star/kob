package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRanklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRanklistServiceImpl implements GetRanklistService {
    private final UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<User> iPage = new Page<>(page, 13);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(iPage, queryWrapper).getRecords();
        for (User user : users) {
            user.setPassword("");
        }
        JSONObject resp = new JSONObject();
        resp.put("users", users);
        resp.put("user_count", userMapper.selectCount(null));
        return resp;
    }
}
