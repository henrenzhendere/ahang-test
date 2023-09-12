package com.jnbat.shop.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xjy
 * @create 2023-06-27 10:47
 */
public class WxPayUtil {
    private static String WxPayH5Url = "http://47.94.220.67:8080/Pay/wxPays";
    private static String orderSubject = "aaa";
    private static String returnUrl = "http://001225xjy.e1.luyouxia.net:24237/Success";
    private static String notifyUrl = "http://001225xjy.e1.luyouxia.net:24248/";


    public static String getAliPayData(String orderNo, BigDecimal totalPrice) throws IOException {
        Map paramsMap = new HashMap();

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(WxPayH5Url);

        paramsMap.put("out_trade_no","564788");
        paramsMap.put("total_amount",1);
        paramsMap.put("subject","aaa");
        paramsMap.put("return_url",returnUrl);
        paramsMap.put("notify_url",notifyUrl);

        String jsonString = JSON.toJSONString(paramsMap);
        System.out.println("请求参数 ===> " + jsonString);
        StringEntity entity = new StringEntity(jsonString, StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json;charset=utf-8");
        CloseableHttpResponse response = client.execute(httpPost);
        String bodyAsString = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode ===> " + statusCode);
        System.out.println("entity ===> " + response.getEntity());
        System.out.println(bodyAsString);
        //Map<String,String> resultMap = JSON.parseObject(bodyAsString, HashMap.class);
        //String data = resultMap.get("data");

        return bodyAsString;
    }
}
