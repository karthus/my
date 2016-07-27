package com.hdj.hook.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.adapter.AppInfosAdapter;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.mode.DataListMode;
import com.hdj.hook.util.AppInfosUtil;
import com.hdj.hook.util.DataCleanManager;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.ProgressDialogUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.StringUtil;
import com.hdj.hook.util.ToastUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ClearBrowerDataFragment extends Fragment implements OnClickListener {

	private Activity mActivity;
	private ListView lv_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View veiw = View.inflate(mActivity, R.layout.fragment_clear_brower_data, null);
		findViewById(veiw);
		initString();
		getLocalData();
		getBrowserApp();
		setListener();
		return veiw;
	}

	HashMap<String, Integer> mLocalBrowserMap = new HashMap<String, Integer>();

	private void getLocalData() {
		String browser = SPrefUtil.getSettingStr(mActivity, SPrefUtil.KEY_BROWSER);
		if (!TextUtils.isEmpty(browser)) {
			Gson gson = new Gson();
			ArrayList<String> browserList = gson.fromJson(browser, DataListMode.class).getDataList();
			for (String string : browserList) {
				mLocalBrowserMap.put(string, 1);
			}
		}
	}

	private void setListener() {
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfosMode browerMode = mBrowerList.get(position);
				browerMode.setIscheck(!browerMode.isIscheck());
				mClearBrowerAdapter.notifyDataSetChanged();
			}
		});

	}

	Button btn_select_all, btn_select_opposite, btn_delete_record, btn_delete_record2, btn_save;
	private AppInfosAdapter mClearBrowerAdapter;
	private ArrayList<AppInfosMode> mBrowerList;

	private void findViewById(View veiw) {
		lv_list = (ListView) veiw.findViewById(R.id.lv_list);
		btn_save = (Button) veiw.findViewById(R.id.btn_save);
		btn_select_all = (Button) veiw.findViewById(R.id.btn_select_all);
		btn_select_opposite = (Button) veiw.findViewById(R.id.btn_select_opposite);
		btn_delete_record = (Button) veiw.findViewById(R.id.btn_delete_record);
		btn_delete_record2 = (Button) veiw.findViewById(R.id.btn_delete_record2);
		btn_select_all.setOnClickListener(this);
		btn_select_opposite.setOnClickListener(this);
		btn_delete_record.setOnClickListener(this);
		btn_delete_record2.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		String string = getResources().getString(R.string.clear);
		String cacheDir = new File(mActivity.getCacheDir().getParent()).getParent();
		String externalCacheDir = new File(mActivity.getExternalCacheDir().getParent()).getParent();
		btn_delete_record.setText(string + ":" + cacheDir);
		btn_delete_record2.setText(string + ":" + externalCacheDir);

	}

	// 获取手机浏览器信息
	public void getBrowserApp() {

		String default_browser = "android.intent.category.DEFAULT";
		String browsable = "android.intent.category.BROWSABLE";
		String view = "android.intent.action.VIEW";

		Intent intent = new Intent(view);
		intent.addCategory(default_browser);
		intent.addCategory(browsable);
		Uri uri = Uri.parse("http://");
		intent.setDataAndType(uri, null);

		// 找出手机当前安装的所有浏览器程序
		List<ResolveInfo> resolveInfoList = mActivity.getPackageManager().queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
		mBrowerList = new ArrayList<AppInfosMode>();
		try {
			for (ResolveInfo resolveInfo : resolveInfoList) {
				AppInfosMode browerMode = new AppInfosMode();
				ActivityInfo activityInfo = resolveInfo.activityInfo;
				String packageName = activityInfo.packageName;
				String className = activityInfo.name;

				PackageManager pm = mActivity.getPackageManager();
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
				browerMode.setClassName(className);
				browerMode.setPackageInfo(packageInfo);
				browerMode.setIscheck(mLocalBrowserMap.containsKey(packageInfo.packageName));
				mBrowerList.add(browerMode);
			}
			if (mBrowerList.size() > 0) {
				mClearBrowerAdapter = new AppInfosAdapter(mActivity, mBrowerList, false, true);
				lv_list.setAdapter(mClearBrowerAdapter);
			}
		} catch (Exception e) {
		}

	}

	int clearCount;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			ArrayList<String> arrayList = AppInfosUtil.getAvailablePackageNameList(mBrowerList);
			boolean putBrowser;
			if (arrayList.size() > 0) {
				HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
				hashMap.put(DataListMode.DATA_LIST, arrayList);
				Gson gson = new Gson();
				String json = gson.toJson(hashMap);
				putBrowser = SPrefUtil.putSettingStr(mActivity, json, SPrefUtil.KEY_BROWSER);
			} else {
				putBrowser = SPrefUtil.putSettingStr(mActivity, "", SPrefUtil.KEY_BROWSER);
			}
			ToastUtil.saveResult(putBrowser, mActivity);
			break;
		case R.id.btn_select_all:
			for (AppInfosMode browerMode : mBrowerList) {
				browerMode.setIscheck(true);
				mClearBrowerAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.btn_select_opposite:
			for (AppInfosMode browerMode : mBrowerList) {
				boolean ischeck = browerMode.isIscheck();
				browerMode.setIscheck(!ischeck);
				mClearBrowerAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.btn_delete_record2:
			if (mBrowerList.size() > 0) {
				final ArrayList<AppInfosMode> availableData = AppInfosUtil.getAvailableData(mBrowerList);
				clearCount = availableData.size();
				if (clearCount == 0) {
					Toast.makeText(mActivity, getResources().getString(R.string.please_select_browser),
							Toast.LENGTH_SHORT).show();
					return;
				}
				ProgressDialogUtil pdU = new ProgressDialogUtil(mActivity);
				final ProgressDialog pd = pdU.show(getResources().getString(R.string.affirm));
				clear_result = new SpannableStringBuilder();
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (AppInfosMode browerMode : availableData) {
							PackageInfo packageInfo = browerMode.getPackageInfo();
							String packageName = packageInfo.packageName;
							clearExternalCache(packageName, pd);
						}
					}
				}).start();
			}

			break;
		case R.id.btn_delete_record:
			if (mBrowerList.size() > 0) {
				AppInfosMode browerMode = mBrowerList.get(0);
				PackageInfo packageInfo = browerMode.getPackageInfo();
				try {
					// ApplicationInfo applicationInfo =
					// mActivity.getPackageManager()
					// .getApplicationInfo(packageInfo.packageName,
					// PackageManager.GET_META_DATA);
					// Intent intent = new Intent(Intent.ACTION_DEFAULT);
					// intent.setClassName(applicationInfo.packageName,
					// applicationInfo.manageSpaceActivityName);
					// startActivityForResult(intent, -1);
					String packageName = packageInfo.packageName;
					// clearCache(packageName);
					File dataDirectory = Environment.getDataDirectory();
					File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
					File externalStorageDirectory = Environment.getExternalStorageDirectory();
					File rootDirectory = Environment.getRootDirectory();
					File cacheDir = mActivity.getCacheDir();
					File externalCacheDir = mActivity.getExternalCacheDir();
					File filesDir = mActivity.getFilesDir();

					// Log.v(GlobalConstant.MYTAG, "---dataDirectory---" +
					// dataDirectory);
					// Log.v(GlobalConstant.MYTAG,
					// "---downloadCacheDirectory---" + downloadCacheDirectory);
					// Log.v(GlobalConstant.MYTAG,
					// "---externalStorageDirectory---" +
					// externalStorageDirectory);
					// Log.v(GlobalConstant.MYTAG, "---rootDirectory---" +
					// rootDirectory);
					// Log.v(GlobalConstant.MYTAG, "---cacheDir---" + cacheDir);
					// Log.v(GlobalConstant.MYTAG, "---externalCacheDir---" +
					// externalCacheDir);
					// Log.v(GlobalConstant.MYTAG, "---filesDir---" + filesDir);
				} catch (Exception e) {
					e.printStackTrace();
					Log.v(GlobalConstant.MYTAG, "---Exception22---" + e.toString());
				}

			}
			break;

		default:
			break;
		}
	}

	CharSequence clear_ok, clear_fail;

	private void initString() {
		clear_ok = StringUtil.SpanColor(getResources().getString(R.string.clear_ok), Color.GREEN);
		clear_fail = StringUtil.SpanColor(getResources().getString(R.string.clear_fail), Color.GREEN);
	}

	SpannableStringBuilder clear_result = new SpannableStringBuilder();

	private void clearExternalCache(String packageName, final ProgressDialog pd) {
		String externalCacheDir = new File(mActivity.getExternalCacheDir().getParent()).getParent() + File.separator
				+ packageName;
		final File file = new File(externalCacheDir);
		boolean deleteFilesByDirectory2 = DataCleanManager.deleteFilesByDirectory2(file);
		CharSequence spanColor;
		boolean str_empty = TextUtils.isEmpty(clear_result.toString());
		if (deleteFilesByDirectory2) {
			if (str_empty) {
				spanColor = StringUtil.SpanColor(packageName + ":" + clear_ok, Color.GREEN);
			} else {
				spanColor = StringUtil.SpanColor("\n" + packageName + ":" + clear_ok, Color.GREEN);
			}

		} else {
			if (str_empty) {
				spanColor = StringUtil.SpanColor(packageName + ":" + clear_fail, Color.RED);
			} else {
				spanColor = StringUtil.SpanColor("\n" + packageName + ":" + clear_fail, Color.RED);
			}
		}

		clear_result.append(spanColor);
		clearCount--;
		if (clearCount == 0) {
			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					pd.dismiss();
					Toast.makeText(mActivity, clear_result, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
