package com.saphire.iopush.bean;

import java.util.Arrays;

public class ExecuteTransactionBean {
	
	
	
	
	private AmountBean amount;
	private PayeeBean payee;
	private String description;
	private String invoice_number;
	private ItemListBean item_list;
	private RelatedResources[] related_resources;

	
	
	public ExecuteTransactionBean(AmountBean amount, PayeeBean payee, String description, String invoice_number,
			ItemListBean item_list, RelatedResources[] related_resources) {
		super();
		this.amount = amount;
		this.payee = payee;
		this.description = description;
		this.invoice_number = invoice_number;
		this.item_list = item_list;
		this.related_resources = related_resources;
	}

	public AmountBean getAmount() {
		return amount;
	}

	public void setAmount(AmountBean amount) {
		this.amount = amount;
	}

	public PayeeBean getPayee() {
		return payee;
	}

	public void setPayee(PayeeBean payee) {
		this.payee = payee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public ItemListBean getItem_list() {
		return item_list;
	}

	public void setItem_list(ItemListBean item_list) {
		this.item_list = item_list;
	}

	public RelatedResources[] getRelated_resources() {
		return related_resources;
	}

	public void setRelated_resources(RelatedResources[] related_resources) {
		this.related_resources = related_resources;
	}

	@Override
	public String toString() {
		return "ExecuteTransactionBean [amount=" + amount + ", payee=" + payee + ", description=" + description
				+ ", invoice_number=" + invoice_number + ", item_list=" + item_list + ", related_resources="
				+ Arrays.toString(related_resources) + "]";
	}
	
	
	
	
	
}
