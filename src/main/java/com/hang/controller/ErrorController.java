package com.hang.controller;

import com.hang.Res.LoginRes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Proxy;

/**
 * @author Ahang
 * @create 2023/6/24 17:40
 */
@RestController
@RequestMapping("/shop")
public class ErrorController {

    @RequestMapping("/goLogin")
    public LoginRes goLogin(){
        LoginRes res = new LoginRes();
        res.setCode("11");
        return res;
    }
    public void aa(HttpServletResponse response){
    }

}
