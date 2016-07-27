package com.hdj.hook.util;

/*  * �� �� ��:  DataCleanManager.java  * ��    ��:  ��Ҫ�����������/�⻺�棬������ݿ⣬���sharedPreference�����files������Զ���Ŀ¼  */
import java.io.File;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/** * ��Ӧ��������������� */
public class DataCleanManager {
	/** * �����Ӧ���ڲ�����(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * �����Ӧ���������ݿ�(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	/**
	 * * �����Ӧ��SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/** * �����������Ӧ�����ݿ� * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * ���/data/data/com.xxx.xxx/files�µ����� * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * * ����ⲿcache�µ�����(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	 * context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** * ����Զ���·���µ��ļ���ʹ����С�ģ��벻Ҫ��ɾ������ֻ֧��Ŀ¼�µ��ļ�ɾ�� * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/** * �����Ӧ�����е����� * * @param context * @param filepath */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/** * ɾ������ ����ֻ��ɾ��ĳ���ļ����µ��ļ�����������directory�Ǹ��ļ������������� * * @param directory */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * * ɾ������ ����ֻ��ɾ��ĳ���ļ����µ��ļ����ļ��У���������directory�Ǹ��ļ������������� * * @param directory
	 */
	public static boolean deleteFilesByDirectory2(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isDirectory()) {
					deleteFilesByDirectory2(item);
				} else {
					boolean delete = item.delete();
				}
			}
			return directory.delete();
		} else {
			return true;
		}
	}
}