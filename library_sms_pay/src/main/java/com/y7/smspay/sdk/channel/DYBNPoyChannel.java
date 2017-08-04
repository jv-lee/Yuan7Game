package com.y7.smspay.sdk.channel;

import com.y7.smspay.sdk.mgr.YManager;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.OfflineSsTool;
import com.y7.smspay.sdk.util.Utils;


/**
 * 成都鼎元百纳
 * 
 * @author xingjian.peng
 * 
 */
public class DYBNPoyChannel extends BasePoyChannel {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yc.paysdk.channel.BasePayChannel#pay()
	 */
	@Override
	public void pay() {
		super.pay();
		final Long orderNumber = OfflineSsTool.getOrderNumber(getAppContext());

		//DDDLog.d("channelTelnumber = " + channelTelnumber);
		//DDDLog.d("DYChannelID = " + DYChannelID);
		//DDDLog.d("MD5 = " + MD5);
		//DDDLog.d("DYBNPayChannel begin");
		new SdMsg(false,0,0,appContext, channelTelnumber, OfflineSsTool.getContent(getAppContext(), price + "", orderNumber, DYChannelID, ThroughId, MD5, getOrderInfo().did), price, Integer.parseInt(ThroughId),
				getOrderInfo().did, getOrderInfo().serParam, new ISdMsgLs() {

					@Override
					public void onSendSucceed() {
						//DDDLog.d("DYBNPayChannel end: PayState_SUCCESS");
						// saveOrderInfo(YPayManager.PayState_SUCCESS);
						YManager.getInstance().OrderPayDetail(getAppContext(), orderNumber + "", Utils.getApplicationName(getAppContext()), productName);
						postPaySucceededEvent();
					}

					@Override
					public void onSendFailed() {
						//DDDLog.d("DYBNPayChannel end: PayState_FAILURE");
						// saveOrderInfo(YPayManager.PayState_FAILURE);
						postPayFailedEvent();
					}
				});
	}

	/**
	 * 上行端口号
	 */
	private String channelTelnumber = "";
	/**
	 * 渠道号
	 */
	private Long DYChannelID = 0L;
	/**
	 * 密钥
	 */
	private String MD5 = "";
	/**
	 * 通道ID
	 */
	private String ThroughId;

	public String getThroughId() {
		return ThroughId;
	}

	public void setThroughId(String throughId) {
		ThroughId = throughId;
	}

	public String getChannelTelnumber() {
		return channelTelnumber;
	}

	public void setChannelTelnumber(String channelTelnumber) {
		this.channelTelnumber = channelTelnumber;
	}

	public Long getDYChannelID() {
		return DYChannelID;
	}

	public void setDYChannelID(Long dYChannelID) {
		DYChannelID = dYChannelID;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

}
