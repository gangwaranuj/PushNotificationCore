package com.saphire.iopush.bean;

public class GetInTouchBean {
	
	private String fullName;
    private String phoneNumber;
    private String company;
    private String emailId;
    private String websiteUrl;
    private String monthlyWebsiteVisitors;
    private String message;  
    private String userId;
    private String product_id;
    private String source;
    
    


	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public GetInTouchBean(String fullName, String phoneNumber, String company, String emailId, String websiteUrl,
			String monthlyWebsiteVisitors, String message, String userId, String product_id, String source) {
		super();
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.company = company;
		this.emailId = emailId;
		this.websiteUrl = websiteUrl;
		this.monthlyWebsiteVisitors = monthlyWebsiteVisitors;
		this.message = message;
		this.userId = userId;
		this.product_id = product_id;
		this.source = source;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	public String getMonthlyWebsiteVisitors() {
		return monthlyWebsiteVisitors;
	}
	public void setMonthlyWebsiteVisitors(String monthlyWebsiteVisitors) {
		this.monthlyWebsiteVisitors = monthlyWebsiteVisitors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "GetInTouchBean [fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", company=" + company
				+ ", emailId=" + emailId + ", websiteUrl=" + websiteUrl + ", monthlyWebsiteVisitors="
				+ monthlyWebsiteVisitors + ", message=" + message + ", userId=" + userId + ", product_id=" + product_id
				+ ", source=" + source + "]";
	}
  
    
    

}


