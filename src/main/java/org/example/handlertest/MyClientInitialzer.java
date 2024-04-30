package org.example.handlertest;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author:lmw
 * @date:2024/3/27 18:04
 **/
public class MyClientInitialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline= channel.pipeline();
        //加入一个出站的handler对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());
        //加入一个自定义的handler，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
