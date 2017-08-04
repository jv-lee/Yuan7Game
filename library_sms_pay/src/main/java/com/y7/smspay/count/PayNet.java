package com.y7.smspay.count;

import android.util.Log;

import com.y7.smspay.PayManager;
import com.y7.smspay.sdk.mgr.YManager;
import com.y7.smspay.sdk.util.StrUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/12.
 */

public class PayNet extends Thread {

    protected String url = StrUtils.SERVER_URL + StrUtils.URL;

    @Override
    public void run() {
        super.run();
        connection();
    }

    protected void connection() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url + StrUtils.NET_PAY + "?payId=" + PayManager.payId).openConnection();
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
