package cn.com.u2be.mvptest.hlkwifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.u2be.mvptest.hlkwifi.persenter.WifiPersenter;
import cn.com.u2be.mvptest.hlkwifi.view.IWifiView;
import cn.com.u2be.mvptest.hlkwifi.widget.WifiConnectDialogView;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiActivityFragment extends Fragment implements IWifiView, AdapterView.OnItemClickListener {

    @Bind(R.id.listview_wifi)
    ListView listviewWifi;

    private WifiManager wifiManger;

    private List<ScanResult> scanResults = null;
    private WifiAdapter adapter;

    private WifiPersenter wifiPersenter;

    public WifiActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        wifiPersenter = new WifiPersenter(getActivity(), this);

        scanResults = new ArrayList<>(0);
        adapter = (new WifiAdapter(getActivity(), scanResults));
        listviewWifi.setAdapter(adapter);
        listviewWifi.setOnItemClickListener(this);
        wifiPersenter.scanWifi();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void openWifiDialog(String wifiSSID) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(wifiSSID)
                .setView(new WifiConnectDialogView(getContext()))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(listviewWifi, "which:" + which, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(listviewWifi, "which:" + which, Snackbar.LENGTH_SHORT).show();
                    }
                }).create();

        alertDialog.show();

    }


    @Override
    public void showWifiScanResult(List<ScanResult> scanResults) {
        adapter.setScanResult(scanResults);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult wifi = (ScanResult) parent.getAdapter().getItem(position);
        openWifiDialog(wifi.SSID);
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

            int signalLevel = wifiManger.calculateSignalLevel(item.level, 5);
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
