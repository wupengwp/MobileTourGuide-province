package com.jiagu.mobile.tourguide.activities;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Md5;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//修改密码
public class EditPassActivity extends Activity implements OnClickListener {

	private EditText mOldPassEt, mPhoneEt, mCodeEt, mNewPassEt, mRePassEt,
			mNameEt;
	private Button mOkBtn, getcode;
//	private int a = 1;
	private int count = 0;
	private int timeindex = 60;//倒计时1分钟

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editpass);
		findView();
	}

	private void findView() {
		mNameEt = (EditText) findViewById(R.id.edit_user_name_et);
		mOldPassEt = (EditText) findViewById(R.id.edit_old_pass_et);
		mPhoneEt = (EditText) findViewById(R.id.edit_buding_phone_et);
		mCodeEt = (EditText) findViewById(R.id.edit_user_code_et);
		mNewPassEt = (EditText) findViewById(R.id.edit_new_pass_et);
		mRePassEt = (EditText) findViewById(R.id.edit_new_repass_et);

		getcode = (Button) findViewById(R.id.getcode_btn);
		mOkBtn = (Button) findViewById(R.id.ok_edit_pass);
		mOkBtn.setOnClickListener(this);
		getcode.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		String oldpass, newpass, repass;
		String name = mNameEt.getText().toString();
		String phone = mPhoneEt.getText().toString();
		String code = mCodeEt.getText().toString();
		oldpass = mOldPassEt.getText().toString();
		newpass = mNewPassEt.getText().toString();
		repass = mRePassEt.getText().toString();
		switch (v.getId()) {
		case R.id.ok_edit_pass:
			if (name.equals("")) {
				Toast.makeText(EditPassActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
			} else if (oldpass.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入原密码",
						Toast.LENGTH_SHORT).show();
			} else if (phone.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入手机号",
						Toast.LENGTH_SHORT).show();
			} else if (!(phone.length() == 11)) {
				Toast.makeText(EditPassActivity.this, "请正确输入手机号",
						Toast.LENGTH_SHORT).show();
			} else if (code.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入验证码",
						Toast.LENGTH_SHORT).show();
			} else if (!code.equals(UesrInfo.code)) {
				Toast.makeText(EditPassActivity.this, "验证码不正确",
						Toast.LENGTH_SHORT).show();
			} else if (newpass.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入新密码",
						Toast.LENGTH_SHORT).show();
			} else if (repass.equals("")) {
				Toast.makeText(EditPassActivity.this, "请确认新密码",
						Toast.LENGTH_SHORT).show();
			} else if (!(newpass.equals(repass))) {
				Toast.makeText(EditPassActivity.this, "新密码不一致",
						Toast.LENGTH_SHORT).show();
			} else if (newpass.equals(oldpass)) {
				Toast.makeText(EditPassActivity.this, "新密码不能与原密码一致",
						Toast.LENGTH_SHORT).show();
			} else {
				RequestParams params = new RequestParams(); // 绑定参数
				try {
					params.put("oldpass", Md5.En(oldpass));
					params.put("newpass", Md5.En(newpass));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				params.put("Mobi_number", phone);
				params.put("code", code);
				params.put("username", name);
				params.put("bj", "1");
				String path = Path.SERVER_ADRESS1 + "register/forgetPass.htm";
				getData(path, params);
			}
			break;
		case R.id.getcode_btn:// 验证
			if (name.equals("")) {
				Toast.makeText(EditPassActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
			} else if (oldpass.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入原密码",
						Toast.LENGTH_SHORT).show();
			} else if (phone.equals("")) {
				Toast.makeText(EditPassActivity.this, "请输入手机号",
						Toast.LENGTH_SHORT).show();
			} else if (!(phone.length() == 11)) {
				Toast.makeText(EditPassActivity.this, "请正确输入手机号",
						Toast.LENGTH_SHORT).show();
			} else {
//				if (a != 1) {
//					Toast.makeText(EditPassActivity.this, "请耐心等待验证码",
//							Toast.LENGTH_SHORT).show();
//				} else {
				getcode.setClickable(false);
				getcode.setFocusable(false);
				initTimer();
					RequestParams params = new RequestParams(); // 绑定参数
//					a++;
					params.put("Mobi_number", phone);
					params.put("loginuser", name);
					String path = Path.SERVER_ADRESS1 + "register/getCode.htm";
					HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "获取失败",
									Toast.LENGTH_LONG).show();
//							a = 1;
						}

						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							try {
								JSONObject json = new JSONObject(arg0);
								org.json.JSONArray jsons = json
										.getJSONArray("records");
								String result = json.getString("result");
								if (result.equals("0")) {
									for (int j = 0; j < jsons.length(); j++) {
										JSONObject jsonObject = jsons
												.getJSONObject(j);
										UesrInfo.code = jsonObject
												.getString("vericode");
										Toast.makeText(getApplicationContext(),
												"获取成功", Toast.LENGTH_LONG)
												.show();
										mPhoneEt.setFocusable(false);
//										a++;
									}
								} else {
									Toast.makeText(getApplicationContext(),
											result, Toast.LENGTH_LONG).show();
//									a = 1;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
//				}
			}
			break;
		}
	}

	private void getData(final String path, final RequestParams params) {
		HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				count++;
				if (count < 3) {
					getData(path, params);
				} else {
					count = 0;
					Toast.makeText(getApplicationContext(), "修改失败",Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				JSONObject json;
				try {
					json = new JSONObject(arg0);
					String result = json.getString("result");
					if (result.equals("0")) {
						Toast.makeText(getApplicationContext(), "修改成功",
								Toast.LENGTH_LONG).show();
						UesrInfo.code = "";
						onBackPressed();

					} else {
						Toast.makeText(getApplicationContext(), result,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}
	/**
	 * 倒计时
	 */
	private void initTimer() {
		
		getcode.post(new Runnable() {

			@Override
			public void run() {
				if (timeindex > 0) {
					
					StringBuffer second = new StringBuffer();				
					if (timeindex < 10) {
						second.append(0);
					}
					second.append(timeindex);
					getcode.setText("再次发送"+second);
					timeindex--;
					getcode.postDelayed(this, 1000);
				}else {
					timeindex = 60;
					getcode.setText("获取验证码");
					getcode.setClickable(true);
					getcode.setFocusable(true);
				}
			}
		});
	}
}
