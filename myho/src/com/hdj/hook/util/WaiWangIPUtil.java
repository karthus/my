package com.hdj.hook.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.hdj.hook.R;
import com.hdj.hook.activity.MainActivity;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.InfosMode;
import com.hdj.hook.util.HttpUtil.ResponseListner;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

public class WaiWangIPUtil {
	private HookFragment mHookFragment;
	private InfosMode mInfosMode;
	private Activity activity;

	public WaiWangIPUtil(HookFragment mainActivity) {
		this.mHookFragment = mainActivity;
		mInfosMode = mainActivity.getmInfosMode();
		activity = mainActivity.getActivity();
	}

	private int tag_count;

	private int getTag_count() {
		return tag_count;
	}

	public void get() {
		tag_count++;
		deal(tag_count);
	}

	private void deal(final int tag_count) {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.post(new ArrayList<NameValuePair>(), GlobalConstant.URL_WAI_WANG_IP);
		httpUtil.setResponseListner(new ResponseListner() {

			@Override
			public void result(String result) {
				Log.v(GlobalConstant.MYTAG, "---IP---" + result);
				int tag_count2 = getTag_count();
				if (tag_count != tag_count2)
					return;
				if (!TextUtils.isEmpty(result)) {
					try {
						JSONObject json = new JSONObject(result);
						boolean flag = json.getBoolean(GlobalConstant.FLAG);
						if (flag) {
							String remote_addr = json.getString(GlobalConstant.REMOTE_ADDR);
							mInfosMode.setRemote_addr(remote_addr);
						} else {
							mInfosMode.setRemote_addr(activity.getResources().getString(R.string.get_fail));
						}

					} catch (JSONException e) {
						e.printStackTrace();
						mInfosMode.setRemote_addr(e.toString());
					}
				} else {
					mInfosMode.setRemote_addr(activity.getResources().getString(R.string.net_error));
				}
				mHookFragment.noticeDataChange();

			}
		});

	}

}
