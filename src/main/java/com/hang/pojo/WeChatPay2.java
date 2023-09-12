package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

/**
 * @author Ahang
 * @create 2023/6/26 17:20
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatPay2 {

    private String subject;

    //订单编号
    private String out_trade_no;
    private int total_amount;
    private String time_end;
    private String message;
    private String notify_url;
    private String return_url;

//    订单支付时间
    private String payTime;
}
