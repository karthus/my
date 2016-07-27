package com.hdj.hook.util;

import com.hdj.hook.activity.MainActivity;
import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.SimInfosMode;

import android.R.string;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

public class TelephonyUtil {

	public static void getCallState(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int callState = tm.getCallState();
		String state = null;
		switch (callState) {
		case TelephonyManager.CALL_STATE_IDLE:
			state = "电话闲置";
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			state = "电话接通";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			state = "有电话进入";
			break;
		default:
			break;
		}

	}

	public static String getPhoneType(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// 移动终端的手机制式类型
		int phoneType = tm.getPhoneType();
		String type = null;
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			type = "CDMA信号";// 电信
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			type = "GSM信号";// 移动和联通
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			type = "无信号";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			type = "SIP信号";
			break;
		default:
			break;
		}
		// 返回SIM卡提供商的国家代码
		tm.getSimCountryIso();
		// 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
		tm.getSimOperator();
		tm.getSimOperatorName();
		/**
		 * * 获取网络类型 * * NETWORK_TYPE_CDMA 网络类型为CDMA * NETWORK_TYPE_EDGE
		 * 网络类型为EDGE * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 * NETWORK_TYPE_EVDO_A
		 * 网络类型为EVDOA * NETWORK_TYPE_GPRS 网络类型为GPRS * NETWORK_TYPE_HSDPA
		 * 网络类型为HSDPA * NETWORK_TYPE_HSPA 网络类型为HSPA * NETWORK_TYPE_HSUPA
		 * 网络类型为HSUPA * NETWORK_TYPE_UMTS 网络类型为UMTS
		 * 
		 * 在中国,联通的3G为UMTS或HSDPA,移动和联通的2G为GPRS或EGDE,电信的2G为CDMA,电信的3G为EVDO
		 */
		tm.getNetworkType();
		// 返回移动网络运营商的名字(SPN)
		tm.getNetworkOperatorName();
		// 返回ISO标准的国家码,即国际长途区号
		tm.getNetworkCountryIso();
		// 返回移动终端的软件版本,例如:GSM手机的IMEI/SV码。
		tm.getDeviceSoftwareVersion();

		/**
		 * * 获取数据活动状态 * * DATA_ACTIVITY_IN 数据连接状态:活动,正在接受数据 * DATA_ACTIVITY_OUT
		 * 数据连接状态:活动,正在发送数据 * DATA_ACTIVITY_INOUT 数据连接状态:活动,正在接受和发送数据 *
		 * DATA_ACTIVITY_NONE 数据连接状态:活动,但无数据发送和接受
		 */
		tm.getDataActivity();
		/**
		 * * 获取数据连接状态 * * DATA_CONNECTED 数据连接状态:已连接 * DATA_CONNECTING
		 * 数据连接状态:正在连接 * DATA_DISCONNECTED 数据连接状态:断开 * DATA_SUSPENDED 数据连接状态:暂停
		 */
		tm.getDataState();

		/**
		 * * 返回电话状态 * * CALL_STATE_IDLE 无任何状态时 * CALL_STATE_OFFHOOK 接起电话时 *
		 * CALL_STATE_RINGING 电话进来时
		 */
		tm.getCallState();
		// 返回当前移动终端的位置
		CellLocation location = tm.getCellLocation();
		/**
		 * * 返回移动终端 * * SIM_STATE_ABSENT SIM卡未找到 * SIM_STATE_NETWORK_LOCKED
		 * SIM卡网络被锁定,需要Network PIN解锁 * SIM_STATE_PIN_REQUIRED SIM卡PIN被锁定,需要User
		 * PIN解锁 * SIM_STATE_PUK_REQUIRED SIM卡PUK被锁定,需要User PUK解锁 *
		 * SIM_STATE_READY SIM卡可用 * SIM_STATE_UNKNOWN SIM卡未知
		 */
		tm.getSimState();

		// 获取语音信箱号码关联的字母标识。
		tm.getVoiceMailAlphaTag();
		// 返回语音邮件号码
		tm.getVoiceMailNumber();
		tm.hasIccCard();
		// 返回手机是否处于漫游状态
		tm.isNetworkRoaming();
		// 解释: //IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
		// //IMSI共有15位,其结构如下: //MCC+MNC+MIN //MCC:Mobile Country
		// Code,移动国家码,共3位,中国为460; //MNC:Mobile NetworkCode,移动网络码,共2位
		// //在中国,移动的代码为电00和02,联通的代码为01,电信的代码为03
		// //合起来就是(也是Android手机中APN配置文件中的代码): //中国移动:46000 46002 //中国联通:46001
		// //中国电信:46003 //举例,一个典型的IMSI号码为460030912121001 //IMEI是International
		// Mobile Equipment Identity (国际移动设备标识)的简称
		// //IMEI由15位数字组成的”电子串号”,它与每台手机一一对应,而且该码是全世界唯一的 //其组成为: //1.
		// 前6位数(TAC)是”型号核准号码”,一般代表机型 //2. 接着的2位数(FAC)是”最后装配号”,一般代表产地 //3.
		// 之后的6位数(SNR)是”串号”,一般代表生产顺序号 //4. 最后1位数(SP)通常是”0″,为检验码,目前暂备用
		return type;
	}

	/**
	 * 
	 * @param context
	 * @param tm
	 * @return SPN 服务提供商名称
	 */
	private static String getSimOperatorName(TelephonyManager tm) {
		int state = tm.getSimState();
		String simOperatorName = "";
		if (TelephonyManager.SIM_STATE_READY == state) {
			simOperatorName = tm.getSimOperatorName();
		}
		return simOperatorName;
	}

	// ICCID：Integrate circuit card identity 集成电路卡识别码（固化在手机SIM卡中）
	// ICCID为IC卡的唯一识别号码，共有20位数字组成，其编码格式为：XXXXXX 0MFSS YYGXX XXXXX。分别介绍如下：
	// 前六位运营商代码：中国移动的为：898600；898602 ，中国联通的为：898601，中国电信898603
	// ICCID的号码 SIM卡的序号

	private static String getSimSerialNumber(TelephonyManager tm) {
		boolean hasIccCard = tm.hasIccCard();
		String simSerialNumber = "";
		if (hasIccCard) {
			simSerialNumber = tm.getSimSerialNumber();
		}
		return simSerialNumber;
	}

	public static boolean getHasIccCard(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		boolean hasIccCard = tm.hasIccCard();
		return hasIccCard;
	}

	/**
	 * 返回移动终端的类型：<br/>
	 * PHONE_TYPE_CDMA 手机制式为CDMA，电信<br/>
	 * PHONE_TYPE_GSM 手机制式为GSM，移动和联通<br/>
	 * PHONE_TYPE_NONE 手机制式未知<br/>
	 * 
	 * @return
	 */
	private static int getPhoneType2(TelephonyManager tm) {
		return tm.getPhoneType();
	}

	public static void get(HookFragment mainActivity) {
		SimInfosMode simInfosMode = new SimInfosMode();
		TelephonyManager tm = (TelephonyManager) mainActivity.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		simInfosMode.setSim_state(tm.getSimState());
		simInfosMode.setOperator(tm.getNetworkOperator());
		simInfosMode.setPhoneType(getPhoneType2(tm));
		simInfosMode.setSim_serial(getSimSerialNumber(tm));
		simInfosMode.setSim_operator_name(getSimOperatorName(tm));
		simInfosMode.setNetwork_operator_name(tm.getNetworkOperatorName());
		simInfosMode.setPhone_num(tm.getLine1Number());
		simInfosMode.setImei(tm.getDeviceId());
		simInfosMode.setSim_country_iso(tm.getSimCountryIso());// SIM卡提供商的国家代码
		simInfosMode.setSim_operator(getSimOperator(tm));
		mainActivity.getmInfosMode().setSimInfosMode(simInfosMode);
	}

	// 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
	private static String getSimOperator(TelephonyManager tm) {
		String simOperator = "";
		int state = tm.getSimState();
		if (TelephonyManager.SIM_STATE_READY == state) {
			simOperator = tm.getSimOperator();
		}
		return simOperator;
	}

}
