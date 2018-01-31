package com.saphire.iopush.bean;

import java.util.Collection;

import java.util.Date;

import java.util.Iterator;

public class UserBean {

	private int userId;
    private String password;
    private String phoneNumber;
    private String middleName;
    private String lastPassword;
    private String countryCode;
    private String emailId;
    private String company;
    private String firstName;
    private String lastName;
    private String imagePath;
    private boolean isActive;
    private short loginAttempts;
    private String salutation;
    private String title;
    private String username;
    private String targetVisitors;
    private String subDomain;
    private String websiteUrl;
    private String pid;
    private String registrationDate;

    private int emailFlag;

    private Collection<?> grantedAuthorities ;
    private String fullname;
    
	public UserBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserBean(int userId, String password, String phoneNumber, String middleName, String lastPassword,
			String countryCode, String emailId, String company, String firstName, String lastName, String imagePath,
			boolean isActive, short loginAttempts, String salutation, String title, String username,
			String targetVisitors, String subDomain, String websiteUrl, String pid, String registrationDate,
			Collection<?> grantedAuthorities) {
		super();
		this.userId = userId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.middleName = middleName;
		this.lastPassword = lastPassword;
		this.countryCode = countryCode;
		this.emailId = emailId;
		this.company = company;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imagePath = imagePath;
		this.isActive = isActive;
		this.loginAttempts = loginAttempts;
		this.salutation = salutation;
		this.title = title;
		this.username = username;
		this.targetVisitors = targetVisitors;
		this.subDomain = subDomain;
		this.websiteUrl = websiteUrl;
		this.pid = pid;
		this.registrationDate = registrationDate;
		this.grantedAuthorities = grantedAuthorities;
	}
	public UserBean(int userId, String password, String phoneNumber, String middleName, String lastPassword,
			String countryCode, String emailId, String company, String firstName, String lastName, String imagePath,
			boolean isActive, short loginAttempts, String salutation, String title, String username,

			String targetVisitors, String subDomain, String websiteUrl, String pid, Collection<?> grantedAuthorities) {

		super();
		this.userId = userId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.middleName = middleName;
		this.lastPassword = lastPassword;
		this.countryCode = countryCode;
		this.emailId = emailId;
		this.company = company;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imagePath = imagePath;
		this.isActive = isActive;
		this.loginAttempts = loginAttempts;
		this.salutation = salutation;
		this.title = title;
		this.username = username;
		this.targetVisitors = targetVisitors;

		

		this.subDomain = subDomain;
		this.websiteUrl = websiteUrl;
		this.pid = pid;
		this.grantedAuthorities = grantedAuthorities;

	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastPassword
	 */
	public String getLastPassword() {
		return lastPassword;
	}
	/**
	 * @param lastPassword the lastPassword to set
	 */
	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the loginAttempts
	 */
	public short getLoginAttempts() {
		return loginAttempts;
	}
	/**
	 * @param loginAttempts the loginAttempts to set
	 */
	public void setLoginAttempts(short loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	/**
	 * @return the salutation
	 */
	public String getSalutation() {
		return salutation;
	}
	/**
	 * @param salutation the salutation to set
	 */
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the targetVisitors
	 */
	public String getTargetVisitors() {
		return targetVisitors;
	}
	/**
	 * @param targetVisitors the targetVisitors to set
	 */
	public void setTargetVisitors(String targetVisitors) {
		this.targetVisitors = targetVisitors;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	public String getSubDomain() {
		return subDomain;
	}
	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}
	
	
	
	
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Collection<?> getGrantedAuthorities() {
		return grantedAuthorities;
	}
	public void setGrantedAuthorities(Collection<?> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	
	
	
	
	public int getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(int emailFlag) {
		this.emailFlag = emailFlag;
	}
	
	

	public String getFullName() {
		return fullname;
	}

	public void setFullName(String fullName) {
		this.fullname = fullName;
	}


	@Override
	public String toString() {
		final int maxLen = 10;
		return "UserBean [userId=" + userId + ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", middleName=" + middleName + ", lastPassword=" + lastPassword + ", countryCode=" + countryCode
				+ ", emailId=" + emailId + ", company=" + company + ", firstName=" + firstName + ", lastName="
				+ lastName + ", imagePath=" + imagePath + ", isActive=" + isActive + ", loginAttempts=" + loginAttempts
				+ ", salutation=" + salutation + ", title=" + title + ", username=" + username + ", targetVisitors="
				+ targetVisitors + ", subDomain=" + subDomain + ", websiteUrl=" + websiteUrl + ", pid=" + pid
				+ ", registrationDate=" + registrationDate + ", grantedAuthorities="
				+ (grantedAuthorities != null ? toString(grantedAuthorities, maxLen) : null) + "]";
	}




	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
    
    
	
}
