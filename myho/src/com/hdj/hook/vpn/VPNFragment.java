package com.hdj.hook.vpn;

import java.io.IOException;
import java.util.ArrayList;

import com.hdj.hook.R;
import com.hdj.hook.fragment.BaseFragment;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.NoticUtil;
import com.hdj.hook.util.ToastUtil;

import android.app.Notification;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class VPNFragment extends BaseFragment implements OnClickListener {
	private EditText et_username, et_password, et_server;
	private Spinner sn_type;
	private ProfileLoader mLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(mMainActivity, R.layout.activity_vpn, null);
		mLoader = ProfileLoader.getInstance(mMainActivity);
		findViewById(view);
		initString();
		VpnProfile dfVpnProfile = mLoader.getDefault();
		setProfile(dfVpnProfile);
		// startPNPService();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(GlobalConstant.ACTION_VPN_RECEVIER);
		vpnRecevier = new VPNRecevier();
		mMainActivity.registerReceiver(vpnRecevier, intentFilter);
		initSwith(dfVpnProfile);
		return view;
	}

	String connect, disconnect, please_input_username, please_input_code, please_input_service;

	private void initString() {
		connect = getResources().getString(R.string.connect);
		disconnect = getResources().getString(R.string.disconnect);
		please_input_username = getResources().getString(R.string.please_input_username);
		please_input_code = getResources().getString(R.string.please_input_code);
		please_input_service = getResources().getString(R.string.please_input_service);
	}

	private void initSwith(VpnProfile dfVpnProfile) {
		final boolean blockUntilStarted = VpnManager.isStarted();
		setConnectBtnText(blockUntilStarted && dfVpnProfile != null);
	}

	private void initSwith() {
		final boolean blockUntilStarted = VpnManager.isStarted();
		setConnectBtnText(blockUntilStarted);
	}

	private void setConnectBtnText(boolean isConnect) {
		if (btn_connect == null)
			return;
		if (isConnect) {
			btn_connect.setText(disconnect);
			setEtEnability(false);
		} else {
			btn_connect.setText(connect);
			setEtEnability(true);
		}
	}

	class VPNRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String flag = intent.getStringExtra(GlobalConstant.FLAG);
			if (GlobalConstant.FLAG_CHECK_CONNECT.equals(flag)) {
				initSwith();
			}
		}

	}

	private void setEtEnability(boolean isEnable) {
		et_username.setEnabled(isEnable);
		et_server.setEnabled(isEnable);
		et_password.setEnabled(isEnable);

	}

	private void setProfile(VpnProfile dfVpnProfile) {
		if (dfVpnProfile == null)
			return;
		et_username.setText(dfVpnProfile.username);
		// et_name.setText(dfVpnProfile.name);
		et_server.setText(dfVpnProfile.server);
		et_password.setText(dfVpnProfile.password);
	}

	Button btn_connect, btn_test, btn_save;
	EditText et_run;
	Spinner sn_dns1;

	private void findViewById(View view) {
		// et_name = (EditText) view.findViewById(R.id.et_name);
		sn_dns1 = (Spinner) view.findViewById(R.id.sn_dns1);
		btn_connect = (Button) view.findViewById(R.id.btn_connect);
		et_run = (EditText) view.findViewById(R.id.et_run);
		btn_save = (Button) view.findViewById(R.id.btn_save);
		btn_test = (Button) view.findViewById(R.id.btn_test);
		et_server = (EditText) view.findViewById(R.id.et_server);
		et_username = (EditText) view.findViewById(R.id.et_username);
		et_password = (EditText) view.findViewById(R.id.et_password);
		sn_type = (Spinner) view.findViewById(R.id.sn_type);
		btn_connect.setOnClickListener(this);
		btn_test.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	private VpnProfile mProfile = new VpnProfile();
	private VPNRecevier vpnRecevier;

	public VpnProfile getProfile() {
		// if (mProfile == null) return null;
		mProfile.server = et_server.getText().toString().trim();
		mProfile.username = et_username.getText().toString().trim();
		mProfile.password = et_password.getText().toString().trim();
		mProfile.name = et_username.getText().toString().trim();
		// mProfile.name = et_name.getText().toString().trim();
		mProfile.dns1 = sn_dns1.getSelectedItem().toString();
		mProfile.dns2 = "8.8.4.4";
		mProfile.mppe = false;

		return mProfile;
	}

	@Override
	public void onDestroy() {
		if (vpnRecevier != null)
			mMainActivity.unregisterReceiver(vpnRecevier);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_connect:
			toConnect();
			break;
		case R.id.btn_test:
			break;
		case R.id.btn_save:
			if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_username);
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_code);
				return;
			}
			if (TextUtils.isEmpty(et_server.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_service);
				return;
			}
			VpnProfile profile = getProfile();
			PPTPVPNService.saveVpnInfos(profile, mMainActivity);
			break;
		default:
			break;
		}

	}


	Notification showVpn;


	private void toConnect() {
		if (connect.equals(btn_connect.getText().toString())) {
			if (TextUtils.isEmpty(et_username.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_username);
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_code);
				return;
			}
			if (TextUtils.isEmpty(et_server.getText().toString().trim())) {
				ToastUtil.show(mMainActivity, please_input_service);
				return;
			}
			boolean started = VpnManager.isStarted();
			if (started) {
				setConnectBtnText(true);
				return;
			}
			VpnProfile profile = getProfile();
			PPTPVPNInterface pPTPVPN = PPTPVPNServiceConn.getInstance();
			if (pPTPVPN == null) {
				PPTPVPNService.startVpnService(mMainActivity);
				setConnectBtnText(false);
			} else {
				pPTPVPN.start(profile, mMainActivity);
			}

		} else if (disconnect.equals(btn_connect.getText().toString())) {
			final PPTPVPNInterface pPTPVPN = PPTPVPNServiceConn.getInstance();
			if (pPTPVPN == null) {
				PPTPVPNService.startVpnService(mMainActivity);
			} else {
				ProgressDialogUtil pdu = new ProgressDialogUtil(mMainActivity);
				final ProgressDialog pd = pdu.show();
				pd.setTitle(getResources().getString(R.string.disconnectting));
				new Thread(new Runnable() {
					public void run() {
						pPTPVPN.stop(mMainActivity);
						pd.dismiss();
						mMainActivity.runOnUiThread(new Runnable() {
							public void run() {
								initSwith();
							}
						});

					}
				}).start();
			}
		}

	}

}
