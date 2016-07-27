package com.hdj.hook.util;

import com.hdj.hook.R;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

	private Context context;

	public ProgressDialogUtil(Context context) {
		this.context = context;
	}

	public ProgressDialog show(String str) {
		ProgressDialog d = new ProgressDialog(context);
		d.setTitle(str);
		d.setCancelable(false);
		d.show();
		return d;
	}

}
