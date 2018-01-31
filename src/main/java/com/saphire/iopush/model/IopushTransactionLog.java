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
@Table(name="iopush_transaction_log")
public class IopushTransactionLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="iopush_transaction_log_seq", sequenceName="iopush_transaction_log_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_transaction_log_seq")
	@Column(name = "transaction_id", unique = true, nullable = false,columnDefinition = "serial")
	private int transactionId;
	
	@Column(name="fk_payment_id")
	private int fkPaymentId;
	
	@Column(name="json_response", length = 100)
	private String jsonResponse;

	/**
	 * @return the transactionId
	 */
	public int getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the fkPaymentId
	 */
	public int getFkPaymentId() {
		return fkPaymentId;
	}

	/**
	 * @param fkPaymentId the fkPaymentId to set
	 */
	public void setFkPaymentId(int fkPaymentId) {
		this.fkPaymentId = fkPaymentId;
	}

	/**
	 * @return the jsonResponse
	 */
	public String getJsonResponse() {
		return jsonResponse;
	}

	/**
	 * @param jsonResponse the jsonResponse to set
	 */
	public void setJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	

}
