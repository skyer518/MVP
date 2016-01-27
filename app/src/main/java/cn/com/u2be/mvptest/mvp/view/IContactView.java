package cn.com.u2be.mvptest.mvp.view;

import java.util.List;

import cn.com.u2be.mvptest.mvp.bean.Contact;

/**
 * Created by alek on 2016/1/7.
 */
public interface IContactView {
    /**
     * show contacts list
     */
    void showContacts(List<Contact> contactList);
    /**
     * show operator menu
     *
     * @param contact
     */
    void showOperator(Contact contact);

    /**
     * call telephone
     */
    void callTelephone(Contact contact);

    /**
     * send email
     */
    void sendEmail(Contact contact);

    /**
     * send message
     */
    void sendMessage(Contact contact);

}
