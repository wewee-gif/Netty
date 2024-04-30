package org.example.dubborpc.provider;

import org.example.dubborpc.publicinterface.HelloService;

/**
 * @author:lmw
 * @date:2024/4/11 18:27
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String Hello(String msg) {
        System.out.println("收到客户端消息="+msg);
        if(msg!=null){
            return "你好客户端，我已经收到你的消息["+msg+"]";
        }else
            return "你好客户端，我已经收到你的消息";
    }
}
