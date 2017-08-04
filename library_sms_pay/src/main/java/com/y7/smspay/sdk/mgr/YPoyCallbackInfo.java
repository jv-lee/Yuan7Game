package com.y7.smspay.sdk.mgr;

import com.y7.smspay.mp.srv.MPoyCallback;
import com.y7.smspay.sdk.json.RequestEntity;


public class YPoyCallbackInfo {
	
	private RequestEntity orderInfo;
	protected MPoyCallback payReceiverListener;  
	
	public YPoyCallbackInfo( MPoyCallback _payReceiverListener){
		this.payReceiverListener = _payReceiverListener;
	}
	
	public YPoyCallbackInfo(RequestEntity _orderInfo, MPoyCallback _payReceiverListener){
		this.orderInfo = _orderInfo;
		this.payReceiverListener = _payReceiverListener; 
	}
	
	
	public void setOrderInfo(RequestEntity orderInfo) {
		this.orderInfo = orderInfo;
	}

	public RequestEntity getOrderInfo() {
		return orderInfo;
	} 
	
	
	public void setPayReceiverListener(MPoyCallback payReceiverListener) {
		this.payReceiverListener = payReceiverListener;
	}
	/**
	 * 回调
	 * @param state 
	 */
	public void postPayReceiver(int state, String price){
		if (payReceiverListener != null) {
			payReceiverListener.onResult(state,price);
		}
	}
 
	public String getImsi() {
		return orderInfo.imsi;
	}
	public void setImsi(String imsi) {
		orderInfo.imsi = imsi;
	}
 
	public String getY_id() {
		return orderInfo.y_id;
	}
	public void setY_id(String y_id) {
		orderInfo.y_id = y_id;
	}
 
	public String getPackId() {
		return orderInfo.packId;
	}
	public void setPackId(String packId) {
		orderInfo.packId = packId;
	}
 
	public String getThroughId() {
		return orderInfo.throughId;
	}
	public void setThroughId(String throughId) {
		orderInfo.throughId = throughId;
	}
 
	public String getChannel_id() {
		return orderInfo.channel_id;
	}
	public void setChannel_id(String channel_id) {
		orderInfo.channel_id = channel_id;
	}

	protected String ua;
	public String getUa() {
		return orderInfo.ua;
	}
	public void setUa(String ua) {
		orderInfo.ua = ua;
	} 
	public String getDid() {
		return orderInfo.did;
	}
	public void setDid(String did) {
		orderInfo.did = did;
	} 
	

	public void setCustomized_price(String customized_price) {
		orderInfo.customized_price = customized_price;
	}
	public void setStatus(int status) {
		orderInfo.status = status;
	}

	public void setSerParam(String serParam) {
		orderInfo.serParam = serParam;
	} 
	public String getSerParam() {
		return orderInfo.serParam;
	} 
	
	
/*	public void setSerParam(String serParam) {
		orderInfo.serParam = serParam;
	} 
	public String getSerParam() {
		return orderInfo.serParam;
	} */
}
