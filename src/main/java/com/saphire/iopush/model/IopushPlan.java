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
@Table(name = "iopush_user_plan")
public class IopushPlan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="iopush_plan_user_seq", sequenceName="iopush_plan_user_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_plan_user_seq")
	@Column(name = "plan_id", unique = true, nullable = false,columnDefinition = "serial")
	private int planId;

	@Column(name="plan_name", length= 40)
	private String planName;

	@Column(name="plan_text", length = 150)
	private String planText;
	public IopushPlan() {
		// TODO Auto-generated constructor stub
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
	 * @return the planText
	 */
	public String getPlanText() {
		return planText;
	}
	/**
	 * @param planText the planText to set
	 */
	public void setPlanText(String planText) {
		this.planText = planText;
	}


}
