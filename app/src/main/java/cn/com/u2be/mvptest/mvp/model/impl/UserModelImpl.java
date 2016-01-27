package cn.com.u2be.mvptest.mvp.model.impl;

import cn.com.u2be.mvptest.mvp.bean.User;
import cn.com.u2be.mvptest.mvp.model.IUserModel;

/**
 * Created by alek on 2016/1/7.
 */
public class UserModelImpl implements IUserModel {
    @Override
    public boolean regist() {
        return false;
    }

    @Override
    public User login(User user) {

        if (user.equals(new User("alek@126.com", "123456"))) {
            return user;
        }
        return null;
    }
}
