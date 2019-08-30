package aicare.net.cn.iweightlibrary;

import android.app.Application;
import android.test.ApplicationTestCase;

import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testParseBroadAddress() {
        byte[] scanRecord = {-84, 2, -29, 10, -81, -20, -77, 0};
//        String broadAddress = ParseData.getBroadAddress(scanRecord);
//        L.e("ApplicationTest", "broadAddress = " + broadAddress);
    }
}
