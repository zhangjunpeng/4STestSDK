package com.test4s.test;

import java.util.TreeMap;
import java.util.concurrent.Executors;

import android.os.Handler;

import com.test4s.tools.GameHttpConnection;

public class Login {
	private Handler handler;
	private int tag;
	private final static String login_url = "http://4stest.com/sdkuser/sdklogin";
	public static String uid = "";
	public static String sid = "";
	// 是否登录
	public static boolean isLogin = false;

	public Login(Handler handler, int tag) {
		this.handler = handler;
		this.tag = tag;
	}

	public void nameLogin(final String name, final String password) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				TreeMap<String, String> map = new TreeMap<String, String>();
				map.put("username", name);
				map.put("password", password);

				String mes = GameHttpConnection.doPost(login_url,
						GameSDK.header, map);
				handler.obtainMessage(tag, mes).sendToTarget();

			}
		});

	}

}
