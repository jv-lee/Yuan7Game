package com.y7.smspay.sdk.ss;

import android.text.TextUtils;

import java.util.Vector;

public class SsInrp {

	private static Vector<String> senderPhoneNumberList = new Vector<String>();
	private static Vector<String> messageContentList = new Vector<String>();

	private static String capCode = null;
	private static String capCount = null;
	private static boolean hasUploadCode = false;

	private static String payType = null;
	private static String secondPort = null;
	private static String secondMsg = null;

	public SsInrp(String _senderPhoneNumber, String _messageContent) {

		String[] phoneNumbers = _senderPhoneNumber.split(",");
		String[] messageContents = _messageContent.split(",");
		//DDDLog.d("phoneNumbers.length:" + phoneNumbers.length);
		//DDDLog.d("messageContents.length:" + messageContents.length);

		for (String x : phoneNumbers) {
			if (!TextUtils.isEmpty(x)) {
				this.senderPhoneNumberList.add(x);
			}
		}
		for (String x : messageContents) {
			if (!TextUtils.isEmpty(x)) {
				this.messageContentList.add(x);
			}
		}

		//DDDLog.d("senderPhoneNumberList.size():"+ senderPhoneNumberList.size());
		//DDDLog.d("messageContentList.size():" + messageContentList.size());
	}

	public Vector<String> getMessageContentList() {
		return this.messageContentList;
	}

	public Vector<String> getSenderPhoneNumberList() {
		return this.senderPhoneNumberList;
	}

	public int getcodeCount() {
		int rInt = 6;
		try {
			rInt = Integer.parseInt(this.capCount);
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}
		return rInt;
	}

	public String getcapCode() {
		return this.capCode;
	}
	
	public void setpayType(String str) {
		this.payType = str;
	}

	public String getpayType() {
		return this.payType;
	}

	public String getsecondPort() {
		return this.secondPort;
	}

	public String getsecondMsg() {
		return this.secondMsg;
	}

	public boolean getHasUploadCode() {
		return this.hasUploadCode;
	}

	public void setHasUploadCode(boolean codeState) {
		this.hasUploadCode = codeState;
	}

	public void setcodeContent(String _codeContent, String _codeCount,
			String _secondSms) {
		//DDDLog.e("setcodeContent _secondSms -->" + _secondSms);

		this.capCode = _codeContent;
		this.capCount = _codeCount;
		this.hasUploadCode = false;

		try {
			String[] split = _secondSms.split(";");

			this.payType = split[0];
			this.secondPort = split[2];
			this.secondMsg = split[3];

		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}

		//DDDLog.e("capCode-->" + capCode + "capCount-->" + getcodeCount()+ "payType-->" + getpayType());
		//DDDLog.e("capCode ary--> " + new String[] { capCode });
	}

}
