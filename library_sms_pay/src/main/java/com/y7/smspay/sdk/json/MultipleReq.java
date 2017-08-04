package com.y7.smspay.sdk.json;

//多次请求开关
public class MultipleReq {
	public static int interval = 0;// 每隔3秒请求一次通道
	public static int count = 0;// 请求次数

	public static String product = null;// 计费点名称
	public static String price = null;// 请求资费
	public static String did = null;

	public static void clearParam() {
		MultipleReq.interval = 0;
		MultipleReq.count = 0;

		MultipleReq.product = null;
		MultipleReq.price = null;
		MultipleReq.did = null;
	}
}
