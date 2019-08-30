package com.datalife.datalife_company.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.bean.LoginBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LG on 2019/7/16.
 */

public class IDatalifeConstant {

    //连接蓝牙返回值
    public static final int MESSAGESERVICE = 0x123;
    /**
     * 蓝牙搜索信息
     */
    public static final int MESSAGEMACHINE = 0x1122;

    //服务器返回错误
    public static String RESULT_SUCCESS = "success";
    public static String RESULT_FAIL = "fail";

    /**
     * 登陆跳转注册页面
     */
    public static final int RESULT_REGISTER = 0x1234;

    /**
     * 主界面跳转登录
     */
    public static final int INTENT_LOGIN =0x0012;

    /**
     * 跳转设置界面
     */
    public static final int SETTINGREQUESTCODE = 0x232;

    /**
     * 蓝牙返回值
     */
    public static final int REQUEST_OPEN_BT = 0x23;
    public static final int REQUEST_COARSE_LOCATION = 0x24;

    public static final String TYPE = "type";
    public static final int TYPE_BACK = 0x0003;


    public static final int BTINT = 2;//体温的project_id
    public static final int SPO2HINT = 3;//血氧的project_id
    public static final int ECGINT = 5;//心电图的project_id
    public static final int BPINT = 1;//血压的project_id

    /**
     * HealthDialProgress自定义数据
     */
    public static final int DEFAULT_SIZE = 150;
    public static final int DEFAULT_START_ANGLE = 270;
    public static final int DEFAULT_SWEEP_ANGLE = 360;
    public static final int DEFAULT_ANIM_TIME = 1000;
    public static final int DEFAULT_MAX_VALUE = 100;
    public static final int DEFAULT_VALUE = 50;
    public static final int DEFAULT_HINT_SIZE = 15;
    public static final int DEFAULT_UNIT_SIZE = 30;
    public static final int DEFAULT_VALUE_SIZE = 15;
    public static final int DEFAULT_ARC_WIDTH = 15;

    public static final int VISIBLEHEALTH = 0x12341;
    public static final int CHARGINGHEALTH = 0x1222;
    public static final int POWER_BATTERY = 0X666;

    public static final int MESSAGEWHAT_CHART = 0x112;

    public static final Display display(Context context){
        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay;
    }

    //检测历史记录
    public static final int DAY_History = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    public static final String APP_ID = "wx7b154709878a1cbe";
    public static final String SECRET = "b92b65a7e3452eb4714685abd1ad3b73";

    public static final int PAGE_HOME = 0;
    public static final int PAGE_BP = 1;
    public static final int PAGE_TEMP = 2;
    public static final int PAGE_SPO2H = 3;
    public static final int PAGE_ECG = 4;
    public static final int PAGE_EQUIT = 5;

    public static final int HEALTH_HISTORY_PAGE_HEART = 1;
    public static final int HEALTH_HISTORY_PAGE_BP = 2;
    public static final int HEALTH_HISTORY_PAGE_TEMP = 3;
    public static final int HEALTH_HISTORY_PAGE_SPO2H = 0;
    public static final int HEALTH_HISTORY_PAGE_ECG = 4;

    public static final int IMAGEBUNDLE = 0x221;
    public static final int REQUEST_CAMERA = 0x222;

    public static final String MACHINE_FAT = "1";
    public static final String MACHINE_HEALTH = "2";
    public static final String MACHINE_BRUSH = "3";

    public static final int PAGE_HOMEPAGE = 0;
    public static final int PAGE_MALL = 1;
    public static final int PAGE_HEALTHHOME = 2;
    public static final int PAGE_ME = 3;

    public static final int TOOTH_MODE = 1;
    public static final int TOOTH_TIME = 2;

    public static final int TYPE_HEALTH = 1;
    public static final int TYPE_FAT = 2;
    public static final int TYPE_TOOTH = 3;


    public static final int COMMOMHANDLERMACHINE = 0x112234;
    public static final int COMMOMHANDLERMEMBER = 0x112244;


    public static final String BUNDLEMEMBER = "bundle";
    public static final String BUNDLEMEMBERID = "memberid";
    public static final String BUNDLEMEASURE = "Measure";
    public static final String BUNDLEMACHINEMEMBER = "MACHINEMEMBER";
    public static final String SESSIONID = "SESSIONID";
    public static final String FAMILYUSERINFO = "familyUserInfo";
    public static final String OPENID = "OPENID";
    public static final String UNIONID = "UNIONID";
    public static final String DAY = "DAY";

    public static final String PAGE = "page";

    public static final String LOGIN = "login";
    public static final String REGISTER = "register";

    public static final String SWAN = "SWAN";

    public static final String ZSONIC = "ZSONIC";

    public static final String USAGEPROTOCOL_URL = "https://auth.100zt.com/News/showAgreement/52";

    public static final String HELP_URL = "https://appapi.100zt.com/home/helpcenter";

    /**
     * 序列化对象
     * @param loginBean
     * @return
     * @throws IOException
     */
    public static String serialize(LoginBean loginBean) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(loginBean);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 序列化对象
     * @return
     * @throws IOException
     */
    public static String serialize(Serializable t) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(t);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LoginBean deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        LoginBean loginBean = (LoginBean) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable unSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Serializable loginBean = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 保存数据
     * @param context
     * @param str
     */
    public static void saveData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("logininfo",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBtData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bt",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBpData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bp",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveSpo2hData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("spo2h",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveEcgData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("ecg",str).commit();
    }

    /**
     * 获取测量数据
     */
    public  static String getTestData(Context context,String title){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(title,"");
        return  str;
    }

    /**
     * 获取登录信息
     * @param context
     * @return
     */
    public static LoginBean getLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)IDatalifeConstant.deSerialization(str);

                return loginBean;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 得到数据
     * @param context
     * @return
     */
    public static String getData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("logininfo","");
        return  str;
    }

    /**
     * 去除单位的方法
     */
    public static final String getPlusUnit(String str){

        String resultStr = str.substring(0,str.length()-2);
        return resultStr;

    }


    /**
     *播放提示音
     * @param context
     */
    public static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    /**
     * 获取当前时间
     * @return
     */
    public  static String getCurrentTime(){
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(day);
    }


    //出生日期字符串转化成Date对象
    public static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(strDate);
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }


    public static final int[] getColor(Context context){
        int[] colors = new int[]{context.getResources().getColor(R.color.dial_blue), context.getResources().getColor(R.color.dial_green), context.getResources().getColor(R.color.dial_red)};
        return colors;
    }

    public static int getAgeByBirthDay(String birthDay){
        if (birthDay == null || birthDay.length()<4) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到当前的年份
        String cYear = sdf.format(new Date()).substring(0,4);
        String cMouth = sdf.format(new Date()).substring(5,7);
        String cDay = sdf.format(new Date()).substring(8,10);
        //得到生日年份
        String birth_Year = birthDay .substring(0,4);
        String birth_Mouth = birthDay .substring(5,7);
        String birth_Day = birthDay .substring(8,10);
        int age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))<0) {
            age=age-1;
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))==0) {
            if ( (Integer.parseInt(cDay) - Integer.parseInt(birth_Day))>0) {
                age=age-1;
            }else {
                age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
            }
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))>0) {
            age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        }
        return age;
    }

    public static int getInt(String num){
        if (num == null){
            return 0;
        }
        double number = Double.valueOf(num);
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }



    public static String getUniqueId(Context context){
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    //测试账号的sessionid
    public static String getMeasureUniqueId(Context context){
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL + "Measure";
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }

    /**
     * 获取头像
     * @param str
     * @param iv
     */
    public static void GetPIC(Context context ,String str , ImageView iv){
        if (str.equals(DefaultPicEnum.PIC1.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC1.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC2.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC2.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC3.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC3.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC4.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC4.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC5.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC5.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC6.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC6.getResPic()));
        }
    }

    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    public static int measure(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision
     * @return
     */
    public static String getPrecisionFormat(int precision) {
        return "%." + precision + "f";
    }

    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> T[] reverse(T[] arrays) {
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    /**
     * 测量文字高度
     * @param paint
     * @return
     */
    public static float measureTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (Math.abs(fontMetrics.ascent) - fontMetrics.descent);
    }

    //进度表的宽度
    public static int getWM(Context context){
        int wm_width = IDatalifeConstant.display(context).getWidth();
        wm_width = wm_width - DensityUtil.dip2px(context, 60);
        return wm_width;
    }

    //血压进度表的宽度
    public static int getSmallWM(Context context){

        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int wm_width = wm.getDefaultDisplay().getWidth();
        wm_width = (wm_width - DensityUtil.dip2px(context, 60))/2;
        return wm_width;
    }



}
