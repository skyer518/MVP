package cn.com.u2be.mvptest.mvp.view.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.u2be.mvptest.mvp.R;
import cn.com.u2be.mvptest.mvp.adapter.ContactAdapter;
import cn.com.u2be.mvptest.mvp.bean.Contact;
import cn.com.u2be.mvptest.mvp.presenter.ContactPresenter;
import cn.com.u2be.mvptest.mvp.presenter.UserPresenter;
import cn.com.u2be.mvptest.mvp.view.IContactView;

public class ContactActivity extends AppCompatActivity implements IContactView {


    private ContactPresenter contactPresenter;

    private BaseAdapter adapter;
    private List<Contact> contactList = new ArrayList<>(0);

    @Bind(R.id.listview_contact)
    ListView listviewContact;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        contactPresenter = new ContactPresenter(this, this);

        adapter = new ContactAdapter(this, contactList);
        listviewContact.setAdapter(adapter);

        registerForContextMenu(listviewContact);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.snackbar_fab), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        contactPresenter.loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("system.out", v.toString());
        menu.add(0, 0, 0, "call telephone");
        menu.add(0, 1, 1, "send message");
        menu.add(0, 2, 2, "send email");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//获得AdapterContextMenuInfo,以此来获得选择的listview项目
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contact contact = (Contact) adapter.getItem(menuInfo.position);

        Intent intent = new Intent();
        int itemId = item.getItemId();
        switch (itemId) {
            case 0:
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contact.getPhone()));
                startActivity(intent);
                break;
            case 1:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + contact.getPhone()));
                startActivity(intent);
                break;
            case 2:

                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + contact.getEmail()));
                startActivity(intent);
                break;
        }


        return super.onContextItemSelected(item);
    }

    @Override
    public void showContacts(List<Contact> contactList) {
        if (contactList != null) {
            this.contactList.clear();
            this.contactList.addAll(contactList);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showOperator(Contact contact) {

    }

    @Override
    public void callTelephone(Contact contact) {

    }

    @Override
    public void sendEmail(Contact contact) {

    }

    @Override
    public void sendMessage(Contact contact) {

    }


}
