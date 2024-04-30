package org.example.handlertest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.NettyClientHandler;

/**
 * @author:lmw
 * @date:2024/3/27 18:03
 **/
public class MyClient {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventExecutors=new NioEventLoopGroup();
        try{

            Bootstrap bootstrap =new Bootstrap();
            //设置参数
            bootstrap.group(eventExecutors)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
                    .handler(new MyClientInitialzer());
            System.out.println("客户端 ok..");

            //启动客户端连接服务器端
            ChannelFuture channelFuture=bootstrap.connect("127.0.0.1",7000).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
