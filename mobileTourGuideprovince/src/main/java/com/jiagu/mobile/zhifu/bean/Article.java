package com.jiagu.mobile.zhifu.bean;

public class Article {
	private String exchangeid;
	private String orderid;
	private String number;
	private String isuse;
	private String createtime;
	private String usetime;
	private String price;
	private String imageurl;
	private Boolean checked = false;
	public Article(String exchangeid, String orderid, String number,
			String isuse, String createtime, String usetime, String price,
			String imageurl, Boolean checked) {
		super();
		this.exchangeid = exchangeid;
		this.orderid = orderid;
		this.number = number;
		this.isuse = isuse;
		this.createtime = createtime;
		this.usetime = usetime;
		this.price = price;
		this.imageurl = imageurl;
		this.checked = checked;
	}
	public Article() {
		super();
	}
	public String getExchangeid() {
		return exchangeid;
	}
	public void setExchangeid(String exchangeid) {
		this.exchangeid = exchangeid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return "Article [exchangeid=" + exchangeid + ", orderid=" + orderid
				+ ", number=" + number + ", isuse=" + isuse + ", createtime="
				+ createtime + ", usetime=" + usetime + ", price=" + price
				+ ", imageurl=" + imageurl + ", checked=" + checked + "]";
	}
}
