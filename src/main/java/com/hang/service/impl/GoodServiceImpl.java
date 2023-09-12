package com.hang.service.impl;

import com.hang.MyException.MyException;
import com.hang.Req.PageReq;
import com.hang.mapper.GoodMapper;
import com.hang.pojo.Good;
import com.hang.pojo.ShopCar;
import com.hang.service.GoodService;
import com.hang.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Ahang
 * @create 2023/6/19 17:54
 */

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;

    /*
     * 查询所有货物
     * */
    public List<Good> selectAllGoods(int begin, int pageSizeInt) {
        List<Good> goodList = goodMapper.selectAllGoods(begin, pageSizeInt);
        return goodList;
    }


    /*
     * 商品数量
     * */
    public int queryCount() {
        int count = goodMapper.queryCount();
        return count;
    }

    /*
     * 查看商品详情
     * */

    public Good showGoodDetail(String goodNo) {
        Good good = goodMapper.showGoodDetail(goodNo);
        return good;
    }

    /*
     * 查看购物车
     * */

    public List<ShopCar> shopCarGoodList(String token) {
        String goodNo = "";
        String num = "";
        String userName = "";
        List<ShopCar> shopCarList = new ArrayList<ShopCar>();

        JedisUtils jedis = new JedisUtils();
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<String> iterator = tokenMap.keySet().iterator();
            while (iterator.hasNext()) {
                userName = iterator.next();
            }
            if (jedis.exists(userName)) {
                Map<String, String> userNameMap = jedis.hgetAll(userName);
                Iterator<Map.Entry<String, String>> iterator1 = userNameMap.entrySet().iterator();
                while (iterator1.hasNext()) {
                    Map.Entry<String, String> entry = iterator1.next();
                    goodNo = entry.getKey();
                    Good good = goodMapper.shopCarGood(goodNo);
                    num = entry.getValue();
                    ShopCar shopCar1 = new ShopCar();
                    shopCar1.setGood(good);
                    shopCar1.setNum(num);
                    shopCarList.add(shopCar1);
                }
            }
        }
        return shopCarList;
    }

    /*
     * 添加购物车
     * */

    public int addShopCar(ShopCar shopCar, String token) {
        JedisUtils jedis = new JedisUtils();
        String userName = "";

        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<String> iterator = tokenMap.keySet().iterator();
            while (iterator.hasNext()) {
                userName = iterator.next();
            }
            if (jedis.exists(userName)) {
                if (jedis.hget(userName, shopCar.getGoodNo()) != null) {
                    jedis.hincrBy(userName, shopCar.getGoodNo(), 1);
                } else {
                    jedis.hset(userName, shopCar.getGoodNo(), "1");
                }
            } else {
                jedis.hset(userName, shopCar.getGoodNo(), "1");
            }
            return 1;
        }
        return 0;
    }

    /*
     * 更新数量
     * */
//        String userName,String goodNo,String flag
    public int updateNum(String token, String goodNo, String flag) {
        String userName = "";
        JedisUtils jedis = new JedisUtils();
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<String> iterator = tokenMap.keySet().iterator();
            while (iterator.hasNext()) {
                userName = iterator.next();
            }
            if (jedis.hexists(userName, goodNo)) {
                if ("incr".equals(flag)) {
                    jedis.hincrBy(userName, goodNo, 1);
                    return 1;
                } else if ("decr".equals(flag)) {
                    if (Integer.valueOf(jedis.hget(userName, goodNo)) >= 2) {
                        jedis.hincrBy(userName, goodNo, -1);
                        return 1;
                    } else {
                        delete(token, goodNo);
                    }
                }
            }
        }
        return 0;
    }

    /*
     * 删除购物车中的商品
     * */
    public void delete(String token, String goodNo) {
        String userName = "";
        JedisUtils jedis = new JedisUtils();
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<String> iterator = tokenMap.keySet().iterator();
            while (iterator.hasNext()) {
                userName = iterator.next();
            }
            if (jedis.exists(userName)) {
                if (jedis.exists(userName)) {
                    jedis.hdel(userName, goodNo);
                }
            }
        }
    }

    /*
     * 查询购物车中的某个商品的价格
     * */
    public BigDecimal queryPrice(String goodNo) {
        BigDecimal price = goodMapper.queryPrice(goodNo);
        return price;
    }

    @Override
    public PageReq queryPage(PageReq pageReq) {
        PageReq req = new PageReq();
        String pageSize = pageReq.getPageSize();
        String pageNo = pageReq.getPageNo();
        String flag = pageReq.getFlag();

        int count = goodMapper.queryCount();
        int pageNoInt = 0;
        int pageSizeInt = 0;
        if (pageNo == null || "".equals(pageNo)) {
            pageNoInt = 1;
//            如果前端传过来的pageNo为null，则默认是第一页
        } else {
//            如果不是null，我们就根据操作来判断是上一页还是下一页
            pageNoInt = Integer.valueOf(pageNo);

            if ("next".equals(flag)) {
                pageNoInt++;
            } else if ("pre".equals(flag)) {
                pageNoInt--;
            }
        }

        if (pageSize == null || "".equals(pageSize)) {
            pageSizeInt = 3;
        } else {
            pageSizeInt = Integer.valueOf(pageSize);
        }
        int begin = pageSizeInt * (pageNoInt - 1);
        req.setBegin(begin);
        req.setPageSizeInt(pageSizeInt);
        req.setCount(count);
        req.setPageNoInt(pageNoInt);
        return req;
    }

    @Override
    public int updateStock(String token, String goodNo) {
        String num = "";
        String userName = "";
        JedisUtils jedis = new JedisUtils();
        if (jedis.exists(token)) {
            Map<String, String> tokenMap = jedis.hgetAll(token);
            Iterator<String> iterator = tokenMap.keySet().iterator();
            while (iterator.hasNext()) {
                userName = iterator.next();
            }
            if (jedis.exists(userName)) {
                num = jedis.hget(userName, goodNo);
            }
        }
        goodMapper.updateStock(Integer.valueOf(num),goodNo);
        int i = goodMapper.queryStock(goodNo);
        if (i < 0){
            throw new MyException("商品库存不足");
        }
        return 1;
    }


}
