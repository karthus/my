package com.hdj.hook.adapter;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.hdj.hook.R;
import com.hdj.hook.mode.SystemValueMode;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SystemValueAdapter extends BaseAdapter {

	private Activity mActivity;

	ArrayList<String> mAlldata = new ArrayList<String>();


	public SystemValueAdapter(Activity mActivity) {
		this.mActivity = mActivity;
		initStirng();
	}
	
	public void setData(ArrayList<String> mAlldata){
		this.mAlldata = mAlldata;
	}
	public void clearData(){
		mAlldata.removeAll(mAlldata);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAlldata.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	class Holder {
		TextView tv_text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mActivity, R.layout.adapter_system_value, null);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_text.setText(showData(mAlldata.get(position)));
		return convertView;
	}

	String str_method, str_result, str_params;

	private void initStirng() {
		str_method = mActivity.getResources().getString(R.string.method);
		str_result = mActivity.getResources().getString(R.string.result);
		str_params = mActivity.getResources().getString(R.string.params);

	}

	private Gson gson;

	private String showData(String str) {
		if (gson == null)
			gson = new Gson();
		SystemValueMode mode = gson.fromJson(str, SystemValueMode.class);
		String method = mode.getMethod();
		ArrayList<String> data = mode.getData();
		String param = "";
		for (int i = 0; i < data.size(); i++) {
			if (i == 0) {
				param += data.get(i);
			} else {
				param += " , " + data.get(i);
			}
		}
		String result = mode.getResult();
		return str_method + method + str_params + param + str_result + result;
	}

}
