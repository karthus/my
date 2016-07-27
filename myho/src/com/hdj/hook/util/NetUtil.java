package com.hdj.hook.util;

import com.hdj.hook.mode.NetMode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.text.TextUtils;
import android.util.Log;

public class NetUtil {

	/**
	 * 判断是否有手机网络
	 * 
	 * @param context
	 *            上下文
	 * @return true：有手机网络
	 * 
	 *         false :没有手机网络
	 */
	public static boolean isMobileConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mobileInfo.isConnected();
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return true：有网络
	 * 
	 *         false :没有网络
	 */
	public static boolean iSHasNet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}

	/**
	 * 判断是否有wifi网络
	 * 
	 * @param context
	 *            上下文
	 * @return true：有wifi网络
	 * 
	 *         false :没有wifi网络
	 */
	public static boolean isWifiConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo.isConnected();
	}



	
	public static String wifi_mobile_connect_state(Context context){
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (localConnectivityManager == null) {
			return "";
		}
		String str ="";
		NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		str += "wifi:"+	localNetworkInfo1.getState();
		str +="      ";
		localNetworkInfo1 = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		str += "mobile:"+	localNetworkInfo1.getState();
		return str;
	}
	public static NetMode getNetMode(Context context) {
		NetMode netMode = new NetMode();
		
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return netMode;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netMode;
		}
		return toSetNetMode(netMode,networkInfo);
	}
	
	public static NetMode getNetMode2(Context context) {
		NetMode netMode = new NetMode();
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (localConnectivityManager == null) {
			return netMode;
		}
		NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
			return toSetNetMode(netMode,localNetworkInfo1);
		}
		localNetworkInfo1 = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
			return toSetNetMode(netMode,localNetworkInfo1);
		}
		return netMode;
	}

	private static NetMode toSetNetMode(NetMode netMode, NetworkInfo localNetworkInfo1) {
		netMode.setActiveNetType(localNetworkInfo1.getType());
		netMode.setActiveNetSubtype(localNetworkInfo1.getSubtype());
		netMode.setActiveNetTypeName(localNetworkInfo1.getTypeName());
		netMode.setGetActiveNetSubtypeName(localNetworkInfo1.getSubtypeName());
		netMode.setState(localNetworkInfo1.getState().toString());
		return netMode;
	}

	public static String netWorkInfo(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		String typeName = null;
		int type = -1;
		int subtype = -1;

		if (networkInfo == null) {
			typeName = "UNKNOW";
		} else {
			type = networkInfo.getType();
			typeName = networkInfo.getTypeName();
			subtype = networkInfo.getSubtype();
			// switch (type) {
			// case 0:
			// typeName = "MOBILE";
			// switch (subtype) {
			// case 7:
			// typeName = "1xRTT";
			// break;
			// case 4:
			// typeName = "CDMA";
			// break;
			// case 2:
			// typeName = "EDGE";
			// break;
			// case 5:
			// typeName = "EVDO_0";
			// break;
			// case 6:
			// typeName = "EVDO_A";
			// break;
			// case 1:
			// typeName = "GPRS";
			// break;
			// case 3:
			// typeName = "UMTS";
			// break;
			// case 14:
			// typeName = "EHRPD";
			// break;
			// case 12:
			// typeName = "EVDO_B";
			// break;
			// case 8:
			// typeName = "HSDPA";
			// break;
			// case 10:
			// typeName = "HSPA";
			// break;
			// case 15:
			// typeName = "HSPAP";
			// break;
			// case 9:
			// typeName = "HSUPA";
			// break;
			// case 11:
			// typeName = "IDEN";
			// break;
			// case 13:
			// typeName = "LTE";
			// default:
			// break;
			// }
			// break;
			// case 1:
			// typeName = "WIFI";
			// break;
			// case 6:
			// typeName = "WIMAX";
			// break;
			// default:
			// break;
			// }

		}
		return "\ngetTypeName:" + typeName + "\ngetType:" + type + "\ngetSubtype:" + subtype;
	}

}
