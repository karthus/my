package com.hdj.hook.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoadDataFromNet {
	private Context context;
	private Map<String, String> map;
	private String url;
	public LoadDataFromNet(Context context,String url,Map<String, String> map) {
		this.context=context;
		this.map=map;
		this.url=url;
	}
	public void getData(final DataCallBack callback){
		final Handler handler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				callback.onDataCallBack(msg);
			};
		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpClient client=new DefaultHttpClient();
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
				List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
				for(Map.Entry<String, String> entry:map.entrySet()){
					BasicNameValuePair param=new BasicNameValuePair(entry.getKey(), entry.getValue());
					params.add(param);
					Log.i("Amy", entry.getKey()+": "+ entry.getValue());
				}
				HttpPost request=new HttpPost(url);
				try {
					request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
					HttpResponse response=client.execute(request);
					if(response.getStatusLine().getStatusCode()==200){
						String result=EntityUtils.toString(response.getEntity());
                    	Log.i("Amy", "result = " + result); //获取响应内   
                    	Message msg=handler.obtainMessage();
                    	msg.what=ContantsUtil.HANDLER_GETDATA_OK;
                    	msg.obj=result;
                    	handler.sendMessage(msg);
					}
				} catch (UnsupportedEncodingException e) {
					Message msg=handler.obtainMessage();
                	msg.what=ContantsUtil.HANDLER_GETDATA_ERR;
                	msg.obj=e.toString();
                	handler.sendMessage(msg);
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					Message msg=handler.obtainMessage();
                	msg.what=ContantsUtil.HANDLER_GETDATA_ERR;
                	msg.obj=e.toString();
                	handler.sendMessage(msg);
					e.printStackTrace();
				} catch (IOException e) {
					Message msg=handler.obtainMessage();
                	msg.what=ContantsUtil.HANDLER_GETDATA_ERR;
                	msg.obj=e.toString();
                	handler.sendMessage(msg);
					e.printStackTrace();
				}	
			}
		}).start();
		
	}
	public interface DataCallBack{
		public void onDataCallBack(Message msg);
	}
	

}
