package org.example.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author:lmw
 * @date:2024/1/3 13:59
 **/
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过channel拿到pipeline
        ChannelPipeline pipeline=channel.pipeline();
        //加入netty提供的HttpServerCodec  http 编/解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //增加自定义的handler
        pipeline.addLast("MyHttpServerHandler",new HttpServerHandler());
    }
}
