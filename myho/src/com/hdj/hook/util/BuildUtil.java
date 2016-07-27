package com.hdj.hook.util;

import android.os.Build;

public class BuildUtil {

	/**
	 * 
	 * @return ϵͳ�汾
	 */
	public static String getRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * @return �ֻ�Ʒ��
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 
	 * @return �ֻ��ͺ�
	 */
	public static String getMold() {
		return android.os.Build.MODEL;
	}

	/**
	 * 
	 * @return ����
	 */
	public static String getBoard() {
		return android.os.Build.BOARD;
	}

	/**
	 * 
	 * @return ��Ʒ����
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
	 * ID/�޶��汾
	 * 
	 * @return
	 */
	public static String getID() {
		return android.os.Build.ID;
	}

	/**
	 * 
	 * @return ����汾��
	 */
	public static String getDisplay() {
		return android.os.Build.DISPLAY;
	}

	/**
	 * 
	 * @return ָ��/Ψһʶ����
	 */
	public static String getFingerprint() {
		return android.os.Build.FINGERPRINT;
	}

	/**
	 * 
	 * @return �汾����
	 */
	public static String getCodename() {
		return android.os.Build.VERSION.CODENAME;
	}

	/**
	 * 
	 * @return ������
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 
	 * @return HOST/ϵͳ������
	 */
	public static String getHost() {
		return android.os.Build.HOST;
	}

	/**
	 * 
	 * @return �û���
	 */
	public static String getType() {
		return android.os.Build.TYPE;
	}

	/**
	 * 
	 * @return SDK�汾��(����1)
	 */
	public static int getSDK_INT() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 
	 * @return SDK�汾��(����2)
	 */
	public static String getSDK() {
		return android.os.Build.VERSION.SDK;
	}

	/**
	 * 
	 * @return ��ǩ/ϵͳ���
	 */
	public static String getTags() {
		return android.os.Build.TAGS;
	}

	/**
	 * 
	 * @return �豸����/�豸����
	 */
	public static String getDevice() {
		return android.os.Build.DEVICE;
	}

	/**
	 * 
	 * @return Ӳ�����к�
	 */
	public static String getSerial() {
		return android.os.Build.SERIAL;
	}

	public static String getBootloader() {
		return android.os.Build.BOOTLOADER;
	}

	/**
	 * 
	 * @return Դ����ư汾��
	 */
	public static String getIncremental() {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	public static String getRadioVersion() {
		return android.os.Build.getRadioVersion();
	}

}
