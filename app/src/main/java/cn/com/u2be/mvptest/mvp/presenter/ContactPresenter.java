package cn.com.u2be.mvptest.mvp.presenter;

import android.content.Context;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.com.u2be.mvptest.mvp.R;
import cn.com.u2be.mvptest.mvp.bean.Contact;
import cn.com.u2be.mvptest.mvp.model.IContactModel;
import cn.com.u2be.mvptest.mvp.model.impl.ContactModelImpl;
import cn.com.u2be.mvptest.mvp.view.IContactView;

/**
 * Created by alek on 2016/1/7.
 */
public class ContactPresenter {

    private Context mContext;
    private IContactView contactView;
    private IContactModel contactModel;


    public ContactPresenter(IContactView contactView, Context context) {
        this.contactView = contactView;
        this.contactModel = new ContactModelImpl();
        this.mContext = context;
    }

    public void loadContacts() {
        List<Contact> contactList = null;

        //  发网络请求取数据
        RequestParams params = new RequestParams(mContext.getString(R.string.host) + "/json/contacts");
        x.http().get(params, new Callback.CommonCallback<List<Contact>>() {

            @Override
            public void onSuccess(List<Contact> result) {
                contactView.showContacts(result);
                try {
                    contactModel.save(result);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadLocalData();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                loadLocalData();
            }

            @Override
            public void onFinished() {
                loadLocalData();
            }
        });
        //  取本地数据库数据 显示



    }

    public void loadLocalData() {
        List<Contact> contactList = null;
        try {
            contactList = contactModel.getContacts();
            this.contactView.showContacts(contactList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
