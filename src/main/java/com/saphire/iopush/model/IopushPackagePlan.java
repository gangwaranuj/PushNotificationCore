package com.saphire.iopush.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "iopush_package_plan")
public class IopushPackagePlan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="iopush_package_plan_seq", sequenceName="iopush_package_plan_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_package_plan_seq")
	@Column(name = "package_id", unique = true, nullable = false,columnDefinition = "serial")
	private int packageId;
	
	@Column(name = "subscribers_limit")
	private int subscribersLimit;
	
	@Column(name = "pricing")
	private int pricing;
	
	@Column(name = "fk_plan_id")
	private int fkPlanId;
	public IopushPackagePlan() {
		// TODO Auto-generated constructor stub
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
	 * @return the fkPlanId
	 */
	public int getFkPlanId() {
		return fkPlanId;
	}
	/**
	 * @param fkPlanId the fkPlanId to set
	 */
	public void setFkPlanId(int fkPlanId) {
		this.fkPlanId = fkPlanId;
	}
	@Override
	public String toString() {
		return "IopushPackagePlan [packageId=" + packageId + ", subscribersLimit=" + subscribersLimit + ", pricing="
				+ pricing + ", fkPlanId=" + fkPlanId + "]";
	}
	
	
	
	
}
