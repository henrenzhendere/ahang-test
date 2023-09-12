package com.hang.service;


import com.hang.Req.PageReq;
import com.hang.Res.ReturnRes;
import com.hang.pojo.Good;
import com.hang.pojo.Order;
import com.hang.pojo.ShopCar;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/19 17:54
 */
public interface GoodService {
    //查询所有商品信息
    List<Good> selectAllGoods(int begin, int pageSizeInt);

    //查询商品数量用于分页
    int queryCount();

    //查看商品详情
    Good showGoodDetail(String goodNo);

    //查看购物车
    List<ShopCar> shopCarGoodList(String token);

    //添加购物车
    int addShopCar(ShopCar shopCar,String token);

    //更新购物车中的数量
    int updateNum(String token,String goodNo,String flag);
    //获取redis中的商品数量

    //删除购物车内商品
    void delete(String token ,String goodNo);

    BigDecimal queryPrice(String goodNo);

    PageReq queryPage(PageReq pageReq);

    int updateStock(String token,String goodNo);
}
