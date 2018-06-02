package com.jiagu.mobile.tourguide.bean;

public class HomePagerBean {
	private String id;
	private String title;
	private String timer;
	private String indexImage;
	private String url;
	private String photoid;
	public HomePagerBean(String id, String title, String timer,
			String indexImage, String url, String photoid) {
		super();
		this.id = id;
		this.title = title;
		this.timer = timer;
		this.indexImage = indexImage;
		this.url = url;
		this.photoid = photoid;
	}
	public HomePagerBean() {
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
	public String getIndexImage() {
		return indexImage;
	}
	public void setIndexImage(String indexImage) {
		this.indexImage = indexImage;
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
		return "HomePagerBean [id=" + id + ", title=" + title + ", timer="
				+ timer + ", indexImage=" + indexImage + ", url=" + url
				+ ", photoid=" + photoid + "]";
	}
}	
