package com.saphire.iopush.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="iopush_product")
public class IopushProduct {

	private int productID;
	private String productName;


	private String hash;
	

	
	
	public IopushProduct() {
		// TODO Auto-generated constructor stub
	}
	
	public IopushProduct(int productID, String productName) {
		super();
		this.productID = productID;
		this.productName = productName;
		
	}
	public IopushProduct(int i) {
		this.productID=i;
	}

	/**
	 * @return the productID
	 */
	@Id   
    @SequenceGenerator(name="iopush_product_seq", sequenceName="iopush_product_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_product_seq")
    @Column(name = "product_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getProductID() {
		return productID;
	}
	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}
	/**
	 * @return the productName
	 */
	@Column(name="product_name", nullable=false, unique=true, length=50)
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	

	@Column(name="hash", nullable=false, unique=true, length=15)
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}



//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "iopushProduct")
//	public IopushUsercategory getIopushUserCategory() {
//		return iopushUserCategory;
//	}
//
//	public void setIopushUserCategory(IopushUsercategory iopushUserCategory) {
//		this.iopushUserCategory = iopushUserCategory;
//	}
//	
	
}
