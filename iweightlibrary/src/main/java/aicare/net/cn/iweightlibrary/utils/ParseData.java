package aicare.net.cn.iweightlibrary.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ParseData {

    /**
     * byte转16进制
     *
     * @param b
     * @return
     */
    public static String binaryToHex(byte b) {
        String str = byteToBit(b);
        String hexStr = Integer.toHexString(Integer.parseInt(str, 2));
        StringBuffer stringBuffer = new StringBuffer();
        if (hexStr.length() == 1) {
            stringBuffer.append("0");
        }
        stringBuffer.append(hexStr);
        return stringBuffer.toString().toUpperCase();
    }

    /**
     * byte转十进制
     *
     * @param b
     * @return
     */
    public static int binaryToDecimal(byte b) {
        String str = byteToBit(b);
        return Integer.parseInt(str, 2);
    }

    /**
     * Byte转Bit
     */
    public static String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * int转byte
     *
     * @param res
     * @return
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[2];

        targets[1] = (byte) (res & 0xff);// 最低位
        targets[0] = (byte) ((res >> 8) & 0xff);// 次低位
        /*targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移*/
        return targets;
    }

    /**
     * Bit转Byte
     */
    public static byte bitToByte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    /**
     * 解析体脂数据
     *
     * @param first
     * @param second
     * @param b
     * @return
     */
    public static double getData(int first, int second, byte[] b) {
        double data = ((b[first] & 0xFF) << 8) + (b[second] & 0xFF);
        L.e(ParseData.class, "data = " + data);
        return data / 10;
    }

    /**
     * 解析体脂数据
     *
     * @param first
     * @param second
     * @param b
     * @return
     */
    public static int getDataInt(int first, int second, byte[] b) {
        int data = ((b[first] & 0xFF) << 8) + (b[second] & 0xFF);
        L.e(ParseData.class, "getDataInt = " + data);
        return data;
    }


    // 获取系统时间
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    // 获取系统日期
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前系统时间
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 合并数组
     *
     * @return
     */
    public static byte[] concat(byte[] b, byte[] mByte) {
        byte[] result = Arrays.copyOf(b, b.length + mByte.length);
        System.arraycopy(mByte, 0, result, b.length, mByte.length);
        return result;
    }

    /**
     * 字符串长度等于1的话，补0
     *
     * @param str
     * @return
     */
    public static String addZero(String str) {
        //L.i(ParseData.class, "addZero");
        StringBuffer sBuffer = new StringBuffer();
        if (str.length() == 1) {
            sBuffer.append("0");
            sBuffer.append(str);
            return sBuffer.toString();
        } else {
            return str;
        }
    }

    public static double getPercent(double d) {
        if (d > 100) {
            return 0.0;
        }
        return d;
    }

    /**
     * byte数组转str
     * @param b
     * @return
     */
    public static String byteArr2Str(byte[] b) {
        if (b.length != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            for (int i = 0; i < b.length; i++) {
                stringBuffer.append(addZero(binaryToHex(b[i])));
                if (i < b.length - 1) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
        return null;
    }

    /**
     * 保留小数点后count位
     *
     * @param d
     * @param count
     * @return
     */
    public static double keepDecimal(double d, int count) {
        BigDecimal decimal = new BigDecimal(d);
        return decimal.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * kg转lb
     * @param weight
     * @return
     */
    public static double kg2lb(double weight) {
        return keepDecimal(weight * 2.2046226, 1);
    }

    /**
     * kg转jin
     * @param weight
     * @return
     */
    public static double kg2jin(double weight) {
        return keepDecimal(weight * 2, 1);
    }

    /**
     * kg转st
     * @param weight
     * @return
     */
    public static String kg2st(double weight) {
        double lbData = kg2lb(weight);
        StringBuffer stringBuffer = new StringBuffer();
        int lb = (int) (lbData / 14);
        int st = (int) lbData % 14;
        stringBuffer.append(lb);
        stringBuffer.append(":");
        stringBuffer.append(addZero(String.valueOf(st)));
        return stringBuffer.toString();
    }

    public static String int2HexStr(int i) {
        return binaryToHex(Integer.valueOf(i).byteValue());
    }
}
