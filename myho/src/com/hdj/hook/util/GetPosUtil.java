package com.hdj.hook.util;

import java.util.List;

import com.hdj.hook.activity.MainActivity;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.InfosMode;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GetPosUtil {
	LocationManager locationManager;

	HookFragment mainActivity;

	private InfosMode mInfosMode;

	public GetPosUtil(HookFragment mainActivity) {
		this.mainActivity = mainActivity;
		mInfosMode = mainActivity.getmInfosMode();
	}

	public void getJW() {
		locationManager = (LocationManager) mainActivity.getActivity().getSystemService(Context.LOCATION_SERVICE);
		new Thread() {
			@Override
			public void run() {
				try {
					final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						mainActivity.getActivity().runOnUiThread(new Runnable() {
							public void run() {
								double latitude = location.getLatitude(); //  纬度
								double longitude = location.getLongitude(); // 经度
								float accuracy = location.getAccuracy();
								mInfosMode.setLatitude(latitude);
								mInfosMode.setLongitude(longitude);
								mInfosMode.setLogLanAccuracy(accuracy);
								mainActivity.noticeDataChange();
							}
						});
					}

				} catch (Exception e) {
				}

			}
		}.start();
		// MyLocationListner myLocationListner = new MyLocationListner();
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0, myLocationListner);
	}

	class MyLocationListner implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location == null)
				return;
			double latitude = location.getLatitude(); // 经度
			double longitude = location.getLongitude(); // 纬度
			float accuracy = location.getAccuracy();
			mInfosMode.setLatitude(latitude);
			mInfosMode.setLongitude(longitude);
			mInfosMode.setLogLanAccuracy(accuracy);
			mainActivity.noticeDataChange();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}

}
