package cn.com.u2be.mvptest.mvp.model.impl;

import org.xutils.DbManager;
import org.xutils.config.DbConfigs;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.u2be.mvptest.mvp.bean.Contact;
import cn.com.u2be.mvptest.mvp.model.IContactModel;

/**
 * Created by alek on 2016/1/7.
 */
public class ContactModelImpl implements IContactModel {

    private static final String DB_PATH = "lightech_Contact";
    private static final String DB_NAME = "lightech_Contct_db";
    private List<Contact> contacts = new ArrayList<>(0);

    private DbManager.DaoConfig daoConfig;


    public ContactModelImpl() {
        daoConfig = new DbManager.DaoConfig().setDbName(DB_NAME).setDbDir(new File(DB_PATH));
    }


    @Override
    public List<Contact> getContacts() throws DbException {
        List<Contact> results = x.getDb(daoConfig).findAll(Contact.class);
        return results;
    }

    @Override
    public Contact getContact(int id) throws DbException {
        Contact contact = x.getDb(daoConfig).findById(Contact.class, id);
        return contact;
    }

    @Override
    public void save(Contact contact) throws DbException {
        x.getDb(daoConfig).saveOrUpdate(contact);
    }

    @Override
    public void save(List<Contact> contacts) throws DbException {
        for (Contact contact : contacts) {
            save(contact);
        }
    }
}
