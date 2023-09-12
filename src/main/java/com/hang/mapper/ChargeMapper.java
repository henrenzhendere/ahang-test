package com.hang.mapper;

import com.hang.pojo.Charge;
import com.hang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ahang
 * @create 2023/7/27 11:34
 */

@Mapper
public interface ChargeMapper {

    /*
    * 查询当前余额
    * */
    User queryPresentCharge(int userId);


    /*
    * 展示充值信息
    * */
    List<Charge> chargeInfo(int userId);

    /*
    * 查询本次充值信息
    * */

    Charge preChargeInfo(String chargeNo);
    /*
    * 插入一条充值记录
    * */
    int insertChargeInfo(Charge charge);

    /*
    * 修改充值完成时间 确定完成
    * */

    int updateFinishTime(Charge charge);

    int delChargeInfo(String chargeNo);


    /*
    * 更新user表的version
    * */
    int updateVersionInt(@Param("userId") int userId,@Param("total")int total);

    /*
    *根据chargeNo查userid
    * */

    int queryUserId(String chargeNo);

    /*
    * 根据chargeNo修改订单状态
    * */

    int updateStatus(String chargeNo);
}
