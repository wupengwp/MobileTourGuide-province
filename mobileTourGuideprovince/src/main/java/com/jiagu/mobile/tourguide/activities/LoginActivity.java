package com.jiagu.mobile.tourguide.activities;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
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
import com.jiagu.mobile.tourguide.activities.bases.BaseActivity;
import com.jiagu.mobile.tourguide.fragments.PersonalFragment;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Md5;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.tourguide.widget.CircleImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//登录界面
public class LoginActivity extends BaseActivity implements OnClickListener,
		TextWatcher {

	private EditText mAccountEt, mPasswordEt;
	private Button mLoginBtn, mLoginBtnno;
	private TextView mFotgetTvBtn, mRegisterTvBtn;
	@SuppressWarnings("unused")
	private CircleImageView mLogoCiv;

	private TextView mField;
	private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
	}

	private void findView() {
		mAccountEt = (EditText) findViewById(R.id.login_user_name_et);
		mPasswordEt = (EditText) findViewById(R.id.login_user_pass_et);
		mAccountEt.addTextChangedListener(this);
		mPasswordEt.addTextChangedListener(this);

		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtnno = (Button) findViewById(R.id.login_btnno);
		mFotgetTvBtn = (TextView) findViewById(R.id.forget_tv_btn);
		mRegisterTvBtn = (TextView) findViewById(R.id.register_tv_btn);
		mLoginBtn.setOnClickListener(this);
		mLoginBtnno.setOnClickListener(this);
		mFotgetTvBtn.setOnClickListener(this);
		mRegisterTvBtn.setOnClickListener(this);

		mLogoCiv = (CircleImageView) findViewById(R.id.login_user_logo_iv);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 登录
		case R.id.login_btn:
			String account = mAccountEt.getText().toString();
			String password = mPasswordEt.getText().toString();
			// if (validateInput(account, password))
			if (account.equals("")) {
				showToast("请输入用户名");
			} else if (password.equals("")) {
				showToast("请输入密码");
			} else {
				getLogin(account, password);
			}
			break;
		// 取消
		case R.id.login_btnno:
			onBackPressed();
			break;
		// 注册
		case R.id.register_tv_btn:
			i = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(i);
			break;
		// 忘记密码
		case R.id.forget_tv_btn:
			i = new Intent(LoginActivity.this, ForgetPassActivity.class);
			startActivity(i);
			break;
		}
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
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	
	/**
	 * @author
	 * @param username
	 * @param password
	 *            登陆
	 */
	private void getLogin(final String username, final String userpass) {
		showProgress();
		String path = Path.SERVER_ADRESS1 + "login/checkuser.htm";
		RequestParams params = new RequestParams(); // 绑定参数
		params.put("username", username);
		try {
			params.put("password", Md5.En(userpass));
			HttpUtil.post(path, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					hideProgress();
					Toast.makeText(getApplicationContext(), arg1,
							Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(String arg0) {

					super.onSuccess(arg0);
					JSONObject json;
					try {
						showProgress();
						json = new JSONObject(arg0);
						String result = json.getString("result");
						if (result.equals("0")) {// 登陆成功
							showToast("登录成功");
							JSONArray liststr = json.getJSONArray("records");
							for (int i = 0; i < liststr.length(); i++) {
								JSONObject jsonObject = liststr
										.getJSONObject(i);
								UesrInfo.tourIDid = jsonObject
										.getString("tourid");// 用户ID
								UesrInfo.username = jsonObject
										.getString("username");
								UesrInfo.userpass = jsonObject
										.getString("userpass");
								UesrInfo.userphone = jsonObject
										.getString("userphone");
								UesrInfo.userIcon = jsonObject
										.getString("touricon");
								UesrInfo.save();
								if (PersonalFragment.personalFragment.name!=null) {
									PersonalFragment.personalFragment.name.setText(UesrInfo.getUsername());
								}
								hideProgress();
								onBackPressed();
							}
						} else {
							Toast.makeText(getApplicationContext(), result,
									Toast.LENGTH_LONG).show();
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
}
