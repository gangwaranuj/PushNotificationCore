package com.saphire.iopush.bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PaypalCreatePaymentBean {
	
	
	private String intent;
	private PayerBean payer;
	private List<TransactionBean> transactions;
	private String note_to_payer;
	private PaypalUrl redirect_urls;
	// attributes we get from paypal payment response
	private String id;
	private String state;
	private LinksBean[] links;
	
	
	public List<TransactionBean> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionBean> transactions) {
		this.transactions = transactions;
	}
	public String getPaymentId() {
		return id;
	}
	public void setPaymentId(String paymentId) {
		this.id = paymentId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return create_time;
	}
	public void setCreateTime(Date createTime) {
		this.create_time = createTime;
	}
	public LinksBean[] getLinks() {
		return links;
	}
	public void setLinks(LinksBean[] links) {
		this.links = links;
	}
	private Date create_time;
	
	
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public PayerBean getPayer() {
		return payer;
	}
	public void setPayer(PayerBean payer) {
		this.payer = payer;
	}

	public String getNote_to_payer() {
		return note_to_payer;
	}
	public void setNote_to_payer(String note_to_payer) {
		this.note_to_payer = note_to_payer;
	}
	public PaypalUrl getRedirect_urls() {
		return redirect_urls;
	}
	public void setRedirect_urls(PaypalUrl redirect_urls) {
		this.redirect_urls = redirect_urls;
	}
	@Override
	public String toString() {
		return "PaypalCreatePaymentBean [intent=" + intent + ", payer=" + payer + ", transactions=" + transactions
				+ ", note_to_payer=" + note_to_payer + ", redirect_urls=" + redirect_urls + ", paymentId=" + id
				+ ", state=" + state + ", links=" + Arrays.toString(links) + ", createTime=" + create_time + "]";
	}
	
}
