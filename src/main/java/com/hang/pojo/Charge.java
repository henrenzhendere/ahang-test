package com.hang.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author Ahang
 * @create 2023/7/27 11:39
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
    private Integer chargeId;
    private Integer charge;
    private Integer total;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String createTime;
    private int userId;
    private String finishTime;
    private String chargeNo;
    private int version;
    private int status;

}
