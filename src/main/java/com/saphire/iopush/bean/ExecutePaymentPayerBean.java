package com.saphire.iopush.bean;

public class ExecutePaymentPayerBean {

	
	private String payment_method;
	private String status;
	private ExecutePaymentPayerInfoBean payer_info;
	
	
	
	
	public ExecutePaymentPayerBean(String payment_method, String status,
			ExecutePaymentPayerInfoBean executePaymentPayerInfoBean) {
		super();
		this.payment_method = payment_method;
		this.status = status;
		this.payer_info = executePaymentPayerInfoBean;
	}
	
	
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ExecutePaymentPayerInfoBean getExecutePaymentPayerInfoBean() {
		return payer_info;
	}
	public void setExecutePaymentPayerInfoBean(ExecutePaymentPayerInfoBean executePaymentPayerInfoBean) {
		this.payer_info = executePaymentPayerInfoBean;
	}
	@Override
	public String toString() {
		return "ExecutePaymentPayerBean [payment_method=" + payment_method + ", status=" + status
				+ ", executePaymentPayerInfoBean=" + payer_info + "]";
	}
	
	
	
	
	
}