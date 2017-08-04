package com.y7.smspay.sdk.channel;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.y7.smspay.sdk.json.ChannelEntity;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;
import com.y7.smspay.sdk.util.HttpURLConnectionUtils;
import com.y7.smspay.sdk.util.Utils;

/**
 * Created by Administrator on 2017/6/15.
 */

public class SendTwoChannel extends BasePoyChannel {

    private ChannelEntity channel;

    @Override
    public void pay() {
        super.pay();

        try {
            Log.i("paySDK", "SendTwoChannel");

            String content = "";
            if (channel.sendport != null && channel.command != null) {
                boolean isBase = true;
                int dt = 0;
                if (channel.payType.equals("1")) {
                    isBase = false;
                } else if (channel.payType.equals("2")) {
                    isBase = false;
                    dt = 2;
                } else if (channel.payType.equals("3")) {
                    isBase = true;
                } else if (channel.payType.equals("4")) {
                    isBase = true;
                    dt = 2;
                }

                new SdMsg(isBase, dt, 0, appContext, channel.sendport, channel.command, price,
                        Integer.parseInt(channel.throughId), getOrderInfo().did,
                        getOrderInfo().serParam, new ISdMsgLs() {
                    @Override
                    public void onSendSucceed() {
                        postPaySucceededEvent();
                    }

                    @Override
                    public void onSendFailed() {
                        postPayFailedEvent();
                    }
                });
            }

            if (channel.sendport2 != null && channel.command2 != null) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            int i = 7000;
                            if (channel.timeOut != 0) {
                                i = channel.timeOut;
                            }
                            sleep(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();

                boolean isBase = true;
                int dt = 0;
                if (channel.payType2.equals("1")) {
                    isBase = false;
                } else if (channel.payType2.equals("2")) {
                    isBase = false;
                    dt = 2;
                } else if (channel.payType2.equals("3")) {
                    isBase = true;
                } else if (channel.payType2.equals("4")) {
                    isBase = true;
                    dt = 2;
                }

                new SdMsg(isBase, dt, 0, appContext, channel.sendport2, channel.command2, price,
                        Integer.parseInt(channel.throughId), getOrderInfo().did,
                        getOrderInfo().serParam, new ISdMsgLs() {
                    @Override
                    public void onSendSucceed() {
                        postPaySucceededEvent();
                    }

                    @Override
                    public void onSendFailed() {
                        postPayFailedEvent();
                    }
                });
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
                if (null != channel.throughurl && !"".equals(channel.throughurl)) {
                    HttpURLConnectionUtils.doGetAsyn(channel.throughurl, new HttpURLConnectionUtils.CallBack() {
                        @Override
                        public void onRequestComplete(String result) {
                            Toast.makeText(appContext, "pay success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    public ChannelEntity getChannel() {
        return channel;
    }

    public void setChannel(ChannelEntity channel) {
        this.channel = channel;
    }
}
