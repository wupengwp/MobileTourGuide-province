package com.jiagu.mobile.tourguide.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;

//景点描述信息
@SuppressWarnings("serial")
public class ScenicSpot implements Serializable {
	    public String id;//景区主键ID
		public String scenicID;// 景区ID
		public double longitude;// 经度
		public double latitude;// 纬度
		public float level;// 显示的图层
		public String audio;// 语音文件路径
		public String video;// 视频文件路径
		public String name;//景点名称
		public float broadcastDistance;//播报距离
		public int attrctType;//景点类型1：景点， 2：区域，3：道路
		public String attracID;//景点ID
		
		//新增支持道路和区域的描绘已经播报记录 xujintao 20150302
		public Overlay ol;//道路或区域的覆盖图层
		public ArrayList<LatLng> points;//存放区域、道路的数据采集点
		public long lastBroadcastTime;//上次播报时间，若与上次播报时间间隔超过10分钟，可以重新再次播报 
	}