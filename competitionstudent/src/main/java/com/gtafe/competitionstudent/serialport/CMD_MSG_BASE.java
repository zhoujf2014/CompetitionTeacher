package com.gtafe.competitionstudent.serialport;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

/**
 * Created by ZhouJF on 2020-10-13.
 */
public class CMD_MSG_BASE {







    public ByteBuffer mByteBuffer;
    public int index;

    protected byte[] pack() {
       /* CRC = Crc16CcittUpdate(mByteBuffer.array());
        putShort(CRC);*/
        return mByteBuffer.array();
    }




    public CMD_MSG_BASE() {



    }

    public CMD_MSG_BASE(byte[] bytes) {
        mByteBuffer = ByteBuffer.wrap(bytes);
    }

    public String floatToString(float price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(price);// format 返回的是字符串
        return p;
    }

    public ByteBuffer getData() {
        return mByteBuffer;
    }

    public int size() {
        return mByteBuffer.position();
    }

    public void add(byte c) {
        mByteBuffer.put(c);
    }

    public void resetIndex() {
        index = 0;
    }

    public byte getByte() {
        byte result = 0;
        result |= (mByteBuffer.get(index + 0) & 0xFF);
        index += 1;
        return result;
    }

    public short getUnsignedByte() {
        short result = 0;
        result |= mByteBuffer.get(index + 0) & 0xFF;
        index += 1;
        return result;
    }

    public short getShort() {
        short result = 0;
        result |= (mByteBuffer.get(index + 1) & 0xFF) << 8;
        result |= (mByteBuffer.get(index + 0) & 0xFF);
        index += 2;
        return result;
    }

    public short getShort_upgrade() {
        short result = 0;
        result |= (mByteBuffer.get(index + 0) & 0xFF) << 8;
        result |= (mByteBuffer.get(index + 1) & 0xFF);
        index += 2;
        return result;
    }

    public short getUnsignedShort() {
        short result = 0;
        result |= (mByteBuffer.get(index + 1) & 0xFF) << 8;
        result |= (mByteBuffer.get(index + 0) & 0xFF);
        index += 2;
        return result;
    }

    public int getInt() {
        int result = 0;
        result |= (mByteBuffer.get(index + 3) & 0xFF) << 24;
        result |= (mByteBuffer.get(index + 2) & 0xFF) << 16;
        result |= (mByteBuffer.get(index + 1) & 0xFF) << 8;
        result |= (mByteBuffer.get(index + 0) & 0xFF);
        index += 4;
        return result;
    }

    public int getInt_() {
        int result = 0;
        result |= (mByteBuffer.get(index + 0) & 0xFF);
        result |= (mByteBuffer.get(index + 1) & 0xFF) << 8;
        result |= (mByteBuffer.get(index + 2) & 0xFF) << 16;
        result |= (mByteBuffer.get(index + 3) & 0xFF) << 24;
        index += 4;
        return result;
    }

    public long getUnsignedInt() {
        long result = 0;
        result |= (mByteBuffer.get(index + 3) & 0xFFL) << 24;
        result |= (mByteBuffer.get(index + 2) & 0xFFL) << 16;
        result |= (mByteBuffer.get(index + 1) & 0xFFL) << 8;
        result |= (mByteBuffer.get(index + 0) & 0xFFL);
        index += 4;
        return result;
    }

    public long getLong() {
        long result = 0;
        result |= (mByteBuffer.get(index + 7) & 0xFFL) << 56;
        result |= (mByteBuffer.get(index + 6) & 0xFFL) << 48;
        result |= (mByteBuffer.get(index + 5) & 0xFFL) << 40;
        result |= (mByteBuffer.get(index + 4) & 0xFFL) << 32;
        result |= (mByteBuffer.get(index + 3) & 0xFFL) << 24;
        result |= (mByteBuffer.get(index + 2) & 0xFFL) << 16;
        result |= (mByteBuffer.get(index + 1) & 0xFFL) << 8;
        result |= (mByteBuffer.get(index + 0) & 0xFFL);
        index += 8;
        return result;
    }

    public long getUnsignedLong() {
        return getLong();
    }

    public long getLongReverse() {
        long result = 0;
        result |= (mByteBuffer.get(index + 0) & 0xFFL) << 56;
        result |= (mByteBuffer.get(index + 1) & 0xFFL) << 48;
        result |= (mByteBuffer.get(index + 2) & 0xFFL) << 40;
        result |= (mByteBuffer.get(index + 3) & 0xFFL) << 32;
        result |= (mByteBuffer.get(index + 4) & 0xFFL) << 24;
        result |= (mByteBuffer.get(index + 5) & 0xFFL) << 16;
        result |= (mByteBuffer.get(index + 6) & 0xFFL) << 8;
        result |= (mByteBuffer.get(index + 7) & 0xFFL);
        index += 8;
        return result;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt_());
    }

    public float getFloat_() {
        return Float.intBitsToFloat(getInt_());
    }

    public void putByte(byte data) {
        add(data);
    }


    public void putShort(short data) {
        add((byte) (data >> 8));
        add((byte) (data >> 0));

  /*      add((byte) (data >> 0));
        add((byte) (data >> 8));*/
    }

    public void putShort_upgread(short data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
    }


    public void putInt(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
    }


    public void putLong(long data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
        add((byte) (data >> 32));
        add((byte) (data >> 40));
        add((byte) (data >> 48));
        add((byte) (data >> 56));
    }


    public void putFloat(float data) {
        putInt(Float.floatToIntBits(data));
    }

    private short Crc16CcittUpdate(byte[] byIn) {
        short g_wCrc16CCITT = 0;
        int[] g_awhalfCrc16CCITT = { /* CRC half byte table */
                0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7,
                0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c, 0xd1ad, 0xe1ce, 0xf1ef};
        for (byte b : byIn) {
            byte byTemp = (byte) (((byte) (g_wCrc16CCITT >> 8)) >> 4);
            g_wCrc16CCITT <<= 4;
            g_wCrc16CCITT ^= g_awhalfCrc16CCITT[byTemp ^ (b >> 4)];
            byTemp = (byte) (((byte) (g_wCrc16CCITT >> 8)) >> 4);
            g_wCrc16CCITT <<= 4;
            g_wCrc16CCITT ^= g_awhalfCrc16CCITT[byTemp ^ (b & 0x0f)];
        }
        return g_wCrc16CCITT;
    }
}
