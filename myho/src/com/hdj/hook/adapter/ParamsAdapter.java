package com.hdj.hook.adapter;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdj.hook.R;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.InfosMode;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.NetUtil;
import com.hdj.hook.util.ProcessUtil;
import com.hdj.hook.util.SensorUtil;
import com.hdj.hook.util.StringUtil;
import com.hdj.hook.util.TelephonyUtil;
import com.hdj.hook.util.WifiUtil;
import com.hp.hpl.sparta.Text;
import com.mz.iplocation.Mode.WifiMode;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ParamsAdapter extends BaseAdapter {

	HookFragment mainActivity;
	public boolean isColorTag = false;
	private InfosMode mInfosMode;
	private int mColorGreen01, mColorBlue01;

	public ParamsAdapter(HookFragment mainActivity) {
		super();
		this.mainActivity = mainActivity;
		mInfosMode = mainActivity.getmInfosMode();
		initColor();
	}

	private void initColor() {
		mColorGreen01 = mainActivity.getResources().getColor(R.color.green01);
		mColorBlue01 = mainActivity.getResources().getColor(R.color.blue01);
	}

	@Override
	public int getCount() {
		return TOTAL_COUNT;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public boolean isColorTag() {
		return isColorTag;
	}

	public void setColorTag(boolean isColorTag) {
		this.isColorTag = isColorTag;
	}

	private static final int POS_ADD = 0;
	private static final int POS_ADD_COUNT = 10;
	private static final int SIM_ADD = POS_ADD_COUNT + POS_ADD;
	private static final int SIM_ADD_COUNT = 11;
	private static final int WIFI_ADD = SIM_ADD_COUNT + SIM_ADD;
	private static final int WIFI_ADD_COUNT = 8;
	private static final int NET_ADD = WIFI_ADD_COUNT + WIFI_ADD;
	private static final int NET_ADD_COUNT = 5;
	private static final int BUILD_ADD = NET_ADD_COUNT + NET_ADD;
	private static final int BUILD_ADD_COUNT = 22;
	private static final int TIME_ZOO_ADD = BUILD_ADD_COUNT + BUILD_ADD;
	private static final int TIME_ZOO_ADD_COUNT = 3;
	private static final int DISPLAY_ADD = TIME_ZOO_ADD_COUNT + TIME_ZOO_ADD;
	private static final int DISPLAY_ADD_COUNT = 17;
	private static final int OTHER01_ADD = DISPLAY_ADD_COUNT + DISPLAY_ADD;
	private static final int OTHER01_ADD_COUNT = 8;
	private static final int TOTAL_COUNT = OTHER01_ADD_COUNT + OTHER01_ADD + 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mainActivity.getActivity(), R.layout.adapter_params, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.et_params = (EditText) convertView.findViewById(R.id.et_params);
			holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_explain.setVisibility(View.GONE);
		holder.tv_name.setTextColor(mColorBlue01);
		holder.et_params.setVisibility(View.VISIBLE);
		holder.tv_explain.setTextColor(Color.GRAY);
		switch (position) {
		case 0 + POS_ADD:
			setLatitude(holder.tv_name, holder.et_params, position);
			break;
		case 1 + POS_ADD:
			setLongitude(holder.tv_name, holder.et_params, position);
			break;
		case 2 + POS_ADD:
			setAccuracy(holder.tv_name, holder.et_params, position);
			break;
		case 3 + POS_ADD:
			setCellID(holder.tv_name, holder.et_params, position);
			break;
		case 4 + POS_ADD:
			setCellLac(holder.tv_name, holder.et_params, position);
			break;
		case 5 + POS_ADD:
			setInnerIP(holder.tv_name, holder.et_params, position);
			break;
		case 6 + POS_ADD:
			setRemoteAddr(holder.tv_name, holder.et_params, position);
			break;
		case 7 + POS_ADD:
			setAndroidID(holder.tv_name, holder.et_params, position);
			break;
		case 8 + POS_ADD:
			setAndroidID2(holder.tv_name, holder.et_params, position);
			break;
		case 9 + POS_ADD:
			setUserAgent(holder.tv_name, holder.et_params, position);
			break;
		case 10 + POS_ADD:
			setUserAgent2(holder.tv_name, holder.et_params, position);
			break;
		case 1 + SIM_ADD:
			setIMEI(holder.tv_name, holder.et_params, position);
			break;
		case 2 + SIM_ADD:
			sethas_icc_card(holder.tv_name, holder.et_params, position);
			break;
		case 3 + SIM_ADD:
			setSimSerial(holder.tv_name, holder.et_params, position);
			break;
		case 4 + SIM_ADD:
			setPhoneNum(holder.tv_name, holder.et_params, position);
			break;
		case 5 + SIM_ADD:
			setPhoneType(holder.tv_name, holder.et_params, position);
			break;
		case 6 + SIM_ADD:
			setOperator(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 7 + SIM_ADD:
			setNetworkOperatorName(holder.tv_name, holder.et_params, position);
			break;
		case 8 + SIM_ADD:
			setSimOperatorName(holder.tv_name, holder.et_params, position);
			break;
		case 9 + SIM_ADD:
			setSimState(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 10 + SIM_ADD:
			setSimOperator(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 11 + SIM_ADD:
			setSimCountryIso(holder.tv_name, holder.et_params, position);
			break;
		case 1 + WIFI_ADD:
			setIsWifiEnabled(holder.tv_name, holder.et_params, position);
			break;
		case 2 + WIFI_ADD:
			setWifiIsconnect(holder.tv_name, holder.et_params, position);
			break;
		case 3 + WIFI_ADD:
			setWifiBssid(holder.tv_name, holder.et_params, position);
			break;
		case 4 + WIFI_ADD:
			setWifiMacAddress(holder.tv_name, holder.et_params, position);
			break;
		case 5 + WIFI_ADD:
			setWifiSsid(holder.tv_name, holder.et_params, position);
			break;
		case 6 + WIFI_ADD:
			setWifiLinkSpeed(holder.tv_name, holder.et_params, position);
			break;
		case 7 + WIFI_ADD:
			setWifiRssi(holder.tv_name, holder.et_params, position);
			break;
		case 8 + WIFI_ADD:
			setWifiList(holder.tv_name, holder.et_params, position);
			break;
		case 1 + NET_ADD:
			setNetType(holder.tv_name, holder.et_params, position);
			break;
		case 2 + NET_ADD:
			setMobieConnect(holder.tv_name, holder.et_params, position);
			break;
		case 3 + NET_ADD:
			setIsHasAvailableNet(holder.tv_name, holder.et_params, position);
			break;
		case 4 + NET_ADD:
			setActiveNetTypeName(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 5 + NET_ADD:
			setActiveNetState(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 1 + BUILD_ADD:
			setBuildRelease(holder.tv_name, holder.et_params, position);
			break;
		case 2 + BUILD_ADD:
			setBuildBrand(holder.tv_name, holder.et_params, position);
			break;
		case 3 + BUILD_ADD:
			setBuildMold2(holder.tv_name, holder.et_params, position);
			break;
		case 4 + BUILD_ADD:
			setBuildBoard2(holder.tv_name, holder.et_params, position);
			break;
		case 5 + BUILD_ADD:
			setBuildProduct2(holder.tv_name, holder.et_params, position);
			break;
		case 6 + BUILD_ADD:
			setBuildCpuAbi(holder.tv_name, holder.et_params, position);
			break;
		case 7 + BUILD_ADD:
			setBuildCpuAbi2(holder.tv_name, holder.et_params, position);
			break;
		case 8 + BUILD_ADD:
			setBuildID(holder.tv_name, holder.et_params, position);
			break;
		case 9 + BUILD_ADD:
			setBuildDisplay(holder.tv_name, holder.et_params, position);
			break;
		case 10 + BUILD_ADD:
			setBuildFingerprint(holder.tv_name, holder.et_params, position);
			break;
		case 11 + BUILD_ADD:
			setBuildCodeName(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 12 + BUILD_ADD:
			setManufacturer(holder.tv_name, holder.et_params, position);
			break;
		case 13 + BUILD_ADD:
			setBuildHost(holder.tv_name, holder.et_params, position);
			break;
		case 14 + BUILD_ADD:
			setBuildType(holder.tv_name, holder.et_params, position);
			break;
		case 15 + BUILD_ADD:
			setBuildSdk(holder.tv_name, holder.et_params, position);
			break;
		 case 16+ BUILD_ADD:
		 setBuildSdkInt(holder.tv_name, holder.et_params, position);
		 break;
	
		case 17 + BUILD_ADD:
			setBuildTag(holder.tv_name, holder.et_params, position);
			break;
		case 18 + BUILD_ADD:
			setBuildDevice(holder.tv_name, holder.et_params, position);
			break;
		case 19 + BUILD_ADD:
			setBuildSerial(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		case 20 + BUILD_ADD:
			setBuildBootloader(holder.tv_name, holder.et_params, position);
			break;
		case 21 + BUILD_ADD:
			setBuildIncremental(holder.tv_name, holder.et_params, position);
			break;
		case 22 + BUILD_ADD:
			setBuildRadioVersion(holder.tv_name, holder.et_params, position);
			break;
		case 1 + TIME_ZOO_ADD:
			setTimeZooId(holder.tv_name, holder.et_params, position);
			break;
		case 2 + TIME_ZOO_ADD:
			setTimeZooLanguage(holder.tv_name, holder.et_params, position);
			break;
		case 3 + TIME_ZOO_ADD:
			setTimeZooCountry(holder.tv_name, holder.et_params, position);
			break;
		case 1 + DISPLAY_ADD:
			setXdpi(holder.tv_name, holder.et_params, position);
			break;
		case 2 + DISPLAY_ADD:
			setYdpi(holder.tv_name, holder.et_params, position);
			break;
		case 3 + DISPLAY_ADD:
			setDensity(holder.tv_name, holder.et_params, position);
			break;
		case 4 + DISPLAY_ADD:
			setDensityDpi(holder.tv_name, holder.et_params, position);
			break;
		case 5 + DISPLAY_ADD:
			setHeightPixels(holder.tv_name, holder.et_params, position);
			break;
		case 6 + DISPLAY_ADD:
			setWidthPixels(holder.tv_name, holder.et_params, position);
			break;
		case 7 + DISPLAY_ADD:
			setScaledDensity(holder.tv_name, holder.et_params, position);
			break;
		case 8 + DISPLAY_ADD:
			setHeight(holder.tv_name, holder.et_params, position);
			break;
		case 9 + DISPLAY_ADD:
			setWidth(holder.tv_name, holder.et_params, position);
			break;
		case 10 + DISPLAY_ADD:
			setDiplaySize(holder.tv_name, holder.et_params, position);
			break;
		case 11 + DISPLAY_ADD:
			setXdpi2(holder.tv_name, holder.et_params, position);
			break;
		case 12 + DISPLAY_ADD:
			setYdpi2(holder.tv_name, holder.et_params, position);
			break;
		case 13 + DISPLAY_ADD:
			setDensity2(holder.tv_name, holder.et_params, position);
			break;
		case 14 + DISPLAY_ADD:
			setDensityDpi2(holder.tv_name, holder.et_params, position);
			break;
		case 15 + DISPLAY_ADD:
			setHeightPixels2(holder.tv_name, holder.et_params, position);
			break;
		case 16 + DISPLAY_ADD:
			setWidthPixels2(holder.tv_name, holder.et_params, position);
			break;
		case 17 + DISPLAY_ADD:
			setScaledDensity2(holder.tv_name, holder.et_params, position);
			break;
		case 1 + OTHER01_ADD:
			setBluetoothAddr(holder.tv_name, holder.et_params, position);
			break;
		case 2 + OTHER01_ADD:
			setCpuMainFrequency(holder.tv_name, holder.et_params, position);
			break;
		case 3 + OTHER01_ADD:
			setKernelVersion(holder.tv_name, holder.et_params, position);
			break;
		case 4 + OTHER01_ADD:
			setBasebandVersion(holder.tv_name, holder.et_params, position);
			break;
		case 5 + OTHER01_ADD:
			setRunningProcessCount(holder.tv_name, holder.et_params, position);
			break;
		case 6 + OTHER01_ADD:
			setNeiMemery(holder.tv_name, holder.et_params, position);
			break;
		case 7 + OTHER01_ADD:
			setNeiSdMemery(holder.tv_name, holder.et_params, position);
			break;
		case 8 + OTHER01_ADD:
			setSensoInfo(holder.tv_name, holder.et_params, holder.tv_explain, position);
			break;
		default:
			break;
		}
		return convertView;
	}

	private void sethas_icc_card(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "是否有IccCard");
		et_params.setText(StringUtil.SpanBoolean(TelephonyUtil.getHasIccCard(mainActivity.getActivity())));
	}

	private void setSimState(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "SIM卡状态");
		et_params.setText(String.valueOf(mInfosMode.getSimInfosMode().getSim_state()));
	}

	private void setSimOperator(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "IMSI");
		tv_explain.setText("说明 ：MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)");
		tv_explain.setVisibility(View.VISIBLE);
		et_params.setText(mInfosMode.getSimInfosMode().getSim_operator());

	}

	private void setSimCountryIso(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SIM卡提供商的国家代码");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(mInfosMode.getSimInfosMode().getSim_country_iso());
	}

	private void setNetworkOperatorName(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SPN 服务提供商名称");
		et_params.setText(mInfosMode.getSimInfosMode().getNetwork_operator_name());
	}

	private void setSimOperatorName(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SPN 服务提供商名称(SIM)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(mInfosMode.getSimInfosMode().getSim_operator_name());
	}

	private void setSensoInfo(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "传感器");
		tv_name.setTextColor(Color.GRAY);
		tv_explain.setText("说明:\n" + "Gyroscope-陀螺\n" + "Raw Gyroscope-原始陀螺仪\n" + "Accelerometer-加速传感器\n"
				+ "Magnetic Field-磁场传感器\n" + "Orientation-方位传感器\n" + "Rotation Vector-矢量旋转传感器\n"
				+ "Linear Acceleration-直线加速传感器\n" + "Gravity-重力传感器\n" + "Light Sensor-亮度传感器\n" + "Proximity-接近传感器");
		tv_explain.setVisibility(View.VISIBLE);
		et_params.setText(StringUtil.SpanSize(SensorUtil.get(mainActivity.getActivity()), GlobalConstant.SIZE_26));
	}

	private void setNeiSdMemery(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "内置SD卡存储 可用/总");
		tv_name.setTextColor(Color.GRAY);
		String neiSDAvaiSize = mInfosMode.getMemeryMode().getNeiSDAvaiSize();
		String neiSDTotalSize = mInfosMode.getMemeryMode().getNeiSDTotalSize();
		et_params.setText(neiSDAvaiSize + "/" + neiSDTotalSize);
	}

	private void setNeiMemery(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "内置存储 可用/总");
		tv_name.setTextColor(Color.GRAY);
		String neiAvaiSize = mInfosMode.getMemeryMode().getNeiMemorySize();
		String neiTotalSize = mInfosMode.getMemeryMode().getNeiTotalMemorySize();
		et_params.setText(neiAvaiSize + "/" + neiTotalSize);
	}

	private void setRunningProcessCount(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "正在运行的进程的个数");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(ProcessUtil.getCount(mainActivity.getActivity())));
	}

	private void setBasebandVersion(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "基带版本");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(mInfosMode.getBase_band_version());
	}

	private void setKernelVersion(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "内核版本");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(mInfosMode.getKernel_version());
	}

	private void setCpuMainFrequency(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "CPU主频");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(mInfosMode.getCpu_main_frequency());
	}

	private void setBluetoothAddr(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "蓝牙MAC");
		et_params.setText(mInfosMode.getBluetooth_address());
	}

	private void setScaledDensity2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "ScaledDensity (方法二)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getScaledDensity()));
	}

	private void setWidthPixels2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "WidthPixels (方法二)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getWidthPixels()));
	}

	private void setHeightPixels2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "HeightPixels (方法二)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getHeightPixels()));
	}

	private void setDensityDpi2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "DensityDpi (方法二)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getDensityDpi()));
	}

	private void setDensity2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Density (方法二)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getDensity()));
	}

	private void setYdpi2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Ydpi (方法二)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getYdpi()));
	}

	private void setXdpi2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Xdpi (方法二)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode2().getXdpi()));
	}

	private void setWidth(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Width");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getWidth()));
	}

	private void setHeight(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Height");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getHeight()));
	}

	private void setDiplaySize(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "DiplaySize");
		et_params.setText(mInfosMode.getDisplayMode().getDiplaySize());
	}

	private void setScaledDensity(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "ScaledDensity (方法一)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getScaledDensity()));
	}

	private void setWidthPixels(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "WidthPixels (方法一)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getWidthPixels()));
	}

	private void setHeightPixels(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "HeightPixels (方法一)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getHeightPixels()));
	}

	private void setDensityDpi(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "DensityDpi (方法一)");
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getDensityDpi()));
	}

	private void setDensity(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Density (方法一)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getDensity()));
	}

	private void setYdpi(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Ydpi (方法一)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getYdpi()));
	}

	private void setXdpi(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Xdpi (方法一)");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getDisplayMode().getXdpi()));
	}

	private void setTimeZooCountry(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "时间区域国家");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getTimeZooMode().getCountry()));
	}

	private void setTimeZooLanguage(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "时间区域语言");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getTimeZooMode().getLanguage()));
	}

	private void setTimeZooId(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "时间区域ID");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(String.valueOf(mInfosMode.getTimeZooMode().getId()));
	}

	private void setPhoneType(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "手机制式");
		et_params.setText(String.valueOf(mInfosMode.getSimInfosMode().getPhoneType()));
	}

	private void setUserAgent(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "UserAgent(方法一)");
		et_params.setText(mInfosMode.getUser_agent());
	}

	private void setUserAgent2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "UserAgent(方法二)");
		et_params.setText(mInfosMode.getUser_agent2());
	}

	private void setAndroidID2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "安卓ID(方法二)");
		et_params.setText(mInfosMode.getAndroid_id2());
	}

	private void setAndroidID(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "安卓ID(方法一)");
		et_params.setText(mInfosMode.getAndroid_id());
	}

	private void setPhoneNum(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "手机号码");
		et_params.setText(mInfosMode.getSimInfosMode().getPhone_num());
	}

	private void setSimSerial(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SIM卡的序号");
		et_params.setText(mInfosMode.getSimInfosMode().getSim_serial());
	}

	// private void setActiveNetSubtype(TextView tv_name, EditText et_params,
	// int position) {
	// tv_name.setText(position + "." + "可用网络的类型子类型");
	// String new_str =
	// String.valueOf(mInfosMode.getNetMode().getActiveNetSubtype());
	// et_params.setText(new_str);
	// }
	//
	// private void setActiveNetSubtypeName(TextView tv_name, EditText
	// et_params, int position) {
	// tv_name.setText(position + "." + "可用网络的类型子类型名称");
	// et_params.setText(mInfosMode.getNetMode().getGetActiveNetSubtypeName());
	// }
	//
	// private void setActiveNetType(TextView tv_name, EditText et_params, int
	// position) {
	// tv_name.setText(position + "." + "可用网络的类型");
	// String new_str =
	// String.valueOf(mInfosMode.getNetMode().getActiveNetType());
	// et_params.setText(new_str);
	// }

	private void setActiveNetTypeName(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "可用网络:名称/子名称/类型/子类型");
		String string = mInfosMode.getNetMode().toString();
		if (!TextUtils.isEmpty(string)) {
			tv_explain.setText(string);
			tv_explain.setVisibility(View.VISIBLE);
			tv_explain.setTextColor(Color.BLACK);
		}
		et_params.setVisibility(View.GONE);
	}

//	private void setActiveNetTypeName2(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
//		tv_name.setText(position + "." + "可用网络（方法二）:名称/子名称/类型/子类型");
//		et_params.setVisibility(View.GONE);
//		String string = mInfosMode.getNetMode2().toString();
//		if (!TextUtils.isEmpty(string)) {
//			tv_explain.setText(string);
//			tv_explain.setVisibility(View.VISIBLE);
//			tv_explain.setTextColor(Color.BLACK);
//		}
//	}

	private void setActiveNetState(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "可用网络状态");
		et_params.setText(mInfosMode.getNetMode().getConnect_state());
	}

	private void setIsHasAvailableNet(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "是否有可用网络");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(StringUtil.SpanBoolean(NetUtil.iSHasNet(mainActivity.getActivity())));
	}

	private void setMobieConnect(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "手机网络是否已连接");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(StringUtil.SpanBoolean(NetUtil.isMobileConnect(mainActivity.getActivity())));
	}

	private void setNetType(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "网络类型");
		String new_str = String.valueOf(mInfosMode.getNetMode().getNetType());
		et_params.setText(new_str);
	}

	private void setIsWifiEnabled(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "wifi是否打开");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(StringUtil.SpanBoolean(WifiUtil.isWifiEnabled(mainActivity.getActivity())));
	}

	private void setWifiIsconnect(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "wifi网络是否已连接");
		tv_name.setTextColor(Color.GRAY);
		et_params.setText(StringUtil.SpanBoolean(NetUtil.isWifiConnect(mainActivity.getActivity())));
	}

	private void setInnerIP(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "内网IP");
		et_params.setText(String.valueOf(mInfosMode.getInnerIp()));
	}

	private void setRemoteAddr(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "外网IP");
		tv_name.setTextColor(mColorGreen01);
		et_params.setText(String.valueOf(mInfosMode.getRemote_addr()));
	}

	private void setWifiList(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "wifi列表");
		String wifilist = mInfosMode.getWifi().getWifilist();
		try {
			// JSONArray jsonArray = new JSONArray(wifilist);
			if (!TextUtils.isEmpty(wifilist)) {
				Gson gson = new Gson();
				List<com.mz.iplocation.Mode.WifiMode> list = gson.fromJson(wifilist,
						new TypeToken<ArrayList<com.mz.iplocation.Mode.WifiMode>>() {
						}.getType());
				StringBuffer stringBuffer = new StringBuffer();
				boolean isFirst = true;
				for (WifiMode wifiMode : list) {
					if (!isFirst)
						stringBuffer.append("\n\n");
					isFirst = false;
					String bssid = wifiMode.getBssid();
					String ssid = wifiMode.getSsid();
					int level = wifiMode.getLevel();
					int frequency = wifiMode.getFrequency();
					String capabilities = wifiMode.getCapabilities();
					CharSequence spanAppendLn = StringUtil.SpanAppendLn("BSSID/接入点地址:" + bssid, "SSID/网络的名称:" + ssid,
							"level:" + level, "频率(MHz):" + frequency, "网络的加密方式:" + capabilities);
					stringBuffer.append(spanAppendLn);
				}
				et_params.setText(StringUtil.SpanSize(stringBuffer.toString(), GlobalConstant.SIZE_26));
			} else {
				et_params.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setWifiRssi(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Rssi");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getWifi().getRssi());
		et_params.setText(new_str);
	}

	private void setWifiLinkSpeed(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Wifi_LinkSpeed");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getWifi().getLinkSpeed());
		et_params.setText(new_str);
	}

	private void setWifiSsid(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SSID/网络的名称");
		String new_str = String.valueOf(mInfosMode.getWifi().getSsid());
		et_params.setText(new_str);
	}

	private void setWifiMacAddress(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Wifi_MacAddress");
		String new_str = String.valueOf(mInfosMode.getWifi().getMacAddress());
		et_params.setText(new_str);
	}

	private void setWifiBssid(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "BSSID/接入点地址");
		String new_str = String.valueOf(mInfosMode.getWifi().getBssid());
		et_params.setText(new_str);
	}

	private void setAccuracy(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "经纬度精确度");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getLogLanAccuracy());
		et_params.setText(new_str);
	}

	private void setBuildRadioVersion(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Build_radio_version");
		String new_str = String.valueOf(mInfosMode.getBuild_radio_version());
		et_params.setText(new_str);
	}

	private void setBuildIncremental(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "源码控制版本号");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_incremental());
		et_params.setText(new_str);
	}

	private void setBuildBootloader(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "BOOTLOADER");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_bootloader());
		et_params.setText(new_str);
	}

	private void setBuildSerial(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "硬件序列号");
		tv_explain.setText(StringUtil.SpanAppendStrColor("requires API level 9", Color.GRAY));
		tv_explain.setVisibility(View.VISIBLE);
		String new_str = String.valueOf(mInfosMode.getBuild_serial());
		et_params.setText(new_str);
	}

	private void setBuildDevice(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "设备驱动/设备参数");
		String new_str = String.valueOf(mInfosMode.getBuild_device());
		et_params.setText(new_str);
	}

	private void setBuildTag(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "标签/系统标记");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_tag());
		et_params.setText(new_str);
	}

	private void setBuildSdk(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SDK版本号(方法1)");
		String new_str = String.valueOf(mInfosMode.getBuild_sdk());
		et_params.setText(new_str);
	}

	private void setBuildSdkInt(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "SDK版本号(方法2)");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_sdk_int());
		et_params.setText(new_str);
	}

	private void setBuildType(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "用户组");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_type());
		et_params.setText(new_str);
	}

	private void setBuildHost(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "HOST/系统主机名");
		String new_str = String.valueOf(mInfosMode.getBuild_host());
		et_params.setText(new_str);
	}

	private void setBuildCodeName(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "版本代号");
		tv_name.setTextColor(Color.GRAY);
		tv_explain.setText(StringUtil.SpanAppendStrColor("说明：如果是发行版，则为", Color.GRAY, "REL", Color.RED));
		tv_explain.setVisibility(View.VISIBLE);
		String new_str = String.valueOf(mInfosMode.getBuild_code_name());
		et_params.setText(new_str);
	}

	private void setBuildFingerprint(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "指纹/唯一识别码");
		String new_str = String.valueOf(mInfosMode.getBuild_fingerprint());
		et_params.setText(new_str);
	}

	private void setBuildDisplay(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "软件版本号/Build_display");
		String new_str = String.valueOf(mInfosMode.getBuild_display());
		et_params.setText(new_str);
	}

	private void setBuildID(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "ID/修订版本");
		String new_str = String.valueOf(mInfosMode.getBuild_id());
		et_params.setText(new_str);
	}

	private void setManufacturer(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "制造商");
		String new_str = String.valueOf(mInfosMode.getBuild_manufacturer());
		et_params.setText(new_str);
	}

	private void setBuildCpuAbi(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "CPU的版本");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_cup_abi());
		et_params.setText(new_str);
	}

	private void setBuildCpuAbi2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "CPU_ABI2");
		String new_str = String.valueOf(mInfosMode.getBuild_cup_abi2());
		et_params.setText(new_str);
	}

	private void setBuildProduct2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "产品名称");
		String new_str = String.valueOf(mInfosMode.getBuild_product());
		et_params.setText(new_str);
	}

	private void setBuildBoard2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "主板");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getBuild_board());
		et_params.setText(new_str);
	}

	private void setBuildMold2(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "手机型号");
		String new_str = String.valueOf(mInfosMode.getBuild_mold());
		et_params.setText(new_str);
	}

	private void setBuildBrand(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "手机品牌");
		String new_str = String.valueOf(mInfosMode.getBuild_brand());
		et_params.setText(new_str);
	}

	private void setBuildRelease(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "系统版本");
		String new_str = String.valueOf(mInfosMode.getBuild_release());
		et_params.setText(new_str);
	}

	private void setCellID(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "小区ID");
		String new_str = String.valueOf(mInfosMode.getScell().getCid());
		et_params.setText(new_str);
	}

	private void setCellLac(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "Lac");
		String new_str = String.valueOf(mInfosMode.getScell().getLac());
		et_params.setText(new_str);
	}

	private void setOperator(TextView tv_name, EditText et_params, TextView tv_explain, int position) {
		tv_name.setText(position + "." + "注册的网络运营商");
		tv_name.setTextColor(Color.GRAY);
		String new_str = String.valueOf(mInfosMode.getSimInfosMode().getOperator());
		et_params.setText(new_str);
	}

	private void setLongitude(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "经度");
		String new_str = String.valueOf(mInfosMode.getLongitude());
		setColor(et_params, new_str);
		et_params.setText(new_str);
	}

	private void setLatitude(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "纬度");
		String new_str = String.valueOf(mInfosMode.getLatitude());
		setColor(et_params, new_str);
		et_params.setText(new_str);
	}

	private void setIMEI(TextView tv_name, EditText et_params, int position) {
		tv_name.setText(position + "." + "IMEI");
		String new_str = mInfosMode.getSimInfosMode().getImei();
		setColor(et_params, new_str);
		et_params.setText(new_str);
	}

	private void setColor(EditText et_params, String new_str) {
		// String old_str = et_params.getText().toString();
		// if(!TextUtils.isEmpty(new_str)&&!new_str.equals(old_str)&&isColorTag()){
		// et_params.setTextColor(Color.RED);
		// Log.v("mytag", "imei--RED--");
		// } else {
		// et_params.setTextColor(Color.BLACK);
		// Log.v("mytag", "imei--BLACK--");
		// }
	}

	class Holder {
		EditText et_params;
		TextView tv_name;
		TextView tv_explain;
	}

}
