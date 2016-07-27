package com.hdj.hook.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.RecordToFile;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.activity.SelectAppActivity;
import com.hdj.hook.adapter.FileRecordAdapter;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.MD5;
import com.hdj.hook.util.ProgressDialogUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.StreamTextUtils;
import com.hdj.hook.util.StringUtil;
import com.hdj.hook.util.ToastUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ListnerFileFragment extends Fragment implements OnClickListener {

	private Activity mActivity;
	private FrameLayout fl_frame;
	// private TextView tv_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		initReceiver();
	}

	private void setBtnRefresh(boolean isTvRefresh) {
		if (isTvRefresh) {
			btn_refresh.setText(getResources().getString(R.string.refresh_record));
		} else {
			btn_refresh.setText(getResources().getString(R.string.look_record));
		}
	}

	ListnerAppReceiver mReceiver;

	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GlobalConstant.ACTION_LISTNER_APP);
		mReceiver = new ListnerAppReceiver();
		mActivity.registerReceiver(mReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		if (mReceiver != null)
			mActivity.unregisterReceiver(mReceiver);
		super.onDestroy();
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
				reSetData();
				// new Thread(new Runnable() {
				// public void run() {
				// mActivity.runOnUiThread(new Runnable() {
				// public void run() {
				// if (new_packageName.equals(packageName)) {
				// getData(packageName);
				// } else {
				// packageName = new_packageName;
				// getDataForce(packageName);
				// }
				// }
				// });
				//
				// }
				// }).start();
			} else if (GlobalConstant.FLAG_CLEAR_LISTNER_APP.equals(flag)) {
				fl_frame.removeAllViews();
				reSetData();
			}

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_listner_file, null);
		findViewById(view);
		packageName = SPrefUtil.getSettingStr(mActivity, GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
		if (!TextUtils.isEmpty(packageName)) {
			PackageManager pm = mActivity.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
				View appInfoView = SelectAppActivity.getAppInfoView(packageInfo, mActivity.getPackageManager(),
						mActivity);
				fl_frame.removeAllViews();
				fl_frame.addView(appInfoView);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			// getData(packageName);
		}

		return view;

	}

	FileInfo fileInfo;

	private void getData(String packageName) {

		if (TextUtils.isEmpty(packageName)) {
			ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.has_no_app_listen));
			return;
		}

		final File file = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_FILE_PATH), packageName);
		if (file.exists()) {
			if (fileInfo == null)
				fileInfo = new FileInfo();
			String new_md5sum = MD5.md5sum(file.getAbsolutePath());
			// if (new_md5sum.equals(fileInfo.str_md5)) {
			// Toast.makeText(mActivity,
			// mActivity.getResources().getString(R.string.no_new_data),
			// Toast.LENGTH_SHORT)
			// .show();
			// return;
			// }
			fileInfo.str_md5 = new_md5sum;
			ProgressDialogUtil pdU = new ProgressDialogUtil(mActivity);
			final ProgressDialog pd = pdU.show(mActivity.getResources().getString(R.string.getting_data));
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(500);
						final FileInputStream is = new FileInputStream(file);
						// final String text =
						// StreamTextUtils.getTextFromStream(is);
						final ArrayList<String> textList = StreamTextUtils.getTextListFromStream2(is);
						mActivity.runOnUiThread(new Runnable() {
							public void run() {
								if (textList.size() > 0) {
									mFileRecordAdapter.setData(textList);
									lv_list.setSelection(textList.size());
									setBtnRefresh(true);
								} else {
									reSetData();
									Toast.makeText(mActivity, mActivity.getResources().getString(R.string.no_data),
											Toast.LENGTH_SHORT).show();
								}
								// if (TextUtils.isEmpty(text)) {
								// reSetData();
								// Toast.makeText(mActivity,
								// mActivity.getResources().getString(R.string.no_data),
								// Toast.LENGTH_SHORT).show();
								// } else {
								// setBtnRefresh(false);
								// tv_text.setText(text);
								// tv_text.scrollTo(TRIM_MEMORY_COMPLETE,
								// TRIM_MEMORY_BACKGROUND);
								// sv_scroll.post(new Runnable() {
								// public void run() {
								// sv_scroll.scrollTo(0, tv_text.getBottom());
								//
								// }
								// });
								// }
								pd.dismiss();
							}
						});
					} catch (Exception e) {
						pd.dismiss();
					}

				}
			}).start();

		} else {
			reSetData();
			Toast.makeText(mActivity, mActivity.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
		}
	}

	public static void deleteFileDataAuto(String packageName, final Context context) {
		final File file5 = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_FILE_PATH), packageName);
		if (file5.exists()) {
			try {
				final FileInputStream is = new FileInputStream(file5);
				final ArrayList<String> allList = StreamTextUtils.getTextListFromStream2(is);
				ArrayList<String> fileList = new ArrayList<>();
				ArrayList<String> folderList = new ArrayList<>();
				File file;
				for (String string : allList) {
					if (!TextUtils.isEmpty(string) && !string.equals(GlobalConstant.NOT_DELETE_FILE)) {
						file = new File(string);
						if (!file.exists())
							continue;
						if (file.isDirectory()) {
							folderList.add(string);
						} else {
							fileList.add(string);
						}
					}
				}
				allList.removeAll(allList);
				AutoActivity.sendHandlerOnMain(
						context.getResources().getString(R.string.need_delete_file_count) + fileList.size(), context);
				int has_delete_file_count = 0;
				if (fileList.size() > 0) {
					for (String string2 : fileList) {
						file = new File(string2);
						if (file.exists() && file.isFile()) {
							boolean delete = file.delete();
							if (delete) {
								has_delete_file_count++;
							} else {
								allList.add(string2);
							}
						}

					}
					AutoActivity.sendHandlerOnMain(
							context.getResources().getString(R.string.has_delete_file_count) + has_delete_file_count,
							context);
					int not_delete_file_count = fileList.size() - has_delete_file_count;
					if (not_delete_file_count > 0) {
						AutoActivity.sendHandlerRedOnMain(
								context.getResources().getString(R.string.has_not_delete_file_count)
										+ (fileList.size() - has_delete_file_count),
								context);
					}

				}
				int has_delete_folder_count = 0;
				AutoActivity.sendHandlerOnMain(
						context.getResources().getString(R.string.need_delete_folder_count) + folderList.size(),
						context);
				if (folderList.size() > 0) {
					for (String string2 : folderList) {
						file = new File(string2);
						if (file.exists() && file.isDirectory()) {
							boolean delete = file.delete();
							if (delete) {
								has_delete_folder_count++;
							} else {
								allList.add(string2);
							}
						}
					}

					AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.has_delete_folder_count)
							+ has_delete_folder_count, context);

					int not_delete_folder_count = folderList.size() - has_delete_folder_count;
					if (not_delete_folder_count > 0) {
						AutoActivity.sendHandlerRedOnMain(
								context.getResources().getString(R.string.has_not_delete_folder_count)
										+ (folderList.size() - has_delete_folder_count),
								context);
					}
				}

				boolean deleteRecord = deleteRecord(context, packageName);
				if (deleteRecord) {
					RecordToFile.clearMap();
					AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.delete_file_record)
							+ context.getResources().getString(R.string.opera_ok), context);
				} else {
					AutoActivity.sendHandlerRedOnMain(context.getResources().getString(R.string.delete_file_record)
							+ context.getResources().getString(R.string.opera_fail), context);
				}
			} catch (Exception e) {
			}

		} else {
			AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.no_file_to_delete), context);
		}
	}

	public void deleteFileData() {
		final File file5 = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_FILE_PATH), packageName);
		StringBuffer s = new StringBuffer();
		s.append("\n");
		if (file5.exists()) {
			try {
				final FileInputStream is = new FileInputStream(file5);
				final ArrayList<String> allList = StreamTextUtils.getTextListFromStream2(is);
				ArrayList<String> fileList = new ArrayList<>();
				ArrayList<String> folderList = new ArrayList<>();
				File file;
				for (String string : allList) {
					if (!TextUtils.isEmpty(string) && !string.equals(GlobalConstant.NOT_DELETE_FILE)) {
						file = new File(string);
						if (!file.exists())
							continue;
						if (file.isDirectory()) {
							folderList.add(string);
						} else {
							fileList.add(string);
						}
					}
				}
				allList.removeAll(allList);
				s.append(StringUtil.SpanColor(
						mActivity.getResources().getString(R.string.need_delete_file_count) + fileList.size(),
						Color.BLACK)).append("\n");
				int has_delete_file_count = 0;
				if (fileList.size() > 0) {
					for (String string2 : fileList) {
						file = new File(string2);
						if (file.exists() && file.isFile()) {
							boolean delete = file.delete();
							if (delete) {
								has_delete_file_count++;
							} else {
								allList.add(string2);
							}
						}
					}
					s.append(StringUtil.SpanColor(
							mActivity.getResources().getString(R.string.has_delete_file_count) + has_delete_file_count,
							Color.BLACK)).append("\n");
					int not_delete_file_count = fileList.size() - has_delete_file_count;
					if (not_delete_file_count > 0) {
						s.append(StringUtil
								.SpanColor(mActivity.getResources().getString(R.string.has_not_delete_file_count)
										+ (fileList.size() - has_delete_file_count), Color.RED))
								.append("\n");
					}

				}
				int has_delete_folder_count = 0;
				s.append(StringUtil.SpanColor(
						mActivity.getResources().getString(R.string.need_delete_folder_count) + folderList.size(),
						Color.BLACK)).append("\n");
				if (folderList.size() > 0) {
					for (String string2 : folderList) {
						file = new File(string2);
						if (file.exists() && file.isDirectory()) {
							boolean delete = file.delete();
							if (delete) {
								has_delete_folder_count++;
							} else {
								allList.add(string2);
							}
						}
					}
					s.append(StringUtil.SpanColor(mActivity.getResources().getString(R.string.has_delete_folder_count)
							+ has_delete_folder_count, Color.BLACK)).append("\n");

					int not_delete_folder_count = folderList.size() - has_delete_folder_count;
					if (not_delete_folder_count > 0) {
						s.append(StringUtil
								.SpanColor(mActivity.getResources().getString(R.string.has_not_delete_folder_count)
										+ (folderList.size() - has_delete_folder_count), Color.RED))
								.append("\n");
					}
				}

				boolean deleteRecord = deleteRecord(mActivity, packageName);
				if (deleteRecord) {
					RecordToFile.clearMap();
					s.append(StringUtil.SpanColor(mActivity.getResources().getString(R.string.delete_file_record)
							+ mActivity.getResources().getString(R.string.opera_ok), Color.BLACK)).append("\n");
				} else {
					s.append(StringUtil.SpanColor(mActivity.getResources().getString(R.string.delete_file_record)
							+ mActivity.getResources().getString(R.string.opera_fail), Color.RED)).append("\n");
				}
			} catch (Exception e) {
			}

		} else {
			s.append(StringUtil.SpanColor(mActivity.getResources().getString(R.string.no_file_to_delete), Color.BLACK))
					.append("\n");
		}
		toToastDleteResult(s);
	}

	private void toToastDleteResult(final StringBuffer s) {
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.showL(mActivity, s);
				mFileRecordAdapter.clearData();
			}
		});
	}

	class FileInfo {
		String str_md5;
		Long fileLong;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	// ScrollView sv_scroll;
	private String packageName;
	private Button btn_refresh;
	ListView lv_list;
	private FileRecordAdapter mFileRecordAdapter;

	private void findViewById(View view) {
		Button btn_select_app = (Button) view.findViewById(R.id.btn_select_app);
		Button btn_delete_file = (Button) view.findViewById(R.id.btn_delete_file);
		btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
		lv_list = (ListView) view.findViewById(R.id.lv_list);
		Button btn_delete_record = (Button) view.findViewById(R.id.btn_delete_record);
		// tv_text = (TextView) view.findViewById(R.id.tv_text);
		// sv_scroll = (ScrollView) view.findViewById(R.id.sv_scroll);
		fl_frame = (FrameLayout) view.findViewById(R.id.fl_frame);
		btn_select_app.setOnClickListener(this);
		btn_delete_record.setOnClickListener(this);
		btn_delete_file.setOnClickListener(this);
		btn_refresh.setOnClickListener(this);
		setBtnRefresh(false);
		mFileRecordAdapter = new FileRecordAdapter(mActivity);
		lv_list.setAdapter(mFileRecordAdapter);
	}

	private void reSetData() {
		setBtnRefresh(false);
		mFileRecordAdapter.clearData();
		// tv_text.setText("");
		fileInfo = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select_app:
			startActivity(new Intent(mActivity, SelectAppActivity.class));
			break;
		case R.id.btn_delete_file:
			deleteFileData();
			break;
		case R.id.btn_refresh:
			getData(packageName);
			break;
		case R.id.btn_delete_record:
			final File file = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_FILE_PATH), packageName);
			if (file.exists() && file.isFile()) {
				ProgressDialogUtil pdU = new ProgressDialogUtil(mActivity);
				final ProgressDialog pd = pdU.show(mActivity.getResources().getString(R.string.deleting_record));
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(500);
							for (int i = 0; i < 10; i++) {
								boolean delete = file.delete();
								if (delete) {
									pd.dismiss();
									mActivity.runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(mActivity,
													getResources().getString(R.string.delete_record_ok),
													Toast.LENGTH_SHORT).show();
											reSetData();
										}
									});
									return;
								}
								Thread.sleep(1000);
							}
							pd.dismiss();
							deleteFail();
						} catch (InterruptedException e) {
							e.printStackTrace();
							pd.dismiss();
							deleteFail();
						}
					}
				}).start();

			} else {
				Toast.makeText(mActivity, getResources().getString(R.string.recordfile_not_exist), Toast.LENGTH_SHORT)
						.show();
			}
			break;

		default:
			break;
		}
	}

	public static boolean deleteRecord(Context context, String packageName) {
		final File file = new File(RecordToFile.getFoldPath(GlobalConstant.FOLDER_FILE_PATH), packageName);
		Log.v(GlobalConstant.MYTAG, "--deleteRecord--00--" + file.getAbsolutePath());
		boolean delete = false;
		if (file.exists() && file.isFile()) {
			delete = file.delete();
			Log.v(GlobalConstant.MYTAG, "--deleteRecord--11--" + delete);
		} else {
			delete = true;
			Log.v(GlobalConstant.MYTAG, "--deleteRecord--22--" + delete);
		}
		return delete;
	};

	private void deleteFail() {
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(mActivity, getResources().getString(R.string.delete_record_fail), Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

}
