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
@Table(name="iopush_renewal_config")
public class IopushRenewalConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="iopush_renewal_config_seq", sequenceName="iopush_renewal_config_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_renewal_config_seq")
	@Column(name = "renewal_id", unique = true, nullable = false,columnDefinition = "serial")
	private int renewalId;
	
	@Column(name="retries")
	private int retries;
	
	@Column(name="frequency_interval")
	private int frequencyInterval;
	
	@Column(name="frequency")
	private String frequency;
	
	@Column(name="mail_duration")
	private int mailDuration;
	
	public IopushRenewalConfig() {
	}
	/**
	 * @return the renewalId
	 */
	public int getRenewalId() {
		return renewalId;
	}
	/**
	 * @param renewalId the renewalId to set
	 */
	public void setRenewalId(int renewalId) {
		this.renewalId = renewalId;
	}
	/**
	 * @return the retries
	 */
	public int getRetries() {
		return retries;
	}
	/**
	 * @param retries the retries to set
	 */
	public void setRetries(int retries) {
		this.retries = retries;
	}
	/**
	 * @return the frequencyInterval
	 */
	public int getFrequencyInterval() {
		return frequencyInterval;
	}
	/**
	 * @param frequencyInterval the frequencyInterval to set
	 */
	public void setFrequencyInterval(int frequencyInterval) {
		this.frequencyInterval = frequencyInterval;
	}
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the mailDuration
	 */
	public int getMailDuration() {
		return mailDuration;
	}
	/**
	 * @param mailDuration the mailDuration to set
	 */
	public void setMailDuration(int mailDuration) {
		this.mailDuration = mailDuration;
	}
	
	
	
}
