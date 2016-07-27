package com.hdj.hook;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdj.hook.mode.DataListMode;
import com.hdj.hook.mode.ReceiverDataMode;
import com.hdj.hook.util.AppInfosUtil;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.GsonUtil;
import com.hdj.hook.util.InnerIPUtil;
import com.hdj.hook.util.SPrefUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {

	public static ReceiverDataMode getHookMode(XSharedPreferences mXSPrefHook) {
		String str = mXSPrefHook.getString(SPrefUtil.KEY_HOOK, "");
		ReceiverDataMode receiverDataMode = null;
		if (!TextUtils.isEmpty(str)) {
			Gson gson = GsonUtil.getInstance();
			receiverDataMode = gson.fromJson(str, ReceiverDataMode.class);
		}
		return receiverDataMode;
	}

	String input_method;
	HashMap<String, Integer> mInputMethodMap;

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
//		Log.v(GlobalConstant.HOOK_TAG, "--loading--77--" + packageName);
		// XposedHelpers.findField(HookUtil.class, "IS_HOOK").set(null, 1);

		if (mXSPrefHook == null)
			mXSPrefHook = new XSharedPreferences(GlobalConstant.THIS_PACKAGE_NAME, SPrefUtil.KEY_HOOK);
		ReceiverDataMode hookMode = getHookMode(mXSPrefHook);
		if (hookMode == null)
			return;
		if (mXSPrefSetting == null)
			mXSPrefSetting = new XSharedPreferences(GlobalConstant.THIS_PACKAGE_NAME, SPrefUtil.KEY_SETTING);
		try {
			String packageName = lpparam.packageName;
			ClassLoader classLoader = lpparam.classLoader;
			ApplicationInfo appInfo = lpparam.appInfo;
			Log.v(GlobalConstant.HOOK_TAG, "--loading--00--" + packageName);
			// system app return
			if (appInfo == null || ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
					|| TextUtils.isEmpty(packageName))
				return;
			boolean hook_global = mXSPrefSetting.getBoolean(SPrefUtil.KEY_MODIFY_GLOBAL, true);

			String hook_package_name = mXSPrefSetting.getString(GlobalConstant.KEY_HHOOK_PACKAGE_NAME, "");
			String global_chagne_protect = mXSPrefSetting.getString(SPrefUtil.KEY_GLOBAL_CHAGNE_PROTECT, "");
			if (input_method == null) {
				input_method = mXSPrefSetting.getString(SPrefUtil.KEY_INPUT_METHOD, "");
			}
			if (!TextUtils.isEmpty(input_method) && mInputMethodMap == null) {
				mInputMethodMap = new HashMap<String, Integer>();
				Gson gson = new Gson();
				ArrayList<String> browserList = gson.fromJson(input_method, DataListMode.class).getDataList();
				for (String string : browserList) {
					mInputMethodMap.put(string, 1);
				}
			}

			boolean hook_test = mXSPrefSetting.getBoolean(SPrefUtil.KEY_MODIFY_TEST, false);
			// this package and is test mode then return
			Log.v(GlobalConstant.HOOK_TAG, "--loading--22--" + packageName);
			if (AppInfosUtil.isDefaultHidePackageAndGlobalChangeProtect(packageName)) {
				Log.v(GlobalConstant.HOOK_TAG, "--loading--33--" + packageName);
				if (packageName.equals(GlobalConstant.THIS_PACKAGE_NAME) && !hook_test) {
					Log.v(GlobalConstant.HOOK_TAG, "--loading--44--" + packageName);
					return;
				} else if (!packageName.equals(GlobalConstant.THIS_PACKAGE_NAME)) {
					Log.v(GlobalConstant.HOOK_TAG, "--loading--55--" + packageName);
					return;
				}
			} else if (hook_global && !TextUtils.isEmpty(global_chagne_protect)
					&& !packageName.equals(hook_package_name)) {
				Gson gson = new Gson();
				ArrayList<String> browserList = gson.fromJson(global_chagne_protect, DataListMode.class).getDataList();
				for (String string : browserList) {
					if (packageName.equals(string)) {
						Log.v(GlobalConstant.HOOK_TAG, "--loading--66--" + packageName);
						return;
					}
				}
			}
			Log.v(GlobalConstant.HOOK_TAG, "--loading--77--" + packageName);
			if (!hook_global && !(packageName.equals(hook_package_name)
					|| packageName.equals(GlobalConstant.THIS_PACKAGE_NAME))) {
				return;
			}

			Log.v(GlobalConstant.HOOK_TAG, "--loading--11--" + packageName);

			if (!GlobalConstant.THIS_PACKAGE_NAME.equals(packageName)) {
				XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", classLoader,
						"getInstalledPackages", int.class, new AppInfos_XC_MethodHook(INSTALLED_PACKAGES, packageName));
				XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", classLoader,
						"getInstalledApplications", int.class,
						new AppInfos_XC_MethodHook(INSTALLED_PACKAGES2, packageName));
				HookMethod(ActivityManager.class, "getRunningAppProcesses", packageName, INSTALLED_PACKAGES3);

				HookMethod(TelephonyManager.class, "getDeviceId", packageName, GET_DEVICE_ID);
			}

			HookMethod(LocationManager.class, "getLastKnownLocation", packageName, GET_LAST_KNOWN_LOCATION,
					String.class);

			HookMethod(Location.class, "getLatitude", packageName, GET_LATITUDE);
			HookMethod(Location.class, "getLongitude", packageName, GET_LONGITUDE);

			HookMethod(TelephonyManager.class, "getCellLocation", packageName, GET_CELL_LOCATION);
			HookMethod(CdmaCellLocation.class, "getNetworkId", packageName, GET_CELL_ID);
			HookMethod(CdmaCellLocation.class, "getBaseStationId", packageName, GET_CELL_LAC);
			HookMethod(GsmCellLocation.class, "getCid", packageName, GET_CELL_ID);
			HookMethod(GsmCellLocation.class, "getLac", packageName, GET_CELL_LAC);

			HookMethod(WifiInfo.class, "getIpAddress", packageName, GET_INNER_IP_INT);

			HookMethod(Secure.class, "getString", packageName, GET_ANDROID_ID, ContentResolver.class, String.class);
			HookMethod(System.class, "getString", packageName, GET_ANDROID_ID, ContentResolver.class, String.class);

			HookMethod(WebView.class, "getSettings", packageName, GET_USER_AGENT);
			HookMethod(WebSettings.class, "getDefaultUserAgent", packageName, GET_USER_AGENT2, Context.class);

			HookMethod(TelephonyManager.class, "hasIccCard", packageName, GET_HAS_ICC_CARD);
			HookMethod(TelephonyManager.class, "getSimSerialNumber", packageName, GET_SIM_SERIAL_NUMBER);

			HookMethod(TelephonyManager.class, "getLine1Number", packageName, GET_LINE1_NUMBER);

			HookMethod(TelephonyManager.class, "getPhoneType", packageName, GET_PHONE_TYPE);

			HookMethod(TelephonyManager.class, "getNetworkOperatorName", packageName, GET_NETWORK_OPERATOR_NAME);
			HookMethod(TelephonyManager.class, "getSimState", packageName, GET_SIM_STATE);
			HookMethod(TelephonyManager.class, "getSimOperator", packageName, GET_SIM_OPERATOR);

			HookMethod(WifiInfo.class, "getBSSID", packageName, GET_WIFI_BSSID);
			HookMethod(WifiInfo.class, "getMacAddress", packageName, GET_MAC_ADDRESS);
			HookMethod(WifiInfo.class, "getSSID", packageName, GET_WIFI_SSID);
			HookMethod(WifiManager.class, "getScanResults", packageName, GET_WIFI_LIST);

			HookMethod(TelephonyManager.class, "getNetworkType", packageName, GET_NET_TYPE);

			HookMethod(NetworkInfo.class, "getType", packageName, GET_ACTIVE_TYPE);
			HookMethod(NetworkInfo.class, "getTypeName", packageName, GET_ACTIVE_TYPE_NAME);
			HookMethod(NetworkInfo.class, "getSubtypeName", packageName, GET_SUB_TYPE_NAME);
			HookMethod(NetworkInfo.class, "getSubtype", packageName, GET_ACTIVE_SUBTYPE);
			HookMethod(ConnectivityManager.class, "getNetworkInfo", packageName, GET_NET_WORK_INFO, int.class);

			String build_release = hookMode.getRelease();
			if (!TextUtils.isEmpty(build_release)) {
				XposedHelpers.findField(android.os.Build.VERSION.class, "RELEASE").set(null, build_release);
			}

			String build_brand = hookMode.getBrand();
			if (!TextUtils.isEmpty(build_brand)) {
				XposedHelpers.findField(android.os.Build.class, "BRAND").set(null, build_brand);
			}

			String build_mold = hookMode.getModel();
			if (!TextUtils.isEmpty(build_mold)) {
				XposedHelpers.findField(android.os.Build.class, "MODEL").set(null, build_mold);
			}
			String build_product = hookMode.getProduct();
			if (!TextUtils.isEmpty(build_product)) {
				XposedHelpers.findField(android.os.Build.class, "PRODUCT").set(null, build_product);
			}

			String build_cpu_abi2 = hookMode.getSetCpuName();
			if (!TextUtils.isEmpty(build_cpu_abi2)) {
				XposedHelpers.findField(android.os.Build.class, "CPU_ABI").set(null, build_cpu_abi2);
				XposedHelpers.findField(android.os.Build.class, "CPU_ABI2").set(null, build_cpu_abi2);
			}

			String build_id = hookMode.getBuild_id();
			if (!TextUtils.isEmpty(build_id)) {
				XposedHelpers.findField(android.os.Build.class, "ID").set(null, build_id);
			}

			String build_display = hookMode.getBuild_display();
			if (!TextUtils.isEmpty(build_display)) {
				XposedHelpers.findField(android.os.Build.class, "DISPLAY").set(null, build_display);
			}

			String build_fingerprint = hookMode.getFingerprint();
			if (!TextUtils.isEmpty(build_display)) {
				XposedHelpers.findField(android.os.Build.class, "FINGERPRINT").set(null, build_fingerprint);
			}

			String build_manufacturer = hookMode.getMANUFACTURER();
			if (!TextUtils.isEmpty(build_manufacturer)) {
				XposedHelpers.findField(android.os.Build.class, "MANUFACTURER").set(null, build_manufacturer);
			}

			String build_host = hookMode.getBuild_host();
			if (!TextUtils.isEmpty(build_host)) {
				XposedHelpers.findField(android.os.Build.class, "HOST").set(null, build_host);
			}

			String build_sdk = hookMode.getSdk();

			// SDK版本
			if (!TextUtils.isEmpty(build_sdk)) {
				if (!packageName.equals(GlobalConstant.THIS_PACKAGE_NAME)
						|| ((packageName.equals(GlobalConstant.THIS_PACKAGE_NAME)
								&& Integer.valueOf(build_sdk) >= GlobalConstant.MIN_SDK_VERSION))) {
					// XposedHelpers.findField(android.os.Build.VERSION.class,
					// "SDK_INT").setInt(null,
					// Integer.valueOf(build_sdk));
					XposedHelpers.findField(android.os.Build.VERSION.class, "SDK").set(null, build_sdk);
				}
			}

			String build_device = hookMode.getDevice();
			if (!TextUtils.isEmpty(build_device)) {
				XposedHelpers.findField(android.os.Build.class, "DEVICE").set(null, build_device);
			}

			String build_serial = hookMode.getBuild_serial();
			if (!TextUtils.isEmpty(build_serial)) {
				XposedHelpers.findField(android.os.Build.class, "SERIAL").set(null, build_serial);
			}

			HookMethod(android.os.Build.class, "getRadioVersion", packageName, GET_BUILD_RADIO_VERSION);

			if (mInputMethodMap == null || (mInputMethodMap != null && !mInputMethodMap.containsKey(packageName))) {
				HookMethod(Display.class, "getMetrics", packageName, GET_DISPLAY, DisplayMetrics.class);
				HookMethod(Resources.class, "getDisplayMetrics", packageName, GET_DISPLAY2);
				HookMethod(Display.class, "getHeight", packageName, GET_HEIGHT);
				HookMethod(Display.class, "getWidth", packageName, GET_WIDTH);
				HookMethod(Display.class, "getSize", packageName, GET_SIZE, Point.class);
			}
			HookMethod(BluetoothAdapter.class, "getAddress", packageName, GET_BLUETOOTH_ADDRESS);

			// ----------------------------------------------------------

			// XposedHelpers.findField(android.os.Build.VERSION.class,
			// "INCREMENTAL").set(null,
			// GetUtils.getStr(GetUtils.BUILD_INCREMENTAL));
			// XposedHelpers.findField(android.os.Build.class, "TAGS").set(null,
			// GetUtils.getStr(GetUtils.BUILD_TAG));
			// XposedHelpers.findField(android.os.Build.class, "TYPE").set(null,
			// GetUtils.getStr(GetUtils.BUILD_TYPE));
			// XposedHelpers.findField(android.os.Build.VERSION.class,
			// "CODENAME").set(null,
			// GetUtils.getStr(GetUtils.BUILD_CODE_NAME));

			// HookMethod(TelephonyManager.class, "getNetworkOperator",
			// packageName, GET_NETWORK_OPERATOR);

			//

			// HookMethod(WifiInfo.class, "getLinkSpeed", packageName,
			// GET_LINK_SPEED);
			// HookMethod(WifiInfo.class, "getRssi", packageName, GET_RSSI);
			//

			// XposedHelpers.findField(android.os.Build.class,
			// "BOARD").set(null, GetUtils.getStr(GetUtils.BUILD_BOARD));

			if (!packageName.equals(hook_package_name))
				return;

			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader,
					new Object[] { String.class.getName(), new My_XC_MethodHook(packageName) });
			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader,
					new Object[] { String.class.getName(), String.class.getName(), new My_XC_MethodHook(packageName) });

			HookMethod(File.class, "createNewFile", packageName, GET_CREATE_APP);
			HookMethod(File.class, "renameTo", packageName, GET_CREATE_APP2, File.class);

			SystemVauleHook(GlobalConstant.PUT_STRING, packageName, SYSTEM_VALUE_PUT_STRING, ContentResolver.class,
					String.class, String.class);
			SystemVauleHook(GlobalConstant.GET_STRING, packageName, SYSTEM_VALUE_GET_STRING, ContentResolver.class,
					String.class);

			// bellow not change now
			// SystemVauleHook(GlobalConstant.GET_LONG, packageName,
			// SYSTEM_VALUE_GET_LONG_2, ContentResolver.class,
			// String.class);
			// SystemVauleHook(GlobalConstant.GET_LONG, packageName,
			// SYSTEM_VALUE_GET_LONG_3, ContentResolver.class,
			// String.class, long.class);
			// SystemVauleHook(GlobalConstant.PUT_LONG, packageName,
			// SYSTEM_VALUE_PUT_LONG, ContentResolver.class,
			// String.class, long.class);
			// SystemVauleHook(GlobalConstant.GET_FLOAT, packageName,
			// SYSTEM_VALUE_GET_FLOAT_2, ContentResolver.class,
			// String.class);
			// SystemVauleHook(GlobalConstant.GET_FLOAT, packageName,
			// SYSTEM_VALUE_GET_FLOAT_3, ContentResolver.class,
			// String.class, float.class);
			// SystemVauleHook(GlobalConstant.PUT_FLOAT, packageName,
			// SYSTEM_VALUE_PUT_FLOAT, ContentResolver.class,
			// String.class, float.class);
			// SystemVauleHook(GlobalConstant.GET_INT, packageName,
			// SYSTEM_VALUE_GET_INT_2, ContentResolver.class,
			// String.class);
			// SystemVauleHook(GlobalConstant.GET_INT, packageName,
			// SYSTEM_VALUE_GET_INT_3, ContentResolver.class,
			// String.class, int.class);
			// SystemVauleHook(GlobalConstant.PUT_INT, packageName,
			// SYSTEM_VALUE_PUT_INT, ContentResolver.class,
			// String.class, int.class);

		} catch (Exception e) {
			Log.v(GlobalConstant.HOOK_ERROR_TAG, "--Exception--" + e.toString());
		}
	}

	public static final int GET_DEVICE_ID = 1;
	public static final int GET_LATITUDE = 2;
	public static final int GET_LONGITUDE = 3;

	public static final int GET_CELL_ID = 5;
	public static final int GET_CELL_LAC = 22;
	public static final int GET_CELL_LOCATION = 6;
	public static final int GET_NETWORK_OPERATOR = 7;
	public static final int GET_SIM_SERIAL_NUMBER = 17;
	public static final int GET_HAS_ICC_CARD = 25;
	public static final int GET_LINE1_NUMBER = 18;
	public static final int GET_ANDROID_ID = 19;
	public static final int GET_ANDROID_ID_2 = 20;
	public static final int GET_PHONE_TYPE = 21;
	public static final int GET_SIM_OPERATOR = 23;
	public static final int GET_SIM_STATE = 24;

	public static final int GET_WIFI_BSSID = 8;
	public static final int GET_WIFI_SSID = 9;
	public static final int GET_MAC_ADDRESS = 10;
	public static final int GET_LINK_SPEED = 11;
	public static final int GET_RSSI = 12;

	public static final int GET_NET_TYPE = 4;
	public static final int GET_ACTIVE_TYPE_NAME = 14;
	public static final int GET_ACTIVE_TYPE = 15;
	public static final int GET_ACTIVE_SUBTYPE = 16;

	public static final int INSTALLED_PACKAGES = 31;
	public static final int GET_LAST_KNOWN_LOCATION = 32;
	public static final int GET_BUILD_RADIO_VERSION = 33;
	public static final int GET_DISPLAY = 34;
	public static final int GET_BLUETOOTH_ADDRESS = 35;
	public static final int GET_CREATE_APP = 36;
	public static final int GET_NETWORK_OPERATOR_NAME = 37;
	public static final int GET_INNER_IP_STR = 38;
	public static final int GET_INNER_IP_INT = 39;
	public static final int GET_WIFI_LIST = 40;
	public static final int GET_USER_AGENT = 41;
	public static final int GET_USER_AGENT2 = 42;

	public static final int INSTALLED_PACKAGES2 = 43;
	public static final int INSTALLED_PACKAGES3 = 44;
	public static final int GET_DISPLAY2 = 45;
	public static final int GET_HEIGHT = 46;
	public static final int GET_WIDTH = 47;
	public static final int GET_SIZE = 50;
	public static final int GET_CREATE_APP2 = 51;
	public static final int GET_SUB_TYPE_NAME = 52;
	public static final int GET_NET_WORK_INFO = 54;

	public static final int SYSTEM_VALUE_GET_INT_2 = 101;
	public static final int SYSTEM_VALUE_GET_INT_3 = 102;
	public static final int SYSTEM_VALUE_GET_FLOAT_2 = 103;
	public static final int SYSTEM_VALUE_GET_FLOAT_3 = 104;
	public static final int SYSTEM_VALUE_GET_LONG_2 = 105;
	public static final int SYSTEM_VALUE_GET_LONG_3 = 106;
	public static final int SYSTEM_VALUE_GET_STRING = 107;

	public static final int SYSTEM_VALUE_PUT_FLOAT = 108;
	public static final int SYSTEM_VALUE_PUT_INT = 109;
	public static final int SYSTEM_VALUE_PUT_STRING = 110;
	public static final int SYSTEM_VALUE_PUT_LONG = 111;

	private void HookMethod(final Class<?> class1, String methodName, final String packageName, final int type) {
		XposedHelpers.findAndHookMethod(class1, methodName,
				new Object[] { new AppInfos_XC_MethodHook(type, packageName) });
	}

	private void SystemVauleHook(String methodName, final String packageName, final int type, Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type, packageName);
		XposedHelpers.findAndHookMethod(System.class, methodName, new_objects);
	}

	private void HookMethod(final Class<?> class1, String methodName, final String packageName, final int type,
			Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type, packageName);
		XposedHelpers.findAndHookMethod(class1, methodName, new_objects);
	}

	XSharedPreferences mXSPrefHook;
	XSharedPreferences mXSPrefSetting;

	class AppInfos_XC_MethodHook extends XC_MethodHook {
		String packageName_str;
		String pref_str;
		int type;
		String packageName;
		String str_markets;
		ReceiverDataMode hookMode;
		HashMap<String, Integer> mHidePackageNameMap;

		public AppInfos_XC_MethodHook(int type, String packageName) {
			this.type = type;
			this.packageName = packageName;
			if (mXSPrefHook == null)
				mXSPrefHook = new XSharedPreferences(GlobalConstant.THIS_PACKAGE_NAME, SPrefUtil.KEY_HOOK);
			hookMode = getHookMode(mXSPrefHook);
			if (mXSPrefSetting == null)
				mXSPrefSetting = new XSharedPreferences(GlobalConstant.THIS_PACKAGE_NAME, SPrefUtil.KEY_SETTING);

			switch (type) {
			case INSTALLED_PACKAGES:
			case INSTALLED_PACKAGES2:
			case INSTALLED_PACKAGES3:
				mHidePackageNameMap = new HashMap<String, Integer>();
				pref_str = mXSPrefSetting.getString(SPrefUtil.KEY_HIDE_PACKAGE_NAME, "");
				if (!TextUtils.isEmpty(pref_str)) {
					Gson gson = new Gson();
					ArrayList<String> browserList = gson.fromJson(pref_str, DataListMode.class).getDataList();
					for (String string : browserList) {
						mHidePackageNameMap.put(string, 1);
					}
				}

				break;

			default:
				break;
			}

		}

		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			Log.v(GlobalConstant.HOOK_TAG+"Amy", "---type--" + type + "/" + packageName);
			// super.afterHookedMethod(param);
			if(type==GET_CREATE_APP){
				Log.v(GlobalConstant.HOOK_TAG+"Amy", "---type--" + type + "/" + packageName+"====="+param.getResult()+"----"+param.thisObject);
			}
			int cell_id;
			int cell_lac;
			String str = null;
			String str2 = null;
			float float1, float2;
			boolean boolean1;
			File file1;
			int int1 = -5;
			Object result2;
			int int2 = -5;
			String a = "";
			String b = "";
			String c = "";
			Object[] args3;
			DisplayMetrics metric;
			switch (type) {
			case SYSTEM_VALUE_GET_INT_2:
				a = "getInt";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_INT_3:
				a = "getInt";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_FLOAT_2:
				a = "getFloat";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_FLOAT_3:
				a = "getFloat";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_LONG_2:
				a = "getLong";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_LONG_3:
				a = "getLong";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_GET_STRING:
				a = "getString";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_PUT_FLOAT:
				a = "putFloat";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_PUT_INT:
				a = "putInt";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_PUT_STRING:
				a = "putString";
				str = param.getResult().toString();
				break;
			case SYSTEM_VALUE_PUT_LONG:
				a = "putLong";
				str = param.getResult().toString();
				break;
			case GET_LAST_KNOWN_LOCATION:
				result2 = param.getResult();
				if (result2 != null)
					return;
				// str = mXSPrefHook.getString(GetUtils.LONGITUDE, "");
				// str2 = mXSPrefHook.getString(GetUtils.LATITUDE, "");
				str = hookMode.getLongitude();
				str2 = hookMode.getLatitude();
				if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
					param.setResult(Double.valueOf(str));
					Location location = new Location(LocationManager.NETWORK_PROVIDER);
					location.setLatitude(Double.valueOf(str2));
					location.setLongitude(Double.valueOf(str));
					param.setResult(location);
				}
				break;
			case GET_LATITUDE:
				// double latitude = GetUtils.getDouble(GetUtils.LATITUDE);
				str = hookMode.getLatitude();
				// str = mXSPrefHook.getString(GetUtils.LATITUDE, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(Double.valueOf(str));
				}
				break;
			case GET_LONGITUDE:
				// double longitude = GetUtils.getDouble(GetUtils.LONGITUDE);\
				str = hookMode.getLongitude();
				// str = mXSPrefHook.getString(GetUtils.LONGITUDE, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(Double.valueOf(str));
				}
				break;
			case GET_CELL_LOCATION:
				// cellID = GetUtils.getInt(GetUtils.CELL_ID);
				// int cellLac = GetUtils.getInt(GetUtils.CELL_LAC);
				result2 = param.getResult();
				if (result2 != null)
					return;
				cell_id = Integer.valueOf(hookMode.getCid());
				cell_lac = Integer.valueOf(hookMode.getLac());
				// cell_id = mXSPrefHook.getInt(GetUtils.CELL_ID, -1);
				// cell_lac = mXSPrefHook.getInt(GetUtils.CELL_LAC, -1);
				if (cell_id > 0 && cell_lac > 0) {
					GsmCellLocation gsmCellLocation = new GsmCellLocation();
					gsmCellLocation.setLacAndCid(cell_lac, cell_id);
					CellLocation cellLocation = gsmCellLocation;
					param.setResult(cellLocation);
				}
				break;
			case GET_CELL_ID:
				// cellID = GetUtils.getInt(GetUtils.CELL_ID);
				cell_id = Integer.valueOf(hookMode.getCid());
				// cell_id = mXSPrefHook.getInt(GetUtils.CELL_ID, -1);
				if (cell_id > 0) {
					param.setResult(cell_id);
				}
				break;
			case GET_CELL_LAC:
				cell_lac = Integer.valueOf(hookMode.getLac());
				// cell_lac = mXSPrefHook.getInt(GetUtils.CELL_LAC, -1);
				if (cell_lac > 0) {
					param.setResult(cell_lac);
				}
				break;
			case GET_INNER_IP_INT:
				str = hookMode.getInnerIP();
				// str = mXSPrefHook.getString(GetUtils.INNER_IP, "");
				if (!TextUtils.isEmpty(str)) {
					int ip2int = (int) InnerIPUtil.ip2int(str);
					param.setResult(ip2int);
				}
				break;
			case GET_ANDROID_ID:
				if (param.args.length > 1 && param.args[1].equals(android.provider.Settings.Secure.ANDROID_ID)) {
					// String android_id = GetUtils.getStr(GetUtils.ANDROID_ID);
					str = hookMode.getAndroid_id();
					// str = mXSPrefHook.getString(GetUtils.ANDROID_ID, "");
					if (!TextUtils.isEmpty(str)) {
						param.setResult(str);
					}
				}
				break;
			case GET_USER_AGENT:
				str = hookMode.getUserAgent();
				WebSettings webSettings2 = (WebSettings) param.getResult();
				// str = mXSPrefHook.getString(GetUtils.USER_AGENT, "");
				if (!TextUtils.isEmpty(str)) {
					webSettings2.setUserAgentString(str);
					param.setResult(webSettings2);
				}
				break;
			case GET_USER_AGENT2:
				String build_sdk = hookMode.getSdk();
				// String build_sdk = mXSPrefHook.getString(GetUtils.BUILD_SDK,
				// "");
				if (!TextUtils.isEmpty(build_sdk)) {
					Integer valueOf = Integer.valueOf(build_sdk);
					if (valueOf > 16) {
						str = hookMode.getUserAgent();
						// str = mXSPrefHook.getString(GetUtils.USER_AGENT, "");
						if (!TextUtils.isEmpty(str)) {
							param.setResult(str);
						}
					}
				}

				break;
			case GET_DEVICE_ID:
				str = hookMode.getImei();
				// str = mXSPrefHook.getString(GetUtils.IMEI, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_SIM_SERIAL_NUMBER:
				str = hookMode.getSimSerialNum();
				// str = mXSPrefHook.getString(GetUtils.SIM_SERIAL, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_HAS_ICC_CARD:
				param.setResult(true);
				break;
			case GET_LINE1_NUMBER:
				// String phone_num = GetUtils.getStr(GetUtils.PHONE_NUM);
				str = hookMode.getPhoneNum();
				// str = mXSPrefHook.getString(GetUtils.PHONE_NUM, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_PHONE_TYPE:
				int1 = Integer.valueOf(hookMode.getPhoneType());
				// int1 = mXSPrefHook.getInt(GetUtils.PHONE_TYPE, -5);
				if (int1 > -3) {
					param.setResult(int1);
				}
				break;
			case GET_NETWORK_OPERATOR_NAME:
				str = hookMode.getNetWorkoperatorName();
				// str = mXSPrefHook.getString(GetUtils.NETWORK_OPERATOR_NAME,
				// "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_SIM_STATE:
				int1 = Integer.valueOf(hookMode.getSimState());
				// int1 = mXSPrefHook.getInt(GetUtils.SIM_STATE, -5);
				if (int1 > -3) {
					param.setResult(int1);
				}
				break;
			case GET_SIM_OPERATOR:
				str = hookMode.getSimOperator_id();
				// str = mXSPrefHook.getString(GetUtils.SIM_OPERATOR, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;

			case GET_WIFI_BSSID:
				// String wifi_bssid = GetUtils.getStr(GetUtils.WIFI_BSSID);
				int1 = hookMode.getIsWifi();
				// int1 = mXSPrefHook.getInt(GetUtils.IS_WIFI, -1);
				switch (int1) {
				case GlobalConstant.NET_TYPE_MOBILE:
					param.setResult(null);
					break;
				case GlobalConstant.NET_TYPE_WIFI:
					str = hookMode.getWifiMac();
					// str = mXSPrefHook.getString(GetUtils.WIFI_BSSID, "");
					if (!TextUtils.isEmpty(str)) {
						param.setResult(str);
					}
					break;

				default:
					break;
				}

				break;
			case GET_MAC_ADDRESS:
				// String wifi_macaddress =
				// GetUtils.getStr(GetUtils.WIFI_MACADDRESS);
				str = hookMode.getMac();
				// str = mXSPrefHook.getString(GetUtils.WIFI_MACADDRESS, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_WIFI_SSID:
				// String wifi_ssid = GetUtils.getStr(GetUtils.WIFI_SSID);
				int1 = hookMode.getIsWifi();
				switch (int1) {
				case GlobalConstant.NET_TYPE_MOBILE:
					param.setResult("<unknown ssid>");
					break;
				case GlobalConstant.NET_TYPE_WIFI:
					str = hookMode.getWifiName();
					if (!TextUtils.isEmpty(str)) {
						param.setResult(str);
					}
					break;

				default:
					break;
				}

				break;
			case GET_WIFI_LIST:
				List<ScanResult> result3;
				int1 = hookMode.getIsWifi();
				switch (int1) {
				case GlobalConstant.NET_TYPE_MOBILE:
					result3 = (List<ScanResult>) param.getResult();
					if (result3.size() > 0) {
						result3.removeAll(result3);
						param.setResult(result3);
					}
					break;
				case GlobalConstant.NET_TYPE_WIFI:
					str = hookMode.getWifilist();
					result3 = (List<ScanResult>) param.getResult();
					List<ScanResult> list2 = new ArrayList<ScanResult>();
					if (result3.size() > 0 && !TextUtils.isEmpty(str)) {
						Gson gson = new Gson();
						List<com.mz.iplocation.Mode.WifiMode> list = gson.fromJson(str,
								new TypeToken<ArrayList<com.mz.iplocation.Mode.WifiMode>>() {
								}.getType());
						int size = result3.size();
						int count = 0;
						for (com.mz.iplocation.Mode.WifiMode wifiMode : list) {
							ScanResult scanResult = result3.get(count);
							scanResult.BSSID = wifiMode.getBssid();
							scanResult.SSID = wifiMode.getSsid();
							scanResult.frequency = wifiMode.getFrequency();
							scanResult.level = wifiMode.getLevel();
							scanResult.capabilities = wifiMode.getCapabilities();
							list2.add(scanResult);
							count++;
							if (count == size)
								count = 0;
						}
						param.setResult(list2);
					}
					break;

				default:
					break;
				}

				break;

			case GET_NET_TYPE:
				int1 = Integer.valueOf(hookMode.getNetWorkType());
				// int1 = mXSPrefHook.getInt(GetUtils.NET_TYPE, -5);
				if (int1 > -3) {
					param.setResult(int1);
				}
				break;

			case GET_SUB_TYPE_NAME:
				str = hookMode.getNetSubTypeName();
				param.setResult(str);
				break;
			case GET_ACTIVE_TYPE_NAME:
				str = hookMode.getNetTypeName();
				param.setResult(str);
				break;
			case GET_ACTIVE_TYPE:
				int1 = Integer.valueOf(hookMode.getIsWifi());
				param.setResult(int1);
				break;
			case GET_ACTIVE_SUBTYPE:
				int1 = Integer.valueOf(hookMode.getNetSubType());
				param.setResult(int1);
				break;
			case GET_NET_WORK_INFO:
				args3 = param.args;
				int1 = (int) args3[0];
				int2 = hookMode.getIsWifi();
				result2 = param.getResult();
				if (result2 == null)
					return;
				NetworkInfo networkInfo = (NetworkInfo) result2;
				Field field = Class.forName("android.net.NetworkInfo").getDeclaredField("mState");
				field.setAccessible(true);
				switch (int1) {
				case ConnectivityManager.TYPE_WIFI:
					switch (int2) {
					case GlobalConstant.NET_TYPE_MOBILE:
						field.set(networkInfo, State.DISCONNECTED);
						param.setResult(networkInfo);
						break;
					case GlobalConstant.NET_TYPE_WIFI:
						field.set(networkInfo, State.CONNECTED);
						param.setResult(networkInfo);
						break;
					default:
						break;
					}
					break;
				case ConnectivityManager.TYPE_MOBILE:
					switch (int2) {
					case GlobalConstant.NET_TYPE_MOBILE:
						field.set(networkInfo, State.CONNECTED);
						param.setResult(networkInfo);
						break;
					case GlobalConstant.NET_TYPE_WIFI:
						field.set(networkInfo, State.DISCONNECTED);
						param.setResult(networkInfo);
						break;
					default:
						break;
					}
					break;

				default:
					break;
				}

				break;
			case GET_BUILD_RADIO_VERSION:
				str = hookMode.getGetRadioVersion();
				// str = mXSPrefHook.getString(GetUtils.BUILD_RADIO_VERSION,
				// "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;
			case GET_DISPLAY:
				Object[] args = param.args;
				if (args[0] == null) {
					return;
				}
				metric = (DisplayMetrics) args[0];
				// float1 = mXSPrefHook.getFloat(GetUtils.DISPLAY_XDPI, -1);
				// if (float1 < 0)
				// return;
				// float2 = mXSPrefHook.getFloat(GetUtils.DISPLAY_YDPI, -1);
				// metric.xdpi = float1;
				// metric.ydpi = float2;

				int2 = Integer.valueOf(hookMode.getHeightPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_HEIGHTPIXELS, -1);
				if (int2 < 1)
					return;
				metric.heightPixels = int2;
				int2 = Integer.valueOf(hookMode.getWidthPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_WIDTHPIXELS, -1);
				metric.widthPixels = int2;

				boolean1 = mXSPrefSetting.getBoolean(SPrefUtil.KEY_MODIFY_DENSITY, false);
				if (boolean1) {
					int2 = Integer.valueOf(hookMode.getDensity());
					// int2 = mXSPrefHook.getInt(GetUtils.DENSITY_DPI, -1);
					metric.densityDpi = int2;
				}
				param.setResult(metric);
				break;
			case GET_DISPLAY2:
				result2 = param.getResult();
				if (result2 == null) {
					return;
				}
				metric = (DisplayMetrics) result2;
				// float1 = mXSPrefHook.getFloat(GetUtils.DISPLAY_XDPI, -1);
				// if (float1 < 0)
				// return;
				// float2 = mXSPrefHook.getFloat(GetUtils.DISPLAY_YDPI, -1);
				// metric.xdpi = float1;
				// metric.ydpi = float2;

				int2 = Integer.valueOf(hookMode.getHeightPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_HEIGHTPIXELS, -1);
				if (int2 < 1)
					return;
				metric.heightPixels = int2;
				int2 = Integer.valueOf(hookMode.getWidthPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_WIDTHPIXELS, -1);
				metric.widthPixels = int2;

				boolean1 = mXSPrefSetting.getBoolean(SPrefUtil.KEY_MODIFY_DENSITY, false);
				if (boolean1) {
					int2 = Integer.valueOf(hookMode.getDensity());
					// int2 = mXSPrefHook.getInt(GetUtils.DENSITY_DPI, -1);
					metric.densityDpi = int2;
				}

				param.setResult(metric);
				break;
			case GET_HEIGHT:
				int2 = Integer.valueOf(hookMode.getHeightPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_HEIGHTPIXELS, -1);
				if (int2 < 1)
					return;
				param.setResult(int2);
				break;
			case GET_WIDTH:
				int2 = Integer.valueOf(hookMode.getWidthPixels());
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_WIDTHPIXELS, -1);
				if (int2 < 1)
					return;
				param.setResult(int2);
				break;
			case GET_SIZE:
				Object[] args2 = param.args;
				if (args2[0] == null) {
					return;
				}
				Point point = (Point) args2[0];
				int2 = Integer.valueOf(hookMode.getWidthPixels());
				int1 = Integer.valueOf(hookMode.getHeightPixels());
				// int1 = mXSPrefHook.getInt(GetUtils.DISPLAY_HEIGHTPIXELS, -1);
				// int2 = mXSPrefHook.getInt(GetUtils.DISPLAY_WIDTHPIXELS, -1);
				// int1 = mXSPrefHook.getInt(GetUtils.DISPLAY_HEIGHTPIXELS, -1);
				if (int2 < 0)
					return;
				point.set(int2, int1);
				param.setResult(point);
				break;
			case GET_BLUETOOTH_ADDRESS:
				str = hookMode.getBluetoothMac();
				// str = mXSPrefHook.getString(GetUtils.BLUETOOTH_ADDRESS, "");
				if (!TextUtils.isEmpty(str)) {
					param.setResult(str);
				}
				break;

			case INSTALLED_PACKAGES:
				// "cn.mxtrip.ontheroad"
				pref_str = mXSPrefSetting.getString(SPrefUtil.KEY_HIDE_PACKAGE_NAME, "");
				List<PackageInfo> result = (List<PackageInfo>) param.getResult();
				List<PackageInfo> arrayList = new ArrayList<PackageInfo>();
				for (PackageInfo packageInfo : result) {
					str = packageInfo.packageName;
					if (isHidePackageName(str, pref_str, packageName, mHidePackageNameMap))
						continue;
					arrayList.add(packageInfo);
				}
				param.setResult(arrayList);
				break;
			case INSTALLED_PACKAGES2:
				// "cn.mxtrip.ontheroad"
				pref_str = mXSPrefSetting.getString(SPrefUtil.KEY_HIDE_PACKAGE_NAME, "");
				List<ApplicationInfo> ApplicationInfo_result3 = (List<ApplicationInfo>) param.getResult();
				List<ApplicationInfo> ApplicationInfo_arrayList2 = new ArrayList<ApplicationInfo>();
				for (ApplicationInfo packageInfo : ApplicationInfo_result3) {
					str = packageInfo.packageName;
					if (isHidePackageName(str, pref_str, packageName, mHidePackageNameMap))
						continue;
					ApplicationInfo_arrayList2.add(packageInfo);
				}
				param.setResult(ApplicationInfo_arrayList2);
				break;
			case INSTALLED_PACKAGES3:
				// "cn.mxtrip.ontheroad"
				pref_str = mXSPrefSetting.getString(SPrefUtil.KEY_HIDE_PACKAGE_NAME, "");
				List<RunningAppProcessInfo> RunningAppProcessInfo_result3 = (List<RunningAppProcessInfo>) param
						.getResult();
				List<RunningAppProcessInfo> RunningAppProcessInfo_arrayList2 = new ArrayList<RunningAppProcessInfo>();
				for (RunningAppProcessInfo packageInfo : RunningAppProcessInfo_result3) {
					str = packageInfo.processName;
					if (isHidePackageName(str, pref_str, packageName, mHidePackageNameMap))
						continue;
					RunningAppProcessInfo_arrayList2.add(packageInfo);
				}
				param.setResult(RunningAppProcessInfo_arrayList2);
				break;
			case GET_CREATE_APP:
				boolean1 = (boolean) param.getResult();
				if (boolean1) {
					file1 = (File) param.thisObject;
					str = file1.getAbsolutePath();
					if (AppInfosUtil.iSApp(str))
						RecordToFile.saveMarketFilePath(packageName, str);
					RecordToFile.saveFilePath(packageName, str);
				}

				break;
			case GET_CREATE_APP2:
				boolean1 = (boolean) param.getResult();
				if (boolean1) {
					Object object = param.args[0];
					if (object == null)
						return;
					file1 = (File) param.args[0];
					str = file1.getAbsolutePath();
					if (AppInfosUtil.iSApp(str))
						RecordToFile.saveMarketFilePath(packageName, str);
					RecordToFile.saveFilePath(packageName, str);
				}

				break;

			// --------------------------------------------------------------------------

			case GET_NETWORK_OPERATOR:
				break;

			case GET_LINK_SPEED:
				break;
			case GET_RSSI:
				break;

			default:
				break;
			}

			// 系统值操作
			if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(str)) {
				Object[] args = param.args;
				if (args.length > 1) {
					ArrayList<String> arrayList = new ArrayList<>();
					for (int i = 1; i < args.length; i++) {
						arrayList.add(args[i].toString());
					}
					HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
					hashMap.put(GlobalConstant.DATA, arrayList);
					Gson gson = new Gson();
					String json2 = gson.toJson(hashMap);
					JSONObject json = new JSONObject(json2);
					json.put(GlobalConstant.METHOD, a);
					json.put(GlobalConstant.RESULT, str);
					RecordToFile.saveSystemValue(packageName, json.toString());
				}
			}
		}

	}

	private boolean isHidePackageName(String str, String pref_str, String packageName,
			HashMap<String, Integer> mLocalProtectMap) {
		// String packageName_str = '"' + str + '"';
		if (packageName.equals(GlobalConstant.XPOSED_01_PACKAGE_NAME)
				|| packageName.equals(GlobalConstant.XPOSED_02_PACKAGE_NAME))
			return false;
		if (AppInfosUtil.isDefaultHidePackageAndGlobalChangeProtect(str) || mLocalProtectMap.containsKey(str))
			return true;
		return false;
	}

	class My_XC_MethodHook extends XC_MethodHook {

		String packageName;

		public My_XC_MethodHook(String packageName) {
			super();
			this.packageName = packageName;
		}

		@Override
		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
			Object[] args = param.args;
			String path = null;
			if (args.length == 2) {
				path = (args[0].toString() + "/" + args[1].toString());
			} else if (args.length == 1) {
				path = args[0].toString();
			}
			if (isProtectFile(path))
				return;
			if (!AppInfosUtil.iSExternalStorageDirectory(path))
				return;
			RecordToFile.saveFilePath(packageName, path);
		}

	}

	public static boolean isProtectFile(String path) {
		int lastIndexOf = path.lastIndexOf(GlobalConstant.NOT_DELETE_FILE);
		if (lastIndexOf > 0 && (path.length() == (lastIndexOf + GlobalConstant.NOT_DELETE_FILE.length())))
			return true;
		return false;
	}

}
