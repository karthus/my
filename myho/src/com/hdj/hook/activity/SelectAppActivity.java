package com.hdj.hook.activity;

import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.adapter.AppInfosAdapter;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.util.AppInfosUtil;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.SendBroadCastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectAppActivity extends Activity {
	private ListView lv_list;
	public ArrayList<AppInfosMode> mAppList;
	private AppInfosAdapter mSelectAppAdapter;
	public PackageManager mPm;
	Button btn_clear;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_app);
		lv_list = (ListView) findViewById(R.id.lv_list);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		getInstallApp();
		setListner();
		mPm = getPackageManager();
		packageName = SPrefUtil.getSettingStr(SelectAppActivity.this, GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
		if (TextUtils.isEmpty(packageName))
			btn_clear.setVisibility(View.GONE);
	}

	private void setListner() {
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfosMode packageInfoMode = mAppList.get(position);
				showDialog(packageInfoMode);
			}
		});
		btn_clear.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showClearListnerDialog();
			}
		});

	}

	AlertDialog dialog;

	protected void showDialog(final AppInfosMode packageInfoMode) {
		final String new_packageName = AppInfosUtil.getPackageName(packageInfoMode);
		if (new_packageName.equals(packageName)) {
			Toast.makeText(SelectAppActivity.this, getResources().getString(R.string.no_need_to_reListen),
					Toast.LENGTH_SHORT).show();
			return;
		}
		View view = getAppInfoView(packageInfoMode, mPm, SelectAppActivity.this);
		Builder builder = new AlertDialog.Builder(SelectAppActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		dialog = builder.create();
		dialog.setTitle(getResources().getString(R.string.please_affirm_listen_app));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				boolean commit = SPrefUtil.putSettingStr(SelectAppActivity.this, new_packageName,
						GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
				if (commit) {
					Intent intent = new Intent();
					intent.setAction(GlobalConstant.ACTION_LISTNER_APP);
					intent.putExtra(GlobalConstant.FLAG, GlobalConstant.FLAG_NEW_LISTNER_APP);
					Bundle bundle = new Bundle();
					bundle.putParcelable(GlobalConstant.KEY_SELECT_APP, packageInfoMode);
					intent.putExtras(bundle);
					sendBroadcast(intent);
					finish();
				} else {
					Toast.makeText(SelectAppActivity.this, getResources().getString(R.string.opera_fail),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.setView(view);
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		dialog.show();
	}

	AlertDialog dialog2;

	protected void showClearListnerDialog() {
		Builder builder2 = new AlertDialog.Builder(SelectAppActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		dialog2 = builder2.create();
		dialog2.setTitle(getResources().getString(R.string.please_affirm_clear_listen_app));
		dialog2.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.affirm),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean commit = SPrefUtil.putSettingStr(SelectAppActivity.this, "",
								GlobalConstant.KEY_HHOOK_PACKAGE_NAME);
						if (commit) {
							SendBroadCastUtil.listenApp(GlobalConstant.FLAG_CLEAR_LISTNER_APP, SelectAppActivity.this);
							finish();
						} else {
							Toast.makeText(SelectAppActivity.this, getResources().getString(R.string.opera_fail),
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog2.show();
	}

	public static View getAppInfoView(AppInfosMode packageInfoMode, PackageManager mPm, Context context) {
		CharSequence name = AppInfosAdapter.getName(packageInfoMode, mPm);
		View view = View.inflate(context, R.layout.adapter_select_app, null);
		ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
		TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
		tv_text.setText(name);
		AppInfosAdapter.setImage(iv_image, packageInfoMode, mPm);
		return view;
	}

	public static View getAppInfoView(PackageInfo packageInfo, PackageManager mPm, Context context) {
		CharSequence name = AppInfosAdapter.getName(packageInfo, mPm);
		View view = View.inflate(context, R.layout.adapter_select_app, null);
		ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
		TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
		tv_text.setText(name);
		AppInfosAdapter.setImage(iv_image, packageInfo, mPm);
		return view;
	}

	private void getInstallApp() {
		mAppList = AppInfosUtil.getInstallApp(SelectAppActivity.this);
		mSelectAppAdapter = new AppInfosAdapter(SelectAppActivity.this, mAppList, true, false);
		lv_list.setAdapter(mSelectAppAdapter);
	}

}
