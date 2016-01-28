package cn.com.u2be.mvptest.hlkwifi.persenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import cn.com.u2be.mvptest.hlkwifi.WifiActivity;
import cn.com.u2be.mvptest.hlkwifi.net.UDPClient;
import cn.com.u2be.mvptest.hlkwifi.view.IMainView;

/**
 * Created by alek on 2016/1/25.
 */
public class MainPersenter {

    private IMainView mainView;

    private UDPClient udpClient;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mainView.msg(msg.obj.toString());
        }
    };

    public MainPersenter(IMainView mainView) {
        this.mainView = mainView;
    }


    public void FindHLKWifiIPAddress() {
        if (udpClient == null)
            udpClient = new UDPClient(handler);
        udpClient.send("HLK");
    }


    public void addNewLED(Activity context) {
        // 扫描Wifi 连接到 灯
        Intent intent = new Intent(context, WifiActivity.class);
        context.startActivity(intent);
/*      // 调用系统wifi
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivityForResult(wifiSettingsIntent);*/


        // 扫描Wifi 设置灯要连接的Wifi

        //保存设置 重启 灯具并连接

    }

}



