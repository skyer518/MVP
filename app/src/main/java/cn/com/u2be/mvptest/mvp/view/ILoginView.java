package cn.com.u2be.mvptest.mvp.view;

import android.content.Context;

/**
 * Created by alek on 2015/12/23.
 */
public interface ILoginView {
    /**
     * goto Net Activity
     *
     * @param context
     */
    void gotoNetActivity(Context context);

    /**
     * goto Main activity
     *
     * @param context
     */
    void gotoMainActivity(Context context);

    /**
     * goto Regist activity
     *
     * @param context
     */
    void gotRegistActivity(Context context);

    /**
     * login failed,show msg
     */
    void showLoginError(Context context);

    /**
     * show progeress
     *
     * @param b
     */
    void showProgress(boolean b);


}
