package com.hdj.hook.util;

import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.DisplayMode;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {

	public static void get(HookFragment mainActivity) {
		DisplayMode displayMode = new DisplayMode();
		DisplayMetrics metric = new DisplayMetrics();
		mainActivity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		float xdpi = metric.xdpi;
		float ydpi = metric.ydpi;
		displayMode.setXdpi(xdpi);
		displayMode.setYdpi(ydpi);

		float density = metric.density;// ÆÁÄ»ÃÜ¶È£¨0.75 / 1.0 / 1.5£©
		int densityDpi = metric.densityDpi;// ÆÁÄ»ÃÜ¶ÈDPI£¨120 / 160 / 240£©
		displayMode.setDensity(density);
		displayMode.setDensityDpi(densityDpi);

		int heightPixels = metric.heightPixels;
		float scaledDensity = metric.scaledDensity;
		int widthPixels = metric.widthPixels;
		displayMode.setHeightPixels(heightPixels);
		displayMode.setScaledDensity(scaledDensity);
		displayMode.setWidthPixels(widthPixels);

		Display display = mainActivity.getActivity().getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		int width = display.getWidth();
		displayMode.setHeight(height);
		displayMode.setWidth(width);

		Point point = new Point();
		display.getSize(point);
		String diplaySize = point.toString();
		displayMode.setDiplaySize(diplaySize);

		mainActivity.getmInfosMode().setDisplayMode(displayMode);
	}

	public static void get2(HookFragment mainActivity) {
		DisplayMode displayMode = new DisplayMode();
		DisplayMetrics metric = mainActivity.getActivity().getResources().getDisplayMetrics();
		float xdpi = metric.xdpi;
		float ydpi = metric.ydpi;
		displayMode.setXdpi(xdpi);
		displayMode.setYdpi(ydpi);

		float density = metric.density;// ÆÁÄ»ÃÜ¶È£¨0.75 / 1.0 / 1.5£©
		int densityDpi = metric.densityDpi;// ÆÁÄ»ÃÜ¶ÈDPI£¨120 / 160 / 240£©
		displayMode.setDensity(density);
		displayMode.setDensityDpi(densityDpi);

		int heightPixels = metric.heightPixels;
		float scaledDensity = metric.scaledDensity;
		int widthPixels = metric.widthPixels;
		displayMode.setHeightPixels(heightPixels);
		displayMode.setScaledDensity(scaledDensity);
		displayMode.setWidthPixels(widthPixels);

		mainActivity.getmInfosMode().setDisplayMode2(displayMode);
	}


}
