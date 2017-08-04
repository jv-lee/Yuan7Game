package com.y7.smspay.sdk.channel;

public interface IPoyChannelListener {
	void onPaySucceeded();
	void onPayFailed();
	void onPayCanceled();
}
