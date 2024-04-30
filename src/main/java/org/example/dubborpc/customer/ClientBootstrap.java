package org.example.dubborpc.customer;

import org.example.dubborpc.netty.NettyClient;
import org.example.dubborpc.publicinterface.HelloService;

/**
 * @author:lmw
 * @date:2024/4/12 16:00
 **/
public class ClientBootstrap {
    public static final String providerName="HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient ccustomer =new NettyClient();
        //创建一个代理对象
        HelloService service=(HelloService) ccustomer.getBean(HelloService.class,providerName);

        //通过代理对象调用服务提供者的方法（服务）
        String res=service.Hello("你好 dubbo");
    }
}
