package com.jiagu.mobile.tourguide.bean;

public class ScenicSpots {
	private String id;
	private String text;
	private String title;
	private String attracGps;
	private String attracid;
	private String imageurl;
	private String voiceurl;
	private String intro;
	private String attractdate;
	private String attracttype;
	private String listenCount;
	public ScenicSpots(String id, String text, String title, String attracGps,
			String attracid, String imageurl, String voiceurl, String intro,
			String attractdate, String attracttype, String listenCount) {
		super();
		this.id = id;
		this.text = text;
		this.title = title;
		this.attracGps = attracGps;
		this.attracid = attracid;
		this.imageurl = imageurl;
		this.voiceurl = voiceurl;
		this.intro = intro;
		this.attractdate = attractdate;
		this.attracttype = attracttype;
		this.listenCount = listenCount;
	}
	public ScenicSpots() {
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAttracGps() {
		return attracGps;
	}
	public void setAttracGps(String attracGps) {
		this.attracGps = attracGps;
	}
	public String getAttracid() {
		return attracid;
	}
	public void setAttracid(String attracid) {
		this.attracid = attracid;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getVoiceurl() {
		return voiceurl;
	}
	public void setVoiceurl(String voiceurl) {
		this.voiceurl = voiceurl;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getAttractdate() {
		return attractdate;
	}
	public void setAttractdate(String attractdate) {
		this.attractdate = attractdate;
	}
	public String getAttracttype() {
		return attracttype;
	}
	public void setAttracttype(String attracttype) {
		this.attracttype = attracttype;
	}
	public String getListenCount() {
		return listenCount;
	}
	public void setListenCount(String listenCount) {
		this.listenCount = listenCount;
	}
	@Override
	public String toString() {
		return "ScenicSpots [id=" + id + ", text=" + text + ", title=" + title
				+ ", attracGps=" + attracGps + ", attracid=" + attracid
				+ ", imageurl=" + imageurl + ", voiceurl=" + voiceurl
				+ ", intro=" + intro + ", attractdate=" + attractdate
				+ ", attracttype=" + attracttype + ", listenCount="
				+ listenCount + "]";
	}
}
