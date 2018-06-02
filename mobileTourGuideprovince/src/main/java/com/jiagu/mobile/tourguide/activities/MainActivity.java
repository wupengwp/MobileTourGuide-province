package com.jiagu.mobile.tourguide.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity.OnHomeMenuItemClickListener;
import com.jiagu.mobile.tourguide.fragments.HomeFragment;
import com.jiagu.mobile.tourguide.utils.UesrInfo;

public class MainActivity extends TitleDrawerActivity implements
		OnHomeMenuItemClickListener {

	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private HomeFragment mHomeFragment;
	private Fragment mPersonalFragment, mSettingFragment;

	private int oldId;

	IntentFilter mFilter = null;// 系统监听消息过滤
	public static MainActivity mainActivity;
	// 导航初始化
	@SuppressWarnings("unused")
	private boolean mIsEngineInitSuccess = false;
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			// 导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		mainActivity = this;	
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, "pzVZIHjYbQ01tVecQG8r316v", null);
		setContentView(R.layout.activity_main);
		
		
		showBottomNavigation();
		mFragmentManager = getSupportFragmentManager();
		setAppNameDrawableRight();
		findView();
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
//		setAppName(UesrInfo.area);
		appName.setText(UesrInfo.area + "手机导游");
	}

	private void findView() {
		mHomeFragment = (HomeFragment) mFragmentManager
				.findFragmentById(R.id.fragement_home);
		mPersonalFragment = mFragmentManager
				.findFragmentById(R.id.fragement_personal_center);
		mSettingFragment = mFragmentManager
				.findFragmentById(R.id.fragement_settings_center);
		mFragmentTransaction = mFragmentManager.beginTransaction()
				.hide(mHomeFragment).hide(mPersonalFragment)
				.hide(mSettingFragment);
		mFragmentTransaction.show(mHomeFragment).commit();
		oldId = R.id.home_panel_btn;

		super.setOnTitleDrawerListener(mHomeFragment);
		super.setOnHomeMenuItemClickListener(this);
	}

	// fragment替换
	@Override
	public void onHomeMenuItem(View v, ViewGroup menu) {
		if (oldId == v.getId()) {
			return;
		}
		mFragmentTransaction = mFragmentManager.beginTransaction()
				.hide(mHomeFragment).hide(mPersonalFragment)
				.hide(mSettingFragment);
		switch (v.getId()) {
		case R.id.home_panel_btn: // 首页
			mFragmentTransaction.show(mHomeFragment).commit();
			break;
		case R.id.menu_personal_btn: // 个人中心
			String tourIDid = UesrInfo.getTourIDid();
			if (tourIDid == null || tourIDid.equals("")) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, LoginActivity.class);
				startActivity(i);
			} else {
				mFragmentTransaction.show(mPersonalFragment).commit();
			}
			break;
		case R.id.menu_setting_btn: // 设置
			mFragmentTransaction.show(mSettingFragment).commit();
			break;
		}
		menu.setVisibility(View.GONE);
		oldId = v.getId();
	}

	@Override
	protected void onResume() {
		super.onResume();
		oldId = -1;
	}

	// 重写返回键方法
	boolean tag = true;
	@Override
	public void onBackPressed() {
		if (getIsshow()) {
			popdismiss();
		}else if (tag) {
				tag = false;
				Toast.makeText(MainActivity.this, "再按一次您就要离开了", Toast.LENGTH_SHORT).show();
				new Thread() {
					public void run() {
						try {
							Thread.sleep(3000);
							tag = true;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();
			} else {
				// finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myUid());
			}
		}
}
