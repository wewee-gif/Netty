package org.example.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author:lmw
 * @date:2024/1/1 17:25
 **/
public class NettyClient {

    private static ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    //使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},((proxy, method, args) -> {
                    if(client==null){
                        initclient();
                    }
                    //设置要发给服务器端的消息
                    client.setPara(providerName+args[0]);
                    return executorService.submit(client).get();
                }));
    }
    private static void initclient(){
        client= new NettyClientHandler();
        EventLoopGroup eventExecutors=new NioEventLoopGroup();

        try{
            //创建客户端启动对象
            //注意客户端使用的不是ServerBootstrap 而是Bootstrap
            Bootstrap bootstrap =new Bootstrap();

            //设置参数
            bootstrap.group(eventExecutors)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(client);//加入自己的处理器
                        }
                    });
            System.out.println("客户端 ok..");

            //启动客户端连接服务器端
            ChannelFuture channelFuture=bootstrap.connect("127.0.0.1",6668).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}

