package com.hdj.hook.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BluetoothUtil {

	public static String getBtMac() {
		String btMac ="";
		BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
		if (bAdapt != null) {
			btMac = bAdapt.getAddress();
		}
		return btMac;
	}

}
