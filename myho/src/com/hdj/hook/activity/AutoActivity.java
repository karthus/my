package com.hdj.hook.activity;

import java.util.ArrayList;

import com.hdj.hook.MyApplication;
import com.hdj.hook.R;
import com.hdj.hook.RecordToFile;
import com.hdj.hook.adapter.ConstrolAdapter;
import com.hdj.hook.fragment.FileFragment;
import com.hdj.hook.fragment.InstallFragment;
import com.hdj.hook.fragment.ListenSystemValueFragment;
import com.hdj.hook.fragment.ListnerFileFragment;
import com.hdj.hook.fragment.TaskManagerFragment;
import com.hdj.hook.util.AppInfosUtil;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.ProcessUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.StringUtil;
import com.hdj.hook.vpn.PPTPVPNInterface;
import com.hdj.hook.vpn.PPTPVPNService;
import com.hdj.hook.vpn.PPTPVPNServiceConn;
import com.hdj.hook.vpn.ProfileLoader;
import com.hdj.hook.vpn.VpnManager;
import com.hdj.hook.vpn.VpnProfile;
import com.mz.iplocation.utils.MyParamInterface;
import com.mz.iplocation.utils.SilentInstallUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ListView;

public class AutoActivity extends Activity {

	public static Activity mAutoActivity;
	public static final int ADD_DATA = 1;
	public static final int ADD_DATA_RED = 2;
	public static final int ADD_DATA_BLUE = 3;
	public static final int FINISH = 4;
	public static Handler mControlHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDataList.size() > 200) {
				for (int i = 0; i < 100; i++) {
					mDataList.remove(0);
				}
			}
			CharSequence c;
			switch (msg.what) {
			case ADD_DATA:
				c = (CharSequence) msg.obj;
				mDataList.add(c);
				break;
			case ADD_DATA_RED:
				c = (CharSequence) msg.obj;
				mDataList.add(StringUtil.SpanColor(c, Color.RED));
				break;
			case ADD_DATA_BLUE:
				c = (CharSequence) msg.obj;
				mDataList.add(StringUtil.SpanColor(c, Color.BLUE));
				break;
			case FINISH:
				Context context = (Context) msg.obj;
				autoActivityFinish(context);
				break;

			default:
				break;
			}
			if (mAdapter != null) {
				mAdapter.setData(mDataList);
				mLvList.setAdapter(mAdapter);
				mLvList.setSelection(mDataList.size());
			}
		};
	};

	public static boolean mCanBack = false;

	private static void autoActivityFinish(Context context) {
		mCanBack = true;
		mCanStart = true;
		if (mAutoActivity != null) {
//			mAutoActivity.finish();
			boolean isOpen = SPrefUtil.getSettingBooleanDefaultTrue(context,
					SPrefUtil.KEY_MODIFY_AFTER_OPERA_AUTO_OPEN_APP);
			if (isOpen)
				SilentInstallUtil.openAPK(context, packageName);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mCanBack) {
			mCanStart = true;
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	public static ArrayList<CharSequence> mDataList = new ArrayList<>();
	AutoActivity mMainActivity;

	public static final String IS_CLICK = "is_click";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		boolean isclick = intent.getBooleanExtra(IS_CLICK, false);
		if (!isclick && mCanStart) {
			finish();
			return;
		}
		if (!isclick && mDataList.size() == 0) {
			finish();
			return;
		}
		mCanBack = false;
		mAutoActivity = this;
		packageName = SPrefUtil.getSettingStr(mMainActivity, GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
		if (TextUtils.isEmpty(packageName)) {
			finish();
			return;
		}
		setContentView(R.layout.fragment_control);
		findViewById();
		mMainActivity = AutoActivity.this;
		mAdapter = new ConstrolAdapter(mMainActivity);
		mLvList.setAdapter(mAdapter);
		mLoader = ProfileLoader.getInstance(mMainActivity);
		if (mCanStart)
			toStart();
	}

	public static boolean mCanStart = true;

	public static ConstrolAdapter mAdapter;

	private static String packageName;

	public static ListView mLvList;

	private void findViewById() {
		mLvList = (ListView) findViewById(R.id.lv_list);
	}

	public static void sendHandler(CharSequence str) {
		Message msg = Message.obtain();
		msg.what = AutoActivity.ADD_DATA;
		msg.obj = str;
		AutoActivity.mControlHandler.sendMessageDelayed(msg, 200);
	}

	public static void sendHandlerOnMain(final CharSequence str, Context context) {
		((Activity) (context)).runOnUiThread(new Runnable() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = AutoActivity.ADD_DATA;
				msg.obj = str;
				AutoActivity.mControlHandler.sendMessageDelayed(msg, 200);
			}
		});
	}

	public static void sendHandlerRedOnMain(final CharSequence str, Context context) {
		((Activity) (context)).runOnUiThread(new Runnable() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = AutoActivity.ADD_DATA_RED;
				msg.obj = str;
				AutoActivity.mControlHandler.sendMessageDelayed(msg, 200);
			}
		});
	}

	public static void sendHandlerBlueOnMain(final CharSequence str, Context context) {
		((Activity) (context)).runOnUiThread(new Runnable() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = AutoActivity.ADD_DATA_BLUE;
				msg.obj = str;
				AutoActivity.mControlHandler.sendMessageDelayed(msg, 200);
			}
		});
	}

	public static void sendHandlerRed(CharSequence str) {
		Message msg = Message.obtain();
		msg.what = AutoActivity.ADD_DATA_RED;
		StringUtil.SpanColor(str, Color.RED);
		msg.obj = str;
		AutoActivity.mControlHandler.sendMessageDelayed(msg, 200);
	}

	public void toStart() {
		mCanStart = false;
		new Thread(new Runnable() {
			public void run() {
				MyApplication app = MyApplication.getInstance();
				app.setAuto(true);
				startAutoRun(mMainActivity);
			}
		}).start();
	}

	private void startAutoRun(final Context context) {
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.going), context);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_close_app), context);
		boolean killProcess = ProcessUtil.killProcess(mMainActivity, packageName);
		CharSequence label = AppInfosUtil.getLabel(packageName, mMainActivity);
		if (!killProcess) {
			AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.request_close_app) + label + "/"
					+ packageName + "\n" + getResources().getString(R.string.opera_fail), context);
		} else {
			AutoActivity.sendHandlerOnMain(getResources().getString(R.string.request_close_app) + label + "/"
					+ packageName + "\n" + getResources().getString(R.string.opera_ok), context);
		}
		
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_reset_system_value), context);
		ListenSystemValueFragment.setDefault(packageName, mMainActivity);
		
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_delete_silent_install_app),
				context);
		InstallFragment.removeInstalledAPK(context);
		boolean deleteAppRecordFile = RecordToFile.deleteAppRecordFile(packageName);
		if (deleteAppRecordFile) {
			AutoActivity.sendHandlerOnMain(getResources().getString(R.string.delete_silent_install_app_record_file)
					+ getResources().getString(R.string.opera_ok_ln), context);
		} else {
			AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.delete_silent_install_app_record_file)
					+ getResources().getString(R.string.opera_fail_ln), context);
		}

		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_clear_app_data), context);
		boolean isClear = SilentInstallUtil.clearAllData(packageName);
		if (isClear) {
			AutoActivity.sendHandlerOnMain(
					getResources().getString(R.string.clear_app_data) + getResources().getString(R.string.opera_ok_ln),
					context);
		} else {
			AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.clear_app_data)
					+ getResources().getString(R.string.opera_fail_ln), context);
		}

		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_detete_listen_file), context);
		ListnerFileFragment.deleteFileDataAuto(packageName, mMainActivity);
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_detete_point_folder), context);
		FileFragment.deletePointFile(context);
		AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_detete_browser_data), context);
		TaskManagerFragment.deleteBrowerData(context);

		// AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_disconnect_vpn),
		// context);
		// PPTPVPNInterface vpnconn = getconn(mMainActivity);
		// if (vpnconn == null) {
		// AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.vpn_disconnect)
		// + "\n"
		// + getResources().getString(R.string.opera_fail), context);
		// } else {
		// boolean disconnectVpn = disconnectVpn(vpnconn);
		// if (disconnectVpn)
		// AutoActivity.sendHandlerOnMain(getResources().getString(R.string.vpn_disconnect)
		// + getResources().getString(R.string.opera_ok_ln), mMainActivity);
		// AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_connect_vpn),
		// context);
		// VpnProfile vpnProfile = mLoader.getDefault();
		// boolean connectVpn = connectVpn(vpnconn, vpnProfile);
		// if (connectVpn) {
		// AutoActivity.sendHandlerOnMain(getResources().getString(R.string.vpn_connect)
		// + "\n"
		// + getResources().getString(R.string.opera_ok), context);
		// }
		// }
		getRandomData();
	}

	private boolean connectVpn(PPTPVPNInterface vpnconn, VpnProfile vpnProfile) {
		for (int i = 0; i < CONNECT_VPN_COUNT; i++) {
			AutoActivity.sendHandlerOnMain(
					getResources().getString(R.string.di) + (i + 1) + getResources().getString(R.string.ci),
					mMainActivity);
			boolean isVpnStart = vpnconn.startAuto(vpnProfile, mMainActivity);
			if (isVpnStart) {
				return true;
			}
			AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.vpn_connect) + "\n"
					+ getResources().getString(R.string.opera_fail), mMainActivity);
		}
		return false;
	}

	private boolean disconnectVpn(PPTPVPNInterface vpnconn) {
		for (int i = 0; i < DISCONNECT_VPN_COUNT; i++) {
			AutoActivity.sendHandlerOnMain(
					getResources().getString(R.string.di) + (i + 1) + getResources().getString(R.string.ci),
					mMainActivity);
			vpnconn.stopAuto(mMainActivity);
			boolean vpn_started = VpnManager.isStarted();
			if (vpn_started) {
				AutoActivity.sendHandlerRedOnMain(getResources().getString(R.string.vpn_disconnect)
						+ getResources().getString(R.string.opera_fail_ln), mMainActivity);
			} else {
				return true;
			}
		}
		return false;
	}

	ProfileLoader mLoader;

	private PPTPVPNInterface getconn(Context context) {
		PPTPVPNInterface pPTPVPN = null;
		for (int i = START_VPN_TIME; i > 0; i--) {
			pPTPVPN = PPTPVPNServiceConn.getInstance();
			if (pPTPVPN == null && i == START_VPN_TIME) {
				PPTPVPNService.startVpnService(context);
			} else if (pPTPVPN != null) {
				return pPTPVPN;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pPTPVPN;

	}

	public static final int DISCONNECT_NET_TIME = 15;
	public static final int CONNECT_NET_TIME = 30;
	public static final int START_VPN_TIME = 5;
	public static final int CONNECT_VPN_COUNT = 2;
	public static final int DISCONNECT_VPN_COUNT = 3;

	private void getRandomData() {
		mMainActivity.runOnUiThread(new Runnable() {
			public void run() {
				AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.oprera_random_data),
						mMainActivity);
				MyParamInterface.getParam();
			}
		});
	}

}
