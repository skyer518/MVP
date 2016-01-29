package cn.com.u2be.mvptest.hlkwifi.view;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by alek on 2016/1/29.
 */
public interface IDeviceView {
    void showDevices(List<ScanResult> scanResults);

    void showProgress(final boolean show);

    void showConnectError();

    void gotoNext();
}
