package com.jiagu.mobile.tourguide.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.adapter.HelpAdapter;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
/**
 * 
 * @author Administrator 2014.12.31 高磊 使用帮助页面
 * 
 */
public class HelpActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ScrollView mLinearLayout;
	private ViewPager viewpager;
	private static int a = 1;
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		mLinearLayout = (ScrollView) findViewById(R.id.help_linearlayout);
		ImageButton imagereturn = (ImageButton) findViewById(R.id.activity_help_image);
		viewpager = (ViewPager) findViewById(R.id.help_viewpager);
		TextView tv01 = (TextView) findViewById(R.id.help_textView_01);
		TextView tv02 = (TextView) findViewById(R.id.help_textView_02);
		TextView tv03 = (TextView) findViewById(R.id.help_textView_03);
		TextView tv04 = (TextView) findViewById(R.id.help_textView_04);
		TextView tv05 = (TextView) findViewById(R.id.help_textView_05);
		imagereturn.setOnClickListener(this);
		tv01.setOnClickListener(this);
		tv02.setOnClickListener(this);
		tv03.setOnClickListener(this);
		tv04.setOnClickListener(this);
		tv05.setOnClickListener(this);
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
		case R.id.activity_help_image:
			onBackPressed();
			break;
		case R.id.help_textView_01:
			mLinearLayout.setVisibility(View.GONE);
			int[] dataA = {R.drawable.help_shishidaoyou_01,
					R.drawable.help_shishidaoyou_03,R.drawable.help_shishidaoyou_02 };
			HelpAdapter helpAdapterA = new HelpAdapter(HelpActivity.this,
					dataA);
			a=2;
			viewpager.setAdapter(helpAdapterA);
			viewpager.setVisibility(View.VISIBLE);
			break;
		case R.id.help_textView_02:
			mLinearLayout.setVisibility(View.GONE);
			int[] dataB = {R.drawable.help_zhuyaojingguan_01};
			HelpAdapter helpAdapterB = new HelpAdapter(HelpActivity.this,
					dataB);
			a=2;
			viewpager.setAdapter(helpAdapterB);
			viewpager.setVisibility(View.VISIBLE);
			break;
		case R.id.help_textView_03:
			mLinearLayout.setVisibility(View.GONE);
			int[] dataC = {R.drawable.help_tesexian};
			HelpAdapter helpAdapterC = new HelpAdapter(HelpActivity.this,
					dataC);
			a=2;
			viewpager.setAdapter(helpAdapterC);
			viewpager.setVisibility(View.VISIBLE);
			break;
		case R.id.help_textView_04:
			mLinearLayout.setVisibility(View.GONE);
			int[] dataD = {R.drawable.help_huodeongxiangqing};
			HelpAdapter helpAdapterD = new HelpAdapter(HelpActivity.this,
					dataD);
			a=2;
			viewpager.setAdapter(helpAdapterD);
			viewpager.setVisibility(View.VISIBLE);

			break;
		case R.id.help_textView_05:
			mLinearLayout.setVisibility(View.GONE);
			int[] dataE = {R.drawable.help_gonglue};
			HelpAdapter helpAdapter = new HelpAdapter(HelpActivity.this,
					dataE);
			a=2;
			viewpager.setAdapter(helpAdapter);
			viewpager.setVisibility(View.VISIBLE);
			break;
		}
	}
	@Override
	public void onBackPressed() {
		switch (a) {
		case 1:
			super.onBackPressed();
			break;
		case 2:
			viewpager.setVisibility(View.GONE);
			mLinearLayout.setVisibility(View.VISIBLE);
			a=1;
			break;

		}
	}
}
