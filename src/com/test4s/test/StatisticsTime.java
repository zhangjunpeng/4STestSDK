package com.test4s.test;

import java.util.TreeMap;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.text.TextUtils;

import com.game.tools.MyLog;
import com.game.tools.MySharePreferences;
import com.test4s.tools.GameHttpConnection;

public class StatisticsTime {

	private static final String url = "http://4stest.com/sdkuser/logintime";

	public static void send(final String startTime, final String endTime) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				TreeMap<String, String> body = new TreeMap<String, String>();
				body.put("sid", Login.sid);
				body.put("server_id", "1");
				body.put("user_id", Login.uid);
				body.put("start_time", startTime);

				body.put("end_time", endTime);
				String mes = GameHttpConnection.doPost(url, GameSDK.header,
						body);
				MyLog.i("发送时间返回" + mes);

				try {

					JSONObject jsonObject = new JSONObject(mes);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						GameSDK.startTime = 0;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	public static void sendLastSaveTime() {
		MyLog.i("统计上次断网时在线时间");
		String startTime = MySharePreferences.getInfoFromSP("startTime");
		String endTime = MySharePreferences.getInfoFromSP("endTime");
		if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
			MyLog.i("记录为空");
			return;
		}
		if (startTime.equals("0")) {
			MyLog.i("记录为空2");
			return;
		}

		send(startTime, endTime);
		MySharePreferences.saveInfo("startTime", "");
		MySharePreferences.saveInfo("endTime", "");
	}

}
