package org.example.modbus;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

/**
 * @author:lmw
 * @date:2024/4/12 22:06
 **/
public class ByteToHexUtil {

    final static byte b1=0b00000001;
    final static byte b2=0b00000010;
    final static byte b3=0b00000100;
    final static byte b4=0b00001000;
    final static byte b5=0b00010000;
    final static byte b6=0b00100000;
    final static byte b7=0b01000000;
    final static byte b8= (byte)(0b10000000&0xff);

    static byte[] byteboolean={b1,b2,b3,b4,b5,b6,b7,b8};
    //模拟量的类型
    int datasize=2;

    public static String toHexStringAtLeastTwoDigits(int number) {
        String hex = Integer.toHexString(number&0xff);
        return hex.length() < 2 ? "0" + hex : hex;
    }

    //按照modbus tcp协议去解析
    public static void AnalysisMessage(ByteBuf byteBuf,HashMap<Short,Short> map,HashMap<Short,Short> map1) throws MessageLengthException {
        //先校验一下报文长度
        short len;
        len=byteBuf.getShort(4);
        if((byteBuf.readableBytes()-6)!=len){
            throw new MessageLengthException();
        }
        //没问题就开始解析
        //先区分功能码
        String code=ByteToHexUtil.toHexStringAtLeastTwoDigits(byteBuf.getByte(7));
        //数据的byte数
        int DataByteNum=byteBuf.getByte(8);
        //获得报文序列号，再去获得数据开始地址
        short num =byteBuf.getShort(0);
        if(map.containsKey(num)) {
            short adder = map.get(num);
            map.remove(num);
            //数据点的个数
            short PiontNum=map1.get(num);
            map1.remove(num);
            System.out.println();
            System.out.println("本次接收到的报文序列号：" + num);
            //寄存器默认是short型数据
            if (code.equals("01")) {
                System.out.println("接收功能码类型：读输出线圈");
                int a;
                for (int i = 0; i < PiontNum; i++) {
                    byte b=(byte)(byteBuf.getByte(i/8+9) & byteboolean[i%8]);
                    if((b&0xff)==byteboolean[i%8])
                        a=1;
                    else
                        a=0;
                    System.out.println("地址：" + (adder + i) + "            " + "测值：" +a);
                }
            } else if (code.equals("02")) {
                System.out.println("接收功能码类型：读输入线圈");
                int a;
                for (int i = 0; i < PiontNum; i++) {
                    byte b=(byte)(byteBuf.getByte(i/8+9) & byteboolean[i%8]);
                    if((b&0xff)==byteboolean[i%8])
                        a=1;
                    else
                        a=0;
                    System.out.println("地址：" + (adder + i) + "            " + "测值：" +a);
                }
            } else if (code.equals("03")) {
                System.out.println("接收功能码类型：读输出寄存器");
                for (int i = 0; i < PiontNum; i++) {
                    System.out.println("地址：" + (adder + i) + "            " + "测值：" + byteBuf.getShort(9 + i * 2));
                }

            } else if (code.equals("04")) {
                System.out.println("接收功能码类型：读输入寄存器");
                for (int i = 0; i < PiontNum; i++) {
                    System.out.println("地址：" + (adder + i) + "            " + "测值：" + byteBuf.getShort(9 + i * 2));
                }
            } else if (code.equals("05")){
                System.out.println("接收功能码类型：写单个输出线圈");
            } else if (code.equals("0f")) {
                System.out.println("接收功能码类型：写多个输出线圈");
            } else if (code.equals("06")) {
                System.out.println("接收功能码类型：写单个输出寄存器");
            } else if (code.equals("10")) {
                System.out.println("接收功能码类型：写多个输出寄存器");
            } else {
                //System.out.println("其他或错误功能码");
                System.out.println(code);
                throw new MessageLengthException("其他或错误功能码");
            }
        }else
            System.out.println("超过存储序列号map的上限，该报文的序列号已被丢弃，放弃解析本次报文");


    }
}
