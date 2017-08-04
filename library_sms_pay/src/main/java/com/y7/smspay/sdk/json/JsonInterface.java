package com.y7.smspay.sdk.json;

import org.json.JSONObject;

interface JsonInterface {
	JSONObject buildJson();
	//解析json
	void parseJson(JSONObject json);
	//获取键值对
	String getShortName();
}
