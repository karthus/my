package com.hdj.hook.activity;
/*
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hdj.hook.R;
import com.hdj.hook.util.ContantsUtil;
import com.hdj.hook.util.LoadDataFromNet;
import com.hdj.hook.util.LoadDataFromNet.DataCallBack;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends BaseActivity {
	Button bt_regist;
	EditText et_pass;
	TextView tv_regist_tip;
	
	Map<String, String> map;
	static final int REGISTE_PASS_ERR=3;
	static final int HAVE_REGISTED=2;
	static final int REGISTER_OK=1;
	static final int REGISTER_ERR=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		tv_regist_tip=(TextView)findViewById(R.id.tv_regist_tip);
		
		map=new HashMap<String, String>();
		et_pass=(EditText)findViewById(R.id.password);
		
		bt_regist=(Button)findViewById(R.id.bt_regist);
		bt_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String imei=tm.getDeviceId();
				String password=et_pass.getText().toString().trim();
				map.put("imei", imei);
				map.put("password", password);
				LoadDataFromNet load=new LoadDataFromNet(getApplicationContext(), ContantsUtil.REGIST_SERVLET, map);
				load.getData(new DataCallBack() {
					@Override
					public void onDataCallBack(Message msg) {
						switch (msg.what) {
						case ContantsUtil.HANDLER_GETDATA_ERR:
							tv_regist_tip.setText("×¢²áÊ§°Ü,ÍøÂç´íÎó");
							ShowToast("×¢²áÊ§°Ü,ÍøÂç´íÎó");
							break;

						case ContantsUtil.HANDLER_GETDATA_OK:
							String result=(String)msg.obj;
							try {
								JSONObject json=new JSONObject(result);
								int flag=json.optInt("flag");
								switch (flag) {
								case REGISTER_ERR:
									tv_regist_tip.setText("×¢²áÊ§°Ü,Êý¾Ý¿â´íÎó");
									ShowToast("×¢²áÊ§°Ü,Êý¾Ý¿â´íÎó");
									break;
								case REGISTER_OK:
									tv_regist_tip.setText("×¢²á³É¹¦");
									ShowToast("×¢²á³É¹¦");
									RegisterActivity.this.finish();
									startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
									overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
									break;
								case HAVE_REGISTED:
									tv_regist_tip.setText("ÒÑ¾­×¢²á¹ýÁË");
									ShowToast("ÒÑ¾­×¢²á¹ýÁË");
									break;
								case REGISTE_PASS_ERR:
									tv_regist_tip.setText("×¢²áÃÜÂë´íÎó");
									ShowToast("×¢²áÃÜÂë´íÎó");
									break;
								default:
									break;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}
						
					}
				});
				
			}
		});
	}
}
*/

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hdj.hook.R;
import com.hdj.hook.util.ContantsUtil;
import com.hdj.hook.util.LoadDataFromNet;
import com.hdj.hook.util.LoadDataFromNet.DataCallBack;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends BaseActivity {
	Button bt_regist;
	EditText et_pass,et_uid;
	TextView tv_regist_tip;
	
	Map<String, String> map;
	static final int REGISTE_UID_EXIST=4;
	static final int REGISTE_PASS_ERR=3;
	static final int HAVE_REGISTED=2;
	static final int REGISTER_OK=1;
	static final int REGISTER_ERR=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		tv_regist_tip=(TextView)findViewById(R.id.tv_regist_tip);
		
		map=new HashMap<String, String>();
		et_pass=(EditText)findViewById(R.id.password);
		et_uid=(EditText)findViewById(R.id.et_uid);
		
		bt_regist=(Button)findViewById(R.id.bt_regist);
		bt_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String imei=tm.getDeviceId();
				String password=et_pass.getText().toString().trim();
				map.put("imei", imei);
				map.put("password", password);
				map.put("uid", et_uid.getText().toString().trim());
				LoadDataFromNet load=new LoadDataFromNet(getApplicationContext(), ContantsUtil.REGIST_SERVLET, map);
				load.getData(new DataCallBack() {
					@Override
					public void onDataCallBack(Message msg) {
						switch (msg.what) {
						case ContantsUtil.HANDLER_GETDATA_ERR:
							tv_regist_tip.setText("×¢²áÊ§°Ü,ÍøÂç´íÎó");
							ShowToast("×¢²áÊ§°Ü,ÍøÂç´íÎó");
							break;

						case ContantsUtil.HANDLER_GETDATA_OK:
							String result=(String)msg.obj;
							try {
								JSONObject json=new JSONObject(result);
								int flag=json.optInt("flag");
								switch (flag) {
								case REGISTER_ERR:
									tv_regist_tip.setText("×¢²áÊ§°Ü,Êý¾Ý¿â´íÎó");
									ShowToast("×¢²áÊ§°Ü,Êý¾Ý¿â´íÎó");
									break;
								case REGISTER_OK:
									tv_regist_tip.setText("×¢²á³É¹¦");
									ShowToast("×¢²á³É¹¦");
									RegisterActivity.this.finish();
									startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
									overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
									break;
								case HAVE_REGISTED:
									tv_regist_tip.setText("ÒÑ¾­×¢²á¹ýÁË");
									ShowToast("ÒÑ¾­×¢²á¹ýÁË");
									break;
								case REGISTE_PASS_ERR:
									tv_regist_tip.setText("×¢²áÃÜÂë´íÎó");
									ShowToast("×¢²áÃÜÂë´íÎó");
									break;
								case REGISTE_UID_EXIST:
									tv_regist_tip.setText("¸Ã×¢²áIDÒÑ¾­±»×¢²áÁË£¬Çë¸ü¸Ä×¢²áID");
									ShowToast("¸Ã×¢²áIDÒÑ¾­±»×¢²áÁË£¬Çë¸ü¸Ä×¢²áID");
									break;
								default:
									break;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}
						
					}
				});
				
			}
		});
	}
}
