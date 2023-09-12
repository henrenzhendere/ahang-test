package com.hang.Res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ahang
 * @create 2023/7/25 16:59
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    private Integer code;
    private String message;
    private T data;
    public R(Integer code, String message){
        this(code,message,null);
    }
}
