package com.jiagu.mobile.zhifu.bean;

public class GroupPurchase {
	 private String cargoclassify;
	 private String oldPrice;
	 private String phonenumber;
	 private String text;
	 private String issue;
	 private String isdelete;
	 private String soldNumber;
	 private String cargoid;
	 private String id;
	 private String cashNotice;
	 private String classify;
	 private String title;
	 private String adress;
	 private String price;
	 private String timeStart;
	 private String refundNow;
	 private String timer;
	 private String soldSum;
	 private String priceDescription;
	 private String timeOver;
	 private String refundOver;
	 private String scenicid;
	 private String imageurl;
	public String getCargoclassify() {
		return cargoclassify;
	}

	public void setCargoclassify(String cargoclassify) {
		this.cargoclassify = cargoclassify;
	}

	public GroupPurchase(String oldPrice, String phonenumber, String text,
			String issue, String isdelete, String soldNumber, String cargoid,
			String id, String cashNotice, String classify, String title,
			String adress, String price, String timeStart, String refundNow,
			String timer, String soldSum, String priceDescription,
			String timeOver, String refundOver, String scenicid, String imageurl,String cargoclassify) {
		super();
		this.cargoclassify = cargoclassify;
		this.oldPrice = oldPrice;
		this.phonenumber = phonenumber;
		this.text = text;
		this.issue = issue;
		this.isdelete = isdelete;
		this.soldNumber = soldNumber;
		this.cargoid = cargoid;
		this.id = id;
		this.cashNotice = cashNotice;
		this.classify = classify;
		this.title = title;
		this.adress = adress;
		this.price = price;
		this.timeStart = timeStart;
		this.refundNow = refundNow;
		this.timer = timer;
		this.soldSum = soldSum;
		this.priceDescription = priceDescription;
		this.timeOver = timeOver;
		this.refundOver = refundOver;
		this.scenicid = scenicid;
		this.imageurl = imageurl;
	}
	
	public GroupPurchase() {
		super();
	}

	public String getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
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
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	public String getSoldNumber() {
		return soldNumber;
	}
	public void setSoldNumber(String soldNumber) {
		this.soldNumber = soldNumber;
	}
	public String getCargoid() {
		return cargoid;
	}
	public void setCargoid(String cargoid) {
		this.cargoid = cargoid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCashNotice() {
		return cashNotice;
	}
	public void setCashNotice(String cashNotice) {
		this.cashNotice = cashNotice;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getRefundNow() {
		return refundNow;
	}
	public void setRefundNow(String refundNow) {
		this.refundNow = refundNow;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getSoldSum() {
		return soldSum;
	}
	public void setSoldSum(String soldSum) {
		this.soldSum = soldSum;
	}
	public String getPriceDescription() {
		return priceDescription;
	}
	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}
	public String getTimeOver() {
		return timeOver;
	}
	public void setTimeOver(String timeOver) {
		this.timeOver = timeOver;
	}
	public String getRefundOver() {
		return refundOver;
	}
	public void setRefundOver(String refundOver) {
		this.refundOver = refundOver;
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
	@Override
	public String toString() {
		return "GroupPurchase [cargoclassify=" + cargoclassify + ", oldPrice="
				+ oldPrice + ", phonenumber=" + phonenumber + ", text=" + text
				+ ", issue=" + issue + ", isdelete=" + isdelete
				+ ", soldNumber=" + soldNumber + ", cargoid=" + cargoid
				+ ", id=" + id + ", cashNotice=" + cashNotice + ", classify="
				+ classify + ", title=" + title + ", adress=" + adress
				+ ", price=" + price + ", timeStart=" + timeStart
				+ ", refundNow=" + refundNow + ", timer=" + timer
				+ ", soldSum=" + soldSum + ", priceDescription="
				+ priceDescription + ", timeOver=" + timeOver + ", refundOver="
				+ refundOver + ", scenicid=" + scenicid + ", imageurl="
				+ imageurl + "]";
	}
	
}
