package com.BeeFramework.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class NetworkStateService extends Service {

	public static final int NETWORK_NULL = 0;
	public static final int NETWORK_GPRS = 1;
	public static final int NETWORK_WIFI = 2;
	
	private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private boolean isToast = false;
    public static int network;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        	//if (isToast) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    info = connectivityManager.getActiveNetworkInfo();  
                    if(info != null && info.isAvailable()) {
                        String name = info.getTypeName();
                        if(name.equals("WIFI")) {
                        	WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        	WifiInfo wifiInfo = mWifi.getConnectionInfo();
                        	network = NETWORK_WIFI;
                        	//Toast.makeText(context, "当前网络：WIFI，名称："+wifiInfo.getSSID(), 0).show();
                        } else {
                        	network = NETWORK_GPRS;
                        	//Toast.makeText(context, "当前网络：2G/3G", 0).show();
                        }
                    } else {
                    	network = NETWORK_NULL;
                    	Toast.makeText(context, "网络异常，请检查网络", 0).show();
                    }
                }
			//} else {
			//	isToast = true;
			//}
        }
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
}
