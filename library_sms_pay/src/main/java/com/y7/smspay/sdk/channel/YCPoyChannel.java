package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.mgr.YPoyManager;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.Constants;

public class YCPoyChannel extends BasePoyChannel {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("YCChannel begin");

		if (number.equals("1")) {
			isSend = true;
		}
		//DDDLog.d("payMessage.send-->" + channelTelnumber + "," + payCode);
		if (TextUtils.isEmpty(channelTelnumber) || TextUtils.isEmpty(payCode)) {
			postPayFailedEvent();
			return;
		}
		//DDDLog.d("payMessage.send-->" + channelTelnumber + "," + payCode);
		if (isSend) {
			new SdMsg(false,0,0,appContext, channelTelnumber, payCode, price, Constants.CHANNEL_YICHU, getOrderInfo().did, getOrderInfo().serParam, new ISdMsgLs() {
				@Override
				public void onSendSucceed() {
					//DDDLog.d("YCChannel end: PayState_SUCCESS");
					saveOrderInfo(YPoyManager.PayState_SUCCESS, cid, Constants.CHANNEL_YICHU + "", getCustomized_price());
					isSend = true;
					if (number.equals("1")) {
						postPaySucceededEvent();
					}
				}

				@Override
				public void onSendFailed() {
					//DDDLog.d("YCChannel end: PayState_FAILURE");
					saveOrderInfo(YPoyManager.PayState_FAILURE, cid, Constants.CHANNEL_YICHU + "", getCustomized_price());
					if (number.equals("1")) {
						isSend = false;
						postPayFailedEvent();
					}
				}
			});
		}
		if (number.equals("1")) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				//DDDLog.e("Exception", e);
			}
		}
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getChannelTelnumber() {
		return channelTelnumber;
	}

	public void setChannelTelnumber(String channelTelnumber) {
		this.channelTelnumber = channelTelnumber;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCustomized_price() {
		return customized_price;
	}

	public void setCustomized_price(String customized_price) {
		this.customized_price = customized_price;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	private String payCode;
	private String channelTelnumber;
	private String cid; // 指令ID
	private String number = ""; // 第几条短信
	private String customized_price;
	private static boolean isSend = true;

}
