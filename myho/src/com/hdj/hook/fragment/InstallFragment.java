package com.hdj.hook.fragment;

import com.hdj.hook.R;
import com.mz.iplocation.constants.URLConstants;
import com.mz.iplocation.utils.DBHelper;
import com.mz.iplocation.utils.SilentInstallUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class InstallFragment extends Fragment implements OnValueChangeListener, OnScrollListener, Formatter {
	private NumberPicker np_ins_minute, np_uns_minute, np_ins_second, np_uns_second;
	private TextView tv_install_tip, tv_open_tip, tv_uninstall_tip;
	private ToggleButton tb_install, tb_open, tb_uninstall;
	private LinearLayout showuninstall, uninstall_time;
	private SharedPreferences preferences;
	private Editor editor;
	private String minuteArray[] = new String[6];
	boolean install_open, openApp_open, uninstall_open;
	int install_period_m, install_period_s, unstall_period_m, unstall_period_s;
	int installTime, uninstallTime;// s

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	private Activity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = View.inflate(mActivity, R.layout.activity_install_setting, null);
		preferences = mActivity.getSharedPreferences(URLConstants.SEETING_KEEP_SP, Context.MODE_PRIVATE);
		editor = preferences.edit();
		install_open = preferences.getBoolean("install_open", false);
		openApp_open = preferences.getBoolean("openApp_open", false);
		uninstall_open = preferences.getBoolean("uninstall_open", false);
		installTime = preferences.getInt("install_period", 15);
		uninstallTime = preferences.getInt("uninstall_period", 5);
		formatMS();
		for (int i = 0; i <= 5; i++) {
			if (i < 10) {
				minuteArray[i] = "0" + String.valueOf(i);
			}
		}
		initView(view);

		return view;
	}

	private void formatMS() {
		install_period_m = installTime / 60;
		install_period_s = installTime % 60;
		unstall_period_m = uninstallTime / 60;
		unstall_period_s = uninstallTime % 60;
	}

	private void initView(View view) {
		tv_install_tip = (TextView) view.findViewById(R.id.tv_install_tip);
		tv_open_tip = (TextView) view.findViewById(R.id.tv_open_tip);
		tv_uninstall_tip = (TextView) view.findViewById(R.id.tv_uninstall_tip);
		showuninstall = (LinearLayout) view.findViewById(R.id.showuninstall);
		uninstall_time = (LinearLayout) view.findViewById(R.id.uninstall_time);
		tb_install = (ToggleButton) view.findViewById(R.id.tb_install);
		tb_open = (ToggleButton) view.findViewById(R.id.tb_open);
		tb_uninstall = (ToggleButton) view.findViewById(R.id.tb_uninstall);

		np_ins_minute = (NumberPicker) view.findViewById(R.id.np_ins_minute);
		np_uns_minute = (NumberPicker) view.findViewById(R.id.np_uns_minute);
		np_ins_second = (NumberPicker) view.findViewById(R.id.np_ins_second);
		np_uns_second = (NumberPicker) view.findViewById(R.id.np_uns_second);

		np_ins_minute.setFormatter(this);
		np_ins_minute.setDisplayedValues(minuteArray);
		np_ins_minute.setOnValueChangedListener(this);
		np_ins_minute.setOnScrollListener(this);
		np_ins_minute.setMaxValue(minuteArray.length - 1);
		np_ins_minute.setMinValue(0);
		np_ins_minute.setValue(install_period_m);
		np_ins_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		np_ins_second.setFormatter(this);
		np_ins_second.setOnValueChangedListener(this);
		np_ins_second.setOnScrollListener(this);
		np_ins_second.setMaxValue(59);
		np_ins_second.setMinValue(0);
		np_ins_second.setValue(install_period_s);
		np_ins_second.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		np_uns_minute.setFormatter(this);
		np_uns_minute.setDisplayedValues(minuteArray);
		np_uns_minute.setOnValueChangedListener(this);
		np_uns_minute.setOnScrollListener(this);
		np_uns_minute.setMaxValue(minuteArray.length - 1);
		np_uns_minute.setMinValue(0);
		np_uns_minute.setValue(unstall_period_m);
		np_uns_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		np_uns_second.setFormatter(this);
		np_uns_second.setOnValueChangedListener(this);
		np_uns_second.setOnScrollListener(this);
		np_uns_second.setMaxValue(59);
		np_uns_second.setMinValue(0);
		np_uns_second.setValue(unstall_period_s);
		np_uns_second.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		tb_install.setChecked(install_open);
		tb_open.setChecked(openApp_open);
		tb_uninstall.setChecked(uninstall_open);

		if (install_open) {
			tv_install_tip.setText("静默安装已开");
			showuninstall.setVisibility(View.VISIBLE);
		} else {
			tv_install_tip.setText("静默安装已关");
			showuninstall.setVisibility(View.GONE);
		}
		if (openApp_open)
			tv_open_tip.setText("打开app功能（开启）");
		else
			tv_open_tip.setText("打开app功能（关闭）");
		if (uninstall_open) {
			tv_uninstall_tip.setText("静默卸载已开");
			uninstall_time.setVisibility(View.VISIBLE);
		} else {
			tv_uninstall_tip.setText("静默卸载已关");
			uninstall_time.setVisibility(View.GONE);
		}
		tb_install.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tv_install_tip.setText("静默安装已开");
					showuninstall.setVisibility(View.VISIBLE);
					//
				} else {
					tv_install_tip.setText("静默安装已关");
					showuninstall.setVisibility(View.GONE);
				}
				editor.putBoolean("install_open", isChecked).commit();/* .commit() */;
			}
		});
		tb_open.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tv_open_tip.setText("打开app功能（开启）");
				} else {
					tv_open_tip.setText("打开app功能（关闭）");
				}
				editor.putBoolean("openApp_open", isChecked).commit()/* .commit() */;
			}
		});
		tb_uninstall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tv_uninstall_tip.setText("静默卸载已开");
					uninstall_time.setVisibility(View.VISIBLE);
				} else {
					tv_uninstall_tip.setText("静默卸载已关");
					uninstall_time.setVisibility(View.GONE);
				}
				editor.putBoolean("uninstall_open", isChecked).commit();/* .commit() */;
			}
		});
	}

	@Override
	public String format(int value) {
		String tmpStr = String.valueOf(value);
		if (value < 10) {
			tmpStr = "0" + tmpStr;
		}
		return tmpStr;
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		switch (picker.getId()) {
		case R.id.np_ins_minute:
			install_period_m = newVal;
			break;
		case R.id.np_ins_second:
			install_period_s = newVal;
			break;
		case R.id.np_uns_minute:
			unstall_period_m = newVal;
			break;
		case R.id.np_uns_second:
			unstall_period_s = newVal;
			break;
		}
		saveTimeSetData();
	}

	private boolean saveTimeSetData() {
		int in = install_period_m * 60 + install_period_s;
		int un = unstall_period_m * 60 + unstall_period_s;

		Log.e("SilentSettingActivity--", "WaitTiem = " + in + "UninstallTime = " + un);

		if (in == 0 || un == 0) {
			showToast("时间不能为0");
			return false;
		}
		// Editor edit = preferences.edit();
		editor.putInt("install_period", in);
		editor.putInt("uninstall_period", un);//
		editor.commit();

		return true;
	}

	Toast toast;

	private void showToast(String tip) {
		toast = toast == null ? Toast.makeText(mActivity, "", Toast.LENGTH_SHORT) : toast;
		toast.setText(tip);
		toast.show();
	}
	
	public static void removeInstalledAPK(Context context){
		SilentInstallUtil.removeInstalledAPK(context);
//		DBHelper dbHelper = DBHelper.getInstance(context);
//		dbHelper.reCreateInstallTable();
	}

}
