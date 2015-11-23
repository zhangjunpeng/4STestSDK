package com.test4s.test;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.game.tools.MyLog;
import com.game.tools.MySharePreferences;

public class TimeBroadcastReceiver extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			MyLog.i("网络状态已改变");
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				MyLog.i("网络状态发生改变，当前网络状态：" + info.getTypeName());
			} else {
				MyLog.i("网络状态发生改变，当前无网络连接,记录时间");
				GameSDK.endTime = new Date().getTime();
				MySharePreferences
						.saveInfo("startTime", GameSDK.startTime + "");
				MySharePreferences.saveInfo("endTime", GameSDK.endTime + "");
			}
		}
	}

}
