package com.hdj.hook.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hdj.hook.mode.WifiMode;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtil {

	public static WifiMode get(Context context) {
		WifiMode wifiMode = new WifiMode();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		wifiMode.setSsid(info.getSSID());
		wifiMode.setBssid(info.getBSSID());
		wifiMode.setMacAddress(info.getMacAddress());
		wifiMode.setLinkSpeed(info.getLinkSpeed());
		wifiMode.setRssi(info.getRssi());
		List<ScanResult> scanResults = wifiManager.getScanResults();
		ArrayList<com.mz.iplocation.Mode.WifiMode> arrayList = new ArrayList<>();
		for (ScanResult scanResult : scanResults) {
			String bSSID = scanResult.BSSID;  
			int level = scanResult.level;
			String sSID = scanResult.SSID;
			int frequency = scanResult.frequency;
			String capabilities = scanResult.capabilities;
			com.mz.iplocation.Mode.WifiMode wifiMode2 = new com.mz.iplocation.Mode.WifiMode();
			wifiMode2.setBssid(bSSID);
			wifiMode2.setSsid(sSID);
			wifiMode2.setLevel(level);
			wifiMode2.setFrequency(frequency);
			wifiMode2.setCapabilities(capabilities);
			arrayList.add(wifiMode2);
		}
		if (arrayList.size() > 0) {
			Gson gson = new Gson();
			String json = gson.toJson(arrayList);
			wifiMode.setWifilist(json);
		}

		return wifiMode;

	}

	public static boolean isWifiEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();

	}

}
