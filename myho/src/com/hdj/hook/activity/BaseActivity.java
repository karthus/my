package com.hdj.hook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	Toast toast=null;
	public void ShowToast(String tips){
		toast=toast==null?Toast.makeText(BaseActivity.this, "", Toast.LENGTH_SHORT):toast;
		toast.setText(tips);
		toast.show();
	}

}
