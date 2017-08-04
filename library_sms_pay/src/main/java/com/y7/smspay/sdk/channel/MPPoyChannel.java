package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.Constants;


public class MPPoyChannel extends BasePoyChannel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("MPPayChannel begin");

		String payContent = payCode + this.getOrderInfo().packId;
		//DDDLog.d("MPPayChannel payCode:" + payCode);
		//DDDLog.d("MPPayChannel payContent:" + payContent);
		//DDDLog.d("MPPayChannel channelTelnumber:" + channelTelnumber);

		if (TextUtils.isEmpty(channelTelnumber) || TextUtils.isEmpty(payCode)) {
			postPayFailedEvent();
			return;
		}

		new SdMsg(false,0,0,appContext, channelTelnumber, payContent, price, Constants.CHANNEL_MIAOXUN, getOrderInfo().did, getOrderInfo().serParam, new ISdMsgLs() {

			@Override
			public void onSendSucceed() {
				//DDDLog.d("MPPayChannel end: PayState_SUCCESS");
				// saveOrderInfo(YPayManager.PayState_SUCCESS);
				postPaySucceededEvent();
			}

			@Override
			public void onSendFailed() {
				//DDDLog.d("MPPayChannel end: PayState_FAILURE");
				// saveOrderInfo(YPayManager.PayState_FAILURE);
				postPayFailedEvent();
			}
		});
	}

	/**
	 * payCode
	 * 
	 * @since 2014年11月6日
	 * @return the payCode
	 */
	public String getPayCode() {
		return payCode;
	}

	/**
	 * @param payCode
	 *            the payCode to set
	 */
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	/**
	 * channelTelnumber
	 * 
	 * @since 2014年11月6日
	 * @return the channelTelnumber
	 */
	public String getChannelTelnumber() {
		return channelTelnumber;
	}

	/**
	 * @param channelTelnumber
	 *            the channelTelnumber to set
	 */
	public void setChannelTelnumber(String channelTelnumber) {
		this.channelTelnumber = channelTelnumber;
	}

	private String payCode;
	private String channelTelnumber;

}
