package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Ahang
 * @create 2023/6/19 19:09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good {
    private String goodNo;
    private String goodName;
    private Integer price;
    private String photo;
    private String info;
    private String num;
    private int totalPrice;
}
