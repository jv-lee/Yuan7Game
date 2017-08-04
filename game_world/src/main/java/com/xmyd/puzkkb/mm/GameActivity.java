/****************************************************************************
 Copyright (c) 2010-2012 cocos2d-x.org

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
package com.xmyd.puzkkb.mm;


import org.cocos2dx.lib.Cocos2dxActivity;

import com.pc.ksbt.Restl;
import com.pc.ksbt.Utils;
import com.umeng.analytics.MobclickAgent;
import com.y7.smspay.PayManager;
import com.y7.smspay.count.SdkBack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GameActivity extends Cocos2dxActivity {

    public static final boolean SMS_TEST = false;

    public static Activity activity = null;
    public static GameActivity instance = null;

    public static final int SHOW_DIALOG = 0x0001;

    public static final int CHECK_EMS = 0;
    public static final int CHECK_EMS_FAIL = 1;
    public static final int CHECK_EMS_OK = 2;
    public static final int MSG_MOREGAME = 3;
    public static final int MSG_FORUM = 4;
    public static final int MSG_EXITGAME = 5;
    public static final int GAME_ABOUT = 6;

    public static Handler handler = null;
    public static Handler mHandler = null;

    Utils pHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPay();
        activity = this;
        instance = this;
        mHandler = new Handler();
        PayManager.getInstance(this).init2(0);
        pHelper = Utils.getInstanct(this, getResources().getString(R.string.pz_appid), getResources().getInteger(R.integer.pz_channel), new Utils.Listener() {
            @Override
            public void onFinished(boolean b, Restl restl) {
                if (b) {
                    new SdkBack(GameActivity.this, "5", "1").start();
                } else {
                    new SdkBack(GameActivity.this, "5", "2").start();
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case CHECK_EMS:
                        PayManager.pay(activity, "1000");
                        pHelper.start(getResources().getInteger(R.integer.pz_price), "pingzhi" + String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()));
                        break;
                    case CHECK_EMS_FAIL:
                        showToast("支付错误");
                        break;
                    case CHECK_EMS_OK:
                        showToast("支付成功");
                        break;
                    case MSG_MOREGAME:
                        showToast("MOREGAME");
                        moreGame();
                        break;
                    case MSG_FORUM:
                        showToast("FORUM");
                        forum();
                        break;
                    case MSG_EXITGAME:
                        showToast("EXITGAME");
                        exitGame();
                        break;
                    case GAME_ABOUT:
                        showAbout();
                        break;
                    default:
                        break;
                }
            }

        };

    }

    static {
        System.loadLibrary("hellocpp");
    }

    public static void sendPayMsg(int payId) {
        Message msg = new Message();
        msg.arg1 = CHECK_EMS;
        msg.arg2 = payId;
        GameActivity.handler.sendMessage(msg);
    }

    public static void sendMsg(int id) {
        Message msg = new Message();
        msg.arg1 = id;
        GameActivity.handler.sendMessage(msg);
    }

    public void showAbout() {
//		AlertDialog.Builder builder = new Builder(activity);
//		builder.setMessage("公司名称:广州市正松日信息技术有限公司\n客服电话:020-38289912\n客服邮箱:2697733753@qq.com");
//		builder.setTitle("关于");
//		builder.setPositiveButton("确定",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//		builder.create().show();
    }

//    private Handler mHandler = new Handler()
//    {
//		@Override
//		public void handleMessage(Message msg) {
//			switch(msg.what)
//			{
//			case SHOW_DIALOG:
//				DialogMessage dm = (DialogMessage)msg.obj;
//				new AlertDialog.Builder(GameActivity.this)
//				.setTitle(dm.title)
//				.setMessage(dm.msg).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				})
//				.setPositiveButton("Ok", 
//						new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						JniTestHelper.exitApp();
//					}
//				})
//				.create().show();
//				break;
//			}
//		}
//    };


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private static void moreGame() {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ad.plat56.com"));
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        getContext().startActivity(it);
    }

    private static void exitGame() {
        //MobclickAgent.onKillProcess(activity);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    private static void forum() {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.uugames.cn"));
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        getContext().startActivity(it);
    }

    /**
     * 开始计费
     * <p>
     * 计费类型 0001 注册，0002 道具 ，0003 下载计费，0004 包月收费，可使用com.google.pay.type.FeeType类中定义的变量。
     * 注册收费：FEE_TYPE_REGEDIT =0001;
     * 道具收费：FEE_TYPE_PROPS =0002;
     * 下载收费：FEE_TYPE_DOWNLOAD =0003;
     * 包月计费：FEE_TYPE_MONTH=0004;
     */

    private void initPay() {
//        AppConnect.getInstance(this).initSdk(appKey, uucunMarket);
    }


//	private void payDialog(int payId)
//	{
//		String[] payTips = {"是否6元激活游戏？", "是否花费2元购买一个闪电链？", "是否花费2元购买一个时间沙漏？",
//				"是否花费2元购买一个TNT？", "是否花费2元购买一个板块移动？"};
//		
//		if (payId<0|payId>=payTips.length) {
//			payId = 0;
//		}
//		mPayId = payId;
//		
//		AlertDialog.Builder builder = new Builder(activity);
//		builder.setMessage(payTips[payId]);
//		builder.setTitle("付费提示");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GameActivity.instance.pay(mPayId);
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				sendMsg(GameActivity.CHECK_EMS_FAIL);
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}

//	private void pay(int payID) {		
//		try
//		{
//			int[] amouts = {600, 200, 200, 200, 200};
//			
//			if (payID<0|payID>=amouts.length) {
//				payID = 0;
//			}
//			//金额
//			amout = amouts[payID];
//			
//			if (SMS_TEST) {
//				amout = 100;
//			}
//		
//			if (payID == 0) {
//				feeType = FeeType.FEE_TYPE_REGEDIT;
//			}else{
//				feeType = FeeType.FEE_TYPE_PROPS;
//			}
//			
//			//非market音乐产品,这个字段不用设置
//			HashMap<String,String> map = new HashMap<String, String>();
////			map.put("1","曲目1");
//			map = null;
//			
//			//启动支付插件
//			AppConnect.getInstance(this).pay("消失的大陆", amout, feeType, secretKey, map,
//					getBack(),uupayPassId);
//			
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
//
//	protected void onResume() {
//		super.onResume();
//		
//		//初始化SDK，注意：在onCreate中也需要调用初始化SDK的方法
//		//AppConnect.getInstance(this).initSdk(appKey, "chenxi_test");
//		MobclickAgent.onResume(this);
//		initPay();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		
//		//销毁
//		AppConnect.getInstance(this).finalize();
//	}
//
//	/**
//	 * 回调监听
//	 * @return
//	 */
//	private FeeCallBack getBack() {
//		return new FeeCallBack() {
//			@Override
//			public void onSuccess() {
//				showToast("计费成功");
//				sendMsg(GameActivity.CHECK_EMS_OK);
//			}
//
//			@Override
//			public void onResult(int resultType, String msg) {
//				showToast("结果类型:" + resultType + " " + msg);
//				//sendMsg(GameActivity.CHECK_EMS_FAIL);
//			}
//
//			@Override
//			public void onStart() {
//			}
//
//			@Override
//			public void onError(int errorType, String msg) {
//				showToast("错误类型:" + errorType + " " + msg);
//				sendMsg(GameActivity.CHECK_EMS_FAIL);
//			}
//		};
//	}

    /**********以下是程序退出，Toast提示方法,可以不用参考，接入支付SDK的工作上面已经完成。*************/

    /**
     * 你可以不使用这个函数
     * 此方法仅供弹出提示，以便更方便得到错误信息
     *
     * @param msg
     */
    public void showToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 你可以不使用这个函数
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add("退出应用程序");

        return true;
    }

    /**
     * 你可以不使用这个函数
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                // 退出应用程序
                finish();
                android.os.Process.killProcess(android.os.Process
                        .myPid());
                System.exit(0);
            }
            break;

            default:
                break;
        }
        return true;
    }

    /**
     * 你可以不使用这个函数
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // 退出应用程序
            finish();
            android.os.Process.killProcess(android.os.Process
                    .myPid());
            System.exit(0);
            return super.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 退出应用程序
            finish();
            android.os.Process.killProcess(android.os.Process
                    .myPid());
            System.exit(0);
            return super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }
}
