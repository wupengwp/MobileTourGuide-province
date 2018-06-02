package com.jiagu.mobile.tourguide.application;

//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.jiagu.mobile.tourguide.utils.CrashHandler;
import com.jiagu.mobile.tourguide.utils.UesrInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;
/**
 * @Title: ScenicPhotoMapper.java
 * @Package com.jiagu.mobile.tourguide.application
 * @Description: TODO(Application)
 * @author GaoLei
 * @date 2014年12月26日 上午19:53:34
 * @version 2.0
 * @company 西安甲骨企业文化传播有限公司
 */
public class MobileTourGuideApplication extends Application {
	public static final String SEVER_PATH = "";
	// private static RequestQueue mRequestQueue;
	private static SharedPreferences m_sharedPreferences;
	private static Context context;
	private static int screenWidth, screenHeight;
	public static final String S_MSG_TAG = "msgHandler";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		
		context = getApplicationContext();
		CrashHandler crashHandler = CrashHandler.getInstance();  
		crashHandler.init(context);
	
		initImageLoader();  
		
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		this.setScreenWidth(wm.getDefaultDisplay().getWidth());// 屏幕宽度
		this.setScreenHeight(wm.getDefaultDisplay().getHeight());// 屏幕高度
	}

	// 初始化ImageLoader
	public void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(5 * 1024 * 1024).diskCacheFileCount(100)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/*
	 * public static RequestQueue getVolley(Context context) { if (mRequestQueue
	 * == null) { mRequestQueue = Volley.newRequestQueue(context); } return
	 * mRequestQueue; }
	 */
	public static SharedPreferences getUserPreferences() {
		if (m_sharedPreferences == null) {
			m_sharedPreferences = context.getSharedPreferences(
					UesrInfo.m_sharedPreferences, MODE_PRIVATE);
		}
		return m_sharedPreferences;
	}
	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		MobileTourGuideApplication.screenWidth = screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		MobileTourGuideApplication.screenHeight = screenHeight;
	}
}
