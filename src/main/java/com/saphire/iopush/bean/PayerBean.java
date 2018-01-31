package com.saphire.iopush.bean;

public class PayerBean {

	private String payer_id;
    private String payment_id;
    
    private String payment_method;

	public String getPaymentMethod() {
		return payment_method;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.payment_method = paymentMethod;
	}   
    String getToken() {
		return token;
	}

	void setToken(String token) {
		this.token = token;
	}

	private String token;
	
	public String getPayer_id() {
		return payer_id;
	}

	public void setPayer_id(String payer_id) {
		this.payer_id = payer_id;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
}
