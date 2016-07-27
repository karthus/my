package com.hdj.hook.util;

import java.io.IOException;
import java.io.InputStream;

public class CPUMainFrequencyUtil {

	/*
	 * »ñÈ¡CUPÖ÷Æµ
	 */
	public static String get() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		int a = Integer.parseInt(result.trim());
		int b = a / (1000 * 1000);
		int c = (int) ((((a + (0.5 * 1000 * 100)) / (1000 * 100))) % 10);
		return b + "." + c + "GHz";
	}

}
