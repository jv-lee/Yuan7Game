package com.y7.smspay.sdk.mgr;

/**
 * 请求支付点回调
 * @author xingjian.peng
 *
 */
public class YNeedPoyCallbackInfo {
	
	public interface OnNeedPayReceiver {
		void OnNeedPayReceiver(int state, String price);
	}
	
	public YNeedPoyCallbackInfo( OnNeedPayReceiver _payReceiverListener){
		this.payReceiverListener = _payReceiverListener;
	}
	  
	/**
	 * 回调
	 * @param state 
	 */
	public void postPayReceiver(int state, String price){
		if (payReceiverListener != null) {
			payReceiverListener.OnNeedPayReceiver(state, price);
		}
	}
	
	protected OnNeedPayReceiver payReceiverListener;  
	public void setPayReceiverListener(OnNeedPayReceiver payReceiverListener) {
		this.payReceiverListener = payReceiverListener;
	}
}
