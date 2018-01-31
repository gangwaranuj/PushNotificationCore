package com.saphire.iopush.bean;

import java.util.Date;

import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSegmentType;

public class SegmentationBean {

	private int segmentId = 0;
	private String segmentName;
	private IopushProduct iopushProduct;
	private int productId=0;
	private IopushSegmentType segmentType; 
	private int segmentTypeID=0;
	private int pageSize;
	private int startIndex;
	private String createdBy;
	private String modifiedBy;
	private Date createdOn;
	private Date modifiedOn;
	private String hash;
	private String segmentTypeName;
	// Order criteria
			String columnForOrdering="";
			String requiredOrder  = "";
	/**
	 * @return the segmentTypeID
	 */
	public int getSegmentTypeID() {
		return segmentTypeID;
	}
	/**
	 * @return the createdBy
	 */
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
	public Date getCreatedOn() {
		return createdOn;
	}
	public String getColumnForOrdering() {
		return columnForOrdering;
	}

	public String getRequiredOrder() {
		return requiredOrder;
	}

	public void setColumnForOrdering(String columnForOrdering) {
		this.columnForOrdering = columnForOrdering;
	}

	public void setRequiredOrder(String requiredOrder) {
		this.requiredOrder = requiredOrder;
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
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	/**
	 * @param segmentTypeID the segmentTypeID to set
	 */
	public void setSegmentTypeID(int segmentTypeID) {
		this.segmentTypeID = segmentTypeID;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public SegmentationBean(int segmentId,String segmentName, Date createdOn ,Date modifiedOn,String hash,int segmentTypeID,String createdBy,String segmentTypeName,int pid) {
		this.segmentId = segmentId;
		this.segmentName = segmentName;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.hash = hash;
		this.segmentTypeID = segmentTypeID;
		this.createdBy = createdBy;
		this.segmentTypeName = segmentTypeName;
		this.productId = pid;
	}
	/**
	 * @return the segmentType
	 */

	/**
	 * @return the segmentId
	 */
	public int getSegmentId() {
		return segmentId;
	}
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
	 * @param segmentId the segmentId to set
	 */
	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
	}
	/**
	 * @return the segmentName
	 */
	public String getSegmentName() {
		return segmentName;
	}
	/**
	 * @param segmentName the segmentName to set
	 */
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	/**
	 * @return the iopushProduct
	 */
	public IopushProduct getIopushProduct() {
		return iopushProduct;
	}
	/**
	 * @param iopushProduct the iopushProduct to set
	 */
	public void setIopushProduct(IopushProduct iopushProduct) {
		this.iopushProduct = iopushProduct;
	}
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}


	public SegmentationBean(int segmentId, String segmentName,int productId,int segmentTypeID) {
		super();
		this.segmentId = segmentId;
		this.segmentName = segmentName;
		this.productId = productId;
		this.segmentTypeID = segmentTypeID;
	}


	

	@Override
	public String toString() {
		return "CustomNotificationBean [segmentId=" + segmentId + ", segmentName=" + segmentName
				+ ", productId=" + productId 
				+ "]";
	}









}
