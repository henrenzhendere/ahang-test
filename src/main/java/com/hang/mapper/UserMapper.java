package com.hang.mapper;

import com.hang.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Ahang
 * @create 2023/6/22 15:30
 */

@Repository
public interface UserMapper {
    /*
    * 用户登录
    * */

    User Login(@Param("userName")String userName, @Param("password")String password);
}
