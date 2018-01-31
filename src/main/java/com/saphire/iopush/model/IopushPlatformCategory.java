package com.saphire.iopush.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="iopush_platform_category")
public class IopushPlatformCategory {

	public IopushPlatformCategory() {
		// TODO Auto-generated constructor stub
	}
	private int categoryID;
	private String categoryName;
	public IopushPlatformCategory(int categoryID, String categoryName) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}
	/**
	 * @return the categoryID
	 */
	@Id   
    @SequenceGenerator(name="iopush_platform_category_seq", sequenceName="iopush_platform_category_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_platform_category_seq")
    @Column(name = "category_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getCategoryID() {
		return categoryID;
	}
	/**
	 * @param categoryID the categoryID to set
	 */
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
