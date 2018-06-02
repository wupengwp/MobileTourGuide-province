package com.jiagu.mobile.tourguide.bean;

import java.util.ArrayList;

public class TravelStrategy {
	private String strategyid;
	private String text;
	private String garde;
	private String ndays;
	private String type;
	private String date;
	private String url;
	private ArrayList<PhotoAlbum> photoList;
	private String tourid;
	private String aapraise;
	private String id;
	private String title;
	private String timer;
	private String tourname;
	private String photoid;
	private String tourIcon;
	public TravelStrategy(String strategyid, String text, String garde,
			String ndays, String type, String date, String url,
			ArrayList<PhotoAlbum> photoList, String tourid, String aapraise,
			String id, String title, String timer, String tourname,
			String photoid, String tourIcon) {
		super();
		this.strategyid = strategyid;
		this.text = text;
		this.garde = garde;
		this.ndays = ndays;
		this.type = type;
		this.date = date;
		this.url = url;
		this.photoList = photoList;
		this.tourid = tourid;
		this.aapraise = aapraise;
		this.id = id;
		this.title = title;
		this.timer = timer;
		this.tourname = tourname;
		this.photoid = photoid;
		this.tourIcon = tourIcon;
	}
	public TravelStrategy() {
		super();
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
	public String getGarde() {
		return garde;
	}
	public void setGarde(String garde) {
		this.garde = garde;
	}
	public String getNdays() {
		return ndays;
	}
	public void setNdays(String ndays) {
		this.ndays = ndays;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArrayList<PhotoAlbum> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<PhotoAlbum> photoList) {
		this.photoList = photoList;
	}
	public String getTourid() {
		return tourid;
	}
	public void setTourid(String tourid) {
		this.tourid = tourid;
	}
	public String getAapraise() {
		return aapraise;
	}
	public void setAapraise(String aapraise) {
		this.aapraise = aapraise;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getTourIcon() {
		return tourIcon;
	}
	public void setTourIcon(String tourIcon) {
		this.tourIcon = tourIcon;
	}
	@Override
	public String toString() {
		return "TravelStrategy [strategyid=" + strategyid + ", text=" + text
				+ ", garde=" + garde + ", ndays=" + ndays + ", type=" + type
				+ ", date=" + date + ", url=" + url + ", photoList="
				+ photoList + ", tourid=" + tourid + ", aapraise=" + aapraise
				+ ", id=" + id + ", title=" + title + ", timer=" + timer
				+ ", tourname=" + tourname + ", photoid=" + photoid
				+ ", tourIcon=" + tourIcon + "]";
	}
}
