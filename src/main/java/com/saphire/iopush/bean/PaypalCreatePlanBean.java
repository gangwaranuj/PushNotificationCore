package com.saphire.iopush.bean;



public class PaypalCreatePlanBean {

	private String name;
	private String description;
	private String type	;
	public PaypalPaymentDefinitions[] payment_definitions;
	public  PaypalMerchantPreferences merchant_preferences;
	//return objects
	private String id;
	private String state;
	private String create_time;
	private String update_time;
	public PaypalLinksBean[] link;
	
	
	public PaypalCreatePlanBean(String name, String description, String type, PaypalPaymentDefinitions[] payment_definitions,
			PaypalMerchantPreferences merchant_preferences, String id, String state, String create_time, String update_time,
			PaypalLinksBean[] link) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.payment_definitions = payment_definitions;
		this.merchant_preferences = merchant_preferences;
		this.id = id;
		this.state = state;
		this.create_time = create_time;
		this.update_time = update_time;
		this.link = link;
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
	public PaypalLinksBean[] getLink() {
		return link;
	}
	public void setLink(PaypalLinksBean[] link) {
		this.link = link;
	}

	public class Link{
		
		private String href;
		private String rel;
		private String method;
		
		public String getHref() {
			return href;
		}
		public void setHref(String href) {
			this.href = href;
		}
		public String getRel() {
			return rel;
		}
		public void setRel(String rel) {
			this.rel = rel;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public Link(String href, String rel, String method) {
			super();
			this.href = href;
			this.rel = rel;
			this.method = method;
		}
		
			
		
	}
	

	public PaypalCreatePlanBean(String name, String description, String type, PaypalPaymentDefinitions[] payment_definitions,
			PaypalMerchantPreferences merchant_preferences) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.payment_definitions = payment_definitions;
		this.merchant_preferences = merchant_preferences;
	}
	public PaypalCreatePlanBean() {
		// TODO Auto-generated constructor stub
	}
	public PaypalPaymentDefinitions[] getPayment_definitions() {
		return payment_definitions;
	}
	public void setPayment_definitions(PaypalPaymentDefinitions[] payment_definitions) {
		this.payment_definitions = payment_definitions;
	}
	public PaypalMerchantPreferences getMerchant_preferences() {
		return merchant_preferences;
	}
	public void setMerchant_preferences(PaypalMerchantPreferences merchant_preferences) {
		this.merchant_preferences = merchant_preferences;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "PaypalCreatePlanBean [payment_definitions=" + payment_definitions + ", merchant_preferences="
				+ merchant_preferences + ", name=" + name + ", description=" + description + ", type=" + type + "]";
	}

	


	

}
