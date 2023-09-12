package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/25 17:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String userName;
    private String addrId;
    private String userId;
    private String addr;
    private String phone;
    private String orderNo;
    private List addressList;
}
