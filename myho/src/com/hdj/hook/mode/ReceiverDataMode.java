package com.hdj.hook.mode;

public class ReceiverDataMode {

	public Integer isWifi =-1;
	public static final String IS_WIFI = "isWifi";
	public String MANUFACTURER;
	public static final String MANUFACTURER_CT = "MANUFACTURER";
	public String android_id;
	public static final String ANDROID_ID = "android_id";
	public String bluetoothMac;
	public static final String BLUETOOTHMAC = "bluetoothMac";
	public String brand;
	public static final String BRAND = "brand";
	public String build_display;
	public static final String BUILD_DISPLAY = "build_display";
	public String build_host;
	public static final String BUILD_HOST = "build_host";
	public String build_id;
	public static final String BUILD_ID = "build_id";
	public String build_serial;
	public static final String BUILD_SERIAL = "build_serial";
	public String cid;
	public static final String CID = "cid";
	public String wifilist;
	public static final String WIFI_LIST = "wifilist";

	public String density;
	public static final String DENSITY = "density";
	public String device;
	public static final String DEVICE = "device";
	public String fingerprint;
	public static final String FINGERPRINT = "fingerprint";
	public String getRadioVersion;
	public static final String GETRADIOVERSION = "getRadioVersion";
	public String hardware;
	public static final String HARDWARE = "hardware";
	public String heightPixels;
	public static final String HEIGHTPIXELS = "heightPixels";
	public String imei;
	public static final String IMEI = "imei";
	public String innerIP;
	public static final String INNERIP = "innerIP";
	public String ip;
	public static final String IP = "ip";
	public String lac;
	public static final String LAC = "lac";
	public String latitude;
	public static final String LATITUDE = "latitude";
	public String longitude;
	public static final String LONGITUDE = "longitude";

	/**
	 * String android.net.wifi.WifiInfo.getMacAddress()
	 */
	public String mac;
	public static final String MAC = "mac";
	public String model;
	public static final String MODEL = "model";
	public String netWorkType;
	public static final String NETWORKTYPE = "netWorkType";
	public String netWorkoperatorName;
	public static final String NETWORKOPERATORNAME = "netWorkoperatorName";
	public String phoneNum;
	public static final String PHONENUM = "phoneNum";
	public String phoneType;
	public static final String X = "phoneType";
	public String product;
	public static final String PRODUCT = "product";
	public String release;
	public static final String RELEASE = "release";
	public String sdk;
	public static final String SDK = "sdk";
	public String setCpuName;
	public static final String SETCPUNAME = "setCpuName";

	/**
	 * String com.hdj.hook.util.TelephonyUtil.getSimOperator(TelephonyManager
	 * tm)
	 */
	public String simOperator_id;
	public static final String SIMOPERATOR_ID = "simOperator_id";
	public String simSerialNum;
	public static final String SIMSERIALNUM = "simSerialNum";
	public String simState;
	public static final String SIMSTATE = "simState";
	public String userAgent;
	public static final String USERAGENT = "userAgent";
	public String widthPixels;
	public static final String WIDTHPIXELS = "widthPixels";
	
	public String netTypeName,netSubType,netSubTypeName;
	public static final String NET_TYPE_NAME = "netTypeName";
	public static final String NET_SUB_TYPE = "netSubType";
	public static final String NET_SUB_TYPE_NAME = "netSubTypeName";
	

	public String getNetTypeName() {
		return netTypeName;
	}

	public void setNetTypeName(String netTypeName) {
		this.netTypeName = netTypeName;
	}

	public String getNetSubType() {
		return netSubType;
	}

	public void setNetSubType(String netSubType) {
		this.netSubType = netSubType;
	}

	public String getNetSubTypeName() {
		return netSubTypeName;
	}

	public void setNetSubTypeName(String netSubTypeName) {
		this.netSubTypeName = netSubTypeName;
	}

	public Integer getIsWifi() {
		return isWifi;
	}

	public void setIsWifi(Integer isWifi) {
		this.isWifi = isWifi;
	}

	/**
	 * String android.net.wifi.WifiInfo.getBSSID()
	 */
	public String wifiMac;
	public static final String WIFIMAC = "wifiMac";

	/**
	 * String android.net.wifi.WifiInfo.getSSID()
	 */
	public String wifiName;
	public static final String WIFINAME = "wifiName";

	public String getWifilist() {
		return wifilist;
	}

	public void setWifilist(String wifilist) {
		this.wifilist = wifilist;
	}

	public String getMANUFACTURER() {
		return MANUFACTURER;
	}

	public void setMANUFACTURER(String mANUFACTURER) {
		MANUFACTURER = mANUFACTURER;
	}

	public String getAndroid_id() {
		return android_id;
	}

	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}

	public String getBluetoothMac() {
		return bluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		this.bluetoothMac = bluetoothMac;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBuild_display() {
		return build_display;
	}

	public void setBuild_display(String build_display) {
		this.build_display = build_display;
	}

	public String getBuild_host() {
		return build_host;
	}

	public void setBuild_host(String build_host) {
		this.build_host = build_host;
	}

	public String getBuild_id() {
		return build_id;
	}

	public void setBuild_id(String build_id) {
		this.build_id = build_id;
	}

	public String getBuild_serial() {
		return build_serial;
	}

	public void setBuild_serial(String build_serial) {
		this.build_serial = build_serial;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getGetRadioVersion() {
		return getRadioVersion;
	}

	public void setGetRadioVersion(String getRadioVersion) {
		this.getRadioVersion = getRadioVersion;
	}

	public String getHardware() {
		return hardware;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public String getHeightPixels() {
		return heightPixels;
	}

	public void setHeightPixels(String heightPixels) {
		this.heightPixels = heightPixels;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getInnerIP() {
		return innerIP;
	}

	public void setInnerIP(String innerIP) {
		this.innerIP = innerIP;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getNetWorkType() {
		return netWorkType;
	}

	public void setNetWorkType(String netWorkType) {
		this.netWorkType = netWorkType;
	}

	public String getNetWorkoperatorName() {
		return netWorkoperatorName;
	}

	public void setNetWorkoperatorName(String netWorkoperatorName) {
		this.netWorkoperatorName = netWorkoperatorName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getSdk() {
		return sdk;
	}

	public void setSdk(String sdk) {
		this.sdk = sdk;
	}

	public String getSetCpuName() {
		return setCpuName;
	}

	public void setSetCpuName(String setCpuName) {
		this.setCpuName = setCpuName;
	}

	public String getSimOperator_id() {
		return simOperator_id;
	}

	public void setSimOperator_id(String simOperator_id) {
		this.simOperator_id = simOperator_id;
	}

	public String getSimSerialNum() {
		return simSerialNum;
	}

	public void setSimSerialNum(String simSerialNum) {
		this.simSerialNum = simSerialNum;
	}

	public String getSimState() {
		return simState;
	}

	public void setSimState(String simState) {
		this.simState = simState;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getWidthPixels() {
		return widthPixels;
	}

	public void setWidthPixels(String widthPixels) {
		this.widthPixels = widthPixels;
	}

	public String getWifiMac() {
		return wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

	public String getWifiName() {
		return wifiName;
	}

	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

}
