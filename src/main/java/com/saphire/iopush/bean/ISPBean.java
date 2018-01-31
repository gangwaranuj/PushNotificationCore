package com.saphire.iopush.bean;

import java.util.List;

public class ISPBean implements Comparable<ISPBean>{

	String ispName ;
	String platformName = "" ;
	long totalusers = 0L;
	int count=0;
	int ispCode=0;
	List<PlatformBean> browserBean;
	
	public List<PlatformBean> getBrowserBean() {
		return browserBean;
	}
	public void setBrowserBean(List<PlatformBean> browserBean) {
		this.browserBean = browserBean;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public long getTotalusers() {
		return totalusers;
	}
	public void setTotalusers(long totalusers) {
		this.totalusers = totalusers;
	}
	public int getIspCode() {
		return ispCode;
	}
	public void setIspCode(int ispCode) {
		this.ispCode = ispCode;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ISPBean(String ispName,String platformName,int count,int totalusers,int ispCode) {
		super();
		this.ispName = ispName;
		this.platformName = platformName;
		this.count = count;
		this.totalusers = totalusers;
		this.ispCode=ispCode;
	}
	  public ISPBean(int ispCode, String ispName, long totalusers, List<PlatformBean> browserBean)
	  {
	    this.totalusers = totalusers;
	    this.browserBean = browserBean;
	    this.ispName=ispName ;
	    this.ispCode=ispCode;
	  }
	
	
	/**
	 * @return the ispName
	 */
	public String getIspName() {
		return ispName;
	}
	/**
	 * @param ispName the ispName to set
	 */
	public void setIspName(String ispName) {
		this.ispName = ispName;
	}
	@Override
	public int compareTo(ISPBean o) {
	    return (int)(o.totalusers - this.totalusers);
	}

}
