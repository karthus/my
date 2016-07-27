package com.hdj.hook.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class GetNetTypeUtil {

	   /** 
     * 当前使用的网络类型：<br/> 
     * NETWORK_TYPE_UNKNOWN 网络类型未知 0<br/> 
     * NETWORK_TYPE_GPRS GPRS网络 1<br/> 
     * NETWORK_TYPE_EDGE EDGE网络 2<br/> 
     * NETWORK_TYPE_UMTS UMTS网络 3<br/> 
     * NETWORK_TYPE_HSDPA HSDPA网络 8<br/> 
     * NETWORK_TYPE_HSUPA HSUPA网络 9<br/> 
     * NETWORK_TYPE_HSPA HSPA网络 10<br/> 
     * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br/> 
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br/> 
     * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br/> 
     * NETWORK_TYPE_1xRTT 1xRTT网络 7<br/> 
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO<br/> 
     *  
     * @return 
     */  
	public static int get(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telMgr.getNetworkType();
	}

}
