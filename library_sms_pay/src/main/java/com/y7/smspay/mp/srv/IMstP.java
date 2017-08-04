package com.y7.smspay.mp.srv;


import android.content.Context;
import android.content.Intent;

public interface IMstP {
	boolean onReceive(Intent intent);
	
	public void Init(Context ctx);

	public void reqChannelId(Context ctx, String price, int payItemID,
                             String str, String product, String Did, String extData,
                             MPoyCallback receiver);
	
	public String getNeedPayList(Context ctx);
}
