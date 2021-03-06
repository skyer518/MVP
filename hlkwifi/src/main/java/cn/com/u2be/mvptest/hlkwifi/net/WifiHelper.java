package cn.com.u2be.mvptest.hlkwifi.net;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 明 on 2016/1/29.
 */
public abstract class WifiHelper {

    private static final String TAG = WifiHelper.class.getSimpleName();

    WifiManager wifiManager;

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造函数
    public WifiHelper(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public static WifiCipherType getWifiCipherType(ScanResult scResult) {
        if (!TextUtils.isEmpty(scResult.SSID)) {
            String capabilities = scResult.capabilities;

            if (!TextUtils.isEmpty(capabilities)) {

                if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                    return WifiCipherType.WIFICIPHER_WPA;
                } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                    return WifiCipherType.WIFICIPHER_WEP;
                }
            }
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
        return WifiCipherType.WIFICIPHER_INVALID;

    }

    // 提供一个外部接口，传入要连接的无线网
    public void connect(String ssid, String password, WifiCipherType type) {
        WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
        if (wifiConfig == null) {
            throw new RuntimeException("WifiConfig is null!");
        }
        Thread thread = new Thread(new ConnectRunnable(wifiConfig));
        thread.start();
    }

    // 查看以前是否也配置过这个网络
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            Log.d(TAG, "param[SSID : " + SSID + "]  , item[SSID:" + existingConfig.SSID + "]");
            if (existingConfig.SSID.equals(SSID)) {
                return existingConfig;
            }
        }
        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        // nopass
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wep
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wpa
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    // 打开wifi功能
    public boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    public void scanWifi() {
        wifiManager.startScan();
    }

    public List<ScanResult> getScanResult() {
        return wifiManager.getScanResults();
    }


    class ConnectRunnable implements Runnable {

        private WifiConfiguration wifiConfig;

        private Timer timer;

        public ConnectRunnable(WifiConfiguration wifiConfig) {
            this.wifiConfig = wifiConfig;

        }

        @Override
        public void run() {
            try {
                // 打开wifi
                openWifi();
                // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
                // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
                while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    try {
                        // 为了避免程序一直while循环，让它睡个100毫秒检测……
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                    }
                }

                WifiConfiguration tempConfig = isExsits(wifiConfig.SSID);
                if (tempConfig != null) {
                    wifiManager.removeNetwork(tempConfig.networkId);
                }

                int netID = wifiManager.addNetwork(wifiConfig);
                boolean enabled = wifiManager.enableNetwork(netID, true);
                Log.d(TAG, "enableNetwork status enable=" + enabled);
                wifiManager.saveConfiguration();

                final boolean connected = wifiManager.reconnect();
                Log.d(TAG, "enableNetwork connected=" + connected);
                if (connected && enabled) {
                    timer = new Timer();
                    timer.schedule(connectStatusTask, 100, 500);
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                afterConnect();
            }
        }

        private TimerTask connectStatusTask = new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                count++;
                WifiConfiguration exsits = isExsits(wifiConfig.SSID);
                int status = exsits.status;
                Log.d(TAG, "status=" + status);
                if (status == WifiConfiguration.Status.CURRENT) {
                    onWifiConnected(exsits);
                    timer.cancel();
                }
                if (count > 20) {
                    onWifiError(exsits);
                    timer.cancel();
                }
            }
        };


        public void afterConnect() {
            if (timer != null)
                timer.cancel();
        }

    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }

        return true;
    }

    public abstract void onWifiConnected(WifiConfiguration config);

    public abstract void onWifiError(WifiConfiguration config);

}