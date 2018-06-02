package com.jiagu.mobile.tourguide.bean;

public class Area {
	private String title;
	private String area;
	private String areaType;
	private String id;
	private String x;
	private String y;
	public Area(String title, String area, String areaType, String id,
			String x, String y) {
		super();
		this.title = title;
		this.area = area;
		this.areaType = areaType;
		this.id = id;
		this.x = x;
		this.y = y;
	}
	public Area() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAreaType() {
		return areaType;
	}
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "Area [title=" + title + ", area=" + area + ", areaType="
				+ areaType + ", id=" + id + ", x=" + x + ", y=" + y + "]";
	}
	
}