package org.example.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author:lmw
 * @date:2024/3/21 20:47
 **/
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) //给bossgroup加log
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline=channel.pipeline();
                            //加入一个netty 提供IdleStateHandler
                            /*
                            1.IdleStateHandler 是netty提供的处理空闲状态的处理器
                            2.readerIdeleTime :表示多久没有读，就会发送一个心跳检查包 检测是否连接
                            3.writerIdleTime :表示多久没有写，就会发送一个心跳检查包 检测是否连接
                            4.allIdleTime  :表示多久没有读和写，就会发送一个心跳检查包 检测是否连接
                            5.当IdleState触发后会传递给管道的下一个handler去处理
                            通过调用下一个handler的userEventTigger
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            pipeline.addLast(null);
                        }
                    });
            ChannelFuture channelFuture=serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
