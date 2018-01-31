 package com.saphire.iopush.bean;
 
 
 import java.util.List;



public class GeoBean implements Comparable<GeoBean> {
  String geoCode = "";
  String geoName = "";
  String geoFlag = "";
  String geoLongitude = "";
  String geoLatitude = "";
  String platformName = "" ;
  long totalusers = 0L;
  int count=0;
  

  public String getGeoCode() {
	return geoCode;
}


public void setGeoCode(String geoCode) {
	this.geoCode = geoCode;
}


public String getGeoName() {
	return geoName;
}


public void setGeoName(String geoName) {
	this.geoName = geoName;
}


public String getGeoFlag() {
	return geoFlag;
}


public void setGeoFlag(String geoFlag) {
	this.geoFlag = geoFlag;
}


public String getGeoLongitude() {
	return geoLongitude;
}


public void setGeoLongitude(String geoLongitude) {
	this.geoLongitude = geoLongitude;
}


public String getGeoLatitude() {
	return geoLatitude;
}


public void setGeoLatitude(String geoLatitude) {
	this.geoLatitude = geoLatitude;
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


public int getCount() {
	return count;
}


public void setCount(int count) {
	this.count = count;
}


public List<PlatformBean> getBrowserBean() {
	return browserBean;
}


public void setBrowserBean(List<PlatformBean> browserBean) {
	this.browserBean = browserBean;
}


List<PlatformBean> browserBean;
  
  public GeoBean(String geoCode, String geoName, String geoLongitude, String geoLatitude, String platformName, int count)
  {
    this.geoCode = geoCode;
    this.geoName = geoName;
    this.geoLongitude = geoLongitude;
    this.geoLatitude = geoLatitude;
    this.platformName = platformName;
    this.count = count;
  }


  public GeoBean(String geoCode, String geoName, String geoFlag, String geoLongitude, String geoLatitude, long totalusers, List<PlatformBean> browserBean)
  {
    this.geoCode = geoCode;
    this.geoName = geoName;
    this.geoFlag = geoFlag;
    this.geoLongitude = geoLongitude;
    this.geoLatitude = geoLatitude;
    this.totalusers = totalusers;
    this.browserBean = browserBean;
  }
  

  public int compareTo(GeoBean o)
  {
    return (int)(o.totalusers - this.totalusers);
  }
}
