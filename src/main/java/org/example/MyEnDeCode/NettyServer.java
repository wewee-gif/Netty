package org.example.MyEnDeCode;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author:lmw
 * @date:2023/12/31 16:43
 **/

//NettyServer
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {


        //bossGroup只是处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        //bossGroup和workerGroup 含有的子线程（NioEventloop）的个数  默认为实际cpu核数*2
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
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });  //给我们的workerGroup 的 Eventloop对应的管道设置处理器

            System.out.println(".........启动服务器..........");

            //绑定一个端口并同步，生成了一个ChannelFuture对象
            //启动服务器（并绑定端口）
            ChannelFuture cf=bootstrap.bind(6668).sync();

            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("成功");
                    }
                    else
                        System.out.println("失败");
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
