package com.y7.smspay.sdk.util;

public class Kode {
	// 1.1
	public static int[] k = new int[] { 17, 50, 97, 4, 94, 34, 66, 22, 24, 70 };

	public static String a(String str) {
		if (str == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		//sb.append("s");
		char[] result = str.toCharArray();
		try {
			for (int i = 0; i < result.length; i++) {
				char c = result[i];
				char cc = (char) (c + k[i % k.length]);
				sb.append(cc);
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}
		return sb.toString();
	}

	public static String e(String code) {
		if (code == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(code);
		char[] result = sb.toString().toCharArray();
		sb = new StringBuilder();
		try {
			for (int i = 0; i < result.length; i++) {
				char c = result[i];
				char cc = (char) (c - k[i % k.length]);
				sb.append(cc);
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}

		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("a:" + e("123"));
	}
}
