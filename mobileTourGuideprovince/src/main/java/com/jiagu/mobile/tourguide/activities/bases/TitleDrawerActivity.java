package com.jiagu.mobile.tourguide.activities.bases;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.jiagu.mobile.daohang.RouteGuideDemo;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.ExerciseActivity;
import com.jiagu.mobile.tourguide.activities.FeatureActivity;
import com.jiagu.mobile.tourguide.activities.FirstTouristActivity;
import com.jiagu.mobile.tourguide.activities.MainActivity;
import com.jiagu.mobile.tourguide.activities.SceneActivity;
import com.jiagu.mobile.tourguide.activities.SceneDetialActivity;
import com.jiagu.mobile.tourguide.activities.StrategyActivity;
import com.jiagu.mobile.tourguide.adapter.MyMenuListAdapter;
import com.jiagu.mobile.tourguide.adapter.TitlePOPAdapter;
import com.jiagu.mobile.tourguide.bean.Area;
import com.jiagu.mobile.tourguide.fragments.HomeFragment;
import com.jiagu.mobile.tourguide.utils.HttpUtil;
import com.jiagu.mobile.tourguide.utils.Path;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.jiagu.mobile.zhifu.GroupPurchaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TitleDrawerActivity extends BaseActivity implements
		DrawerListener, OnClickListener {
	
	private View mLayout;
	private ImageButton mMenu, mLocation;
	private DrawerLayout mDrawerLayout;
	private View mDrawerView;
	private ListView mDrawerList;
	private ViewGroup mContentView;

	private LinearLayout mMenuPanel;
	private RelativeLayout mHome;
	private LinearLayout mPersonal, mSettings;
	private ImageButton mBottmMenu;
	public TextView appName;
	private  ArrayList<Area> list;
	private static ArrayList<HashMap<String, Object>> data;
	// private View oldView;
	public static TitleDrawerActivity titleDrawerActivity;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = getLayoutInflater().inflate(R.layout.activity_title_drawer,null);
		titleDrawerActivity = this;
		mMenu = (ImageButton) mLayout.findViewById(R.id.action_menu_btn);
		
		appName = (TextView) mLayout.findViewById(R.id.activity_title_appname_textview);
		appName.setOnClickListener(this);
		mLocation = (ImageButton) mLayout.findViewById(R.id.action_location_btn);
		mDrawerLayout = (DrawerLayout) mLayout.findViewById(R.id.drawer_layout);
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);
		mDrawerLayout.setDrawerListener(this);
	
		mDrawerView = mLayout.findViewById(R.id.left_drawer);
		mDrawerList = (ListView) mLayout.findViewById(R.id.left_drawer_list);
		ArrayList<HashMap<String, Object>> list = getData();
		MyMenuListAdapter adapter = new MyMenuListAdapter(getApplication(),list, R.layout.item_menu_listview);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent i = new Intent();
				switch (position) {
				case 0:
					i.setClass(getApplication(), MainActivity.class);
					startActivity(i);
					break;
				case 1:
					i.setClass(getApplication(), FirstTouristActivity.class);
					startActivity(i);
					break;
				case 2:
					i.setClass(getApplication(), GroupPurchaseActivity.class);
					startActivity(i);
					break;
				case 3:
					
					if (UesrInfo.areaType.equals("2")) {
						i.setClass(getApplication(), SceneActivity.class);
					} else {
						i.putExtra("id", UesrInfo.sceneId);
						i.putExtra("a", "b");
						i.setClass(getApplication(), SceneDetialActivity.class);
					}
					
//					i.setClass(getApplication(), SceneActivity.class);
					startActivity(i);
					break;
				case 4:
					i.setClass(getApplication(), FeatureActivity.class);
					startActivity(i);
					break;
				case 5:
					i.setClass(getApplication(), ExerciseActivity.class);
					startActivity(i);
					break;
				case 6:
					i.putExtra("judge", "noMy");
					i.setClass(getApplication(), StrategyActivity.class);
					startActivity(i);
					break;
				
				}
				if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
					mDrawerLayout.closeDrawer(mDrawerView);
				}
			}
		});

		mContentView = (ViewGroup) mLayout.findViewById(R.id.content_frame);
		LayoutParams params = new LayoutParams(getWindowWidth()- (getWindowWidth() / 5), LayoutParams.MATCH_PARENT,
				Gravity.START);
		mDrawerView.setLayoutParams(params);

		mMenu.setOnClickListener(click);
		mLocation.setOnClickListener(click);
		
	}

	private ArrayList<HashMap<String, Object>> getData() {
		String[] textData = { "首	       页", "开始导游", "旅游团购", "主要景观", "特色城市",
				"近期活动", "旅游攻略" };
		int[] imagerData = { R.drawable.zhuye, R.drawable.btn_yuyin_normal,
				R.drawable.btn_home_selector_06,
				R.drawable.btn_jingguan_normal, R.drawable.btn_tese_normal,
				R.drawable.btn_huodong_normal, R.drawable.btn_gonglue_normal };
		data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < textData.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("text", textData[i]);
			hashMap.put("imager", imagerData[i]);
			data.add(hashMap);
		}
		return data;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void setContentView(int layoutResID) {
		mContentView.removeAllViews();
		View view = getLayoutInflater().inflate(layoutResID, null);
		mContentView.addView(view);
		setContentView(mLayout);
	}

	private View.OnClickListener click = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.action_menu_btn:
				if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
					mDrawerLayout.closeDrawer(mDrawerView);
				} else {
					mDrawerLayout.openDrawer(mDrawerView);
				}
				break;
			case R.id.action_location_btn:
				Intent i = new Intent();
				i.setClass(getApplication(), RouteGuideDemo.class);
				i.putExtra("x", ""+UesrInfo.x);
				i.putExtra("y", ""+UesrInfo.y);
				i.putExtra("name", UesrInfo.area);
				startActivity(i);
				break;
			case R.id.main_bottom_menu:
				if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
					mDrawerLayout.closeDrawer(mDrawerView);
				}
				if (mMenuPanel.getVisibility() == View.GONE) {
					mMenuPanel.setVisibility(View.VISIBLE);
				} else {
					mMenuPanel.setVisibility(View.GONE);
				}
				break;
			}
		}
	};
	private View.OnClickListener checke = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
				mDrawerLayout.closeDrawer(mDrawerView);
			}
			if (mHomeMenuItemClickListener != null)
				mHomeMenuItemClickListener.onHomeMenuItem(v, mMenuPanel);
		}
	};

	protected void showBottomNavigation() {
		mHome = (RelativeLayout) mLayout.findViewById(R.id.home_panel_btn);
		mPersonal = (LinearLayout) mLayout.findViewById(R.id.menu_personal_btn);
		mSettings = (LinearLayout) mLayout.findViewById(R.id.menu_setting_btn);
		mHome.setOnClickListener(checke);
		mHome.setVisibility(View.VISIBLE);
		mPersonal.setOnClickListener(checke);
		mSettings.setOnClickListener(checke);

		mMenuPanel = (LinearLayout) mLayout.findViewById(R.id.home_menu_panel);
		mBottmMenu = (ImageButton) mLayout.findViewById(R.id.main_bottom_menu);
		mBottmMenu.setOnClickListener(click);
	}

	@Override
	public void onDrawerClosed(View arg0) {
		if (mTitleDrawerListener != null)
			mTitleDrawerListener.onTitleDrawerClosed(arg0);
	}

	@Override
	public void onDrawerOpened(View arg0) {
		if (mTitleDrawerListener != null)
			mTitleDrawerListener.onTitleDrawerOpened(arg0);
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		if (mTitleDrawerListener != null)
			mTitleDrawerListener.onTitleDrawerSlide(arg0, arg1);
	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		if (mTitleDrawerListener != null)
			mTitleDrawerListener.onTitleDrawerStateChanged(arg0);
		if (mMenuPanel != null && arg0 == DrawerLayout.STATE_SETTLING)
			mMenuPanel.setVisibility(View.GONE);
	}

	private OnTitleDrawerListener mTitleDrawerListener;
	private OnHomeMenuItemClickListener mHomeMenuItemClickListener;
	private PopupWindow popupwindow;
	private ListView popListView;

	// private Intent i;

	protected void setOnTitleDrawerListener(OnTitleDrawerListener titleDrawerListener) {
		this.mTitleDrawerListener = titleDrawerListener;
	}

	public interface OnTitleDrawerListener {
		public void onTitleDrawerClosed(View arg0);

		public void onTitleDrawerOpened(View arg0);

		public void onTitleDrawerSlide(View arg0, float arg1);

		public void onTitleDrawerStateChanged(int arg0);
	}

	protected void setOnHomeMenuItemClickListener(OnHomeMenuItemClickListener homeMenuItemClickListener) {
		this.mHomeMenuItemClickListener = homeMenuItemClickListener;
	}

	public interface OnHomeMenuItemClickListener {
		public void onHomeMenuItem(View v, ViewGroup menu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_appname_textview:
			initmPopupWindowView();
			popupwindow.showAsDropDown(v, 0, 0);
			break;
		default:
			break;
		}
	}

	protected void setAppName(String title) {
		if (!title.equals("") && !title.equals(appName.getText().toString())&&!UesrInfo.abc.equals("")) {
			
			appName.setText(title + "手机导游");
		}
	}
	protected void setMap(){
		mLocation.setVisibility(View.GONE);
	}
	protected void setAppNameDrawableRight() {
		Drawable drawable = TitleDrawerActivity.this.getResources().getDrawable(R.drawable.a);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		appName.setCompoundDrawables(null, null, drawable, null);
	}

	public void initmPopupWindowView() {
		showProgress();
		// 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.pop_listview,null, false);
		WindowManager wm = (WindowManager) TitleDrawerActivity.this.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();// 获取屏幕的宽度
		// int width = appName.getWidth();
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, width,LayoutParams.MATCH_PARENT);
		// 自定义view添加触摸事件
		popupwindow.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));  
		popupwindow.setBackgroundDrawable(dw);
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		/** 在这里可以实现自定义视图的功能 */
		popListView = (ListView) customView.findViewById(R.id.pop_listview);
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UesrInfo.areaType = list.get(arg2).getAreaType();
				if (list.get(arg2).getAreaType().equals("4")) {
					UesrInfo.area = list.get(arg2).getTitle();
					UesrInfo.sceneId = list.get(arg2).getArea();
				} else {
					UesrInfo.area = list.get(arg2).getTitle();
				}
				UesrInfo.x = list.get(arg2).getX();
				UesrInfo.y = list.get(arg2).getY();
				
				UesrInfo.abc = "0.0";
				setAppName(UesrInfo.area);
				appName.setText(UesrInfo.area + "手机导游");
				popupwindow.dismiss();
//				Intent intent = new Intent();
//				intent.setAction("gaolei");
//				intent.putExtra("area", list.get(arg2).getArea());
//				intent.putExtra("id", list.get(arg2).getId());
//				intent.putExtra("titlenames", list.get(arg2).getTitle());
//				TitleDrawerActivity.this.sendBroadcast(intent);
				HomeFragment.homeFragment.getPhotoToo(list.get(arg2).getId());
			}
		});
		HttpUtil.post(Path.SERVER_ADRESS + "area/areaList.htm", null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						hideProgress();
					}

					@Override
					public void onSuccess(String response) {
						showProgress();
						// TODO Auto-generated method stub
						super.onSuccess(response);
						if (response.equals("") || response == null) {
							Toast.makeText(TitleDrawerActivity.this, "服务器在打盹",Toast.LENGTH_SHORT).show();
						} else {
							JSONObject object = JSONObject
									.parseObject(response);
							JSONArray top = object.getJSONArray("records");
							String result = object.getString("result");
							if (result.equals("0")) {
								list = (ArrayList<Area>) JSONArray.parseArray(top.toJSONString(), Area.class);
								TitlePOPAdapter adapter = new TitlePOPAdapter(TitleDrawerActivity.this, list);
								popListView.setAdapter(adapter);
							} else {

							}
						}
						hideProgress();
					}
				});
		popListView.setAdapter(null);
	}
	public boolean getIsshow(){
		if (popupwindow!=null) {
			return popupwindow.isShowing();
		}
		return false;
	}
	public void popdismiss(){
		popupwindow.dismiss();
	}


}
