package com.hdj.hook.util;

import java.util.Random;

public class RandomUtil {

	/**
	 * 
	 * @param length
	 *            ³¤¶È
	 * @return
	 */
	public static String getRandInt(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	public static int getInt(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return Integer.parseInt(sb.toString());
	}
	public static String getStr(int length,String str) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return str+sb.toString();
	}
	public static boolean getBoolean() {
		Random random = new Random();
		return	random.nextBoolean();
	}

	public static String getRandomBuildBrand() {
		String[] brand = new String[] { "Xiaomi", "Samsumg", "OPPO", "HUAWEI" };
		Random random = new Random();
		return brand[random.nextInt(brand.length)];
	}
	public static String getRandomNetTypeName() {
		String[] brand = new String[] {  "UNKNOW", "MOBILE", "WIFI" };
		Random random = new Random();
		return brand[random.nextInt(brand.length)];
	}

	public static String getBuildMold() {
		Random random = new Random();
		int year = 2011 + random.nextInt(5);
		int month = 1+random.nextInt(12);
		int day = 1+random.nextInt(28);
		String str_month=null;
		String str_day=null;
		if(month<10){
			str_month =0+""+month;
		} else {
			str_month = String.valueOf(month);
		}
		if(day<10){
			str_day =0+""+day;
		} else{
			str_day = String.valueOf(day);
		}
		
		return year+str_month+str_day;
	}
	
	public static int getRandNum(int start,int end){
		Random random = new Random();
		return start+random.nextInt(end-start+1);
		
	}
	public static String getBuildProduct() {
		Random random = new Random();
		int year = 2011 + random.nextInt(5);
		int month = 1+random.nextInt(12);
		int day = 1+random.nextInt(28);
		String str_month=null;
		String str_day=null;
		if(month<10){
			str_month =0+""+month;
		} else {
			str_month = String.valueOf(month);
		}
		if(day<10){
			str_day =0+""+day;
		} else{
			str_day = String.valueOf(day);
		}
		
		return year+str_month+str_day;
	}

}
