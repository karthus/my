package com.hdj.hook.fragment;

import com.hdj.hook.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public abstract class BaseFragment extends Fragment {

	protected MainActivity mMainActivity;
	protected FragmentManager mFm;
	protected SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) getActivity();
		mFm = mMainActivity.fm;
		mSlidingMenu = mMainActivity.slidingMenu;
	}

}
