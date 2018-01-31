package com.saphire.iopush.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="iopush_platform_details")
public class IopushPlatformDetails  implements java.io.Serializable {

	public IopushPlatformDetails() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int platformID;
	 private IopushPlatformCategory iopushPlatformCategory;
     private String createdBy;
     private Date creationDate;
     private String platformName;
     
     
	public IopushPlatformDetails(int platformID, IopushPlatformCategory iopushPlatformCategory, String createdBy,
			Date creationDate, String platformName) {
		super();
		this.platformID = platformID;
		this.iopushPlatformCategory = iopushPlatformCategory;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.platformName = platformName;
	}
	/**
	 * @return the platformID
	 */
	@Id   
    @SequenceGenerator(name="iopush_platform_details_seq", sequenceName="iopush_platform_details_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_platform_details_seq")
    @Column(name = "platform_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getPlatformID() {
		return platformID;
	}
	/**
	 * @param platformID the platformID to set
	 */
	public void setPlatformID(int platformID) {
		this.platformID = platformID;
	}
	/**
	 * @return the iopushPlatformCategory
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id", nullable=false)
	public IopushPlatformCategory getIopushPlatformCategory() {
		return iopushPlatformCategory;
	}
	/**
	 * @param iopushPlatformCategory the iopushPlatformCategory to set
	 */
	public void setIopushPlatformCategory(IopushPlatformCategory iopushPlatformCategory) {
		this.iopushPlatformCategory = iopushPlatformCategory;
	}
	/**
	 * @return the createdBy
	 */
	@Column(name="created_by", length=50)
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the creationDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_date", length=29)
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the productName
	 */
	@Column(name="platform_name", nullable=false, length=50)
	public String getPlatformName() {
		return platformName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
     
     
}
