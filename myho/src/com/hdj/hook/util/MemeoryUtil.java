package com.hdj.hook.util;

import java.io.File;

import com.hdj.hook.activity.MainActivity;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.MemeryMode;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class MemeoryUtil {
	
	/**
	 * 手机内部SD卡
	 */
	public static void neiSdCard(HookFragment mainActivity) {
		File storageDirectory = Environment.getExternalStorageDirectory();
		StatFs fs = new StatFs(storageDirectory.getAbsolutePath());
		long blockCount = fs.getBlockCount();
		long blockSize = fs.getBlockSize();
		long availableBlocks = fs.getAvailableBlocks();
		long avaiSizeFs = availableBlocks * blockSize;
		long totalSizeFs = blockCount * blockSize;
		String totalSizeFsString = Formatter.formatFileSize(mainActivity.getActivity(), totalSizeFs);
		String avaiSizeFsString = Formatter.formatFileSize(mainActivity.getActivity(), avaiSizeFs);
		
		MemeryMode memeryMode = mainActivity.getmInfosMode().getMemeryMode();
		memeryMode.setNeiSDAvaiSize(avaiSizeFsString);
		memeryMode.setNeiSDTotalSize(totalSizeFsString);
	}
	
	/**
	 * 手机内部存储
	 */
	public static void neiMemery(HookFragment mainActivity) {
		File storageDirectory = Environment.getDataDirectory();
		StatFs fs = new StatFs(storageDirectory.getAbsolutePath());
		long blockCount = fs.getBlockCount();
		long blockSize = fs.getBlockSize();
		long availableBlocks = fs.getAvailableBlocks();
		long avaiSizeFs = availableBlocks * blockSize;
		long totalSizeFs = blockCount * blockSize;
		String totalSizeFsString = Formatter.formatFileSize(mainActivity.getActivity(), totalSizeFs);
		String avaiSizeFsString = Formatter.formatFileSize(mainActivity.getActivity(), avaiSizeFs);
		MemeryMode memeryMode = mainActivity.getmInfosMode().getMemeryMode();
		memeryMode.setNeiMemorySize(avaiSizeFsString);
		memeryMode.setNeiTotalMemorySize(totalSizeFsString);
	}

}
