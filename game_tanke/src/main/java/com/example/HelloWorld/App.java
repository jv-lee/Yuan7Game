package com.example.HelloWorld;

import android.app.Application;

import com.pc.ksbt.on.Onib;

/**
 * Created by Administrator on 2017/8/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Onib.init(this);
    }
}
