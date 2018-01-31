package com.saphire.iopush.bean;

public class PlanDetailsBean {

	private int planId;
	private int subscribersLimit;
	private int pricing;
	private String planName;
	private int packageId;
	
	public PlanDetailsBean() {
	}
	public PlanDetailsBean(int packageId,int planId, int subscribersLimit, int pricing, String planName) {
		this.packageId = packageId;
		this.planId = planId;
		this.subscribersLimit = subscribersLimit;
		this.pricing = pricing;
		this.planName = planName;
	}
	
	/**
	 * @return the packageId
	 */
	public int getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId the packageId to set
	 */
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return the planId
	 */
	public int getPlanId() {
		return planId;
	}
	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	/**
	 * @return the subscribersLimit
	 */
	public int getSubscribersLimit() {
		return subscribersLimit;
	}
	/**
	 * @param subscribersLimit the subscribersLimit to set
	 */
	public void setSubscribersLimit(int subscribersLimit) {
		this.subscribersLimit = subscribersLimit;
	}
	/**
	 * @return the pricing
	 */
	public int getPricing() {
		return pricing;
	}
	/**
	 * @param pricing the pricing to set
	 */
	public void setPricing(int pricing) {
		this.pricing = pricing;
	}
	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}
	/**
	 * @param planName the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	
	
}
