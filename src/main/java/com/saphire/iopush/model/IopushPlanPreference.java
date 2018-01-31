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
@Table(name="iopush_plan_preference")
public class IopushPlanPreference implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@SequenceGenerator(name="iopush_plan_preference_seq", sequenceName="iopush_plan_preference_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_plan_preference_seq")
	@Column(name = "preference_id", unique = true, nullable = false,columnDefinition = "serial")
	private int preferenceId;
	
	@Column(name="preference")
	private int preference;
	
	@Column(name = "fk_plan_id")
	private int fkPlanId;
	
	public IopushPlanPreference() {
	}
	/**
	 * @return the preferenceId
	 */
	public int getPreferenceId() {
		return preferenceId;
	}
	/**
	 * @param preferenceId the preferenceId to set
	 */
	public void setPreferenceId(int preferenceId) {
		this.preferenceId = preferenceId;
	}
	/**
	 * @return the preference
	 */
	public int getPreference() {
		return preference;
	}
	/**
	 * @param preference the preference to set
	 */
	public void setPreference(int preference) {
		this.preference = preference;
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
	
	
}
