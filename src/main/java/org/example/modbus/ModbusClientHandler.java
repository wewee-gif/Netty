package org.example.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author:lmw
 * @date:2024/4/12 21:21
 **/
public class ModbusClientHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<HashMap> SHARED_DATA_KEY = AttributeKey.valueOf("SHARED_DATA1");
    public static final AttributeKey<HashMap> SHARED_DATA_KEY_2 = AttributeKey.valueOf("SHARED_DATA2");
    //接收解析信息用的buf
    ByteBuf byteBuf1;
    //存地址的map
    HashMap<Short,Short> map;
    //存查询点个数的map
    HashMap<Short,Short> map1;
    short a=0;
    short b=10;
    byte c=0x03;

    int count=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        map=new HashMap<Short,Short>();
        map1=new HashMap<Short,Short>();
        ctx.channel().attr(SHARED_DATA_KEY).set(map);
        ctx.channel().attr(SHARED_DATA_KEY_2).set(map1);
        //用netty的taskqueue去跑每十秒读一次信息的任务
//        ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                //重点！   每次必须重新new bytebuff 因为writeAndFlush到最后的tailhandler会自动release传入的buf
//                byteBuf= Unpooled.directBuffer(12);
//                //组装 modbus主站的ADU ADU=MBAP(报文头)+PDU(桢结构)
//                //设置序号 两字节
//                byteBuf.writeShort(num);
//                //协议号：Modbus TCP协议 两字节
//                byteBuf.writeByte(0);
//                byteBuf.writeByte(0);
//                //报文长度 两字节
//                byteBuf.writeByte(0);
//                byteBuf.writeByte(0x06);
//                //单元标识符 一字节
//                byteBuf.writeByte(0x01);
//                //设置功能码 一字节
//                byteBuf.writeByte(CodeFunc);
//                //开始读的地址  两字节
//                byteBuf.writeShort(StartAdder);
//                //读多少点 两字节
//                byteBuf.writeShort(PointNum);
//                //把序号和对应的数据地址放入map
//                //这里的5可以做配置文件去改 根据内存大小去选择
//                if(map.size()<=5){
//                    map.put(num,StartAdder);
//                    map1.put(num,PointNum);
//                }else {
//                    map.clear();
//                    map1.clear();
//                    map.put(num,StartAdder);
//                    map1.put(num,PointNum);
//                }
//                System.out.println("发送的报文内容：");
//                for (int i = 0; i < byteBuf.capacity(); i++) {
//                    System.out.printf(ByteToHexUtil.toHexStringAtLeastTwoDigits(byteBuf.getByte(i))+" ");
//                }
//                System.out.println();
//                ctx.writeAndFlush(byteBuf);
//                //TransactionIdentifier 每次的信息序号加一
//                num += 1;
//                //重置一下 读写index
//                byteBuf.clear();
//
//            }
//        },0 , 5, TimeUnit.SECONDS);
        ctx.writeAndFlush(new ModbusMessage(c,a,b));

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (count == 0) {
            System.out.println("0x01：读线圈\n" +
                    "0x05：写单个线圈\n" +
                    "0x0F：写多个线圈\n" +
                    "0x02：读离散量输入\n" +
                    "0x04：读输入寄存器\n" +
                    "0x03：读保持寄存器\n" +
                    "0x06：写单个保持寄存器\n" +
                    "0x10：写多个保持寄存器");
            System.out.println("输入格式：[1/2](选择发送次数，1单次，2循环),[功能码],[起始地址],[查询/写入测点个数],[写的数据（/隔开）]");
        }
        count=1;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byteBuf1 = (ByteBuf) msg;
        System.out.println("接收的报文内容：");
        while (byteBuf1.isReadable()){
            System.out.printf(ByteToHexUtil.toHexStringAtLeastTwoDigits(byteBuf1.readByte())+" ");
        }
        byteBuf1.readerIndex(0);
        ByteToHexUtil.AnalysisMessage(byteBuf1,map,map1);
        //解析报文内容
        System.out.println("----------------------------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("关闭了");
        cause.printStackTrace();
        ctx.close();
    }
}
