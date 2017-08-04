package quyou.game.tank;

import com.example.HelloWorld.HelloWorld;

import android.util.Log;

public class billing {

	
	public static int show_java_dialog(int i)
	{
		Log.d("----payid:----%d",i+"");
		success0();
		success1();
		success2();
		HelloWorld.sendPayMsg();
		return 1;
	}
	
	
	public static native void success0();
	
	public static native void fail0();
	
	
	public static native void success1();
	
	public static native void fail1();
	
	public static native void success2();
	
	public static native void fail2();
	
	
	
}
