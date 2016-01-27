package cn.com.u2be.mvptest.mvp;

import android.app.Application;

import org.xutils.x;

/**
 * Created by alek on 2016/1/19.
 */
public class CustApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
