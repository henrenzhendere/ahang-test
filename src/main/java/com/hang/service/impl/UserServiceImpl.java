package com.hang.service.impl;

import com.hang.mapper.UserMapper;
import com.hang.pojo.User;
import com.hang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ahang
 * @create 2023/6/22 14:10
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User login(String userName, String password) {
        User login = userMapper.Login(userName, password);
        return login;
    }
}
