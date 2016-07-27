package com.hdj.hook.util;

import android.os.Build;

public class BuildUtil {

	/**
	 * 
	 * @return 系统版本
	 */
	public static String getRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * @return 手机品牌
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 
	 * @return 手机型号
	 */
	public static String getMold() {
		return android.os.Build.MODEL;
	}

	/**
	 * 
	 * @return 主板
	 */
	public static String getBoard() {
		return android.os.Build.BOARD;
	}

	/**
	 * 
	 * @return 产品名称
	 */
	public static String getProduct() {
		return android.os.Build.PRODUCT;
	}

	public static String getCpuAbi() {
		return android.os.Build.CPU_ABI;
	}

	public static String getCpuAbi2() {
		return android.os.Build.CPU_ABI2;
	}

	/**
	 * ID/修订版本
	 * 
	 * @return
	 */
	public static String getID() {
		return android.os.Build.ID;
	}

	/**
	 * 
	 * @return 软件版本号
	 */
	public static String getDisplay() {
		return android.os.Build.DISPLAY;
	}

	/**
	 * 
	 * @return 指纹/唯一识别码
	 */
	public static String getFingerprint() {
		return android.os.Build.FINGERPRINT;
	}

	/**
	 * 
	 * @return 版本代号
	 */
	public static String getCodename() {
		return android.os.Build.VERSION.CODENAME;
	}

	/**
	 * 
	 * @return 制造商
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 
	 * @return HOST/系统主机名
	 */
	public static String getHost() {
		return android.os.Build.HOST;
	}

	/**
	 * 
	 * @return 用户组
	 */
	public static String getType() {
		return android.os.Build.TYPE;
	}

	/**
	 * 
	 * @return SDK版本号(方法1)
	 */
	public static int getSDK_INT() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 
	 * @return SDK版本号(方法2)
	 */
	public static String getSDK() {
		return android.os.Build.VERSION.SDK;
	}

	/**
	 * 
	 * @return 标签/系统标记
	 */
	public static String getTags() {
		return android.os.Build.TAGS;
	}

	/**
	 * 
	 * @return 设备驱动/设备参数
	 */
	public static String getDevice() {
		return android.os.Build.DEVICE;
	}

	/**
	 * 
	 * @return 硬件序列号
	 */
	public static String getSerial() {
		return android.os.Build.SERIAL;
	}

	public static String getBootloader() {
		return android.os.Build.BOOTLOADER;
	}

	/**
	 * 
	 * @return 源码控制版本号
	 */
	public static String getIncremental() {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	public static String getRadioVersion() {
		return android.os.Build.getRadioVersion();
	}

}
