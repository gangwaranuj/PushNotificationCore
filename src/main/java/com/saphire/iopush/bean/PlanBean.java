package com.saphire.iopush.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.saphire.iopush.model.IopushUsercategory;

public class PlanBean {
	
	private	int planId=0;
	private String planName="";
	private int pricing;
	private int subscriberLimit;
	private boolean isRenew;
	private Set<IopushUsercategory> iopushUsercategories = new HashSet<IopushUsercategory>(0);
	private String symbol;
	private String currencyName;
	private Date duration;
	private int total = 0;
	private	int balance = 0;
	private int used = 0;
	
	
	

	public PlanBean(int planId, String planName) {
		super();
		this.planId = planId;
		this.planName = planName;
	}


	public PlanBean(int i, String planName, int total, int balance) {
		super();
		this.planId=i;
		this.planName = planName;
		this.total = total;
		this.balance = balance;
		
	}
	
	
	
	public PlanBean(String planName, int total, int balance, int used) {
		super();
		this.planName = planName;
		this.total = total;
		this.balance = balance;
		this.used = used;
	}



	public PlanBean(int planId, String planName, int pricing, int subscriberLimit, String symbol,
			String currencyName, Date duration) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.pricing = pricing;
		this.subscriberLimit = subscriberLimit;
		this.symbol = symbol;
		this.currencyName = currencyName;
		this.duration = duration;
	}





	public PlanBean() {
		// TODO Auto-generated constructor stub
	}


	public int getPricing() {
		return pricing;
	}




	public void setPricing(int pricing) {
		this.pricing = pricing;
	}




	public int getSubscriberLimit() {
		return subscriberLimit;
	}




	public void setSubscriberLimit(int subscriberLimit) {
		this.subscriberLimit = subscriberLimit;
	}




	public boolean isRenew() {
		return isRenew;
	}




	public void setRenew(boolean isRenew) {
		this.isRenew = isRenew;
	}




	public Set<IopushUsercategory> getIopushUsercategories() {
		return iopushUsercategories;
	}




	public void setIopushUsercategories(Set<IopushUsercategory> iopushUsercategories) {
		this.iopushUsercategories = iopushUsercategories;
	}








	public String getSymbol() {
		return symbol;
	}




	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}




	public String getCurrencyName() {
		return currencyName;
	}




	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}




	public Date getDuration() {
		return duration;
	}




	public void setDuration(Date duration) {
		this.duration = duration;
	}




	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	/**
	 * @param planName the planName to set
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}
	
}
