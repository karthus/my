package com.hdj.hook.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtil {

	public static boolean isExternalStorageFile(String path) {
		String string = Environment.getExternalStorageDirectory().toString();
		int len = string.length();
		if(path.length() > len){
			String substring = path.substring(0, len);
		}
		if (!TextUtils.isEmpty(path) && path.length() > len && path.substring(0, len).equals(string))
			return true;

		return false;
	}

}
