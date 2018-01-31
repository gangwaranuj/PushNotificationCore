package com.saphire.iopush.bean;

public class TransactionFeeBean {

	
	private String value;
	private String currency;
	
	
	
	
	public TransactionFeeBean(String value, String currency) {
		super();
		this.value = value;
		this.currency = currency;
	}
	
	@Override
	public String toString() {
		return "TransactionFeeBean [value=" + value + ", currency=" + currency + "]";
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
}
