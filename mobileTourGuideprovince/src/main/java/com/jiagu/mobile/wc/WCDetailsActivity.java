package com.jiagu.mobile.wc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.FeatureDetailsActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;

public class WCDetailsActivity extends TitleDrawerActivity implements
		OnClickListener {
	private ListView listView;
	private ImageView jb;
	private Button but;
	private Switch sw_01,sw_02;
	private TextView dz;
	private TextView dz_xg;

	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wcdetails);
		listView = (ListView) findViewById(R.id.wc_detail_listview);

		View top = LayoutInflater.from(WCDetailsActivity.this).inflate(
				R.layout.activity_wcdetails_top, null);
		jb = (ImageView) top.findViewById(R.id.wc_details_text_jb);
		dz = (TextView) top.findViewById(R.id.wc_d_dz);
		dz_xg = (TextView) top.findViewById(R.id.wc_d_text_dz_xg);
		dz_xg.setText("修改");
		dz_xg.setVisibility(View.GONE);
		sw_01 = (Switch) top.findViewById(R.id.wc_d_sw_01);
		sw_02 = (Switch) top.findViewById(R.id.wc_d_sw_02);
		listView.addHeaderView(top);
		// 添加底部试
		View bottom = LayoutInflater.from(WCDetailsActivity.this).inflate(
				R.layout.bottom_button, null);
		TextView more = (TextView) bottom.findViewById(R.id.bottom_textview);
		listView.addFooterView(bottom);

		but = (Button) findViewById(R.id.wc_btn_xg);
		but.setOnClickListener(this);
		listView.setAdapter(null);
		jb.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.wc_details_text_jb:// 跳转到举报页面
			i.setClass(WCDetailsActivity.this, ReportActivity.class);
			startActivity(i);
			break;
		case R.id.wc_btn_xg:// 修改按钮
			if (but.getText().toString().equals("修改")) {
				dz_xg.setVisibility(View.VISIBLE);
				sw_01.setVisibility(View.VISIBLE);
				sw_02.setVisibility(View.VISIBLE);
				but.setText("确定");
			}else{
				dz_xg.setVisibility(View.GONE);
				sw_01.setVisibility(View.GONE);
				sw_02.setVisibility(View.GONE);
				but.setText("修改");
			}
			break;

		default:
			break;
		}
	}

}
