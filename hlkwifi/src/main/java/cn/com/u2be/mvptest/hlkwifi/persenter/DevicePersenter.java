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
import cn.com.u2be.mvptest.hlkwifi.view.IDeviceView;

/**
 * Created by alek on 2016/1/29.
 */
public class DevicePersenter {

    private static final int WHAT_CONNECT_SUCCESSS = 0;
    private static final int WHAT_CONNECT_ERROR = 1;
    private static final int WHAT_SHOW_PROGRESS = 2;
    private static final int WHAT_HIDE_PROGRESS = 3;
    private static final int WHAT_SCANNED = 4;

    private Context context;

    private IDeviceView deviceView;

    private WifiHelper wifiManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CONNECT_SUCCESSS:
                    deviceView.gotoNext();
                    break;
                case WHAT_CONNECT_ERROR:
                    deviceView.showConnectError();
                    break;
                case WHAT_SHOW_PROGRESS:
                    deviceView.showProgress(true);
                    break;
                case WHAT_HIDE_PROGRESS:
                    deviceView.showProgress(false);
                    break;
                case WHAT_SCANNED:
                    List<ScanResult> deviceResults = (List<ScanResult>) msg.obj;
                    deviceView.showDevices(deviceResults);
                    break;
                default:


            }
        }
    };
    private final String password = "12345678";
    private Timer timer;

    public DevicePersenter(Context context, IDeviceView deviceView) {
        this.context = context;
        this.deviceView = deviceView;
        wifiManager = new WifiHelper((WifiManager) context.getSystemService(Context.WIFI_SERVICE)) {

            @Override
            public void onWifiConnected(WifiConfiguration config) {
                // connect to device

                // got next;
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

    public void scanDevice() {
        wifiManager.scanWifi();

        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<ScanResult> deviceResults = new ArrayList<>(0);
                List<ScanResult> wifiResults = wifiManager.getScanResult();
                for (ScanResult scanResult : wifiResults) {
                    if (scanResult.SSID.startsWith("HI-LINK_")) {
                        deviceResults.add(scanResult);
                    }
                }
                //deviceView.showDevices(deviceResults);

                Message msg = new Message();
                msg.what = WHAT_SCANNED;
                msg.obj = deviceResults;
                handler.sendMessage(msg);
            }
        }, 3000);

    }


    public void connectDevice(ScanResult device) {
        handler.sendEmptyMessage(WHAT_SHOW_PROGRESS);
        wifiManager.connect(device.SSID, password, WifiHelper.getWifiCipherType(device));
    }
}
