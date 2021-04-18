package com.smile.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smile.server.mapper.UserMapper;
import com.smile.server.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User findUser(String mobile,String password){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        queryWrapper.eq("password",password);
        return userMapper.selectOne(queryWrapper);
    }

}
