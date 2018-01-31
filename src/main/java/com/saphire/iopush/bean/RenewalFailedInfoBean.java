package com.saphire.iopush.bean;

public class RenewalFailedInfoBean {

	//String[] columns = new String[]{"First Name","Email Id","Phone Number","Renew Amount","Creation Date","Agreement Status","OutStanding Balance"};
	
	private String firstName;
	private String emailId;
	private String phoneNumber;
	private String renewAmount;
	private String date;
	private String AgreementStatus;
	private String OutstandingBalance;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRenewAmount() {
		return renewAmount;
	}
	public void setRenewAmount(String renewAmount) {
		this.renewAmount = renewAmount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAgreementStatus() {
		return AgreementStatus;
	}
	public void setAgreementStatus(String agreementStatus) {
		AgreementStatus = agreementStatus;
	}
	public String getOutstandingBalance() {
		return OutstandingBalance;
	}
	public void setOutstandingBalance(String outstandingBalance) {
		OutstandingBalance = outstandingBalance;
	}
	
	
	
	
}

