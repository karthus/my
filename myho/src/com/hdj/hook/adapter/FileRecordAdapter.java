package com.hdj.hook.adapter;

import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.util.FileUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FileRecordAdapter extends BaseAdapter {
	ArrayList<String> dataList = new ArrayList<String>();
	Context context;

	public FileRecordAdapter(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	public void setData(ArrayList<String> dataList) {
		this.dataList = dataList;
		notifyDataSetChanged();
	}

	public void clearData() {
		dataList = new ArrayList<String>();
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.adapter_file_record, null);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv_num.setText(position + ".");
		holder.tv_text.setText(dataList.get(position));
		if (FileUtil.isExternalStorageFile(dataList.get(position))) {
			holder.tv_text.setTextColor(Color.RED);
		} else {
			holder.tv_text.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	class Holder {
		TextView tv_text;
		TextView tv_num;
	}

}
