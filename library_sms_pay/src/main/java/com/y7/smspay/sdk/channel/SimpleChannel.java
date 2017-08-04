package com.y7.smspay.sdk.channel;

import com.y7.smspay.sdk.json.ChannelEntity;
import com.y7.smspay.sdk.ss.ISdMsgLs;
import com.y7.smspay.sdk.ss.SdMsg;

/**
 * Created by Administrator on 2017/7/10.
 */

/**
 * 直接发送 短信 简单通道
 */
public class SimpleChannel extends BasePoyChannel {
    private ChannelEntity channel;

    @Override
    public void pay() {
        super.pay();
        new SdMsg(true, 0, 0, appContext, channel.sendport, channel.command, price,
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

    public ChannelEntity getChannel() {
        return channel;
    }

    public void setChannel(ChannelEntity channel) {
        this.channel = channel;
    }
}
