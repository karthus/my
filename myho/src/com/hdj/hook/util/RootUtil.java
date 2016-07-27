package com.hdj.hook.util;

public class RootUtil {

	public static boolean isRoot() {
		try {
			Process process = Runtime.getRuntime().exec("su");
			process.getOutputStream().write("exit\n".getBytes());
			process.getOutputStream().flush();
			int i = process.waitFor();
			if (0 == i) {
				process = Runtime.getRuntime().exec("su");
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;

	}

}
