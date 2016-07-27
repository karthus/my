package com.hdj.hook.receiver;

import com.hdj.hook.util.GlobalConstant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(GlobalConstant.MYTAG, "--net-state-change----" + intent.getAction());
	}

}
