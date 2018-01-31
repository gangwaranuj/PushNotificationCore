package com.saphire.iopush.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Iopush_renewal_customer")
public class IopushRenewalCustomer implements Serializable{

	public String getAgreementID() {
		return agreementID;
	}

	public void setAgreementID(String agreementID) {
		this.agreementID = agreementID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="Iopush_renewal_customer_seq", sequenceName="Iopush_renewal_customer_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="Iopush_renewal_customer_seq")
	@Column(name = "customer_id", unique = true, nullable = false,columnDefinition = "serial")
	private int Customer;
	
	@Temporal(TemporalType.DATE)
	@Column(name= "next_renew_date", length=29)
	private Date nextRenewDate;
	
	@Column(name="fk_product_id")
	private int fkProductId;
	
	@Column(name="total_amount")
	private int totalAmount;
	
	@Column(name="outstanding_balance")
	private double OutstandingBalance;

	@Column(name="fk_agreement_id")
	private String agreementID;
	
	@Column(name="fk_payment_id")
	private int paymentID;
	
	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getOutstandingBalance() {
		return OutstandingBalance;
	}

	public void setOutstandingBalance(double d) {
		OutstandingBalance = d;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getCustomer() {
		return Customer;
	}

	public void setCustomer(int customer) {
		Customer = customer;
	}

	public Date getNextRenewDate() {
		return nextRenewDate;
	}

	public void setNextRenewDate(Date nextRenewDate) {
		this.nextRenewDate = nextRenewDate;
	}

	public int getFkProductId() {
		return fkProductId;
	}

	public void setFkProductId(int fkProductId) {
		this.fkProductId = fkProductId;
	}

	public Date getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name= "last_payment_date", length=29)
	private Date lastPaymentDate;
	
	@Column(name= "renew_status", length=29)
	private boolean renewStatus;

	public boolean getRenewStatus() {
		return renewStatus;
	}

	public void setRenewStatus(boolean b) {
		this.renewStatus = b;
	}
	
	
}
