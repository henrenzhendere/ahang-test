package com.hang.Res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Ahang
 * @create 2023/7/28 9:34
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayRes {
    private String out_trade_no;//订单编号

    private BigDecimal receiptAmount;//实付金额
    private String payTime;
}
