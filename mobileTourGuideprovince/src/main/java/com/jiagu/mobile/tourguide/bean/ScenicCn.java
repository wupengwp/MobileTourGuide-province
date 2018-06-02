package com.jiagu.mobile.tourguide.bean;

public class ScenicCn {
    private String id;
    private String text;
    private String title;
    private String scenicid;
    private String imageurl;
    private String voiceurl;
	public ScenicCn() {
		super();
	}
	public ScenicCn(String id, String text, String title, String scenicid,
			String imageurl, String voiceurl) {
		super();
		this.id = id;
		this.text = text;
		this.title = title;
		this.scenicid = scenicid;
		this.imageurl = imageurl;
		this.voiceurl = voiceurl;
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
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
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
	@Override
	public String toString() {
		return "ScenicCn [id=" + id + ", text=" + text + ", title=" + title
				+ ", scenicid=" + scenicid + ", imageurl=" + imageurl
				+ ", voiceurl=" + voiceurl + "]";
	}
	
}
