package com.jiagu.mobile.zhifu.bean;

public class Pop {
	private String id;
	private String idkey;
	private String caption;
	private String note;
	public Pop(String id, String idkey, String caption, String note) {
		super();
		this.id = id;
		this.idkey = idkey;
		this.caption = caption;
		this.note = note;
	}
	public Pop() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdkey() {
		return idkey;
	}
	public void setIdkey(String idkey) {
		this.idkey = idkey;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "Pop [id=" + id + ", idkey=" + idkey + ", caption=" + caption
				+ ", note=" + note + "]";
	}
	 
}
