package com.hdj.hook.vpn;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class PPTPVPNServiceConn implements ServiceConnection {
	public static PPTPVPNInterface pptpVpn;

	public static PPTPVPNInterface getInstance() {
		return pptpVpn;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		pptpVpn = (PPTPVPNInterface) service;

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {

	}

}
