package com.jiagu.mobile.zhifu.bean;

public class Collect {
	private String collectClassify;
	private String id;
	private String isDelete;
	private String title;
	private String collectid;
	private String timer;
	private String tourid;
	private String url;
	private String soldNumber;
	private String price;
	private String cargoclassify;

	public String getCargoclassify() {
		return cargoclassify;
	}

	public void setCargoclassify(String cargoclassify) {
		this.cargoclassify = cargoclassify;
	}

	public Collect(String collectClassify, String id, String isDelete,
			String title, String collectid, String timer, String tourid,
			String url, String soldNumber, String price) {
		super();
		this.collectClassify = collectClassify;
		this.id = id;
		this.isDelete = isDelete;
		this.title = title;
		this.collectid = collectid;
		this.timer = timer;
		this.tourid = tourid;
		this.url = url;
		this.soldNumber = soldNumber;
		this.price = price;
	}

	public Collect() {
		super();
	}

	public String getCollectClassify() {
		return collectClassify;
	}

	public void setCollectClassify(String collectClassify) {
		this.collectClassify = collectClassify;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCollectid() {
		return collectid;
	}

	public void setCollectid(String collectid) {
		this.collectid = collectid;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getTourid() {
		return tourid;
	}

	public void setTourid(String tourid) {
		this.tourid = tourid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(String soldNumber) {
		this.soldNumber = soldNumber;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Collect [collectClassify=" + collectClassify + ", id=" + id
				+ ", isDelete=" + isDelete + ", title=" + title
				+ ", collectid=" + collectid + ", timer=" + timer + ", tourid="
				+ tourid + ", url=" + url + ", soldNumber=" + soldNumber
				+ ", price=" + price + ", cargoclassify=" + cargoclassify + "]";
	}
}
