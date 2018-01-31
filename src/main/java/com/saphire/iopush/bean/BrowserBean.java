package com.saphire.iopush.bean;

public class BrowserBean implements Comparable<BrowserBean> {

	String platformName = "";
	  int count = 0;
	   
		 
		 
	   public BrowserBean(String platformName, int count)
	   {
	     this.platformName = platformName;
	     this.count = count;
	   }
	   
	   public String getPlatformName() { return this.platformName; }
	   
	   public void setPlatformName(String platformName) {
	     this.platformName = platformName;
	   }
	   
	   public int getCount() { return this.count; }
	   
	   public void setCount(int count) {
	     this.count = count;
	   }
	   
	   @Override
	   public int compareTo(BrowserBean o)
	   {
	     return this.platformName.compareTo(o.platformName);
	   }

	

}
