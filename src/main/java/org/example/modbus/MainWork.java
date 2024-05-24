package org.example.modbus;

import java.util.LinkedList;

/**
 * @author:lmw
 * @date:2024/4/21 16:44
 **/
public class MainWork {
    static String[] ValueString;
    static private ModbusMessage message=new ModbusMessage();
    public static byte convest(String s) {
        byte code;
        switch (s) {
            case "0x01":
                code = 1;
                break;
            case "0x05":
                code = 0x05;
                break;
            case "0x0F":
                code = 0x0F;
                break;
            case "0x02":
                code = 0x02;
                break;
            case "0x04":
                code = 0x04;
                break;
            case "0x03":
                code = 0x03;
                break;
            case "0x06":
                code = 0x06;
                break;
            case "0x10":
                code = 0x10;
                break;
            default:
                System.out.println("没有该功能码：" + s + "请检查");
                code = (byte) 0xff;
        }
        return code;
    }

    private static LinkedList<Short> StringToShort(String s){
        LinkedList<Short> num = new LinkedList<Short>();
        ValueString=s.trim().substring(1,s.length()-1).split("/");
        for (int i = 0; i < ValueString.length; i++) {
            num.add(Short.parseShort(ValueString[i]));
        }
        return num;
    }

    public static ModbusMessage loadmessage(String[] s,byte code){

        message.setCodeFunc(code);
        message.setPointNum(Short.parseShort(s[3]));
        message.setStartAdder(Short.parseShort(s[2]));
        message.setValue(StringToShort(s[4]));
        return message;

    }
}
