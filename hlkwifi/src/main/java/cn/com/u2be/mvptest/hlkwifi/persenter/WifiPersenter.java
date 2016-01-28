package cn.com.u2be.mvptest.hlkwifi.persenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import cn.com.u2be.mvptest.hlkwifi.view.IWifiView;

/**
 * Created by alek on 2016/1/28.
 */
public class WifiPersenter {

    private Context context;

    private IWifiView wifiView;

    public WifiPersenter(Context context, IWifiView wifiWiew) {
        this.context = context;
        this.wifiView = wifiWiew;
    }

    public void scanWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        wifiView.showWifiScanResult(scanResults);

    }


}
