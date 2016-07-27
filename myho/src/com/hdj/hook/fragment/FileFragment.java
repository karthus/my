package com.hdj.hook.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.adapter.FileAdapter;
import com.hdj.hook.mode.DataListMode;
import com.hdj.hook.mode.FileMode;
import com.hdj.hook.util.DataCleanManager;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.SoftInput;
import com.hdj.hook.util.ToastUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FileFragment extends BaseFragment implements OnClickListener {

	private ListView mLvList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mMainActivity, R.layout.activity_task_manager, null);
		findViewById(view);
		getLocalData();
		getData();
		setListener();
		return view;
	}

	HashMap<String, Integer> mLocalMap = new HashMap<String, Integer>();
	HashMap<String, Integer> mDataMap = new HashMap<String, Integer>();

	private void getLocalData() {
		String file_str = SPrefUtil.getSettingStr(mMainActivity, SPrefUtil.KEY_FILE);
		if (!TextUtils.isEmpty(file_str)) {
			Gson gson = new Gson();
			ArrayList<String> list = gson.fromJson(file_str, DataListMode.class).getDataList();
			for (String string : list) {
				FileMode fileMode = new FileMode();
				fileMode.setCheck(true);
				fileMode.setName(string);
				addNeedDeleteFileList(fileMode);
			}
		}
	}

	public static void deletePointFile(Context context) {
		String file_str = SPrefUtil.getSettingStr(context, SPrefUtil.KEY_FILE);
		ArrayList<String> list = new ArrayList<>();
		if (!TextUtils.isEmpty(file_str)) {
			Gson gson = new Gson();
			list = gson.fromJson(file_str, DataListMode.class).getDataList();
		}
		if (list.size() > 0) {
			File file;
			for (String string : list) {
				file = new File(Environment.getExternalStorageDirectory(), string);
				boolean isDelete = DataCleanManager.deleteFilesByDirectory2(file);
				if (isDelete) {
					AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.delete_fold)
							+ file.getName() + "\n" + context.getResources().getString(R.string.opera_ok), context);
				} else {
					AutoActivity.sendHandlerRedOnMain(context.getResources().getString(R.string.delete_fold)
							+ file.getName() + "\n" + context.getResources().getString(R.string.opera_fail), context);
				}

			}

		} else {
			AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.no_point_delete_folder),
					context);
		}
	}

	private ArrayList<String> getStringData(ArrayList<FileMode> dataList) {
		ArrayList<String> list = new ArrayList<String>();
		for (FileMode fileMode : dataList) {
			list.add(fileMode.getName());
		}
		return list;
	}

	private boolean saveData(FileMode fileMode, boolean isAdd, int pos) {
		boolean putData = false;
		ArrayList<FileMode> list = new ArrayList<>();
		list.addAll(mProtectDataList);
		if (isAdd) {
			list.add(fileMode);
		} else {
			list.remove(pos);
		}
		ArrayList<String> strList = getStringData(list);
		if (list.size() > 0) {
			Gson gson = new Gson();
			HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
			hashMap.put(DataListMode.DATA_LIST, strList);
			String json = gson.toJson(hashMap);
			putData = SPrefUtil.putSettingStr(mMainActivity, json, SPrefUtil.KEY_FILE);
		} else {
			putData = SPrefUtil.putSettingStr(mMainActivity, "", SPrefUtil.KEY_FILE);
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
				if (mIsTask) {
					if (currentTimeMillis - lastTaskTime < 300 && lastTaskPos == position) {
						addProtectData(position, view);
					}
					lastTaskPos = position;
					lastTaskTime = currentTimeMillis;
				} else {
					if (currentTimeMillis - lastProtectTime < 300 && lastProtectPos == position) {
						removeProtectData(position, view);
					}
					lastProtectPos = position;
					lastProtectTime = currentTimeMillis;
				}
			}
		});
		tv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLock)
					return;
				isLock = true;
				String string = et_et.getText().toString();
				if (TextUtils.isEmpty(string)) {
					ToastUtil.show(mMainActivity, getResources().getString(R.string.please_input_folder));

				} else if (mLocalMap.containsKey(string)) {
					ToastUtil.show(mMainActivity, getResources().getString(R.string.folder_exist));
				} else {
					FileMode fileMode = new FileMode();
					fileMode.setName(string);
					boolean saveData = saveData(fileMode, true, -1);
					if (saveData) {
						fileMode.setCheck(true);
						addNeedDeleteFileList(fileMode);
						removeDataFileList(fileMode);
						sortList(mProtectDataList);
						mFileAdapter.notifyDataSetChanged();
						et_et.setText("");
						SoftInput.toHide(mMainActivity);
					} else {
						ToastUtil.show(mMainActivity, getResources().getString(R.string.add_fail));
					}
				}
				isLock = false;
			}
		});

	}

	private void addNeedDeleteFileList(FileMode fileMode) {
		mProtectDataList.add(fileMode);
		mLocalMap.put(fileMode.getName(), 1);
	}

	private void removeNeedDeleteFileList(FileMode fileMode, int position) {
		mProtectDataList.remove(position);
		mLocalMap.remove(fileMode.getName());
	}

	private void addDataFileList(FileMode fileMode) {
		mDataList.add(fileMode);
		String name = fileMode.getName();
		mDataMap.put(name, 1);
	}

	private void addDataFileList2(FileMode fileMode) {
		String name = fileMode.getName();
		if (mDataMap.containsKey(name)) {
			return;
		}
		File file = new File(mStorageDirectory, fileMode.getName());
		if (!file.exists() && !file.isDirectory())
			return;
		mDataList.add(fileMode);
		mDataMap.put(fileMode.getName(), 1);
	}

	private void removeDataFileList(FileMode fileMode, int position) {
		mDataList.remove(position);
		mDataMap.remove(fileMode.getName());
	}

	private void removeDataFileList(FileMode fileMode) {
		String name = fileMode.getName();
		if (mDataMap.containsKey(name)) {
			for (int i = 0; i < mDataList.size(); i++) {
				String name2 = mDataList.get(i).getName();
				if (name.equals(name2)) {
					mDataList.remove(i);
					mDataMap.remove(name);
					return;
				}
			}
		}
	}

	private void addProtectData(final int position, View view) {
		isLock = true;
		final FileMode fileMode = mDataList.get(position);
		boolean saveData = saveData(fileMode, true, position);
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
					fileMode.setCheck(true);
					addNeedDeleteFileList(fileMode);
					removeDataFileList(fileMode, position);
					mFileAdapter.notifyDataSetChanged();
					isLock = false;
				}
			}, 700);
		} else {
			isLock = false;
		}
	}

	private void removeProtectData(final int position, View view) {
		isLock = true;
		final FileMode fileMode = mProtectDataList.get(position);
		boolean saveData = saveData(fileMode, false, position);
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
					fileMode.setCheck(false);
					addDataFileList2(fileMode);
					removeNeedDeleteFileList(fileMode, position);
					mFileAdapter.notifyDataSetChanged();
					isLock = false;
				}
			}, 700);
		} else {
			isLock = false;
		}
	}

	ArrayList<FileMode> mProtectDataList = new ArrayList<FileMode>();
	ArrayList<FileMode> mDataList = new ArrayList<FileMode>();
	private FileAdapter mFileAdapter;

	private void getData() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File[] files = Environment.getExternalStorageDirectory().listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					FileMode fileMode = new FileMode();
					String name = file.getName();
					fileMode.setName(name);
					if (!mLocalMap.containsKey(name)) {
						addDataFileList(fileMode);
					}
				}
			}
			sortList(mProtectDataList);
			mFileAdapter = new FileAdapter(mMainActivity, mProtectDataList);
			mLvList.setAdapter(mFileAdapter);
		}
	}

	private void sortList(List<FileMode> list) {
		Collections.sort(list, new Comparator<FileMode>() {
			@Override
			public int compare(FileMode lhs, FileMode rhs) {
				String lhs_name = lhs.getName().toLowerCase();
				String rhs_name = rhs.getName().toLowerCase();
				return lhs_name.compareTo(rhs_name);
			}
		});
	}

	Button btn_task;
	Button btn_protect;
	LinearLayout ll_add;
	EditText et_et;
	TextView tv_explain, tv_add;
	String mStorageDirectory;

	private void findViewById(View view) {
		tv_explain = (TextView) view.findViewById(R.id.tv_explain);
		tv_add = (TextView) view.findViewById(R.id.tv_add);
		et_et = (EditText) view.findViewById(R.id.et_et);
		mLvList = (ListView) view.findViewById(R.id.lv_list);
		ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
		btn_task = (Button) view.findViewById(R.id.btn_task);
		btn_protect = (Button) view.findViewById(R.id.btn_protect);
		btn_task.setOnClickListener(this);
		btn_protect.setOnClickListener(this);
		btn_protect.setSelected(true);
		btn_protect.setText(getResources().getString(R.string.need_delete_fold));
		btn_task.setText(getResources().getString(R.string.fold));
		mStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
		tv_explain.setText(mStorageDirectory);
		tv_explain.setVisibility(View.VISIBLE);
		ll_add.setVisibility(View.VISIBLE);
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
			sortList(mDataList);
			mFileAdapter.setData(mDataList);
			mFileAdapter.notifyDataSetChanged();
			ll_add.setVisibility(View.GONE);
		} else {
			btn_task.setSelected(false);
			btn_protect.setSelected(true);
			sortList(mProtectDataList);
			mFileAdapter.setData(mProtectDataList);
			mFileAdapter.notifyDataSetChanged();
			ll_add.setVisibility(View.VISIBLE);
		}

	}

}
