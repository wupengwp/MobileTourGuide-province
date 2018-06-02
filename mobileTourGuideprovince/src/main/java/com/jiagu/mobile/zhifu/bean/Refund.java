package com.jiagu.mobile.zhifu.bean;

public class Refund {
	private String createtime;
	private String price;
	private String exchangeid;
	private String usetime;
	private String number;
	private String orderid;
	private String isuse;
	private Boolean checked = false;
	public Refund(String createtime, String price, String exchangeid,
			String usetime, String number, String orderid, String isuse,
			Boolean checked) {
		super();
		this.createtime = createtime;
		this.price = price;
		this.exchangeid = exchangeid;
		this.usetime = usetime;
		this.number = number;
		this.orderid = orderid;
		this.isuse = isuse;
		this.checked = checked;
	}
	public Refund() {
		super();
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getExchangeid() {
		return exchangeid;
	}
	public void setExchangeid(String exchangeid) {
		this.exchangeid = exchangeid;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return "Refund [createtime=" + createtime + ", price=" + price
				+ ", exchangeid=" + exchangeid + ", usetime=" + usetime
				+ ", number=" + number + ", orderid=" + orderid + ", isuse="
				+ isuse + ", checked=" + checked + "]";
	}
}