package com.jiagu.mobile.tourguide.bean;

public class Scene {
	 private String createTime;
	 private String phonenumber;
     private String text;
     private String regcode;
     private String regurl;
     private String scenicLevel;
     private String mapurl;
     private String scenicphoto;
     private String scenicVideo;
     private String listenCount;
     private String scenicType;
     private String id;
     private String price;
     private String area;
     private String maplineurl;
     private String steet;
     private String scenicabbr;
     private String goodTimer;
     private String accreditUrl;
     private String otherprice;
     private String scenicid;
     private String scenicPhoto;
     private String scenicname;
     private ScenicCn scenicCn;
	public Scene(String createTime, String phonenumber, String text,
			String regcode, String regurl, String scenicLevel, String mapurl,
			String scenicphoto, String scenicVideo, String listenCount,
			String scenicType, String id, String price, String area,
			String maplineurl, String steet, String scenicabbr,
			String goodTimer, String accreditUrl, String otherprice,
			String scenicid, String scenicPhoto2, String scenicname,
			ScenicCn scenicCn) {
		super();
		this.createTime = createTime;
		this.phonenumber = phonenumber;
		this.text = text;
		this.regcode = regcode;
		this.regurl = regurl;
		this.scenicLevel = scenicLevel;
		this.mapurl = mapurl;
		this.scenicphoto = scenicphoto;
		this.scenicVideo = scenicVideo;
		this.listenCount = listenCount;
		this.scenicType = scenicType;
		this.id = id;
		this.price = price;
		this.area = area;
		this.maplineurl = maplineurl;
		this.steet = steet;
		this.scenicabbr = scenicabbr;
		this.goodTimer = goodTimer;
		this.accreditUrl = accreditUrl;
		this.otherprice = otherprice;
		this.scenicid = scenicid;
		scenicPhoto = scenicPhoto2;
		this.scenicname = scenicname;
		this.scenicCn = scenicCn;
	}
	public Scene() {
		super();
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRegcode() {
		return regcode;
	}
	public void setRegcode(String regcode) {
		this.regcode = regcode;
	}
	public String getRegurl() {
		return regurl;
	}
	public void setRegurl(String regurl) {
		this.regurl = regurl;
	}
	public String getScenicLevel() {
		return scenicLevel;
	}
	public void setScenicLevel(String scenicLevel) {
		this.scenicLevel = scenicLevel;
	}
	public String getMapurl() {
		return mapurl;
	}
	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}
	public String getScenicphoto() {
		return scenicphoto;
	}
	public void setScenicphoto(String scenicphoto) {
		this.scenicphoto = scenicphoto;
	}
	public String getScenicVideo() {
		return scenicVideo;
	}
	public void setScenicVideo(String scenicVideo) {
		this.scenicVideo = scenicVideo;
	}
	public String getListenCount() {
		return listenCount;
	}
	public void setListenCount(String listenCount) {
		this.listenCount = listenCount;
	}
	public String getScenicType() {
		return scenicType;
	}
	public void setScenicType(String scenicType) {
		this.scenicType = scenicType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getMaplineurl() {
		return maplineurl;
	}
	public void setMaplineurl(String maplineurl) {
		this.maplineurl = maplineurl;
	}
	public String getSteet() {
		return steet;
	}
	public void setSteet(String steet) {
		this.steet = steet;
	}
	public String getScenicabbr() {
		return scenicabbr;
	}
	public void setScenicabbr(String scenicabbr) {
		this.scenicabbr = scenicabbr;
	}
	public String getGoodTimer() {
		return goodTimer;
	}
	public void setGoodTimer(String goodTimer) {
		this.goodTimer = goodTimer;
	}
	public String getAccreditUrl() {
		return accreditUrl;
	}
	public void setAccreditUrl(String accreditUrl) {
		this.accreditUrl = accreditUrl;
	}
	public String getOtherprice() {
		return otherprice;
	}
	public void setOtherprice(String otherprice) {
		this.otherprice = otherprice;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getScenicPhoto() {
		return scenicPhoto;
	}
	public void setScenicPhoto(String scenicPhoto) {
		this.scenicPhoto = scenicPhoto;
	}
	public String getScenicname() {
		return scenicname;
	}
	public void setScenicname(String scenicname) {
		this.scenicname = scenicname;
	}
	public ScenicCn getScenicCn() {
		return scenicCn;
	}
	public void setScenicCn(ScenicCn scenicCn) {
		this.scenicCn = scenicCn;
	}
	@Override
	public String toString() {
		return "Scene [createTime=" + createTime + ", phonenumber="
				+ phonenumber + ", text=" + text + ", regcode=" + regcode
				+ ", regurl=" + regurl + ", scenicLevel=" + scenicLevel
				+ ", mapurl=" + mapurl + ", scenicphoto=" + scenicphoto
				+ ", scenicVideo=" + scenicVideo + ", listenCount="
				+ listenCount + ", scenicType=" + scenicType + ", id=" + id
				+ ", price=" + price + ", area=" + area + ", maplineurl="
				+ maplineurl + ", steet=" + steet + ", scenicabbr="
				+ scenicabbr + ", goodTimer=" + goodTimer + ", accreditUrl="
				+ accreditUrl + ", otherprice=" + otherprice + ", scenicid="
				+ scenicid + ", scenicPhoto=" + scenicPhoto + ", scenicname="
				+ scenicname + ", scenicCn=" + scenicCn + "]";
	}
    

}
