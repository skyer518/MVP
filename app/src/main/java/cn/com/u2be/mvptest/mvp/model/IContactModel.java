package cn.com.u2be.mvptest.mvp.model;

import org.xutils.ex.DbException;

import java.util.List;

import cn.com.u2be.mvptest.mvp.bean.Contact;

/**
 * Created by alek on 2016/1/7.
 */
public interface IContactModel {
    /**
     * get all contacts
     *
     * @return
     */
    List<Contact> getContacts() throws DbException;

    /**
     * get contact
     *
     * @return
     */
    Contact getContact(int id) throws DbException;


    void save(Contact contact) throws DbException;


    void save(List<Contact> contacts) throws DbException;


}
