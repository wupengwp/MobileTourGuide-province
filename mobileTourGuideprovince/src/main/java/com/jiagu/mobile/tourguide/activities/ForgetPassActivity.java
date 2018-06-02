package com.jiagu.mobile.tourguide.activities;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Md5;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//忘记密码
public class ForgetPassActivity extends Activity implements OnClickListener {
	private EditText phoneEditText, codeEditText, passEditText, repassEditText,nameEditText;
	private Button codeButton, okButton, cancelButton;
//	private int a = 1;
	private int timeindex = 60;//倒计时1分钟
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpass);
		findView();
	}

	private void findView() {
		phoneEditText = (EditText) findViewById(R.id.forgetpass_user_phone_et);
		codeEditText = (EditText) findViewById(R.id.forgetpass_user_code_et);
		passEditText = (EditText) findViewById(R.id.forgetpass_user_pass_et);
		repassEditText = (EditText) findViewById(R.id.forgetpass_user_repass_et);
		nameEditText = (EditText) findViewById(R.id.forgetpass_user_name_et);
		nameEditText.setVisibility(View.GONE);
		codeButton = (Button) findViewById(R.id.forgetpass_btn);
		okButton = (Button) findViewById(R.id.forgetpass_ok_btn);
		cancelButton = (Button) findViewById(R.id.forgetpass_cancel_btn);
		codeButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	@Override
	public void onClick(View v) {
		String phone = phoneEditText.getText().toString();
		String code = codeEditText.getText().toString();
//		String name = nameEditText.getText().toString();
		String name = "f-o-r-g-e-t";
		String pass,repass;
		try {
			pass = Md5.En(passEditText.getText().toString());
			repass = Md5.En(repassEditText.getText().toString());
			switch (v.getId()) {
			case R.id.forgetpass_btn://验证码
				if (phone.equals("")) {
					Toast.makeText(ForgetPassActivity.this, "请输入绑定的手机号",
							Toast.LENGTH_SHORT).show();
				} else if (!(phone.length() == 11)) {
					Toast.makeText(ForgetPassActivity.this, "请正确输入绑定的手机号",
							Toast.LENGTH_SHORT).show();
				} else {
//					if(a==1){
					codeButton.setClickable(false);
					codeButton.setFocusable(false);
					initTimer();
						RequestParams params = new RequestParams(); // 绑定参数
//						a++;
						params.put("Mobi_number", phone);
						params.put("loginuser", name);
						String path = Path.SERVER_ADRESS1+"register/getCode.htm";
						HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Toast.makeText(getApplicationContext(), "获取失败",
										Toast.LENGTH_LONG).show();
//								a=1;
							}
							@Override
							public void onSuccess(String arg0) {
								super.onSuccess(arg0);
								try {
									JSONObject json = new JSONObject(arg0);
									org.json.JSONArray jsons = json
											.getJSONArray("records");
									String result = json.getString("result");
									if(result.equals("0")){
										for (int j = 0; j < jsons.length(); j++) {
											JSONObject jsonObject = jsons.getJSONObject(j);
											UesrInfo.code = jsonObject
													.getString("vericode");
											Toast.makeText(getApplicationContext(), "获取成功",
													Toast.LENGTH_LONG).show();
											phoneEditText.setFocusable(false);
										}
									}else{
										Toast.makeText(getApplicationContext(), result,
												Toast.LENGTH_LONG).show();
//										a=1;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						});
//					}else{
//						Toast.makeText(getApplicationContext(), "请耐心等待验证码",
//								Toast.LENGTH_LONG).show();
//					}
				}
				break;
			case R.id.forgetpass_ok_btn://确定
				if (phone.equals("")) {
					Toast.makeText(ForgetPassActivity.this, "请输入绑定的手机号",
							Toast.LENGTH_SHORT).show();
				} else if (!(phone.length() == 11)) {
					Toast.makeText(ForgetPassActivity.this, "请正确输入绑定的手机号",
							Toast.LENGTH_SHORT).show();
				} else if (code.equals("")) {
					Toast.makeText(ForgetPassActivity.this, "请输入验证码",
							Toast.LENGTH_SHORT).show();
				} else if (!code.equals(UesrInfo.code)) {
					Toast.makeText(ForgetPassActivity.this, "请正确输入验证码",
							Toast.LENGTH_SHORT).show();
				} else if (pass.equals("")) {
					Toast.makeText(ForgetPassActivity.this, "请输入新密码",
							Toast.LENGTH_SHORT).show();
				} else if (repass.equals("")) {
					Toast.makeText(ForgetPassActivity.this, "请再次输入密码",
							Toast.LENGTH_SHORT).show();
				} else if (!repass.equals(pass)) {
					Toast.makeText(ForgetPassActivity.this, "密码不一致",
							Toast.LENGTH_SHORT).show();
				} else {
					RequestParams params = new RequestParams(); // 绑定参数
					params.put("Mobi_number", phone);
					params.put("code", code);
					params.put("newpass", pass);
					params.put("username", name);
					params.put("bj", "0");
					String path = Path.SERVER_ADRESS1+"register/forgetPass.htm";
					HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "修改失败",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							JSONObject json;
							try {
								json = new JSONObject(arg0);
								String result = json.getString("result");
								if(result.equals("0")){
									Toast.makeText(getApplicationContext(), "修改成功",
											Toast.LENGTH_LONG).show();
									UesrInfo.code = "";
//									onBackPressed();
									Intent i = new Intent().setClass(ForgetPassActivity.this, LoginActivity.class);
									startActivity(i);
									ForgetPassActivity.this.finish();
								}else{
									Toast.makeText(getApplicationContext(), result,
											Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
				}
				break;
			case R.id.forgetpass_cancel_btn://取消
				onBackPressed();
				break;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 倒计时
	 */
	private void initTimer() {
		
		codeButton.post(new Runnable() {

			@Override
			public void run() {
				if (timeindex > 0) {
					
					StringBuffer second = new StringBuffer();				
					if (timeindex < 10) {
						second.append(0);
					}
					second.append(timeindex);
					codeButton.setText("再次发送"+second);
					timeindex--;
					codeButton.postDelayed(this, 1000);
				}else {
					timeindex = 60;
					codeButton.setText("获取验证码");
					codeButton.setClickable(true);
					codeButton.setFocusable(true);
				}
			}
		});
	}
}
