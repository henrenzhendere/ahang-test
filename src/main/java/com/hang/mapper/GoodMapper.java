package com.hang.mapper;

import com.hang.pojo.Good;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/19 19:49
 */
@Repository
public interface GoodMapper {

    /*
    * 查询所有商品信息
    * */
    List<Good> selectAllGoods(@Param("begin") int begin,@Param("pageSize") int pageSize);

    /*
    * 查询所有商品数量
    * */
    int queryCount();

    /*
    * 查看商品详情
    * */
    Good showGoodDetail(@Param("goodNo") String goodNo);

    /*
    * 查看购物车
    * */
   Good shopCarGood(String goodNo);

    /*
    * 查看商品价格
    * */
    BigDecimal queryPrice(String goodNo);

    /*
     * 扣库存
     * */
    int updateStock(@Param("num") int num,@Param("g·oodNo") String goodNo);

    int queryStock(String goodNo);
}
