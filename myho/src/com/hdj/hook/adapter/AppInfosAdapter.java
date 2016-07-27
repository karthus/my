package com.hdj.hook.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hdj.hook.R;
import com.hdj.hook.mode.AppInfosMode;
import com.hdj.hook.util.StringUtil;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfosAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<AppInfosMode> mAppList;
	private PackageManager mPm;
	private boolean mIsShowAlpha;
	private boolean mIsShowCheckBox;

	public AppInfosAdapter(Activity mActivity, List<AppInfosMode> mAppList, boolean mIsShowAlpha,
			boolean mIsShowCheckBox) {
		this.mActivity = mActivity;
		this.mAppList = mAppList;
		this.mIsShowAlpha = mIsShowAlpha;
		this.mIsShowCheckBox = mIsShowCheckBox;
		mPm = mActivity.getPackageManager();
		initColor();
	}

	int color_green01;
	private void initColor() {
		 color_green01 = mActivity.getResources().getColor(R.color.green01);
		
	}

	public void setData(List<AppInfosMode> mAppList) {
		this.mAppList = mAppList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	class Holder {
		TextView tv_alpha;
		TextView tv_text;
		ImageView iv_image;
		CheckBox cb_checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mActivity, R.layout.adapter_select_app, null);
			holder.tv_alpha = (TextView) convertView.findViewById(R.id.tv_alpha);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		AppInfosMode packageInfoMode = mAppList.get(position);
		boolean default1 = packageInfoMode.isDefault();
		if(default1){
			holder.tv_text.setTextColor(color_green01);
		} else {
			holder.tv_text.setTextColor(Color.BLACK);
		}
		holder.tv_text.setText(getName(packageInfoMode, mPm));
		setImage(holder.iv_image, packageInfoMode, mPm);
		if (mIsShowAlpha) {
			setAlpha(position, holder.tv_alpha);
		}
		if (mIsShowCheckBox) {
			holder.cb_checkbox.setVisibility(View.VISIBLE);
			holder.cb_checkbox.setChecked(packageInfoMode.isIscheck());
		}
		return convertView;
	}

	private void setAlpha(int position, TextView tv_alpha) {
		AppInfosMode packageInfoMode = mAppList.get(position);
		String alpha = packageInfoMode.getAlpha();
		if (position == 0) {
			setTvAlpha(alpha, tv_alpha);
		} else {
			AppInfosMode bf_packageInfoMode = mAppList.get(position - 1);
			String bf_alpha = bf_packageInfoMode.getAlpha();
			if (TextUtils.isEmpty(alpha) || alpha.equals(bf_alpha)) {
				tv_alpha.setVisibility(View.GONE);
			} else {
				setTvAlpha(alpha, tv_alpha);
			}
		}
	}

	private void setTvAlpha(String alpha, TextView tv_alpha) {
		tv_alpha.setText(alpha);
		tv_alpha.setVisibility(View.VISIBLE);
	}

	public static CharSequence getName(AppInfosMode packageInfoMode, PackageManager mPm) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(mPm);
		String packageName = packageInfo.packageName;
		CharSequence spanColor = StringUtil.SpanColor(packageName, Color.GRAY);
		CharSequence spanAppendLn = StringUtil.SpanAppendLn(loadLabel, spanColor);
		return spanAppendLn;
	}

	public static CharSequence getName(PackageInfo packageInfo, PackageManager mPm) {
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(mPm);
		String packageName = packageInfo.packageName;
		CharSequence spanColor = StringUtil.SpanColor(packageName, Color.GRAY);
		CharSequence spanAppendLn = StringUtil.SpanAppendLn(loadLabel, spanColor);
		return spanAppendLn;
	}

	public static void setImage(ImageView imageView, AppInfosMode packageInfoMode, PackageManager mPm) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		Drawable loadIcon = applicationInfo.loadIcon(mPm);
		imageView.setImageDrawable(loadIcon);
	}

	public static void setImage(ImageView imageView, PackageInfo packageInfo, PackageManager mPm) {
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		Drawable loadIcon = applicationInfo.loadIcon(mPm);
		imageView.setImageDrawable(loadIcon);
	}

}
