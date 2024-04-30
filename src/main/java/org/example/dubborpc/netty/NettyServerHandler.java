package org.example.dubborpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.example.dubborpc.provider.HelloServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

/**
 * @author:lmw
 * @date:2024/1/1 12:12
 **/


public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg"+msg);
        //客户端在调用服务器的api时，我们需要定义一个协议
        //比如我们要求 每次发送的消息都必须以“HelloService#hello#你好”开头
        if(msg.toString().startsWith("HelloService#hello#")){
            String result=new HelloServiceImpl().Hello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
            ctx.writeAndFlush(result);
        }

    }

    //数据读取完毕


    //处理异常，一般是关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.channel().close();
        ctx.close();
    }
}
