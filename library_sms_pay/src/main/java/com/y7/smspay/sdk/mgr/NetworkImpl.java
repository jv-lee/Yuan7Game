package com.y7.smspay.sdk.mgr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.y7.smspay.sdk.util.StrUtils;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


/**
 * @Description: TODO(添加描述)
 * @author Jerry @date 2011-9-27 上午09:29:11
 * @version 1.0
 * @JDK 1.6
 */

public class NetworkImpl {
	/**
	 * 获取一个网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static HttpClient getHttpClient(Context context) {
		HttpClient client = null;
		if (getNetworkTypeName(context) == null)
			return client;
		if (isCmwapConnection(context)) {
			// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
			HttpParams httpParams = new BasicHttpParams();
			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);
			HttpConnectionParams.setSocketBufferSize(httpParams, 200 * 1024);
			// 设置重定向，缺省为 true
			HttpClientParams.setRedirecting(httpParams, true);
			HttpHost httpHost = new HttpHost(StrUtils.HTTPHOST1, 80);
			if(StrUtils.INTERNET1.equalsIgnoreCase(getNetworkTypeName(context))) {
				httpHost = new HttpHost(StrUtils.HTTPHOST2, 80);
			}
			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
			client = new DefaultHttpClient(httpParams);
		} else {
			client = new DefaultHttpClient();
		}
		
		return client;
	}

	/**
	 * 判断是否是3gwap cmwap uniwap
	 */
	protected static boolean isCmwapConnection(Context ctx) {
		String str = getNetworkTypeName(ctx);
		return str != null ? (str.compareTo(StrUtils.INTERNET2) == 0
				|| str.compareTo(StrUtils.INTERNET3) == 0 || str.compareTo(StrUtils.INTERNET4) == 0)
				: false;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return wifi cmnet cmwap uniwap uninet...
	 */
	public static String getNetworkTypeName(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		if (info == null) {
			return null;
		}
		return info.getTypeName();
	}
}
