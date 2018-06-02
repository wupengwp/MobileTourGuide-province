package com.jiagu.mobile.tourguide.activities;

import java.io.IOException;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.VerificationCodeActivity;
import com.jiagu.mobile.tourguide.activities.bases.VerificationCodeActivity.OnCodeBtnClickListener;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Md5;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//注册页面
public class RegisterActivity extends VerificationCodeActivity implements
		OnClickListener, TextWatcher, OnCodeBtnClickListener {

	
	private int timeindex = 60;//倒计时1分钟
	private EditText mAccountEt, mPhoneEt, mCodeEt, mNewPassEt, mRePassEt;
	private Button mOkBtn, mCancelBtn, mGetcode;

	private TextView mField;
	private String codeId;
//	private int a = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findView();
	}

	private void findView() {
		mAccountEt = (EditText) findViewById(R.id.register_user_name_et);
		mPhoneEt = (EditText) findViewById(R.id.register_user_phone_et);
		mCodeEt = (EditText) findViewById(R.id.register_user_code_et);
		mNewPassEt = (EditText) findViewById(R.id.register_user_pass_et);
		mRePassEt = (EditText) findViewById(R.id.register_user_repass_et);
		mAccountEt.addTextChangedListener(this);
		mPhoneEt.addTextChangedListener(this);
		mCodeEt.addTextChangedListener(this);
		mNewPassEt.addTextChangedListener(this);
		mRePassEt.addTextChangedListener(this);

		mOkBtn = (Button) findViewById(R.id.register_ok_btn);
		mCancelBtn = (Button) findViewById(R.id.register_cancel_btn);
		mGetcode = (Button) findViewById(R.id.getcode_btn);
		mOkBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mGetcode.setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	@Override
	public void onClick(View v) {
		String phone = mPhoneEt.getText().toString();
		switch (v.getId()) {
		case R.id.register_ok_btn:
			String account = mAccountEt.getText().toString();
			String code = mCodeEt.getText().toString();
			String newpass = mNewPassEt.getText().toString();
			String repass = mRePassEt.getText().toString();

			if (account.equals("")) {
				Toast.makeText(RegisterActivity.this, "请输入用户名",
						Toast.LENGTH_SHORT).show();
			} else if (account.length() < 6) {
				Toast.makeText(RegisterActivity.this, "用户名不能小于6位哦",
						Toast.LENGTH_SHORT).show();
			} else if (account.length() > 15) {
				Toast.makeText(RegisterActivity.this, "用户名不能大于15位哦",
						Toast.LENGTH_SHORT).show();
			} else if (phone.equals("")) {
				Toast.makeText(RegisterActivity.this, "请输入绑定的手机号",
						Toast.LENGTH_SHORT).show();
			} else if (!(phone.length() == 11)) {
				Toast.makeText(RegisterActivity.this, "请正确输入手机号",
						Toast.LENGTH_SHORT).show();
			} else if (code.equals("")) {
				Toast.makeText(RegisterActivity.this, "请输入验证码!",
						Toast.LENGTH_SHORT).show();
			} else if (!code.equals(codeId)) {
				Toast.makeText(RegisterActivity.this, "亲,验证码不对哦!",
						Toast.LENGTH_SHORT).show();
			} else if (newpass.equals("")) {
				Toast.makeText(RegisterActivity.this, "请输入密码!",
						Toast.LENGTH_SHORT).show();
			} else if (repass.equals("")) {
				Toast.makeText(RegisterActivity.this, "请再次输入密码!",
						Toast.LENGTH_SHORT).show();
			} else if (!newpass.equals(repass)) {
				Toast.makeText(RegisterActivity.this, "密码不一致哦!",
						Toast.LENGTH_SHORT).show();
			} else {
				getRegister(account, newpass, phone, code);
			}
			break;
		case R.id.register_cancel_btn:
			onBackPressed();
			break;
		case R.id.getcode_btn:// 验证手机
			String path = Path.SERVER_ADRESS1 + "register/getCode.htm";
//			if (a == 1) {
						
				if (phone.length() == 11) {
//					a++;
					mGetcode.setClickable(false);
					mGetcode.setFocusable(false);
					initTimer();
					RequestParams params = new RequestParams(); // 绑定参数
					params.put("Mobi_number", phone);
					HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "获取失败",
									Toast.LENGTH_LONG).show();
//							a = 1;
						}

						@Override
						public void onSuccess(String arg0) {
							System.out.println("1111111--------"+arg0);
							super.onSuccess(arg0);
							try {
								JSONObject json = new JSONObject(arg0);
								org.json.JSONArray jsons = json
										.getJSONArray("records");
								String result = json.getString("result");
								if (result.equals("0")) {
									for (int j = 0; j < jsons.length(); j++) {
										Toast.makeText(getApplicationContext(),
												"获取成功", Toast.LENGTH_LONG)
												.show();
										JSONObject jsonObject = jsons
												.getJSONObject(j);
										codeId = jsonObject
												.getString("vericode");
										mPhoneEt.setFocusable(false);
									}
								} else {
									Toast.makeText(getApplicationContext(),
											result, Toast.LENGTH_LONG).show();
//									a = 1;
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} else {
					Toast.makeText(RegisterActivity.this, "请正确输入手机号",
							Toast.LENGTH_SHORT).show();
				}
//			} 
//			else {
//				Toast.makeText(RegisterActivity.this, "请耐心等待验证码",
//						Toast.LENGTH_SHORT).show();
//			}
			break;
		}
	}

	@Override
	public void onCodeBtnClick() {
		String phoneNumber = mPhoneEt.getText().toString();
		if (Utils.isNullOrEmpty(phoneNumber)) {
			focusOnError(mPhoneEt, R.string.phone_null_prompt);
			return;
		}
		if (!Utils.isMobileNO(phoneNumber)) {
			focusOnError(mPhoneEt, R.string.phone_error_prompt);
			return;
		}
		getVerificationCode(phoneNumber);
	}

	private void focusOnError(TextView field, int resourceId) {
		this.mField = field;
		field.setBackgroundResource(R.drawable.view_edittext_error_border);
		field.requestFocus();
		field.setError(getString(resourceId));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mField != null)
			mField.setBackgroundResource(R.drawable.view_edittext_normal_border);

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	/**
	 * @author 用户注册
	 * @param phoner
	 */
	private void getRegister(String username, String userpass, String phoner,
			String code) {
		showProgress();
		String path = Path.SERVER_ADRESS1 + "register/scenic_reg.htm";
		try {
			String pass = Md5.En(userpass);
			RequestParams params = new RequestParams(); // 绑定参数
			params.put("username", username);
			params.put("userpass", pass);
			params.put("Mobi_number", phoner);
			params.put("code", code);
			params.put("gender", "男");
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					hideProgress();
					Toast.makeText(getApplicationContext(), "注册失败",Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					try {
						JSONObject json = new JSONObject(arg0);
						String result = json.getString("result");
						if (result.equals("0")) {
							Toast.makeText(getApplicationContext(), "注册成功",
									Toast.LENGTH_LONG).show();
							onBackPressed();
						} else {
							showToast(result);
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						hideProgress();
					}

				}

			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 倒计时
	 */
	private void initTimer() {
		
		mGetcode.post(new Runnable() {

			@Override
			public void run() {
				if (timeindex > 0) {
					
					StringBuffer second = new StringBuffer();				
					if (timeindex < 10) {
						second.append(0);
					}
					second.append(timeindex);
					mGetcode.setText("再次发送"+second);
					timeindex--;
					mGetcode.postDelayed(this, 1000);
				}else {
					timeindex = 60;
					mGetcode.setText("获取验证码");
					mGetcode.setClickable(true);
					mGetcode.setFocusable(true);
				}
			}
		});
	}
}
