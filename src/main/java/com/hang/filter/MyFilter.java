package com.hang.filter;

import com.google.gson.Gson;
import com.hang.Res.LoginRes;
import com.hang.utils.Jwtutils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//@WebFilter("/*")
public class MyFilter implements Filter {
    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        String uri = req.getRequestURI();

        if(uri.endsWith("/shop/showAllGoods") || "/shop/goLogin".equals(uri) || "/shop/login".equals(uri) || uri.startsWith("/shop/showGoodDetail")){
            resp.setContentType("text/html;charset=utf-8");
            filterChain.doFilter(req,resp);
            return;
        }

        String token = req.getHeader("token");
        if (token == null || "".equals(token)){
            System.out.println("token ===> " + token);
            PrintWriter writer = resp.getWriter();
            LoginRes res = new LoginRes();
            res.setCode("11");
            Gson gson = new Gson();
            String str = gson.toJson(res);
            writer.write(str);
            writer.flush();
            writer.close();
            return;

//            req.getRequestDispatcher("/shop/goLogin");
//            return;
        }

        if(Jwtutils.verifyToken(token)){
            resp.setContentType("application/json;charset=utf-8");
            filterChain.doFilter(req,resp);
            return;
        }else {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write(0);
            writer.flush();
            writer.close();
        }
    }
    public void destroy() {

    }
}
