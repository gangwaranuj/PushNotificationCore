package com.saphire.iopush.bean;

public class ISPCategoryBean {

	private int mobile;
	private int desktop;
	public ISPCategoryBean(int mobile, int desktop) {
		super();
		this.mobile = mobile;
		this.desktop = desktop;
	}
	/**
	 * @return the mobile
	 */
	public int getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(int mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the desktop
	 */
	public int getDesktop() {
		return desktop;
	}
	/**
	 * @param desktop the desktop to set
	 */
	public void setDesktop(int desktop) {
		this.desktop = desktop;
	}
	
	
}
