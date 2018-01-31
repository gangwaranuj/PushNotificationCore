package com.saphire.iopush.bean;

public class TotalClicksBean {

	String date = "";
	int value = 0;
	public TotalClicksBean(String date, int value) {
		super();
		this.date = date;
		this.value = value;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	
	
	
}
