package cn.com.u2be.mvptest.hlkwifi.view;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by alek on 2016/1/28.
 */
public interface IWifiView {

    void showWifiScanResult(List<ScanResult> scanResults);
}
