package com.hdj.hook.mode;

import android.text.TextUtils;

public class NetMode {
	
	public int netType,activeNetType,activeNetSubtype;
	public String activeNetTypeName,getActiveNetSubtypeName,connect_state ="",state;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGetActiveNetSubtypeName() {
		return getActiveNetSubtypeName;
	}
	
	public String getConnect_state() {
		return connect_state;
	}

	public void setConnect_state(String connect_state) {
		this.connect_state = connect_state;
	}

	public String toString(){
		if(!TextUtils.isEmpty(activeNetTypeName)){
			return activeNetTypeName+"   /   "+getActiveNetSubtypeName+"   /   "+activeNetType+"   /   "+activeNetSubtype;
		} 
		return "";
	}

	public void setGetActiveNetSubtypeName(String getActiveNetSubtypeName) {
		this.getActiveNetSubtypeName = getActiveNetSubtypeName;
	}

	public int getNetType() {
		return netType;
	}
	
	public void setNetType(int netType) {
		this.netType = netType;
	}

	public int getActiveNetType() {
		return activeNetType;
	}

	public void setActiveNetType(int activeNetType) {
		this.activeNetType = activeNetType;
	}

	public int getActiveNetSubtype() {
		return activeNetSubtype;
	}

	public void setActiveNetSubtype(int activeNetSubtype) {
		this.activeNetSubtype = activeNetSubtype;
	}

	public String getActiveNetTypeName() {
		return activeNetTypeName;
	}

	public void setActiveNetTypeName(String activeNetTypeName) {
		this.activeNetTypeName = activeNetTypeName;
	}

	

}
