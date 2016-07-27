package com.hdj.hook.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.RecordToFile;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.adapter.SystemValueAdapter;
import com.hdj.hook.mode.GetStringUtil;
import com.hdj.hook.mode.SystemValueMode;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.ProgressDialogUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.StreamTextUtils;
import com.hdj.hook.util.ToastUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListenSystemValueFragment extends Fragment implements OnClickListener {

	private Activity mActivity;
	private ListView mLvlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_listen_system_value, null);
		findViewById(view);
		return view;
	}

	private void findViewById(View view) {
		Button btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
		Button btn_delete_record = (Button) view.findViewById(R.id.btn_delete_record);
		mLvlist = (ListView) view.findViewById(R.id.lv_list);
		btn_refresh.setOnClickListener(this);
		btn_delete_record.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete_record:

			break;
		case R.id.btn_refresh:

			togetData();
			// togetAllData();
			break;

		default:
			break;
		}

	}

	HashMap<String, ArrayList<String>> mDataMap;

	SystemValueAdapter mSystemValueAdapter;

	private void togetData() {
		if (mSystemValueAdapter != null)
			mSystemValueAdapter.clearData();
		try {
			String packageName = SPrefUtil.getSettingStr(mActivity, GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
			if (TextUtils.isEmpty(packageName)) {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.has_no_app_listen));
					}
				});
				return;
			}
			String foldPath = RecordToFile.getFoldPath(GlobalConstant.FOLDER_SYSTEM_VALUE);
			File file = new File(foldPath, packageName);
			if (file.exists()) {
				final FileInputStream is = new FileInputStream(file);
				final ArrayList<String> textList = StreamTextUtils.getTextListFromStream2(is);
				if (textList.size() > 0) {
					if (mSystemValueAdapter == null)
						mSystemValueAdapter = new SystemValueAdapter(mActivity);
					mSystemValueAdapter.setData(textList);
					mLvlist.setAdapter(mSystemValueAdapter);
				} else {
					mActivity.runOnUiThread(new Runnable() {
						public void run() {
							ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.no_data));
						}
					});
				}
			} else {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.no_data));
					}
				});
			}
		} catch (Exception e) {
		}
	}

	public static void setDefault(String packageName, Context context) {
		String oprera_ok = GetStringUtil.get(context, R.string.opera_ok);
		String opera_fail = GetStringUtil.get(context, R.string.opera_fail);

		File file = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_SYSTEM_VALUE), packageName);
		if (!file.exists()) {
			AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.no_system_value_setting), context);
			return;
		}
		try {
			FileInputStream is = new FileInputStream(file);
			final ArrayList<String> textList = StreamTextUtils.getTextListFromStream2(is);
			if (textList.size() == 0) {
				AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.no_system_value_setting),
						context);
				return;
			}
			Gson gson = new Gson();
			HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
			for (String string : textList) {
				if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string.trim())) {
					SystemValueMode mode = gson.fromJson(string, SystemValueMode.class);
					String method = mode.getMethod();
					ArrayList<String> data = mode.getData();
					if (GlobalConstant.GET_STRING.equals(method) || GlobalConstant.PUT_STRING.equals(method)) {
						switch (data.size()) {
						case 1:
						case 2:
							String string2 = data.get(0);
							if(hashMap.containsKey(string2))return;
							hashMap.put(string2, 1);
							boolean isOk = System.putString(context.getContentResolver(), string2, null);
							if (isOk) {
								AutoActivity.sendHandlerOnMain(
										GlobalConstant.PUT_STRING + "\n" + data.get(0) + "\n" + oprera_ok, context);
							} else {
								AutoActivity.sendHandlerRedOnMain(
										GlobalConstant.PUT_STRING + "\n" + data.get(0) + "\n" + opera_fail, context);
							}
							break;

						default:
							break;
						}
					}
					// else if (GlobalConstant.PUT_LONG.equals(method)) {
					// switch (data.size()) {
					// case 1:
					// case 2:
					// boolean isOk =
					// System.putLong(context.getContentResolver(), data.get(0),
					// -1l);
					// if (isOk) {
					// ControlFragment.sendHandlerOnMain(
					// GlobalConstant.PUT_LONG + "\n" + data.get(0) + "\n" +
					// oprera_ok, context);
					// } else {
					// ControlFragment.sendHandlerRedOnMain(
					// GlobalConstant.PUT_LONG + "\n" + data.get(0) + "\n" +
					// opera_fail, context);
					// }
					// break;
					//
					// default:
					// break;
					// }
					// } else if (GlobalConstant.PUT_FLOAT.equals(method)) {
					// switch (data.size()) {
					// case 1:
					// case 2:
					// boolean isOk =
					// System.putFloat(context.getContentResolver(),
					// data.get(0), -1f);
					// if (isOk) {
					// ControlFragment.sendHandlerOnMain(
					// GlobalConstant.PUT_FLOAT + "\n" + data.get(0) + "\n" +
					// oprera_ok, context);
					// } else {
					// ControlFragment.sendHandlerRedOnMain(
					// GlobalConstant.PUT_FLOAT + "\n" + data.get(0) + "\n" +
					// opera_fail, context);
					// }
					// break;
					//
					// default:
					// break;
					// }
					// } else if (GlobalConstant.PUT_INT.equals(method)) {
					// switch (data.size()) {
					// case 1:
					// case 2:
					// boolean isOk =
					// System.putInt(context.getContentResolver(), data.get(0),
					// -1);
					// if (isOk) {
					// ControlFragment.sendHandlerOnMain(
					// GlobalConstant.PUT_INT + "\n" + data.get(0) + "\n" +
					// oprera_ok, context);
					// } else {
					// ControlFragment.sendHandlerRedOnMain(
					// GlobalConstant.PUT_INT + "\n" + data.get(0) + "\n" +
					// opera_fail, context);
					// }
					// break;
					//
					// default:
					// break;
					// }
					// }

				}

			}
			boolean delete = file.delete();
			if (delete) {
				AutoActivity.sendHandlerOnMain(
						GetStringUtil.get(context, R.string.delete_system_value_record_file) + "\n" + oprera_ok,
						context);
			} else {
				AutoActivity.sendHandlerRedOnMain(
						GetStringUtil.get(context, R.string.delete_system_value_record_file) + "\n" + opera_fail,
						context);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.v(GlobalConstant.MYTAG, "--system--value--FileNotFoundException---" + e.toString());
		}

	}

}
