package cn.com.u2be.mvptest.hlkwifi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.u2be.mvptest.hlkwifi.persenter.MainPersenter;
import cn.com.u2be.mvptest.hlkwifi.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_test_upd_broadcast)
    Button btnTestUpdBroadcast;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.btn_add_device)
    Button btnConnectDevices;

    private MainPersenter persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        persenter = new MainPersenter(this);


    }

    @OnClick(R.id.btn_test_upd_broadcast)
    public void onBtnTestUpdBroadcastClick() {
        persenter.FindHLKWifiIPAddress();
    }

    @OnClick(R.id.btn_add_device)
    public void onBtnAddNewDevicesClick() {
        persenter.addNewLED(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void msg(String s) {
        Snackbar.make(fab, "HLK ip Address:" + s, Snackbar.LENGTH_SHORT).show();
    }
}
