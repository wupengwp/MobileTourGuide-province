package com.jiagu.mobile.tourguide.bean;

public class Exercise {
//	  "text": "——西安城墙·国际文化艺术中心位于南门历史文化街区（南门月城西侧）。在“尊重”（Respect）历史的责任下，在“热爱”（Reallove）生活的情怀下，在“复兴”（Recover）文化的使命下，通过开展各类文化艺术交流活动，联络国内国际遗产机构、文化机构、公益组织、文保专家、文化学者、艺术名家等社会各界人士，共同关注、参与、致力于文化遗产的保护与研究、世界文化艺术的交流与传播。该中心定于2014年11月5日落成开放：",
//      "scenic": null,
//      "imagurl4": "",
//      "imagurl3": "",
//      "imagurl2": "",
//      "timeStar": "2014-10-25",
//      "imagurl1": "app/scenic_images/S0001/planning/RAC3.jpg",
//      "id": "6",
//      "title": "西安城墙·RAC",
//      "timer": "2014-10-25",
//      "isdone": "0",
//      "timeOver": "2014-10-25",
//      "scenicid": "S0008",
//      "photoid": "sp5"
	 private String text;
	 private String scenic;
	 private String imagurl4;
	 private String imagurl3;
	 private String imagurl2;
	 private String timeStar;
	 private String imagurl1;
	 private String id;
	 private String title;
	 private String timer;
	 private String isdone;
	 private String timeOver;
	 private String scenicid;
	 private String photoid;
	public Exercise(String text, String scenic, String imagurl4,
			String imagurl3, String imagurl2, String timeStar, String imagurl1,
			String id, String title, String timer, String isdone,
			String timeOver, String scenicid, String photoid) {
		super();
		this.text = text;
		this.scenic = scenic;
		this.imagurl4 = imagurl4;
		this.imagurl3 = imagurl3;
		this.imagurl2 = imagurl2;
		this.timeStar = timeStar;
		this.imagurl1 = imagurl1;
		this.id = id;
		this.title = title;
		this.timer = timer;
		this.isdone = isdone;
		this.timeOver = timeOver;
		this.scenicid = scenicid;
		this.photoid = photoid;
	}
	public Exercise() {
		super();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getScenic() {
		return scenic;
	}
	public void setScenic(String scenic) {
		this.scenic = scenic;
	}
	public String getImagurl4() {
		return imagurl4;
	}
	public void setImagurl4(String imagurl4) {
		this.imagurl4 = imagurl4;
	}
	public String getImagurl3() {
		return imagurl3;
	}
	public void setImagurl3(String imagurl3) {
		this.imagurl3 = imagurl3;
	}
	public String getImagurl2() {
		return imagurl2;
	}
	public void setImagurl2(String imagurl2) {
		this.imagurl2 = imagurl2;
	}
	public String getTimeStar() {
		return timeStar;
	}
	public void setTimeStar(String timeStar) {
		this.timeStar = timeStar;
	}
	public String getImagurl1() {
		return imagurl1;
	}
	public void setImagurl1(String imagurl1) {
		this.imagurl1 = imagurl1;
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
	public String getIsdone() {
		return isdone;
	}
	public void setIsdone(String isdone) {
		this.isdone = isdone;
	}
	public String getTimeOver() {
		return timeOver;
	}
	public void setTimeOver(String timeOver) {
		this.timeOver = timeOver;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	@Override
	public String toString() {
		return "Exercise [text=" + text + ", scenic=" + scenic + ", imagurl4="
				+ imagurl4 + ", imagurl3=" + imagurl3 + ", imagurl2="
				+ imagurl2 + ", timeStar=" + timeStar + ", imagurl1="
				+ imagurl1 + ", id=" + id + ", title=" + title + ", timer="
				+ timer + ", isdone=" + isdone + ", timeOver=" + timeOver
				+ ", scenicid=" + scenicid + ", photoid=" + photoid + "]";
	}
	 
	
}
