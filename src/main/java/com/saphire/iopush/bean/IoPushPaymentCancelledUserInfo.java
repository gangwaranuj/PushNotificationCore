package com.saphire.iopush.bean;

public class IoPushPaymentCancelledUserInfo {

	private String userName;
	private String email;
	private String phoneNumber;
	private String PackageInfo;
	private int id;
	private String date;
	

	    public int getId() {
	        return this.id;
	    }
	    
	    public void setId(int ispId) {
	        this.id = ispId;
	    }
	   
	public String getPackageName() {
		return PackageInfo;
	}
	public void setPackageName(String packageName) {
		this.PackageInfo = packageName;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String string) {
		this.phoneNumber = string;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "IoPushPaymentCanceledUserInfo [userName=" + userName + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", packageName=" + PackageInfo + "]";
	}
	
	
	
}
