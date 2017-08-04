package com.y7.smspay.sdk.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: OfflineSmsToolByDemo
 * @Description: 离线短信工具加密计算类
 * @author 很灰很太狼
 * @date
 */
public class OfflineSsTool {
	/**
	 * 剔除&符号,由于&作为分隔符
	 */
	public static final String baseAscII = " !\"#$%'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	public static final BigInteger baseNum = BigInteger.valueOf(baseAscII.length());

	/**
	 * 签名算法
	 * 
	 * @param value
	 *            待签名数据
	 * @return 安全码
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String sig(String data, String apSecert) throws NoSuchAlgorithmException, IOException {
		assert null != data : "签名数据不能为空";
		assert null != apSecert : "apSecert不能为空";
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(apSecert.getBytes());

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data.getBytes());
		stream.write(md5.digest());
		md5.update(stream.toByteArray());
		byte[] md = md5.digest();

		byte[] ret = new byte[5]; // 首字节保留0 防止转换成负数, 只使用后4位
		for (int i = 0; i < md.length; i++) {
			ret[i % 4 + 1] ^= md[i];
		}
		return toBase94(new BigInteger(ret));
	}

	/**
	 * Base94 编码
	 * 
	 * @param value
	 *            待转换成base94的数字
	 */
	public static String toBase94(long value) {
		// return value + "";
		return toBase94(BigInteger.valueOf(value));
	}

	/**
	 * Base94 编码
	 * 
	 * @param value
	 *            待转换成base94的数字
	 */
	public static String toBase94(BigInteger value) {
		assert value.compareTo(BigInteger.ZERO) >= 0 : "toBase94 值必须大于等于0";
		StringBuilder result = new StringBuilder();
		while (value.compareTo(BigInteger.ZERO) > 0) {
			BigInteger[] bis = value.divideAndRemainder(baseNum);
			result.append(baseAscII.charAt(bis[1].intValue()));
			value = bis[0];
		}
		return result.reverse().toString();
	}

	/**
	 * Base94 解码
	 * 
	 * @param value
	 *            待解码的base94的字符串
	 */
	public static BigInteger fromBase94(String value) {
		assert null != value : "不是一个有效的Base94字符串: null";
		BigInteger result = BigInteger.ZERO;
		for (char c : value.toCharArray()) {
			int i = baseAscII.indexOf(c);
			assert i >= 0 : "不是一个有效的Base94字符串:" + value;
			result = result.multiply(baseNum).add(BigInteger.valueOf(i));
		}
		return result;
	}

	/**
	 * 获取当前时间（base94的分钟数）
	 * 
	 * @return
	 */
	public static String getCurTime() {
		return toBase94(System.currentTimeMillis() / 60000);
	}

	/**
	 * 获取订单号
	 * 
	 * imei后4位加上时间，时间精确到毫秒
	 * 
	 * @return
	 */
	public static Long getOrderNumber(Context context) {
		String imei = Utils.getIMEI(context);
		try {
			return Long.parseLong(imei.substring(imei.length() - 4, imei.length()) + System.currentTimeMillis());
		} catch (Exception e) {
			return System.currentTimeMillis();
		}
	}

	/**
	 * 
	 * @param mContext
	 * @param customized_price
	 *            金额
	 * @param orderNumber
	 *            订单号
	 * @param THChannelID
	 *            后台传过来的渠道号
	 * @param ThroughId
	 *            通道ID
	 * @param MD5
	 *            MD5码
	 * @return
	 */
	public static String getContent(Context mContext, String customized_price, Long orderNumber, Long THChannelID, String ThroughId, String MD5, String did) {
		Long imsi = 0L;
		int price = 0;
		try {
			imsi = Long.parseLong(Utils.getIMSI(mContext));
		} catch (Exception e) {
			imsi = 0L;
		}
		try {
			price = Integer.parseInt(customized_price);
		} catch (Exception e) {
			price = 0;
		}
		StringBuilder content = new StringBuilder();
		content.append(toBase94(1)).append("&")// 程序包ID
				.append(getCurTime()).append("&")// 时间
				.append(toBase94(price)).append("&")// 金额
				.append(toBase94(orderNumber)).append("&")// 第三方事务号
				.append(toBase94(THChannelID)).append("&")// 渠道号
				.append(toBase94(1)).append("&")// 版本号
				.append(toBase94(imsi)).append("&")// IMSI
				.append("gw" + ThroughId + "x" + Utils.getPackId(mContext) + "x" + did );// EXDATA
		try {
			String sig = sig(content.toString(), MD5);// 生成sig，其中0E2C83D839C026A5E0530100007FEB90是Apsecret
			content.append("&").append(sig);// 最后把sig拼接进去
		} catch (Exception e) {
		}
		return content.toString();
	}
}
