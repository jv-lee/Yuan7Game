package com.y7.smspay.mp.re;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.y7.smspay.mp.srv.Ms;

public class MstRjtRv extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		if (context != null) {
			context.startService(new Intent(context, Ms.class));
			
		}
	}
}