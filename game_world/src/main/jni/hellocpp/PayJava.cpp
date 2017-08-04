#include "cocos2d.h"
#include <jni.h>
#include "platform/android/jni/JniHelper.h"
#include "PayJava.h"
#include "Pay.h"

#define CLASS_NAME "android/pay/Pay"

using namespace cocos2d;

extern "C"
{
void pay(int payId)
{
	JniMethodInfo t;
	if(JniHelper::getStaticMethodInfo(t, CLASS_NAME, "payMsg", "(I)V"))
	{
//		jstring jTitle = t.env->NewStringUTF(title);
//		jstring jMsg = t.env->NewStringUTF(msg);
		t.env->CallStaticVoidMethod(t.classID, t.methodID, payId);
		CCLog("pay");
//		t.env->DeleteLocalRef(jTitle);
//		t.env->DeleteLocalRef(jMsg);
	}
}

void moreGame()
{
	JniMethodInfo t;
		if(JniHelper::getStaticMethodInfo(t, CLASS_NAME, "moreGame", "()V"))
		{
	//		jstring jTitle = t.env->NewStringUTF(title);
	//		jstring jMsg = t.env->NewStringUTF(msg);
			t.env->CallStaticVoidMethod(t.classID, t.methodID);
			CCLog("moreGame");
	//		t.env->DeleteLocalRef(jTitle);
	//		t.env->DeleteLocalRef(jMsg);
		}
}

void forum()
{
	JniMethodInfo t;
		if(JniHelper::getStaticMethodInfo(t, CLASS_NAME, "forum", "()V"))
		{
	//		jstring jTitle = t.env->NewStringUTF(title);
	//		jstring jMsg = t.env->NewStringUTF(msg);
			t.env->CallStaticVoidMethod(t.classID, t.methodID);
			CCLog("forum");
	//		t.env->DeleteLocalRef(jTitle);
	//		t.env->DeleteLocalRef(jMsg);
		}
}


void exitGame()
{
	JniMethodInfo t;
		if(JniHelper::getStaticMethodInfo(t, CLASS_NAME, "exitGame", "()V"))
		{
	//		jstring jTitle = t.env->NewStringUTF(title);
	//		jstring jMsg = t.env->NewStringUTF(msg);
			t.env->CallStaticVoidMethod(t.classID, t.methodID);
			CCLog("exitGame");
	//		t.env->DeleteLocalRef(jTitle);
	//		t.env->DeleteLocalRef(jMsg);
		}
}
//void Java_android_pay_Pay_EGame_setPackageName(JNIEnv *env, jobject thiz, jstring packageName)
//{
//	const char *pkgName = env->GetStringUTFChars(packageName, NULL);
//	setPackageName(pkgName);
//	env->ReleaseStringUTFChars(packageName, pkgName);
//}

	void Java_android_pay_Pay_setPackage(JNIEnv *env, jobject thiz, jstring packageName)
	{
		const char *pkgName = env->GetStringUTFChars(packageName, NULL);
		Pay::sharePay()->setPackage(pkgName);
		env->ReleaseStringUTFChars(packageName, pkgName);
	}

	void Java_android_pay_Pay_payFail(JNIEnv *env, jobject thiz)
	{
		Pay::sharePay()->payFail();
	}

	void Java_android_pay_Pay_payOK(JNIEnv *env, jobject thiz)
	{
		Pay::sharePay()->payOK();
	}
}


