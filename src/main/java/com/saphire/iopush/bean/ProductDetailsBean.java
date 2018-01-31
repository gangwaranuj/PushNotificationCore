package com.saphire.iopush.bean;

public class ProductDetailsBean {

	private int productId;
	private String productName;
	public ProductDetailsBean(int productId, String productName) {
		super();
		this.productId = productId;
		this.productName = productName;
	}
	/**
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductDetailsBean [productId=" + productId + ", productName=" + productName + "]";
	}
	
	
}
