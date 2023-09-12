package com.hang.pojo;

import com.hang.Req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Ahang
 * @create 2023/6/21 16:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String userName;
    private String password;
    private Integer money;
    private Integer version;
}
