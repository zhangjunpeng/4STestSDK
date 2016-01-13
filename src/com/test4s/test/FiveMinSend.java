package com.test4s.test;

import java.util.Date;

import com.game.tools.MyLog;

public class FiveMinSend extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("开始统计");
		while (true) {

			long endtime = new Date().getTime();
			if (endtime - GameSDK.startTime >= 300 * 1000) {
				// 3分钟统计一次
				StatisticsTime.send(GameSDK.startTime + "", endtime + "");
				GameSDK.startTime = endtime;
				MyLog.i("3分钟统计一次");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
