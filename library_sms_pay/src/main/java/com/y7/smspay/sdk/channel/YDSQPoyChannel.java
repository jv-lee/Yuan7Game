package com.y7.smspay.sdk.channel;

import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.StrUtils;


/**
 * 移动书券
 * @author Administrator
 * 
 */
public class YDSQPoyChannel extends BasePoyChannel {
	
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("begin");
		HttpSdSsTool.ISREQUESCODE = HttpSdSsTool.YDSQ;
	}
	
	/**
	 * 计费验证接口
	 * 
	 * http://localhost:8080/gwzoo2/pinterface/PhoneAPIAction!parsingCode?throughid=101&imsi=1111111111&code=555555555&packId=11&did=22
	 * 
	 *  throughid  通道id
		imsi
		code 验证码
		packId 包id
		did 道具id

	 * @return
	 */
	public static String getcallBackUrl(String code) {
		
		String httpUrl = StrUtils.SERVER_URL+"/gwzoo/pinterface/PhoneAPIAction!parsingCode";
		String paymentCode = "code=" + code;
		
		if(Constants.isTest){
			paymentCode = "code=debug" + code;
		}
		
		String endUrl = httpUrl + "?"  + paymentCode + "&" + HttpSdSsTool.YDSQStr ;
		//DDDLog.e("获取验证指令url:" + endUrl);
		return endUrl;
	}
}
