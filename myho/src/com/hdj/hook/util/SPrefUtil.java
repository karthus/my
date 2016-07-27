package com.hdj.hook.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPrefUtil {

	public static final String KEY_SETTING = "key_setting";
	public static final String KEY_HOOK = "key_hook";
	public static final String KEY_BROWSER = "key_browser";
	public static final String KEY_GLOBAL_CHAGNE_PROTECT = "key_protect_task";
	public static final String KEY_INPUT_METHOD = "key_input_method";
	public static final String KEY_TASK = "key_task";
	public static final String KEY_MODIFY_WIFI_LIST = "key_modify_wifi_list";
	public static final String KEY_MODIFY_DENSITY = "key_modify_density";
	public static final String KEY_MODIFY_GLOBAL = "key_modify_global";
	public static final String KEY_MODIFY_TEST = "key_modify_test";
	public static final String KEY_MODIFY_AFTER_OPERA_AUTO_OPEN_APP = "key_modify_after_opera_auto_open_app";
	public static final String KEY_FILE = "key_file";
	public static final String KEY_HIDE_PACKAGE_NAME = "key_hide_app_package_name";
	public static final String KEY_MARKET = "key_market";
	public static final String IMEI_RANDOM = "imei_random";

	private static SharedPreferences hook_pref;
	private static SharedPreferences setting_pref;

	private static SharedPreferences getSettingInstance(Context context) {
		if (setting_pref == null)
			setting_pref = context.getSharedPreferences(SPrefUtil.KEY_SETTING, Context.MODE_WORLD_READABLE);
		return setting_pref;
	}

	private static SharedPreferences getHookInstance(Context context) {
		if (hook_pref == null)
			hook_pref = context.getSharedPreferences(SPrefUtil.KEY_HOOK, Context.MODE_WORLD_READABLE);
		return hook_pref;
	}

	// ------------------
	public static boolean putHookStr(Context context, String str, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean commit = pref.edit().putString(key, str).commit();
		return commit;
	}

	public static boolean putHookFloat(Context context, Float str, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean commit = pref.edit().putFloat(key, str).commit();
		return commit;
	}

	public static boolean putHookBoolean(Context context, Boolean str, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean commit = pref.edit().putBoolean(key, str).commit();
		return commit;
	}

	public static boolean putHookInt(Context context, int str, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean commit = pref.edit().putInt(key, str).commit();
		return commit;
	}

	public static boolean clearHookStr(Context context, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean commit = pref.edit().putString(key, "").commit();
		return commit;
	}

	public static String getHookStr(Context context, String key) {
		SharedPreferences pref = getHookInstance(context);
		String browser = pref.getString(key, "");
		return browser;
	}

	public static float getHookFloat(Context context, String key) {
		SharedPreferences pref = getHookInstance(context);
		float browser = pref.getFloat(key, -1f);
		return browser;
	}

	public static int getHookInt(Context context, String key) {
		SharedPreferences pref = getHookInstance(context);
		int browser = pref.getInt(key, 0);
		return browser;
	}

	public static boolean getHookBoolean(Context context, String key) {
		SharedPreferences pref = getHookInstance(context);
		boolean str = pref.getBoolean(key, false);
		return str;
	}

	// ------------------


	public static boolean putSettingStr(Context context, String str, String key) {
		SharedPreferences pref = getSettingInstance(context);
		boolean commit = pref.edit().putString(key, str).commit();
		return commit;
	}
	
	public static boolean putSettingBoolean(Context context, Boolean str, String key) {
		SharedPreferences pref = getSettingInstance(context);
		boolean commit = pref.edit().putBoolean(key, str).commit();
		return commit;
	}
	
	public static boolean getSettingBoolean(Context context, String key) {
		SharedPreferences pref = getSettingInstance(context);
		boolean str = pref.getBoolean(key, false);
		return str;
	}
	public static boolean getSettingBooleanDefaultTrue(Context context, String key) {
		SharedPreferences pref = getSettingInstance(context);
		boolean str = pref.getBoolean(key, true);
		return str;
	}

	public static String getSettingStr(Context context, String key) {
		SharedPreferences pref = getSettingInstance(context);
		String str = pref.getString(key, "");
		return str;
	}

}
