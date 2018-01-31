package com.saphire.iopush.bean;

public class ProductBean {
	int productId;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public ProductBean(int productId, String name) {
		super();
		this.productId = productId;
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	String name ;

}
