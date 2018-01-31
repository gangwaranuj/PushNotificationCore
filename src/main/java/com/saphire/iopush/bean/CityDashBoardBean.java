package com.saphire.iopush.bean;

import java.util.List;

public class CityDashBoardBean {
   int city_id=0;
   List<BrowserBean> browserBean;
   String city_name="";
   String city_latitude="";
   String city_longitude="";
   long totalusers = 0L;
   
   
   
   
/**
 * @param city_id
 * @param city_name
 * @param city_latitude
 * @param city_longitude
 * @param browserBean
 * @param totalusers
 */
public CityDashBoardBean(int city_id, String city_name, String city_latitude, String city_longitude,
		List<BrowserBean> browserBean, long totalusers) {
	this.city_id = city_id;
	this.city_name = city_name;
	this.city_latitude = city_latitude;
	this.city_longitude = city_longitude;
	this.browserBean = browserBean;
	this.totalusers = totalusers;
}
/**
 * @return the city_id
 */
public int getCity_id() {
	return city_id;
}
/**
 * @return the browserBean
 */
public List<BrowserBean> getBrowserBean() {
	return browserBean;
}
/**
 * @return the city_name
 */
public String getCity_name() {
	return city_name;
}
/**
 * @return the city_latitude
 */
public String getCity_latitude() {
	return city_latitude;
}
/**
 * @return the city_longitude
 */
public String getCity_longitude() {
	return city_longitude;
}
/**
 * @return the totalusers
 */
public long getTotalusers() {
	return totalusers;
}
/**
 * @param city_id the city_id to set
 */
public void setCity_id(int city_id) {
	this.city_id = city_id;
}
/**
 * @param browserBean the browserBean to set
 */
public void setBrowserBean(List<BrowserBean> browserBean) {
	this.browserBean = browserBean;
}
/**
 * @param city_name the city_name to set
 */
public void setCity_name(String city_name) {
	this.city_name = city_name;
}
/**
 * @param city_latitude the city_latitude to set
 */
public void setCity_latitude(String city_latitude) {
	this.city_latitude = city_latitude;
}
/**
 * @param city_longitude the city_longitude to set
 */
public void setCity_longitude(String city_longitude) {
	this.city_longitude = city_longitude;
}
/**
 * @param totalusers the totalusers to set
 */
public void setTotalusers(long totalusers) {
	this.totalusers = totalusers;
}
   
   
 
}
