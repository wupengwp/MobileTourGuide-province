package com.jiagu.mobile.wc;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;

public class ReportActivity extends TitleDrawerActivity{
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
	}
}
