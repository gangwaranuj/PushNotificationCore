package com.saphire.iopush.bean;

import java.util.Arrays;
import java.util.List;

public class ExecutePaymentResponseBean {

	
	
	private String id;
	private String intent;
	private String state;
	private String cart;
	private ExecutePaymentPayerBean payer;
	private List<ExecuteTransactionBean> transactions;
	private String create_time;
	private LinksBean[] links;
	
	
	
	public ExecutePaymentResponseBean(String id, String intent, String state, String cart,
			ExecutePaymentPayerBean payer, List<ExecuteTransactionBean> transactions, String create_time,
			LinksBean[] links) {
		super();
		this.id = id;
		this.intent = intent;
		this.state = state;
		this.cart = cart;
		this.payer = payer;
		this.transactions = transactions;
		this.create_time = create_time;
		this.links = links;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCart() {
		return cart;
	}
	public void setCart(String cart) {
		this.cart = cart;
	}
	public ExecutePaymentPayerBean getPayer() {
		return payer;
	}
	public void setPayer(ExecutePaymentPayerBean payer) {
		this.payer = payer;
	}
	public List<ExecuteTransactionBean> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<ExecuteTransactionBean> transactions) {
		this.transactions = transactions;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public LinksBean[] getLinks() {
		return links;
	}
	public void setLinks(LinksBean[] links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "ExecutePaymentResponseBean [id=" + id + ", intent=" + intent + ", state=" + state + ", cart=" + cart
				+ ", payer=" + payer + ", transactions=" + transactions + ", create_time=" + create_time + ", links="
				+ Arrays.toString(links) + "]";
	}
	
	
	
	
	
	
}
