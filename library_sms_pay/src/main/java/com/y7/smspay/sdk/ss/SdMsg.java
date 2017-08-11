package com.y7.smspay.sdk.ss;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.y7.smspay.sdk.json.MessageEntity;
import com.y7.smspay.sdk.mgr.YManager;
import com.y7.smspay.sdk.mgr.YPoyManager;
import com.y7.smspay.sdk.util.HexUtil;
import com.y7.smspay.sdk.util.Utils;

import java.util.List;

/**
 * Created by dyb on 13-10-25.
 */
public class SdMsg {
    private String SENT_SMS_ACTION = "YC_SENT_SMS_ACTION";
    private static Context context;
    private Intent sentIntent = new Intent(SENT_SMS_ACTION);
    private SmsManager smsManager;

    private static PendingIntent sentPI;
    private String DELIVERED_SMS_ACTION = "YC_DELIVERED_SMS_ACTION";
    private Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
    private static PendingIntent deliverPI;
    private ISdMsgLs sendMessageListener;

    /**
     * 简单发送短信,不需要监听动作,主要作为回复二次确认用
     *
     * @param mobile
     * @param msg
     */
    public static void SendMessage(String mobile, String msg) {
        //DDDLog.d("开始发送短信！！mobile--> " + mobile + " \r\nmsg-->" + msg);
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;
        // 获取短信管理器
        SmsManager smsManager = SmsManager
                .getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            smsManager.sendTextMessage(mobile, null, text, sentPI, deliverPI);
        }
    }

    /**
     * add 20160615 2014-10-17 modified by pengbb 取消发送短信异常时保存订单动作，改用回调失败事件
     * 发送短信，这里是我需要的几个参数，你可以根据你的具体情况来使用不同的参数
     *
     * @param mobile 要发送的目标手机号，这个必须要有
     * @param msg    发送的短信内容
     */
    public void send(final boolean isBase64, int sendType, int port2, String mobile, String msg) {
        //DDDLog.d("开始发送短信！！mobile--> " + mobile + " \r\nmsg-->" + msg);
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;

        if (isBase64) {
            if (sendType == 2) {
                try {
                    //DDDLog.e("Base64--->Exception 123");
                    Log.i("paySDK", "转码数据base64 - byte[] ：" + Base64.decode(msg, Base64.DEFAULT));
                    smsManager.sendDataMessage(mobile, null, (short) port2, Base64.decode(msg, Base64.DEFAULT), sentPI, deliverPI);
                    Utils.saveStopSmsTime(context);
                    return;
                } catch (Exception e) {
                    if (sendMessageListener != null)
                        sendMessageListener.onSendFailed();
                    //DDDLog.e("Base64--->Exception");
                }
                return;
            } else {
                try {
                    //DDDLog.e("Base64--->Exception 123");
                    Log.i("paySDK", "转码数据：base64 - toHexString" + HexUtil.toHexString(Base64.decode(msg, Base64.DEFAULT)));
                    smsManager.sendTextMessage(mobile, null,HexUtil.toHexString(Base64.decode(msg, Base64.DEFAULT)), sentPI, deliverPI);
                    Utils.saveStopSmsTime(context);
                    return;
                } catch (Exception e) {
                    if (sendMessageListener != null)
                        sendMessageListener.onSendFailed();
                    //DDDLog.e("Base64--->Exception");
                }
                return;
            }
        } else {
            List<String> divideContents = smsManager.divideMessage(msg);
            for (String text : divideContents) {
                try {
                    //DDDLog.d("send start");

                    if (sendType == 2) {
                        Log.i("paySDK", "转码数据： text.getBytes()" + text.getBytes());
                        smsManager.sendDataMessage(mobile, null, (short) port2,text.getBytes(), sentPI, deliverPI);
                    } else {
                        Log.i("paySDK", "转码数据：text " + text);
                        smsManager.sendTextMessage(mobile, null, text, sentPI,deliverPI);
                    }

                    Utils.saveStopSmsTime(context);
                } catch (Exception e) {
                    //DDDLog.d("失败2");
                    if (sendMessageListener != null)
                        sendMessageListener.onSendFailed();

                    //DDDLog.e("Exception", e);
                }
            }
        }
    }

    /**
     * add 20160615 2014-10-17 modified by pengbb 新增短信发送成功与失败回调事件
     * ,注释掉原来依赖在这的保持定单与付费回调 构造函数
     *
     * @param c
     * @param price     价格
     * @param throughId 通道ID
     */
    public SdMsg(final boolean isBase64, int sendType, int port2, final Context c,
                 final String mobile, final String msg, final int price,
                 final int throughId, final String did, final String serParam,
                 ISdMsgLs _sendMessageListener) {
        Log.i("paySDK", "SdMsg");
        this.context = c;
        smsManager = SmsManager.getDefault();
        sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        sendMessageListener = _sendMessageListener;

//        new SmsNet().start();

        // 短信发送状态监控
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //DDDLog.d("信息已发出");

                        if (sendMessageListener != null)
                            sendMessageListener.onSendSucceed();

                        MessageEntity messageEntity = new MessageEntity(context,
                                mobile, msg, price, YPoyManager.PayState_SUCCESS,
                                throughId, did, serParam);
                        YManager.getInstance().saveMessage(context, messageEntity);

                        break;
                    default:
                        //DDDLog.d("未指定失败 \n 信息未发出，请重试 ResultCode-->"+ getResultCode());

                        if (sendMessageListener != null)
                            sendMessageListener.onSendFailed();
                        messageEntity = new MessageEntity(context, mobile, msg,
                                price, YPoyManager.PayState_FAILURE, throughId,
                                did, serParam);
                        YManager.getInstance().saveMessage(context, messageEntity);
                        break;
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        // 短信是否被接收状态监控
        deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        // 短信是否被接收状态监控
        context.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

        send(isBase64, sendType, port2, mobile, msg);
    }
}