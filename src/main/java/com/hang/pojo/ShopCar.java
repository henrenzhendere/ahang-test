package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ahang
 * @create 2023/6/23 10:28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCar {
    private String goodNo;
    private String userName;
    private String num;
    private Good good;
    private int flag;

}
