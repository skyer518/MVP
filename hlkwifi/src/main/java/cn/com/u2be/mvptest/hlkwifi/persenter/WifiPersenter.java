package cn.com.u2be.mvptest.hlkwifi.persenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import java.util.List;

import cn.com.u2be.mvptest.hlkwifi.net.WifiConnectManager;
import cn.com.u2be.mvptest.hlkwifi.net.WifiConnectManager.OnWifiConnectListener;
import cn.com.u2be.mvptest.hlkwifi.view.IWifiView;

/**
 * Created by alek on 2016/1/28.
 */
public class WifiPersenter {

    private Context context;

    private IWifiView wifiView;

    private WifiConnectManager wifiManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                wifiView.gotoNext();
            } else {
                wifiView.showConnectError();
            }
        }
    };

    public WifiPersenter(Context context, IWifiView wifiWiew) {
        this.context = context;
        this.wifiView = wifiWiew;
        wifiManager = new WifiConnectManager((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
    }

    public void scanWifi() {
        wifiManager.scanWifi();
        List<ScanResult> scanResults = wifiManager.getScanResult();
        wifiView.showWifiScanResult(scanResults);

    }


    public void connectWifi(ScanResult wifi, String password) {
        wifiManager.connect(wifi.SSID, password, WifiConnectManager.getWifiCipherType(wifi), new OnWifiConnectListener() {
            @Override
            public void onWifiConnected(WifiConfiguration config) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onWifiError(WifiConfiguration config) {
                handler.sendEmptyMessage(0);
            }
        });

    }


}
