package com.gtafe.competitionstudent.serialport;

import com.gtafe.competitionlib.utils.Util;

import java.nio.ByteBuffer;

/**
 * Created by ZhouJF on 2020-10-13.
 */
public class CMD_MSG_CMD extends CMD_MSG_BASE {

    public short HEAD = (short) 0xAAAA;
    public byte index;
    public byte dataLength = 2;
    public byte workMode;
    public byte powerState;
    public short end = (short) 0xBBBB;
    public byte CRC;

    public CMD_MSG_CMD() {
        super();
        mByteBuffer = ByteBuffer.allocate(9);
    }

    public CMD_MSG_CMD(byte[] bytes) {
        super(bytes);
        HEAD = getShort();
        index = getByte();
        dataLength = getByte();
        workMode = getByte();
        powerState = getByte();
    }

    @Override
    public byte[] pack() {

        putShort(HEAD);
        putByte(index);
        putByte(dataLength);
        putByte(workMode);
        putByte(powerState);
        putShort(end);
        putByte(CRC);
        byte[] bytes = super.pack();
        bytes[bytes.length - 1] = (byte) Util.FindCRC(bytes);
        return bytes;
    }

    @Override
    public String toString() {
        return "CMD_MSG_HAND_RECOGNITION{" +
                "HEAD=" + HEAD +
                ", index=" + index +
                ", dataLength=" + dataLength +
                ", workMode=" + workMode +
                ", powerState=" + powerState +
                ", end=" + end +
                ", CRC=" + CRC +
                '}';
    }
}
