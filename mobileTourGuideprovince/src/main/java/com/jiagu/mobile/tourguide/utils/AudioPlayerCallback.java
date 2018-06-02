package com.jiagu.mobile.tourguide.utils;

import android.app.Activity;
import android.view.View;

public abstract interface AudioPlayerCallback {
	public void sendErrorTips(String tips);
	public void hideLoading();
	public void showLoading();
	public View getView();
	public Activity getActivity();
	public String getX();
	public String getY();
	public String getName();
	
	public boolean isNeedGuide();
}
