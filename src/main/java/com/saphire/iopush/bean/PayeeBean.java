package com.saphire.iopush.bean;

public class PayeeBean {
	
	private String merchant_id;
	private String email;
	
	
	
	public PayeeBean(String merchant_id, String email) {
		super();
		this.merchant_id = merchant_id;
		this.email = email;
	}
	
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "PayeeBean [merchant_id=" + merchant_id + ", email=" + email + "]";
	}
	
	
	

}
