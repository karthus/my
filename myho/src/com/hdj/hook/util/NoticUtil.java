package com.hdj.hook.util;

import com.hdj.hook.R;
import com.hdj.hook.activity.VpnDialogActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NoticUtil {


	public static void showVpn(Context context) {
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = context.getResources().getString(R.string.vpn_has_connect);
		long when = System.currentTimeMillis();
		// 第二个参数为弹出通知瞬间的提示
		Notification	notification = new Notification(icon, tickerText, when);
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// notification.flags = Notification.DEFAULT_SOUND;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		// 内容
		CharSequence contentText = context.getResources().getString(R.string.click_to_manager);
		Intent intent = new Intent(context, VpnDialogActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		// 第二个参数为标题
		notification.setLatestEventInfo(context, tickerText, contentText, contentIntent);
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(GlobalConstant.ID_NOTIFICATION_VPN, notification);
	}
	
	public static void cancelVpn(Context context){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(GlobalConstant.ID_NOTIFICATION_VPN);
	}
	

}
