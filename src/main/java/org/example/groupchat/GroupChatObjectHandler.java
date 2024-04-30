package org.example.groupchat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author:lmw
 * @date:2024/3/20 22:23
 **/
public class GroupChatObjectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("start");

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("11111111");
//        ByteBuf buf =(ByteBuf) msg;
//        System.out.println("客户端发送消息是："+buf.toString(StandardCharsets.UTF_8));
        User user=(User) msg;
        System.out.println(user);
    }
}
