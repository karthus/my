package com.hdj.hook.vpn;

import android.content.Context;

public interface PPTPVPNInterface {
	 void start(VpnProfile profile,Context context);
	 boolean startAuto(VpnProfile profile,Context context);
	 void stop(Context context);
	 void stopAuto(Context context);

}
