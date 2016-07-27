package com.hdj.hook.mode;

public class DisplayMode {
	
	float xdpi;
	float ydpi ;
	float density ;// ÆÁÄ»ÃÜ¶È£¨0.75 / 1.0 / 1.5£©
	int densityDpi ;// ÆÁÄ»ÃÜ¶ÈDPI£¨120 / 160 / 240£©
	int heightPixels ;
	int widthPixels;
	float scaledDensity ;
	
	int height;
	int width ;
	String diplaySize;
	public float getXdpi() {
		return xdpi;
	}
	public void setXdpi(float xdpi) {
		this.xdpi = xdpi;
	}
	public float getYdpi() {
		return ydpi;
	}
	public void setYdpi(float ydpi) {
		this.ydpi = ydpi;
	}
	public float getDensity() {
		return density;
	}
	public void setDensity(float density) {
		this.density = density;
	}
	public int getDensityDpi() {
		return densityDpi;
	}
	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}
	public int getHeightPixels() {
		return heightPixels;
	}
	public void setHeightPixels(int heightPixels) {
		this.heightPixels = heightPixels;
	}
	public float getScaledDensity() {
		return scaledDensity;
	}
	public void setScaledDensity(float scaledDensity) {
		this.scaledDensity = scaledDensity;
	}
	public int getWidthPixels() {
		return widthPixels;
	}
	public void setWidthPixels(int widthPixels) {
		this.widthPixels = widthPixels;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getDiplaySize() {
		return diplaySize;
	}
	public void setDiplaySize(String diplaySize) {
		this.diplaySize = diplaySize;
	}

}
