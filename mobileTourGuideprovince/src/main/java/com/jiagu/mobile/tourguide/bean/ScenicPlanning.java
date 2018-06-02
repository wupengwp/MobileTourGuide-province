package com.jiagu.mobile.tourguide.bean;

/**   
* @Title: ScenicPlanning.java 
* @Package com.jiagu.dataCollect.model 
* @Description: TODO(景区活动实体类) 
* @author Jing    
* @date 2014年12月26日 下午5:06:31 
* @version 2.0  
* @company 西安甲骨企业文化传播有限公司
*/
public class ScenicPlanning {
    private String id;

    private String scenicid;

    private String timer;

    private String title;

    private String text;

    private String isdone;

    private String timeStar;

    private String timeOver;

    private String photoid;

    private String imagurl1;

    private String imagurl2;

    private String imagurl3;

    private String imagurl4;
    
    private Scenic scenic;
    
    private Touristadmin touristadmin;

	public ScenicPlanning(String id, String scenicid, String timer,
			String title, String text, String isdone, String timeStar,
			String timeOver, String photoid, String imagurl1, String imagurl2,
			String imagurl3, String imagurl4, Scenic scenic,
			Touristadmin touristadmin) {
		super();
		this.id = id;
		this.scenicid = scenicid;
		this.timer = timer;
		this.title = title;
		this.text = text;
		this.isdone = isdone;
		this.timeStar = timeStar;
		this.timeOver = timeOver;
		this.photoid = photoid;
		this.imagurl1 = imagurl1;
		this.imagurl2 = imagurl2;
		this.imagurl3 = imagurl3;
		this.imagurl4 = imagurl4;
		this.scenic = scenic;
		this.touristadmin = touristadmin;
	}

	public ScenicPlanning() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScenicid() {
		return scenicid;
	}

	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIsdone() {
		return isdone;
	}

	public void setIsdone(String isdone) {
		this.isdone = isdone;
	}

	public String getTimeStar() {
		return timeStar;
	}

	public void setTimeStar(String timeStar) {
		this.timeStar = timeStar;
	}

	public String getTimeOver() {
		return timeOver;
	}

	public void setTimeOver(String timeOver) {
		this.timeOver = timeOver;
	}

	public String getPhotoid() {
		return photoid;
	}

	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}

	public String getImagurl1() {
		return imagurl1;
	}

	public void setImagurl1(String imagurl1) {
		this.imagurl1 = imagurl1;
	}

	public String getImagurl2() {
		return imagurl2;
	}

	public void setImagurl2(String imagurl2) {
		this.imagurl2 = imagurl2;
	}

	public String getImagurl3() {
		return imagurl3;
	}

	public void setImagurl3(String imagurl3) {
		this.imagurl3 = imagurl3;
	}

	public String getImagurl4() {
		return imagurl4;
	}

	public void setImagurl4(String imagurl4) {
		this.imagurl4 = imagurl4;
	}

	public Scenic getScenic() {
		return scenic;
	}

	public void setScenic(Scenic scenic) {
		this.scenic = scenic;
	}

	public Touristadmin getTouristadmin() {
		return touristadmin;
	}

	public void setTouristadmin(Touristadmin touristadmin) {
		this.touristadmin = touristadmin;
	}

	@Override
	public String toString() {
		return "ScenicPlanning [id=" + id + ", scenicid=" + scenicid
				+ ", timer=" + timer + ", title=" + title + ", text=" + text
				+ ", isdone=" + isdone + ", timeStar=" + timeStar
				+ ", timeOver=" + timeOver + ", photoid=" + photoid
				+ ", imagurl1=" + imagurl1 + ", imagurl2=" + imagurl2
				+ ", imagurl3=" + imagurl3 + ", imagurl4=" + imagurl4
				+ ", scenic=" + scenic + ", touristadmin=" + touristadmin + "]";
	}

}