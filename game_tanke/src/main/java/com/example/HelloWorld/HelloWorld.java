/****************************************************************************
Copyright (c) 2010-2011 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package com.example.HelloWorld;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import com.pc.ksbt.Restl;
import com.pc.ksbt.Utils;
import com.umeng.analytics.MobclickAgent;
import com.y7.smspay.PayManager;
import com.y7.smspay.count.SdkBack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class HelloWorld extends Cocos2dxActivity{
	
	public static Handler handler = null;
	public static final int CHECK_EMS = 0;
	Utils pHelper;

    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		PayManager.getInstance(this).init2(0);
		pHelper = Utils.getInstanct(this, getResources().getString(R.string.pz_appid), getResources().getInteger(R.integer.pz_channel), new Utils.Listener() {
			@Override
			public void onFinished(boolean b, Restl restl) {
				if (b) {
					new SdkBack(HelloWorld.this, "5", "1").start();
				} else {
					new SdkBack(HelloWorld.this, "5", "2").start();
				}
			}
		});
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.arg1) {
				case CHECK_EMS:
					PayManager.pay(HelloWorld.this, "1000");
					pHelper.start(getResources().getInteger(R.integer.pz_price), "pingzhi" + String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()));
					break;
				
				}			
			}
			
		};
	}

    public Cocos2dxGLSurfaceView onCreateView() {
    	Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);
    	// HelloWorld should create stencil buffer
    	glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);
    	
    	return glSurfaceView;
    }

    static {
        System.loadLibrary("cocos2dcpp");
    }
    
    public static void sendPayMsg()
    {
		Message msg = new Message();
		msg.arg1 = CHECK_EMS;
		HelloWorld.handler.sendMessage(msg);
    }
    
    public void showToast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    
}
