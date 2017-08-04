package com.y7.smspay.sdk.util;

/**
 * 网络请求支付监听
 * @author xingjian.peng
 *
 */
public interface IHttpRequestLs {
	void onSendSucceed();
	void onSendFailed();
}
