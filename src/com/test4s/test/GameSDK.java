package com.test4s.test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.game.tools.MyLog;
import com.game.tools.NetWorkState;
import com.test4s.tools.GameHttpConnection;
import com.test4s.tools.StateListener;

public class GameSDK {

	public final static String loginUrl = "http://4stest.com/sdkuser/sdklogin";
	public static boolean isDebug = true;

	public static String key = "";

	public static GameCallback myGameCallback;

	public static Context mContext;

	// 统计登录时间
	public static long startTime = 0;
	public static long endTime = 0;
	// 请求参数头
	static String version = "1.0";
	static String imei = "";
	static String package_name = "";
	static String game_id = "";
	static String channel_id = "";
	static TreeMap<String, String> header;

	// 获取加密key
	static final String url_getkey = "http://4stest.com/sdkuser/getkey";
	static Handler handler_getkey = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj == null) {
					return;
				}
				String data = (String) msg.obj;
				MyLog.i("获取加密key返回===" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						key = jsonObject2.getString("key");

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			}
		}
	};

	public static void init(Context context, String appid, String channelid) {

		mContext = context.getApplicationContext();

		// 获取imei
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			String serialnum = null;
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class, String.class);
				serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
			} catch (Exception ignored) {
			}
			imei = serialnum;
		}
		package_name = context.getPackageName();
		game_id = appid;
		channel_id = channelid;
		header = new TreeMap<String, String>();
		header.put("version", version);
		header.put("imei", imei);
		header.put("package_name", package_name);
		header.put("game_id", game_id);
		header.put("channel_id", channel_id);
		if (!NetWorkState.getNetState(mContext)) {
			Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
			return;
		}

		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String mes = GameHttpConnection
						.doPost(url_getkey, header, null);

				handler_getkey.obtainMessage(0, mes).sendToTarget();
			}
		});

		activityLis(context);

		StatisticsTime.sendLastSaveTime();
	}

	public static void login(Context context, GameCallback gameCallback) {

		myGameCallback = gameCallback;
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);

	}

	public static void activityLis(final Context context) {
		MyLog.i("监测运行2");
		new Thread(new Runnable() {

			@Override
			public void run() {
				MyLog.i("监测运行");
				// TODO Auto-generated method stub

				Activity activity = (Activity) context;
				while (true) {
					if (!NetWorkState.getNetState(context)) {
						MyLog.i("监测网络状态");
						Login.isLogin = false;
						return;
					}
					if (StateListener.isLauncherRunnig(context)) {

						if (startTime != 0 && Login.isLogin) {
							MyLog.i("后台运行,发送时间");
							StatisticsTime.send(startTime + "",
									new Date().getTime() + "");
							startTime = 0;
						}
					} else {

						if (startTime == 0 && Login.isLogin) {
							MyLog.i("前台运行,记录时间");
							startTime = new Date().getTime();
						}

					}

					if (activity.isFinishing()) {
						MyLog.i("程序退出,发送时间");

						StatisticsTime.send(startTime + "",
								new Date().getTime() + "");

						break;
					}

				}
			}
		}).start();
	}

}
