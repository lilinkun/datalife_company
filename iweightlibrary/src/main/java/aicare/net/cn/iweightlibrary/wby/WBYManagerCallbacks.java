package aicare.net.cn.iweightlibrary.wby;

import aicare.net.cn.iweightlibrary.bleprofile.BleManagerCallbacks;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;

public interface WBYManagerCallbacks extends BleManagerCallbacks {

    void getWeightData(WeightData weightData);

    void getSettingStatus(@AicareBleConfig.SettingStatus int status);

    void getResult(int index, String result);

    void getFatData(boolean isHistory, BodyFatData bodyFatData);
}
