package com.hdj.hook.util;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorUtil {

	public static String get(Context context) {
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		StringBuffer sb = new StringBuffer();
		List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensorList) {
			String name = sensor.getName();
			int type = sensor.getType();
			String vendor = sensor.getVendor();
			int version = sensor.getVersion();
			float resolution = sensor.getResolution();
			sb.append("name:"+name);
			sb.append("\n");
			sb.append("type:"+type);
			sb.append("\n");
			sb.append("vendor:"+vendor);
			sb.append("\n");
			sb.append("version:"+version);
			sb.append("\n");
			sb.append("resolution:"+resolution);
			sb.append("\n\n");
		}
		return sb.toString();
	}

}
