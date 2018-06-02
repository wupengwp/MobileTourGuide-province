package com.jiagu.mobile.tourguide.bean;

public class Records {

	private String id;
	private String text;
	private String title;
	private String timer;
	private String areaid;
	private String imageurl;
	private String appraise;
	private String url;
	private String name;
	private String voiceurl;
	private String photoid;
	private String listenCount;
	
	public Records() {
		super();
	}
	public Records(String id, String text, String title, String timer,
			String areaid, String imageurl, String appraise, String url,
			String name, String voiceurl, String photoid, String listenCount) {
		super();
		this.id = id;
		this.text = text;
		this.title = title;
		this.timer = timer;
		this.areaid = areaid;
		this.imageurl = imageurl;
		this.appraise = appraise;
		this.url = url;
		this.name = name;
		this.voiceurl = voiceurl;
		this.photoid = photoid;
		this.listenCount = listenCount;
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
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getAppraise() {
		return appraise;
	}
	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVoiceurl() {
		return voiceurl;
	}
	public void setVoiceurl(String voiceurl) {
		this.voiceurl = voiceurl;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getListenCount() {
		return listenCount;
	}
	public void setListenCount(String listenCount) {
		this.listenCount = listenCount;
	}
	@Override
	public String toString() {
		return "Records [id=" + id + ", text=" + text + ", title=" + title
				+ ", timer=" + timer + ", areaid=" + areaid + ", imageurl="
				+ imageurl + ", appraise=" + appraise + ", url=" + url
				+ ", name=" + name + ", voiceurl=" + voiceurl + ", photoid="
				+ photoid + ", listenCount=" + listenCount + "]";
	}
}
