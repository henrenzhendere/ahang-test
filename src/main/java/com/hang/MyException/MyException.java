package com.hang.MyException;

/**
 * @author Ahang
 * @create 2023/7/1 10:53
 */
public class MyException extends RuntimeException{

    public MyException(){
    }
    public MyException(String msg){
        super(msg);
    }

}
