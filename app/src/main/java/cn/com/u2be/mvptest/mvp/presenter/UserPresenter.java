package cn.com.u2be.mvptest.mvp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import cn.com.u2be.mvptest.mvp.bean.User;
import cn.com.u2be.mvptest.mvp.model.IUserModel;
import cn.com.u2be.mvptest.mvp.model.impl.UserModelImpl;
import cn.com.u2be.mvptest.mvp.view.ILoginView;

/**
 * Created by alek on 2015/12/23.
 */
public class UserPresenter {

    private ILoginView userView;
    private IUserModel userModel;
    private Context mContext;

    private UserLoginTask mAuthTask;

    public UserPresenter(ILoginView userView, Context context) {
        this.userView = userView;
        userModel = new UserModelImpl();
        mContext = context;
    }


    public void regist(User user) {

    }

    public void login(User user) {
        mAuthTask = new UserLoginTask(user.getUsername(), user.getPassword());
        mAuthTask.execute((Void) null);

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }


            User user = userModel.login(new User(mEmail, mPassword));
            if (user == null) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            userView.showProgress(false);

            if (success) {
                userView.gotoMainActivity(mContext);
            } else {
                userView.showLoginError(mContext);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            userView.showProgress(false);
        }
    }

}
