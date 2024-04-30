package org.example.handlertest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author:lmw
 * @date:2024/3/27 17:29
 **/
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param channelHandlerContext  上下文
     * @param byteBuf      入站的bytebuf
     * @param list          List集合 将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //因为Long 8个字节，需要判断有8个字节，才能读取一个Long
        if(byteBuf.readableBytes()>=8){
            list.add(byteBuf.readLong());
        }
    }
}
