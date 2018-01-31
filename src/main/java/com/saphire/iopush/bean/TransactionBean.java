package com.saphire.iopush.bean;

public class TransactionBean {
	
	private AmountBean amount;
	private String description;
	private String custom;
	private String invoice_number;
	private PaymentOptions payment_options;
	private String soft_descriptor;
	private ItemListBean item_list;
	

	public AmountBean getAmount() {
		return amount;
	}
	public void setAmount(AmountBean amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	@Override
	public String toString() {
		return "TransactionBean [amount=" + amount + ", description=" + description + ", custom=" + custom
				+ ", invoice_number=" + invoice_number + ", payment_options=" + payment_options + ", soft_descriptor="
				+ soft_descriptor + ", item_list=" + item_list + "]";
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	public String getInvoice_number() {
		return invoice_number;
	}
	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}
	public PaymentOptions getPayment_options() {
		return payment_options;
	}
	public void setPayment_options(PaymentOptions payment_options) {
		this.payment_options = payment_options;
	}
	public String getSoft_descriptor() {
		return soft_descriptor;
	}
	public void setSoft_descriptor(String soft_descriptor) {
		this.soft_descriptor = soft_descriptor;
	}
	public ItemListBean getItem_list() {
		return item_list;
	}
	public void setItem_list(ItemListBean item_list) {
		this.item_list = item_list;
	}

	
}
