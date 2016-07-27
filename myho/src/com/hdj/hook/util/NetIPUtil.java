package com.hdj.hook.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.text.TextUtils;

public class NetIPUtil {
	private Activity activity;
	private GetNetIpListner listener;
	private final String URL_NET_IP = "http://1212.ip138.com/ic.asp";
	private final String CHARSET_GBK = "gbk";
	private String newNetIp;

	public NetIPUtil(Activity context) {
		activity = context;
	}

	/**
	 * 获取外网IP
	 * 
	 * @return <br>
	 */
	private String setNetIp() {
		newNetIp = null;
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL(URL_NET_IP);
			HttpURLConnection conn = (HttpURLConnection) infoUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				inStream = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, CHARSET_GBK));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();
				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("[");
				int end = strber.indexOf("]", start + 1);
				line = strber.substring(start + 1, end);
				if (!TextUtils.isEmpty(line))
					newNetIp = line;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}

	public void setGetNetIpListner(GetNetIpListner listner) {
		this.listener = listner;
	}

	public interface GetNetIpListner {

		public void getFail(String result);

		/**
		 * 
		 * @param newNetIP
		 *            <br>
		 *            新的外网IP
		 */
		public void getOk(String newNetIP);
	}

	/**
	 * 获取外网IP
	 */
	public void initNetIP() {
		new Thread(new Runnable() {

			public void run() {
				final String result = setNetIp();
				activity.runOnUiThread(new Runnable() {
					public void run() {
						if (newNetIp != null) {
							listener.getOk(newNetIp);
						} else {
							listener.getFail(result);
						}
					}
				});
			}
		}).start();
	}

}
