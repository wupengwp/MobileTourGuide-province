package com.jiagu.mobile.tourguide.utils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DownloadStatistics {
	private static final String DOWNLOAD_STATISTICS = "downloadCount/statistics.htm";// 获取景点信息的请求地址
	
	private String mobileid;
	private String mobilebrand;
	private String oldversion;
	private String newversion;
	private String downloadfile;
	private String downloadtime;
	private String nettype;
	private String status;
	
	public void setMobileid(String mobileid){
		this.mobileid = mobileid;
	}
	
	public void setMobilebrand(String mobilebrand){
		this.mobilebrand = mobilebrand;
	}
	
	public void setOldversion(String oldversion){
		this.oldversion = oldversion;
	}
	
	public void setNewversion(String newversion){
		this.newversion = newversion;
	}
	
	public void setDownloadfile(String downloadfile){
		this.downloadfile = downloadfile;
	}

	public void setNettype(String nettype){
		this.nettype = nettype;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	// 从服务器获得景点描述信息，向服务器请求需要展示的景点
	public void submitStatistics() {
		RequestParams params = new RequestParams();
		
		params.put("mobileid", mobileid);// 设置搜索范围
		params.put("mobilebrand", mobilebrand);//
		params.put("oldversion", oldversion);//
		params.put("newversion", newversion);//
		params.put("downloadfile", downloadfile);//
		params.put("nettype", nettype);//
		params.put("status", status);//
		
		String serverUrl = Path.SERVER_ADRESS + DOWNLOAD_STATISTICS;// 请求地址
		
		HttpUtil.post(serverUrl, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
			}
		});
	}
}
