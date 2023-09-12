package com.hang.Res;

import com.hang.pojo.Good;
import com.hang.pojo.ShopCar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ahang
 * @create 2023/6/23 10:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class shopCarRes {
    private int pageSize;
    private int pageNo;
    private List<ShopCar> shopCarGoodList;
    private Good good;
    private String code;
    private Object data;
    private String message;
}
