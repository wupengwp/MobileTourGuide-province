package com.jiagu.mobile.tourguide.bean;

public class PhotoAlbum {
	private String id;
	private String text;
	private String scenicid;
	private String imgaeurl;
	private String url;
	private String title;
	public PhotoAlbum(String id, String text, String scenicid,
			String imgaerurl, String url, String title) {
		super();
		this.id = id;
		this.text = text;
		this.scenicid = scenicid;
		this.imgaeurl = imgaerurl;
		this.url = url;
		this.title = title;
	}
	public PhotoAlbum() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getImgaeurl() {
		return imgaeurl;
	}
	public void setImgaeurl(String imgaerurl) {
		this.imgaeurl = imgaerurl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "PhotoAlbum [id=" + id + ", text=" + text + ", scenicid="
				+ scenicid + ", imgaerurl=" + imgaeurl + ", url=" + url
				+ ", title=" + title + "]";
	}
	
}
