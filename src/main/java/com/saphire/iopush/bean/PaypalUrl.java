package com.saphire.iopush.bean;

public class PaypalUrl {

	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	@Override
	public String toString() {
		return "PaypalUrl [return_url=" + return_url + ", cancel_urls=" + cancel_url + "]";
	}
	private String return_url;
	private String cancel_url;
	public String getCancel_urls() {
		return cancel_url;
	}
	public void setCancel_urls(String cancel_urls) {
		this.cancel_url = cancel_urls;
	}
}
