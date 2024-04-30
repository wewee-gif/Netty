package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @author:lmw
 * @date:2024/1/5 16:28
 **/
public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建 ByteBuf对象，该对象包含一个byte数组
        //netty的 ByteBuf 不用 flip
        //底层维护了readerindex 和 writerindex
        ByteBuf buf = Unpooled.directBuffer(10);

//        for (int i = 0; i < 10; i++) {
//            buf.writeByte(i);
//        }
        buf.writeShort(11);
        System.out.println(buf.readableBytes());
        System.out.println(buf.capacity());
        ReferenceCountUtil.release(buf);
//        for (int i = 0; i < buf.capacity(); i++) {
//            System.out.println(buf.getByte(i));
//        }
//
//        for (int i = 0; i < buf.capacity(); i++) {
//            System.out.println(buf.readByte());
//        }
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world",
                CharsetUtil.UTF_8);
        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());
        while (byteBuf.isReadable()){
            System.out.println((char)byteBuf.readByte());
        }
        byteBuf.discardReadBytes(); //重置readerIndex 、 writerIndex 为0
        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());
        System.out.println((char) byteBuf.getByte(0));

        // 使用大端字节序读取int
        int bigEndianInt = buf.readInt();
        // 使用小端字节序读取int
        int littleEndianInt = buf.readIntLE();

        // 使用大端字节序写入int
        buf.writeInt(bigEndianInt);
        // 使用小端字节序写入int
        buf.writeIntLE(littleEndianInt);

        //记得释放
        ReferenceCountUtil.release(byteBuf);
    }
}
