package com.hang.Req;

import lombok.Data;

import javax.security.sasl.SaslServer;
import java.math.BigDecimal;

/**
 * @author Ahang
 * @create 2023/5/11 13:32
 */

@Data
public class PageReq {
    private String pageNo;
    private String pageSize;
    private String flag;
    private String goodNo;
    private String address;
    private BigDecimal totalPrice;
    private int begin;
    private int pageSizeInt;
    private int count;
    private int pageNoInt;
    private String token;
}
