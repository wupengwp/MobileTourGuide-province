package com.jiagu.mobile.tourguide.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//反馈页面
public class MatterActivity extends TitleDrawerActivity implements
		OnClickListener {
	private EditText et;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matter);
		ImageButton imageTeturn = (ImageButton) findViewById(R.id.activity_matter_image_return);
		Button btn = (Button) findViewById(R.id.activity_matter_btn);
		et = (EditText) findViewById(R.id.activity_matter_edittext);
		imageTeturn.setOnClickListener(this);
		btn.setOnClickListener(this);
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
		case R.id.activity_matter_image_return:
			onBackPressed();
			break;
		case R.id.activity_matter_btn:
			String text = et.getText().toString().trim();
			if (!text.equals("") && text!= null &&!text.equals(null)) {
				RequestParams params = new RequestParams();
				params.put("tourId", UesrInfo.getTourIDid());
				params.put("tourName", UesrInfo.getUsername());
				params.put("content", text);
				String path = Path.SERVER_ADRESS
						+ "personalCenter/insertSysQuestion.htm";
				HttpUtil.post(path, params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Toast.makeText(MatterActivity.this, "反馈失败,请检查您的网络",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						Toast.makeText(MatterActivity.this, "反馈成功,谢谢您的宝贵意见",
								Toast.LENGTH_SHORT).show();
						finish();
					};
				});
			} else {
				Toast.makeText(MatterActivity.this, "请输入反馈内容",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
