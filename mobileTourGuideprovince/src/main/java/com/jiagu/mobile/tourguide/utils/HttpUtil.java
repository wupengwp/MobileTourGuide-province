package com.jiagu.mobile.tourguide.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实锟斤拷锟斤拷锟斤拷锟斤拷

	static {
		client.setTimeout(20000); // 锟斤拷锟斤拷锟斤拷锟接筹拷时锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷茫锟侥拷锟轿�10s
		//client.addHeader(arg0, arg1);
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 锟斤拷一锟斤拷锟斤拷锟斤拷url锟斤拷取一锟斤拷string锟斤拷锟斤拷
	{
		client.get(urlString, res);
		
	}

	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // url锟斤拷锟斤拷锟斤拷锟斤拷锟�
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷取json锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷取json锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 锟斤拷锟斤拷锟斤拷锟斤拷使锟矫ｏ拷锟结返锟斤拷byte锟斤拷锟斤拷
	{
		client.get(uString, bHandler);
	}

	public static void post(String urlString, RequestParams params,
			JsonHttpResponseHandler res) // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷取json锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	{
		client.post(urlString, params, res);
	}

	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷取json锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	{
		client.post(urlString, params, res);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}