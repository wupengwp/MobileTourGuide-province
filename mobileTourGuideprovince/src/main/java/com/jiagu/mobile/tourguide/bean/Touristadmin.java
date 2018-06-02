package com.jiagu.mobile.tourguide.bean;

public class Touristadmin {
    private String id;

    private String touristadminid;

    private String areaid;

    private String title;

    private String adress;

    private String phone;

    private String city;

    private String site;

    private String complainPhone;

    private String regcode;

    private String regurl;

    private String accrediturl;

    private String url1;

    private String url2;

    private String url3;

    private String jpgeurl1;

    private String jpgeurl2;

    private String jpgeurl3;
    
    
    public Touristadmin() {
		super();
	}

	public Touristadmin(String id, String touristadminid, String areaid,
			String title, String adress, String phone, String city,
			String site, String complainPhone, String regcode, String regurl,
			String accrediturl, String url1, String url2, String url3,
			String jpgeurl1, String jpgeurl2, String jpgeurl3) {
		super();
		this.id = id;
		this.touristadminid = touristadminid;
		this.areaid = areaid;
		this.title = title;
		this.adress = adress;
		this.phone = phone;
		this.city = city;
		this.site = site;
		this.complainPhone = complainPhone;
		this.regcode = regcode;
		this.regurl = regurl;
		this.accrediturl = accrediturl;
		this.url1 = url1;
		this.url2 = url2;
		this.url3 = url3;
		this.jpgeurl1 = jpgeurl1;
		this.jpgeurl2 = jpgeurl2;
		this.jpgeurl3 = jpgeurl3;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTouristadminid() {
        return touristadminid;
    }

    public void setTouristadminid(String touristadminid) {
        this.touristadminid = touristadminid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getComplainPhone() {
        return complainPhone;
    }

    public void setComplainPhone(String complainPhone) {
        this.complainPhone = complainPhone;
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

    public String getAccrediturl() {
        return accrediturl;
    }

    public void setAccrediturl(String accrediturl) {
        this.accrediturl = accrediturl;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getJpgeurl1() {
        return jpgeurl1;
    }

    public void setJpgeurl1(String jpgeurl1) {
        this.jpgeurl1 = jpgeurl1;
    }

    public String getJpgeurl2() {
        return jpgeurl2;
    }

    public void setJpgeurl2(String jpgeurl2) {
        this.jpgeurl2 = jpgeurl2;
    }

    public String getJpgeurl3() {
        return jpgeurl3;
    }

    public void setJpgeurl3(String jpgeurl3) {
        this.jpgeurl3 = jpgeurl3;
    }

	@Override
	public String toString() {
		return "Touristadmin [id=" + id + ", touristadminid=" + touristadminid
				+ ", areaid=" + areaid + ", title=" + title + ", adress="
				+ adress + ", phone=" + phone + ", city=" + city + ", site="
				+ site + ", complainPhone=" + complainPhone + ", regcode="
				+ regcode + ", regurl=" + regurl + ", accrediturl="
				+ accrediturl + ", url1=" + url1 + ", url2=" + url2 + ", url3="
				+ url3 + ", jpgeurl1=" + jpgeurl1 + ", jpgeurl2=" + jpgeurl2
				+ ", jpgeurl3=" + jpgeurl3 + "]";
	}
    
}