package com.jiagu.mobile.tourguide.bean;

public class ScenicGps {
	private String id;
	private String scope;
	private String scenicid;
	private String offset;
	private String z;
	private String y;
	private String x;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getZ() {
		return z;
	}
	public void setZ(String z) {
		this.z = z;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public ScenicGps(String id, String scope, String scenicid, String offset,
			String z, String y, String x) {
		super();
		this.id = id;
		this.scope = scope;
		this.scenicid = scenicid;
		this.offset = offset;
		this.z = z;
		this.y = y;
		this.x = x;
	}
	public ScenicGps() {
		super();
	}
	@Override
	public String toString() {
		return "ScenicGps [id=" + id + ", scope=" + scope + ", scenicid="
				+ scenicid + ", offset=" + offset + ", z=" + z + ", y=" + y
				+ ", x=" + x + "]";
	}
	
}
