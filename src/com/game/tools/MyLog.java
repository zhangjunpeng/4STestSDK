package com.game.tools;

import android.util.Log;

import com.test4s.test.GameSDK;

public class MyLog {

	public static void i(String mes) {
		if (!GameSDK.isDebug) {
			return;
		}
		Log.i("4Stest", StringTools.decodeUnicode(mes));
	}
}
