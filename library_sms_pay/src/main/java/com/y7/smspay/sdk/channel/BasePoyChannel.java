package com.y7.smspay.sdk.channel;

import android.content.Context;

import com.y7.smspay.sdk.json.RequestEntity;
import com.y7.smspay.sdk.mgr.YManager;

import java.util.Vector;

public abstract class BasePoyChannel implements IPoyChannel {

    private RequestEntity orderInfo;
    protected Context appContext;
    protected static int price;
    protected String secondTipInfo;
    protected String productName;
    protected String extData;

    private Vector<IPoyChannelListener> payChannelListenerList = new Vector<IPoyChannelListener>();

    public BasePoyChannel() {

    }

    @Override
    public void addPayChannelListener(IPoyChannelListener newPayChannelListener) {
        if (!payChannelListenerList.contains(newPayChannelListener)) {
            payChannelListenerList.add(newPayChannelListener);
        }
    }

    @Override
    public void removePayChannelListener(
            IPoyChannelListener newPayChannelListener) {
        if (payChannelListenerList.contains(newPayChannelListener)) {
            payChannelListenerList.remove(newPayChannelListener);
        }
    }

    @Override
    public void pay() {
    }


    protected void postPaySucceededEvent() {
        for (IPoyChannelListener payChannelListener : payChannelListenerList) {
//            payChannelListener.onPaySucceeded(); 会反复吊起
        }
    }

    protected void postPayFailedEvent() {
        for (IPoyChannelListener payChannelListener : payChannelListenerList) {
//            payChannelListener.onPayFailed(); 会反复吊起
        }
    }

    protected void postPayCanceledEvent() {
        for (IPoyChannelListener payChannelListener : payChannelListenerList) {
            payChannelListener.onPayCanceled();
        }
    }

    /**
     * appContext
     *
     * @return the appContext
     * @since 2014年11月6日
     */
    public Context getAppContext() {
        return appContext;
    }

    /**
     * @param appContext the appContext to set
     */
    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * price
     *
     * @return the price
     * @since 2014年11月6日
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * secondTipInfo
     *
     * @return the secondTipInfo
     * @since 2014年11月6日
     */
    public String getSecondTipInfo() {
        return secondTipInfo;
    }

    /**
     * @param secondTipInfo the secondTipInfo to set
     */
    public void setSecondTipInfo(String secondTipInfo) {
        this.secondTipInfo = secondTipInfo;
    }

    /**
     * productName
     *
     * @return the productName
     * @since 2014年11月6日
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * extData
     *
     * @return the extData
     * @since 2014年11月6日
     */
    public String getExtData() {
        return extData;
    }

    /**
     * @param extData the extData to set
     */
    public void setExtData(String extData) {
        this.extData = extData;
    }

    protected void saveOrderInfo(int payResult, String cid, String throughId, String customized_price) {
        RequestEntity order = new RequestEntity(appContext);
        order = this.orderInfo;
        order.status = payResult;
        order.cid = cid;
        order.throughId = throughId;
        if (!customized_price.equals("")) {
            order.customized_price = customized_price;
        } else {
            order.customized_price = price + "";
        }
        //DDDLog.d("saveOrderInfo -->" + order.toString());
        YManager.getInstance().saveOrder(appContext, order);
    }

    public RequestEntity getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(RequestEntity orderInfo) {
        this.orderInfo = orderInfo;
    }
}
