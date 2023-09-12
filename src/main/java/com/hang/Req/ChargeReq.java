package com.hang.Req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ahang
 * @create 2023/7/27 17:27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeReq {
    private String flag;
    private Integer charge;
}
