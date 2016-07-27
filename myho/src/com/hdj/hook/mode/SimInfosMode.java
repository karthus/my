package com.hdj.hook.mode;

public class SimInfosMode {
	
	String operator;//注册的网络运营商
	private int phoneType,sim_state;
	String sim_serial;//手机序列号
	String phone_num;//手机号码
	String sim_operator_name;
	String network_operator_name;
	String imei;//IMEI
	public String getNetwork_operator_name() {
		return network_operator_name;
	}

	public void setNetwork_operator_name(String network_operator_name) {
		this.network_operator_name = network_operator_name;
	}

	String sim_country_iso;
	String sim_operator;

	
	
	public int getSim_state() {
		return sim_state;
	}

	public void setSim_state(int sim_state) {
		this.sim_state = sim_state;
	}

	public String getSim_operator() {
		return sim_operator;
	}

	public void setSim_operator(String sim_operator) {
		this.sim_operator = sim_operator;
	}

	public String getImei() {
		return imei;
	}

	public String getSim_country_iso() {
		return sim_country_iso;
	}

	public void setSim_country_iso(String sim_country_iso) {
		this.sim_country_iso = sim_country_iso;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public String getSim_operator_name() {
		return sim_operator_name;
	}

	public void setSim_operator_name(String sim_operator_name) {
		this.sim_operator_name = sim_operator_name;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getSim_serial() {
		return sim_serial;
	}

	public void setSim_serial(String sim_serial) {
		this.sim_serial = sim_serial;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}

}
