package com.hdj.hook.util;

import android.content.Context;

public class ProviderUtil {
	
	//获得设备id
	public static String	getAndroidID(Context context){
		return android.provider.Settings.Secure.getString(context.getContentResolver(), 
												android.provider.Settings.Secure.ANDROID_ID);
	}
	public static String	getAndroidID2(Context context){
		return android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	}

	//获得手机是否已经启用adb调试选项
	public static boolean	isAdbEnabled(Context context){
		int	nRet = android.provider.Settings.Secure.getInt(context.getContentResolver(),
												android.provider.Settings.Secure.ADB_ENABLED, 0);
		return nRet > 0;
	}

	//获得手机是否打开了“允许未知来源”的apk安装选项
	public static boolean	isAllowedUnknownSource(Context context){
		int	nRet = android.provider.Settings.Secure.getInt(context.getContentResolver(),
												android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
		return nRet == 1;
	}

}
