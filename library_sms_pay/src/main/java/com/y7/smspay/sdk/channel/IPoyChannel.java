package com.y7.smspay.sdk.channel;

public interface IPoyChannel {
	void addPayChannelListener(IPoyChannelListener newPayChannelListener);
	void removePayChannelListener(IPoyChannelListener newPayChannelListener);
	void pay(); 
}
