package com.gtafe.competitionstudent.serialport;

/**
 * Created by ZhouJF on 2020-10-13.
 */
public class CMD_MSG_BACK extends CMD_MSG_BASE{

    public short HEAD ;
    public byte index ;
    public byte dataLength ;
    public byte workMode ;
    public byte powerState ;
    public int dianya ;
    public int dianliu ;
    public int gonglv ;
    public short end ;
    public byte CRC ;

    public CMD_MSG_BACK() {
        super();
    }
    public CMD_MSG_BACK(byte[] bytes) {
        super(bytes);
        HEAD =  getShort();
        index =  getByte();
        dataLength =  getByte();
        workMode =  getByte();
        powerState =  getByte();
        dianya =  getInt();
        dianliu =  getInt();
        gonglv =  getInt();
    }

    @Override
    protected byte[] pack() {

      //  putShort(HEAD);
        return super.pack();
    }

    @Override
    public String toString() {
        return "CMD_MSG_HAND_RECOGNITION{" +
                "HEAD=" + HEAD +
                ", index=" + index +
                ", dataLength=" + dataLength +
                ", workMode=" + workMode +
                ", powerState=" + powerState +
                ", dianya=" + dianya +
                ", dianliu=" + dianliu +
                ", gonglv=" + gonglv +
                ", end=" + end +
                ", CRC=" + CRC +
                '}';
    }
}
