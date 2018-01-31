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
@Table(name = "iopush_payment")
public class IopushPayment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="iopush_payment_seq", sequenceName="iopush_payment_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_payment_seq")
	@Column(name = "payment_id", unique = true, nullable = false,columnDefinition = "serial")
	private int paymentId;

	@Column(name="agreement_status")
	private String status;

	@Column(name="amount")
	private Double amount;

	@Column(name="tp_acknowledgement")
	private int tpAcknowledgement;

	@Column(name="payment_method", length= 15)
	private String paymentMethod;

	@Column(name="payment_type", length= 20)
	private String paymentType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="next_payment_date", length=29)
	private Date nextPaymentdate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="payment_date", length=29)
	private Date Paymentdate;

	@Column(name="failed_payment_count")
	private int failedPaymentCount;

	@Column(name="agreement_id")
	private String agreementId;

	@Column(name="tp_token", length= 100)
	private String tpToken;

	/* @Column(name="cycle_completed")
 private int cycleCompleted;*/

	@Column(name="renew_amount")
	private int RenewAmount;

	@Column(name="outstanding_balance")
	private double outstandingBalance;


	public double getOutstandingBalance() {
		return outstandingBalance;
	}
	public void setOutstandingBalance(double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", length=29)
	private Date creationDate;

	@Column(name="created_by", length = 20)
	private String createdBy;

	@Column(name="fk_plan_id")
	private int fkPlanId;

	@Column(name="fk_package_id")
	private int fkPackageId;

	@Column(name="fk_product_id")
	private int fkProductId;

	// newly added columns
	@Column(name="modified_by")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modified_on")
	private Date modifiedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="cancellation_Date")
	private Date cancellationDate;





	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Date getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	public IopushPayment() {
	}




	/**
	 * @return the paymentId
	 */
	public int getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the status
	 */
	public String getAgreementStatus() {
		return status;
	}
	/**
	 * @param string the status to set
	 */
	public void setAgreementStatus(String string) {
		this.status = string;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the tpAcknowledgement
	 */
	public int getTpAcknowledgement() {
		return tpAcknowledgement;
	}
	/**
	 * @param tpAcknowledgement the tpAcknowledgement to set
	 */
	public void setTpAcknowledgement(int tpAcknowledgement) {
		this.tpAcknowledgement = tpAcknowledgement;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the nextPaymentdate
	 */
	public Date getNextPaymentdate() {
		return nextPaymentdate;
	}
	/**
	 * @param nextPaymentdate the nextPaymentdate to set
	 */
	public void setNextPaymentdate(Date nextPaymentdate) {
		this.nextPaymentdate = nextPaymentdate;
	}
	/**
	 * @return the paymentdate
	 */
	public Date getPaymentdate() {
		return Paymentdate;
	}
	/**
	 * @param paymentdate the paymentdate to set
	 */
	public void setPaymentdate(Date paymentdate) {
		Paymentdate = paymentdate;
	}
	/**
	 * @return the tpPaymentId
	 */
	public int getfailedPaymentCount() {
		return failedPaymentCount;
	}
	/**
	 * @param tpPaymentId the tpPaymentId to set
	 */
	public void setfailedPaymentCount(int failedPaymentCount) {
		this.failedPaymentCount = failedPaymentCount;
	}
	/**
	 * @return the agreementId
	 */
	public String getAgreementId() {
		return agreementId;
	}
	/**
	 * @param agreementId the agreementId to set
	 */
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	/**
	 * @return the tpToken
	 */
	public String getTpToken() {
		return tpToken;
	}
	/**
	 * @param tpToken the tpToken to set
	 */
	public void setTpToken(String tpToken) {
		this.tpToken = tpToken;
	}
	/**
	 * @return the cycleCompleted
  //
 public int getCycleCompleted() {
  return cycleCompleted;
 }
 //*
	 * @param cycleCompleted the cycleCompleted to set
  //
 public void setCycleCompleted(int cycleCompleted) {
  this.cycleCompleted = cycleCompleted;
 }*/
	/**
	 * @return the renewAmount
	 */
	public int getRenewAmount() {
		return RenewAmount;
	}
	/**
	 * @param renewAmount the renewAmount to set
	 */
	public void setRenewAmount(int renewAmount) {
		RenewAmount = renewAmount;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	/**
	 * @return the fkPackageId
	 */
	public int getFkPackageId() {
		return fkPackageId;
	}
	/**
	 * @param fkPackageId the fkPackageId to set
	 */
	public void setFkPackageId(int fkPackageId) {
		this.fkPackageId = fkPackageId;
	}
	/**
	 * @return the fkProdId
	 */
	public int getFkProductId() {
		return fkProductId;
	}
	/**
	 * @param fkProdId the fkProdId to set
	 */
	public void setFkProductId(int fkProductId) {
		this.fkProductId = fkProductId;
	}

	@Column(name="tp_execute_URL", length= 100)
	private String tpExecuteURL;
	public String getTpExecuteURL() {
		return tpExecuteURL;
	}
	public void setTpExecuteURL(String tpExecuteURL) {
		this.tpExecuteURL = tpExecuteURL;
	}

	@Column(name="renew_Payment_status")
	private boolean renewStatus;


	public boolean isRenewStatus() {
		return renewStatus;
	}
	public void setRenewalPaymentStatus(boolean renewStatus) {
		this.renewStatus = renewStatus;
	}
	public boolean getRenewalPaymentStatus() {
		return renewStatus;
	}
	@Override
	public String toString() {
		return "IopushPayment [paymentId=" + paymentId + ", " + (status != null ? "status=" + status + ", " : "")
				+ "amount=" + amount + ", tpAcknowledgement=" + tpAcknowledgement + ", "
				+ (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "")
				+ (paymentType != null ? "paymentType=" + paymentType + ", " : "")
				+ (nextPaymentdate != null ? "nextPaymentdate=" + nextPaymentdate + ", " : "")
				+ (Paymentdate != null ? "Paymentdate=" + Paymentdate + ", " : "") + "failedPaymentCount="
				+ failedPaymentCount + ", " + (agreementId != null ? "agreementId=" + agreementId + ", " : "")
				+ (tpToken != null ? "tpToken=" + tpToken + ", " : "") + "RenewAmount=" + RenewAmount
				+ ", outstandingBalance=" + outstandingBalance + ", "
				+ (creationDate != null ? "creationDate=" + creationDate + ", " : "")
				+ (createdBy != null ? "createdBy=" + createdBy + ", " : "") + "fkPlanId=" + fkPlanId + ", fkPackageId="
				+ fkPackageId + ", fkProductId=" + fkProductId + ", "
				+ (tpExecuteURL != null ? "tpExecuteURL=" + tpExecuteURL + ", " : "") + "renewStatus=" + renewStatus + "]";
	}



}