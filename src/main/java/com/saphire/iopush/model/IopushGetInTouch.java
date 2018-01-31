package com.saphire.iopush.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "iopush_getintouch", schema = "public")
public class IopushGetInTouch implements java.io.Serializable{
	
	 private static final long serialVersionUID = 1L;
	 private int id;

	private int product_id;//private IopushProduct iopushProduct;  
	 private int userId; //shUser iopushUser;
	 private boolean customizePlan;
	 private Date createdOn;
	 private Date modifiedOn;
	 private String createdBy;
	 private String modifiedBy;
	 private String fullName;
	 private String phoneNumber;
	 private String company;
	 private String emailId;
	 private String websiteUrl;
	 private String monthlyWebsiteVisitors;
	 private String message;
	 private String source;
	 
	

	public IopushGetInTouch(){}
	 public IopushGetInTouch(int id, int product_id, int userId, boolean customizePlan, Date createdOn, Date modifiedOn,
				String createdBy, String modifiedBy, String fullName, String phoneNumber, String company, String emailId,
				String websiteUrl, String monthlyWebsiteVisitors, String message, String source) {
			super();
			this.id = id;
			this.product_id = product_id;
			this.userId = userId;
			this.customizePlan = customizePlan;
			this.createdOn = createdOn;
			this.modifiedOn = modifiedOn;
			this.createdBy = createdBy;
			this.modifiedBy = modifiedBy;
			this.fullName = fullName;
			this.phoneNumber = phoneNumber;
			this.company = company;
			this.emailId = emailId;
			this.websiteUrl = websiteUrl;
			this.monthlyWebsiteVisitors = monthlyWebsiteVisitors;
			this.message = message;
			this.source = source;
		}

	public IopushGetInTouch(int id, int product_id, int userId, boolean customizePlan, Date createdOn, Date modifiedOn,
			String createdBy, String modifiedBy, String fullName, String phoneNumber, String company, String emailId,
			String websiteUrl, String monthlyWebsiteVisitors, String message) {
		super();
		this.id = id;
		this.product_id = product_id;
		this.userId = userId;
		this.customizePlan = customizePlan;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.company = company;
		this.emailId = emailId;
		this.websiteUrl = websiteUrl;
		this.monthlyWebsiteVisitors = monthlyWebsiteVisitors;
		this.message = message;
	}


	 @Id
	 @SequenceGenerator(name = "iopush_getintouch_seq", sequenceName = "iopush__seq", allocationSize = 1)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iopush_getintouch_seq")
	 @Column(name = "getintouch_id", unique = true, nullable = false, columnDefinition = "serial")
	 int getId() {
		return id;
	}
public	void setId(int id) {
		this.id = id;
	}
	@Column(name = "product_id", nullable = false)
	int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	@Column(name = "user_id", nullable = false)
	int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
//	@Column(name = "enterprise", nullable = false)

	
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "created_on", length = 29)
	Date getCreatedOn() {
		return createdOn;
	}
	 
	 @Column(name = "enterprise")
	public boolean isCustomizePlan() {
		return customizePlan;
	}
	public void setCustomizePlan(boolean customizePlan) {
		this.customizePlan = customizePlan;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "modified_on", length = 29)
	Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	
	 @Column(name = "created_by", length = 30)
	String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	 @Column(name = "modified_by", length = 30)
	String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "username", nullable = false, length = 50)
	String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Column(name = "phone_number", length = 14)
	String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Column(name = "company", nullable = true)
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@Column(name = "email_id", nullable = false)
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Column(name = "website_url", nullable = true, length = 250)
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	
	@Column(name = "monthly_web_visitors", nullable = true, length = 250)
	public String getMonthlyWebsiteVisitors() {
		return monthlyWebsiteVisitors;
	}
	public void setMonthlyWebsiteVisitors(String monthlyWebsiteVisitors) {
		this.monthlyWebsiteVisitors = monthlyWebsiteVisitors;
	}
	
	@Column(name = "message", nullable = true, length = 250)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Column(name = "source", nullable = true, length = 100)
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Override
	public String toString() {
		return "IopushGetInTouch [id=" + id + ", product_id=" + product_id + ", userId=" + userId + ", customizePlan="
				+ customizePlan + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber
				+ ", company=" + company + ", emailId=" + emailId + ", websiteUrl=" + websiteUrl
				+ ", monthlyWebsiteVisitors=" + monthlyWebsiteVisitors + ", message=" + message + ", source=" + source
				+ "]";
	}  

	
	 
}
