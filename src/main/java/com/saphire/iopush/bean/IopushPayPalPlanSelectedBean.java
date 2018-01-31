package com.saphire.iopush.bean;

import com.saphire.iopush.model.IopushPayPalPayment;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushUserplan;

public class IopushPayPalPlanSelectedBean {

	
	
	
	private IopushUserplan iopushUserplan;
	private IopushProduct iopushProduct;
	private IopushPayPalPayment iopushPayPalPayment;
	private int subscriberLimit;
	private double planCost;
	private int id;
	
	
	public IopushPayPalPlanSelectedBean(IopushUserplan iopushUserplan, IopushProduct iopushProduct,
			IopushPayPalPayment iopushPayPalPayment, int subscriberLimit, double planCost, int id) {
		super();
		this.iopushUserplan = iopushUserplan;
		this.iopushProduct = iopushProduct;
		this.iopushPayPalPayment = iopushPayPalPayment;
		this.subscriberLimit = subscriberLimit;
		this.planCost = planCost;
		this.id = id;
	}
	public IopushUserplan getIopushUserplan() {
		return iopushUserplan;
	}
	public void setIopushUserplan(IopushUserplan iopushUserplan) {
		this.iopushUserplan = iopushUserplan;
	}
	public IopushProduct getIopushProduct() {
		return iopushProduct;
	}
	public void setIopushProduct(IopushProduct iopushProduct) {
		this.iopushProduct = iopushProduct;
	}
	public IopushPayPalPayment getIopushPayPalPayment() {
		return iopushPayPalPayment;
	}
	public void setIopushPayPalPayment(IopushPayPalPayment iopushPayPalPayment) {
		this.iopushPayPalPayment = iopushPayPalPayment;
	}
	public int getSubscriberLimit() {
		return subscriberLimit;
	}
	public void setSubscriberLimit(int subscriberLimit) {
		this.subscriberLimit = subscriberLimit;
	}
	public double getPlanCost() {
		return planCost;
	}
	public void setPlanCost(double planCost) {
		this.planCost = planCost;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "IopushPayPalPlanSelectedBean [iopushUserplan=" + iopushUserplan + ", iopushProduct=" + iopushProduct
				+ ", iopushPayPalPayment=" + iopushPayPalPayment + ", subscriberLimit=" + subscriberLimit
				+ ", planCost=" + planCost + ", id=" + id + "]";
	}


	
	
}
