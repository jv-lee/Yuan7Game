package com.y7.smspay.sdk.channel;

import com.y7.smspay.sdk.util.HttpSdSsTool;


/**
 * 永正 wo+
 * @author Administrator
 * 
 */
public class YZRDOPoyChannel extends BasePoyChannel {

	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("YZRDOPayChannel begin");
		HttpSdSsTool.ISREQUESCODE = HttpSdSsTool.YZRDO;
		return;
	}

	public static String getCallPayUrl(String url)
	{
		//DDDLog.d("url.substring(4) -->" + url.substring(4));
		return url.substring(4);
	}

	/**
	 * 计费验证接口
	 * 
	 * @param code
	 * @return
	 */
	public static String getcallBackUrl(String code) {
		
		// 拼代码
		String httpUrl = "http://182.50.1.131:9000/ds/yzrdoy.jsp";
		String urlReq = "linkid=" + linkid; // req-计费请求 ver-计费验证码
		String paymentCode = "code=" + code;
		String endUrl = httpUrl + "?" + urlReq + "&" + paymentCode ;
		//DDDLog.d("获取验证指令url:" + endUrl);
		return endUrl;
	}

	private static String linkid;
}
