package com.xmyd.puzkkb.mm;


import android.app.Application;

import com.mata.deft.on.Onib;


public class App extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Onib.init(this);
        an.dy.t.W.a(this, getResources().getInteger(R.integer.tk_appid), getResources().getString(R.string.tk_channel));  //初始化接口，
    }


}
