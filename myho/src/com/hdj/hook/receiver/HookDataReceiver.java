package com.hdj.hook.receiver;

import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.ReceiverDataMode;
import com.hdj.hook.util.GetUtils;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.SPrefUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HookDataReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		final String info = intent.getExtras().getString("param");
		Log.i("Recevier1", "Ω” ’µΩ-auto-:" + info);
		new Thread(new Runnable() {
			public void run() {
				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						boolean putHook = SPrefUtil.putHookStr(context, info, SPrefUtil.KEY_HOOK);
						if (putHook) {
							saveData(context);
							Intent intent2 = new Intent();
							intent2.setAction(GlobalConstant.ACTION_FILL_DATA);
							context.sendBroadcast(intent2);
						}

					}
				});
			}
		}).start();
	}

	private void saveData(Context context) {
		// dealLatitudeLongitude
		ReceiverDataMode receiverMode = HookFragment.getReceiverMode(context);
		SPrefUtil.putHookStr(context, String.valueOf(receiverMode.getLatitude()), GetUtils.LATITUDE);
		SPrefUtil.putHookStr(context, String.valueOf(receiverMode.getLongitude()), GetUtils.LONGITUDE);

		// dealCell
		SPrefUtil.putHookInt(context, Integer.valueOf(receiverMode.getCid()), GetUtils.CELL_ID);
		SPrefUtil.putHookInt(context, Integer.valueOf(receiverMode.getLac()), GetUtils.CELL_LAC);

		// dealInnerIP
		SPrefUtil.putHookStr(context, receiverMode.getInnerIP(), GetUtils.INNER_IP);

		// dealAndroidID
		SPrefUtil.putHookStr(context, receiverMode.getAndroid_id(), GetUtils.ANDROID_ID);

		// dealSim
		SPrefUtil.putHookStr(context, receiverMode.getImei(), GetUtils.IMEI);
		SPrefUtil.putHookStr(context, receiverMode.getSimSerialNum(), GetUtils.SIM_SERIAL);
		SPrefUtil.putHookInt(context, Integer.valueOf(receiverMode.getPhoneType()), GetUtils.PHONE_TYPE);
		SPrefUtil.putHookStr(context, receiverMode.getPhoneNum(), GetUtils.PHONE_NUM);
		SPrefUtil.putHookStr(context, receiverMode.getSimOperator_id(), GetUtils.SIM_OPERATOR);
		SPrefUtil.putHookInt(context, Integer.valueOf(receiverMode.getSimState()), GetUtils.SIM_STATE);

		// dealWifi
		SPrefUtil.putHookStr(context, receiverMode.getWifiMac(), GetUtils.WIFI_BSSID);
		SPrefUtil.putHookStr(context, receiverMode.getWifiName(), GetUtils.WIFI_SSID);
		SPrefUtil.putHookStr(context, receiverMode.getMac(), GetUtils.WIFI_MACADDRESS);

		// dealNetwork
		SPrefUtil.putHookInt(context, Integer.valueOf(receiverMode.getNetWorkType()), GetUtils.NET_TYPE);

		// dealBuildRelease
		SPrefUtil.putHookStr(context, receiverMode.getRelease(), GetUtils.BUILD_RELEASE);

		// dealBuildBrand
		SPrefUtil.putHookStr(context, receiverMode.getBrand(), GetUtils.BUILD_BRAND);

		// dealBuildMold
		SPrefUtil.putHookStr(context, receiverMode.getModel(), GetUtils.BUILD_MOLD);

		// dealBuildProduct
		SPrefUtil.putHookStr(context, receiverMode.getProduct(), GetUtils.BUILD_PRODUCT);

		// dealBuildCpuAbi2
		SPrefUtil.putHookStr(context, receiverMode.getSetCpuName(), GetUtils.BUILD_CPU_ABI2);

		// dealBuildID
		SPrefUtil.putHookStr(context, receiverMode.getBuild_id(), GetUtils.BUILD_ID);

		// dealBuildDisplay
		SPrefUtil.putHookStr(context, receiverMode.getBuild_display(), GetUtils.BUILD_DISPLAY);

		// dealBuildFingerprint
		SPrefUtil.putHookStr(context, receiverMode.getFingerprint(), GetUtils.BUILD_FINGERPRINT);

		// dealBuildManufacturer
		SPrefUtil.putHookStr(context, receiverMode.getMANUFACTURER(), GetUtils.BUILD_MANUFACTURER);

		// dealBuildHost
		SPrefUtil.putHookStr(context, receiverMode.getBuild_host(), GetUtils.BUILD_HOST);

		// dealBuildSdk
		SPrefUtil.putHookStr(context, receiverMode.getSdk(), GetUtils.BUILD_SDK);

		// dealBuildDevice
		SPrefUtil.putHookStr(context, receiverMode.getDevice(), GetUtils.BUILD_DEVICE);

		// dealBuildSerial
		SPrefUtil.putHookStr(context, receiverMode.getBuild_serial(), GetUtils.BUILD_SERIAL);

		// dealRadioVersion
		SPrefUtil.putHookStr(context, receiverMode.getGetRadioVersion(), GetUtils.BUILD_RADIO_VERSION);

		// dealBlue
		SPrefUtil.putHookStr(context, receiverMode.getBluetoothMac(), GetUtils.BLUETOOTH_ADDRESS);

	}

}
