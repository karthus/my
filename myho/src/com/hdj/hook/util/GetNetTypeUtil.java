package com.hdj.hook.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class GetNetTypeUtil {

	   /** 
     * ��ǰʹ�õ��������ͣ�<br/> 
     * NETWORK_TYPE_UNKNOWN ��������δ֪ 0<br/> 
     * NETWORK_TYPE_GPRS GPRS���� 1<br/> 
     * NETWORK_TYPE_EDGE EDGE���� 2<br/> 
     * NETWORK_TYPE_UMTS UMTS���� 3<br/> 
     * NETWORK_TYPE_HSDPA HSDPA���� 8<br/> 
     * NETWORK_TYPE_HSUPA HSUPA���� 9<br/> 
     * NETWORK_TYPE_HSPA HSPA���� 10<br/> 
     * NETWORK_TYPE_CDMA CDMA����,IS95A �� IS95B. 4<br/> 
     * NETWORK_TYPE_EVDO_0 EVDO����, revision 0. 5<br/> 
     * NETWORK_TYPE_EVDO_A EVDO����, revision A. 6<br/> 
     * NETWORK_TYPE_1xRTT 1xRTT���� 7<br/> 
     * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO<br/> 
     *  
     * @return 
     */  
	public static int get(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telMgr.getNetworkType();
	}

}
