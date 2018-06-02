package com.jiagu.mobile.tourguide.bean;

import java.util.ArrayList;

public class UesrIcon {
	private String result;
	private ArrayList<String> records;
	public UesrIcon(String result, ArrayList<String> records) {
		super();
		this.result = result;
		this.records = records;
	}
	public UesrIcon() {
		super();
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<String> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<String> records) {
		this.records = records;
	}
	@Override
	public String toString() {
		return "UesrIcon [result=" + result + ", records=" + records + "]";
	}
}
