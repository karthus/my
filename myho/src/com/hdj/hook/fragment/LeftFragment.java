package com.hdj.hook.fragment;

import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.adapter.LeftAdapter;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.vpn.VPNFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class LeftFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initString();
		initSwitchString();
		View view = View.inflate(mMainActivity, R.layout.view_left, null);
		findViewById(view);
		setListner();
		return view;
	}

	int lastPost = -1;

	private void setListner() {
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int type = mLeftAdapter.getItemViewType(position);
				boolean checked = false;
				String str = null;
				switch (type) {
				case LeftAdapter.TYPE_STR:
					if (position == lastPost)
						return;
					lastPost = position;
					str = mStrList.get(position);
					mSlidingMenu.toggle();
					break;
				case LeftAdapter.TYPE_SWITCH_STR:
					str = mSwitchStrList.get(position - mStrList.size());
					int nowPosition = position - parent.getFirstVisiblePosition();
					View childView = parent.getChildAt(nowPosition);
					CheckBox cb_checkbox = (CheckBox) childView.findViewById(R.id.cb_checkbox);
					checked = cb_checkbox.isChecked();
					cb_checkbox.setChecked(!checked);
					break;
				default:
					break;
				}
				toChangeFragment(str, checked);
			}
		});

	}

	TaskManagerFragment mTaskManagerFragment;

	protected void toChangeFragment(final String str, final boolean checked) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mMainActivity.runOnUiThread(new Runnable() {
					public void run() {
						mMainActivity.setLeft_str(str);
						if (mStr_hook.equals(str)) {
							replaceFragment(mMainActivity.mHookFragment);
						} else if (mStr_listner_file.equals(str)) {
							replaceFragment(new ListnerFileFragment());
						} else if (mStr_listner_system_settings.equals(str)) {
							replaceFragment(new ListenSystemValueFragment());
						} else if (mStr_hide_packagename.equals(str)) {
							replaceFragment(new TaskManagerFragment(TaskManagerFragment.TYPE_HIDE_PACKAGE_NAME));
						} else if (mStr_liu_chun.equals(str)) {
							replaceFragment(new LiuCunFragment());
						} else if (mStr_clear_brower_data.equals(str)) {
							replaceFragment(new TaskManagerFragment(TaskManagerFragment.TYPE_BROWSER));
						} else if (mStr_delete_file.equals(str)) {
							replaceFragment(new FileFragment());
						} else if (mStr_vpn.equals(str)) {
							replaceFragment(new VPNFragment());
						} else if (mStr_silent_install.equals(str)) {
							replaceFragment(new InstallFragment());
						} else if (mStr_globle_protect.equals(str)) {
							replaceFragment(new TaskManagerFragment(TaskManagerFragment.TYPE_GLOBAL_CHANGE_PROTECT));
						} else if (mStr_listen_market_down.equals(str)) {
							replaceFragment(new TaskManagerFragment(TaskManagerFragment.TYPE_MARKET));
						} else if (mStr_modify_wifi_list.equals(str)) {
							SPrefUtil.putSettingBoolean(mMainActivity, !checked, SPrefUtil.KEY_MODIFY_WIFI_LIST);
						} else if (mStr_modify_density.equals(str)) {
							SPrefUtil.putSettingBoolean(mMainActivity, !checked, SPrefUtil.KEY_MODIFY_DENSITY);
						} else if (mStr_modify_global.equals(str)) {
							SPrefUtil.putSettingBoolean(mMainActivity, !checked, SPrefUtil.KEY_MODIFY_GLOBAL);
						} else if (mStr_modify_test.equals(str)) {
							SPrefUtil.putSettingBoolean(mMainActivity, !checked, SPrefUtil.KEY_MODIFY_TEST);
						} else if (mStr_modify_after_opera_auto_open_app.equals(str)) {
							SPrefUtil.putSettingBoolean(mMainActivity, !checked,
									SPrefUtil.KEY_MODIFY_AFTER_OPERA_AUTO_OPEN_APP);
						}
					}
				});
			}
		}).start();

	}

	public String mStr_hook, mStr_listner_file, mStr_listner_system_settings, mStr_hide_packagename,
			mStr_clear_brower_data, mStr_delete_file, mStr_vpn, mStr_silent_install, mStr_liu_chun, mStr_globle_protect,
			mStr_listen_market_down;
	public ArrayList<String> mStrList, mSwitchStrList;

	private void initString() {
		mStr_hook = getResources().getString(R.string.hook_ui);
		mStr_listner_file = getResources().getString(R.string.listner_app_operate_file);
		mStr_listner_system_settings = getResources().getString(R.string.listner_system_settings);
		mStr_hide_packagename = getResources().getString(R.string.hide_packagename);
		mStr_clear_brower_data = getResources().getString(R.string.clear_brower_data);
		mStr_delete_file = getResources().getString(R.string.delete_folder);
		mStr_vpn = getResources().getString(R.string.vpn);
		mStr_silent_install = getResources().getString(R.string.silent_install);
		mStr_liu_chun = getResources().getString(R.string.liu_chun);
		mStr_globle_protect = getResources().getString(R.string.golbal_change_protect);
		mStr_listen_market_down = getResources().getString(R.string.listen_market_down);
		mStrList = new ArrayList<String>();
		mStrList.add(mStr_hook);
		// mStrList.add(mStr_control_platfrom);
		mStrList.add(mStr_listner_file);
		mStrList.add(mStr_listner_system_settings);
		// mStrList.add(mStr_vpn);
		mStrList.add(mStr_silent_install);
		mStrList.add(mStr_liu_chun);
		mStrList.add(mStr_delete_file);
		mStrList.add(mStr_clear_brower_data);
		mStrList.add(mStr_hide_packagename);
		mStrList.add(mStr_globle_protect);
		// mStrList.add(mStr_listen_market_down);
	}

	public String mStr_modify_wifi_list, mStr_modify_density, mStr_modify_global, mStr_modify_test,
			mStr_modify_after_opera_auto_open_app;

	private void initSwitchString() {
		mStr_modify_wifi_list = getResources().getString(R.string.modify_wifi_list);
		mStr_modify_density = getResources().getString(R.string.modify_density);
		mStr_modify_global = getResources().getString(R.string.modify_global);
		mStr_modify_test = getResources().getString(R.string.modify_test);
		mStr_modify_after_opera_auto_open_app = getResources().getString(R.string.modify_after_opera_auto_open_app);
		mSwitchStrList = new ArrayList<String>();
		// mSwitchStrList.add(mStr_modify_wifi_list);
		mSwitchStrList.add(mStr_modify_density);
		mSwitchStrList.add(mStr_modify_global);
		mSwitchStrList.add(mStr_modify_test);
		mSwitchStrList.add(mStr_modify_after_opera_auto_open_app);
	}

	private void replaceFragment(Fragment fragment) {
		Fragment cFragment = mMainActivity.getCurrentFragment();
		FragmentTransaction ft = mFm.beginTransaction();
		if (cFragment instanceof HookFragment) {
			ft.hide(cFragment);
		} else {
			ft.remove(cFragment);
		}
		if (!fragment.isAdded()) {
			ft.add(R.id.fl_main, fragment);
		}
		ft.show(fragment);
		ft.commit();
		mMainActivity.setCurrentFragment(fragment);
	}

	ListView lv_list;
	private LeftAdapter mLeftAdapter;

	private void findViewById(View view) {
		lv_list = (ListView) view.findViewById(R.id.lv_list);
		mLeftAdapter = new LeftAdapter(LeftFragment.this);
		lv_list.setAdapter(mLeftAdapter);
	}

}
