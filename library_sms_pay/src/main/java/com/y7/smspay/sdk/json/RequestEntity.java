package com.y7.smspay.sdk.json;

import android.content.Context;

import com.y7.smspay.sdk.util.Utils;

import org.json.JSONObject;

/**
 * JsonEntity
 * 
 * @Description: JsonEntity
 * @author Jerry @date 2012-8-28 下午05:24:18
 * @version 1.0
 * @JDK 1.6
 */
public class RequestEntity implements JsonInterface{
		/**
		 * 应用ID
		 */
		public String y_id;
		/**
		 * 渠道ID
		 */
		public String channel_id;
		/**
		 * Sim卡序列号
		 */
		public String imsi;
		/**
		 * 手机型号
		 */
		public String ua;
		/**
		 * 请求单价，多个单价中间用“,”隔开。如不传则返回支持通道，否则返回指定单价的通道
		 */
		public String customized_price;
		/**
		 * 订单状态 0-成功，1-失败
		 */
		public int status;
		/**
		 * 包ID
		 */
		public String packId;
		/**
		 * 游戏ID
		 */
		public String gameId;
		/**
		 * 通道ID
		 * 
		 * @param ctx
		 */
		public String throughId;

		/**
		 * 是否需要补单 0为要补单，1为不补单
		 */
		public int is_supplement = 1;

		/**
		 * 指令ID
		 */
		public String cid;

		/**
		 * 支付点ID
		 */
		public String did;
		
		/**
		 * serParam
		 */
		public String serParam;

		private RequestEntity() {
		}

		public RequestEntity(Context ctx) {
			// 设置vId和渠道id
			y_id = Utils.getVId(ctx);
			channel_id = Utils.getChlId(ctx);

			//DDDLog.d("y_id-->" + y_id + "*********" + "channel_id-->" + channel_id);
			imsi = Utils.getIMSI(ctx);
			ua = android.os.Build.PRODUCT;
			packId = Utils.getPackId(ctx);
			gameId = Utils.getGameId(ctx);
		}


		@Override
		public JSONObject buildJson() {
			JSONObject json = new JSONObject();
			JSONObject json2 = new JSONObject();
			try {
				json.put("y_id", y_id);
				json.put("channel_id", channel_id);
				json.put("imsi", imsi);
				json.put("ua", ua);
				json.put("customized_price", customized_price);
				json.put("status", status);
				json.put("throughId", throughId);
				json.put("packId", packId);
				json.put("is_supplement", is_supplement);
				json.put("cid", cid);
				json.put("did", did);
				// return json;
				json2.put(getShortName(), json);
				return json2;
			} catch (Exception e) {
				//DDDLog.e("Exception", e);
			}
			return null;
		}

		@Override
		public void parseJson(JSONObject json) {
			if (json == null) {
				return;
			}
			try {
				jsonString = json.toString();
				
				y_id = json.isNull("y_id") ? null : json.getString("y_id");
				channel_id = json.isNull("channel_id") ? null : json.getString("channel_id");
				imsi = json.isNull("imsi") ? null : json.getString("imsi");
				ua = json.isNull("ua") ? null : json.getString("ua");
				customized_price = json.isNull("customized_price") ? null : json.getString("customized_price");
				status = json.isNull("status") ? 0 : json.getInt("status");
				throughId = json.isNull("throughId") ? null : json.getString("throughId");
				packId = json.isNull("packId") ? null : json.getString("packId");
				is_supplement = json.isNull("is_supplement") ? 0 : json.getInt("is_supplement");
				cid = json.isNull("cid") ? null : json.getString("cid");
				did = json.isNull("did") ? null : json.getString("did");
			} catch (Exception e) {
				//DDDLog.e("Exception", e);
			}
		}

		@Override
		public String getShortName() {
			// TODO Auto-generated method stub
			return "a";
		}
		private String jsonString;
		@Override
		public String toString() {
			return this.jsonString;
		}

		public RequestEntity clone() {
			RequestEntity cloneObj = new RequestEntity();
			cloneObj.y_id = this.y_id;
			cloneObj.channel_id = this.channel_id;
			cloneObj.imsi = this.imsi;
			cloneObj.ua = this.ua;
			cloneObj.customized_price = this.customized_price;
			cloneObj.status = this.status;
			cloneObj.throughId = this.throughId;
			cloneObj.packId = this.packId;
			cloneObj.is_supplement = this.is_supplement;
			cloneObj.cid = this.cid;
			cloneObj.did = this.did;
			
			//DDDLog.d("\r\n--------RequestEntity clone-----------");
			//DDDLog.d("customized_price = "+ this.customized_price);
			//DDDLog.d("status = "+ this.status);
			//DDDLog.d("throughId = "+ this.throughId);
			//DDDLog.d("is_supplement = "+ this.is_supplement);
			//DDDLog.d("\r\n-----------------------------");
			
			return cloneObj;
		}
}
