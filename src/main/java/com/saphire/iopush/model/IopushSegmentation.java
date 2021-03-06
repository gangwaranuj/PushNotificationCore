package com.saphire.iopush.model;
// Generated 24 Jan, 2017 10:27:42 PM by Hibernate Tools 3.4.0.CR1


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

/**
 * IopushCampaign generated by hbm2java
 */
@Entity
@Table(name="iopush_segment"
,schema="public"
) 
		
public class IopushSegmentation  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int segmentId;
	private IopushProduct iopushProduct;
	private String segmentName; 
	private IopushSegmentType segmentType; 
	private String createdBy;
	private String modifiedBy;
	private Date createdOn;
	private Date modifiedOn;
	private String hash;
	private String segmentTypeName;
	
	//private boolean requireInteraction;
	/**
	 * @return the subscribed
	 */
	

	






		/**
	 * @return the segmentTypeName
	 */
	public String getSegmentTypeName() {
		return segmentTypeName;
	}







	/**
	 * @param segmentTypeName the segmentTypeName to set
	 */
	public void setSegmentTypeName(String segmentTypeName) {
		this.segmentTypeName = segmentTypeName;
	}







		/**
	 * @return the welcomeId
	 */
	 @Id   
	    @SequenceGenerator(name="iopush_segment_seq", sequenceName="iopush_segment_seq",allocationSize=1)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_segment_seq")
	 @Column(name = "segmentId", unique = true, nullable = false,columnDefinition = "serial")
	public int getSegmentId() {
		return segmentId;
	}

	





	@Column(name="hash", nullable=false, unique=true, length=15)
		public String getHash() {
			return hash;
		}
		
		public void setHash(String hash) {
			this.hash = hash;
		}





	/**
		 * @return the createdBy
		 */
		@Column(name="createdBy", length=50)
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
		 * @return the modifiedBy
		 */
		@Column(name="modifiedBy", length=50)
		public String getModifiedBy() {
			return modifiedBy;
		}








		/**
		 * @param modifiedBy the modifiedBy to set
		 */
		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}








		/**
		 * @return the createdOn
		 */
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="createdOn", length=29)
		public Date getCreatedOn() {
			return createdOn;
		}








		/**
		 * @param createdOn the createdOn to set
		 */
		public void setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
		}








		/**
		 * @return the modifiedOn
		 */
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="modifiedOn", length=29)
		public Date getModifiedOn() {
			return modifiedOn;
		}








		/**
		 * @param modifiedOn the modifiedOn to set
		 */
		public void setModifiedOn(Date modifiedOn) {
			this.modifiedOn = modifiedOn;
		}








	/**
	 * @param welcomeId the welcomeId to set
	 */
	 
	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
	}




	/**
	 * @return the iopushProduct
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fk_product_id", nullable=false)
	public  IopushProduct getIopushProduct() {
		return iopushProduct;
	}




	/**
	 * @return the segmentType
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fk_segmentTypeId", nullable=false)
	public IopushSegmentType getSegmentType() {
		return segmentType;
	}
	/**
	 * @param segmentType the segmentType to set
	 */
	public void setSegmentType(IopushSegmentType segmentType) {
		this.segmentType = segmentType;
	}
	/**
	 * @param iopushProduct the iopushProduct to set
	 */
	public void setIopushProduct(IopushProduct iopushProduct) {
		this.iopushProduct = iopushProduct;
	}
/* @return the segmentName
	 */
	@Column(name="segmentName", length=30)
	public String getSegmentName() {
		return segmentName;
	}
	/**
	 * @param segmentName the segmentName to set
	 */
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
}


