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
			state = "�绰����";
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			state = "�绰��ͨ";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			state = "�е绰����";
			break;
		default:
			break;
		}

	}

	public static String getPhoneType(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// �ƶ��ն˵��ֻ���ʽ����
		int phoneType = tm.getPhoneType();
		String type = null;
		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			type = "CDMA�ź�";// ����
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			type = "GSM�ź�";// �ƶ�����ͨ
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			type = "���ź�";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			type = "SIP�ź�";
			break;
		default:
			break;
		}
		// ����SIM���ṩ�̵Ĺ��Ҵ���
		tm.getSimCountryIso();
		// ����MCC+MNC���� (SIM����Ӫ�̹��Ҵ������Ӫ���������)(IMSI)
		tm.getSimOperator();
		tm.getSimOperatorName();
		/**
		 * * ��ȡ�������� * * NETWORK_TYPE_CDMA ��������ΪCDMA * NETWORK_TYPE_EDGE
		 * ��������ΪEDGE * NETWORK_TYPE_EVDO_0 ��������ΪEVDO0 * NETWORK_TYPE_EVDO_A
		 * ��������ΪEVDOA * NETWORK_TYPE_GPRS ��������ΪGPRS * NETWORK_TYPE_HSDPA
		 * ��������ΪHSDPA * NETWORK_TYPE_HSPA ��������ΪHSPA * NETWORK_TYPE_HSUPA
		 * ��������ΪHSUPA * NETWORK_TYPE_UMTS ��������ΪUMTS
		 * 
		 * ���й�,��ͨ��3GΪUMTS��HSDPA,�ƶ�����ͨ��2GΪGPRS��EGDE,���ŵ�2GΪCDMA,���ŵ�3GΪEVDO
		 */
		tm.getNetworkType();
		// �����ƶ�������Ӫ�̵�����(SPN)
		tm.getNetworkOperatorName();
		// ����ISO��׼�Ĺ�����,�����ʳ�;����
		tm.getNetworkCountryIso();
		// �����ƶ��ն˵�����汾,����:GSM�ֻ���IMEI/SV�롣
		tm.getDeviceSoftwareVersion();

		/**
		 * * ��ȡ���ݻ״̬ * * DATA_ACTIVITY_IN ��������״̬:�,���ڽ������� * DATA_ACTIVITY_OUT
		 * ��������״̬:�,���ڷ������� * DATA_ACTIVITY_INOUT ��������״̬:�,���ڽ��ܺͷ������� *
		 * DATA_ACTIVITY_NONE ��������״̬:�,�������ݷ��ͺͽ���
		 */
		tm.getDataActivity();
		/**
		 * * ��ȡ��������״̬ * * DATA_CONNECTED ��������״̬:������ * DATA_CONNECTING
		 * ��������״̬:�������� * DATA_DISCONNECTED ��������״̬:�Ͽ� * DATA_SUSPENDED ��������״̬:��ͣ
		 */
		tm.getDataState();

		/**
		 * * ���ص绰״̬ * * CALL_STATE_IDLE ���κ�״̬ʱ * CALL_STATE_OFFHOOK ����绰ʱ *
		 * CALL_STATE_RINGING �绰����ʱ
		 */
		tm.getCallState();
		// ���ص�ǰ�ƶ��ն˵�λ��
		CellLocation location = tm.getCellLocation();
		/**
		 * * �����ƶ��ն� * * SIM_STATE_ABSENT SIM��δ�ҵ� * SIM_STATE_NETWORK_LOCKED
		 * SIM�����类����,��ҪNetwork PIN���� * SIM_STATE_PIN_REQUIRED SIM��PIN������,��ҪUser
		 * PIN���� * SIM_STATE_PUK_REQUIRED SIM��PUK������,��ҪUser PUK���� *
		 * SIM_STATE_READY SIM������ * SIM_STATE_UNKNOWN SIM��δ֪
		 */
		tm.getSimState();

		// ��ȡ������������������ĸ��ʶ��
		tm.getVoiceMailAlphaTag();
		// ���������ʼ�����
		tm.getVoiceMailNumber();
		tm.hasIccCard();
		// �����ֻ��Ƿ�������״̬
		tm.isNetworkRoaming();
		// ����: //IMSI�ǹ����ƶ��û�ʶ����ļ��(International Mobile Subscriber Identity)
		// //IMSI����15λ,��ṹ����: //MCC+MNC+MIN //MCC:Mobile Country
		// Code,�ƶ�������,��3λ,�й�Ϊ460; //MNC:Mobile NetworkCode,�ƶ�������,��2λ
		// //���й�,�ƶ��Ĵ���Ϊ��00��02,��ͨ�Ĵ���Ϊ01,���ŵĴ���Ϊ03
		// //����������(Ҳ��Android�ֻ���APN�����ļ��еĴ���): //�й��ƶ�:46000 46002 //�й���ͨ:46001
		// //�й�����:46003 //����,һ�����͵�IMSI����Ϊ460030912121001 //IMEI��International
		// Mobile Equipment Identity (�����ƶ��豸��ʶ)�ļ��
		// //IMEI��15λ������ɵġ����Ӵ��š�,����ÿ̨�ֻ�һһ��Ӧ,���Ҹ�����ȫ����Ψһ�� //�����Ϊ: //1.
		// ǰ6λ��(TAC)�ǡ��ͺź�׼���롱,һ�������� //2. ���ŵ�2λ��(FAC)�ǡ����װ��š�,һ�������� //3.
		// ֮���6λ��(SNR)�ǡ����š�,һ���������˳��� //4. ���1λ��(SP)ͨ���ǡ�0��,Ϊ������,Ŀǰ�ݱ���
		return type;
	}

	/**
	 * 
	 * @param context
	 * @param tm
	 * @return SPN �����ṩ������
	 */
	private static String getSimOperatorName(TelephonyManager tm) {
		int state = tm.getSimState();
		String simOperatorName = "";
		if (TelephonyManager.SIM_STATE_READY == state) {
			simOperatorName = tm.getSimOperatorName();
		}
		return simOperatorName;
	}

	// ICCID��Integrate circuit card identity ���ɵ�·��ʶ���루�̻����ֻ�SIM���У�
	// ICCIDΪIC����Ψһʶ����룬����20λ������ɣ�������ʽΪ��XXXXXX 0MFSS YYGXX XXXXX���ֱ�������£�
	// ǰ��λ��Ӫ�̴��룺�й��ƶ���Ϊ��898600��898602 ���й���ͨ��Ϊ��898601���й�����898603
	// ICCID�ĺ��� SIM�������

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
	 * �����ƶ��ն˵����ͣ�<br/>
	 * PHONE_TYPE_CDMA �ֻ���ʽΪCDMA������<br/>
	 * PHONE_TYPE_GSM �ֻ���ʽΪGSM���ƶ�����ͨ<br/>
	 * PHONE_TYPE_NONE �ֻ���ʽδ֪<br/>
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
		simInfosMode.setSim_country_iso(tm.getSimCountryIso());// SIM���ṩ�̵Ĺ��Ҵ���
		simInfosMode.setSim_operator(getSimOperator(tm));
		mainActivity.getmInfosMode().setSimInfosMode(simInfosMode);
	}

	// ����MCC+MNC���� (SIM����Ӫ�̹��Ҵ������Ӫ���������)(IMSI)
	private static String getSimOperator(TelephonyManager tm) {
		String simOperator = "";
		int state = tm.getSimState();
		if (TelephonyManager.SIM_STATE_READY == state) {
			simOperator = tm.getSimOperator();
		}
		return simOperator;
	}

}
