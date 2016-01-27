package cn.com.u2be.mvptest.hlkwifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiActivityFragment extends Fragment {

    @Bind(R.id.listview_wifi)
    ListView listviewWifi;

    private WifiManager wifiManger;

    private List<ScanResult> scanResults = null;

    public WifiActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        wifiManger = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        if (wifiManger.isWifiEnabled()) {
            wifiManger.setWifiEnabled(true);
        }
        scanResults = wifiManger.getScanResults();
        listviewWifi.setAdapter(new WifiAdapter(getActivity(), scanResults));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class WifiAdapter extends BaseAdapter {

        private List<ScanResult> scanResult;
        private LayoutInflater inflater;

        public WifiAdapter(Context context, List<ScanResult> scanResults) {
            this.scanResult = scanResults;
            this.inflater = LayoutInflater.from(context);
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
