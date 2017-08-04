package android.pay;


/***********************************************************************************
 * 这个是一个计费的接口，提供一些主要的方法
 * 针对部分平台可能会有不同的方法，请自己往后面添加，并保持此文件始终是最新版本了
 * 版本号 1.0.2
 * update by yang.rui
 */
public interface PayManager {
	/*************用于计费结果判断的常量***********************/
	public static final byte PR_NULL=0;     //无付费结果
	public static final byte PR_COMPLTE=1;  //付费成功
	public static final byte PR_FAILED=2;   //付费失败
	
	/************下面是所有平台都用到的方法*******************/
	public void init();             //初始化的方法,游戏平台需要用到
	public void pay(int payType);  //付费
	public boolean doLogic(); 	    //计费逻辑，主要用来检测当前是否在计费界面的
	public byte getPayResult();     //获得当前计费的结果
	public void resetPayResult();   //重置付费后的结果,一般重置计费结果
	public byte getPayID();         //得到当前计费成功或者失败的计费点
}
