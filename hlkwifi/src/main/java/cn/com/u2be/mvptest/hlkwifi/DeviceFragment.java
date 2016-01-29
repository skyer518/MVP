package cn.com.u2be.mvptest.hlkwifi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.u2be.mvptest.hlkwifi.persenter.DevicePersenter;
import cn.com.u2be.mvptest.hlkwifi.persenter.WifiPersenter;
import cn.com.u2be.mvptest.hlkwifi.view.IDeviceView;
import cn.com.u2be.mvptest.hlkwifi.widget.WifiConnectDialogView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DeviceFragment extends Fragment implements IDeviceView, AdapterView.OnItemClickListener {

    @Bind(R.id.listview_wifi)
    ListView listviewWifi;
    @Bind(R.id.pb_progress)
    ProgressBar pbProgress;

    private List<ScanResult> scanResults = null;

    private WifiAdapter adapter;

    private DevicePersenter devicePersenter;

    public DeviceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        devicePersenter = new DevicePersenter(getActivity(), this);

        scanResults = new ArrayList<>(0);
        adapter = (new WifiAdapter(getActivity(), scanResults));
        listviewWifi.setAdapter(adapter);
        listviewWifi.setOnItemClickListener(this);
        devicePersenter.scanDevice();
        showProgress(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void gotoNext() {
        WifiFragment wifiFragment = new WifiFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, wifiFragment).hide(this).commit();
    }

    @Override
    public void showConnectError() {
        Snackbar.make(listviewWifi, getString(R.string.text_connect_wifi_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        listviewWifi.setVisibility(show ? View.GONE : View.VISIBLE);
        listviewWifi.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listviewWifi.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        pbProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult wifi = (ScanResult) parent.getAdapter().getItem(position);
        devicePersenter.connectDevice(wifi);
    }

    @Override
    public void showDevices(List<ScanResult> scanResults) {
        showProgress(false);
        adapter.setScanResult(scanResults);
        adapter.notifyDataSetChanged();
    }

    public class WifiAdapter extends BaseAdapter {

        public void setScanResult(List<ScanResult> scanResult) {
            this.scanResult = scanResult;
            Collections.sort(this.scanResult, comparator);
        }

        private List<ScanResult> scanResult;
        private LayoutInflater inflater;

        private Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return rhs.level - lhs.level;
            }
        };

        public WifiAdapter(Context context, List<ScanResult> scanResults) {
            this.inflater = LayoutInflater.from(context);
            setScanResult(scanResults);
        }

        @Override
        public int getCount() {
            return scanResult.size();
        }

        @Override
        public Object getItem(int position) {
            return scanResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_wifi_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            ScanResult item = (ScanResult) getItem(position);

            int signalLevel = WifiManager.calculateSignalLevel(item.level, 5);
            switch (signalLevel) {
                case 1:
                    holder.wifiStronger.setImageResource(R.mipmap.wifi_1);
                    break;
                case 2:
                    holder.wifiStronger.setImageResource(R.mipmap.wifi_2);
                    break;
                case 3:
                    holder.wifiStronger.setImageResource(R.mipmap.wifi_3);
                    break;
                case 4:
                    holder.wifiStronger.setImageResource(R.mipmap.wifi_4);
                    break;
                default:
                    holder.wifiStronger.setImageResource(R.mipmap.wifi_1);
                    break;
            }


            holder.wifiEncrypt.setText(item.BSSID);
            holder.wifiSSID.setText(item.SSID);
            return convertView;
        }

        /**
         * This class contains all butterknife-injected Views & Layouts from layout file 'item_wifi_listview.xml'
         * for easy to all layout elements.
         *
         * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
         */
        class ViewHolder {
            @Bind(R.id.wifi_SSID)
            TextView wifiSSID;
            @Bind(R.id.wifi_Encrypt)
            TextView wifiEncrypt;
            @Bind(R.id.wifi_stronger)
            ImageView wifiStronger;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


}
