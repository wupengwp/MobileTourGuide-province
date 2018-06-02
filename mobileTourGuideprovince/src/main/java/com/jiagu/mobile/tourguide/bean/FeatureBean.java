package com.jiagu.mobile.tourguide.bean;

import java.util.ArrayList;

public class FeatureBean {

	private int result;
	private ArrayList<Records> records;
	public FeatureBean(int result, ArrayList<Records> records) {
		super();
		this.result = result;
		this.records = records;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public ArrayList<Records> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<Records> records) {
		this.records = records;
	}
	@Override
	public String toString() {
		return "FeatureBean [result=" + result + "]";
	}
	

}
