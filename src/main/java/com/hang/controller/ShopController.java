package com.hang.controller;


import com.hang.Req.PageReq;
import com.hang.Res.LoginRes;
import com.hang.Res.ReturnRes;
import com.hang.Res.ShopQueryPageRes;
import com.hang.Res.shopCarRes;
import com.hang.pojo.Good;
import com.hang.pojo.ShopCar;
import com.hang.pojo.User;
import com.hang.service.GoodService;
import com.hang.service.UserService;
import com.hang.utils.JedisUtils;
import com.hang.utils.Jwtutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/19 17:06
 */


@CrossOrigin
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private UserService userService;


    /*
    * 查询所有商品
    * */
    @RequestMapping("/showAllGoods")
    private ShopQueryPageRes showAllGoods(@RequestBody PageReq pageReq){
        ShopQueryPageRes res = new ShopQueryPageRes();

        PageReq req = goodService.queryPage(pageReq);
        int begin = req.getBegin();
        int pageSizeInt = req.getPageSizeInt();
        int count = req.getCount();

        List<Good> goodList = goodService.selectAllGoods(begin, pageSizeInt);
        res.setCount(count);
        res.setGoodList(goodList);
        return res;
    }

    /*
    * 查看货物详情
    * */

    @RequestMapping("/showGoodDetail/{goodNo}")
    public ReturnRes showGoodDetail(@PathVariable String goodNo){
        ReturnRes res = new ReturnRes();
        Good showGoodDetail = goodService.showGoodDetail(goodNo);
        res.setGood(showGoodDetail);
        return res;
    }

    /*
    * 登录
    * */

    @RequestMapping("/login")
    public LoginRes login(@RequestBody User user) {
        LoginRes res = new LoginRes();
        user = userService.login(user.getUserName(),user.getPassword());

        if (user == null || "".equals(user)) {
            res.setMessage("登陆失败");
            res.setCode("0");
        } else {
            res.setCode("1");
            res.setMessage("欢迎登陆");
            String token = Jwtutils.jwtToken(user.getUserName());
            res.setToken(token);
            JedisUtils jedis = new JedisUtils();
            jedis.hset(token,user.getUserName(),String.valueOf(user.getUserId()));
        }
        return res;
    }

    /*
    * 查看购物车
    * */
    @RequestMapping("/myShopCar")
    public shopCarRes shopCar(HttpServletRequest req){
        String token = req.getHeader("token");
        shopCarRes res = new shopCarRes();
        List<ShopCar> shopCarGoodList = goodService.shopCarGoodList(token);
        res.setShopCarGoodList(shopCarGoodList);
        return res;
    }

    /*
    * 添加购物车
    * */
    @RequestMapping("/addCar")
    public shopCarRes shopCar(@RequestBody ShopCar shopCar, HttpServletRequest req){
        shopCarRes res = new shopCarRes();
        String token = req.getHeader("token");
        int i = goodService.addShopCar(shopCar, token);
        if (i ==0) {
            res.setMessage("添加失败,请先登录");
            res.setCode("0");
        } else {
            res.setCode("1");
            res.setMessage("加入成功");
        }
        return res;
    }


    /*
    * 更新购物车内商品数量
    * */
    @RequestMapping("/updateNum")
    public shopCarRes updateNum(HttpServletRequest req,@RequestBody PageReq pageReq){
        String token = req.getHeader("token");
        shopCarRes res = new shopCarRes();
        String goodNo = pageReq.getGoodNo();
        String flag = pageReq.getFlag();
        int i = goodService.updateNum(token, goodNo, flag);
        if (i == 0 ) {
            res.setMessage("更新失败");
            res.setCode("0");
        } else {
            res.setCode("1");
            res.setMessage("更新成功");
        }
        return res;
    }

/*
* 删除购物车内商品
* */
@RequestMapping("/delete")
    public shopCarRes delete(HttpServletRequest req,@RequestBody PageReq pageReq){
    String token = req.getHeader("token");
    shopCarRes res = new shopCarRes();
    String goodNo = pageReq.getGoodNo();
    goodService.delete(token,goodNo);
    if (goodNo == null || "".equals(goodNo)) {
        res.setMessage("删除失败");
        res.setCode("0");
    } else {
        res.setCode("1");
        res.setMessage("删除成功");
    }
    return res;
}

/*
* 更新商品库存
* */
    @RequestMapping("/updateStock")
    public ReturnRes updateStock(@RequestParam String goodNo,HttpServletRequest req){
        ReturnRes res = new ReturnRes();
        String token = req.getHeader("token");
        int i = goodService.updateStock(token,goodNo);
        if (i==1){
            res.setCode("1");
            res.setMessage("下单成功");
        }else {
            res.setCode("0");
            res.setMessage("库存不足");
        }
        return res;
    }
}
