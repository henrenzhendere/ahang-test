package com.hang.Res;

import com.hang.Req.PageReq;
import com.hang.pojo.Good;
import com.hang.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRes {
    private String code;
    private Object data;
    private String message;
    private Good good;
    private Order order;
    private Map map;

}
