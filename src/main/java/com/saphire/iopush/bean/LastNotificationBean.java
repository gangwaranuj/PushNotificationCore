package com.saphire.iopush.bean;

public class LastNotificationBean {

	String date = "";
	String url = "";
	int delivered = 0;
	int clicked = 0;
	int sent = 0;
	
	public int getSent() {
		return sent;
	}
	public void setSent(int sent) {
		this.sent = sent;
	}
	public LastNotificationBean(String date, String url, int delivered, int clicked,int sent) {
		super();
		this.date = date;
		this.url = url;
		this.delivered = delivered;
		this.clicked = clicked;
		this.sent=sent ;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the delivered
	 */
	public int getDelivered() {
		return delivered;
	}
	/**
	 * @param delivered the delivered to set
	 */
	public void setDelivered(int delivered) {
		this.delivered = delivered;
	}
	/**
	 * @return the clicked
	 */
	public int getClicked() {
		return clicked;
	}
	/**
	 * @param clicked the clicked to set
	 */
	public void setClicked(int clicked) {
		this.clicked = clicked;
	}
	
	
}
