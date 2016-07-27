package com.hdj.hook.mode;

import java.lang.reflect.Method;

public class BasebandVersionUtil {
	
	/**
	 * »ù´ø°æ±¾
	 * 
	 * @return
	 */

	public static String get() {
		String version = "";
		try {
			Class cl = Class.forName("android.os.SystemProperties");
			Object invoker = cl.newInstance();
			Method m = cl.getMethod("get", new Class[] { String.class, String.class });
			Object result = m.invoke(invoker, new Object[] { "gsm.version.baseband", "no message" });
			version = (String) result;
		} catch (Exception e) {
		}
		return version;
	}

}
