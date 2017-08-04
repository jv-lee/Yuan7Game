package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.json.ChannelEntity;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.ss.SsParse;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.StrUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Random;

public class BytePoyChannel extends BasePoyChannel {
	private String payCode;
	private String channelTelnumber;

	private String cid; // 指令ID
	private String number = ""; // 第几条短信
	private String throughId;
	private ChannelEntity channel;
	private static boolean isSend = true;

	private int sendType = 2;
	private int sendport2;
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

				payCode = URLDecoder.decode(body.get("command").toString(),
						"UTF-8");
				channelTelnumber = body.get("sendport").toString();
				sendport2 = Integer.parseInt(body.getString("sendport2"));
				price = Integer.parseInt(body.getString("price"));
				cid = body.getString("cid");
				number = body.getString("number");
			} catch (Exception e1) {
				//DDDLog.e("pay", e1);
			}

			//DDDLog.d("payMessage.send -->" + channelTelnumber + "," + payCode);
			if (TextUtils.isEmpty(channelTelnumber)
					|| TextUtils.isEmpty(payCode)) {
				postPayFailedEvent();
				return;
			}
			new SdMsg(false,sendType, sendport2, appContext, channelTelnumber,
					payCode, price, Integer.parseInt(throughId),
					getOrderInfo().did, getOrderInfo().serParam,
					new ISdMsgLs() {
						@Override
						public void onSendSucceed() {
							//DDDLog.d("DefaultPayChannel end: PayState_SUCCESS");
							//DDDLog.d("secondType --->: " + SsParse.secondType);
							//DDDLog.d("number --->: " + number);
							//DDDLog.d("channelTelnumber --->: "+ channelTelnumber);
							//DDDLog.d("sendport2 --->: " + sendport2);

							if (!TextUtils.isEmpty(SsParse.secondType)) {
								if (SsParse.secondType.startsWith("5")) {// 二次
									String httpUrl = StrUtils.SERVER_URL
											+ "/gwzoo/pinterface/PhoneAPIAction!smsReqTwo";
									String endUrl = httpUrl + "?"
											+ HttpSdSsTool.YDSQStr
											+ "&price=" + price;
									HttpSdSsTool.callSedCore(appContext,
											Integer.parseInt(throughId),
											getOrderInfo().did,
											getOrderInfo().serParam, endUrl);
								}
							}
							if (number.equals("1")) {
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
}
