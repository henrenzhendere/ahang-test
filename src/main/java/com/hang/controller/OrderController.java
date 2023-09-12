package com.hang.controller;

import cn.hutool.db.Page;
import com.hang.Req.PageReq;
import com.hang.Res.ReturnRes;
import com.hang.Res.shopCarRes;
import com.hang.pojo.*;
import com.hang.service.GoodService;
import com.hang.service.OrderService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @author Ahang
 * @create 2023/6/19 17:06
 */


@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
     * 待支付
     * */
    @RequestMapping("/myOrderToPay")
    public ReturnRes myOrderToPay(HttpServletRequest request, @RequestBody PageReq req) {
        String token = request.getHeader("token");
        ReturnRes res = new ReturnRes();
        String goodNo = req.getGoodNo();
        HashMap myOrderToPay = (HashMap) orderService.myOrderToPay(token, goodNo);
        res.setData(myOrderToPay);
        return res;
    }


    @RequestMapping("prepareInsert")
    public ReturnRes prepareInsert(@RequestBody PageReq req,HttpServletRequest request){
        String flag = req.getFlag();
        String address = req.getAddress();
        String goodNo = req.getGoodNo();
        String token = request.getHeader("token");
        String url = "http://localhost:8082/order/insertOrderByAli?goodNo="+goodNo+"&"+"address="+address+"&"+"flag="+flag+"&"+"token="+token;
        ReturnRes res = new ReturnRes();
        res.setCode("1");
        res.setData(url);
        return res;
    }
    /*
     * 生成订单
     * */
    @RequestMapping("/insertOrderByAli")
    public String insertOrderByAli(PageReq req, HttpServletRequest request) throws IOException {
        ReturnRes res = new ReturnRes();
        String address = req.getAddress();
        String goodNo = req.getGoodNo();
        String flag = req.getFlag();
        String token = req.getToken();
//        String token = request.getHeader("token");
        System.out.println("开始插入订单***************************************************");
        Order order = orderService.insertOrder(token, goodNo, address);
        System.out.println("插入订单完成*********************************************");
        if (flag.equals("ali"))
            //调用支付宝接口
            System.out.println("开始调用支付宝接口");
            String data = orderService.payByAli(order);
            System.out.println("调用支付宝接口完成");
            return data;
    }
    @RequestMapping("/insertOrderByWeChat")
    public ReturnRes insertOrderByWeChat(PageReq req, HttpServletRequest request) throws IOException {
        ReturnRes res = new ReturnRes();
        String address = req.getAddress();
        String goodNo = req.getGoodNo();
        String flag = req.getFlag();
        String token = req.getToken();
//        String token = request.getHeader("token");
        System.out.println("开始插入订单***************************************************");
        Order order = orderService.insertOrder(token, goodNo, address);
        System.out.println("插入订单完成*********************************************");
            //调用微信接口
            System.out.println("开始调用微信接口*********************************************");
            String s = orderService.payByWeChat2(order);
            System.out.println("调用微信完成*******************************************");
            if (s == null||s.equals("")){
                res.setMessage("提交失败");
                res.setCode("0");
            }else {
                res.setData(s);
                res.setMessage("提交成功");
                res.setCode("1");
            }
            return res;
    }


    //调用微信接口时，设置的url微信自动调用
    @RequestMapping("/updateOrderStatus")
    public ReturnRes insertOrder(@RequestBody WeChatPay2 weChatPay2) {
        System.out.println("*********************商城回调接口");
        ReturnRes res = orderService.updateOrderStatus(weChatPay2);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&更新状态完成");
        //调用支付宝接口
//        ReturnRes res = orderService.payByAli(order);
        //调用微信接口
        res.setCode("1");
        return res;
    }

    @RequestMapping("/queryAllOrder")
    public ReturnRes queryAllOrder(HttpServletRequest req){
        ReturnRes res = new ReturnRes();
        String token = req.getHeader("token");
        List orderList = orderService.queryAllOrder(token);
         if (orderList == null){
            res.setMessage("查询失败");
            res.setCode("0");
        }else {
            res.setData(orderList);
            res.setMessage("查询成功");
            res.setCode("1");
        }
        return res;
    }

}
