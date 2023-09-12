package com.hang.Res;


import com.hang.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRes {
    private String token;
    private String code;
    private Object data;
    private String message;
    private String user_no;
    private String password;
    private User user;

}
