package com.hang.service;

import com.hang.pojo.User;

/**
 * @author Ahang
 * @create 2023/6/22 14:09
 */
public interface UserService {

    /*
    * 用户登录
    * */
    User login(String userName, String password);
}
