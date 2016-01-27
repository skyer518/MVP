package cn.com.u2be.mvptest.mvp.model;

import cn.com.u2be.mvptest.mvp.bean.User;

/**
 * Created by alek on 2015/12/23.
 */
public interface IUserModel {
    /**
     * 注册
     *
     * @return
     */
    boolean regist();

    /**
     * 登陆
     *
     * @return
     */
    User login(User user);

}
