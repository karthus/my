package com.hdj.hook;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static Context context;
	public static MyApplication application;
	public boolean isAuto = false;
	public boolean isLogined = false;

	public void onCreate() {
		super.onCreate();
		application = this;
		MyApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

	private String imei;

	public String getImei() {
		return imei;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
	public boolean getisLogined() {
		return isLogined;
	}

	public void setisLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public static MyApplication getInstance() {
		return application;
	}

}
