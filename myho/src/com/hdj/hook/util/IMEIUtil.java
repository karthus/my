package com.hdj.hook.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class IMEIUtil {

	/**
	 * 
	 * @return ÊÖ»úµÄIMEI
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

}
