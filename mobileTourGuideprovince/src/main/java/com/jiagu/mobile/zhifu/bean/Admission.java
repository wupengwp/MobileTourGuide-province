package com.jiagu.mobile.zhifu.bean;

import java.util.ArrayList;
import com.jiagu.mobile.tourguide.bean.PhotoData;

public class Admission {

	private String zwyoldprice;
	public String getZwyoldprice() {
		return zwyoldprice;
	}

	public void setZwyoldprice(String zwyoldprice) {
		this.zwyoldprice = zwyoldprice;
	}

	private String id;

	private String scenicid;

	private String cargoid;

	private String timer;

	private String title;

	private String text;

	private String imageurl;

	private String classify;

	private String price;

	private String phonenumber;

	private String adress;

	private String isdelete;

	private String oldPrice;

	private String soldSum;

	private String soldNumber;

	private String priceDescription;

	private String timeStart;

	private String timeOver;

	private String refundNow;

	private String refundOver;

	private String cashNotice;

	private String issue;

	private String scenicText;

	private String state;

	private String cargoclassify;

	private String isems;

	private String nowDate;

	private String exchangeaddress;

	private String isCollect;

	private String x;

	private String y;

	private String distance;

	private String childPrice;

	private String childgroupprice;

	private String refundLimit;

	private String consumertime;

	private String refundtime;

	private String cargosetid;

	private ArrayList<Admission> admissionList;

	private String admissionCount;

	private PhotoData photoAll;

	public Admission(String id, String scenicid, String cargoid, String timer,
			String title, String text, String imageurl, String classify,
			String price, String phonenumber, String adress, String isdelete,
			String oldPrice, String soldSum, String soldNumber,
			String priceDescription, String timeStart, String timeOver,
			String refundNow, String refundOver, String cashNotice,
			String issue, String scenicText, String state,
			String cargoclassify, String isems, String nowDate,
			String exchangeaddress, String isCollect, String x, String y,
			String distance, String childPrice, String childgroupprice,
			String refundLimit, String consumertime, String refundtime,
			String cargosetid, ArrayList<Admission> admissionList,
			String admissionCount, PhotoData photoAll,String zwyoldprice) {
		super();
		this.id = id;
		this.scenicid = scenicid;
		this.cargoid = cargoid;
		this.timer = timer;
		this.title = title;
		this.text = text;
		this.imageurl = imageurl;
		this.classify = classify;
		this.price = price;
		this.phonenumber = phonenumber;
		this.adress = adress;
		this.isdelete = isdelete;
		this.oldPrice = oldPrice;
		this.soldSum = soldSum;
		this.soldNumber = soldNumber;
		this.priceDescription = priceDescription;
		this.timeStart = timeStart;
		this.timeOver = timeOver;
		this.refundNow = refundNow;
		this.refundOver = refundOver;
		this.cashNotice = cashNotice;
		this.issue = issue;
		this.scenicText = scenicText;
		this.state = state;
		this.cargoclassify = cargoclassify;
		this.isems = isems;
		this.nowDate = nowDate;
		this.exchangeaddress = exchangeaddress;
		this.isCollect = isCollect;
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.childPrice = childPrice;
		this.childgroupprice = childgroupprice;
		this.refundLimit = refundLimit;
		this.consumertime = consumertime;
		this.refundtime = refundtime;
		this.cargosetid = cargosetid;
		this.admissionList = admissionList;
		this.admissionCount = admissionCount;
		this.photoAll = photoAll;
	}

	public Admission() {
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

	public String getCargoid() {
		return cargoid;
	}

	public void setCargoid(String cargoid) {
		this.cargoid = cargoid;
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

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getSoldSum() {
		return soldSum;
	}

	public void setSoldSum(String soldSum) {
		this.soldSum = soldSum;
	}

	public String getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(String soldNumber) {
		this.soldNumber = soldNumber;
	}

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeOver() {
		return timeOver;
	}

	public void setTimeOver(String timeOver) {
		this.timeOver = timeOver;
	}

	public String getRefundNow() {
		return refundNow;
	}

	public void setRefundNow(String refundNow) {
		this.refundNow = refundNow;
	}

	public String getRefundOver() {
		return refundOver;
	}

	public void setRefundOver(String refundOver) {
		this.refundOver = refundOver;
	}

	public String getCashNotice() {
		return cashNotice;
	}

	public void setCashNotice(String cashNotice) {
		this.cashNotice = cashNotice;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getScenicText() {
		return scenicText;
	}

	public void setScenicText(String scenicText) {
		this.scenicText = scenicText;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCargoclassify() {
		return cargoclassify;
	}

	public void setCargoclassify(String cargoclassify) {
		this.cargoclassify = cargoclassify;
	}

	public String getIsems() {
		return isems;
	}

	public void setIsems(String isems) {
		this.isems = isems;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getExchangeaddress() {
		return exchangeaddress;
	}

	public void setExchangeaddress(String exchangeaddress) {
		this.exchangeaddress = exchangeaddress;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getChildPrice() {
		return childPrice;
	}

	public void setChildPrice(String childPrice) {
		this.childPrice = childPrice;
	}

	public String getChildgroupprice() {
		return childgroupprice;
	}

	public void setChildgroupprice(String childgroupprice) {
		this.childgroupprice = childgroupprice;
	}

	public String getRefundLimit() {
		return refundLimit;
	}

	public void setRefundLimit(String refundLimit) {
		this.refundLimit = refundLimit;
	}

	public String getConsumertime() {
		return consumertime;
	}

	public void setConsumertime(String consumertime) {
		this.consumertime = consumertime;
	}

	public String getRefundtime() {
		return refundtime;
	}

	public void setRefundtime(String refundtime) {
		this.refundtime = refundtime;
	}

	public String getCargosetid() {
		return cargosetid;
	}

	public void setCargosetid(String cargosetid) {
		this.cargosetid = cargosetid;
	}

	public ArrayList<Admission> getAdmissionList() {
		return admissionList;
	}

	public void setAdmissionList(ArrayList<Admission> admissionList) {
		this.admissionList = admissionList;
	}

	public String getAdmissionCount() {
		return admissionCount;
	}

	public void setAdmissionCount(String admissionCount) {
		this.admissionCount = admissionCount;
	}

	public PhotoData getPhotoAll() {
		return photoAll;
	}

	public void setPhotoAll(PhotoData photoAll) {
		this.photoAll = photoAll;
	}

	@Override
	public String toString() {
		return "Admission [zwyoldprice=" + zwyoldprice + ", id=" + id
				+ ", scenicid=" + scenicid + ", cargoid=" + cargoid
				+ ", timer=" + timer + ", title=" + title + ", text=" + text
				+ ", imageurl=" + imageurl + ", classify=" + classify
				+ ", price=" + price + ", phonenumber=" + phonenumber
				+ ", adress=" + adress + ", isdelete=" + isdelete
				+ ", oldPrice=" + oldPrice + ", soldSum=" + soldSum
				+ ", soldNumber=" + soldNumber + ", priceDescription="
				+ priceDescription + ", timeStart=" + timeStart + ", timeOver="
				+ timeOver + ", refundNow=" + refundNow + ", refundOver="
				+ refundOver + ", cashNotice=" + cashNotice + ", issue="
				+ issue + ", scenicText=" + scenicText + ", state=" + state
				+ ", cargoclassify=" + cargoclassify + ", isems=" + isems
				+ ", nowDate=" + nowDate + ", exchangeaddress="
				+ exchangeaddress + ", isCollect=" + isCollect + ", x=" + x
				+ ", y=" + y + ", distance=" + distance + ", childPrice="
				+ childPrice + ", childgroupprice=" + childgroupprice
				+ ", refundLimit=" + refundLimit + ", consumertime="
				+ consumertime + ", refundtime=" + refundtime + ", cargosetid="
				+ cargosetid + ", admissionList=" + admissionList
				+ ", admissionCount=" + admissionCount + ", photoAll="
				+ photoAll + "]";
	}

}