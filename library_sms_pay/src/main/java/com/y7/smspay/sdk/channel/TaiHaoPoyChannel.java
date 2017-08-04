package com.y7.smspay.sdk.channel;


import android.text.TextUtils;
import android.util.Base64;

import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
 

//75000*6
//450

/**
 * 泰豪
 * @author Administrator
 * 
 */
public class TaiHaoPoyChannel extends BasePoyChannel {

	@Override
	public void pay() {
		super.pay();
//		postPaySucceededEvent();
//		postPayFailedEvent();
		//DDDLog.d("TaiHaoPayChannel begin");
		if (TextUtils.isEmpty(reqPayUrl)) {
			postPayFailedEvent();
			return;
		}
		//DDDLog.d("reqPayUrl -->" + reqPayUrl);
		String json = GetDataImpl.getInstance(appContext).doRequest(reqPayUrl);
		if (TextUtils.isEmpty(json)) {
			postPayFailedEvent();
			return;
		}
		//DDDLog.d("TaiHaoPayChannel -----" + json);
		try {
			JSONObject jo = new JSONObject(json);
			channelTelnumber = jo.getString("dest");
			smstype = jo.getString("smstype");
			content = jo.getString("content");
		} catch (JSONException e) {
			postPayFailedEvent();
			//DDDLog.d("Exception" + e);
			return;
		}
		//DDDLog.d("channelTelnumber --> " + channelTelnumber);
		//DDDLog.d("smstype --> " + smstype);
		//DDDLog.d("content --> " + content);
		
		// TODO 文档的二进制短信格式没有理解,暂时使用二进制字符串
		if (smstype.equals("data")) {
			 byte[] decoded = Base64.decode(content.getBytes(),Base64.DEFAULT);
			 content = Utils.binary(decoded, 2);
		}
		new SdMsg(false,0,0,appContext, channelTelnumber, content, price, Integer.parseInt(throughId), getOrderInfo().did, getOrderInfo().serParam, new ISdMsgLs() {
			@Override
			public void onSendSucceed() {
				postPaySucceededEvent();
			}

			@Override
			public void onSendFailed() {
				postPayFailedEvent();
			}
		});
		//DDDLog.d("TaiHaoPayChannel end");
	}

	private String reqPayUrl;
	private String channelTelnumber; // 上行端口
	private String smstype; // 上线指令字段格式
	private String content; // 上行指令
	private String throughId;
	
	public String getReqPayUrl() {
		return reqPayUrl;
	}

	public void setReqPayUrl(String reqPayUrl) {
		this.reqPayUrl = reqPayUrl;
	}
	
	public String getThroughId() {
		return throughId;
	}

	public void setThroughId(String throughId) {
		this.throughId = throughId;
	}
}
