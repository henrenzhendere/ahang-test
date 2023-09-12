package com.hang.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hang.Res.ReturnRes;
import com.hang.mapper.GoodMapper;
import com.hang.mapper.OrderMapper;
import com.hang.pojo.*;
import com.hang.service.OrderService;
import com.hang.utils.HttpClientUtil;
import com.hang.utils.JedisUtils;
import com.hang.utils.Jwtutils;
import com.hang.utils.OrderNumberGenerator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.*;

/**
 * @author Ahang
 * @create 2023/6/25 14:04
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    GoodMapper goodMapper;

    /*
     * 待支付界面
     * */
    public Map myOrderToPay(String token, String goodNo) {
        String userName = "";
        String userId = "";
        List addressList = new ArrayList();
        Address address = new Address();
        Good good = null;
        Map<Object, Object> map = new HashMap<Object, Object>();
        JedisUtils jedis = new JedisUtils();
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<Map.Entry<String, String>> iterator = tokenMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> userMap = iterator.next();
                userId = userMap.getValue();
                userName = userMap.getKey();
            }
            int num =Integer.valueOf(jedis.hget(userName, goodNo)) ;
            goodMapper.updateStock(num, goodNo);


            int totalPrice = 0;
            if (jedis.exists(userName)) {
//                address.setUserName(userName);
                Map<String, String> userNameMap = jedis.hgetAll(userName);
                Iterator<Map.Entry<String, String>> userNameEntry = userNameMap.entrySet().iterator();
                while (userNameEntry.hasNext()) {
                    Map.Entry<String, String> next = userNameEntry.next();
                    BigDecimal price = goodMapper.queryPrice(goodNo);
                    if (price==null){
                        System.out.println("没有此商品");
                        price = BigDecimal.valueOf(0.0);
                    }
                    totalPrice = Integer.valueOf(num) * price.intValue();
                }
            }
            addressList = orderMapper.orderToPay(userId);
            address.setUserName(userName);
            address.setAddressList(addressList);
            good = orderMapper.goodToPay(goodNo);
            good.setTotalPrice(totalPrice);
        }
        map.put("address", address);
        map.put("good", good);
        return map;
    }

    @Override
    public List queryAllOrder(String token) {
        List<Object> orderList = new ArrayList<>();
        JedisUtils jedis = new JedisUtils();
        String userId = "";
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<Map.Entry<String, String>> iterator = tokenMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> userMap = iterator.next();
                userId = userMap.getValue();
            }
            orderList = orderMapper.queryAllOrder(Integer.valueOf(userId));
        }
        return orderList;
    }

    @Override
    public Order insertOrder(String token, String goodNo, String address) {
        Order order = new Order();
        String userName = "";
        String userId = "";
        String num = "";
        BigDecimal price = goodMapper.queryPrice(goodNo);
        //订单生成器
        String s = OrderNumberGenerator.generateOrderNumber();
        order.setOrderNo("O" + s);

        // 创建JedisUtils对象
        JedisUtils jedis = new JedisUtils();
        // 通过hget方法直接获取redis中的value:1_德克萨斯
        // 将得到的value以 _ 分割，得到userId 与 userName
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<Map.Entry<String, String>> iterator = tokenMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> userMap = iterator.next();
                userId = userMap.getValue();
                userName = userMap.getKey();
//                address.setUserName(userName);
            }
            if (jedis.exists(userName)) {
                Map<String, String> userNameMap = jedis.hgetAll(userName);
                Iterator<Map.Entry<String, String>> userNameEntry = userNameMap.entrySet().iterator();
                while (userNameEntry.hasNext()) {
                    Map.Entry<String, String> next = userNameEntry.next();
                    num = next.getValue();
                }
            }
            order.setUserId(userId);
//        String phone = order.getPhone();
            order.setAddrMsg(address);
            order.setStatus(1);
            java.sql.Date date = new Date(System.currentTimeMillis());
            order.setCreateTime(date);
            order.setPayTime(date);
            order.setLogisticsNo(s);

            int totalPrice = price.intValue() * (Integer.valueOf(num));
            order.setTotalPrice(BigDecimal.valueOf(totalPrice));
            order.setPayMethod("支付宝");
            orderMapper.insertOrder1(order);
        }
        return order;
    }

    @Override
    public String payByAli(Order order) {
        ReturnRes res;
        String out_trade_no = order.getOrderNo();
        BigDecimal total_amount = order.getTotalPrice();
        String subject = "qq";
        String return_url = "http://localhost:8080/payByAli";// 支付成功前端页面
        String notify_url = "http://localhost:8082/Order/updateOrderStatus";// 支付成功后后端要跳转的的
        String quit_url = "http://localhost:8082/showAllGoods";
        String url = "http://localhost:8083/alipay/pay";
        AliPay aliPay = new AliPay();

        aliPay.setOut_trade_no(out_trade_no);
        aliPay.setTotal_amount(total_amount);
        aliPay.setSubject(subject);
        aliPay.setReturn_url(return_url);
        aliPay.setNotify_url(notify_url);
        aliPay.setQuit_url(quit_url);

        String jsonStr = JSON.toJSONString(aliPay);
        String data = HttpClientUtil.doPostJson1(url, jsonStr);
        return data;
    }


    @Override
    public String payByWeChat2(Order order) throws IOException {
        //1、
        String url = "http://m.jnbat.com:8080/lhwx/WeChatUrl/weChatReceive";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        String subject = "1546";
        String outTradeNo = order.getOrderNo();
        int totalAmount = order.getTotalPrice().intValue();
        String notifyUrl = "http://m.jnbat.com:8080/lhshop/order/updateOrderStatus";
        String returnUrl = "http://m.jnbat.com/payByWeChat";

        WeChatPay2 weChatPay2 = new WeChatPay2();
        weChatPay2.setSubject(subject);
        weChatPay2.setOut_trade_no(outTradeNo);
        weChatPay2.setTotal_amount(totalAmount);
        weChatPay2.setNotify_url(notifyUrl);
        weChatPay2.setReturn_url(returnUrl);

        String jsonString = JSON.toJSONString(weChatPay2);

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
//        Map<String, Object> resultMap = JSON.parseObject(bodyAsString, HashMap.class);
//        Gson gson = new Gson();
//        Map<String,String> data =(Map<String, String>) resultMap.get("data");
//        System.out.println("mweb_url====="+data.get("mweb_url"));

        Gson gson = new Gson();
        Map<String,Object> o = gson.fromJson(jsonString,HashMap.class);
        Map<String,String> m = (Map<String, String>) o.get("data");
        String mwebUrl = m.get("mweb_url");
        System.out.println("mweburl===" + mwebUrl);
        return mwebUrl;
    }

    @Override
    public ReturnRes updateOrderStatus(WeChatPay2 weChatPay2) {
        DateTime date = new DateTime(weChatPay2.getPayTime());
        String outTradeNo = weChatPay2.getOut_trade_no();
        ReturnRes res = new ReturnRes();

        System.out.println("*********************************更新数据库订单状态");
        int i = orderMapper.updateOrderStatus(outTradeNo, date);
        if (i == 0) {
            res.setMessage("支付失败");
            res.setCode("0");
        } else {
            res.setCode("1");
            res.setMessage("支付成功");
        }
        return res;
    }

    @Override
    public int queryStatus(String orderNo) {
        return 0;
    }

    /*
     * 删除购物车中的商品
     * */

}
