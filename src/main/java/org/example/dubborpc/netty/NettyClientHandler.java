package org.example.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author:lmw
 * @date:2024/4/12 15:13
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result; //返回的结果
    private String para;   //客户端调用方法时，传入的参数

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;  //让其他不是netty的方法可以用上下文
    }

    @Override
    public synchronized void  channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result =msg.toString();
        notify();
    }

    @Override
    public  synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait();
        return  result;
    }

    void setPara(String para){
        this.para=para;
    }
}
