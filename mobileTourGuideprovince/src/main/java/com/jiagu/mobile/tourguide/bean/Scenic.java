package com.jiagu.mobile.tourguide.bean;
/**   
* @Title: Scenic.java 
* @Package com.jiagu.dataCollect.model 
* @Description: TODO(景区实体) 
* @author gaolei    
* @date 2014年12月19日 上午10:54:13 
* @version 2.0  
* @company 西安甲骨企业文化传播有限公司
*/
public class Scenic {
    private String id;

	private String scenicid;

	private String scenicabbr;

	private String scenicname;

	private String steet;

	private String phonenumber;

	private String scenicLevel;

	private String scenicType;

	private String text;

	private String goodTimer;

	private String price;

	private String otherprice;

	private String mapurl;

	private String maplineurl;

	private String scenicphoto;

	private String regcode;

	private String regurl;

	private String area;
	
	private String accreditUrl;
	
	private String createTime;
	
	private String listenCount;
	
	private ScenicCn scenicCn;
	
	private String areaType= "4";

	public Scenic(String id, String scenicid, String scenicabbr,
			String scenicname, String steet, String phonenumber,
			String scenicLevel, String scenicType, String text,
			String goodTimer, String price, String otherprice, String mapurl,
			String maplineurl, String scenicphoto, String regcode,
			String regurl, String area, String accreditUrl, String createTime,
			String listenCount, ScenicCn scenicCn, String areaType) {
		super();
		this.id = id;
		this.scenicid = scenicid;
		this.scenicabbr = scenicabbr;
		this.scenicname = scenicname;
		this.steet = steet;
		this.phonenumber = phonenumber;
		this.scenicLevel = scenicLevel;
		this.scenicType = scenicType;
		this.text = text;
		this.goodTimer = goodTimer;
		this.price = price;
		this.otherprice = otherprice;
		this.mapurl = mapurl;
		this.maplineurl = maplineurl;
		this.scenicphoto = scenicphoto;
		this.regcode = regcode;
		this.regurl = regurl;
		this.area = area;
		this.accreditUrl = accreditUrl;
		this.createTime = createTime;
		this.listenCount = listenCount;
		this.scenicCn = scenicCn;
		this.areaType = areaType;
	}

	public Scenic() {
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

	public String getScenicabbr() {
		return scenicabbr;
	}

	public void setScenicabbr(String scenicabbr) {
		this.scenicabbr = scenicabbr;
	}

	public String getScenicname() {
		return scenicname;
	}

	public void setScenicname(String scenicname) {
		this.scenicname = scenicname;
	}

	public String getSteet() {
		return steet;
	}

	public void setSteet(String steet) {
		this.steet = steet;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getScenicLevel() {
		return scenicLevel;
	}

	public void setScenicLevel(String scenicLevel) {
		this.scenicLevel = scenicLevel;
	}

	public String getScenicType() {
		return scenicType;
	}

	public void setScenicType(String scenicType) {
		this.scenicType = scenicType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGoodTimer() {
		return goodTimer;
	}

	public void setGoodTimer(String goodTimer) {
		this.goodTimer = goodTimer;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOtherprice() {
		return otherprice;
	}

	public void setOtherprice(String otherprice) {
		this.otherprice = otherprice;
	}

	public String getMapurl() {
		return mapurl;
	}

	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}

	public String getMaplineurl() {
		return maplineurl;
	}

	public void setMaplineurl(String maplineurl) {
		this.maplineurl = maplineurl;
	}

	public String getScenicphoto() {
		return scenicphoto;
	}

	public void setScenicphoto(String scenicphoto) {
		this.scenicphoto = scenicphoto;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAccreditUrl() {
		return accreditUrl;
	}

	public void setAccreditUrl(String accreditUrl) {
		this.accreditUrl = accreditUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getListenCount() {
		return listenCount;
	}

	public void setListenCount(String listenCount) {
		this.listenCount = listenCount;
	}

	public ScenicCn getScenicCn() {
		return scenicCn;
	}

	public void setScenicCn(ScenicCn scenicCn) {
		this.scenicCn = scenicCn;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	@Override
	public String toString() {
		return "Scenic [id=" + id + ", scenicid=" + scenicid + ", scenicabbr="
				+ scenicabbr + ", scenicname=" + scenicname + ", steet="
				+ steet + ", phonenumber=" + phonenumber + ", scenicLevel="
				+ scenicLevel + ", scenicType=" + scenicType + ", text=" + text
				+ ", goodTimer=" + goodTimer + ", price=" + price
				+ ", otherprice=" + otherprice + ", mapurl=" + mapurl
				+ ", maplineurl=" + maplineurl + ", scenicphoto=" + scenicphoto
				+ ", regcode=" + regcode + ", regurl=" + regurl + ", area="
				+ area + ", accreditUrl=" + accreditUrl + ", createTime="
				+ createTime + ", listenCount=" + listenCount + ", scenicCn="
				+ scenicCn + ", areaType=" + areaType + "]";
	}
	
}