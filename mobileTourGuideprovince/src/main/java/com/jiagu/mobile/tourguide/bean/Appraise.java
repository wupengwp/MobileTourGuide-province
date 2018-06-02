package com.jiagu.mobile.tourguide.bean;

public class Appraise {
	 private String site;
	 private String appraiseid;
	 private String apprClassify;
	 private String tourid;
	 private String myname;
	 private String classifyCaption;
	 private String id;
	 private String title;
	 private String timer;
	 private String grade;
	 private String favour;
	 private String dislike;
	 private String note;
	 private String appraise;
	 private String tourIcon;
	public Appraise(String site, String appraiseid, String apprClassify,
			String tourid, String myname, String classifyCaption, String id,
			String title, String timer, String grade, String favour,
			String dislike, String note, String appraise, String tourIcon) {
		super();
		this.site = site;
		this.appraiseid = appraiseid;
		this.apprClassify = apprClassify;
		this.tourid = tourid;
		this.myname = myname;
		this.classifyCaption = classifyCaption;
		this.id = id;
		this.title = title;
		this.timer = timer;
		this.grade = grade;
		this.favour = favour;
		this.dislike = dislike;
		this.note = note;
		this.appraise = appraise;
		this.tourIcon = tourIcon;
	}
	public Appraise() {
		super();
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getAppraiseid() {
		return appraiseid;
	}
	public void setAppraiseid(String appraiseid) {
		this.appraiseid = appraiseid;
	}
	public String getApprClassify() {
		return apprClassify;
	}
	public void setApprClassify(String apprClassify) {
		this.apprClassify = apprClassify;
	}
	public String getTourid() {
		return tourid;
	}
	public void setTourid(String tourid) {
		this.tourid = tourid;
	}
	public String getMyname() {
		return myname;
	}
	public void setMyname(String myname) {
		this.myname = myname;
	}
	public String getClassifyCaption() {
		return classifyCaption;
	}
	public void setClassifyCaption(String classifyCaption) {
		this.classifyCaption = classifyCaption;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getFavour() {
		return favour;
	}
	public void setFavour(String favour) {
		this.favour = favour;
	}
	public String getDislike() {
		return dislike;
	}
	public void setDislike(String dislike) {
		this.dislike = dislike;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAppraise() {
		return appraise;
	}
	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}
	public String getTourIcon() {
		return tourIcon;
	}
	public void setTourIcon(String tourIcon) {
		this.tourIcon = tourIcon;
	}
	@Override
	public String toString() {
		return "Appraise [site=" + site + ", appraiseid=" + appraiseid
				+ ", apprClassify=" + apprClassify + ", tourid=" + tourid
				+ ", myname=" + myname + ", classifyCaption=" + classifyCaption
				+ ", id=" + id + ", title=" + title + ", timer=" + timer
				+ ", grade=" + grade + ", favour=" + favour + ", dislike="
				+ dislike + ", note=" + note + ", appraise=" + appraise
				+ ", tourIcon=" + tourIcon + "]";
	}
	
}
