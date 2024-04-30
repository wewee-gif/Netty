package org.example.modbus;

/**
 * @author:lmw
 * @date:2024/4/13 17:12
 **/
public class MessageLengthException extends Exception{
    public MessageLengthException(){
        super("本次报文长度检验错误");
    }
    public MessageLengthException(String msg){
        super(msg);
    }
}
