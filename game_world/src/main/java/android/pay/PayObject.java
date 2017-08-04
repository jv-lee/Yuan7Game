package android.pay;


import com.xmyd.puzkkb.mm.GameActivity;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

/********************************************
 * 这个是一个抽象类，用来实现PayManger的接口的
 * 是所有计费平台必须继承的类，主要用来实现共同的绘制和逻辑处理
 * version 1.0.2
 * @author yang.rui
 */
public abstract class PayObject implements PayManager{
	
	//设定一些需要的变量
	public static Activity currentActivity;//当前这个工程或者游戏的Activity实例
	public byte mCurTargetPayID=-1;  //当前的计费点
	public byte mLastTargetPayID=-1; //记录最后一次的计费点,这个是当退出计费计费点的时候的计费点
	static byte mCurPayResult; //记录付费的结果
	
	/***************************** 下面是通用的方法和接口方法  *********************************/
	//构造方法
	public PayObject(){
		currentActivity = GameActivity.instance;
	}
	
	//付费的方法
	public void pay(int payType){
		mLastTargetPayID = mCurTargetPayID = (byte) payType; //设置当前的计费点
		mCurPayResult = PayObject.PR_NULL;
		setPayInfoVar();  //设置初始化信息,游戏平台用到了
		sendMessage();  //真正发送短信的地方
	}
	
	public void resetPayInfoVar(){ //重置一些变量的信息，每个平台不一样，但调用的地方就是信息返回和功能界面返回我们的界面的时候
		mCurTargetPayID = -1;
	}
	
	public boolean doLogic(){  //判断当前是否还在计费中,对于不想判定是否在计费中的话，可重写此方法即可，重写返回值为false就行
		if(mCurTargetPayID!=-1){
			return true;
		}
		return false;
	}
	
	public byte getPayID(){  //获得当前的计费点,其实或的是退出计费时候记录的那个计费点
		return mLastTargetPayID;
	}
	
	//付费的一些辅助的方法
	public static void setPayResult(byte payResult){  //设置返回结果的,方便外部调用的
		mCurPayResult = payResult;
	}
	
	public byte getPayResult(){ //得到当前的计费结果
		return mCurPayResult;
	}
	
	public void resetPayResult(){ //重置当前的计费结果
		mLastTargetPayID = -1;
		mCurPayResult = PR_NULL;
	}
	
	//计费结果的一个消息提示,使用系统的消息提示
	Toast tt;
	public void showMessage(String mes){
		if(tt != null){
			tt.cancel();
			tt = null;
		}
		tt = Toast.makeText(currentActivity,mes,Toast.LENGTH_SHORT);
		tt.setGravity(Gravity.CENTER, 0, 0);
		tt.show();
	}
	
	//切回到我们的页面可能需要做的操作
	public void onRestart(){
		
	}
	
	public void onPause(){
		
	}
	
	//下面是需要每个平台都要实现的接口方法
	public abstract void sendMessage();//发送短
	public abstract void setPayInfoVar(); //根据平台初始化计费信息
	public boolean isRepeat(){return false;}
	
	//有些计费，需要将计费的权利交给UI线程，所以写了下面的方法供使用,重写最后一个方法即可,即消息处理机制
	public byte state; //设置当前的状态，是初始化，还是进入付费
	public void setState(byte st){
		state = st;
	}
	
	public void setHanlerManager(){ //这个方法是供子类来调用的方法，
		Thread thread=new Thread(null,doBackgroundThreadProcessing,"Background");
		thread.start();
	}
	
	public void handlerManager(){ //针对计费使用消息机制的处理，这个是供子类重写的方法
		
	}
	
	//下面是消息机制的处理
	private Runnable doBackgroundThreadProcessing=new Runnable() {
		public void run() {
			backgrondThreadProcessing();
		}
	};
	
	private void backgrondThreadProcessing(){
		GameActivity.instance.mHandler.post(doUpdateGUI);
	}
	
	
	private Runnable doUpdateGUI=new Runnable() {
		@Override
		public void run() {
			handlerManager();
		}
	};
	
	public void init(){}
	public void platformRequest(){}//更多游戏
	
	/****************************************
	 * 
	 */
	/*************方便在做View之间跳转的情况下需要使用的方法********/
	//下面是调用挂起和恢复的方法，也就是当我们进行View之间跳转的时候，先调用下挂起，等返回我们的界面的时候，调用下恢复就OK了
	public void reStartOnPause(){
//		SanGuoCA_SiKai.instance.reStartOnPause();
	}
	
	public void reStartOnresum(){
//		SanGuoCA_SiKai.instance.reStartOnResum();
	}
	
	
}
