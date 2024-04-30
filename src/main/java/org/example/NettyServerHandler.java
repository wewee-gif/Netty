package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author:lmw
 * @date:2024/1/1 12:12
 **/

/*
1.我们自定义一个Handler 需要继续netty 规定好的某个HandlerAdapter(规范)
2.这时我们自定义一个Handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据实际(这里我们可以读取客户端发送的消息)
    /*
    1.ChannelHandlerContext ctx:上下文对象，含有 管道pipeline，通道channel，地址
    2.object msg：就是客户端发送的数据 默认object
     */

    //gruop就是充当业务线程池，可以将任务提交到该线程池
    static final EventExecutorGroup gruop=new DefaultEventExecutorGroup(16);


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {

        //如果有一个耗时的业务 -> 异步执行 -> 提交到该channel 对应的NIOEventloop 的 taskQueue中,taskQueue是队列所以提交的任务都是做完一个才做下一个
        /*
            Thread.sleep(10*1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2",CharsetUtil.UTF_8));
        */
        //解决方法一
        //System.out.println(ctx.channel().eventLoop().hashCode());
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10*1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2",CharsetUtil.UTF_8));
//                }catch (Exception e){
//                    System.out.println("发生异常"+e.getMessage());
//                }
//            }
//        });

        //方案二
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10*1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2",CharsetUtil.UTF_8));
//                }catch (Exception e){
//                    System.out.println("发生异常"+e.getMessage());
//                }
//            }
//        },5, TimeUnit.SECONDS);

        //上述两种方法还是有问题的，taskquque中的任务执行还是在当前handler的线程里执行，所以还是会阻塞
        //用ExecutorGroup
        gruop.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(10*1000);
                System.out.println("gruop.submit的call 线程是="+Thread.currentThread().getName());
                return null;
            }
        });



        System.out.println("go on ...");

        System.out.println("server ctx = "+ctx);
        //将msg 转成一个ByteBuf
        //ByteBuf 是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf =(ByteBuf) msg;
        System.out.println("客户端发送消息是："+buf.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址："+ ctx.channel().remoteAddress());
        System.out.println(msg.hashCode());
        System.out.println(buf.hashCode());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
        writeAndFlush 是write+flush
        将数据写入缓存，并刷新
        一般讲，对发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));
    }

    //处理异常，一般是关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.channel().close();
        ctx.close();
    }
}
