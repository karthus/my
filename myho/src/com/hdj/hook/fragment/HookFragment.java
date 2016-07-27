package com.hdj.hook.fragment;

import com.google.gson.Gson;
import com.hdj.hook.MyApplication;
import com.hdj.hook.R;
import com.hdj.hook.R.string;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.activity.SelectAppActivity;
import com.hdj.hook.adapter.ParamsAdapter;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.mode.BasebandVersionUtil;
import com.hdj.hook.mode.DisplayMode;
import com.hdj.hook.mode.InfosMode;
import com.hdj.hook.mode.KernelVersionUtil;
import com.hdj.hook.mode.NetMode;
import com.hdj.hook.mode.ReceiverDataMode;
import com.hdj.hook.mode.SCellMode;
import com.hdj.hook.mode.SimInfosMode;
import com.hdj.hook.mode.TimeZooMode;
import com.hdj.hook.mode.WifiMode;
import com.hdj.hook.util.BluetoothUtil;
import com.hdj.hook.util.BuildUtil;
import com.hdj.hook.util.CPUMainFrequencyUtil;
import com.hdj.hook.util.CellUtil;
import com.hdj.hook.util.DisplayUtil;
import com.hdj.hook.util.GetNetTypeUtil;
import com.hdj.hook.util.GetPosUtil;
import com.hdj.hook.util.GetUtils;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.GsonUtil;
import com.hdj.hook.util.InnerIPUtil;
import com.hdj.hook.util.MemeoryUtil;
import com.hdj.hook.util.NetUtil;
import com.hdj.hook.util.ProviderUtil;
import com.hdj.hook.util.RootUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.TelephonyUtil;
import com.hdj.hook.util.TimeZoo;
import com.hdj.hook.util.ToastUtil;
import com.hdj.hook.util.WaiWangIPUtil;
import com.hdj.hook.util.WifiUtil;
import com.hdj.hook.vpn.ProfileLoader;
import com.mz.iplocation.constants.URLConstants;
import com.mz.iplocation.utils.CommonUtils;
import com.mz.iplocation.utils.DBHelper;
import com.mz.iplocation.utils.MyParamInterface;
import com.param.greendao.ParamEntity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HookFragment extends Fragment implements OnClickListener {
	private Activity mActivity;
	private ParamsAdapter mInfosAdapter;
	private InfosMode mInfosMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		initReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_hook, null);
		mInfosMode = new InfosMode();
		findViewById(view);
		getApp();
		initExtraData();
		dealData(TO_GET);
		totest();
		return view;
	}

	private String packageName;

	private void getApp() {
		packageName = SPrefUtil.getSettingStr(mActivity, GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
		if (!TextUtils.isEmpty(packageName)) {
			PackageManager pm = mActivity.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
				int versionCode = packageInfo.versionCode;
				String packageName2 = mActivity.getPackageName();
				PackageInfo packageInfo2 = pm.getPackageInfo(packageName2, PackageManager.GET_META_DATA);
				int versionCode2 = packageInfo2.versionCode;
				int eclair = Build.VERSION_CODES.ECLAIR;
				int ECLAIR_0_1 = Build.VERSION_CODES.ECLAIR_0_1;
				int ECLAIR_MR1 = Build.VERSION_CODES.ECLAIR_MR1;
				Log.v(GlobalConstant.MYTAG, "--versionCode--packageName--"+versionCode+"/"+packageName);
				Log.v(GlobalConstant.MYTAG, "--versionCode--packageName--"+versionCode2+"/"+packageName2);
				Log.v(GlobalConstant.MYTAG, "--eclair--"+eclair);
				Log.v(GlobalConstant.MYTAG, "--ECLAIR_0_1--"+ECLAIR_0_1);
				Log.v(GlobalConstant.MYTAG, "--ECLAIR_MR1--"+ECLAIR_MR1);
				
				View appInfoView = SelectAppActivity.getAppInfoView(packageInfo, mActivity.getPackageManager(),
						mActivity);
				fl_frame.removeAllViews();
				fl_frame.addView(appInfoView);
				hideSelectApp(true);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				hideSelectApp(false);
			}
			
		} else {
			hideSelectApp(false);
		}

	}

	private void hideSelectApp(boolean isHideSelect) {
		if (isHideSelect) {
			ll_app.setVisibility(View.VISIBLE);
			ll_select.setVisibility(View.GONE);
		} else {
			ll_app.setVisibility(View.GONE);
			ll_select.setVisibility(View.VISIBLE);
		}
	}

	private MyReceiver mDataReceiver;
	private SharedPreferences preferences;// 留存设置文件

	private void initExtraData() {
		MyParamInterface.init(mActivity);
		preferences = mActivity.getSharedPreferences(URLConstants.SEETING_KEEP_SP, Context.MODE_PRIVATE);
		if (MyParamInterface.isTommorrow()) {
			// 拷贝清空数据库；
			MyParamInterface.copy_CleanDB();
		}
		preferences.edit().putLong("time_end", CommonUtils.getDayEnd()).commit();
		mDataReceiver = new MyReceiver();
		// mActivity.registerReceiver(mReceiver, new
		// IntentFilter(GlobalConstant.ACTION_FILL_DATA));
		mActivity.registerReceiver(mDataReceiver, new IntentFilter(URLConstants.GET_PARAM_BROADCAST));
	}

	/**
	 * 接收获取参数广播
	 * 
	 * @author Administrator
	 *
	 */
	// class MyReceiver extends BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// mReceiverMode = getReceiverMode(mActivity);
	// if (mReceiverMode != null)
	// dealData(TO_RANDOM);
	// }
	// }
	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, Intent intent) {
			final String info = intent.getExtras().getString("param");
			Log.i("Recevier1", "接收到:" + info);
			new Thread(new Runnable() {
				public void run() {
					mActivity.runOnUiThread(new Runnable() {
						public void run() {
							mReceiverMode = getReceiverMode(mActivity, info);
							dealData(TO_RANDOM);
							MyApplication app = MyApplication.getInstance();
							boolean auto = app.isAuto();
							if (auto) {
								boolean putHook = SPrefUtil.putHookStr(mActivity, info, SPrefUtil.KEY_HOOK);
								// dealData(TO_SAVE);
								if (putHook) {
									dealData(TO_SAVE);
									liuChun(info, context);
									AutoActivity.sendHandler(getResources().getString(R.string.liu_cun_data));
									AutoActivity.sendHandlerBlueOnMain(getResources().getString(R.string.opera_end),context);
									AutoActivity.sendHandler("");
									Message msg = Message.obtain();
									msg.obj = context;
									msg.what = AutoActivity.FINISH;
									AutoActivity.mControlHandler.sendMessageDelayed(msg, 1000);
								}
							}

						}
					});
				}
			}).start();
		}
	}

	public void liuChun(String info, Context context) {
		Gson gson = new Gson();
		ParamEntity entity = gson.fromJson(info, ParamEntity.class);
		DBHelper dBManager = DBHelper.getInstance(context);
		// 插入数据库
		long count = dBManager.saveParam(entity);
		SharedPreferences preferences = mActivity.getSharedPreferences(URLConstants.SEETING_KEEP_SP,
				Context.MODE_PRIVATE);
		preferences.edit().putInt("now_run_index", dBManager.getItemCount() + 1).commit();
		Log.v(GlobalConstant.MYTAG, "--liu--cun--" + count);
	}

	LinearLayout ll_app, ll_select;
	FrameLayout fl_frame;
	Button btn_select_app;
	TextView tv_text;

	private void findViewById(View view) {
		Button tv_get = (Button) view.findViewById(R.id.tv_random);
		Button tv_save = (Button) view.findViewById(R.id.tv_save);
		Button tv_auto = (Button) view.findViewById(R.id.tv_auto);
		ListView lv_list = (ListView) view.findViewById(R.id.lv_list);
		tv_text = (TextView) view.findViewById(R.id.tv_text);
		fl_frame = (FrameLayout) view.findViewById(R.id.fl_frame);
		ll_app = (LinearLayout) view.findViewById(R.id.ll_app);
		ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
		btn_select_app = (Button) view.findViewById(R.id.btn_select_app);
		mInfosAdapter = new ParamsAdapter(HookFragment.this);
		lv_list.setAdapter(mInfosAdapter);
		tv_save.setOnClickListener(this);
		tv_get.setOnClickListener(this);
		tv_auto.setOnClickListener(this);
		btn_select_app.setOnClickListener(this);
		ll_app.setOnClickListener(this);
//		if (HookUtil.IS_HOOK == 1) {
//			tv_text.setVisibility(View.GONE);
//		} else {
//			tv_text.setText(mActivity.getResources().getString(R.string.sofe_no_effect));
//			tv_text.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public void onDestroy() {
		if (mDataReceiver != null)
			mActivity.unregisterReceiver(mDataReceiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_random:
			if (!RootUtil.isRoot()) {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.please_root));
				return;
			}
			MyApplication app = MyApplication.getInstance();
			app.setAuto(false);
			MyParamInterface.getParam();

			break;
		case R.id.tv_save:
			dealData(TO_SAVE);
			break;
		case R.id.tv_auto:
			if (!RootUtil.isRoot()) {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.please_root));
				return;
			}
			toStartAuto();
			break;
		case R.id.tv_start:
			System.exit(0);
			break;
		case R.id.ll_app:
		case R.id.btn_select_app:
			startActivity(new Intent(mActivity, SelectAppActivity.class));
			break;
		default:
			break;
		}

	}

	private void toStartAuto() {
		ProfileLoader loader = ProfileLoader.getInstance(mActivity);
		if (TextUtils.isEmpty(packageName)) {
			ToastUtil.show(mActivity, getResources().getString(R.string.please_select_appp));
			return;
		}
//		VpnProfile vpnProfile = loader.getDefault();
//		if (vpnProfile == null) {
//			ToastUtil.show(mActivity, getResources().getString(R.string.please_set_vpn_infos));
//			return;
//		}
		boolean modify_test = SPrefUtil.getSettingBoolean(mActivity, SPrefUtil.KEY_MODIFY_TEST);
		if (modify_test) {
			ToastUtil.show(mActivity, getResources().getString(R.string.test_mode_cannot_execute));
			return;
		}
		boolean iSHasNet = NetUtil.iSHasNet(mActivity);
		if (!iSHasNet) {
			ToastUtil.show(mActivity, getResources().getString(R.string.please_check_net));
			return;
		}
		Intent intent = new Intent(mActivity, AutoActivity.class);
		intent.putExtra(AutoActivity.IS_CLICK, true);
		startActivity(intent);
	}
	
	
	

	ListnerAppReceiver mListnerAppReceiver;

	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GlobalConstant.ACTION_LISTNER_APP);
		mListnerAppReceiver = new ListnerAppReceiver();
		mActivity.registerReceiver(mListnerAppReceiver, intentFilter);
	}

	class ListnerAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String flag = intent.getStringExtra(GlobalConstant.FLAG);
			if (GlobalConstant.FLAG_NEW_LISTNER_APP.equals(flag)) {
				Bundle bundle = intent.getExtras();
				AppInfosMode packageInfoMode = (AppInfosMode) bundle.getParcelable(GlobalConstant.KEY_SELECT_APP);
				View view = SelectAppActivity.getAppInfoView(packageInfoMode, mActivity.getPackageManager(), mActivity);
				fl_frame.removeAllViews();
				fl_frame.addView(view);
				final String new_packageName = packageInfoMode.getPackageInfo().packageName;
				packageName = new_packageName;
				hideSelectApp(true);
			} else if (GlobalConstant.FLAG_CLEAR_LISTNER_APP.equals(flag)) {
				fl_frame.removeAllViews();
				packageName = null;
				hideSelectApp(false);
			}

		}

	}

	public InfosMode getmInfosMode() {
		return mInfosMode;
	}

	public void setmInfosMode(InfosMode mInfosMode) {
		this.mInfosMode = mInfosMode;
	}

	private static final int TO_GET = 1;
	private static final int TO_RANDOM = 2;
	private static final int TO_SAVE = 3;

	private void dealData(int type) {
		switch (type) {
		case TO_SAVE:
			if (mReceiverMode != null) {
				Gson gson = new Gson();
				boolean putHook = SPrefUtil.putHookStr(mActivity, gson.toJson(mReceiverMode), SPrefUtil.KEY_HOOK);
			} else {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.no_data_no_save));
			}
			return;

		default:
			break;
		}


		dealLatitudeLongitude(type);
		// float android.location.Location.getAccuracy()

		dealCell(type);
		dealInnerIP(type);
		dealRemoteAddr(type);
		dealAndroidID(type);
		dealUserAgent(type);

		dealSim(type);
		// 没有的
		// String android.telephony.TelephonyManager.getNetworkOperator()
		// String
		// com.hdj.hook.util.TelephonyUtil.getSimOperatorName(TelephonyManager
		// tm)
		// simOperator_id 是什么?
		// String android.telephony.TelephonyManager.getSimCountryIso()

		dealTimeZoo(type);
		// String java.util.TimeZone.getID()
		// String java.util.Locale.getCountry()
		// String java.util.Locale.getLanguage()

		dealWifi(type);
		// int android.net.wifi.WifiInfo.getRssi()
		// int android.net.wifi.WifiInfo.getLinkSpeed()

		dealNetwork(type);
		// String android.net.NetworkInfo.getTypeName()
		// int android.net.NetworkInfo.getSubtype()

		dealBuildRelease(type);

		dealBuildBrand(type);

		dealBuildMold(type);

		dealBuildBoard(type);
		// String android.os.Build.BOARD

		dealBuildProduct(type);

		dealBuildCpuAbi(type);
		// String android.os.Build.CPU_ABI

		dealBuildCpuAbi2(type);

		dealBuildID(type);

		dealBuildDisplay(type);

		dealBuildFingerprint(type);

		dealBuildCodeName(type);
		// String android.os.Build.VERSION.CODENAME

		dealBuildManufacturer(type);

		dealBuildHost(type);

		dealBuildType(type);
		// String android.os.Build.TYPE

		dealBuildSdkInt(type);

		dealBuildSdk(type);

		dealBuildTag(type);
		// String android.os.Build.TAGS

		dealBuildDevice(type);

		dealBuildSerial(type);

		dealBuildBootloader(type);
		// mInfosMode.setBuild_bootloader(BuildUtil.getBootloader());

		dealBuildIncremental(type);
		// String android.os.Build.VERSION.INCREMENTAL

		dealRadioVersion(type);

		dealDisplay(type);
		// float android.util.DisplayMetrics.xdpi
		// float android.util.DisplayMetrics.ydpi
		// float android.util.DisplayMetrics.density
		// float android.util.DisplayMetrics.scaledDensity
		// int android.view.Display.getHeight()
		// int android.view.Display.getWidth()

		dealBlue(type);

		dealCpuMainFrequencyUtil(type);
		// 无

		dealKernelVersion(type);
		// 无

		dealBasebandVersion(type);
		// 无

		dealMemeory(type);
		// 无

		noticeDataChange();
	}


	// imei,
	private void dealSim(int type) {
		SimInfosMode simInfosMode;
		String imei;
		Integer int_phoneType;
		String phoneNum;
		String sim_operator;
		int int_sim_state;
		String netWorkoperatorName;
		switch (type) {
		case TO_GET:
			TelephonyUtil.get(HookFragment.this);
			break;
		case TO_RANDOM:
			simInfosMode = mInfosMode.getSimInfosMode();

			String phoneType = mReceiverMode.getPhoneType();
			int_phoneType = Integer.valueOf(phoneType);
			simInfosMode.setPhoneType(int_phoneType);

			String simSerialNum = mReceiverMode.getSimSerialNum();
			simInfosMode.setSim_serial(simSerialNum);

			phoneNum = mReceiverMode.getPhoneNum();
			simInfosMode.setPhone_num(phoneNum);

			imei = mReceiverMode.getImei();
			simInfosMode.setImei(imei);

			sim_operator = mReceiverMode.getSimOperator_id();
			simInfosMode.setSim_operator(sim_operator);

			String simState = mReceiverMode.getSimState();
			int_sim_state = Integer.valueOf(simState);
			simInfosMode.setSim_state(int_sim_state);

			netWorkoperatorName = mReceiverMode.getNetWorkoperatorName();
			simInfosMode.setNetwork_operator_name(netWorkoperatorName);

			mInfosMode.setSimInfosMode(simInfosMode);

			break;
		case TO_SAVE:
			simInfosMode = mInfosMode.getSimInfosMode();
			imei = simInfosMode.getImei();
			simSerialNum = simInfosMode.getSim_serial();
			int_phoneType = simInfosMode.getPhoneType();
			phoneNum = simInfosMode.getPhone_num();
			sim_operator = simInfosMode.getSim_operator();
			int_sim_state = simInfosMode.getSim_state();
			netWorkoperatorName = simInfosMode.getNetwork_operator_name();
			SPrefUtil.putHookStr(mActivity, imei, GetUtils.IMEI);
			SPrefUtil.putHookStr(mActivity, simSerialNum, GetUtils.SIM_SERIAL);
			SPrefUtil.putHookInt(mActivity, int_phoneType, GetUtils.PHONE_TYPE);
			SPrefUtil.putHookStr(mActivity, phoneNum, GetUtils.PHONE_NUM);
			SPrefUtil.putHookStr(mActivity, sim_operator, GetUtils.SIM_OPERATOR);
			SPrefUtil.putHookInt(mActivity, int_sim_state, GetUtils.SIM_STATE);
			SPrefUtil.putHookStr(mActivity, netWorkoperatorName, GetUtils.NETWORK_OPERATOR_NAME);
			break;
		default:
			break;
		}
	}

	private void dealMemeory(int type) {
		switch (type) {
		case TO_GET:
			MemeoryUtil.neiSdCard(HookFragment.this);
			MemeoryUtil.neiMemery(HookFragment.this);
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBasebandVersion(int type) {
		switch (type) {
		case TO_GET:
			String string = BasebandVersionUtil.get();
			mInfosMode.setBase_band_version(string);
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealKernelVersion(int type) {
		switch (type) {
		case TO_GET:
			String string = KernelVersionUtil.get();
			mInfosMode.setKernel_version(string);
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealCpuMainFrequencyUtil(int type) {
		switch (type) {
		case TO_GET:
			String string = CPUMainFrequencyUtil.get();
			mInfosMode.setCpu_main_frequency(string);
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBlue(int type) {
		switch (type) {
		case TO_GET:
			String btMac = BluetoothUtil.getBtMac();
			mInfosMode.setBluetooth_address(btMac);
			break;
		case TO_RANDOM:
			mInfosMode.setBluetooth_address(mReceiverMode.getBluetoothMac());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBluetooth_address(), GetUtils.BLUETOOTH_ADDRESS);
			break;
		default:
			break;
		}
	}

	private float toIntFload(float f) {
		Integer b = (int) ((f + 5f) / 10) * 10;
		float c = Float.parseFloat(String.valueOf(b) + ".0");
		return c;
	}

	private void dealDisplay(int type) {
		DisplayMode displayMode;
		Integer int_heightPixels;
		Integer int_widthPixels;
		float xdpi;
		float ydpi;
		switch (type) {
		case TO_GET:
			DisplayUtil.get(HookFragment.this);
			DisplayUtil.get2(HookFragment.this);
			break;
		case TO_RANDOM:
			displayMode = mInfosMode.getDisplayMode();

			String density = mReceiverMode.getDensity();
			displayMode.setDensityDpi(Integer.valueOf(density));

			String heightPixels = mReceiverMode.getHeightPixels();
			int_heightPixels = Integer.valueOf(heightPixels);
			displayMode.setHeightPixels(int_heightPixels);
			displayMode.setHeight(int_heightPixels);

			String widthPixels = mReceiverMode.getWidthPixels();
			int_widthPixels = Integer.valueOf(widthPixels);
			displayMode.setWidthPixels(int_widthPixels);
			displayMode.setWidth(int_widthPixels);

			xdpi = displayMode.getXdpi();
			displayMode.setXdpi(toIntFload(xdpi));

			ydpi = displayMode.getYdpi();
			displayMode.setYdpi(toIntFload(ydpi));

			displayMode.setDiplaySize("Point(" + widthPixels + ", " + heightPixels + ")");

			mInfosMode.setDisplayMode(displayMode);
			mInfosMode.setDisplayMode2(displayMode);
			break;
		case TO_SAVE:
			displayMode = mInfosMode.getDisplayMode();
			int densityDpi = displayMode.getDensityDpi();
			int_heightPixels = displayMode.getHeightPixels();
			int_widthPixels = displayMode.getWidthPixels();

			SPrefUtil.putHookFloat(mActivity, toIntFload(displayMode.getXdpi()), GetUtils.DISPLAY_XDPI);
			SPrefUtil.putHookFloat(mActivity, toIntFload(displayMode.getYdpi()), GetUtils.DISPLAY_YDPI);
			SPrefUtil.putHookInt(mActivity, densityDpi, GetUtils.DENSITY_DPI);
			SPrefUtil.putHookInt(mActivity, int_heightPixels, GetUtils.DISPLAY_HEIGHTPIXELS);
			SPrefUtil.putHookInt(mActivity, int_widthPixels, GetUtils.DISPLAY_WIDTHPIXELS);

			break;
		default:
			break;
		}
	}

	private void dealInnerIP(int type) {
		String innerIp;
		switch (type) {
		case TO_GET:
			innerIp = InnerIPUtil.getLocalIP(mActivity);
			mInfosMode.setInnerIp(innerIp);
			break;
		case TO_RANDOM:
			innerIp = mReceiverMode.getInnerIP();
			mInfosMode.setInnerIp(innerIp);
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mReceiverMode.getInnerIP(), GetUtils.INNER_IP);
			break;
		default:
			break;
		}
	}

	WaiWangIPUtil waiWangIPUtil;

	private void dealRemoteAddr(int type) {
		switch (type) {
		case TO_GET:
			if (waiWangIPUtil == null)
				waiWangIPUtil = new WaiWangIPUtil(HookFragment.this);
			waiWangIPUtil.get();
			break;
		case TO_RANDOM:
			// String ip = mReceiverMode.getIp();
			// mInfosMode.setRemote_addr(ip);
			break;
		case TO_SAVE:

			break;
		default:
			break;
		}
	}

	private void dealTimeZoo(int type) {
		switch (type) {
		case TO_GET:
			TimeZooMode timeZooMode = TimeZoo.get();
			mInfosMode.setTimeZooMode(timeZooMode);
			break;
		case TO_RANDOM:

			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void dealUserAgent(int type) {
		String userAgent = null;
		int sdk_INT = BuildUtil.getSDK_INT();
		switch (type) {
		case TO_GET:
			WebView webView = new WebView(mActivity);
			userAgent = webView.getSettings().getUserAgentString();
			mInfosMode.setUser_agent(userAgent);
			if (sdk_INT > 16) {
				userAgent = WebSettings.getDefaultUserAgent(mActivity);// API-17
				mInfosMode.setUser_agent2(userAgent);
			}
			break;
		case TO_RANDOM:
			userAgent = mReceiverMode.getUserAgent();
			mInfosMode.setUser_agent(userAgent);
			if (sdk_INT > 16) {
				mInfosMode.setUser_agent2(userAgent);
			}
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getUser_agent(), GetUtils.USER_AGENT);
			break;
		default:
			break;
		}
	}

	private void dealAndroidID(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setAndroid_id(ProviderUtil.getAndroidID(getActivity()));
			mInfosMode.setAndroid_id2(ProviderUtil.getAndroidID2(getActivity()));
			break;
		case TO_RANDOM:
			String android_id = mReceiverMode.getAndroid_id();
			mInfosMode.setAndroid_id(android_id);
			mInfosMode.setAndroid_id2(android_id);
			break;
		case TO_SAVE:
			// GetUtils.putStr(mInfosMode.getAndroid_id(), GetUtils.ANDROID_ID);
			SPrefUtil.putHookStr(mActivity, mInfosMode.getAndroid_id(), GetUtils.ANDROID_ID);
			break;
		default:
			break;
		}
	}

	private void dealWifi(int type) {
		WifiMode wifi2;
		String bssid;
		String ssid;
		String macAddress;
		String wifilist;
		switch (type) {
		case TO_GET:
			WifiMode wifiMode = WifiUtil.get(getActivity());
			mInfosMode.setWifi(wifiMode);
			break;
		case TO_RANDOM:
			wifi2 = mInfosMode.getWifi();

			macAddress = mReceiverMode.getMac();
			wifi2.setMacAddress(macAddress);

			bssid = mReceiverMode.getWifiMac();
			wifi2.setBssid(bssid);

			ssid = mReceiverMode.getWifiName();
			wifi2.setSsid(ssid);

			wifilist = mReceiverMode.getWifilist();

			wifi2.setWifilist(wifilist);

			mInfosMode.setWifi(wifi2);
			break;
		case TO_SAVE:
			WifiMode wifi = mInfosMode.getWifi();
			bssid = wifi.getBssid();
			ssid = wifi.getSsid();
			wifilist = wifi.getWifilist();
			macAddress = wifi.getMacAddress();
			SPrefUtil.putHookStr(mActivity, macAddress, GetUtils.WIFI_MACADDRESS);
			SPrefUtil.putHookStr(mActivity, bssid, GetUtils.WIFI_BSSID);
			SPrefUtil.putHookStr(mActivity, ssid, GetUtils.WIFI_SSID);
			SPrefUtil.putHookStr(mActivity, wifilist, GetUtils.WIFI_LIST);

			break;
		default:
			break;
		}
	}

	private void dealRadioVersion(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_radio_version(BuildUtil.getRadioVersion());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_radio_version(mReceiverMode.getGetRadioVersion());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_radio_version(), GetUtils.BUILD_RADIO_VERSION);
			break;
		default:
			break;
		}
	}

	private void dealBuildIncremental(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_incremental(BuildUtil.getIncremental());
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildBootloader(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_bootloader(BuildUtil.getBootloader());
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildSerial(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_serial(BuildUtil.getSerial());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_serial(mReceiverMode.getBuild_serial());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_serial(), GetUtils.BUILD_SERIAL);
			break;
		default:
			break;
		}
	}

	private void dealBuildDevice(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_device(BuildUtil.getDevice());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_device(mReceiverMode.getDevice());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_device(), GetUtils.BUILD_DEVICE);
			break;
		default:
			break;
		}
	}

	private void dealBuildTag(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_tag(BuildUtil.getTags());
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildSdk(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_sdk(BuildUtil.getSDK());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_sdk(mReceiverMode.getSdk());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_sdk(), GetUtils.BUILD_SDK);
			break;
		default:
			break;
		}
	}

	private void dealBuildSdkInt(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_sdk_int(BuildUtil.getSDK_INT());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_sdk_int(Integer.valueOf(mReceiverMode.getSdk()));
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildType(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_type(BuildUtil.getType());
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildHost(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_host(BuildUtil.getHost());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_host(mReceiverMode.getBuild_host());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_host(), GetUtils.BUILD_HOST);
			break;
		default:
			break;
		}
	}

	private void dealBuildManufacturer(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_manufacturer(BuildUtil.getManufacturer());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_manufacturer(mReceiverMode.getMANUFACTURER());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_manufacturer(), GetUtils.BUILD_MANUFACTURER);
			break;
		default:
			break;
		}
	}

	private void dealBuildCodeName(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_code_name(BuildUtil.getCodename());
			break;
		case TO_RANDOM:
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildFingerprint(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_fingerprint(BuildUtil.getFingerprint());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_fingerprint(mReceiverMode.getFingerprint());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_fingerprint(), GetUtils.BUILD_FINGERPRINT);
			break;
		default:
			break;
		}
	}

	private void dealBuildDisplay(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_display(BuildUtil.getDisplay());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_display(mReceiverMode.getBuild_display());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_display(), GetUtils.BUILD_DISPLAY);
			break;
		default:
			break;
		}
	}

	private void dealBuildID(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_id(BuildUtil.getID());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_id(mReceiverMode.getBuild_id());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_id(), GetUtils.BUILD_ID);
			break;
		default:
			break;
		}
	}

	private void dealBuildCpuAbi(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_cup_abi(BuildUtil.getCpuAbi());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_cup_abi(mReceiverMode.getSetCpuName());
			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildCpuAbi2(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_cup_abi2(BuildUtil.getCpuAbi2());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_cup_abi2(mReceiverMode.getSetCpuName());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_cup_abi2(), GetUtils.BUILD_CPU_ABI2);
			break;
		default:
			break;
		}
	}

	private void dealBuildProduct(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_product(BuildUtil.getProduct());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_product(mReceiverMode.getProduct());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_product(), GetUtils.BUILD_PRODUCT);
			break;
		default:
			break;
		}
	}

	private void dealBuildBoard(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_board(BuildUtil.getBoard());
			break;
		case TO_RANDOM:

			break;
		case TO_SAVE:
			break;
		default:
			break;
		}
	}

	private void dealBuildMold(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_mold(BuildUtil.getMold());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_mold(mReceiverMode.getModel());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_mold(), GetUtils.BUILD_MOLD);
			break;
		default:
			break;
		}
	}

	private void dealBuildBrand(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_brand(BuildUtil.getBrand());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_brand(mReceiverMode.getBrand());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_brand(), GetUtils.BUILD_BRAND);
			break;
		default:
			break;
		}
	}

	private void dealBuildRelease(int type) {
		switch (type) {
		case TO_GET:
			mInfosMode.setBuild_release(BuildUtil.getRelease());
			break;
		case TO_RANDOM:
			mInfosMode.setBuild_release(mReceiverMode.getRelease());
			break;
		case TO_SAVE:
			SPrefUtil.putHookStr(mActivity, mInfosMode.getBuild_release(), GetUtils.BUILD_RELEASE);
			break;
		default:
			break;
		}

	}

	ReceiverDataMode mReceiverMode;

	private void dealCell(int type) {
		switch (type) {
		case TO_GET:
			CellUtil.get(HookFragment.this);
			break;
		case TO_RANDOM:
			SCellMode sCell = new SCellMode();
			String lac = mReceiverMode.getLac();
			String cid = mReceiverMode.getCid();
			sCell.setLac(Integer.valueOf(lac));
			sCell.setCid(Integer.valueOf(cid));
			mInfosMode.setScell(sCell);
			break;
		case TO_SAVE:
			SCellMode scell2 = mInfosMode.getScell();
			SPrefUtil.putHookInt(mActivity, scell2.getCid(), GetUtils.CELL_ID);
			SPrefUtil.putHookInt(mActivity, scell2.getLac(), GetUtils.CELL_LAC);
			break;
		default:
			break;
		}
	}

	private void dealNetwork(int type) {
		NetMode netMode2;
		Integer int_netWorkType;
		String str ;
		switch (type) {
		case TO_GET:
			// String android.net.NetworkInfo.getTypeName()
			// int android.net.NetworkInfo.getSubtype()

			NetMode netMode = new NetMode();
			netMode.setNetType(GetNetTypeUtil.get(getActivity()));
			
//			netMode.setActiveNetTypeName(NetUtil.getActiveNetTypeName(getActivity()));
//			netMode.setActiveNetType(NetUtil.getActiveNetType(getActivity()));
//			netMode.setActiveNetSubtype(NetUtil.getActiveNetSubtype(getActivity()));
//			netMode.setGetActiveNetSubtypeName(NetUtil.getActiveNetSubtypeName(getActivity()));
			 str = NetUtil.wifi_mobile_connect_state(mActivity);
			NetMode netMode3 = NetUtil.getNetMode(getActivity());
			netMode3.setNetType(GetNetTypeUtil.get(getActivity()));
			netMode3.setConnect_state(str);
			mInfosMode.setNetMode(netMode3);
//			mInfosMode.setNetMode2(NetUtil.getNetMode2(getActivity()));
			break;
		case TO_RANDOM:
			netMode2 = mInfosMode.getNetMode();

			String netWorkType = mReceiverMode.getNetWorkType();
			int_netWorkType = Integer.valueOf(netWorkType);
			netMode2.setNetType(int_netWorkType);
			
			Integer isWifi = mReceiverMode.getIsWifi();
			switch (isWifi) {
			case GlobalConstant.NET_TYPE_MOBILE:
				str = "wifi:"+State.DISCONNECTED.toString()+"      "+"mobile:"+State.CONNECTED.toString();
				break;
			case GlobalConstant.NET_TYPE_WIFI:
				str = "wifi:"+State.CONNECTED.toString()+"      "+"mobile:"+State.DISCONNECTED.toString();
				break;

			default:
				str ="";
				break;
			}
			
			netMode2.setConnect_state(str);
			
			netMode2.setGetActiveNetSubtypeName(mReceiverMode.getNetSubTypeName());
			netMode2.setActiveNetSubtype(Integer.valueOf(mReceiverMode.getNetSubType()));
			netMode2.setActiveNetTypeName(mReceiverMode.getNetTypeName());
			netMode2.setNetType(mReceiverMode.getIsWifi());
			
			mInfosMode.setNetMode(netMode2);
//			mInfosMode.setNetMode2(netMode2);
			

			break;
		case TO_SAVE:
			netMode2 = mInfosMode.getNetMode();
			int_netWorkType = netMode2.getNetType();
			// GetUtils.putInt(netMode3.getNetType(), GetUtils.NET_TYPE);
			// GetUtils.putStr(netMode3.getActiveNetTypeName(),
			// GetUtils.ACTIVE_NET_TYPE_NAME);
			// GetUtils.putInt(netMode3.getActiveNetType(),
			// GetUtils.ACTIVE_NET_TYPE);
			// GetUtils.putInt(netMode3.getActiveNetSubtype(),
			// GetUtils.ACTIVE_NET_SUB_TYPE);
			SPrefUtil.putHookInt(mActivity, int_netWorkType, GetUtils.NET_TYPE);
			break;
		default:
			break;
		}
	}
	
	public void totest() {
		try {

			ConnectivityManager localConnectivityManager = (ConnectivityManager)mActivity.getSystemService("connectivity");

			if (localConnectivityManager == null) {
				return ;
			}
			NetworkInfo localNetworkInfo1 = localConnectivityManager
					.getNetworkInfo(1);
			if (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
				Log.v(GlobalConstant.MYTAG,"---string-11--"+localNetworkInfo1.getSubtypeName());
				return ;
			}
			localNetworkInfo1 = localConnectivityManager
					.getNetworkInfo(0);
			if (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
				Log.v(GlobalConstant.MYTAG,"---string-00--"+localNetworkInfo1.getSubtypeName());
				return ;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}

	}


	GetPosUtil getPosUtil;

	private void dealLatitudeLongitude(int type) {
		switch (type) {
		case TO_GET:
			if (getPosUtil == null) {
				getPosUtil = new GetPosUtil(HookFragment.this);
			}
			getPosUtil.getJW();
			break;
		case TO_RANDOM:
			String longitude = mReceiverMode.getLongitude();
			String latitude = mReceiverMode.getLatitude();
			mInfosMode.setLatitude(Double.parseDouble(latitude));
			mInfosMode.setLongitude(Double.parseDouble(longitude));

			break;
		case TO_SAVE:
			// GetUtils.putDouble(mInfosMode.getLatitude(), GetUtils.LATITUDE);
			// GetUtils.putDouble(mInfosMode.getLongitude(),
			// GetUtils.LONGITUDE);
			SPrefUtil.putHookStr(mActivity, String.valueOf(mInfosMode.getLatitude()), GetUtils.LATITUDE);
			SPrefUtil.putHookStr(mActivity, String.valueOf(mInfosMode.getLongitude()), GetUtils.LONGITUDE);

			break;
		default:
			break;
		}
	}

	public void noticeDataChange() {
		mInfosAdapter.notifyDataSetChanged();
	}

	public static ReceiverDataMode getReceiverMode(Context context) {
		String str = SPrefUtil.getHookStr(context, SPrefUtil.KEY_HOOK);
		ReceiverDataMode receiverDataMode = null;
		if (!TextUtils.isEmpty(str)) {
			Gson gson = GsonUtil.getInstance();
			receiverDataMode = gson.fromJson(str, ReceiverDataMode.class);
		}
		return receiverDataMode;
	}

	public static ReceiverDataMode getReceiverMode(Context context, String str) {
		ReceiverDataMode receiverDataMode = null;
		if (!TextUtils.isEmpty(str)) {
			Gson gson = GsonUtil.getInstance();
			receiverDataMode = gson.fromJson(str, ReceiverDataMode.class);
		}
		return receiverDataMode;
	}

}
