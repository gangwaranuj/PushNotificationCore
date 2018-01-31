package com.saphire.iopush.bean;

public class UserPlanBean {

	private int planId;
    private String planName;
    private int pricing;
    private int subscriberLimit;
	public UserPlanBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserPlanBean(int planId, String planName, int pricing, int subscriberLimit) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.pricing = pricing;
		this.subscriberLimit = subscriberLimit;
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
	 * @return the subscriberLimit
	 */
	public int getSubscriberLimit() {
		return subscriberLimit;
	}
	/**
	 * @param subscriberLimit the subscriberLimit to set
	 */
	public void setSubscriberLimit(int subscriberLimit) {
		this.subscriberLimit = subscriberLimit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserPlanBean [planId=" + planId + ", planName=" + planName + ", pricing=" + pricing
				+ ", subscriberLimit=" + subscriberLimit + "]";
	}
    
    
}
