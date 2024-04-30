package org.example.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.HashMap;

/**
 * @author:lmw
 * @date:2024/4/14 17:53
 **/
public class ModbusMessageEnCoder extends MessageToByteEncoder <ModbusMessage> {
    //消息序列号
    short num =0;
    //功能码类型
    private byte CodeFunc;
    //查询的开始地址
    private short StartAdder;
    //查多少个点
    private short PointNum;

    //存地址的map
    HashMap<Short,Short> map;
    //存查询点个数的map
    HashMap<Short,Short> map1;

    @Override
    protected void encode(ChannelHandlerContext ctx, ModbusMessage msg, ByteBuf out) throws Exception {
        this.CodeFunc= msg.getCodeFunc();
        this.PointNum= msg.getPointNum();
        this.StartAdder= msg.getStartAdder();
        map=ctx.attr(ModbusClientHandler.SHARED_DATA_KEY).get();
        map1=ctx.attr(ModbusClientHandler.SHARED_DATA_KEY_2).get();
        //组装 modbus主站的ADU ADU=MBAP(报文头)+PDU(桢结构)
        //设置序号 两字节
        out.writeShort(num);
        //协议号：Modbus TCP协议 两字节
        out.writeByte(0);
        out.writeByte(0);
        //报文长度 两字节
        out.writeByte(0);
        out.writeByte(0x06);
        //单元标识符 一字节
        out.writeByte(0x01);
        //设置功能码 一字节
        out.writeByte(CodeFunc);
        //开始读的地址  两字节
        out.writeShort(StartAdder);
        //读多少点 两字节
        out.writeShort(PointNum);
        //把序号和对应的数据地址放入map
        //这里的5可以做配置文件去改 根据内存大小去选择
        if(map.size()<=5){
            map.put(num,StartAdder);
            map1.put(num,PointNum);
        }else {
            map.clear();
            map1.clear();
            map.put(num,StartAdder);
            map1.put(num,PointNum);
        }
        System.out.println("发送的报文内容：");
        for (int i = 0; i < 12; i++) {
            System.out.printf(ByteToHexUtil.toHexStringAtLeastTwoDigits(out.getByte(i))+" ");
        }
        System.out.println();
        num += 1;
    }
}
