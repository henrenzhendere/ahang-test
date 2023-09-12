package com.hang.service;

import cn.hutool.http.HttpRequest;
import com.hang.Res.ReturnRes;
import com.hang.pojo.Order;
import com.hang.pojo.WeChatPay2;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Ahang
 * @create 2023/6/25 14:04
 */
public interface OrderService {

    /*
    * 待支付界面
    * */
    Map myOrderToPay(String token, String goodNo);

    /*
    * 查看订单
    * */
    List queryAllOrder(String token);
    /*
    *点击结算，生成一条订单
    * */
    Order insertOrder(String token,String goodNo,String address);

    /*
    * 支付宝
    * */
    String payByAli(Order order);

    /*
    * 公司网关 调微信
    * */
    String payByWeChat2(Order order) throws IOException;


    /*
    * 更改订单状态
    * */
    ReturnRes updateOrderStatus(WeChatPay2 weChatPay2);

    int queryStatus(String orderNo);
}
