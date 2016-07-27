package com.hdj.hook.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class InnerIPUtil {

	/*
	 * 获取IP内网
	 */
	public static String getLocalIP(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (wifiManager.isWifiEnabled()) {
			// wifiManager.setWifiEnabled(true);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ip = intToIp(ipAddress);
			return ip;
			// return ip + "(WIFI)";
		} else {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
						.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ip = inetAddress.getHostAddress().toString();
							return ip;
							// return ip + "GPRS";
						}
					}
				}
			} catch (SocketException ex) {

			}
		}
		return null;
	}

	public static String intToIp(long i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	public static long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[3]) << 24 | Long.valueOf(items[2]) << 16 | Long.valueOf(items[1]) << 8
				| Long.valueOf(items[0]);
	}

}
