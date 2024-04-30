package org.example.modbus;

/**
 * @author:lmw
 * @date:2024/4/14 17:56
 **/
public class ModbusMessage {
    //功能码类型
    private byte CodeFunc=0x01;
    //查询的开始地址
    private short StartAdder=0;
    //查多少个点
    private short PointNum=10;

    public ModbusMessage() {
    }

    public ModbusMessage(byte codeFunc, short startAdder, short pointNum) {
        CodeFunc = codeFunc;
        StartAdder = startAdder;
        PointNum = pointNum;
    }

    public byte getCodeFunc() {
        return CodeFunc;
    }

    public void setCodeFunc(byte codeFunc) {
        CodeFunc = codeFunc;
    }

    public short getStartAdder() {
        return StartAdder;
    }

    public void setStartAdder(short startAdder) {
        StartAdder = startAdder;
    }

    public short getPointNum() {
        return PointNum;
    }

    public void setPointNum(short pointNum) {
        PointNum = pointNum;
    }
}
