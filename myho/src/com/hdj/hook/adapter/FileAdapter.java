package com.hdj.hook.adapter;

import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.mode.FileMode;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {
	private Activity mActivity;
	private ArrayList<FileMode> mDataList;

	public FileAdapter(Activity mActivity, ArrayList<FileMode> mDataList) {
		this.mActivity = mActivity;
		this.mDataList = mDataList;
	}


	@Override
	public int getCount() {
		return mDataList.size();
	}

	public void setData(ArrayList<FileMode> mDataList) {
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
		CheckBox cb_checkbox;
		ImageView iv_image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mActivity, R.layout.adapter_file, null);
			holder.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.cb_checkbox.setVisibility(View.VISIBLE);
		holder.iv_image.setVisibility(View.VISIBLE);
		FileMode fileMode = mDataList.get(position);
		holder.tv_text.setText(fileMode.getName());
		holder.cb_checkbox.setChecked(fileMode.isCheck());
		return convertView;
	}

}
