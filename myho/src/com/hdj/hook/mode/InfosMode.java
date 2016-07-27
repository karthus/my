package com.hdj.hook.mode;

public class InfosMode {

	public String innerIp, remote_addr, android_id, android_id2, user_agent, user_agent2, build_release, build_brand,
			build_mold, build_board, build_product, build_cup_abi, build_cup_abi2, build_manufacturer, build_id,
			build_display, build_fingerprint, build_code_name, build_host, build_type, build_sdk, build_tag,
			build_device, build_serial, build_bootloader, build_incremental, build_radio_version, bluetooth_address,
			cpu_main_frequency, kernel_version, base_band_version;
	public Integer isWifi =-1;
	


	public String getUser_agent() {
		return user_agent;
	}

	public String getUser_agent2() {
		return user_agent2;
	}

	public void setUser_agent2(String user_agent2) {
		this.user_agent2 = user_agent2;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getBuild_radio_version() {
		return build_radio_version;
	}

	public void setBuild_radio_version(String build_radio_version) {
		this.build_radio_version = build_radio_version;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	private double longitude, latitude;
	private float logLanAccuracy;
	private int build_sdk_int;
	private SCellMode scell = new SCellMode();
	private TimeZooMode timeZooMode;

	private DisplayMode displayMode;

	public DisplayMode getDisplayMode2() {
		return displayMode2;
	}

	public void setDisplayMode2(DisplayMode displayMode2) {
		this.displayMode2 = displayMode2;
	}

	private DisplayMode displayMode2;
	private WifiMode wifi;
	private NetMode netMode,netMode2;
	public NetMode getNetMode2() {
		return netMode2;
	}

	public void setNetMode2(NetMode netMode2) {
		this.netMode2 = netMode2;
	}

	private MemeryMode memeryMode = new MemeryMode();
	private SimInfosMode simInfosMode = new SimInfosMode();

	public SimInfosMode getSimInfosMode() {
		return simInfosMode;
	}

	public void setSimInfosMode(SimInfosMode simInfosMode) {
		this.simInfosMode = simInfosMode;
	}

	public MemeryMode getMemeryMode() {
		return memeryMode;
	}

	public void setMemeryMode(MemeryMode memeryMode) {
		this.memeryMode = memeryMode;
	}

	public String getBase_band_version() {
		return base_band_version;
	}

	public void setBase_band_version(String base_band_version) {
		this.base_band_version = base_band_version;
	}

	public String getCpu_main_frequency() {
		return cpu_main_frequency;
	}

	public String getKernel_version() {
		return kernel_version;
	}

	public void setKernel_version(String kernel_version) {
		this.kernel_version = kernel_version;
	}

	public void setCpu_main_frequency(String cpu_main_frequency) {
		this.cpu_main_frequency = cpu_main_frequency;
	}

	public String getBluetooth_address() {
		return bluetooth_address;
	}

	public void setBluetooth_address(String bluetooth_address) {
		this.bluetooth_address = bluetooth_address;
	}

	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(DisplayMode displayMode) {
		this.displayMode = displayMode;
	}

	public String getRemote_addr() {
		return remote_addr;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public TimeZooMode getTimeZooMode() {
		return timeZooMode;
	}

	public void setTimeZooMode(TimeZooMode timeZooMode) {
		this.timeZooMode = timeZooMode;
	}

	public String getAndroid_id() {
		return android_id;
	}

	public String getAndroid_id2() {
		return android_id2;
	}

	public void setAndroid_id2(String android_id2) {
		this.android_id2 = android_id2;
	}

	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}

	public NetMode getNetMode() {
		return netMode;
	}

	public void setNetMode(NetMode netMode) {
		this.netMode = netMode;
	}

	public WifiMode getWifi() {
		return wifi;
	}

	public void setWifi(WifiMode wifi) {
		this.wifi = wifi;
	}

	public SCellMode getScell() {
		return scell;
	}

	public void setScell(SCellMode scell) {
		this.scell = scell;
	}

	public float getLogLanAccuracy() {
		return logLanAccuracy;
	}

	public void setLogLanAccuracy(float logLanAccuracy) {
		this.logLanAccuracy = logLanAccuracy;
	}

	public String getBuild_bootloader() {
		return build_bootloader;
	}

	public String getBuild_incremental() {
		return build_incremental;
	}

	public void setBuild_incremental(String build_incremental) {
		this.build_incremental = build_incremental;
	}

	public void setBuild_bootloader(String build_bootloader) {
		this.build_bootloader = build_bootloader;
	}

	public String getBuild_serial() {
		return build_serial;
	}

	public void setBuild_serial(String build_serial) {
		this.build_serial = build_serial;
	}

	public String getBuild_device() {
		return build_device;
	}

	public void setBuild_device(String build_device) {
		this.build_device = build_device;
	}

	public String getBuild_tag() {
		return build_tag;
	}

	public void setBuild_tag(String build_tag) {
		this.build_tag = build_tag;
	}

	public String getBuild_code_name() {
		return build_code_name;
	}

	public String getBuild_sdk() {
		return build_sdk;
	}

	public void setBuild_sdk(String build_sdk) {
		this.build_sdk = build_sdk;
	}

	public int getBuild_sdk_int() {
		return build_sdk_int;
	}

	public void setBuild_sdk_int(int build_sdk_int) {
		this.build_sdk_int = build_sdk_int;
	}

	public String getBuild_type() {
		return build_type;
	}

	public void setBuild_type(String build_type) {
		this.build_type = build_type;
	}

	public String getBuild_host() {
		return build_host;
	}

	public void setBuild_host(String build_host) {
		this.build_host = build_host;
	}

	public void setBuild_code_name(String build_code_name) {
		this.build_code_name = build_code_name;
	}

	public String getBuild_cup_abi2() {
		return build_cup_abi2;
	}

	public void setBuild_cup_abi2(String build_cup_abi2) {
		this.build_cup_abi2 = build_cup_abi2;
	}

	public String getBuild_board() {
		return build_board;
	}

	public String getBuild_fingerprint() {
		return build_fingerprint;
	}

	public void setBuild_fingerprint(String build_fingerprint) {
		this.build_fingerprint = build_fingerprint;
	}

	public String getBuild_display() {
		return build_display;
	}

	public void setBuild_display(String build_display) {
		this.build_display = build_display;
	}

	public String getBuild_id() {
		return build_id;
	}

	public void setBuild_id(String build_id) {
		this.build_id = build_id;
	}

	public String getBuild_manufacturer() {
		return build_manufacturer;
	}

	public void setBuild_manufacturer(String build_manufacturer) {
		this.build_manufacturer = build_manufacturer;
	}

	public String getBuild_cup_abi() {
		return build_cup_abi;
	}

	public void setBuild_cup_abi(String build_cup_abi) {
		this.build_cup_abi = build_cup_abi;
	}

	public String getBuild_product() {
		return build_product;
	}

	public void setBuild_product(String build_product) {
		this.build_product = build_product;
	}

	public void setBuild_board(String build_board) {
		this.build_board = build_board;
	}

	public String getBuild_mold() {
		return build_mold;
	}

	public void setBuild_mold(String build_mold) {
		this.build_mold = build_mold;
	}

	public String getBuild_brand() {
		return build_brand;
	}

	public void setBuild_brand(String build_brand) {
		this.build_brand = build_brand;
	}

	public String getBuild_release() {
		return build_release;
	}

	public void setBuild_release(String build_release) {
		this.build_release = build_release;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
