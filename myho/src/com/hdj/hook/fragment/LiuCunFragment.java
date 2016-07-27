package com.hdj.hook.fragment;

import com.hdj.hook.R;
import com.mz.iplocation.constants.URLConstants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LiuCunFragment extends BaseFragment {
	private LinearLayout seeting;
	private ToggleButton tb_keep;
	private Button saveBtn;
	private EditText keep_et;
	private TextView keep_num, run_keep, open_keep_tip;
	int num;
	SharedPreferences preferences;
	Editor editor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mMainActivity, R.layout.activity_keep_setting, null);
		preferences = mMainActivity.getSharedPreferences(URLConstants.SEETING_KEEP_SP, Context.MODE_PRIVATE);
		editor = preferences.edit();
		initView(view);
		return view;
	}

	private void initView(View view) {
		seeting = (LinearLayout) view.findViewById(R.id.seeting);
		open_keep_tip = (TextView) view.findViewById(R.id.open_keep_tip);
		tb_keep = (ToggleButton) view.findViewById(R.id.tb_keep);
		saveBtn = (Button) view.findViewById(R.id.save_keepsetting);
		keep_et = (EditText) view.findViewById(R.id.keep_et);
		keep_num = (TextView) view.findViewById(R.id.keep_num);
		run_keep = (TextView) view.findViewById(R.id.run_keep);

		num = preferences.getInt("keep_percent", 20);
		int keep_num_ = preferences.getInt("keep_num", 0);
		int run_keep_num_ = preferences.getInt("run_keep_num", 0);
		boolean isChecked = preferences.getBoolean("is_Keep_open", false);
		keep_et.setText(String.valueOf(num));
		keep_num.setText(String.valueOf(keep_num_));
		if (run_keep_num_ > keep_num_) {
			run_keep.setText(String.valueOf(keep_num_));
		} else {
			run_keep.setText(String.valueOf(run_keep_num_));
		}

		tb_keep.setChecked(isChecked);
		if (isChecked) {
			open_keep_tip.setText("留存已开");
			seeting.setVisibility(View.VISIBLE);
		} else {
			open_keep_tip.setText("留存已关");
			seeting.setVisibility(View.GONE);
		}
		tb_keep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tb_keep.isChecked()) {
					open_keep_tip.setText("留存已开");
					seeting.setVisibility(View.VISIBLE);
					editor.putBoolean("is_Keep_open", true).commit();
					//
				} else {
					open_keep_tip.setText("留存已关");
					seeting.setVisibility(View.GONE);
					editor.putBoolean("is_Keep_open", false).commit();
				}

			}
		});
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String num_ = keep_et.getText().toString().trim();
				if (!TextUtils.isEmpty(num_) && isNum(num_)) {
					num = Integer.parseInt(num_);
					editor.putInt("keep_percent", num).commit();
					ShowToast("保存成功");
					Intent intent = new Intent();
					intent.setAction(URLConstants.SET_KEEP_BROADCAST);
					intent.putExtra("change", true);
					mMainActivity.sendBroadcast(intent);
				} else {
					ShowToast("保存失败");
				}
			}
		});
	}

	private boolean isNum(String txt) {
		boolean result = txt.matches("[0-9]+");
		return result;
	}

	/**
	 * show tosat
	 * 
	 * @param str
	 */
	Toast toast;

	private void ShowToast(String str) {
		toast = toast == null ? Toast.makeText(mMainActivity, "", Toast.LENGTH_SHORT) : toast;
		toast.setText(str);
		toast.show();
	}

}
