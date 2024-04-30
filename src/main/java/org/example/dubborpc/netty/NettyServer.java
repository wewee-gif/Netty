package org.example.dubborpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @author:lmw
 * @date:2024/4/12 13:34
 **/
public class NettyServer {

    public  static void startServer(String hostname,int prot) throws InterruptedException {
        startServer0(hostname,prot);
    }

    private static void startServer0(String hostname,int prot) throws InterruptedException {
        NioEventLoopGroup bossGroup =new NioEventLoopGroup();
        NioEventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)   //设置两个线程组
                    .channel(NioServerSocketChannel.class)   //使用NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new   ChannelInitializer<SocketChannel>() {//创建一个通道测试对象(匿名对象)
                        //给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new NettyServerHandler());
                            //这样NettyServerHandler就不会在IO线程（这个EventLoop）里面跑，而是在这个额外的线程池里面选一个线程跑
                            //ch.pipeline().addLast(gruop,new NettyServerHandler());
                        }
                    });  //给我们的workerGroup 的 Eventloop对应的管道设置处理器

            System.out.println(".........启动服务器..........");

            //绑定一个端口并同步，生成了一个ChannelFuture对象
            //启动服务器（并绑定端口）
            ChannelFuture cf=bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
