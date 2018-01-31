package com.saphire.iopush.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="iopush_segment_type", uniqueConstraints = @UniqueConstraint(columnNames={"segmentTypeName", "productId"}))
public class IopushSegmentType {

	private int segmentTypeId;
	private String segmentTypeName;
	private int productId;

	
	
	

	public IopushSegmentType() {
		// TODO Auto-generated constructor stub
	}
	
	public IopushSegmentType(int segmentTypeId, String segmentTypeName) {
		super();
		this.segmentTypeId = segmentTypeId;
		this.segmentTypeName = segmentTypeName;
		
	}
	/**
	 * @return the segmentTypeId
	 */
	@Id   
    @SequenceGenerator(name="iopush_segmentType_seq", sequenceName="iopush_segmentType_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_segmentType_seq")
    @Column(name = "segmentTypeId", unique = true, nullable = false,columnDefinition = "serial")
	
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
	@Column(name="segmentTypeName", nullable=false,  length=50)
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
	 * @return the productId
	 */
	@Column(name="productId", nullable = false, columnDefinition = "int default 0")
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
