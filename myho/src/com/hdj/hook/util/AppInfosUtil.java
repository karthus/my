package com.hdj.hook.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hdj.hook.mode.AppInfosMode;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class AppInfosUtil {

	public static ArrayList<AppInfosMode> getInstallApp(Activity activity) {
		PackageManager mPm = activity.getPackageManager();
		ArrayList<AppInfosMode> mAppList = new ArrayList<AppInfosMode>();
		List<PackageInfo> installedPackages = mPm.getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		for (PackageInfo packageInfo : installedPackages) {
			int flags = packageInfo.applicationInfo.flags;
			if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0)
				continue;
			AppInfosMode packageInfoMode = new AppInfosMode();
			packageInfoMode.setPackageInfo(packageInfo);
			String pinYin = getLabelNamePinYin(packageInfo, activity);
			packageInfoMode.setPinYin(pinYin);

			String first_str = pinYin.trim().subSequence(0, 1).toString();
			if (first_str.matches("[A-Z]")) {
				packageInfoMode.setAlpha(first_str);
			} else {
				packageInfoMode.setAlpha("#");
			}

			mAppList.add(packageInfoMode);
		}
		Collections.sort(mAppList, new Comparator<AppInfosMode>() {

			@Override
			public int compare(AppInfosMode lhs, AppInfosMode rhs) {
				String lhs_trim = lhs.getPinYin().trim();
				String rhs_trim = rhs.getPinYin().trim();
				return lhs_trim.compareTo(rhs_trim);
			}
		});
		return mAppList;
	}

	public static String getLabelNamePinYin(PackageInfo packageInfo, Activity activity) {
		CharacterParser mCharacterParser = CharacterParser.getInstance();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(activity.getPackageManager());
		String pinYin = mCharacterParser.getSelling(loadLabel.toString()).toUpperCase();
		return pinYin;
	}

	public static String getPackageName(AppInfosMode packageInfoMode) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		String packageName = packageInfo.packageName;
		return packageName;
	}

	public static ArrayList<AppInfosMode> getAvailableData(ArrayList<AppInfosMode> browerList) {
		ArrayList<AppInfosMode> arrayList = new ArrayList<AppInfosMode>();
		for (AppInfosMode browerMode : browerList) {
			boolean ischeck = browerMode.isIscheck();
			if (ischeck) {
				arrayList.add(browerMode);
			}
		}
		return arrayList;
	}

	public static ArrayList<String> getAvailablePackageNameList(ArrayList<AppInfosMode> browerList) {
		ArrayList<AppInfosMode> availableData2 = AppInfosUtil.getAvailableData(browerList);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (AppInfosMode browerMode : availableData2) {
			String packageName = browerMode.getPackageInfo().packageName;
			arrayList.add(packageName);
		}
		return arrayList;
	}

	public static CharSequence getLabel(String packageName, Context context) {
		CharSequence loadLabel = "";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			loadLabel = applicationInfo.loadLabel(pm);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return loadLabel;
	}

	public static String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static boolean iSExternalStorageDirectory(String path) {
		int length = EXTERNAL_STORAGE_DIRECTORY.length();
		if (path.length() > length && path.subSequence(0, length).equals(EXTERNAL_STORAGE_DIRECTORY))
			return true;
		return false;
	}

	public static boolean iSApp(String path) {
		if (path.length() > GlobalConstant.APP_HOU_ZHU.length()
				&& path.substring(path.length() - 4, path.length()).equals(GlobalConstant.APP_HOU_ZHU))
			return true;
		return false;
	}

	public static boolean isDefaultHidePackageAndGlobalChangeProtect(String str) {
		if (GlobalConstant.THIS_PACKAGE_NAME.equals(str) || GlobalConstant.XPOSED_01_PACKAGE_NAME.equals(str)
				|| GlobalConstant.XPOSED_02_PACKAGE_NAME.equals(str) || GlobalConstant.HIDE_PACAGE_NAME_01.equals(str))
			return true;
		return false;
	}
	public static boolean isDefaultDeleteBrowserData(String str) {
		if (GlobalConstant.DELETE_DATA_BROWSER_01.equals(str))
			return true;
		return false;
	}

}
