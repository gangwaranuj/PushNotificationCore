package com.saphire.iopush.bean;

public class AmountBean {
	
	private String total;
	private String currency;
	private DetailsBean details;
	
	@Override
	public String toString() {
		return "AmountBean [total=" + total + ", currency=" + currency + ", details=" + details + "]";
	}
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public DetailsBean getDetails() {
		return details;
	}
	public void setDetails(DetailsBean details) {
		this.details = details;
	}
	

}
