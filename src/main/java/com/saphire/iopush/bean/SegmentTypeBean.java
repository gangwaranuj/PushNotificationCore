package com.saphire.iopush.bean;

public class SegmentTypeBean {

	private int segmentTypeId;
	private String segmentTypeName;
	private int productId;

	public SegmentTypeBean() {
		super();
	}
	/**
	 * @return the segmentType
	 */
	
	public int getProductId() {
		return productId;
	}

	/**
	 * @return the segmentTypeId
	 */
	public int getSegmentTypeId() {
		return segmentTypeId;
	}
	/**
	 * @param segmentTypeId the segmentTypeId to set
	 */
	public void setSegmentTypeId(int segmentTypeId) {
		this.segmentTypeId = segmentTypeId;
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
	public void setProductId(int productId) {
		this.productId = productId;
	}


	public SegmentTypeBean(int segmentTypeId, String segmentTypeName,int productId) {
		super();
		this.segmentTypeId = segmentTypeId;
		this.segmentTypeName = segmentTypeName;
		this.productId = productId;
		
	}


	

	@Override
	public String toString() {
		return "CustomNotificationBean [segmentTypeId=" + segmentTypeId + ", segmentTypeName=" + segmentTypeName
				+ ", productId=" + productId 
				+ "]";
	}









}
