package aicare.net.cn.iweightlibrary.bleprofile;

public interface BleManagerCallbacks {

    /**
     * 已连接
     */
    void onDeviceConnected();

    /**
     * 连接已断开
     */
    void onDeviceDisconnected();

    /**
     * 已发现服务
     */
    void onServicesDiscovered();

    /**
     * 使能(订阅通知)成功
     */
    void onIndicationSuccess();

    /**
     * 连接异常
     * @param message
     * @param errorCode
     */
    void onError(final String message, final int errorCode);
}
