package com.test4s.test;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.game.tools.MyLog;

public class LoginActivity extends Activity {

	private EditText edit_username_login;
	private EditText edit_password_login;

	private Handler login_Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj == null) {
					return;
				}
				String data = msg.obj.toString();
				MyLog.i("登录返回===" + data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					String errorCode = jsonObject.getString("errorCode");
					if ("200".equals(errorCode)) {
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("data");
						String uid = jsonObject2.getString("uid");
						String sid = jsonObject2.getString("sid");
						Login.sid = sid;
						Login.uid = uid;

						Login.isLogin = true;

						GameSDK.startTime = new Date().getTime();

						GameSDK.myGameCallback.loginCallback(uid, sid);
						Toast.makeText(LoginActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();

						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdk_main);
		setFinishOnTouchOutside(false);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		edit_username_login = (EditText) findViewById(R.id.edit_username_login);
		edit_password_login = (EditText) findViewById(R.id.edit_password_login);
		findViewById(R.id.login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = edit_username_login.getEditableText().toString();
				String pwd = edit_password_login.getEditableText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
					Toast.makeText(LoginActivity.this, "请输入账号或密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Login login = new Login(login_Handler, 0);
				login.nameLogin(name, pwd);
			}
		});
	}
}
