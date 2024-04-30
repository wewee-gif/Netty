package org.example.groupchat;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author:lmw
 * @date:2024/3/19 22:53
 **/
public class GroupChatClient {
    private final String host;
    private final int port;

    public GroupChatClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group =new NioEventLoopGroup();
        try {
            Bootstrap bootstrap =new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline=channel.pipeline();
                            //pipeline.addLast("decoder",new StringDecoder());
                            //pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture=bootstrap.connect(host,port).sync();
            Channel channel=channelFuture.channel();
            System.out.println("----------"+channel.localAddress()+"----------");
//            channel.writeAndFlush(new User("LMW",22));
//            System.out.println("send ok");
            Scanner scanner =new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg=scanner.nextLine();
                channel.writeAndFlush(msg);
            }

            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatClient("127.0.0.1",7000).run();
    }
}
