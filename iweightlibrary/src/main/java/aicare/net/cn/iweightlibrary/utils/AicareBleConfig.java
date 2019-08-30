package aicare.net.cn.iweightlibrary.utils;

import android.os.ParcelUuid;
import android.support.annotation.IntDef;
import android.text.SpanWatcher;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.scandecoder.ScanRecord;
import aicare.net.cn.iweightlibrary.wby.WBYManager;

/**
 * Created by Suzy on 2017/2/9.
 */

public class AicareBleConfig {
    private final static String TAG = "AicareBleConfig";

    public final static String JD_UUID = "0000d618-0000-1000-8000-00805f9b34fb";
    public final static String ALI_UUID = "0000feb3-0000-1000-8000-00805f9b34fb";

    private final static int SUM_START = 2;
    private final static int SUM_END = 7;

    private final static byte AICARE_FLAG = (byte) 0xAC;//aicare标志位
    private final static byte TYPE_WEI = 0x02;//体脂/人体秤
    private final static byte TYPE_WEI_TEMP = 0x03;//体脂/人体秤（带温度）
    private final static int AICARE_TYPE_WEI = 0x02AC;
    private final static int AICARE_TYPE_WEI_TEMP = 0x03AC;
    private final static int AICARE_TYPE_UNKNOWN = 0xFFAC;
    private final static int ALI_FLAG = 0x01A8;//阿里标志
    private final static byte[] ALI_WEI = new byte[]{0x01, 0x01, 0x1B};//阿里沃莱体重
    private final static byte[] ALI_FAT = new byte[]{0x01, 0x01, 0x1A};//阿里沃莱体脂
    private final static byte[] ALI_AICARE_FAT = new byte[]{0x01, 0x01, 0x1D};//阿里aicare体脂

    //指令
    public final static byte SYNC_HISTORY = (byte) 0xFF;
    public final static byte UPDATE_USER_OR_LIST = (byte) 0xFD;
    public final static byte SYNC_USER_INFO = (byte) 0xFB;
    public final static byte SYNC_USER_ID = (byte) 0xFA;
    public final static byte SYNC_DATE = (byte) 0xFD;
    public final static byte SYNC_TIME = (byte) 0xFC;
    public final static byte SYNC_UNIT = (byte) 0x06;
    public final static byte GET_BLE_VERSION = (byte) 0xF7;
    public final static byte SYNC_LIST_OVER = TYPE_WEI;

    //标志码
    private final static byte SYNC_HISTORY_OR_LIST = (byte) 0xCF;
    private final static byte SETTINGS = (byte) 0xCC;
    private final static byte OPERATE_OR_STATE = (byte) 0xFE;
    private final static byte WEI_CHANGE = (byte) 0xCE;
    private final static byte WEI_STABLE = (byte) 0xCA;
    private final static byte SYNC_HISTORY_STATUS = OPERATE_OR_STATE;
    private final static byte SYNC_USER_STATUS = SYNC_TIME;
    private final static byte DATA = (byte) 0xCB;
    private final static byte FAT_DATA = OPERATE_OR_STATE;
    private final static byte ADC = SYNC_DATE;
    private final static byte USER_ID = SYNC_TIME;
    private final static byte MCU_DATE = SYNC_USER_INFO;
    private final static byte MCU_TIME = SYNC_USER_ID;
    private final static byte DATA_SEND_OVER = SYNC_TIME;
    private final static byte UPDATE_USER = 0x01;

    //单位
    public final static byte UNIT_KG = 0x00;    //千克
    public final static byte UNIT_LB = 0x01;    //英镑
    public final static byte UNIT_ST = 0x02;    //英石
    public final static byte UNIT_JIN = 0x03;   //斤

    //Map对应的key
    public final static int WEIGHT_DATA = 0;
    public final static int SETTINGS_STATUS = 1;
    public final static int BLE_VERSION = 2; //蓝牙版本
    public final static int USER_ID_STR = 3;
    public final static int MCU_DATE_STR = 4;
    public final static int MCU_TIME_STR = 5;
    public final static int ADC_STR = 6;
    public final static int HISTORY_DATA = 7;
    public final static int BODY_FAT_DATA = 8;
    public final static int DID_STR = 9;

    private static boolean isHistory = false;
    private static byte deviceType = TYPE_WEI;

    @IntDef({SettingStatus.NORMAL, SettingStatus.LOW_POWER, SettingStatus.LOW_VOLTAGE, SettingStatus.ERROR,
                SettingStatus.TIME_OUT, SettingStatus.UNSTABLE, SettingStatus.SET_UNIT_SUCCESS, SettingStatus.SET_UNIT_FAILED,
                SettingStatus.SET_TIME_SUCCESS, SettingStatus.SET_TIME_FAILED, SettingStatus.SET_USER_SUCCESS, SettingStatus.SET_USER_FAILED,
                SettingStatus.UPDATE_USER_LIST_SUCCESS, SettingStatus.UPDATE_USER_LIST_FAILED, SettingStatus.UPDATE_USER_SUCCESS,
                SettingStatus.UPDATE_USER_FAILED, SettingStatus.NO_HISTORY, SettingStatus.HISTORY_START_SEND, SettingStatus.HISTORY_SEND_OVER,
                SettingStatus.NO_MATCH_USER, SettingStatus.ADC_MEASURED_ING, SettingStatus.ADC_ERROR, SettingStatus.REQUEST_DISCONNECT,
				SettingStatus.UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SettingStatus {
        int NORMAL = 0;//正常
        int LOW_POWER = 1;//低功耗
        int LOW_VOLTAGE = 2;//低电压
        int ERROR = 3;//超载
        int TIME_OUT = 4;//超时
        int UNSTABLE = 5;//称不稳定
        int SET_UNIT_SUCCESS = 6;//设置单位成功
        int SET_UNIT_FAILED = 7;//设置单位失败
        int SET_TIME_SUCCESS = 8;//设置时间成功
        int SET_TIME_FAILED = 9;//设置时间失败
        int SET_USER_SUCCESS = 10;//设置用户成功
        int SET_USER_FAILED = 11;//设置用户失败
        int UPDATE_USER_LIST_SUCCESS = 12;//更新用户列表成功
        int UPDATE_USER_LIST_FAILED = 13;//更新用户列表失败
        int UPDATE_USER_SUCCESS = 14;//更新用户成功
        int UPDATE_USER_FAILED = 15;//更新用户失败
        int NO_HISTORY = 16;//没有历史数据
        int HISTORY_START_SEND = 17;//历史数据开始发送
        int HISTORY_SEND_OVER = 18;//历史数据发送完成
        int NO_MATCH_USER = 19;//没有匹配的用户
        int ADC_MEASURED_ING = 20;//阻抗测量中
        int ADC_ERROR = 21;//阻抗测量失败
        int REQUEST_DISCONNECT = 22;//ble请求断开
        int UNKNOWN = -1;//未知
    }

    /**
     * 初始化指令
     *
     * @param index
     * @param user
     * @param unitType
     * @return
     */
    public static byte[] initCmd(byte index, User user, byte unitType) {
        byte[] b = new byte[8];
        b[0] = AICARE_FLAG;
        b[1] = deviceType;
        if (user != null) {
            L.e(TAG, "syncUser: " + user.toString());
        }
        switch (index) {
            case SYNC_HISTORY:
                b[2] = SYNC_HISTORY;
                b[6] = SYNC_HISTORY_OR_LIST;
                break;
            case SYNC_USER_ID:
                b[2] = SYNC_USER_ID;
                b[3] = (byte) user.getId();
                b[6] = SETTINGS;
                break;
            case SYNC_USER_INFO:
                b[2] = SYNC_USER_INFO;
                b[3] = (byte) user.getSex();
                b[4] = ((Integer) user.getAge()).byteValue();
                b[5] = ((Integer) user.getHeight()).byteValue();
                b[6] = SETTINGS;
                break;
            case SYNC_DATE:
                String date = ParseData.getDate();
                b[2] = SYNC_DATE;
                b[3] = Integer.valueOf(date.substring(0, 2)).byteValue();
                b[4] = Integer.valueOf(date.substring(2, 4)).byteValue();
                b[5] = Integer.valueOf(date.substring(4, 6)).byteValue();
                b[6] = SETTINGS;
                break;
            case SYNC_TIME:
                String time = ParseData.getTime();
                b[2] = SYNC_TIME;
                b[3] = Integer.valueOf(time.substring(0, 2)).byteValue();
                b[4] = Integer.valueOf(time.substring(2, 4)).byteValue();
                b[5] = Integer.valueOf(time.substring(4, 6)).byteValue();
                b[6] = SETTINGS;
                break;
            case SYNC_UNIT:
                b[2] = OPERATE_OR_STATE;
                b[3] = SYNC_UNIT;
                b[4] = unitType;
                b[6] = SETTINGS;
                break;
            case GET_BLE_VERSION:
                b[2] = GET_BLE_VERSION;
                b[6] = SETTINGS;
                break;
            case SYNC_LIST_OVER:
                b[2] = UPDATE_USER_OR_LIST;
                b[3] = SYNC_LIST_OVER;
                b[6] = SYNC_HISTORY_OR_LIST;
                break;
        }
        b[7] = getByteSum(b);
        L.e(TAG, "initCmd: " + ParseData.byteArr2Str(b));
        return b;
    }

    /**
     * 初始化更新用户信息的指令
     *
     * @param user
     * @return
     */
    public static byte[] initUpdateUserCmd(User user) {
        L.e(TAG, "updateUser: " + user.toString());
        byte[] b = new byte[20];
        b[0] = AICARE_FLAG;
        b[1] = deviceType;
        b[2] = UPDATE_USER_OR_LIST;
        b[3] = UPDATE_USER;
        initUserListByteArray(b, 4, user);
        L.e(TAG, "initUpdateUserCmd: " + ParseData.byteArr2Str(b));
        return b;
    }

    /**
     * 计算出byte[start]~byte[end]的总和（取低位8个字节）
     *
     * @param b
     * @return
     */
    private static byte getByteSum(byte[] b) {
        if (b[0] == AICARE_FLAG && (b[1] == TYPE_WEI || b[1] == TYPE_WEI_TEMP)) {
            int j = 0;
            for (int i = SUM_START; i < SUM_END; i++) {
                j += b[i];
            }
            int result = j & 0xFF;
            return (byte) result;
        }
        return (byte) 0xFF;
    }

    /**
     * 校验数据是否正确
     *
     * @param b
     * @return
     */
    private static boolean checkData(byte[] b) {
        if (b.length == 8) {
            byte result = getByteSum(b);
            L.e(TAG, "result = " + result);
            L.e(TAG, "b[SUM_END] = " + b[SUM_END]);
            if (result == b[SUM_END]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化用户列表
     *
     * @param userList
     * @return
     */
    public static List<byte[]> initUserListCmds(List<User> userList) {
        List<byte[]> bytes = new ArrayList<>();
        byte[] b = new byte[]{};
        for (int i = 0; i < userList.size(); i++) {
            L.e(TAG, "userList: " + userList.get(i).toString());
            if (i % 2 == 0) {
                b = new byte[20];
                b[0] = AICARE_FLAG;
                b[1] = deviceType;
                b[2] = UPDATE_USER_OR_LIST;
                initUserListByteArray(b, 4, userList.get(i));
                if (i == userList.size() - 1) {
                    bytes.add(b);
                }
            } else {
                initUserListByteArray(b, 12, userList.get(i));
                bytes.add(b);
            }
        }
        return bytes;
    }

    private static void initUserListByteArray(byte[] value, int baseIndex, User user) {
        value[baseIndex] = (byte) user.getId();
        value[++baseIndex] = (byte) user.getSex();
        value[++baseIndex] = (byte) user.getAge();
        value[++baseIndex] = (byte) user.getHeight();
        byte[] weightByte = ParseData.int2byte(user.getWeight());
        value[++baseIndex] = weightByte[0];
        value[++baseIndex] = weightByte[1];
        byte[] adcByte = ParseData.int2byte(user.getAdc());
        value[++baseIndex] = adcByte[0];
        value[++baseIndex] = adcByte[1];
    }

    /**
     * 是否为Aicare设备
     *
     * @param scanRecord
     * @return
     */
    public static int isAicareDevice(byte[] scanRecord) {
        ScanRecord scanResult = ScanRecord.parseFromBytes(scanRecord);
        if (scanResult != null) {
            SparseArray<byte[]> manufacturerData = scanResult.getManufacturerSpecificData();
            List<ParcelUuid> uuidList = scanResult.getServiceUuids();
            if (!isListEmpty(uuidList)) {
                for (ParcelUuid uuid : uuidList) {
                    L.e(TAG, "uuid: " + uuid.getUuid().toString());
                }
                if (manufacturerData != null) {
                    int manufacturerId = manufacturerData.keyAt(0);
                    byte[] specificData = manufacturerData.get(manufacturerId);
                    if (isContainJD(uuidList)) {
                        L.e(TAG, "JD manufacturerId: " + manufacturerId);
                        if (manufacturerId == AICARE_TYPE_WEI || manufacturerId == AICARE_TYPE_WEI_TEMP || manufacturerId == AICARE_TYPE_UNKNOWN) {
                            setDeviceType(manufacturerId);
                            return getFlag(specificData);
                        }
                    } else if (isContainALI(uuidList)) {
                        L.e(TAG, "ALI manufacturerId: " + manufacturerId);
                        if (manufacturerId == ALI_FLAG) {
                            L.e(TAG, "specificData: " + ParseData.byteArr2Str(specificData));
                            if (!isArrEmpty(specificData) && (isArrStartWith(specificData, ALI_WEI) || isArrStartWith(specificData, ALI_FAT) || isArrStartWith(specificData, ALI_AICARE_FAT))) {
                                setDeviceType(specificData);
                                return getFlag(specificData);
                            }
                        }
                    } else if (uuidList.contains(ParcelUuid.fromString(WBYManager.AICARE_SERVICE_UUID_STR))) {
                        L.e(TAG, "Aicare manufacturerId: " + manufacturerId);
                        if (manufacturerId == AICARE_TYPE_WEI || manufacturerId == AICARE_TYPE_WEI_TEMP || manufacturerId == AICARE_TYPE_UNKNOWN) {
                            setDeviceType(manufacturerId);
                            return getFlag(specificData);
                        }
                    }
                }

            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * 设置设备类型
     * @param manufacturerId
     */
    private static void setDeviceType(int manufacturerId) {
        switch (manufacturerId) {
            case AICARE_TYPE_WEI:
                deviceType = TYPE_WEI;
                break;
            case AICARE_TYPE_WEI_TEMP:
                deviceType = TYPE_WEI_TEMP;
                break;
        }
    }

    /**
     * 设置设备类型
     * @param specificData
     */
    private static void setDeviceType(byte[] specificData) {
        if (isArrStartWith(specificData, ALI_WEI)) {
            deviceType = TYPE_WEI;
        } else if (isArrStartWith(specificData, ALI_AICARE_FAT) || isArrStartWith(specificData, ALI_FAT)) {
            deviceType = TYPE_WEI_TEMP;
        } else {
            deviceType = TYPE_WEI;
        }
    }

    /**
     * 获取亮屏标志
     * @param data
     * @return
     */
    private static int getFlag(byte[] data) {
        if (data.length < 7) {
            return Integer.MAX_VALUE;
        } else {
            return data[6];
        }
    }

    /**
     * 判断数组src是否以数组tar开始
     *
     * @param src
     * @param tar
     * @return
     */
    private static boolean isArrStartWith(byte[] src, byte[] tar) {
        if (src.length != 0 && tar.length != 0) {
            for (int i = 0; i < tar.length; i++) {
                if (tar[i] != src[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 是否包含UUID{@link #JD_UUID}
     *
     * @param uuidList
     * @return
     */
    private static boolean isContainJD(List<ParcelUuid> uuidList) {
        if (uuidList.contains(ParcelUuid.fromString(JD_UUID)) && uuidList.contains(ParcelUuid.fromString(WBYManager.AICARE_SERVICE_UUID_STR))) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含UUID{@link #ALI_UUID}
     *
     * @param uuidList
     * @return
     */
    private static boolean isContainALI(List<ParcelUuid> uuidList) {
        if (uuidList.contains(ParcelUuid.fromString(ALI_UUID)) && uuidList.contains(ParcelUuid.fromString(WBYManager.AICARE_SERVICE_UUID_STR))) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否为空
     *
     * @param b
     * @return
     */
    private static boolean isArrEmpty(byte[] b) {
        if (b == null || b.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @param <T>
     * @return
     */
    private static <T> boolean isListEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取设备端返回值
     *
     * @param b
     * @return
     */
    public static SparseArray<Object> getDatas(byte[] b) {
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (checkData(b)) {
            switch (b[6]) {
                case WEI_CHANGE:
                case WEI_STABLE:
                    sparseArray = getWeightData(b);
                    break;
                case DATA:
                    sparseArray = getData(b);
                    break;
                case SETTINGS:
                    sparseArray = getDeviceStatus(b);
                    break;
                case SYNC_HISTORY_OR_LIST:
                    if (b[2] == SYNC_HISTORY_STATUS) {
                        sparseArray = getHistoryStatus(b);
                    } else if (b[2] == SYNC_USER_STATUS) {
                        sparseArray = getSyncUserStatus(b);
                    }
                    break;
            }
        } else if (isHistoryData(b)) {
            byte[] historyBytes = getConcatBytes(b, 3);
            sparseArray = getHistoryData(historyBytes);
        }
        return sparseArray;
    }

    private static BodyFatData bodyFatData = null;

    /**
     * 获取体脂数据
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getBodyFatData(byte[] b) {
        L.i(AicareBleConfig.class, "4-----------------getBodyFatData");
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (bodyFatData == null) {
            bodyFatData = new BodyFatData();
        }
        switch (b[3]) {
            case 0x00:
                double weight = ParseData.getData(4, 5, b);
                bodyFatData.setWeight(weight);
                break;
            case 0x01:
                double bmi = ParseData.getData(4, 5, b);
                bodyFatData.setBmi(bmi);
                break;
            case 0x02:
                double bfr = ParseData.getData(4, 5, b);
                bodyFatData.setBfr(bfr);
                break;
            case 0x03:
                double sfr = ParseData.getData(4, 5, b);
                bodyFatData.setSfr(sfr);
                break;
            case 0x04:
                int uvi = ParseData.getDataInt(4, 5, b);
                bodyFatData.setUvi(uvi);
                break;
            case 0x05:
                double rom = ParseData.getData(4, 5, b);
                bodyFatData.setRom(rom);
                break;
            case 0x06:
                double bmr = ParseData.getData(4, 5, b) * 10;//2017-07-12, 补上*10
                bodyFatData.setBmr(bmr);
                break;
            case 0x07:
                double bm = ParseData.getData(4, 5, b);
                bodyFatData.setBm(bm);
                break;
            case 0x08:
                double vwc = ParseData.getData(4, 5, b);
                bodyFatData.setVwc(vwc);
                break;
            case 0x09:
                int age = (int) ParseData.getData(4, 5, b);
                bodyFatData.setAge(age);
                break;
            case 0x0A:
                double pp = ParseData.getData(4, 5, b);
                bodyFatData.setPp(pp);
                break;
            case DATA_SEND_OVER:
                sparseArray.put(BODY_FAT_DATA, bodyFatData);
                bodyFatData = null;
                break;
        }
        return sparseArray;
    }

    /**
     * 获取mcu返回的用户信息和时间
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getData(byte[] b) {
        L.i(AicareBleConfig.class, "3----------------getData");
        SparseArray<Object> sparseArray = new SparseArray<>();
        switch (b[2]) {
            case FAT_DATA:
                sparseArray = getBodyFatData(b);
                break;
            case ADC:
                sparseArray = getADC(b);
                break;
            case USER_ID:
                sparseArray = getUserId(b);
                break;
            case MCU_DATE:
                sparseArray.put(MCU_DATE_STR, getDateOrTime(MCU_DATE, b, 3));
                break;
            case MCU_TIME:
                sparseArray.put(MCU_TIME_STR, getDateOrTime(MCU_TIME, b, 3));
                break;
        }
        return sparseArray;
    }

    /**
     * 获取返回的日期或时间
     *
     * @param type
     * @param b
     * @return
     */
    private static String getDateOrTime(byte type, byte[] b, int index) {
        L.i(AicareBleConfig.class, "getDateOrTime");
        StringBuffer sBuffer = new StringBuffer();
        String yearOrHour = String.valueOf(ParseData.binaryToDecimal(b[index]));
        String monthOrMinute = String.valueOf(ParseData.binaryToDecimal(b[++index]));
        String dayOrSecond = String.valueOf(ParseData.binaryToDecimal(b[++index]));
        switch (type) {
            case MCU_DATE:
                sBuffer.append(ParseData.addZero(yearOrHour));
                sBuffer.append("-");
                sBuffer.append(ParseData.addZero(monthOrMinute));
                sBuffer.append("-");
                sBuffer.append(ParseData.addZero(dayOrSecond));
                L.i(AicareBleConfig.class, "MCU_DATE = " + sBuffer.toString());
                break;
            case MCU_TIME:
                sBuffer.append(ParseData.addZero(yearOrHour));
                sBuffer.append(":");
                sBuffer.append(ParseData.addZero(monthOrMinute));
                sBuffer.append(":");
                sBuffer.append(ParseData.addZero(dayOrSecond));
                L.i(AicareBleConfig.class, "MCU_TIME = " + sBuffer.toString());
                break;
        }
        return sBuffer.toString();
    }

    /**
     * 获取阻抗值和阻抗值发送状态
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getADC(byte[] b) {
        L.i(AicareBleConfig.class, "-----------------getADC");
        SparseArray<Object> map = new SparseArray<>();
        switch (b[3]) {
            case 0x00:
                map.put(SETTINGS_STATUS, SettingStatus.ADC_MEASURED_ING);
                break;
            case 0x01:
                map.put(ADC_STR, ParseData.getDataInt(4, 5, b));
                break;
            case (byte) 0xFF:
                map.put(SETTINGS_STATUS, SettingStatus.ADC_ERROR);
                break;
        }
        return map;
    }

    /**
     * 获取mcu返回的用户编号
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getUserId(byte[] b) {
        L.i(AicareBleConfig.class, "getUserId");
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (b[3] == 0x7F) {
            sparseArray.put(SETTINGS_STATUS, SettingStatus.NO_MATCH_USER);
        } else {
            sparseArray.put(USER_ID_STR, String.valueOf(ParseData.binaryToDecimal(b[3])));
        }
        return sparseArray;
    }

    /**
     * 获取体重数据
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getWeightData(byte[] b) {
        SparseArray<Object> sparseArray = new SparseArray<>();
        double weight = ParseData.getData(2, 3, b);
        double temp = Double.MAX_VALUE;
        boolean isStable = false;
        if (b[1] == TYPE_WEI_TEMP) {
            temp = getTemp(4, 5, b);
        }
        if (b[6] == WEI_CHANGE) {
            isStable = false;
        }
        if (b[6] == WEI_STABLE) {
            isStable = true;
        }
        sparseArray.put(WEIGHT_DATA, new WeightData(isStable, weight, temp));
        return sparseArray;
    }

    /**
     * 获取mcu返回的历史数据状态
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getHistoryStatus(byte[] b) {
        L.i(AicareBleConfig.class, "getHistoryStatus");
        SparseArray<Object> sparseArray = new SparseArray<>();
        switch (b[3]) {
            case 0x00:
                sparseArray.put(SETTINGS_STATUS, SettingStatus.NO_HISTORY);
                break;
            case 0x01:
                isHistory = true;
                sparseArray.put(SETTINGS_STATUS, SettingStatus.HISTORY_START_SEND);
                break;
            case 0x02:
                isHistory = false;
                sparseArray.put(SETTINGS_STATUS, SettingStatus.HISTORY_SEND_OVER);
                break;
        }
        return sparseArray;
    }

    /**
     * 获取同步用户状态
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getSyncUserStatus(byte[] b) {
        L.i(AicareBleConfig.class, "getSyncUserStatus");
        SparseArray<Object> sparseArray = new SparseArray<>();
        switch (b[3]) {
            case 0x00://同步用户列表成功
                sparseArray.put(SETTINGS_STATUS, SettingStatus.UPDATE_USER_LIST_SUCCESS);
                break;
            case 0x01://同步用户列表失败
                sparseArray.put(SETTINGS_STATUS, SettingStatus.UPDATE_USER_FAILED);
                break;
            case 0x02://更新个人信息成功
                sparseArray.put(SETTINGS_STATUS, SettingStatus.UPDATE_USER_SUCCESS);
                break;
            case 0x03://更新个人信息失败
                sparseArray.put(SETTINGS_STATUS, SettingStatus.UPDATE_USER_FAILED);
                break;
        }
        return sparseArray;
    }

    /**
     * 获取设备状态
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getDeviceStatus(byte[] b) {
        L.i(AicareBleConfig.class, "-----------------getDeviceStatus");
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (b[2] == (byte) 0xF7) {
            String version = getVersion(b);
            sparseArray.put(BLE_VERSION, version);
        } else {
            switch (b[3]) {
                case 0x00:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.NORMAL);
                    break;
                case 0x01:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.LOW_POWER);
                    break;
                case 0x02:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.LOW_VOLTAGE);
                    break;
                case 0x03:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.ERROR);
                    break;
                case 0x04:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.TIME_OUT);
                    break;
                case 0x05:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.UNSTABLE);
                    break;
                case 0x06:
                    sparseArray = getChangeUnitStatus(b);
                    break;
                case 0x07:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_TIME_SUCCESS);
                    break;
                case 0x08:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_TIME_FAILED);
                    break;
                case 0x09:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_USER_SUCCESS);
                    break;
                case 0x0A:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_USER_FAILED);
                    break;
                case 0x1B:
                    sparseArray.put(SETTINGS_STATUS, SettingStatus.REQUEST_DISCONNECT);
                    break;
            }
        }
        return sparseArray;
    }

    /**
     * 获取蓝牙版本
     *
     * @param b
     * @return
     */
    private static String getVersion(byte[] b) {
        int year = ParseData.binaryToDecimal(b[3]) / 16 + 15;
        int month = ParseData.binaryToDecimal(b[3]) % 16;
        int day = ParseData.binaryToDecimal(b[4]);
        float version = (float) ParseData.binaryToDecimal(b[5]);
        BigDecimal bDecimal = new BigDecimal(version / 10);
        version = (float) bDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(year);
        stringBuffer.append(month);
        stringBuffer.append(day);
        stringBuffer.append("_");
        stringBuffer.append(version);
        return stringBuffer.toString();
    }

    /**
     * 获取切换单位状态
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getChangeUnitStatus(byte[] b) {
        L.i(AicareBleConfig.class, "-----------------getChangeUnitStatus");
        SparseArray<Object> sparseArray = new SparseArray<>();
        switch (b[4]) {
            case (byte) 0xFE:
                sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_UNIT_SUCCESS);
                break;
            case (byte) 0xFF:
                sparseArray.put(SETTINGS_STATUS, SettingStatus.SET_UNIT_FAILED);
                break;
        }
        return sparseArray;
    }

    /**
     * 获取温度
     *
     * @param first
     * @param second
     * @param b
     * @return
     */
    private static double getTemp(int first, int second, byte[] b) {
        L.i(AicareBleConfig.class, "getTemp");
        StringBuffer sBuffer = new StringBuffer();
        String str = ParseData.byteToBit(b[first]);    //温度的单位和高位
        byte unit = ParseData.bitToByte(str.substring(0, 4));
        byte high = ParseData.bitToByte(str.substring(4, 8));
        byte[] tempArr = new byte[]{high, b[second]};
        double tempDou = ParseData.getData(0, 1, tempArr);
        L.i(AicareBleConfig.class, "temp unit = " + unit);
        L.i(AicareBleConfig.class, "temp high = " + high);
        if (unit == 0x0F) {
            sBuffer.append("-");
        }
        sBuffer.append(tempDou);
        return Double.parseDouble(sBuffer.toString());
    }

    private static byte[] preByte = null;

    /**
     * 是否是历史数据
     *
     * @param b
     * @return
     */
    private static boolean isHistoryData(byte[] b) {
        L.i(AicareBleConfig.class, "isHistoryData");
        if (b.length == 20 && (isHistoryStart(b) || b[0] == 0x01)) {
            preByte = b;
            return true;
        } else {
            if (preByte != null) {
                if (isHistoryStart(preByte) && preByte[3] == 0x00 && b[0] == 0x01) {
                    preByte = null;
                    return true;
                }
            }
        }
        preByte = null;
        return false;
    }

    /**
     * 是否为历史数据的标志位
     *
     * @param b
     * @return
     */
    private static boolean isHistoryStart(byte[] b) {
        if (b[0] == AICARE_FLAG && (b[1] == TYPE_WEI || b[1] == TYPE_WEI_TEMP) && b[2] == SYNC_HISTORY) {
            return true;
        }
        return false;
    }

    private static byte[] mByte = null;

    /**
     * 拼接两个数据包
     *
     * @param b
     * @param index
     * @return
     */
    private static byte[] getConcatBytes(byte[] b, int index) {
        if (mByte != null) {
            if ((b[0] - mByte[index]) == 1) {
                byte[] result = ParseData.concat(mByte, b);
                resetParam();
                return result;
            }
            resetParam();
        } else {
            mByte = b;
        }
        return b;
    }

    private static void resetParam() {
        mByte = null;
    }

    /**
     * 获取历史数据中体脂数据
     */
    private static SparseArray<Object> getHistoryData(byte[] b) {
        L.i(AicareBleConfig.class, "getHistoryData");
        L.i(AicareBleConfig.class, "b.length = " + b.length);
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (b.length > 20) {
            String date = getDateOrTime(MCU_DATE, b, 6);
            String time = getDateOrTime(MCU_TIME, b, 9);
            double weight = ParseData.getData(12, 13, b);
            double bmi = ParseData.getData(14, 15, b);
            double bfr = ParseData.getPercent(ParseData.getData(16, 17, b));
            double sfr = ParseData.getData(18, 19, b);
            int uvi = ParseData.getDataInt(21, 22, b);
            double rom = ParseData.getPercent(ParseData.getData(23, 24, b));
            double bmr = ParseData.getData(25, 26, b) * 10;
            double bm = ParseData.getData(27, 28, b);
            double vwc = ParseData.getPercent(ParseData.getData(29, 30, b));
            int bodyAge = ParseData.binaryToDecimal(b[31]);
            double pp = ParseData.getData(32, 33, b);
            int number = ParseData.binaryToDecimal(b[34]);
            int sex = ParseData.binaryToDecimal(b[35]);
            int age = ParseData.binaryToDecimal(b[36]);
            int height = ParseData.binaryToDecimal(b[37]);
            int adc = ParseData.getDataInt(38, 39, b);
            BodyFatData bodyFatData = new BodyFatData(date, time, weight, bmi, bfr, sfr, uvi, rom, bmr, bm, vwc, bodyAge, pp, number, sex, age, height, adc);
            if (isHistory) {
                sparseArray.put(HISTORY_DATA, bodyFatData);
            } else {
                sparseArray.put(BODY_FAT_DATA, bodyFatData);
            }
        }
        return sparseArray;
    }
}
