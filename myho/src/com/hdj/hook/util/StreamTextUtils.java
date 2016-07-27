package com.hdj.hook.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.text.TextUtils;

public class StreamTextUtils {

	public static String getTextFromStream(InputStream is) {

		byte[] b = new byte[1024];
		int len = 0;
		// 创建字节数组输出流，读取输入流的文本数据时，同步把数据写入数组输出流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			// 把字节数组输出流里的数据转换成字节数组
			String text = new String(bos.toByteArray());
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> getTextListFromStream(InputStream is) {
		ArrayList<String> arrayList = new ArrayList<String>();
		InputStreamReader inputreader = new InputStreamReader(is);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		try {
			line = buffreader.readLine();
			// 分行读取
			while ((line = buffreader.readLine()) != null) {
				arrayList.add(line);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return arrayList;
	}

	public static ArrayList<String> getTextListFromStream2(InputStream is) {
		ArrayList<String> arrayList = new ArrayList<String>();
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		InputStreamReader inputreader = new InputStreamReader(is);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		try {
			line = buffreader.readLine();
			// 分行读取
			while ((line = buffreader.readLine()) != null) {
				if (!hashMap.containsKey(line)) {
					hashMap.put(line, 1);
					arrayList.add(line);
				}
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hashMap = null;
		return arrayList;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}
