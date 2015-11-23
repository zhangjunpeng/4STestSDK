package com.game.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.test4s.test.GameSDK;

public class MySharePreferences {
	static SharedPreferences sharedPreferences;

	public static void saveInfo(String name, String value) {
		if (GameSDK.mContext == null) {
			return;
		}

		sharedPreferences = GameSDK.mContext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public static String getInfoFromSP(String name) {
		if (GameSDK.mContext == null) {
			return "";
		}
		sharedPreferences = GameSDK.mContext.getSharedPreferences("gameInfo",
				Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(name, "");
		return value;

	}
}
