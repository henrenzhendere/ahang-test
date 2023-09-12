package com.hang.service;

import com.hang.pojo.Charge;
import com.hang.pojo.Order;
import com.hang.pojo.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/7/27 14:01
 */
public interface ChargeService {
    /*
     * 查询当前余额
     * */
    User queryPresentCharge(int userId);

    /*
     * 展示充值信息
     * */
    List<Charge> chargeInfo(int userId);

    /*
     * 插入一条充值记录
     * */
    int insertChargeInfo(Charge charge);

    /*
     * 支付宝
     * */
    String payByAli(Charge charge );





    /*
     * 修改充值完成时间 确定完成
     * */

    int updateFinishTime(Charge charge);

    /*
    * 支付不成功删除信息
    * */
    int delChargeInfo(String chargeNo);

    /*
     *根据chargeNo查userid
     * */

    int queryUserId(String chargeNo);

    /*
     * 查询本次充值信息
     * */

    Charge preChargeInfo(String chargeNo);

    /*
     * 根据chargeNo修改订单状态
     * */

    int updateStatus(String chargeNo, BigDecimal charge,String payTime);

}
