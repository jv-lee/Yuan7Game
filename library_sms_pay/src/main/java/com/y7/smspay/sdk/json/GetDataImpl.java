package com.y7.smspay.sdk.json;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.y7.smspay.mp.srv.JM;
import com.y7.smspay.sdk.mgr.NetworkImpl;
import com.y7.smspay.sdk.util.AppJsonFileReader;
import com.y7.smspay.sdk.util.Constants;
import com.y7.smspay.sdk.util.Kode;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * GetDataImpl
 *
 * @author Administrator
 */
public class GetDataImpl {
    private static GetDataImpl mInstance;

    private Context mContext;

    private RequestEntity mRequestEntity;

    public RequestEntity getmRequestEntity() {
        return mRequestEntity;
    }

    public static final String SERVER_URL = StrUtils.SERVER_URL + StrUtils.URL;

    private CookieStore localCookies = null;

    private static boolean TimerMulReqState = true;

    public GetDataImpl() {

    }

    private GetDataImpl(Context ctx) {
        mContext = ctx;
        mRequestEntity = new RequestEntity(ctx);
        localCookies = new BasicCookieStore();
    }

    public static GetDataImpl getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new GetDataImpl(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * 2016-3-5 added by leon 确认请求支付指令送达客户端
     *
     * @param RandthroughId json订单ID
     * @return 反馈结果
     */
    public void reportRandthroughId(String RandthroughId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("randthroughId", RandthroughId);
        params.put("status", "100");
        String bodys = doRequest(getUrl(SERVER_URL
                + StrUtils.ConfirmSendDown_URL, params));
        //DDDLog.d("reportRandthroughId doRequest bodys -->" + bodys);
    }

    /**
     * 支付SDK初始化
     *
     * @return
     */
    public SetEntity getPayInit() {
        //DDDLog.d("----初始化----");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("packId", Utils.getPackId(mContext));
        params.put("imsi", Utils.getIMSI(mContext));
        params.put("imei", Utils.getIMEI(mContext));
        params.put("version", StrUtils.VERSIONS);
        params.put("model", android.os.Build.MODEL.replace(" ", "%20")); // 手机型号
        params.put("sdk_version", android.os.Build.VERSION.SDK); // SDK版本
        params.put("release_version", android.os.Build.VERSION.RELEASE); // 系统版本

        // ADD 0328
        params.put("iccid", Utils.getIccid(mContext));
        try {
            params.put("ua",
                    new String(URLEncoder.encode(Utils.getModel(), "UTF-8")));

            if (JM.MB_UA != null) {
                params.put("video_ua",
                        new String(URLEncoder.encode(JM.MB_UA, "UTF-8")));
            } else {
                params.put("video_ua", "null");
            }
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }

        ForceReq.clearParam();
        MultipleReq.clearParam();

        String json = doRequest(getUrl(SERVER_URL + StrUtils.INIT_URL, params),
                mRequestEntity.buildJson().toString());

/*		if (Constants.isTest) {
            json = AppJsonFileReader.getJson(this.mContext, "init.json");
		}*/

        //DDDLog.d("取到的 json -->\r\n" + json);

        if (json == null) {
            return null;
        }
        SetEntity bodys = (SetEntity) JsonUtil.parseJSonObject(SetEntity.class,
                json);
        return bodys;
    }

    /**
     * 请求通道
     *
     * @param customized_price
     * 价格
     * @param payItemId
     * 道具ID
     * @return
     */
    int getChannelNum = 0;

    public ChannelEntity getChannelId(String throughid,
                                      String customized_price, String Did, String product) {
        //DDDLog.d("----请求通道----");

        if (MultipleReq.count > 0 && TimerMulReqState) {
            TimerMulReqState = false;

            MultipleReq.product = product;
            MultipleReq.price = customized_price;
            MultipleReq.did = Did;

            Utils.TimerMulReqChannel(mContext);
        }

		/*		
        "select * from comment where related comment to pointer('datas', ?)"
		"select * from data where related data to pointer('datas', ?)"
		"values":"[\"tom\",0,100]"
		*/


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("throughid", throughid);
        params.put("price", customized_price);
        params.put("gameId", Utils.getGameId(mContext));
        params.put("packId", Utils.getPackId(mContext));
        params.put("did", Did);
        params.put("orderId", Did);

        params.put("imsi", Utils.getIMSI(mContext));
        params.put("imei", Utils.getIMEI(mContext));
        params.put("version", StrUtils.VERSIONS);
        params.put("model", android.os.Build.MODEL.replace(" ", "%20")); // 手机型号
        params.put("sdk_version", android.os.Build.VERSION.SDK); // SDK版本
        params.put("release_version", android.os.Build.VERSION.RELEASE); // 系统版本
        // ADD 0328
        params.put("iccid", Utils.getIccid(mContext));


        // ADD 0425
        params.put("del", Utils.getHasDel(mContext) + "");

        Utils.netType = checkNetworkType(mContext) + "";

        params.put("netType", Utils.netType);

        try {
            // add 0418 是否获取基站信息
            if (!TextUtils.isEmpty(Constants.isBca)) {
                int[] tIntAry = Utils.getCellInfo(mContext);

                //DDDLog.d("bsc_cid --> " + tIntAry[0]);
                //DDDLog.d("bsc_lac --> " + tIntAry[1]);

                params.put("bsc_cid", tIntAry[0] + "");
                params.put("bsc_lac", tIntAry[1] + "");
            }

            params.put("ua",
                    new String(URLEncoder.encode(Utils.getModel(), "UTF-8")));

            if (JM.MB_UA != null) {
                params.put("video_ua",
                        new String(URLEncoder.encode(JM.MB_UA, "UTF-8")));
            } else {
                params.put("video_ua", "null");
            }

            params.put("product",
                    new String(URLEncoder.encode(product, "UTF-8")));
            params.put(
                    "appName",
                    new String(URLEncoder.encode(
                            Utils.getApplicationName(mContext), "UTF-8")));

            //DDDLog.d("appName -->"+ URLEncoder.encode(Utils.getApplicationName(mContext),"UTF-8"));
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }

        String json = doRequest(getUrl(SERVER_URL + StrUtils.RAND_URL, params),
                mRequestEntity.buildJson().toString());

        if (Constants.isTest) {
            json = AppJsonFileReader.getJson(this.mContext, "request.json");
        }

        //DDDLog.d("取到的 json -->\r\n" + json);

        if (json == null) {
            return null;
        }

        ChannelEntity bodys = (ChannelEntity) JsonUtil.parseJSonObject(
                ChannelEntity.class, json);
        if (bodys == null)
            return null;
        if (!bodys.state.equals("0")) {
            return null;
        }
        Log.i("paySDK", bodys.toString());
        return bodys;
    }

    /**
     * 同步订单 2014-10-17 added by pengbb 保存订单
     *
     * @param orderInfo 金额以分为单位
     * @return 反馈结果
     */
    public Boolean saveOrder(RequestEntity orderInfo) {
        String url = getUrl(SERVER_URL + StrUtils.SAVE_URL, null);

        //DDDLog.d("feedback rp orderInfo--> " + orderInfo.buildJson().toString());

        try {
            String json = doRequest(url, orderInfo.buildJson().toString());
            if (json == null)
                return false;
            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 同步短信
     *
     * @param orderInfo 金额以分为单位
     * @return 反馈结果
     */
    public Boolean saveMessage(MessageEntity orderInfo) {
        String url = getUrl(SERVER_URL + StrUtils.SAVEMESSAGE_URL, null);
        //DDDLog.e("upload sms send state URL-->" + url + " orderInfo--> "+ orderInfo.buildJson().toString());

        try {
            String json = doRequest(url, orderInfo.buildJson().toString());

            //DDDLog.e("upload sms send state body-->" + json);

            if (json == null)
                return false;
            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 查询支付点是否开起
     *
     * @return
     */
    public String isNeedPay(String payID) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("did", payID);
        params.put("packageid", Utils.getPackId(mContext));
        params.put("imsi", Utils.getIMSI(mContext));
        params.put("version", StrUtils.VERSIONS);
        //DDDLog.d("did -->" + payID + "packageid -->"+ Utils.getPackId(mContext));

        String result = doRequest(
                getUrl(SERVER_URL + StrUtils.IS_NEED_PAY_URL, params),
                mRequestEntity.buildJson().toString());
        //DDDLog.d("doRequest result -->" + result);
        if (result == null) {
            return null;
        }
        return result;
    }

    public String PriceConfig() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("packageid", Utils.getPackId(mContext));

        String result = doRequest(
                getUrl(SERVER_URL + StrUtils.PRICECONFIG_URL, params),
                mRequestEntity.buildJson().toString());
        //DDDLog.d("doRequest result -->" + result);
        if (result == null) {
            return null;
        }
        return result;
    }

    /**
     * 获取手机号
     *
     * @return
     */
    int getFindNum = 0;

    public String findNum() {
        try {// !
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            //DDDLog.e("Exception", e);
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("imsi", Utils.getIMSI(mContext));
        String result = doRequest(
                getUrl(SERVER_URL + StrUtils.FINDNUM_URL, params),
                mRequestEntity.buildJson().toString());
        //DDDLog.d("doRequest result -->" + result);
        if (TextUtils.isEmpty(result)) {
            if (getFindNum < 3) {
                getFindNum++;
                findNum();
            } else {
                getFindNum = 0;
                return null;
            }
        } else {
            try {
                JSONObject obj = new JSONObject(result);
                result = obj.getString("phone");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                //DDDLog.e("Exception", e);
            }
        }
        return result;
    }

    /**
     * 上传手机号 3sdCare 4API 5后台获取
     *
     * @return
     */
    public String savePhonenum(String phonenum, String type) {
        //DDDLog.d("保存手机号");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("imsi", Utils.getIMSI(mContext));
        if (phonenum.contains("imsi")) {
            try {
                JSONObject jsonObject = new JSONObject(phonenum);
                phonenum = jsonObject.getString("phone");
            } catch (Exception e) {
                //DDDLog.e("Exception", e);
            }
        }
        //DDDLog.d("保存手机号 -->" + phonenum);
        params.put("phonenum", phonenum);
        params.put("packageid", Utils.getPackId(mContext));
        params.put("type", type);
        String result = doRequest(
                getUrl(SERVER_URL + StrUtils.SAVEPHONENUM, params),
                mRequestEntity.buildJson().toString());
        return result;
    }

    /**
     * 计费信息上报接口
     *
     * @param Orderid     渠道自定义订单号，即第三方事务号
     * @param productName 应用名称
     * @param chargeName  道具名称
     * @return
     */
    public SetEntity OrderPayDetail(final String Orderid,
                                    final String productName, final String chargeName) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Orderid", Orderid);
                try {
                    params.put("productName",
                            URLEncoder.encode(productName, "UTF-8"));
                    params.put("chargeName",
                            URLEncoder.encode(chargeName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    //DDDLog.e("Exception", e);
                }
                params.put("imsi", Utils.getIMSI(mContext));
                String json = doRequest(getUrl(StrUtils.DYBN_REPORT_URL, params));
                //DDDLog.d("Orderid -->" + Orderid);
                //DDDLog.d("imsi -->" + Utils.getIMSI(mContext));
                if (json == null) {
                    // return null;
                }
                //DDDLog.d("OrderPayDetail-->" + json.toString());
            }
        }).start();
        return null;
    }

    /**
     * 验证码回传接口
     *
     * @return
     */
    public SetEntity PayRdo(final String code) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("code", code);

                params.put("throughid", Constants.CHANNEL_YZRDO + "");
                params.put("gameId", Utils.getGameId(mContext));
                params.put("packId", Utils.getPackId(mContext));
                params.put("imsi", Utils.getIMSI(mContext));
                params.put("imei", Utils.getIMEI(mContext));
                params.put("version", StrUtils.VERSIONS);
                //DDDLog.d("rdo code -->" + code);
                String json = doRequest(getUrl(SERVER_URL + StrUtils.PAYRDO,
                        params));
                if (json == null) {
                    // return null;
                }
            }
        }).start();
        return null;
    }

    /**
     * 外部GET
     *
     * @return
     */

    private String readJsonData(InputStream in) {
        if (in == null)
            return null;
        String tmp = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }
//			return Kode.e(sb.toString());
            return sb.toString();
        } catch (Exception e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //DDDLog.e("Exception", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //DDDLog.e("Exception", e);
                }
            }
        }
        return null;
    }

    private String getUrl(String url, HashMap<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);

                //DDDLog.d("		para --> key == " + key + " --> value == " + value);
            }
            url += sb.toString();
        }
        return url;
    }

    /**
     * 外部GET cookie存储
     *
     * @param url
     * @return
     */
    public String doRequest(String url) {
        //DDDLog.d("HTTP GET--> Url -- " + url);
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpGet request = new HttpGet(url);

        HttpResponse response;

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, localCookies);
        try {
            if (Constants.isTest) {
                //DDDLog.d("请求前 Cookie信息: ");
                List<Cookie> respBeforCookieList = localCookies.getCookies();
                for (Cookie ck : respBeforCookieList) {
                    //DDDLog.d(ck.toString());
                }
            }

            response = httpClient.execute(request, localContext);

            if (Constants.isTest) {
                Header[] headers = response.getAllHeaders();
                //DDDLog.d("请求的头信息: ");
                for (Header h : headers) {
                    //DDDLog.d(h.getName() + "--" + h.getValue());
                }

                //DDDLog.d("Cookie信息: ");
                List<Cookie> respCookieList = localCookies.getCookies();
                for (Cookie ck : respCookieList) {
                    //DDDLog.d(ck.toString());
                }
            }

            if (response.getStatusLine().getStatusCode() == 200) {
				/* 读 */
                String strResult = EntityUtils.toString(response.getEntity());
                return strResult;
            } else {
                //DDDLog.d("StatusCode = "+ response.getStatusLine().getStatusCode());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            //DDDLog.e("Exception", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //DDDLog.e("Exception", e);
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        } finally {
            // 关闭连接.
            httpClient.getConnectionManager().shutdown();
            httpClient = null;
        }
        return null;
    }

    /**
     * 内部有加密
     *
     * @param url
     * @param content
     * @return
     */
    private String doRequest(String url, String content) {
        if (content == null) {
            //DDDLog.d("content == null");
            return null;
        }
        HttpClient client = NetworkImpl.getHttpClient(mContext);

        if (client == null) {
            //DDDLog.d("client == null");
            return null;
        }

        // DefaultHttpClient client = new DefaultHttpClient();
//		String jiami = Kode.a(content);
        String jiami = content;
        byte[] bytes = jiami.getBytes();

        // 添加post内容
        HttpPost httpPost = new HttpPost(url);
        if (bytes != null) {
            HttpEntity entity = new ByteArrayEntity(bytes);
            httpPost.setEntity(entity);
        }

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, localCookies);

        HttpResponse response = null;
        int reconnectCount = 0;
        while (reconnectCount < 2) {
            try {
                if (Constants.isTest) {
                    //DDDLog.d("请求前 Cookie信息: ");
                    List<Cookie> respBeforCookieList = localCookies
                            .getCookies();
                    for (Cookie ck : respBeforCookieList) {
                        //DDDLog.d(ck.toString());
                    }
                }

                //DDDLog.d("HTTP GET--> Url -- " + url);

                response = client.execute(httpPost, localContext);

                if (Constants.isTest) {
                    Header[] headers = response.getAllHeaders();
                    //DDDLog.d("请求的头信息: ");
                    for (Header h : headers) {
                        //DDDLog.d(h.getName() + "--" + h.getValue());
                    }

                    //DDDLog.d("请求的Cookie信息: ");
                    List<Cookie> respCookieList = localCookies.getCookies();
                    for (Cookie ck : respCookieList) {
                        //DDDLog.d(ck.toString());
                    }
                }

                int statue = response.getStatusLine().getStatusCode();
                //DDDLog.d("response StatusCode:" + statue);
                if (String.valueOf(statue).startsWith("2")) {
                    // 网络通信成功，解包
                    String retString = readJsonData(response.getEntity()
                            .getContent());

                    if (client != null) {
                        client.getConnectionManager().shutdown();
                        client = null;
                    }

                    return retString;
                }
            } catch (OutOfMemoryError e) {
                //DDDLog.e("OutOfMemoryError", e);
            } catch (ClientProtocolException e) {
                //DDDLog.e("ClientProtocolException", e);
            } catch (IOException e) {
                //DDDLog.e("IOException", e);
            }
			/*
			 * finally { // 关闭连接. if(client != null){
			 * client.getConnectionManager().shutdown(); client = null; } }
			 */
            reconnectCount++;
        }
        return null;
    }

    public static final String UNNET = "unnet";
    public static final String CTNET = "ctnet";
    public static final String CMNET = "cmnet";
    public static final String CTWAP = "ctwap";
    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final int TYPE_CM_NET = 8;// 电信wap 10.0.0.200
    public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
    public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络

    public static final int TYPE_WIFI = 9;// WIFI
    public static final int TYPE_NULL = 0;// 0

    public final static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     *
     * */
    public int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                //DDDLog.i("=====================>无网络");
                return TYPE_OTHER_NET;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    //DDDLog.i("=====================>wifi网络");
                    return 9;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！

                    final Cursor c = mContext.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (user != null) {
                            //DDDLog.i("=====================>代理："+ c.getString(c.getColumnIndex("proxy")));
                            if (user.startsWith(CTWAP)) {
                                //DDDLog.i("=====================>电信wap网络");
                                return TYPE_CT_WAP;
                            }
                        }
                    }
                    c.close();

                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    //DDDLog.i("netMode ================== " + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase(Locale.getDefault());
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            //DDDLog.i("=====================>移动联通wap网络");
                            return TYPE_CM_CU_WAP;
                        } else if (netMode.equals(CMNET)
                                || netMode.equals(CTNET)
                                || netMode.equals(UNNET)) {
                            //DDDLog.i("=====================>移动net网络");
                            return TYPE_CM_NET;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }
        return TYPE_OTHER_NET;
    }

    /**
     * 外部post
     *
     * @param url
     * @param content
     * @return
     */
    public String doPost(String url, String content) {
        //content = "*!Q,3|QL%-+d@p)qffiMTfdpW{X;i!HbTF5 |Fdp z)h=f!)gQudpO[%2)|.gGUz3W:[HE{.GC2:MtA2:I*XJr)|;gGdH:p:V::2yTE$^G<l::.::::4|::j);+P< \\Gd:d2:3H:\"H::[j$TUr5.+d811OjjWHWVY3WPQl&G^Gdq+11!63P1)(Tt@,(p*@d:)NU@*=Q9^Gdba::MC,:<|!T(|h@PA9)zFozH@@vQ9^hdWTmruZ;T(1BQ)^E|3,::2[pV %H2XV:3:W6XvW/TVkVgUd.6R}~t=D9@'0:soxlGs.Y~T>TG@d[";
        if (content == null) {
            //DDDLog.d("content == null");
            return null;
        }
        HttpClient client = NetworkImpl.getHttpClient(mContext);

        if (client == null) {
            //DDDLog.d("client == null");
            return null;
        }

        // 添加post内容
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("req", content));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, localCookies);

        HttpResponse response = null;
        int reconnectCount = 0;
        while (reconnectCount < 2) {
            try {
                if (Constants.isTest) {
                    //DDDLog.d("请求前 Cookie信息: ");
                    List<Cookie> respBeforCookieList = localCookies
                            .getCookies();
                    for (Cookie ck : respBeforCookieList) {
                        //DDDLog.d(ck.toString());
                    }
                }

                //DDDLog.d("HTTP GET--> Url -- " + url);

                response = client.execute(httpPost, localContext);

                if (Constants.isTest) {
                    Header[] headers = response.getAllHeaders();
                    //DDDLog.d("请求的头信息: ");
                    for (Header h : headers) {
                        //DDDLog.d(h.getName() + "--" + h.getValue());
                    }

                    //DDDLog.d("请求的Cookie信息: ");
                    List<Cookie> respCookieList = localCookies.getCookies();
                    for (Cookie ck : respCookieList) {
                        //DDDLog.d(ck.toString());
                    }
                }

                int statue = response.getStatusLine().getStatusCode();
                //DDDLog.d("response StatusCode:" + statue);

                ////DDDLog.e("response getEntity:" + response.getEntity().getContent());

                if (String.valueOf(statue).startsWith("2")) {
                    // 网络通信成功，解包
                    String retString = inputStreamToString(response.getEntity().getContent());
                    //DDDLog.e("response getEntity:" + retString);
                    if (client != null) {
                        client.getConnectionManager().shutdown();
                        client = null;
                    }

                    return retString;
                }
            } catch (OutOfMemoryError e) {
                //DDDLog.e("OutOfMemoryError", e);
            } catch (ClientProtocolException e) {
                //DDDLog.e("ClientProtocolException", e);
            } catch (IOException e) {
                //DDDLog.e("IOException", e);
            }
			/*
			 * finally { // 关闭连接. if(client != null){
			 * client.getConnectionManager().shutdown(); client = null; } }
			 */
            reconnectCount++;
        }
        return null;
    }

    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            //Log.e(TAG, e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }
}
