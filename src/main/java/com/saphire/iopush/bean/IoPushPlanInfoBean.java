package com.saphire.iopush.bean;

public class IoPushPlanInfoBean {

	private int subscriberLimit;
	private double amount;
	private String paymentId;
	private String planName;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public int getSubscriberLimit() {
		return subscriberLimit;
	}
	public void setSubscriberLimit(int subscriberLimit) {
		this.subscriberLimit = subscriberLimit;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "IoPushPlanInfoBean [subscriberLimit=" + subscriberLimit + ", amount=" + amount + "]";
	}
	
	
}
