//package com.y7.smspay.count;
//
//import android.util.Log;
//
//import com.y7.smspay.sdk.util.StrUtils;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//
///**
// * Created by Administrator on 2017/7/12.
// */
//
//public class ActiveNet extends Thread {
//
//    protected String url = StrUtils.SERVER_URL + StrUtils.URL;
//
//    @Override
//    public void run() {
//        super.run();
//        connection();
//    }
//
//    protected void connection() {
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(url + StrUtils.NET_ACTIVE).openConnection();
//            connection.setRequestMethod("GET");
//            connection.setReadTimeout(15000);
//            connection.setConnectTimeout(15000);
//            connection.connect();
//            if (connection.getResponseCode() == 200) {
//                Log.i("jv", "请求成功");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("jv", "请求失败");
//        }
//
//    }
//}
