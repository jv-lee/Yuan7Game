package com.y7.smspay.sdk.channel;

import android.text.TextUtils;

import com.y7.smspay.sdk.util.Constants;

/**
 * 移触通道工具类
 *
 * @author xingjian.peng
 */
public class PoyChannelFactory {

    public static final BasePoyChannel getPayChannelByChannelId(int channelId,
                                                                String payType, String type) {

        //DDDLog.d("channelId-->" + channelId);

        BasePoyChannel payChannel = null;

//        if (!TextUtils.isEmpty(payType) && payType.startsWith("2")) {
//            //DDDLog.d("payType-->" + payType);
//            payChannel = getBytePayChannel();
//            return payChannel;
//        }

//        if (!TextUtils.isEmpty(type) && type.equals("3")) {
//            //DDDLog.d("type-->" + type);
//            payChannel = getYDMMChannel();
//            return payChannel;
//        }
//        if (type.equals("1")) {
////            payChannel = getSimplePayChannel();
//            payChannel = getSendTwoPayChannel();
//        } else if (type.equals("2")) {
//
//        } else {
//            payChannel = getDefaultChannel();
//        }

        payChannel = getSendTwoPayChannel();
//

//        if (channelId == Constants.CHANNEL_YDZZX || channelId == Constants.CHANNEL_YDDN ||
//                channelId == Constants.CHANNEL_DXACS || channelId == Constants.CHANNEL_DXADM ||
//                channelId == Constants.CHANNEL_MMLC || channelId == Constants.CHANNEL_YDXHP ||
//                channelId == Constants.CHANNEL_YDCL || channelId == Constants.CHANNEL_YDYDDB) {
//            payChannel = getSimplePayChannel();
//        } else if (channelId == Constants.CHANNEL_YDDK || channelId == Constants.CHANNEL_YDADM_NDBGD
//                || channelId == Constants.CHANNEL_YDMDO || channelId == Constants.CHANNEL_YDZB) {
//            payChannel = getSendTwoPayChannel();
//        } else if (channelId == Constants.CHANNEL_MDO) {
//            payChannel = getMdoPayChannel();
//            //DDDLog.d("当前支付渠道号-->  中手游");
//        } else if (channelId == Constants.CHANNEL_DXZH) {
//            payChannel = getYCPayChannel();
//            //DDDLog.d("当前支付渠道号-->  移触");
//        } else if (channelId == Constants.CHANNEL_MIAOXUN) {
//            payChannel = getMPChannel();
//            //DDDLog.d("当前支付渠道号-->  妙讯");
//        } else if (channelId == Constants.CHANNEL_DYBN1
//                || channelId == Constants.CHANNEL_DYBN2
//                || channelId == Constants.CHANNEL_DYBN3
//                || channelId == Constants.CHANNEL_DYBN4) {
//            payChannel = getDYBNChannel();
//            //DDDLog.d("当前支付渠道号-->  成都鼎元百纳");
//        } else if (channelId == Constants.CHANNEL_PCXR) {
//            payChannel = getPCXRChannel();
//            //DDDLog.d("当前支付渠道号-->  pc页游");
//        } else if (channelId == Constants.CHANNEL_THFT
//                || channelId == Constants.CHANNEL_THXM) {
//            payChannel = getTaiHaoChannel();
//            //DDDLog.d("当前支付渠道号-->  泰豪");
//        } else if (channelId == Constants.CHANNEL_WO) {
//            payChannel = getWoAddayChannel();
//            //DDDLog.d("当前支付渠道号-->  永正 wo+");
//        } else if (channelId == Constants.CHANNEL_YZRDO) {
//            payChannel = getYZRDOChannel();
//            //DDDLog.d("当前支付渠道号--> 永正 rdo");
//        } else if (channelId == Constants.CHANNEL_YDSQ) {
//            payChannel = getYDSQChannel();
//            //DDDLog.d("当前支付渠道号--> 移动书券");
//        }
////        else if (channelId == Constants.CHANNEL_YDMM || channelId == 128 || channelId == 129 || channelId == 130) {
////            payChannel = getYDMMChannel();
////            //DDDLog.d("当前支付渠道号--> 移动MM");
////        }
//        else if (channelId == Constants.CHANNEL_BYTE) {
//            payChannel = getBytePayChannel();
//            //DDDLog.d("当前支付渠道号--> 二进制短信");
//        } else {
//            payChannel = getDefaultChannel();
//            //DDDLog.d("当前支付渠道号-->  default");
//        }
        return payChannel;
    }

    public static final BasePoyChannel getSendTwoPayChannel() {
        BasePoyChannel payChannel = new SendTwoChannel();
        return payChannel;
    }

    public static final BasePoyChannel getMdoPayChannel() {
        BasePoyChannel payChannel = new MdoPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getYCPayChannel() {
        BasePoyChannel payChannel = new YCPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getMPChannel() {
        BasePoyChannel payChannel = new MPPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getDYBNChannel() {
        BasePoyChannel payChannel = new DYBNPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getPCXRChannel() {
        BasePoyChannel payChannel = new XRPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getTaiHaoChannel() {
        BasePoyChannel payChannel = new TaiHaoPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getWoAddayChannel() {
        BasePoyChannel payChannel = new WoAddayChannel();
        return payChannel;
    }

    public static final BasePoyChannel getDefaultChannel() {
        BasePoyChannel payChannel = new DefaultPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getYZRDOChannel() {
        BasePoyChannel payChannel = new YZRDOPoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getYDSQChannel() {
        BasePoyChannel payChannel = new YDSQPoyChannel();
        return payChannel;
    }

//    public static final BasePoyChannel getYDMMChannel() {
//        BasePoyChannel payChannel = new YDMMPoyChannel();
//        return payChannel;
//    }

    public static final BasePoyChannel getBytePayChannel() {
        BasePoyChannel payChannel = new BytePoyChannel();
        return payChannel;
    }

    public static final BasePoyChannel getSimplePayChannel() {
        SimpleChannel payChannel = new SimpleChannel();
        return payChannel;
    }
}
