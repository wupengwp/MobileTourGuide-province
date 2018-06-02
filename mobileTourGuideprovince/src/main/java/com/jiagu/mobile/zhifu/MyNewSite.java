package com.jiagu.mobile.zhifu;

import com.alibaba.fastjson.JSONObject;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MyNewSite extends TitleDrawerActivity implements OnClickListener {
	private ImageView image;
	private EditText name, phone, site, postcode;
	private String nametoString, phonetoString, sitetoString, postcodetoString;
	private int a;
	private String id;
	private String postcodes,IsDefault;
	private CheckBox rbtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsite);
		image = (ImageView) findViewById(R.id.activity_newsite_iv_01);
		name = (EditText) findViewById(R.id.et_01);// 收货人
		phone = (EditText) findViewById(R.id.et_02);// 收货电话
		site = (EditText) findViewById(R.id.et_03);// 收货地址
		postcode = (EditText) findViewById(R.id.et_04);// 邮编
		Button yes = (Button) findViewById(R.id.activity_newsite_bottom_queren);
		rbtn = (CheckBox) findViewById(R.id.activity_newsite_radioButton);
		Intent i = getIntent();
		String nametext = i.getStringExtra("name");
		if (!nametext.equals("")) {
			String dianhua = i.getStringExtra("dianhua");
			String dizhi = i.getStringExtra("dizhi");
			id = i.getStringExtra("id");
			postcodes = i.getStringExtra("postcode");
			IsDefault = i.getStringExtra("IsDefault");
			name.setText(nametext);
			phone.setText(dianhua);
			site.setText(dizhi);
			if (IsDefault.equals("0")) {
				rbtn.setChecked(false);
			}else{
				rbtn.setChecked(true);
			}
			postcode.setText(postcodes);
		}else{
			IsDefault = ""+0;
		}
		image.setOnClickListener(this);
		yes.setOnClickListener(this);
		rbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					a=1;
				}else{
					a=0;
				}
			}
		});
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
		// TODO Auto-generated method stub
		nametoString = name.getText().toString();
		phonetoString = phone.getText().toString();
		sitetoString = site.getText().toString();
		postcodetoString = postcode.getText().toString();
		switch (v.getId()) {
		case R.id.activity_newsite_iv_01:
			onBackPressed();
			break;
		case R.id.activity_newsite_bottom_queren:
			if (IsDefault.equals("1")) {
				AlertDialog.Builder builder = new Builder(MyNewSite.this);
				builder.setMessage("您确定要修改默认收货地址吗？");
				builder.setTitle("默认地址修改");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setIs();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}else {
				setIs();
			}
			break;
		}
	}
	private void setIs(){
		if (nametoString.equals("") || nametoString == null) {
			Toast.makeText(MyNewSite.this, "请填写收货人", Toast.LENGTH_SHORT)
					.show();
		} else if (phonetoString.equals("") || phonetoString == null) {
			Toast.makeText(MyNewSite.this, "请填写收货人联系方式", Toast.LENGTH_SHORT)
					.show();
		} else if (sitetoString.equals("") || sitetoString == null) {
			Toast.makeText(MyNewSite.this, "请填写收货地址", Toast.LENGTH_SHORT)
					.show();
		} else if (postcodetoString.equals("") || postcodetoString == null) {
			Toast.makeText(MyNewSite.this, "请填写邮编", Toast.LENGTH_SHORT)
					.show();
		} else {
			RequestParams params = new RequestParams();
			params.put("tourId", "" + UesrInfo.getTourIDid());
			params.put("address",sitetoString);
			params.put("mobilePhone", phonetoString);
			params.put("postCode", postcodetoString);
			params.put("username", nametoString);
			if (rbtn.isChecked()) {
				a=1;
			}else{
				a=0;
			}
			params.put("isDefault", ""+a);
			params.put("id", ""+id);
			String path = Path.SERVER_ADRESS + "personalCenter/insertOrUpdateUserAdress.htm";
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				private Intent i;

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					Toast.makeText(getApplicationContext(), "请检查您的网络",Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(String response) {
					super.onSuccess(response);
					if (response.equals("") || response == null) {
						Toast.makeText(MyNewSite.this, "服务器在打盹!!!!",Toast.LENGTH_SHORT).show();
					} else {
						JSONObject object = JSONObject.parseObject(response);
						String result = object.getString("result");
						if (result.equals("0")) {
							Toast.makeText(MyNewSite.this, "保存成功",Toast.LENGTH_SHORT).show();
							i = new Intent();
							i.setClass(MyNewSite.this,MySite.class);
							i.putExtra("scenicid","");
							startActivity(i);
							finish();
						} else if(result.equals("33")){
							Toast.makeText(MyNewSite.this, "必须有一个默认地址",Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(MyNewSite.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		}
	}
}
