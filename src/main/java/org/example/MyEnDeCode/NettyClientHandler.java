package org.example.MyEnDeCode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author:lmw
 * @date:2024/1/1 18:17
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String mes="今天天气冷，吃火锅";
            byte[] content=mes.getBytes(Charset.forName("utf-8"));
            int length =mes.getBytes(Charset.forName("utf-8")).length;

            //创建协议包对象
            MessageProtocol messageProtocol =new MessageProtocol();
            messageProtocol.setLength(length);
            messageProtocol.setContent(content);
            //这个时候是把这个messageProtocol传入了pipeline里面的下一个出站handler，由这个encoderhandler 进行对messageProtocol拆解再发送
            ctx.writeAndFlush(messageProtocol);
        }
    }

    //当通道有读取事件时，会触发


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {

    }
}
