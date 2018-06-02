package com.jiagu.mobile.tourguide.activities.bases;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BaseActivity extends FragmentActivity {
	private static int windowWidth = -1;
	private static int windowHeight = -1;
	public View mProgressView;
	public static BaseActivity baseActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseActivity= this;
		if (windowWidth == -1) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			windowWidth = dm.widthPixels;
			windowHeight = dm.heightPixels;
		}
	}

	@Override
	public void onContentChanged() {
		mProgressView = findViewById(android.R.id.progress);
		super.onContentChanged();
	}

	protected int getWindowWidth() {
		if (windowWidth == 0 || windowWidth == -1) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			windowWidth = dm.widthPixels;
		}
		return windowWidth;
	}

	protected int getWindowHeight() {
		if (windowHeight == 0 || windowHeight == -1) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			windowHeight = dm.heightPixels;
		}
		return windowHeight;
	}

	public void showProgress() {
		if (mProgressView != null)
			mProgressView.setVisibility(View.VISIBLE);
	}

	public void hideProgress() {
		if (mProgressView != null)
			mProgressView.setVisibility(View.GONE);
	}

	protected void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	protected void onPost(String url, RequestParams params,
			AsyncHttpResponseHandler res) {
		if (HttpUtils.isNetworkConnected(this)) {
			HttpUtils.post(url, params, res);
		} else {
			hideProgress();
			showToast(R.string.network_no_error_prompt);
		}
	}
}
