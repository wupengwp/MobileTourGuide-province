package com.jiagu.mobile.tourguide.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
//关于界面
public class AboutActivity extends TitleDrawerActivity implements OnClickListener{
	private TextView textview;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ImageButton imagereturn = (ImageButton) findViewById(R.id.activity_about_image_return);
		imagereturn.setOnClickListener(this);
		textview = (TextView) findViewById(R.id.textview01);
		textview.setText("        "+"手机导游平台是由西安甲骨企业文化传播有限公司与《西北旅游》杂志联合开发研制,应用于旅游过程中的语音导游、语音导航、旅游团购等旅行辅助功能。");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_about_image_return:
			onBackPressed();
			break;
		}
	}
}
