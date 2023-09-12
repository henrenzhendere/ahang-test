package com.hang.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ahang
 * @create 2023/5/9 10:11
 */

//@WebFilter("/*")
public class AMyCorsFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin","*");//允许访问域名
        response.setHeader("Access-Control-Allow-Methods","*");//允许访问请求
        response.setHeader("Access-Control-Max-Age","3600");//缓存时间
        response.setHeader("Access-Control-Allow-Headers","content-type,token");//自定义的请求头
        filterChain.doFilter(req,res);
    }

    public void destroy() {
    }
}
