package org.example.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author:lmw
 * @date:2024/4/14 17:53
 **/
public class ModbusMessageEnCoder extends MessageToByteEncoder <ModbusMessage> {
    //消息序列号
    short num =0;
    //功能码类型
    private byte CodeFunc;
    //查询/写值的开始地址
    private short StartAdder;
    //查/写多少个点
    private short PointNum;
    //写值数组
    private LinkedList<Short> value;
    //写值使要计算byte个数
    byte bytes;

    //存地址的map
    HashMap<Short,Short> map;
    //存查询点个数的map
    HashMap<Short,Short> map1;

    final static byte b1=0b00000001;
    final static byte b2=0b00000010;
    final static byte b3=0b00000100;
    final static byte b4=0b00001000;
    final static byte b5=0b00010000;
    final static byte b6=0b00100000;
    final static byte b7=0b01000000;
    final static byte b8= (byte)(0b10000000&0xff);
    static byte bb=0b00000000;
    static byte[] byteboolean={b1,b2,b3,b4,b5,b6,b7,b8};

    static int temp=0;

    @Override
    protected void encode(ChannelHandlerContext ctx, ModbusMessage msg, ByteBuf out) throws Exception {
        this.CodeFunc= msg.getCodeFunc();
        this.PointNum= msg.getPointNum();
        this.StartAdder= msg.getStartAdder();
        this.value= msg.getValue();
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
        //开始读/写的地址  两字节
        out.writeShort(StartAdder);
        //写单线圈
        if(CodeFunc==0x05){
            if(value.get(0)==1) {
                out.writeByte(0xff);
                out.writeByte(0x00);
            }else {
                out.writeByte(0x00);
                out.writeByte(0x00);
            }
        }
        //写单寄存器
        else if (CodeFunc==0x06){
            out.writeShort(value.get(0));
        }
        //写多线圈
        else if (CodeFunc==0x0F) {
            bb=0b00000000;
            //先求出需要多少个byte
            out.writeShort((short)value.size());
            bytes=(byte)(value.size()/8);
            if( (value.size()%8)>0 ){
                bytes += 1;
            }
            out.writeByte(bytes);
            //对每个byte进行设置
            outerLoop:
            for (byte i = 0; i < bytes ; i++) {
                //对每一个byte的每一个bit设置
                for (int j = 0; j < 8; j++) {
                    if(temp< value.size()){
                        if(value.get(temp)==1){
                            bb=(byte) (bb|byteboolean[j]);
                        }
                        temp++;
                    }else
                        break outerLoop;
                }
                out.writeByte(bb);
                //每一个byte设置完了后重置
                bb=0b00000000;
            }
            //很重要 如果是最后一次的byte 在上面的循环中是被直接跳出来了 没有执行写那一块，所以要在最外面写
            out.writeByte(bb);
            //最后设置报文头里的长度  #bytes前面是固定的七个byte
            out.setShort(4,bytes+7);
            //重置temp
            temp=0;
        }
        //写多寄存器
        else if (CodeFunc==0x10) {
            out.writeShort(value.size());
            out.writeByte(2*value.size());
            for (int i = 0; i < value.size(); i++) {
                out.writeShort(value.get(i));
            }
            System.out.println(out.writerIndex());
            //最后设置报文头里的长度  #bytes前面是固定的七个byte
            out.setShort(4,2*value.size()+7);
        } else {
            //读多少点 两字节
            out.writeShort(PointNum);
        }
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
        for (int i = 0; i < out.writerIndex(); i++) {
            System.out.printf(ByteToHexUtil.toHexStringAtLeastTwoDigits(out.getByte(i))+" ");
        }
        System.out.println();
        num += 1;
    }
}
