package com.hdj.hook.util;

import com.hdj.hook.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, CharSequence content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	public static void showL(Context context, CharSequence content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public static void saveResult(boolean isOk, Context context) {
		if (isOk) {
			CharSequence save_ok = StringUtil.SpanColor(context.getResources().getString(R.string.save_ok),
					Color.GREEN);
			ToastUtil.show(context, save_ok);
		} else {
			CharSequence save_fail = StringUtil.SpanColor(context.getResources().getString(R.string.save_fail),
					Color.RED);
			ToastUtil.show(context, save_fail);
		}
	}

	public static void operaResult(boolean isOk, Context context) {
		if (!isOk) {
			CharSequence opera_fail = StringUtil.SpanColor(context.getResources().getString(R.string.opera_fail),
					Color.RED);
			ToastUtil.show(context, opera_fail);
		}
	}
}
