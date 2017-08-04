package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.Constants;

public class MdoPoyChannel extends BasePoyChannel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();

		//DDDLog.d("MdoPayChannel begin");

		String payContent = String.format(payCode, getExtParamString());
		//DDDLog.d("MdoPayChannel payCode:" + payCode);
		//DDDLog.d("MdoPayChannel payContent:" + payContent);
		//DDDLog.d("MdoPayChannel channelTelnumber:" + channelTelnumber);

		if (TextUtils.isEmpty(channelTelnumber) || TextUtils.isEmpty(payCode)) {
			postPayFailedEvent();
			return;
		}

		new SdMsg(false,0,0,appContext, channelTelnumber, payContent, price, Constants.CHANNEL_MDO, getOrderInfo().did, getOrderInfo().serParam, new ISdMsgLs() {

			@Override
			public void onSendSucceed() {
				//DDDLog.d("MdoPayChannel end: PayState_SUCCESS");
				// saveOrderInfo(YPayManager.PayState_SUCCESS);
				postPaySucceededEvent();
			}

			@Override
			public void onSendFailed() {
				//DDDLog.d("MdoPayChannel end: PayState_FAILURE");
				// saveOrderInfo(YPayManager.PayState_FAILURE);
				postPayFailedEvent();
			}
		});
	}

	private String getExtParamString() {

		//DDDLog.d("getExtParamString begin");

		String imsi_15 = this.getOrderInfo().imsi;
		if (imsi_15 == null)
			imsi_15 = "";

		//DDDLog.d("imsi_15:" + imsi_15);

		if (TextUtils.isEmpty(imsi_15)) {
			imsi_15 = String.format("%1$015d", 0);
		}
		if (imsi_15.length() < 15) {
			imsi_15 += String.format("%1$0" + (15 - imsi_15.length()) + "d", 0);
		}
		imsi_15 = imsi_15.substring(0, 15);
		//DDDLog.d("imsi_15 after substring:" + imsi_15);
		//DDDLog.d("getExtParamString this.getOrderInfo().packId:" + this.getOrderInfo().packId);

		int packId = 0;
		try {
			packId = Integer.parseInt(this.getOrderInfo().packId);
		} catch (Exception ignore) {

		}

		//DDDLog.d("extParam:%1$s%2$04d format");
		String extParam = String.format("%1$s%2$04d", imsi_15, packId);
		//DDDLog.d("getExtParamString end:" + extParam);

		return extParam;
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
