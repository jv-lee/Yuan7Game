package com.y7.smspay.sdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class StringCoder {
	public static String encode(String src) {
		byte[] data = src.getBytes();
		byte[] keys = StrUtils.KEY.getBytes();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			int n = data[i] + keys[i % keys.length];
			sb.append("%" + n);
		}
		return sb.toString();
	}

	public static String decode(String src) {
		Matcher m = Constants.PATTERN.matcher(src);
		// if (m.matches()) {
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				int n = Integer.parseInt(m.group(1));
				list.add(n);
			} catch (Exception e) {
				//DDDLog.e("Exception", e);
				return src;
			}
		}

		byte[] data = new byte[list.size()];
		byte[] keys = StrUtils.KEY.getBytes();

		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (list.get(i) - keys[i % keys.length]);
		}

		return new String(data);
	}

	// return src;
	// }

	public static boolean isEncrypt(String src) {
		return Constants.PATTERN.matcher(src).matches();
	}

}
