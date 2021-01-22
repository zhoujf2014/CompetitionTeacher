package com.gtafe.competitionlib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class Util {

    private static final String TAG = "TEXT";
    private static final String STR_UTIL = "Util";

    public static String byteToHexString(byte[] buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        for (int i = 0; i < buffer.length; i++) {
            int j = buffer[i] & 0xFF;
            String str = Integer.toHexString(j);
            if (str.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(str + " ");
        }
        return stringBuilder.toString();
    }    public static String byteToHexStringAddr(byte[] buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        for (int i = 0; i < buffer.length; i++) {
            int j = buffer[i] & 0xFF;
            String str = Integer.toHexString(j);
            if (str.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8))
                | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public boolean hasSpecialCharacter(String str) {
        String regEx = "^[0-9]+(.[0-9]{1,})?$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public byte[] sub(byte[] bd2, int n, int len) {
        byte[] bc = new byte[len];
        for (int i = 0; i < len; i++) {
            bc[i] = bd2[n + i];
        }
        return bc;
    }


    public double DataWD(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        dectem = (double) vdec14(by);

		/*double wd = (dectem / 50 - 20);*/
        double wd = dectem / 16 - 80;  //  (mv/160-4)/16*160-40
        /*DecimalFormat df = new DecimalFormat("##0.00");
        stra = df.format(wd);
		return stra;*/
        return wd;
    }

    public double DataSD(byte[] by, int size) {
        Double dectem = (double) vdec14(by);
        double sd = (dectem / 160) / 16 * 50;  //[6.25*(mv/160)]-25
        return sd;

		/*double sd = (dectem /50);*/
        /*DecimalFormat df = new DecimalFormat("##0.00");
        stra = df.format(sd);
		return stra;*/
    }

    public String DataYX(byte[] by, int size) {
        String str = null;
        String stryx = null;
        str = by[7] + "";
        if ("0".equals(str)) {
            stryx = "无";
        } else if ("1".equals(str)) {
            stryx = "有";
        }
        return stryx;
    }

    public static boolean isFirst = true;

    public String DataGZ(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        double gzArr[] = new double[5];
        dectem = (double) vdec14(by);
        /*double gz = (dectem * 40);*/
        double gz = Math.abs(((dectem / 160) - 4) / 16 * 20000);
        if (isFirst) {
            int i;
            for (i = 0; i < 5; i++) {
                gzArr[i] = gz;
            }
            if (i == 5) {
                isFirst = false;
            }
        } else {
            for (int j = 3; j >= 0; j--) {
                gzArr[j + 1] = gzArr[j];
            }
            gzArr[0] = gz;
        }
        double gzsum = 0.0;
        for (int count = 0; count < 5; count++) {
            gzsum = gzsum + gzArr[count];
        }
        double gz2 = gzsum / 5;
        DecimalFormat df = new DecimalFormat("##0");
        stra = df.format(gz2);
        return stra;
    }

    public double DataCO2(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        dectem = (double) vdec14(by);
        /*double co2 = dectem;*/
        double co2 = (dectem / 160) / 16 * 5000;
	/*	DecimalFormat df = new DecimalFormat("##0");
		stra = df.format(co2);
		return stra;*/
        return co2;
    }

    public String DataDQY(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        dectem = (double) vdec14(by);
		/*double dqy = (dectem * 24);*/
        double dqy = (dectem / 160) / 16 * 120;
        DecimalFormat df = new DecimalFormat("##0.00");
        stra = df.format(dqy);
        return stra;
    }


    public String DataTR(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        dectem = (double) vdec14(by);
		/*double tr = (dectem /50);*/
        double tr = 6.25 * (dectem / 160) - 25;
        DecimalFormat df = new DecimalFormat("##0.00");
        stra = df.format(tr);
        return stra;
    }

    public String dataFS(byte[] by, int size) {
        Double dectem = (double) 0;
        String stra = null;
        dectem = (double) vdec14(by);
		/*double fs = (dectem *3)/500;*/
        Log.d(STR_UTIL, "fs=" + dectem);
        double fs = (dectem / 160) / 16 * 30;
        DecimalFormat df = new DecimalFormat("##0.00");
        stra = df.format(fs);
        return stra;


    }


    public int dec14(byte[] bydec) {
        String str = byteToHexString(bydec);
        String frist = str.substring(16, 18);
        String second = str.substring(14, 16);
        String hex = frist + second;
        int dec = Integer.valueOf(hex, 16);
        return dec;
    }

    public int dec18(byte[] bydec) {
        String str = byteToHexString(bydec);
        String frist = str.substring(22, 24);
        String second = str.substring(18, 20);
        String hex = frist + second;
        int dec = Integer.valueOf(hex, 16);
        return dec;
    }

    public int vdec14(byte[] bydec) {
        String str = byteToHexString(bydec);
        String frist = str.substring(16, 18);
        String second = str.substring(14, 16);
        String hex = frist + second;
        Log.d(TAG, "hex=" + hex);
        int dec = Integer.valueOf(hex, 16);
        return dec;
    }

    // CRC16
    public static int get_crc16(byte[] bufData, int buflen, byte[] pcrc) {
        int ret = 0;
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;

        if (buflen == 0) {
            return ret;
        }
        for (i = 0; i < buflen; i++) {
            CRC ^= ((int) bufData[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
            // System.out.println(Integer.toHexString(CRC));
        }

        System.out.println(Integer.toHexString(CRC));
        pcrc[0] = (byte) (CRC & 0x00ff);
        pcrc[1] = (byte) (CRC >> 8);

        return ret;
    }

    // text16
    public byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
        // return (byte) "0123456789ABCDEF".indexOf(c);
    }

    // CRC_CCITT
    public static int CRC_CCITT(byte[] bytes) {
        int crc = 0xFFFF; // initial value
        int polynomial = 0x1021;
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    public static byte dtoh(int lenby) {
        byte buffer = 0x00;
        if (lenby <= 9) {
            buffer = (byte) lenby;
        } else if (lenby > 9 && lenby < 16) {
            if (lenby == 10) {
                buffer = 0x0A;
            } else if (lenby == 11) {
                buffer = 0x0B;
            } else if (lenby == 12) {
                buffer = 0x0C;
            } else if (lenby == 13) {
                buffer = 0x0D;
            } else if (lenby == 14) {
                buffer = 0x0E;
            } else if (lenby == 15) {
                buffer = 0xF;
            }
            ;
        } else if (lenby >= 16) {
            Util util = new Util();
            byte[] epclen1 = util.hexStringToBytes(Integer.toHexString(lenby));
            buffer = epclen1[0];
        }
        return buffer;
    }

    // hex0~f
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String DataTOPO(byte[] buffer, int size) {
        StringBuffer bf = new StringBuffer();
        StringBuffer msg = null;
        String typeMsg;
        int iCount = size / 6;
        String[] s = new String[iCount];
        int[] b = new int[iCount];

        for (int j = 0, i = 0, k = 0; i < iCount; i++, k++) {
            b[k] = buffer[3 + j];
            if (i == 0) {
                s[i] = Integer.toHexString(b[k]);
                msg = bf.append(s[i]);
            } else {
                if (msg.toString().contains(Integer.toHexString(b[k]))) {
                    i = i - 1;
                    iCount = iCount - 1;
                } else {
                    s[i] = Integer.toHexString(b[k]);
                    msg = bf.append(s[i]);
                }
            }
            j = j + 6;
        }
        Log.i("hexxxxxx", msg.toString());
        typeMsg = msg.toString();

        String allMsg = Integer.toHexString(iCount) + typeMsg;
        Log.i("hexxxxxx", allMsg);
        return allMsg;
    }

    public byte address(byte[] buffer) {
        byte b = buffer[3];
        return b;
    }

    public String childNode(byte[] buffer) {
        StringBuffer csb = new StringBuffer();
        for (int i = 4; i < 11; i++) {
            String nodeMsg = Integer.toHexString(buffer[i] & 0xff)
                    .toUpperCase() + ":";
            csb.append(nodeMsg);
        }
        String childMac = csb.append(
                Integer.toHexString(buffer[11] & 0xff).toUpperCase())
                .toString();
        return childMac;
    }

    public String fatherNode(byte[] buffer) {
        StringBuffer fsb = new StringBuffer();
        for (int i = 12; i < 19; i++) {
            String nodeMsg = Integer.toHexString(buffer[i] & 0xff)
                    .toUpperCase() + ":";
            fsb.append(nodeMsg);
        }
        String fatherMac = fsb.append(
                Integer.toHexString(buffer[19] & 0xff).toUpperCase())
                .toString();
        return fatherMac;
    }

    public Boolean hextobin(byte[] buffer, int index) {
        int bb = buffer[index] & 0xFF;

        String hexToBin = Integer.toBinaryString(bb);
        String j = hexToBin.substring(0, 1);
        Boolean bin = null;
        if ("1".equals(j)) {
            bin = true;
        } else if ("0".equalsIgnoreCase(j)) {
            bin = false;
        }
        return bin;
    }

    public int Address12(byte[] buffer) {
        // String str1 = buffer[1]+"";
        // int address12 =Integer.parseInt(str1);
        int address12 = buffer[1] & 0xFF;
        return address12;
    }

    public int AddressType(byte[] buffer) {
        int str = buffer[5] & 0xFF;
        return str;
    }

    public int AddressVoltageType(byte[] buffer) {
        int str = buffer[7] & 0xFF;
        return str;
    }

    public int addr(byte[] bydec) {
        String str = byteToHexString(bydec);
        String frist = str.substring(8, 10);
        String second = str.substring(6, 8);
        String hex = frist + second;
        int dec = Integer.valueOf(hex, 16);
        return dec;
    }

    public static String byte2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append(bytes[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 通过WiFiManager获取mac地址
     *
     * @param context
     * @return
     */
    public static String tryGetWifiMac(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi == null || wi.getMacAddress() == null) {
            return null;
        }
        if ("02:00:00:00:00:00".equals(wi.getMacAddress().trim())) {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;//

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString().replace(":", "");
                }
            } catch (Exception ex) {
            }
            return "02:00:00:00:00:00";

        } else {
            return wi.getMacAddress().trim().replace(":", "");
        }
    }


    public static int crc8(byte[] buf) {
        byte i, l;
        int crc = 0;
        for (l = 0; l < buf.length - 1; l++) {
            crc ^= (buf[l] * 0x100);
            for (i = 0; i < 8; i++) {
                if ((crc & 0x8000) == 1)
                    crc ^= 0x80;
                crc = crc << 1;
            }
        }
        crc = (byte) (crc / 0x100);

        Log.e(TAG, "crc8 init=" + crc);
        return crc;
    }

    public static int FindCRC(byte[] data) {

        int CRC = 0;

        int genPoly = 0x8C;

        for (int i = 0; i < data.length - 1; i++) {

            CRC ^= data[i];

            CRC &= 0xff;//保证CRC余码输出为1字节。

            for (int j = 0; j < 8; j++) {

                if ((CRC & 0x01) != 0) {

                    CRC = (CRC >> 1) ^ genPoly;

                    CRC &= 0xff;//保证CRC余码输出为1字节。

                } else {

                    CRC >>= 1;

                }
            }

        }

        CRC &= 0xff;//保证CRC余码输出为1字节。
        return CRC;
    }


    public static short[] toGetUnsignedByte(byte[] a) {
        short[] tempByteU = null;
        int len = a.length;
        tempByteU = new short[len];
        for (int i = 0; i < len; i++) {
// 如果数据小于0，就用short类型数据与之与运算，负数在内存中以补码存放
            if (a[i] < 0) {
                tempByteU[i] = (short) (a[i] & 0x00ff);
            } else {// 大于0，不需要变
                tempByteU[i] = a[i];
            }
        }
        return tempByteU;
    }

    public static String toGetFormatDouble(double a, double b) {
        short[] tempByteU = null;
        NumberFormat nbf = NumberFormat.getInstance();
        nbf.setMinimumFractionDigits(2);
        String c = nbf.format(a / b);
        return c;
    }

    public static String getCurrentTime() {

        long l = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 E a HH:mm:ss");

        String format = dateFormat.format(l);


        return format;
    }

    public static String getCurrentPm() {

        long l = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("a");
        String format = dateFormat.format(l);
        return format;
    }

    public static String byteToHexString(byte[] buffer, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int j = buffer[i] & 0xFF;
            String str = Integer.toHexString(j);
            if (str.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(str + " ");
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte[] buffer, int start, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        if (start + len > buffer.length) {
            return "输入的索引错误";
        }
        for (int i = start; i < start + len; i++) {
            int j = buffer[i] & 0xFF;
            String str = Integer.toHexString(j);
            if (str.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(str + " ");
        }
        return stringBuilder.toString();
    }

    public static byte[] subByteArray(byte[] buffer, int start, int len) {
        byte[] bytes = new byte[len];
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        if (start + len > buffer.length) {
            return null;
        }
        for (int i = start; i < start + len; i++) {
            bytes[i-start] = buffer[i];
        }
        return bytes;
    }

    public static String byteToHexString_RFID(byte[] buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        for (int i = 0; i < buffer.length; i++) {
            int j = buffer[i] & 0xFF;
            String str = Integer.toHexString(j);
            if (str.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }
    public static String getIP(Context context){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * 字节转换为浮点
     *
     * @param b     字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 3];
        l &= 0xff;
        l |= ((long) b[index + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 0] << 24);
        return Float.intBitsToFloat(l);
    }
    //改变多路开关的状态
    public static byte[] togleSwitch(byte[] data, int i) {
        byte state = data[i];
        data[i] = (byte) (state ^ 1);
        return data;
    }
    public static String floatTString(float f, int num) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("0.");
        for (int i = 0; i < num; i++) {
            stringBuffer.append(0);
        }
        DecimalFormat decimalFormat=new DecimalFormat("0.0");

        return decimalFormat.format(f);
    }
    public static int byte2int(byte[]res,int index){

//一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets=(res[index+3]&0xff)|((res[index+2]<<8)&0xff00)//|表示安位或

                |((res[index+1]<<24)>>>8)|(res[index+0]<<24);

        return targets;

    }

    public static byte getSwitchState_(int index, byte bytes) {
        index = 7 - index;
        return (byte) (bytes >> (index) & 1);
    }

    @NonNull
    public static String getStringByOption(int option) {
        String a = "A";
        switch (option) {
            case 0:
                a = "A";
                break;
            case 1:
                a = "B";
                break;
            case 2:
                a = "C";
                break;
            case 3:
                a = "D";
                break;
            case 4:
                a = "E";
                break;
        }
        return a;
    }
}
