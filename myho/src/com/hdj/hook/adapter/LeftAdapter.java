package com.hdj.hook.adapter;

import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.fragment.LeftFragment;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.SPrefUtil;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class LeftAdapter extends BaseAdapter {

	LeftFragment mLeftFragment;
	private ArrayList<String> mStrList, mSwitchStrList;
	Activity activity;

	public LeftAdapter(LeftFragment mLeftFragment) {
		this.mLeftFragment = mLeftFragment;
		mStrList = mLeftFragment.mStrList;
		mSwitchStrList = mLeftFragment.mSwitchStrList;
		activity = mLeftFragment.getActivity();
	}

	@Override
	public int getCount() {
		return mStrList.size() + mSwitchStrList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mStrList.size())
			return TYPE_STR;
		return TYPE_SWITCH_STR;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public static final int TYPE_STR = 0;
	public static final int TYPE_SWITCH_STR = 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderStr holderStr = null;
		HolderSwitchStr holderSwitchStr = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_STR:
				holderStr = new HolderStr();
				convertView = View.inflate(activity, R.layout.adapter_left, null);
				holderStr.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
				convertView.setTag(holderStr);
				break;
			case TYPE_SWITCH_STR:
				holderSwitchStr = new HolderSwitchStr();
				convertView = View.inflate(activity, R.layout.adapter_left_switch, null);
				holderSwitchStr.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
				holderSwitchStr.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
				convertView.setTag(holderSwitchStr);
				break;
			default:
				break;
			}

		} else {
			switch (type) {
			case TYPE_STR:
				holderStr = (HolderStr) convertView.getTag();
				break;
			case TYPE_SWITCH_STR:
				holderSwitchStr = (HolderSwitchStr) convertView.getTag();
				break;
			default:
				break;
			}

		}
		String str = null;
		switch (type) {
		case TYPE_STR:
			str = mStrList.get(position);
			holderStr.tv_text.setText(str);
			break;
		case TYPE_SWITCH_STR:
			str = mSwitchStrList.get(position - mStrList.size());
			holderSwitchStr.tv_text.setText(str);
			boolean hookBoolean = false;
			if (mLeftFragment.mStr_modify_wifi_list.equals(str)) {
				hookBoolean = SPrefUtil.getSettingBoolean(activity, SPrefUtil.KEY_MODIFY_WIFI_LIST);
			} else if (mLeftFragment.mStr_modify_density.equals(str)) {
				hookBoolean = SPrefUtil.getSettingBoolean(activity, SPrefUtil.KEY_MODIFY_DENSITY);
			} else if (mLeftFragment.mStr_modify_global.equals(str)) {
				hookBoolean = SPrefUtil.getSettingBooleanDefaultTrue(activity, SPrefUtil.KEY_MODIFY_GLOBAL);
			} else if (mLeftFragment.mStr_modify_test.equals(str)) {
				hookBoolean = SPrefUtil.getSettingBoolean(activity, SPrefUtil.KEY_MODIFY_TEST);
			}else if (mLeftFragment.mStr_modify_after_opera_auto_open_app.equals(str)) {
				hookBoolean = SPrefUtil.getSettingBooleanDefaultTrue(activity, SPrefUtil.KEY_MODIFY_AFTER_OPERA_AUTO_OPEN_APP);
			}
			holderSwitchStr.cb_checkbox.setChecked(hookBoolean);
			break;
		default:
			break;
		}

		return convertView;
	}

	class HolderStr {
		TextView tv_text;
	}

	class HolderSwitchStr {
		TextView tv_text;
		CheckBox cb_checkbox;
	}

}
