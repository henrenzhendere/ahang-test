package com.hang.mapper;

import com.hang.Res.ReturnRes;
import com.hang.pojo.Address;
import com.hang.pojo.Good;
import com.hang.pojo.Order;
import com.hang.pojo.WeChatPay2;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/25 15:47
 */

@Repository
public interface OrderMapper {
    /*
     * 待支付信息
     * */
    List orderToPay(String userId);

    Good goodToPay(String goodNo);

    void insertOrder1(Order order);

    int updateOrderStatus(@Param("outTradeNo") String outTradeNo,@Param("payTime") Date payTime);

    List queryAllOrder(int userId);


}
