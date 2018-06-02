package com.jiagu.mobile.zhifu.bean;

public class Site {
	private String id;
	private String mobilephone;
	private String username;
	private String address;
	private String tel;
	private String postcode;
	private String tourid;
	private String isDefault;

	public Site(String id, String mobilephone, String username, String address,
			String tel, String postcode, String tourid, String isDefault) {
		super();
		this.id = id;
		this.mobilephone = mobilephone;
		this.username = username;
		this.address = address;
		this.tel = tel;
		this.postcode = postcode;
		this.tourid = tourid;
		this.isDefault = isDefault;
	}

	public Site() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTourid() {
		return tourid;
	}

	public void setTourid(String tourid) {
		this.tourid = tourid;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "Site [id=" + id + ", mobilephone=" + mobilephone
				+ ", username=" + username + ", address=" + address + ", tel="
				+ tel + ", postcode=" + postcode + ", tourid=" + tourid
				+ ", isDefault=" + isDefault + "]";
	}
}
