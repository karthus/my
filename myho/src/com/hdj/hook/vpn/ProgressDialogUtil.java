package com.hdj.hook.vpn;

import com.hdj.hook.R;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

	private Context context;

	public ProgressDialogUtil(Context context) {
		this.context = context;
	}

	public ProgressDialog show() {
		ProgressDialog d = new ProgressDialog(context);
		d.setTitle(context.getResources().getString(R.string.plz_wait));
		d.setCancelable(false);
		d.show();
		return d;
	}

}
