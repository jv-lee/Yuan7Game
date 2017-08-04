package com.y7.smspay.sdk.mgr;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

//二次确认逻辑
public class SDlgHandle extends Handler {
	private Context context;
	private static String customized_price;
	private static String tipInfo;
	private static String product;
	private static String extData;
	private static String Did;
	private YPoyCallbackInfo cb;
	private Boolean skipSecondConfirm;
	//测试开关
	public static final Boolean isDebug = false;

	public SDlgHandle(Context _context,
			String _customized_price, String _tipInfo, String _product,
			String _extData, final String _Did, final YPoyCallbackInfo _cb,
			final Boolean _skipSecondConfirm) {
		super(_context.getMainLooper());
		context = _context;
		customized_price = _customized_price;
		tipInfo = _tipInfo;
		product = _product;
		extData = _extData;
		Did = _Did;
		cb = _cb;
		skipSecondConfirm = _skipSecondConfirm;
		
		
	}

	@Override
	public void handleMessage(Message msg) {
		
		//DDDLog.e("handleMessage -->"+ msg.what);
		
		if (msg.what == 1001) {
			showDialog(context, customized_price, tipInfo, product,null,null, cb);
		}
		
	}

	/**
	 * 2014-10-09 added by pengbb 新增二次确认信息提示
	 * 
	 * @param ctx
	 * @param price
	 * @param str
	 * @param positiveButton
	 * @param negativeButton
	 * 2016-03-04 修改2次确认 zkE_
	 */
	private static void showDialog(final Context ctx, String price, String str,String objProduct,
			OnClickListener positiveButton,
			OnClickListener negativeButton,
			final YPoyCallbackInfo cb) {

		//select (*) where id = 1 or id = 2 or id

		// sevice 不可用
		Builder builder = new Builder(ctx);
//		builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		if (TextUtils.isEmpty(str)) {
			double priceShowValue = 0;
			try {
			} catch (Exception ignore) {
				builder.setMessage("道具名称:" + objProduct + "\n" +"您确定要支付" + price + "元吗？(不含通讯费,由运营商代收.)");
			}
			builder.setMessage("道具名称:" + objProduct + "\n" +"您确定要支付" + priceShowValue + "元吗？(不含通讯费,由运营商代收.)");
		} else {
			builder.setMessage(str);
		}
		final String p = price;
		builder.setTitle("提示");
		// builder.setCancelable(false);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//cb.postPayReceiver(YPoyManager.PayState_SUCCESS);
				YManager.getInstance().reqChannelId(ctx, customized_price,product, extData, Did, cb);
			}
		});
//		if (negativeButton != null) {
//			
//		}
		builder.setNegativeButton("取消", negativeButton);
		
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				cb.postPayReceiver(YPoyManager.PayState_CANCEL,"1");
				
			}
		});
		builder.create().show();
		
	}
}
