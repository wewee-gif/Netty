package org.example.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:lmw
 * @date:2024/3/19 19:59
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<Object>{
   //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局事件执行器，是单例
    private  static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded 一旦连接建立就执行

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
     Channel channel = ctx.channel();;
     //将该客户加入聊天的信息推送给其他客户端
     //该方法会自动将channelgroup中的channel遍历 并发送信息
     channelGroup.writeAndFlush(sdf.format(new Date())+"--[客户端]"+channel.remoteAddress()+"加入聊天");
     channelGroup.add(channel);
    }

 @Override
 public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
     //会自动去掉groupchannel里面的channel
  channelGroup.writeAndFlush(sdf.format(new Date())+"--[客户端]"+ctx.channel().remoteAddress()+"离开了");
 }



 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  System.out.println(sdf.format(new Date())+ctx.channel().remoteAddress()+"上线了");
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  System.out.println(sdf.format(new Date())+ctx.channel().remoteAddress()+"下线了");
 }

 @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
          Channel channel =ctx.channel();
          System.out.println("111111111");
//          User user=(User) s;
//          System.out.println(user);
          //channelGroup.writeAndFlush(sdf.format(new Date())+"--客户端"+channel.remoteAddress()+"发送："+s,new MyMatcher(channel));
    }
}
