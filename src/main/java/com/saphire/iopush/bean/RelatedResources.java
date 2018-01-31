package com.saphire.iopush.bean;

public class RelatedResources {

	
	private SaleBean sale;

	public RelatedResources(SaleBean sale) {
		super();
		this.sale = sale;
	}

	public SaleBean getSale() {
		return sale;
	}

	public void setSale(SaleBean sale) {
		this.sale = sale;
	}

	@Override
	public String toString() {
		return "RelatedResources [sale=" + sale + "]";
	}
	
	
}
