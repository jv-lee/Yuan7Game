package com.y7.smspay.sdk.json;

import org.json.JSONObject;

public class ThroughEntity implements JsonInterface {
	/** 通道ID */
	public String id;
	/* 通道类型 0,SDK,1,传统裸代,2,动态裸代 */
	public int type;
	/* 间隔时间 */
	public int timing;
	/* 限制支付金额 */
	public String supplyprice;
	/* 通道名称 */
	public String name;


	public ThroughEntity() {
		/**设置默认走易付通*/
		id = "";
		type = 0;
		name = "";
		timing = 1;
		supplyprice = "0";
	}
	
	@Override
	public JSONObject buildJson() {
		try {
			JSONObject json = new JSONObject();
			json.put("id", id);
			json.put("type", type);
			json.put("timing", timing);
			json.put("supplyprice", supplyprice);
			json.put("name", name);
			return json;
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}
		return null;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null){
			return;
		}
		try {
			jsonString = json.toString();
			
			id = json.isNull("id") ? null : json.getString("id");
			type = json.isNull("type") ? -1 : json.getInt("type");
			timing = json.isNull("timing") ? 0 : json.getInt("timing");
			supplyprice = json.isNull("supplyprice") ? null : json.getString("supplyprice");
			name = json.isNull("name") ? null : json.getString("name");
		} catch (Exception e) {
			//DDDLog.e("Exception", e);
		}
	}

	@Override
	public String getShortName() {
		return null;
	}
	
	
	private String jsonString;
	@Override
	public String toString() {
		return this.jsonString;
	}
}
