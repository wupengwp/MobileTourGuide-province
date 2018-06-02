package com.jiagu.mobile.zhifu.bean;

public class Indent {
	private String orderid;
	private String imageurl;
	private String title;
	private String classify;
	private String isEnable;
	private String status;
	private String sold_number;
	private String price;
	public Indent(String orderid, String imageurl, String title,
			String classify, String isEnable, String status,
			String sold_number, String price) {
		super();
		this.orderid = orderid;
		this.imageurl = imageurl;
		this.title = title;
		this.classify = classify;
		this.isEnable = isEnable;
		this.status = status;
		this.sold_number = sold_number;
		this.price = price;
	}
	public Indent() {
		super();
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSold_number() {
		return sold_number;
	}
	public void setSold_number(String sold_number) {
		this.sold_number = sold_number;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Indent [orderid=" + orderid + ", imageurl=" + imageurl
				+ ", title=" + title + ", classify=" + classify + ", isEnable="
				+ isEnable + ", status=" + status + ", sold_number="
				+ sold_number + ", price=" + price + "]";
	}
}
