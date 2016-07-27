package com.hdj.hook.util;

import java.util.ArrayList;
import java.util.List;

import com.hdj.hook.mode.AppInfosMode;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class TaskInfoProvider {

	/**
	 * 获取正在运行的进程信息
	 * 
	 * @return
	 */
	public static List<AppInfosMode> getTaskInfos(Activity context) {
		PackageManager pm = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		List<AppInfosMode> taskInfos = new ArrayList<AppInfosMode>();
		String packageName = context.getPackageName();
		for (RunningAppProcessInfo processInfo : processInfos) {
			String packname = processInfo.processName;
			if (packageName.equals(packname))
				continue;
			if (GlobalConstant.XPOSED_01_PACKAGE_NAME.equals(packname))
				continue;
			AppInfosMode appInfosMode = new AppInfosMode();
			try {
				PackageInfo packInfo = pm.getPackageInfo(packname, 0);
				if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
					continue;
				String pinYin = AppInfosUtil.getLabelNamePinYin(packInfo, context);
				appInfosMode.setPinYin(pinYin);

				String first_str = pinYin.trim().subSequence(0, 1).toString();
				if (first_str.matches("[A-Z]")) {
					appInfosMode.setAlpha(first_str);
				} else {
					appInfosMode.setAlpha("#");
				}

				appInfosMode.setPackageInfo(packInfo);
				taskInfos.add(appInfosMode);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return taskInfos;
	}
}
