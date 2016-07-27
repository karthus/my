package com.hdj.hook;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import com.hdj.hook.util.GlobalConstant;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class RecordToFile {

	public static HashMap<String, Integer> filePathMap;
	public static String APP_RECORD_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "007"
			+ File.separator + "file";

	public static String getFoldPath(String foldName) {
		return APP_RECORD_FOLDER + File.separator + foldName;
	}

	public static String foldPath = getFoldPath(GlobalConstant.FOLDER_FILE_PATH);

	// public static String foldPath = Environment.getExternalStorageDirectory()
	// + File.separator
	// + GlobalConstant.FOLDER_FILE_PATH;

	public static void clearMap() {
		foldPath = null;
	}

	public static boolean isNotDeleteFile(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		if (path.contains(APP_RECORD_FOLDER)) {
			int length = APP_RECORD_FOLDER.length();
			String substring = path.substring(0, length);
			if (substring.equals(APP_RECORD_FOLDER))
				return true;
		}
		return false;
	}

	public static void saveFilePath(String packageName, String path) {
		if (filePathMap == null)
			filePathMap = new HashMap<String, Integer>();
		if (filePathMap.containsKey(path + File.separator + packageName))
			return;
		if (isNotDeleteFile(path))
			return;
		File file = new File(foldPath);
		if (!file.exists()) {
			file.mkdirs();
			file.setExecutable(true, false);
		}
		File file2 = new File(foldPath, packageName);
		try {
			if (!file2.exists()) {
				file2.createNewFile();
				// file2.setExecutable(true,false);
			}
			if (file2.exists()) {
				try {
					RandomAccessFile raf1 = new RandomAccessFile(file2, "rw");
					raf1.seek(raf1.length());
					raf1.write(("\n" + path).getBytes(GlobalConstant.CHARTSET_UTF_8));
					raf1.close();
					filePathMap.put(path + File.separator + packageName, 1);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Integer> appPathMap;

	public static void saveMarketFilePath(String packageName, String path) {
		if (appPathMap == null)
			appPathMap = new HashMap<String, Integer>();
		String key = path + File.separator + packageName;
		if (appPathMap.containsKey(key))
			return;
		try {
			File file3 = new File(APP_RECORD_FOLDER);
			if (!file3.exists()) {
				file3.mkdirs();
			}
			File file4 = new File(file3, "InstallApk1.txt");
			if (!file4.exists())
				file4.createNewFile();
			RandomAccessFile raf2 = new RandomAccessFile(file4, "rw");
			raf2.seek(raf2.length());
			raf2.write(("\n" + path).getBytes(GlobalConstant.CHARTSET_UTF_8));
			raf2.close();
			appPathMap.put(key, 1);
		} catch (Exception e) {
			Log.v(GlobalConstant.HOOK_FILE_TAG, "--GET_CREATE_APP--55--" + e.toString());
		}

	}

	public static void saveSystemValue(String packageName, String str) {
		String foldPath = getFoldPath(GlobalConstant.FOLDER_SYSTEM_VALUE);
		File file = new File(foldPath);
		if (!file.exists()) {
			file.mkdirs();
			// file.setExecutable(true,false);
		}
		File file2 = new File(foldPath, packageName);
		try {
			if (!file2.exists()) {
				file2.createNewFile();
				// file2.setExecutable(true,false);
			}
			if (file2.exists()) {
				RandomAccessFile raf = new RandomAccessFile(file2, "rw");
				raf.seek(raf.length());
				raf.write(("\n" + str).getBytes());
				raf.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean deleteAppRecordFile(String packageName) {
		File file2 = new File(APP_RECORD_FOLDER, packageName);
		boolean delete = true;
		if (file2.exists()) {
			delete = file2.delete();
		}
		return delete;
	}

}
