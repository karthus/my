package com.hdj.hook.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.adapter.AppInfosAdapter;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.mode.DataListMode;
import com.hdj.hook.util.AppInfosUtil;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.ProcessUtil;
import com.hdj.hook.util.SPrefUtil;
import com.mz.iplocation.utils.SilentInstallUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class TaskManagerFragment extends BaseFragment implements OnClickListener {

	private ListView mLvList;
	public static final int TYPE_GLOBAL_CHANGE_PROTECT = 1;
	public static final int TYPE_HIDE_PACKAGE_NAME = 2;
	public static final int TYPE_MARKET = 3;
	public static final int TYPE_BROWSER = 4;
	public int mType;

	public TaskManagerFragment(int type) {
		mType = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mMainActivity, R.layout.activity_task_manager, null);
		findViewById(view);
		getLocalData();
		getAppData();
		setListener();
		return view;
	}

	HashMap<String, Integer> mLocalProtectMap = new HashMap<String, Integer>();

	private void getLocalData() {
		String str = null;
		switch (mType) {
		case TYPE_GLOBAL_CHANGE_PROTECT:
			str = SPrefUtil.getSettingStr(mMainActivity, SPrefUtil.KEY_GLOBAL_CHAGNE_PROTECT);
			break;
		case TYPE_HIDE_PACKAGE_NAME:
			str = SPrefUtil.getSettingStr(mMainActivity, SPrefUtil.KEY_HIDE_PACKAGE_NAME);
			break;
		case TYPE_MARKET:
			str = SPrefUtil.getSettingStr(mMainActivity, SPrefUtil.KEY_MARKET);
			break;
		case TYPE_BROWSER:
			str = SPrefUtil.getSettingStr(mMainActivity, SPrefUtil.KEY_BROWSER);
			break;

		default:
			break;
		}

		if (!TextUtils.isEmpty(str)) {
			Gson gson = new Gson();
			ArrayList<String> browserList = gson.fromJson(str, DataListMode.class).getDataList();
			for (String string : browserList) {
				mLocalProtectMap.put(string, 1);
			}
		}
	}

	public static void deleteBrowerData(Context context) {
		String str_brower = SPrefUtil.getSettingStr(context, SPrefUtil.KEY_BROWSER);
		if (!TextUtils.isEmpty(str_brower)) {
			Gson gson = new Gson();
			ArrayList<String> browserList = gson.fromJson(str_brower, DataListMode.class).getDataList();
			browserList.add(GlobalConstant.DELETE_DATA_BROWSER_01);
			for (String string : browserList) {
				boolean killProcess = ProcessUtil.killProcess(context, string);
				CharSequence label = AppInfosUtil.getLabel(string, context);
				if (killProcess) {
					AutoActivity
							.sendHandlerOnMain(
									context.getResources().getString(R.string.request_close_browser) + label + "/"
											+ string + "\n" + context.getResources().getString(R.string.opera_ok),
									context);
				} else {
					AutoActivity
							.sendHandlerRedOnMain(
									context.getResources().getString(R.string.request_close_browser) + label + "/"
											+ string + "\n" + context.getResources().getString(R.string.opera_fail),
									context);
				}
				clearBrowserExternalCache(string, context, label);
			}
		} else {
			AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.no_browser_to_clear_data),
					context);
		}
	}

	private static void clearBrowserExternalCache(String packageName, Context context, CharSequence label) {
		boolean isDelete = SilentInstallUtil.clearAllData(packageName);

		if (isDelete) {
			AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.clear_browser_data) + label + "/"
					+ packageName + "\n" + context.getResources().getString(R.string.opera_ok), context);
		} else {
			AutoActivity.sendHandlerRedOnMain(context.getResources().getString(R.string.clear_browser_data) + label
					+ "/" + packageName + "\n" + context.getResources().getString(R.string.opera_fail), context);
		}

	}

	private boolean saveData(AppInfosMode appInfosMode2, boolean isAdd, int pos) {
		boolean putData = false;
		ArrayList<AppInfosMode> list = new ArrayList<>();
		list.addAll(mProtectTaskInfos);
		if (isAdd) {
			list.add(appInfosMode2);
		} else {
			list.remove(pos);
		}
		
		ArrayList<String> arrayList = new ArrayList<String>();
		for (AppInfosMode appInfosMode : list) {
			String packageName = appInfosMode.getPackageInfo().packageName;
			switch (mType) {
			case TYPE_GLOBAL_CHANGE_PROTECT:
			case TYPE_HIDE_PACKAGE_NAME:
				if (AppInfosUtil.isDefaultHidePackageAndGlobalChangeProtect(packageName))
					continue;
				break;
			case TYPE_MARKET:
				break;
			case TYPE_BROWSER:
				if (AppInfosUtil.isDefaultDeleteBrowserData(packageName))
					continue;
				break;
			default:
				break;
			}
			arrayList.add(packageName);
		}
		
		if (arrayList.size() > 0) {
			HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
			hashMap.put(DataListMode.DATA_LIST, arrayList);
			Gson gson = new Gson();
			String json = gson.toJson(hashMap);
			switch (mType) {
			case TYPE_GLOBAL_CHANGE_PROTECT:
				putData = SPrefUtil.putSettingStr(mMainActivity, json, SPrefUtil.KEY_GLOBAL_CHAGNE_PROTECT);
				break;
			case TYPE_HIDE_PACKAGE_NAME:
				putData = SPrefUtil.putSettingStr(mMainActivity, json, SPrefUtil.KEY_HIDE_PACKAGE_NAME);
				break;
			case TYPE_MARKET:
				putData = SPrefUtil.putSettingStr(mMainActivity, json, SPrefUtil.KEY_MARKET);
				break;
			case TYPE_BROWSER:
				putData = SPrefUtil.putSettingStr(mMainActivity, json, SPrefUtil.KEY_BROWSER);
				break;
			default:
				break;
			}
		} else {
			switch (mType) {
			case TYPE_GLOBAL_CHANGE_PROTECT:
				putData = SPrefUtil.putSettingStr(mMainActivity, "", SPrefUtil.KEY_GLOBAL_CHAGNE_PROTECT);
				break;
			case TYPE_HIDE_PACKAGE_NAME:
				putData = SPrefUtil.putSettingStr(mMainActivity, "", SPrefUtil.KEY_HIDE_PACKAGE_NAME);
				break;
			default:
				break;
			}
		}
		return putData;
	}

	int lastTaskPos = -1;
	int lastProtectPos = -1;
	long lastTaskTime;
	long lastProtectTime;
	TranslateAnimation ta;
	boolean isLock;

	private void setListener() {
		mLvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (isLock)
					return;
				long currentTimeMillis = System.currentTimeMillis();
				View view2 = view.findViewById(R.id.ll_ll);
				if (mIsTask) {
					if (currentTimeMillis - lastTaskTime < 300 && lastTaskPos == position) {
						addProtectData(position, view2);
					}
					lastTaskPos = position;
					lastTaskTime = currentTimeMillis;
				} else {
					 AppInfosMode appInfosMode = mProtectTaskInfos.get(position);
					 if(appInfosMode.isDefault())return;
					if (currentTimeMillis - lastProtectTime < 300 && lastProtectPos == position) {
						removeProtectData(position, view2,appInfosMode);
					}
					lastProtectPos = position;
					lastProtectTime = currentTimeMillis;
				}

				// AppInfosMode appInfosMode = taskInfos.get(position);
				// PackageInfo packageInfo = appInfosMode.getPackageInfo();
				// String packageName = packageInfo.packageName;
				// SPrefUtil.putHookStr(getApplicationContext(), packageName,
				// packageName);

			}
		});

	}

	private void addProtectData(final int position, View view) {
		isLock = true;
		final AppInfosMode appInfosMode = mTaskInfos.get(position);
		boolean saveData = saveData(appInfosMode, true, position);
		if (saveData) {
			CheckBox cb_checkbox = (CheckBox) view.findViewById(R.id.cb_checkbox);
			cb_checkbox.setChecked(true);
			TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

			ta.setDuration(700);
			view.startAnimation(ta);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					appInfosMode.setIscheck(true);
					mProtectTaskInfos.add(appInfosMode);
					mTaskInfos.remove(position);
					mAppInfosAdapter.notifyDataSetChanged();
					isLock = false;
				}
			}, 700);
		} else {
			isLock = false;
		}
	}

	private void removeProtectData(final int position, View view,final AppInfosMode appInfosMode) {
		isLock = true;
		boolean saveData = saveData(appInfosMode, false, position);
		if (saveData) {
			CheckBox cb_checkbox = (CheckBox) view.findViewById(R.id.cb_checkbox);
			cb_checkbox.setChecked(false);
			TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

			ta.setDuration(700);
			view.startAnimation(ta);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					appInfosMode.setIscheck(false);
					mTaskInfos.add(appInfosMode);
					mProtectTaskInfos.remove(position);
					mAppInfosAdapter.notifyDataSetChanged();
					isLock = false;
				}
			}, 700);
		} else {
			isLock = false;
		}
	}

	private AppInfosAdapter mAppInfosAdapter;
	List<AppInfosMode> mTaskInfos = new ArrayList<>();
	List<AppInfosMode> mProtectTaskInfos = new ArrayList<>();

	private void getAppData() {
		new Thread() {
			public void run() {
				switch (mType) {
				case TYPE_HIDE_PACKAGE_NAME:
				case TYPE_GLOBAL_CHANGE_PROTECT:
					getAllApp();
					break;
				case TYPE_MARKET:
					getMarketApp();
					break;
				case TYPE_BROWSER:
					getBrowserApp();
					break;

				default:
					break;
				}

			};
		}.start();
	}

	private void getAllApp() {
		ArrayList<AppInfosMode> appList = AppInfosUtil.getInstallApp(mMainActivity);
		for (AppInfosMode appInfosMode : appList) {
			PackageInfo packageInfo = appInfosMode.getPackageInfo();
			String packageName = packageInfo.packageName;
			if (mLocalProtectMap.containsKey(packageName)&&!AppInfosUtil.isDefaultHidePackageAndGlobalChangeProtect(packageName)) {
				appInfosMode.setIscheck(true);
				mProtectTaskInfos.add(appInfosMode);
			} else if(AppInfosUtil.isDefaultHidePackageAndGlobalChangeProtect(packageName)){
				appInfosMode.setIscheck(true);
				appInfosMode.setDefault(true);
				mProtectTaskInfos.add(appInfosMode);
			} else {
				mTaskInfos.add(appInfosMode);
			}
		}
		sortList(mProtectTaskInfos);
		mMainActivity.runOnUiThread(new Runnable() {
			public void run() {
				mAppInfosAdapter = new AppInfosAdapter(mMainActivity, mProtectTaskInfos, true, true);
				mLvList.setAdapter(mAppInfosAdapter);
			}
		});
	}

	private void getMarketApp() {
		Uri uri = Uri.parse("market://details?id=");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);

		List<ResolveInfo> resolveInfoList = mMainActivity.getPackageManager().queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);

		ArrayList<AppInfosMode> appList = new ArrayList<>();

		for (ResolveInfo resolveInfo : resolveInfoList) {
			AppInfosMode appInfosMode = new AppInfosMode();
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			String packageName = activityInfo.packageName;
			PackageManager pm = mMainActivity.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
				appInfosMode.setPackageInfo(packageInfo);

				String pinYin = AppInfosUtil.getLabelNamePinYin(packageInfo, mMainActivity);
				appInfosMode.setPinYin(pinYin);

				String first_str = pinYin.trim().subSequence(0, 1).toString();
				if (first_str.matches("[A-Z]")) {
					appInfosMode.setAlpha(first_str);
				} else {
					appInfosMode.setAlpha("#");
				}

				appList.add(appInfosMode);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		for (AppInfosMode appInfosMode : appList) {
			PackageInfo packageInfo = appInfosMode.getPackageInfo();
			String packageName = packageInfo.packageName;
			if (mLocalProtectMap.containsKey(packageName)) {
				appInfosMode.setIscheck(true);
				mProtectTaskInfos.add(appInfosMode);
			} else {
				mTaskInfos.add(appInfosMode);
			}
		}
		sortList(mProtectTaskInfos);
		mMainActivity.runOnUiThread(new Runnable() {
			public void run() {
				mAppInfosAdapter = new AppInfosAdapter(mMainActivity, mProtectTaskInfos, true, true);
				mLvList.setAdapter(mAppInfosAdapter);
			}
		});
	}

	private void getBrowserApp() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		Uri uri = Uri.parse("http://");
		intent.setData(uri);
		List<ResolveInfo> resolveInfoList = mMainActivity.getPackageManager().queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);

		ArrayList<AppInfosMode> appList = new ArrayList<>();

		for (ResolveInfo resolveInfo : resolveInfoList) {
			AppInfosMode appInfosMode = new AppInfosMode();
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			String packageName = activityInfo.packageName;
			PackageManager pm = mMainActivity.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
				appInfosMode.setPackageInfo(packageInfo);

				String pinYin = AppInfosUtil.getLabelNamePinYin(packageInfo, mMainActivity);
				appInfosMode.setPinYin(pinYin);

				String first_str = pinYin.trim().subSequence(0, 1).toString();
				if (first_str.matches("[A-Z]")) {
					appInfosMode.setAlpha(first_str);
				} else {
					appInfosMode.setAlpha("#");
				}

				appList.add(appInfosMode);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		for (AppInfosMode appInfosMode : appList) {
			PackageInfo packageInfo = appInfosMode.getPackageInfo();
			String packageName = packageInfo.packageName;
			if (mLocalProtectMap.containsKey(packageName)&&!AppInfosUtil.isDefaultDeleteBrowserData(packageName)) {
				appInfosMode.setIscheck(true);
				mProtectTaskInfos.add(appInfosMode);
			} else if(AppInfosUtil.isDefaultDeleteBrowserData(packageName)) {
				appInfosMode.setIscheck(true);
				appInfosMode.setDefault(true);
				mProtectTaskInfos.add(appInfosMode);
			}else {
				mTaskInfos.add(appInfosMode);
			}
		}
		
		sortList(mProtectTaskInfos);
		mMainActivity.runOnUiThread(new Runnable() {
			public void run() {
				mAppInfosAdapter = new AppInfosAdapter(mMainActivity, mProtectTaskInfos, true, true);
				mLvList.setAdapter(mAppInfosAdapter);
			}
		});
	}

	private void sortList(List<AppInfosMode> list) {
		Collections.sort(list, new Comparator<AppInfosMode>() {

			@Override
			public int compare(AppInfosMode lhs, AppInfosMode rhs) {
				String lhs_pinYin = lhs.getPinYin();
				String rhs_pinYin = rhs.getPinYin();
				return lhs_pinYin.compareTo(rhs_pinYin);
			}
		});
	}

	Button btn_task;
	Button btn_protect;

	private void findViewById(View view) {
		mLvList = (ListView) view.findViewById(R.id.lv_list);
		btn_task = (Button) view.findViewById(R.id.btn_task);
		btn_protect = (Button) view.findViewById(R.id.btn_protect);
		btn_task.setOnClickListener(this);
		btn_protect.setOnClickListener(this);
		btn_protect.setSelected(true);
		btn_task.setText(getResources().getString(R.string.app));
		switch (mType) {
		case TYPE_GLOBAL_CHANGE_PROTECT:
			btn_protect.setText(getResources().getString(R.string.global_change_protect_task));
			break;
		case TYPE_HIDE_PACKAGE_NAME:
			btn_protect.setText(getResources().getString(R.string.hide_package_name));
			break;
		case TYPE_MARKET:
			btn_protect.setText(getResources().getString(R.string.listen_market));
			break;
		case TYPE_BROWSER:
			btn_protect.setText(getResources().getString(R.string.clear_browser));
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_task:
			selectChange(true);
			break;
		case R.id.btn_protect:
			selectChange(false);
			break;

		default:
			break;
		}
	}

	boolean mIsTask = false;

	private void selectChange(boolean isTask) {
		if (mIsTask == isTask)
			return;
		mIsTask = isTask;
		if (isTask) {
			btn_protect.setSelected(false);
			btn_task.setSelected(true);
			sortList(mTaskInfos);
			mAppInfosAdapter.setData(mTaskInfos);
			mAppInfosAdapter.notifyDataSetChanged();
		} else {
			btn_task.setSelected(false);
			btn_protect.setSelected(true);
			sortList(mProtectTaskInfos);
			mAppInfosAdapter.setData(mProtectTaskInfos);
			mAppInfosAdapter.notifyDataSetChanged();
		}

	}

}
