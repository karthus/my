package com.hdj.hook.vpn;

import com.hdj.hook.R;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.NoticUtil;
import com.hdj.hook.util.SendBroadCastUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class PPTPVPNService extends Service {
	Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		improvePriority();
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				SendBroadCastUtil.checkVpn(PPTPVPNService.this);
				handler.sendEmptyMessageDelayed(0, 3 * 1000);
			};
		};
		handler.sendEmptyMessageDelayed(0, 3 * 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return new PPTPVPN();
	}

	class PPTPVPN extends Binder implements PPTPVPNInterface {

		@Override
		public void start(VpnProfile profile, Context context) {
			startVpn(profile, context);
		}

		@Override
		public void stop(Context context) {
			stopVpn(context);
		}

		@Override
		public void stopAuto(Context context) {
			stopVpnAuto(context);
		}

		@Override
		public boolean startAuto(VpnProfile profile, Context context) {
			return startVpnAuto(profile, context);
		}

	}

	private void improvePriority() {
		NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 我们并不需要为 notification.flags 设置 FLAG_ONGOING_EVENT，因为
		// 前台服务的 notification.flags 总是默认包含了那个标志位
		Notification notification = new Notification(R.drawable.ic_launcher, "pptpvpn", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PPTPVPNService.class), 0);
		notification.setLatestEventInfo(this, "pptpvpn", "pptpvpn", contentIntent);
		// 注意使用 startForeground ，id 为 0 将不会显示 notification
		startForeground(0, notification);
	}

	private String setConnectingStr(long time) {
		String string = getResources().getString(R.string.connectting) + "(" + time / 1000 + ")";
		return string;
	}

	private String setConnectingStr() {
		String string = getResources().getString(R.string.connectting);
		return string;
	}

	private void startVpn(final VpnProfile profile, final Context context) {
		ProgressDialogUtil pdu = new ProgressDialogUtil(context);
		final ProgressDialog pd = pdu.show();
		pd.setTitle(setConnectingStr());
		new Thread(new Runnable() {
			public void run() {
				VpnManager.startVpn(profile, context);
				pd.dismiss();
				SendBroadCastUtil.checkVpn(context);
			}
		}).start();
	}

	public static void saveVpnInfos(VpnProfile profile, Context context) {
		ProfileLoader mLoader = ProfileLoader.getInstance(context);
		mLoader.updateProfile(profile);
		mLoader.setDefault(profile);
	}

	public static void startVpnService(Context context) {
		PPTPVPNInterface pptpVpn = PPTPVPNServiceConn.getInstance();
		if (pptpVpn != null)
			return;
		PPTPVPNServiceConn conn = new PPTPVPNServiceConn();
		Intent intent = new Intent(context, PPTPVPNService.class);
		context.bindService(intent, conn, Service.BIND_AUTO_CREATE);
	}

	private boolean startVpnAuto(final VpnProfile profile, Context context) {
		return VpnManager.startAutoVpn(profile, context);
	}

	// private void stopVpn(Context context) {
	// ProgressDialogUtil pdu = new ProgressDialogUtil(context);
	// final ProgressDialog pd = pdu.show();
	// pd.setTitle(getResources().getString(R.string.disconnectting));
	// new Thread(new Runnable() {
	// public void run() {
	// VpnManager.stopVpn();
	//
	// WifiManager wfiManager = (WifiManager)
	// getSystemService(Context.WIFI_SERVICE);
	// wfiManager.reassociate();
	//
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// pd.dismiss();
	//// if (VpnManager.isStarted()) {
	//// Intent intent = new Intent();
	//// intent.setAction(GlobalConstant.ACTION_VPN_RECEVIER);
	//// intent.putExtra(GlobalConstant.FLAG,
	// GlobalConstant.FLAG_VPN_CONNECT);
	//// sendBroadcast(intent);
	//// } else {
	//// Intent intent = new Intent();
	//// intent.setAction(GlobalConstant.ACTION_VPN_RECEVIER);
	//// intent.putExtra(GlobalConstant.FLAG,
	// GlobalConstant.FLAG_VPN_CONNECT_FAIL);
	//// sendBroadcast(intent);
	//// }
	//
	//
	//
	// }
	// }).start();
	//
	// }

	private void stopVpn(Context context) {
//		VpnManager.stopVpn();
		VpnManager.stopVpn2();
		SendBroadCastUtil.checkVpn(context);
	}

	private void stopVpnAuto(Context context) {
		VpnManager.stopVpnAuto(context);
	}

}
