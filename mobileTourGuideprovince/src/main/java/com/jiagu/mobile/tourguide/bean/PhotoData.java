package com.jiagu.mobile.tourguide.bean;

public class PhotoData {
	 private String id;
	 private String title;
	 private String timer;
	 private String url;
	 private String photoid;
	public PhotoData(String id, String title, String timer, String url,
			String photoid) {
		super();
		this.id = id;
		this.title = title;
		this.timer = timer;
		this.url = url;
		this.photoid = photoid;
	}
	public PhotoData() {
		super();
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
		return "PhotoData [id=" + id + ", title=" + title + ", timer=" + timer
				+ ", url=" + url + ", photoid=" + photoid + "]";
	}
	 
}
