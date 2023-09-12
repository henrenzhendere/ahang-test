package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Ahang
 * @create 2023/6/25 14:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String userName;
    private String orderNo;
    private String userId;
    private String addrMsg;
    private Date createTime;
    private Date payTime;
    private BigDecimal totalPrice;
    private String payMethod;
    private int status;
    private String LogisticsNo;
}
