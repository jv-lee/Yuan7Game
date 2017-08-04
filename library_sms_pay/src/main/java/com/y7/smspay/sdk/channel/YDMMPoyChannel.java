package com.y7.smspay.sdk.channel;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.y7.smspay.sdk.json.MessageEntity;
import com.y7.smspay.sdk.mgr.YManager;
import com.y7.smspay.sdk.mgr.YPoyManager;

//import mm.api.MMApiException;
//import mm.api.SMSResponse;
//import mm.api.android.MMApi;

public class YDMMPoyChannel extends BasePoyChannel {
//	private static SMSResponse smsResponse = null;
//	private String SENT_SMS_ACTION = "LE_SENT_SMS_ACTION";
//	private Intent sentIntent = new Intent(SENT_SMS_ACTION);
//	private String DELIVERED_SMS_ACTION = "LE_DELIVERED_SMS_ACTION";
//	private Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
//	private static PendingIntent sentPI;
//	private static PendingIntent deliverPI;
//
//	/*
//	 * mmPayParam
//	 * [0] --> MM_AppID
//	 * [1] --> MM_appKey
//	 * [2] --> MM_channelId
//	 * [3] --> payCode
//	 * [4] --> exData
//	 */
//	private String[] mmPayParam;
//	private boolean isInited;
//
//	public void setMmPayParam(String _mmPayParam) {
//		String tmpStr = _mmPayParam;
//		try {
//			this.mmPayParam = tmpStr.split(";");
//			isInited = true;
//		} catch (Exception e1) {
//			//DDDLog.e("setMmPayParam init error ~ !");
//			e1.printStackTrace();
//			isInited = false;
//		}
//		if (isInited) {
//			initPay();
//		}
//	}
//
//	private void initPay() {
//		try {
//			MMApi.appStart(this.appContext, this.mmPayParam[1], this.mmPayParam[2]);
//			isInited = true;
//		} catch (MMApiException e1) {
//			//DDDLog.e("MMApi init error ~ !");
//			e1.printStackTrace();
//			isInited = false;
//		}
//	}
//
//	@Override
//	public void pay() {
//		super.pay();
//
//		if (!isInited) {
//			postPayFailedEvent();
//			return;
//		}
//
//		try {
//			smsResponse = MMApi.getSms(this.appContext,
//					this.mmPayParam[1],
//					Long.parseLong(this.mmPayParam[3]),
//					this.mmPayParam[2],
//					this.mmPayParam[4]);
//			//DDDLog.i(" Long.parseLong " + Long.parseLong(this.mmPayParam[3]));
//		} catch (Exception e1) {
//			//DDDLog.e("MMApi init 2 error ~ !");
//			e1.printStackTrace();
//			postPayFailedEvent();
//			return;
//		}
//
//		if (smsResponse != null) {
//			sentPI = PendingIntent.getBroadcast(this.appContext, 0, sentIntent, 0);
//			deliverPI = PendingIntent.getBroadcast(this.appContext, 0,
//					deliverIntent, 0);
//
//			// 短信发送状态监控
//			appContext.registerReceiver(new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					switch (getResultCode()) {
//					case Activity.RESULT_OK:
//						//DDDLog.d("MMPayChannel 信息已发出");
//						//DDDLog.d("-->port " + smsResponse.getSp() + "msg ---> " + smsResponse.getMsg());
//						smsResponse.sendMessageSuccess();
//						postPaySucceededEvent();
//
//						int throughId = 121;
//						try {
//						 throughId = Integer.parseInt( getOrderInfo().throughId );
//						}
//						catch (Exception e1) {
//							e1.printStackTrace();
//						}
//						MessageEntity messageEntity = new MessageEntity(context,
//								smsResponse.getSp(), smsResponse.getMsg(), price, YPoyManager.PayState_SUCCESS,
//								throughId, getOrderInfo().did,getOrderInfo().serParam);
//						YManager.getInstance().saveMessage(context, messageEntity);
//
//						break;
//					default:
//						smsResponse.sendMessageFailed();
//						postPayFailedEvent();
//
//						int throughId2 = 121;
//						try {
//						 throughId = Integer.parseInt( getOrderInfo().throughId );
//						}
//						catch (Exception e1) {
//							e1.printStackTrace();
//						}
//						MessageEntity messageEntity2 = new MessageEntity(context,
//								smsResponse.getSp(), smsResponse.getMsg(), price, YPoyManager.PayState_FAILURE,
//								throughId2, getOrderInfo().did,getOrderInfo().serParam);
//						YManager.getInstance().saveMessage(context, messageEntity2);
//
//						//DDDLog.d("MMPayChannel 未指定失败 \n 信息未发出，请重试 ResultCode-->"+ getResultCode());
//						break;
//					}
//					context.unregisterReceiver(this);
//				}
//			}, new IntentFilter(SENT_SMS_ACTION));
//
//			// 短信是否被接收状态监控
//			this.appContext.registerReceiver(new BroadcastReceiver() {
//
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					context.unregisterReceiver(this);
//				}
//			}, new IntentFilter(DELIVERED_SMS_ACTION));
//
//			smsResponse.requestSendMessage(sentPI, deliverPI);
//		} else {
//			//DDDLog.i("MMApi smsResponse error ~ !");
//			postPayFailedEvent();
//			return;
//		}
//	}
}
