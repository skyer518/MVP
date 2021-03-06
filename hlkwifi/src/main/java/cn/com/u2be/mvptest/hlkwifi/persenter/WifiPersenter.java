package cn.com.u2be.mvptest.hlkwifi.persenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.u2be.mvptest.hlkwifi.net.WifiHelper;
import cn.com.u2be.mvptest.hlkwifi.view.IWifiView;

/**
 * Created by alek on 2016/1/28.
 */
public class WifiPersenter {

    private Context context;
    private IWifiView wifiView;

    private WifiHelper wifiManager;

    private static final int WHAT_CONNECT_SUCCESSS = 0;

    private static final int WHAT_CONNECT_ERROR = 1;
    private static final int WHAT_SHOW_PROGRESS = 2;
    private static final int WHAT_HIDE_PROGRESS = 3;
    private static final int WHAT_SCANNED = 4;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CONNECT_SUCCESSS:
                    wifiView.gotoNext();
                    break;
                case WHAT_CONNECT_ERROR:
                    wifiView.showConnectError();
                    break;
                case WHAT_SHOW_PROGRESS:
                    wifiView.showProgress(true);
                    break;
                case WHAT_HIDE_PROGRESS:
                    wifiView.showProgress(false);
                    break;
                case WHAT_SCANNED:
                    wifiView.showWifiScanResult((List<ScanResult>) msg.obj);
                default:


            }
        }
    };
    private Timer timer;

    public WifiPersenter(Context context, IWifiView wifiWiew) {
        this.context = context;
        this.wifiView = wifiWiew;
        wifiManager = new WifiHelper((WifiManager) context.getSystemService(Context.WIFI_SERVICE)) {

            @Override
            public void onWifiConnected(WifiConfiguration config) {
                handler.sendEmptyMessage(WHAT_HIDE_PROGRESS);
                handler.sendEmptyMessage(WHAT_CONNECT_SUCCESSS);
            }

            @Override
            public void onWifiError(WifiConfiguration config) {
                handler.sendEmptyMessage(WHAT_HIDE_PROGRESS);
                handler.sendEmptyMessage(WHAT_CONNECT_ERROR);
            }
        };

        wifiManager.openWifi();
    }

    public void scanWifi() {
        wifiManager.scanWifi();

        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<ScanResult> wifiResults = wifiManager.getScanResult();

                Message msg = new Message();
                msg.what = WHAT_SCANNED;
                msg.obj = wifiResults;
                handler.sendMessage(msg);
            }
        }, 3000);

//        List<ScanResult> scanResults = wifiManager.getScanResult();
//        wifiView.showWifiScanResult(scanResults);

    }


    public void connectWifi(ScanResult wifi, String password) {
        handler.sendEmptyMessage(WHAT_SHOW_PROGRESS);
        wifiManager.connect(wifi.SSID, password, WifiHelper.getWifiCipherType(wifi));
    }


}