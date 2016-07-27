package com.hdj.hook.vpn;

import com.hdj.hook.R;
import com.hdj.hook.activity.AutoActivity;
import com.hdj.hook.util.GlobalConstant;
import com.hdj.hook.util.NoticUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class VpnManager {
	private static final String PPP_UNIT = "0";
	public static final String PPP_INTERFACE = "ppp" + PPP_UNIT;
	public static final String DEV = "dev";
	private static final int MAX_WAIT_TIME = 15; // seconds
	public static final int STOP_VPN_WAITING_TIME = 5;

	// Start connection to a PPTP server
	public static boolean startVpn(VpnProfile p, Context context) {

		// Check
		if (TextUtils.isEmpty(p.server) || TextUtils.isEmpty(p.username) || TextUtils.isEmpty(p.password)) {
			return false;
		}

		// Iface
		String iface = getDefaultIface();
		// Arguments to mtpd
		String[] args = new String[] { iface, "pptp", p.server, "1723", "name", p.username, "password", p.password,
				"linkname", "vpn", "refuse-eap", "nodefaultroute", "idle", String.valueOf(GlobalConstant.VPN_IDLE_TIME),
				"mtu", "1400", "mru", "1400", (p.mppe ? "+mppe" : "nomppe"), "unit", PPP_UNIT };
				// String[] args = new String[] { iface, "pptp", p.server,
				// "1723", "name", p.username, "password", p.password,
				// "linkname", "vpn",
				// "refuse-eap","refuse-pap","refuse-chap","refuse-mschap",
				// "nodefaultroute", "idle", "10", "mtu", "1400", "mru", "1400",
				// (p.mppe ? "+mppe" : "nomppe"), "unit", PPP_UNIT };

		// Start
		startMtpd(args);

		// Wait for mtpd
		if (!blockUntilStarted()) {
			return false;
		}

		// Set up ip route
		setupRoute();

		// Set up dns
		setupDns(p);

		NoticUtil.showVpn(context);

		return true;
	}

	public static boolean startAutoVpn(VpnProfile p, Context context) {

		// Iface
		String iface = getDefaultIface();
		// Arguments to mtpd
		String[] args = new String[] { iface, "pptp", p.server, "1723", "name", p.username, "password", p.password,
				"linkname", "vpn", "refuse-eap", "nodefaultroute", "idle", String.valueOf(GlobalConstant.VPN_IDLE_TIME),
				"mtu", "1400", "mru", "1400", (p.mppe ? "+mppe" : "nomppe"), "unit", PPP_UNIT };

		// Start
		startMtpd(args);

		// Wait for mtpd
		if (!blockUntilStartedAuto(context)) {
			return false;
		}

		// Set up ip route
		setupRoute();

		// Set up dns
		setupDns(p);

		NoticUtil.showVpn(context);

		return true;
	}

	public static void stopVpn2() {
		try {
			
			StringBuilder s = new StringBuilder();
			s.append("netcfg " + PPP_INTERFACE + " down").append("\n");
			Process p = RunCommand.run(s.toString());
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void stopVpnAuto(Context context) {
		// Kill all vpn stuff
		// iptables –t nat –F-------清除nat表中所有规则
		StringBuilder s = new StringBuilder();
		try {

			Process p = RunCommand.run("ip ro");
			p.waitFor();
			String out = RunCommand.readInput(p);

			String[] split = out.split("\n");
			for (String string : split) {
				if (!TextUtils.isEmpty(string)) {
					int lastIndexOf = string.lastIndexOf(VpnManager.PPP_INTERFACE);
					if (lastIndexOf > 0) {
						String substring = string.substring(0, lastIndexOf + VpnManager.PPP_INTERFACE.length());
						s.append("ip ro del " + substring).append("\n");
					}
				}
			}

			s.append("ip ro del from all table 200 \n").append("pkill mtpd\n").append("pkill pppd\n")
					.append("ip ro flush dev\n").append(PPP_INTERFACE).append("\n").append("iptables -t nat -F\n")
					.append("iptables -t nat -X\n");
			RunCommand.run(s.toString());

			for (int i = STOP_VPN_WAITING_TIME; i > 0; i--) {
				AutoActivity.sendHandlerOnMain(context.getResources().getString(R.string.disconnectting_vpn) + "  " + i,
						context);
				boolean started = isStarted();
				if (!started && i < STOP_VPN_WAITING_TIME - 1)
					return;
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			Log.v(GlobalConstant.MYTAG, "---stopVpn--Exception--" + e.toString());
		}
	}

	public static boolean isVpnRunning() {
		try {
			Process p = RunCommand.run("pgrep mtpd");
			p.waitFor();
			if (!TextUtils.isEmpty(RunCommand.readInput(p).replace("\n", "").trim())) {
				return true;
			}
		} catch (Exception e) {

		}

		return false;
	}

	private static String getDefaultIface() {
		String routes;

		try {
			Process p = RunCommand.run("ip ro");
			p.waitFor();
			routes = RunCommand.readInput(p);
		} catch (Exception e) {
			routes = null;
		}

		if (routes != null) {
			for (String route : routes.split("\n")) {
				if (route.startsWith("default")) {
					String iface = null;
					boolean last = false;
					for (String ele : route.split(" ")) {
						if (last) {
							iface = ele;
							break;
						} else if (ele.equals("dev")) {
							last = true;
						}
					}

					if (iface != null) {
						return iface;
					} else {
						break;
					}
				}
			}
		}

		// Can't load default interface? That's not possible.
		return "eth0";
	}

	private static void startMtpd(String[] args) {
		StringBuilder s = new StringBuilder();
		s.append("mtpd");
		// Add args
		for (String arg : args) {
			s.append(" ").append(arg);
		}

		// Run
		try {
			RunCommand.run(s.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private static boolean blockUntilStarted() {
		int n = MAX_WAIT_TIME * 2;
		for (int i = 0; i < n; i++) {
			try {
				Process p = RunCommand.run("ip ro");
				p.waitFor();
				String out = RunCommand.readInput(p);
				if (out.contains(PPP_INTERFACE)) {
					return true;
				} else {

					Thread.sleep(500);
				}

			} catch (Exception e) {
				break;
			}
		}

		return false;
	}

	private static boolean blockUntilStartedAuto(Context context) {
		for (int i = MAX_WAIT_TIME; i > 0; i--) {
			try {
				Process p = RunCommand.run("ip ro");
				p.waitFor();
				String out = RunCommand.readInput(p);

				if (out.contains(PPP_INTERFACE)) {
					return true;
				} else {
					Thread.sleep(1000);
					AutoActivity.sendHandlerOnMain(
							context.getResources().getString(R.string.connectting_vpn) + "  " + i, context);
				}
			} catch (Exception e) {
				break;
			}
		}

		return false;
	}

	public static boolean isStarted() {

		try {
			Process p = RunCommand.run("ip ro");
			p.waitFor();
			String out = RunCommand.readInput(p);
			String[] split = out.split("\n");
			int count = 0;
			for (String string : split) {
				if (string.contains(PPP_INTERFACE)) {
					count++;
					if (count == 3) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private static void setupRoute() {

		try {
			StringBuilder s = new StringBuilder();
			s.append("netcfg " + PPP_INTERFACE + " up\n").append("ip ro add 0.0.0.0/1 dev ").append(PPP_INTERFACE)
					.append("\n").append("ip ro add 128.0.0.0/1 dev ").append(PPP_INTERFACE).append("\n");
			RunCommand.run(s.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void setupDns(VpnProfile profile) {

		String dns1 = null, dns2 = null;

		try {
			Process p = RunCommand.run("getprop net.dns1");
			p.waitFor();
			dns1 = RunCommand.readInput(p).replace("\n", "").trim();
			p = RunCommand.run("getprop net.dns2");
			p.waitFor();
			dns2 = RunCommand.readInput(p).replace("\n", "").trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (TextUtils.isEmpty(dns1) || TextUtils.isEmpty(dns2)) {
			return;
		}

		StringBuilder s = new StringBuilder();
		s.append("iptables -t nat -A OUTPUT -d ").append(dns1).append("/32 -o ").append(PPP_INTERFACE)
				.append(" -p udp -m udp --dport 53 -j DNAT --to-destination ").append(profile.dns1).append(":53\n")
				.append("iptables -t nat -A OUTPUT -d ").append(dns2).append("/32 -o ").append(PPP_INTERFACE)
				.append(" -p udp -m udp --dport 53 -j DNAT --to-destination ").append(profile.dns2).append(":53");

		try {
			RunCommand.run(s.toString()).waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
