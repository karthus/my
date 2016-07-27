package com.hdj.hook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hdj.hook.MyApplication;
import com.hdj.hook.R;
import com.hdj.hook.util.ContantsUtil;
import com.hdj.hook.util.LoadDataFromNet;
import com.hdj.hook.util.LoadDataFromNet.DataCallBack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
	
	Map<String, String> map;
	TextView tv_login_tips;
	static final int LOGIN_NO_REGISTED=2;
	static final int LOGIN_OPEN=1;
	static final int LOGIN_NOT_OPEN=0;
	SharedPreferences preferences;
	String imei;
	MyApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tv_login_tips=(TextView)findViewById(R.id.tv_login_tips);
		map=new HashMap<String, String>();
		TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imei=tm.getDeviceId();
		app = MyApplication.getInstance();
		
		if(!app.getisLogined()){
			getLogin();
		}else {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			LoginActivity.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void getLogin() {
		map.put("imei", imei);
		LoadDataFromNet load=new LoadDataFromNet(getApplicationContext(), ContantsUtil.LOGIN_SERVLET, map);
		load.getData(new DataCallBack() {
			
			@Override
			public void onDataCallBack(Message msg) {
				switch (msg.what) {
				case ContantsUtil.HANDLER_GETDATA_ERR:
					tv_login_tips.setText("登录出错:网络问题"/*+(String)msg.obj*/);
					break;
				case ContantsUtil.HANDLER_GETDATA_OK:
					String result=msg.obj.toString();
					try {
						JSONObject info=new JSONObject(result);
						int flag=info.optInt("flag");
						if(flag==LOGIN_NO_REGISTED){
							startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
							LoginActivity.this.finish();
							overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						}else if (flag==LOGIN_NOT_OPEN) {
							//不能登录
							tv_login_tips.setText("当前不能登录，因为权限未开放");
						}else if (flag==LOGIN_OPEN) {
							//登录
							app.setisLogined(true);
							startActivity(new Intent(LoginActivity.this,MainActivity.class));
							LoginActivity.this.finish();
							overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		});
	}
}
