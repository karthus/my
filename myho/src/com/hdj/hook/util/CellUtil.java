package com.hdj.hook.util;

import com.hdj.hook.fragment.HookFragment;
import com.hdj.hook.mode.SCellMode;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class CellUtil {


	/**
	 * 获取基站信息
	 * 
	 * @throws Exception
	 */
	public static void get(HookFragment mainActivity)  {
		SCellMode sCell = new SCellMode();
		/** 调用API获取基站信息 */
		TelephonyManager mTelNet = (TelephonyManager) mainActivity.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = mTelNet.getCellLocation();
		if (location instanceof GsmCellLocation) {
			GsmCellLocation cellLocation = (GsmCellLocation) location;
			int	cellid = cellLocation.getCid();
			int lac = cellLocation.getLac();
			sCell.setCid(cellid);
			sCell.setLac(lac);
		} else if (location instanceof CdmaCellLocation) {
			CdmaCellLocation cellLocation = (CdmaCellLocation) location;
			int cellid = cellLocation.getBaseStationId();
			int networkId = cellLocation.getNetworkId();
			sCell.setCid(cellid);
			sCell.setLac(networkId);
		}
		mainActivity.getmInfosMode().setScell(sCell);
	}


}
