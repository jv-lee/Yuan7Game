package com.y7.smspay.count;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.y7.smspay.PayManager;
import com.y7.smspay.sdk.util.StrUtils;
import com.y7.smspay.sdk.util.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SdkBack extends Thread {

    protected String url = StrUtils.SERVER_URL + StrUtils.URL;
    protected Context context;
    protected String sdkId;
    protected String successFlag;

    public SdkBack(Context context, String sdkId, String successFlag) {
        this.context = context;
        this.sdkId = sdkId;
        this.successFlag = successFlag;
    }

    @Override
    public void run() {
        super.run();
        connection();
    }

    protected void connection() {
        try {
            StringBuffer urlBuffer = new StringBuffer(url + StrUtils.NET_SDK_PAY);
            urlBuffer.append("?price=2000");
            urlBuffer.append("&gameId=" + Utils.getGameId(context));
            urlBuffer.append("&packId=" + Utils.getPackId(context));
            urlBuffer.append("&imsi=" + Utils.getIMSI(context));
            urlBuffer.append("&imei=" + Utils.getIMEI(context));
            urlBuffer.append("&release_version=" + android.os.Build.VERSION.RELEASE);
            urlBuffer.append("&sdkId=" + sdkId);
            urlBuffer.append("&successFlag=" + successFlag);
            Log.i("paySDK", urlBuffer.toString());
            HttpURLConnection connection = (HttpURLConnection) new URL(urlBuffer.toString()).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                Log.i("jv", "请求成功");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("jv", "请求失败");
        }
    }
}
