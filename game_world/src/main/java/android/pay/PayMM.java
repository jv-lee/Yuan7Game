package android.pay;

import java.util.HashMap;

import com.xmyd.puzkkb.mm.GameActivity;


import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/*******************
 * 电信平台新的付费系统,针对上一个版本添加了SDK的应用
 * 版本1.0.0
 * update by yang.rui
 */
public class PayMM extends PayObject {
	
	//需要使用到的变量
	private PayMM instance;
	boolean isInitializeOver = false; //是否正在初始化中
	boolean isInitializeSuccess = false; //是否初始化成功
	
	//构造方法
	public PayMM() {
		super();
		instance = this; //得到当前实现接口的实例
		
		//初始化MM的信息
		initMM();
	}
	
	public static void payMsg(int id)
	{
		GameActivity.sendPayMsg(id);
		//GameActivity.sendMsg(GameActivity.PAY_DIALOG);
		/*if (id==0) {
			GameActivity.sendMsg(GameActivity.ACTIVE_DIALOG);
		} else {
			GameActivity.sendPayMsg(id);
		}*/
	}
	
	public void pay_(int id)
	{
		//purchase.order(currentActivity,payCode[id],instance);
		//GameActivity.sendMsg(GameActivity.PAY_DIALOG);
		/*if (id==0) {
			GameActivity.sendMsg(GameActivity.ACTIVE_DIALOG);
		} else {
			GameActivity.sendPayMsg(id);
		}*/
	}
	
	public static void moreGame()
	{
		GameActivity.sendMsg(GameActivity.GAME_ABOUT);
	}
	
	/***************实现父类的一些方法*********************/
	public void setPayInfoVar(){
		mCurPayResult = PR_NULL;
	}
	
	public void resetPayInfoVar(){
		super.resetPayInfoVar();
	}
	
	public void sendMessage() { //发送短信
		setState((byte) 1);
		setHanlerManager();
	}
	
	public void handlerManager(){ //针对计费使用消息机制的处理，这个是供子类重写的方法
		switch(state){
			case 0:  //初始化的操作
				break;
			case 1:
//				Dialog_Show();
				 payMM();
				break;
		}	
	}
	/********************MM单独需要的一些特殊处理的实现的接口方法*************************/

	
	public void onAfterApply() {
		
	}

	
	public void onAfterDownload() {
		
	}

	
	public void onBeforeApply() {
		
	}

	
	public void onBeforeDownload() {
		
	}

	
	public void onBillingFinish(int code, HashMap arg1) {
		/*String result = "订购结果：订购成功";
		if (code == PurchaseCode.ORDER_OK || (code == PurchaseCode.AUTH_OK)) { //购买成功或者已经购买
			//购买成功后，我们需要做的操作
			setPayResult(PayObject.PR_COMPLTE);
			GameActivity.sendMsg(GameActivity.CHECK_EMS_OK);
			resetPayInfoVar();
			onRestart();
		}else{ //购买失败的操作
			result = "订购结果：" + Purchase.getReason(code);//购买失败的原因
			setPayResult(PayObject.PR_FAILED);
			GameActivity.sendMsg(GameActivity.CHECK_EMS_FAIL);
			resetPayInfoVar();
			onRestart();
		}*/
	}


	public void onInitFinish(int arg0) {
		isInitializeOver = true;//不管是初始化失败还是成功，说明初始化结束了
		if(arg0 == 100){ //说明初始化成功
			isInitializeSuccess = true;
		}else{
			isInitializeSuccess = false;
		}
	}


	public void onQueryFinish(int code, HashMap arg1) {
		/*if (code == PurchaseCode.QUERY_OK) { //查到该商品，说明付过费
			setPayResult(PayObject.PR_COMPLTE);
			resetPayInfoVar();
		}else{ //说明未购买，此时考虑购买
			purchase.order(currentActivity,payCode[mCurTargetPayID],instance);
			resetPayInfoVar();
		}*/
	}
	

	public void onUnsubscribeFinish(int arg0) {
		
	}
	/********************MM平台需要添加的变量和方法**************************************/
	//private Purchase purchase;
	public void initMM(){
		/*try{
			purchase = Purchase.getInstance();
			purchase.setAppInfo(APPID, APPKEY);
			purchase.init(currentActivity,instance);
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	public void payMM(){
		if(needQuary[mCurTargetPayID]){ //如果是一次性付费的，看是否本机器上已经付过费了，如果付过了，即使删掉再安装也不付费了
			//purchase.query(currentActivity,payCode[mCurTargetPayID],instance); //根据查询结果来决定是否计费
		}else{
			//purchase.order(currentActivity,payCode[mCurTargetPayID],instance);
		}
	}

	public void Dialog_Show(){
		Builder builder = new Builder(currentActivity);
		builder.setCancelable(false);
		builder.setTitle("提示");
		builder.setMessage(TYPE_PAY_INFO[mCurTargetPayID]);
		builder.setPositiveButton("确认", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();
			   payMM();
		   }
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    mCurPayResult=PR_FAILED;
			resetPayInfoVar();
		   }
		});
		
		builder.show();
	}
	
	public static native void payFail();
	
	public static native void payOK(int id);
	
	/********************MM平台需要修改信息的地方的地方**************************************/
	//应用的基本信息
	private static final String APPID = "300008277786";
	private static final String APPKEY = "AA553678B1481A04";
	
	//计费点的基本信息
	public final static boolean[] needQuary = {false,false,false,false,false, false,false,false,false,false};//是否需要查询，针对当前手机卡已经付费的删掉游戏后，重新安装不需要再次付费的情况
	public final static String[] payCode = {"30000827778601","30000827778602","30000827778603", "30000827778604", "30000827778605", "", ""};
	
	public static final String[] TYPE_PAY_INFO={  //发送的信息提示
		"正版激活",
		"闪电链",
		"时间沙漏",
		"TNT",
		"板块移动",
		"",
		"",
	};
}
