package com.hdj.hook.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileCacheUtil {
	private File mCacheFile;

	private String directoryName;

	/**
	 * @param directoryName
	 *            Ŀ¼��
	 */
	public FileCacheUtil(String directoryName) {
		this.directoryName = directoryName;
	}

	/**
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return
	 */
	public File getFile(String fileName) {
		if (mCacheFile == null) {
			mCacheFile = new File(Environment.getExternalStorageDirectory(), directoryName);
		}
		File file2 = new File(mCacheFile, fileName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (!mCacheFile.exists()) {
				mCacheFile.mkdirs();
			}
			try {
				if (!file2.exists())
					file2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file2;
	}

}