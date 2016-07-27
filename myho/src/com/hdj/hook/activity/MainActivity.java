package com.hdj.hook.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.fragment.LeftFragment;
import com.hdj.hook.mode.DataListMode;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.RootUtil;
import com.hdj.hook.util.SPrefUtil;
import com.hdj.hook.util.ToastUtil;
import com.hdj.hook.vpn.PPTPVPNService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mz.iplocation.service.SilentInstallService;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends SlidingFragmentActivity {

	public FragmentManager fm;
	public HookFragment mHookFragment;
	public SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// startActivity(new Intent(MainActivity.this, LoginActivity.class));
		// overridePendingTransition(android.R.anim.fade_in,
		// android.R.anim.fade_out);

		setContentView(R.layout.activity_main);
		startService(new Intent(MainActivity.this, SilentInstallService.class));
		PPTPVPNService.startVpnService(MainActivity.this);

		initSlidingMenu();
		initFragment();

		if (!RootUtil.isRoot()) {
			ToastUtil.showL(MainActivity.this, getResources().getString(R.string.no_root));
			return;
		}
		initString();
		getInputMethod();
	}

	private void getInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> inputMethodList = imm.getInputMethodList();
		ArrayList<String> arrayList = new ArrayList<String>();
		for (InputMethodInfo inputMethodInfo : inputMethodList) {
			String packageName = inputMethodInfo.getPackageName();
			arrayList.add(packageName);
		}
		if (arrayList.size() > 0) {
			HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
			hashMap.put(DataListMode.DATA_LIST, arrayList);
			Gson gson = new Gson();
			String json = gson.toJson(hashMap);
			boolean putData = SPrefUtil.putSettingStr(MainActivity.this, json, SPrefUtil.KEY_INPUT_METHOD);
		}

	}

	private void initString() {
		String string = getResources().getString(R.string.hook_ui);
		setLeft_str(string);

	}

	private void initFragment() {
		fm = getFragmentManager();
		mHookFragment = new HookFragment();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fl_main, mHookFragment);
		ft.replace(R.id.fl_empty, new LeftFragment());
		ft.commit();
		setCurrentFragment(mHookFragment);
	}

	private Fragment currentFragment;

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	public void setCurrentFragment(Fragment currentFragment) {
		this.currentFragment = currentFragment;
	}

	private void initSlidingMenu() {
		View view = View.inflate(MainActivity.this, R.layout.fragment_empty, null);
		setBehindContentView(view);
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindWidth(3 * width / 4);
	}

	public String left_str;

	public String getLeft_str() {
		return left_str;
	}

	public void setLeft_str(String left_str) {
		this.left_str = left_str;
	}

	@Override
	protected void onStart() {
		if (getResources().getString(R.string.hook_ui).equals(left_str)) {
			Intent intent = new Intent(MainActivity.this, AutoActivity.class);
			startActivity(intent);
		}
		super.onStart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
