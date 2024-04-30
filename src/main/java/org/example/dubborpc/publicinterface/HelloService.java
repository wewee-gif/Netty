package org.example.dubborpc.publicinterface;

/**
 * @author:lmw
 * @date:2024/4/11 18:18
 **/


//这个接口服务消费方和服务提供方都需要
public interface HelloService {
    String Hello(String msg);
}
