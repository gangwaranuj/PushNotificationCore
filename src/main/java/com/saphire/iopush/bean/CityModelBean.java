package com.saphire.iopush.bean;


public class CityModelBean {
	int city_id=0;
	 String browserName = "";
	   String city_name="";
	   String city_latitude="";
	   String city_longitude="";
	   int count = 0;
	/**
	 * @param city_id
	 * @param browserName
	 * @param city_name
	 * @param city_latitude
	 * @param city_longitude
	 * @param count
	 */
	public CityModelBean(int city_id, String browserName, String city_name, String city_latitude, String city_longitude,
			int count) {
		this.city_id = city_id;
		this.browserName = browserName;
		this.city_name = city_name;
		this.city_latitude = city_latitude;
		this.city_longitude = city_longitude;
		this.count = count;
	}
	/**
	 * @return the city_id
	 */
	public int getCity_id() {
		return city_id;
	}
	/**
	 * @return the browserName
	 */
	public String getBrowserName() {
		return browserName;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	/**
	 * @param browserName the browserName to set
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
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
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	   
	   
	   
}
