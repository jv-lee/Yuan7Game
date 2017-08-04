package com.y7.smspay.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PoySharedPreference {

	private static PoySharedPreference instance;
	private SharedPreferences sharedPreferences;

	private String PREFERENCE_NAME = "yc.paysdk";
	private String PRIORITY_THROUGHID = "PRIORITY_THROUGHID";//渠道ID优先级
	private String defaultPriorityThrough = 
//											Constants.CHANNEL_HUOLI + "," + 
//											Constants.CHANNEL_YFT + 
											""; // 默认支付排序

	/*************************************************************************************/
	private PoySharedPreference(Context context) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
	}

	public static PoySharedPreference getInstance(Context context) {
		if (instance == null) {
			instance = new PoySharedPreference(context);
		}
		return instance;
	}
	/*************************************************************************************/
	
	
	public void setPriorityThrough(String versionDesc) {
		sharedPreferences.edit().putString(PRIORITY_THROUGHID, versionDesc).commit();
	}

	public String getPriorityThrough() {
		return sharedPreferences.getString(PRIORITY_THROUGHID, defaultPriorityThrough);
	}
	
	/**
	 * 保存上一次请求该通道的时间
	 * @param throughId
	 */
	public void setThroughTimer(String throughId) {
		sharedPreferences.edit().putLong(throughId, System.currentTimeMillis()).commit();
	}

	/**
	 * 获取上一次请求该通道的时间
	 * @param throughId
	 * @return
	 */
	public Long getThroughTimer(String throughId) {
		return sharedPreferences.getLong(throughId, 0L);
	}
}
