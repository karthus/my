package com.hdj.hook.util;

public class GlobalConstant {

	public static String URL_BASE = "http://120.76.140.243:8787/GetIP/servlet";
	public static String URL_WAI_WANG_IP = URL_BASE + "/GetIPCityServlet";
	public static String FLAG = "flag";
	
	public static final String ACTION_VPN_RECEVIER = "com.pptpvpn";
	public static final String FLAG_CHECK_CONNECT= "flag_check_connect";
	
	// 应用的包名
	public static String THIS_PACKAGE_NAME = "com.hdj.hook";
	public static String XPOSED_01_PACKAGE_NAME = "de.robv.android.xposed.installer";
	public static String XPOSED_02_PACKAGE_NAME = "pro.burgerz.wsm.manager";
	public static String HIDE_PACAGE_NAME_01 = "com.cyjh.mobileanjian";
	public static String DELETE_DATA_BROWSER_01 = "com.android.browser";
	public static String FLAG_NEW_LISTNER_APP = "flag_new_listner_app";
	public static String FLAG_CYCLE_AUTO = "flag_cycle_auto";
	public static String FLAG_CLEAR_LISTNER_APP = "flag_clear_listner_app";
	public static String REMOTE_ADDR = "remoteAddr";

	public static String MYTAG = "mytag";
	public static String HOOK_TAG = "hook_tag";
	public static String HOOK_ERROR_TAG = "hook_error_tag";
	public static String HOOK_FILE_TAG = "hook_file_tag";
	public static String KEY_HHOOK_PACKAGE_NAME = "key_hhook_package_name";
	public static String FILE_HOOK_DATA = "h_data";
	public static String FOLDER_FILE_PATH = "1ah_file_path";
	public static String FOLDER_SYSTEM_VALUE = "1ah_st_vl";

	public static String KEY_SELECT_APP = "key_select_app";
	public static String ACTION_LISTNER_APP = "com.hlistner.app";
	public static String ACTION_HOOK_CONSTRUCTOR_FILE = "com.hconstructor.file";
	public static String ACTION_FILL_DATA = "com.hfill.data";
	// public static String APP_WAN_DOU_JIA = "com.wandoujia.";
	public static String APP_HOU_ZHU = ".apk";
	public static String CHARTSET_UTF_8 = "utf-8";
	public static String NOT_DELETE_FILE = "aaa.txt";
	public static String METHOD = "method";
	public static String RESULT = "result";
	public static String DATA = "data";
	public static String PUT_STRING = "putString";
	public static String GET_STRING = "getString";
	public static String GET_LONG = "getLong";
	public static String PUT_LONG = "putLong";
	public static String GET_FLOAT = "getFloat";
	public static String PUT_FLOAT = "putFloat";
	public static String GET_INT = "getInt";
	public static String PUT_INT = "putInt";
	public static int SIZE_26 = 26;
	public static int MIN_SDK_VERSION = 14;
	public static int ID_NOTIFICATION_VPN = 11;
	public static int VPN_IDLE_TIME = 600;

	public static final int CODE_SELECT_APP = 1;
	
	
	public static final int NET_TYPE_MOBILE = 0;
	public static final int NET_TYPE_WIFI = 1;
	

}
