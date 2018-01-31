package com.saphire.iopush.bean;

public class TrendsBean {
	String date = "";
	int android = 0;
	int chrome = 0;
	int safari = 0;
	int opera = 0;
	int firefox = 0;
	public TrendsBean(String date, int android, int chrome, int safari, int opera, int firefox) {
		super();
		this.date = date;
		this.android = android;
		this.chrome = chrome;
		this.safari = safari;
		this.opera = opera;
		this.firefox = firefox;
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
	 * @return the android
	 */
	public int getAndroid() {
		return android;
	}
	/**
	 * @param android the android to set
	 */
	public void setAndroid(int android) {
		this.android = android;
	}
	/**
	 * @return the chrome
	 */
	public int getChrome() {
		return chrome;
	}
	/**
	 * @param chrome the chrome to set
	 */
	public void setChrome(int chrome) {
		this.chrome = chrome;
	}
	/**
	 * @return the safari
	 */
	public int getSafari() {
		return safari;
	}
	/**
	 * @param safari the safari to set
	 */
	public void setSafari(int safari) {
		this.safari = safari;
	}
	/**
	 * @return the opera
	 */
	public int getOpera() {
		return opera;
	}
	/**
	 * @param opera the opera to set
	 */
	public void setOpera(int opera) {
		this.opera = opera;
	}
	/**
	 * @return the firefox
	 */
	public int getFirefox() {
		return firefox;
	}
	/**
	 * @param firefox the firefox to set
	 */
	public void setFirefox(int firefox) {
		this.firefox = firefox;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TrendsBean [date=" + date + ", android=" + android + ", chrome=" + chrome + ", safari=" + safari
				+ ", opera=" + opera + ", firefox=" + firefox + "]";
	}
	
	
}
