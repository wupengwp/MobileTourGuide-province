package com.jiagu.mobile.zhifu.bean;

public class Indents {
	private String paytype;
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	private String consumetime;
	public String getConsumetime() {
		return consumetime;
	}
	public void setConsumetime(String consumetime) {
		this.consumetime = consumetime;
	}
	private String phonenumber;
	private String name;
	private String childAmount;
	private String idcard;
	private String childPrice;
	private String tradeNo;
	private String status;
	private String cargoclassify;
	private String consignee;
	private String shipper;
	private String paymentUser;
	private String paydate;
	private String cargoRefund;
	private String isenable;
	private String tourid;
	private String cargoid;
	private String ems;
	private String id;
	private String amount;
	private String cargoPayment;
	private String mobilenumber;
	private String timer;
	private String cargoPrice;
	private String busiid;
	private String orderid;
	private Purchase admission;
	public Indents(String phonenumber, String name, String childAmount,
			String idcard, String childPrice, String tradeNo, String status,
			String cargoclassify, String consignee, String shipper,
			String paymentUser, String paydate, String cargoRefund,
			String isenable, String tourid, String cargoid, String ems,
			String id, String amount, String cargoPayment, String mobilenumber,
			String timer, String cargoPrice, String busiid, String orderid,
			Purchase admission, Purchase shop,String paytype,String consumetime ) {
		super();
		this.phonenumber = phonenumber;
		this.name = name;
		this.childAmount = childAmount;
		this.idcard = idcard;
		this.childPrice = childPrice;
		this.tradeNo = tradeNo;
		this.status = status;
		this.cargoclassify = cargoclassify;
		this.consignee = consignee;
		this.shipper = shipper;
		this.paymentUser = paymentUser;
		this.paydate = paydate;
		this.cargoRefund = cargoRefund;
		this.isenable = isenable;
		this.tourid = tourid;
		this.cargoid = cargoid;
		this.ems = ems;
		this.id = id;
		this.amount = amount;
		this.cargoPayment = cargoPayment;
		this.mobilenumber = mobilenumber;
		this.timer = timer;
		this.cargoPrice = cargoPrice;
		this.busiid = busiid;
		this.orderid = orderid;
		this.admission = admission;
		this.paytype = paytype;
	}
	public Indents() {
		super();
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChildAmount() {
		return childAmount;
	}
	public void setChildAmount(String childAmount) {
		this.childAmount = childAmount;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getChildPrice() {
		return childPrice;
	}
	public void setChildPrice(String childPrice) {
		this.childPrice = childPrice;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCargoclassify() {
		return cargoclassify;
	}
	public void setCargoclassify(String cargoclassify) {
		this.cargoclassify = cargoclassify;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getPaymentUser() {
		return paymentUser;
	}
	public void setPaymentUser(String paymentUser) {
		this.paymentUser = paymentUser;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public String getCargoRefund() {
		return cargoRefund;
	}
	public void setCargoRefund(String cargoRefund) {
		this.cargoRefund = cargoRefund;
	}
	public String getIsenable() {
		return isenable;
	}
	public void setIsenable(String isenable) {
		this.isenable = isenable;
	}
	public String getTourid() {
		return tourid;
	}
	public void setTourid(String tourid) {
		this.tourid = tourid;
	}
	public String getCargoid() {
		return cargoid;
	}
	public void setCargoid(String cargoid) {
		this.cargoid = cargoid;
	}
	public String getEms() {
		return ems;
	}
	public void setEms(String ems) {
		this.ems = ems;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCargoPayment() {
		return cargoPayment;
	}
	public void setCargoPayment(String cargoPayment) {
		this.cargoPayment = cargoPayment;
	}
	public String getMobilenumber() {
		return mobilenumber;
	}
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getCargoPrice() {
		return cargoPrice;
	}
	public void setCargoPrice(String cargoPrice) {
		this.cargoPrice = cargoPrice;
	}
	public String getBusiid() {
		return busiid;
	}
	public void setBusiid(String busiid) {
		this.busiid = busiid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public Purchase getAdmission() {
		return admission;
	}
	public void setAdmission(Purchase admission) {
		this.admission = admission;
	}
	
	@Override
	public String toString() {
		return "Indents [paytype=" + paytype + ", consumetime=" + consumetime
				+ ", phonenumber=" + phonenumber + ", name=" + name
				+ ", childAmount=" + childAmount + ", idcard=" + idcard
				+ ", childPrice=" + childPrice + ", tradeNo=" + tradeNo
				+ ", status=" + status + ", cargoclassify=" + cargoclassify
				+ ", consignee=" + consignee + ", shipper=" + shipper
				+ ", paymentUser=" + paymentUser + ", paydate=" + paydate
				+ ", cargoRefund=" + cargoRefund + ", isenable=" + isenable
				+ ", tourid=" + tourid + ", cargoid=" + cargoid + ", ems="
				+ ems + ", id=" + id + ", amount=" + amount + ", cargoPayment="
				+ cargoPayment + ", mobilenumber=" + mobilenumber + ", timer="
				+ timer + ", cargoPrice=" + cargoPrice + ", busiid=" + busiid
				+ ", orderid=" + orderid + ", admission=" + admission + "]";
	}
	
}
