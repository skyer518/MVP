package cn.com.u2be.mvptest.mvp.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.u2be.mvptest.mvp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.com.u2be.mvptest.mvp.bean.Contact;
import cn.com.u2be.mvptest.mvp.util.BitmapUtil;

/**
 * Created by alek on 2016/1/7.
 */
public class ContactAdapter extends BaseAdapter {

    public final Context mContext;
    public final List<Contact> contactList;


    public ContactAdapter(Context mContext, List<Contact> contactList) {
        this.mContext = mContext;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);

            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_headPhoto);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_email = (TextView) convertView.findViewById(R.id.tv_email);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact contact = (Contact) getItem(position);
        holder.tv_name.setText(contact.getName());
        holder.tv_phone.setText(contact.getPhone());
        holder.tv_email.setText(contact.getEmail());
        try {
            InputStream headPhotoStream = mContext.getAssets().open(contact.getHeadPhotoFileName() + ".png");
            holder.iv_head.setImageBitmap(BitmapUtil.toRoundBitmap(BitmapFactory.decodeStream(headPhotoStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertView;
    }


    class ViewHolder {

        ImageView iv_head = null;
        TextView tv_name = null;
        TextView tv_phone = null;
        TextView tv_email = null;

    }
}
