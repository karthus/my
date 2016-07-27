package com.hdj.hook.adapter;

import java.util.ArrayList;

import com.hdj.hook.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConstrolAdapter extends BaseAdapter {
	private Activity mActivity;
	private ArrayList<CharSequence> mDataList = new ArrayList<>();

	public ConstrolAdapter(Activity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	public void setData(ArrayList<CharSequence> mDataList) {
		this.mDataList = mDataList;
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
			convertView = View.inflate(mActivity, R.layout.adapter_control, null);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_text.setText(mDataList.get(position));
		return convertView;
	}

}
