package com.saphire.iopush.bean;

public class PaypalCreateAgreementRequestBean {

	private String name;
	private String description;
	private String start_date;
	private PaypalCreatePlanBean plan;
	public Payer payer;
	private PaypalLinksBean[] links;
	
	
	
	
	
	public PaypalCreatePlanBean getPlan() {
		return plan;
	}






	public void setPlan(PaypalCreatePlanBean plan) {
		this.plan = plan;
	}






	public PaypalLinksBean[] getLinks() {
		return links;
	}






	public void setLinks(PaypalLinksBean[] links) {
		this.links = links;
	}










	public PaypalCreateAgreementRequestBean() {
		// TODO Auto-generated constructor stub
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public String getDescription() {
		return description;
	}






	public void setDescription(String description) {
		this.description = description;
	}






	public String getStart_date() {
		return start_date;
	}






	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}






	public PaypalCreatePlanBean getPlanBean() {
		return plan;
	}






	public void setPlanBean(PaypalCreatePlanBean plan) {
		this.plan = plan;
	}






	public Payer getPayer() {
		return payer;
	}






	public void setPayer(Payer payer) {
		this.payer = payer;
	}






	public PaypalCreateAgreementRequestBean(String name, String description, String start_date,
			PaypalCreatePlanBean plan, Payer payer, PaypalLinksBean[] links) {
		super();
		this.name = name;
		this.description = description;
		this.start_date = start_date;
		this.plan = plan;
		this.payer = payer;
		this.links = links;
	}






	public class Payer{
		
		private String payment_method;

		public String getPayment_method() {
			return payment_method;
		}

		public void setPayment_method(String payment_method) {
			this.payment_method = payment_method;
		}

		public Payer(String payment_method) {
			super();
			this.payment_method = payment_method;
		}
		
		
	}
}
