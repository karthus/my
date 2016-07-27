package com.hdj.hook.util;

import java.security.MessageDigest;

/**
 * 
 * @author dyt
 *
 * description:å­—ç¬¦ä¸²MD5ç®?•åŠ å¯†
 *
 * Time:2016å¹?æœ?4æ—?ä¸‹åˆ1:13:06
 */
public class MD5Encoder {

	public static String encode(String string) throws Exception {
		byte[] hash = MessageDigest.getInstance("MD5").digest(
				string.getBytes("UTF-8"));
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
