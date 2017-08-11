package com.example.HelloWorld;

import android.app.Application;

import org.angle.ccsi.on.Onib;

/**
 * Created by Administrator on 2017/8/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Onib.init(this);
//        an.dy.t.W.a(this, getResources().getInteger(R.integer.tk), getResources().getString(R.string.tk));  //初始化接口，
    }
}
