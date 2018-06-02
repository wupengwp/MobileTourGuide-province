package com.jiagu.mobile.tourguide.bean;

import java.util.ArrayList;

public class Strategy {
	 private String id;
	 private String strategyid;
	 private String text;
	 private String title;
	 private String timer;
	 private String tourname;
	 private String scenicid;
	 private String date;
	 private String type;
	 private ArrayList<PhotoData> photoList;
	 private String url;
	 private String photoid;
	public Strategy(String id, String strategyid, String text, String title,
			String timer, String tourname, String scenicid, String date,
			String type, ArrayList<PhotoData> photoList, String url,
			String photoid) {
		super();
		this.id = id;
		this.strategyid = strategyid;
		this.text = text;
		this.title = title;
		this.timer = timer;
		this.tourname = tourname;
		this.scenicid = scenicid;
		this.date = date;
		this.type = type;
		this.photoList = photoList;
		this.url = url;
		this.photoid = photoid;
	}
	public Strategy() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrategyid() {
		return strategyid;
	}
	public void setStrategyid(String strategyid) {
		this.strategyid = strategyid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getTourname() {
		return tourname;
	}
	public void setTourname(String tourname) {
		this.tourname = tourname;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<PhotoData> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<PhotoData> photoList) {
		this.photoList = photoList;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	@Override
	public String toString() {
		return "Strategy [id=" + id + ", strategyid=" + strategyid + ", text="
				+ text + ", title=" + title + ", timer=" + timer
				+ ", tourname=" + tourname + ", scenicid=" + scenicid
				+ ", date=" + date + ", type=" + type + ", photoList="
				+ photoList + ", url=" + url + ", photoid=" + photoid + "]";
	}
	 
}
