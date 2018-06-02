package com.jiagu.mobile.daohang;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.activities.bases.TitleDrawerActivity;
import com.jiagu.mobile.tourguide.utils.UesrInfo;

public class RouteGuideDemo extends TitleDrawerActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//百度坐标转换为国标
		double gg_lat = 0;
		double gg_lon = 0;

		double X = Double.parseDouble(getIntent().getStringExtra("x"));
		double Y = Double.parseDouble(getIntent().getStringExtra("y"));
		double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

		double x = X - 0.0065, y = Y - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);

		gg_lon = z * Math.cos(theta);
		gg_lat = z * Math.sin(theta);

		BigDecimal bigDecimal = new BigDecimal(gg_lon);
		BigDecimal bigDecimal2 = new BigDecimal(gg_lat);

		X = bigDecimal.setScale(6, BigDecimal.ROUND_DOWN).doubleValue();
		Y = bigDecimal2.setScale(6, BigDecimal.ROUND_DOWN).doubleValue();
//		if (UesrInfo==null) {
//			
//		}
		BaiduNaviManager.getInstance().launchNavigator(this, UesrInfo.myY,
				UesrInfo.myX, UesrInfo.at, Y, X,
				getIntent().getStringExtra("name"),
				// NE_RoutePlan_Mode.ROUTE_PLAN_MOD_AVOID_TAFFICJAM //避免交通拥堵
				// NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_DIST,// 最短距离
				// NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TOLL, //路费最少
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND, // 推荐线路
				// NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener() { // 跳转监听

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(RouteGuideDemo.this,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
						finish();
					}

					@Override
					public void onJumpToDownloader() {
						finish();
					}
				});
	}

}
