package cn.com.u2be.mvptest.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.http.annotation.HttpResponse;

import cn.com.u2be.mvptest.mvp.bean.parser.ContactJsonResponseParser;

/**
 * Created by alek on 2016/1/7.
 */
@Table(name = "Contact")
@HttpResponse(parser =  ContactJsonResponseParser.class)
public class Contact implements Parcelable {

    @Column(name = "id", isId = true,autoGen = false)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "headPhotoFileName")
    private String headPhotoFileName;

    public Contact() {
    }

    public Contact(String phone, String name, String email) {
        this.phone = phone;
        this.name = name;
        this.email = email;
    }


    public Contact(String name, String email, String phone, String headPhotoFileName) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.headPhotoFileName = headPhotoFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadPhotoFileName() {
        return headPhotoFileName;
    }

    public void setHeadPhotoFileName(String headPhotoFileName) {
        this.headPhotoFileName = headPhotoFileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(headPhotoFileName);

    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel source) {
            Contact contact = new Contact();
            contact.id = source.readInt();
            contact.name = source.readString();
            contact.phone = source.readString();
            contact.email = source.readString();
            contact.headPhotoFileName = source.readString();
            return contact;
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[0];
        }
    };

}
