package com.hdj.hook.util;

import java.util.ArrayList;
import java.util.List;

import com.hdj.hook.mode.AppInfosMode;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class ProcessUtil {

	/**
	 * 获取正在运行的进程的个数
	 * 
	 * @return 进程数量
	 */
	public static int getCount(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public static boolean killProcess(Context context, String packageName) {
		ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		mAm.killBackgroundProcesses(packageName);
		List<RunningAppProcessInfo> processInfos = mAm.getRunningAppProcesses();
		for (RunningAppProcessInfo processInfo : processInfos) {
			String packname = processInfo.processName;
			if (packageName.equals(packname))
				return false;
		}
		return true;
	}

}
