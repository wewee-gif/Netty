package org.example.codc2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.example.codec.StudentPOJO;

/**
 * @author:lmw
 * @date:2024/1/1 12:12
 **/

/*
1.我们自定义一个Handler 需要继续netty 规定好的某个HandlerAdapter(规范)
2.这时我们自定义一个Handler，才能称为一个handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {




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


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {
        MyDataInfo.MyMessage.DataType dataType=myMessage.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student=myMessage.getStudent();
            System.out.println("学生id="+student.getId()+"学生名字="+student.getName());
        } else if (dataType== MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker=myMessage.getWorker();
            System.out.println("工人名字="+worker.getName()+"年龄"+worker.getAge());
        }else
            System.out.println("传输的类型不正确");
    }
}
