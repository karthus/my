package com.hdj.hook.mode;

public class WifiMode {

	/**
	 * SSID/���������
	 */
	public String ssid;
	/**
	 * BSSID/������ַ
	 */
	public String wifilist;
	public String bssid;
	public String macAddress;
	public String getWifilist() {
		return wifilist;
	}

	public void setWifilist(String wifilist) {
		this.wifilist = wifilist;
	}

	public int linkSpeed;
	/**
	 * Rssi/��������Ľ����ź�ǿ��
	 */
	public int rssi;
	
	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(int linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

}
