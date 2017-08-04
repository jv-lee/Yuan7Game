package com.y7.smspay.mp.srv;
/**
 * 请求返回接口
 * @author Administrator
 */
public interface MPoyCallback {
	void onResult(int state, String price);
}