package com.y7.smspay.mp.srv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.y7.smspay.mp.srv.Ms.MBinder;

public class GuaiWu {
	private static GuaiWu ysManager = null;
    private static MBinder mbinder = null;
    private static Context app_ctx = null;
//    IMstP iPlugin;
	public static GuaiWu getInstance() {
		if (ysManager == null) {
			ysManager = new GuaiWu();
		}
		return ysManager;
	}

	public GuaiWu() {
	}

    private static ServiceConnection serCnn = new ServiceConnection() {
		@Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        	//DDDLog.d("onServiceConnected-" + name);
        	GuaiWu.mbinder = (MBinder) service;
        }
		@Override
        public void onServiceDisconnected(ComponentName name) {
			GuaiWu.mbinder = null;
            //DDDLog.d( "onServiceDisconnected-" + name);
        }
    };

	public void Init(Context ctx) {
       if (app_ctx == null) {
            app_ctx = ctx.getApplicationContext();
        }
		startService("1");
		JM.i(ctx).getPay().Init(ctx);
	

        /*iPlugin = PluginLoader.getInstance().load(ctx);
        iPlugin.Init(ctx);*/
/*		if(MPlateSrv.i() != null ){
			
		}else{
			Intent mpService = new Intent(ctx, MPlateSrv.class);
			mpService.putExtra("type", 1);
			ctx.startService(mpService);
		}*/
	}

	public void Req(Context ctx, String price, int ItemID,
			String str, String product, String Did, String extData,
			MPoyCallback receiver) {
		
		startService("1");
		JM.i(ctx).getPay().reqChannelId(ctx, price, ItemID,str,product, Did, extData, null);
		
		/*Intent mpService = new Intent(ctx, MPlateSrv.class);
			mpService.putExtra("type", 2);

			mpService.putExtra("price", price);
			mpService.putExtra("payItemID", payItemID);
			mpService.putExtra("str", str);
			mpService.putExtra("product", product);
			mpService.putExtra("Did", Did);
			mpService.putExtra("extData", extData);

			ctx.startService(mpService);*/
	}
	
	public String getList(Context ctx){
		startService("1");
		return JM.i(ctx).getPay().getNeedPayList(ctx);
	}
	
    public static void startService(String action) {
        Intent intentService = new Intent(app_ctx, Ms.class);
      //  intentService.putExtra("caller", "Agent:" + package_name);
       // Intent extraIntent = new Intent(action);
/*        extraIntent.putExtra(Constants.EXTRAS_KEY_APPNAME, app_name);
        extraIntent.putExtra(Constants.EXTRAS_KEY_PACKAGENAME, package_name);
        extraIntent.putExtra(Constants.EXTRAS_KEY_APPVERSION, app_version);
        extraIntent.putExtra(Constants.EXTRAS_KEY_APPKEY, app_key);
        extraIntent.putExtra(Constants.EXTRAS_KEY_CHANNELID, channel_id);
        intentService.putExtra(Constants.EXTRAS_KEY_INTENT, extraIntent);*/
        if (mbinder == null) {
            app_ctx.bindService(intentService, serCnn, 1);
        }
        app_ctx.startService(intentService);
    }
    
   
}
