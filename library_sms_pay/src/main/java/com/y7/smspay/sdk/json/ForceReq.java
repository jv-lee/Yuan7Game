package com.y7.smspay.sdk.json;

//制请求开关
public class ForceReq {
	public static int init = 0;// 初始化完成10秒后
	public static int interval = 0;// 每隔3秒自动请求一次通道
	public static int count = 0;// 请求次数
	public static String product = null;// 计费点名称
	public static String price = null;// 请求资费
	public static String did = null;

	public static void clearParam() {
		ForceReq.init = 0;
		ForceReq.interval = 0;
		ForceReq.count = 0;
		ForceReq.product = null;
		ForceReq.price = null;
		ForceReq.did = null;
	}
}
