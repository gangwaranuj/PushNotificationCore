package com.saphire.iopush.bean;

import java.util.Arrays;

public class SaleBean {

	private String id;
	private String state;
	private AmountBean amount;
	private String payment_mode;
	private String protection_eligibility;
	private String protection_eligibility_type;
	private TransactionFeeBean transaction_fee;
	private String parent_payment;
	private String create_time;
	private String update_time;
	private LinksBean[] links;
	
	
	
	
	public SaleBean(String id, String state, AmountBean amount, String payment_mode, String protection_eligibility,
			String protection_eligibility_type, TransactionFeeBean transaction_fee, String parent_payment,
			String create_time, String update_time, LinksBean[] links) {
		super();
		this.id = id;
		this.state = state;
		this.amount = amount;
		this.payment_mode = payment_mode;
		this.protection_eligibility = protection_eligibility;
		this.protection_eligibility_type = protection_eligibility_type;
		this.transaction_fee = transaction_fee;
		this.parent_payment = parent_payment;
		this.create_time = create_time;
		this.update_time = update_time;
		this.links = links;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public AmountBean getAmount() {
		return amount;
	}
	public void setAmount(AmountBean amount) {
		this.amount = amount;
	}
	public String getPayment_mode() {
		return payment_mode;
	}
	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}
	public String getProtection_eligibility() {
		return protection_eligibility;
	}
	public void setProtection_eligibility(String protection_eligibility) {
		this.protection_eligibility = protection_eligibility;
	}
	public String getProtection_eligibility_type() {
		return protection_eligibility_type;
	}
	public void setProtection_eligibility_type(String protection_eligibility_type) {
		this.protection_eligibility_type = protection_eligibility_type;
	}
	public TransactionFeeBean getTransaction_fee() {
		return transaction_fee;
	}
	public void setTransaction_fee(TransactionFeeBean transaction_fee) {
		this.transaction_fee = transaction_fee;
	}
	public String getParent_payment() {
		return parent_payment;
	}
	public void setParent_payment(String parent_payment) {
		this.parent_payment = parent_payment;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public LinksBean[] getLinks() {
		return links;
	}
	public void setLinks(LinksBean[] links) {
		this.links = links;
	}
	@Override
	public String toString() {
		return "Sale [id=" + id + ", state=" + state + ", amount=" + amount + ", payment_mode=" + payment_mode
				+ ", protection_eligibility=" + protection_eligibility + ", protection_eligibility_type="
				+ protection_eligibility_type + ", transaction_fee=" + transaction_fee + ", parent_payment="
				+ parent_payment + ", create_time=" + create_time + ", update_time=" + update_time + ", links="
				+ Arrays.toString(links) + "]";
	}
	
	
	
}
