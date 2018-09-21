package com.onemeter.receiver;

import com.onemeter.R;
import com.onemeter.app.MyApplication;
import com.onemeter.util.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态改变接收广播
 * 
 * @ClassName: NetChangeReceiver
 * @author angelyin 2015-10-9 下午4:01:41
 */
public class NetChangeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobNetInfo != null && mobNetInfo.isConnected()) {
			MyApplication.isNetAvailable = true;
		} else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
			MyApplication.isNetAvailable = true;
		} else {
			Utils.showDebugToast(
					context,
					context.getResources().getString(
							R.string.msg_sys_net_unavailable));
			MyApplication.isNetAvailable = false;
		}
	}
}