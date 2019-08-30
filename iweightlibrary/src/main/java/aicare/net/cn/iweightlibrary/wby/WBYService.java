package aicare.net.cn.iweightlibrary.wby;

import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import aicare.net.cn.iweightlibrary.bleprofile.BleManager;
import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;

public class WBYService extends BleProfileService implements WBYManagerCallbacks {
    private WBYManager mManager;
    public boolean mBinded;

    public final static int BLE_VERSION = 0;
    public final static int MCU_DATE = 1;
    public final static int MCU_TIME = 2;
    public final static int USER_ID = 3;
    public final static int ADC = 4;

    private final LocalBinder mBinder = new WBYBinder();

    public static final String ACTION_WEIGHT_DATA = "aicare.net.cn.fatscale.action.WEIGHT_DATA";
    public static final String EXTRA_WEIGHT_DATA = "aicare.net.cn.fatscale.extra.WEIGHT_DATA";

    public static final String ACTION_SETTING_STATUS_CHANGED = "aicare.net.cn.fatscale.action.SETTING_STATUS_CHANGED";
    public static final String EXTRA_SETTING_STATUS = "aicare.net.cn.fatscale.extra.SETTING_STATUS";

    public static final String ACTION_RESULT_CHANGED = "aicare.net.cn.fatscale.action.RESULT_CHANGED";
    public static final String EXTRA_RESULT_INDEX = "aicare.net.cn.fatscale.extra.RESULT_INDEX";
    public static final String EXTRA_RESULT = "aicare.net.cn.fatscale.extra.RESULT";

    public static final String ACTION_FAT_DATA = "aicare.net.cn.fatscale.action.FAT_DATA";
    public static final String EXTRA_FAT_DATA = "aicare.net.cn.fatscale.extra.FAT_DATA";
    public static final String EXTRA_IS_HISTORY = "aicare.net.cn.fatscale.extra.IS_HISTORY";

    public static final String ACTION_AUTH_DATA = "aicare.net.cn.fatscale.action.AUTH_DATA";
    public static final String EXTRA_SOURCE_DATA = "aicare.net.cn.fatscale.extra.SOURCE_DATA";
    public static final String EXTRA_BLE_DATA = "aicare.net.cn.fatscale.extra.BLE_DATA";
    public static final String EXTRA_ENCRYPT_DATA = "aicare.net.cn.fatscale.extra.ENCRYPT_DATA";
    public static final String EXTRA_IS_EQUALS = "aicare.net.cn.fatscale.extra.IS_EQUALS";

    public static final String ACTION_DID = "aicare.net.cn.fatscale.action.DID";
    public static final String EXTRA_DID = "aicare.net.cn.fatscale.extra.DID";

    @Override
    public void getWeightData(WeightData weightData) {
        Intent intent = new Intent(ACTION_WEIGHT_DATA);
        intent.putExtra(EXTRA_WEIGHT_DATA, weightData);
        sendBroadcast(intent);
    }

    @Override
    public void getSettingStatus(@AicareBleConfig.SettingStatus int status) {
        Intent intent = new Intent(ACTION_SETTING_STATUS_CHANGED);
        intent.putExtra(EXTRA_SETTING_STATUS, status);
        sendBroadcast(intent);
    }

    @Override
    public void getResult(int index, String result) {
        Intent intent = new Intent(ACTION_RESULT_CHANGED);
        intent.putExtra(EXTRA_RESULT_INDEX, index);
        intent.putExtra(EXTRA_RESULT, result);
        sendBroadcast(intent);
    }

    @Override
    public void getFatData(boolean isHistory, BodyFatData bodyFatData) {
        Intent intent = new Intent(ACTION_FAT_DATA);
        intent.putExtra(EXTRA_IS_HISTORY, isHistory);
        intent.putExtra(EXTRA_FAT_DATA, bodyFatData);
        sendBroadcast(intent);
    }

    public class WBYBinder extends LocalBinder {

        /**
         * 获取历史记录
         */
        public void syncHistory(){
            mManager.sendCmd(AicareBleConfig.SYNC_HISTORY, AicareBleConfig.UNIT_KG);
        }

        /**
         * 同步当前用户
         * @param user
         */
        public void syncUser(User user) {
            if (user == null) {
                return;
            }
            if (mManager != null){
                mManager.syncUser(user);
            }
        }

        /**
         * 同步用户列表
         * @param userList
         */
        public void syncUserList(List<User> userList) {
            mManager.syncUserList(userList);
        }

        /**
         * 同步当前单位
         * @param unit {@link AicareBleConfig#UNIT_KG}
         *             {@link AicareBleConfig#UNIT_LB}
         *             {@link AicareBleConfig#UNIT_ST}
         *             {@link AicareBleConfig#UNIT_JIN}
         */
        public void syncUnit(byte unit) {
            mManager.sendCmd(AicareBleConfig.SYNC_UNIT, unit);
        }

        /**
         * 同步时间
         */
        public void syncDate() {
            mManager.syncDate();
        }

        /**
         * 查询蓝牙版本信息
         */
        public void queryBleVersion() {
            mManager.sendCmd(AicareBleConfig.GET_BLE_VERSION, AicareBleConfig.UNIT_KG);
        }

        /**
         * 更新用户信息
         * @param user
         */
        public void updateUser(User user) {
            if (user == null) {
                return;
            }
            mManager.updateUser(user);
        }

        public WBYService getService() {
            return WBYService.this;
        }
    }

    @Override
    protected LocalBinder getBinder() {
        return mBinder;
    }

    @Override
    protected BleManager<WBYManagerCallbacks> initializeManager() {
        return mManager = WBYManager.getWBYManager();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        mBinded = true;
        return super.onBind(intent);
    }

    @Override
    public void onRebind(final Intent intent) {
        mBinded = true;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        mBinded = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager = null;
        mBinded = false;
    }
}
