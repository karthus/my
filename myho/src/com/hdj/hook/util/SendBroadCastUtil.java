package com.hdj.hook.util;

import android.content.Context;
import android.content.Intent;

public class SendBroadCastUtil {

	public static void listenApp(String flag, Context context) {
		Intent intent = new Intent();
		intent.setAction(GlobalConstant.ACTION_LISTNER_APP);
		intent.putExtra(GlobalConstant.FLAG, flag);
		context.sendBroadcast(intent);
	}
	public static void checkVpn(Context context) {
		Intent intent = new Intent();
		intent.setAction(GlobalConstant.ACTION_VPN_RECEVIER);
		intent.putExtra(GlobalConstant.FLAG, GlobalConstant.FLAG_CHECK_CONNECT);
		context.sendBroadcast(intent);
	}
}
