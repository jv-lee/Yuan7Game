package com.xmyd.puzkkb.mm;




import android.app.Application;
import android.util.Log;

import com.pc.ksbt.on.Onib;


public class App extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Onib.init(this);
    }


}
