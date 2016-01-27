package cn.com.u2be.mvptest.mvp.bean.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import cn.com.u2be.mvptest.mvp.bean.Contact;

/**
 * Created by alek on 2016/1/20.
 */
public class ContactJsonResponseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (resultClass == List.class) {
            Type listType = new TypeToken<LinkedList<Contact>>() {
            }.getType();
            Gson gson = new Gson();
            List<Contact> contacts = gson.fromJson(result, listType);
            return contacts;

        } else {
            Gson gson = new Gson();
            Contact contact = gson.fromJson(result, Contact.class);

            return contact;
        }
    }
}
