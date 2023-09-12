package com.hang.pojo;

import com.hang.Req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Ahang
 * @create 2023/6/25 21:09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPay {
    private String Out_trade_no;
    private BigDecimal total_amount;
    private String subject;
    private String Return_url;
    private String Notify_url;
    private String quit_url;
}
