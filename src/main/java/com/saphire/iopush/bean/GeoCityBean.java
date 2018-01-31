package com.saphire.iopush.bean;

import java.util.ArrayList;
import java.util.List;



public class GeoCityBean {
	
	int city_id=0;
     String county_name = "";
     String county_code = "";
     String city_name = "";
     String city_code = "";
     Double city_longitude = 00.00;
     Double city_latitude = 00.00;
     String time_zone="";
     String isp="";
     int geoid=0;
     public int getGeoid() {
		return geoid;
	}

	public List<PlatformStatBean> getPlatform() {
		return platform;
	}

	public void setGeoid(int geoid) {
		this.geoid = geoid;
	}

	public void setPlatform(List<PlatformStatBean> platform) {
		this.platform = platform;
	}

	List<PlatformStatBean> platform=new ArrayList<PlatformStatBean>();
 	/**
     
	/**
	 * 
	 */
	public GeoCityBean() {
	}

	public GeoCityBean(String county_name, String county_code, String city_name, String city_code,
			Double city_longitude, Double city_latitude,String time_zone,String isp) {
		super();
		this.county_name = county_name;
		this.county_code = county_code;
		this.city_name = city_name;
		this.city_code = city_code;
		this.city_longitude = city_longitude;
		this.city_latitude = city_latitude;
		this.time_zone=time_zone;
		this.isp=isp;
	}
	
	public GeoCityBean(int city_id,String county_name, String county_code, String city_name, String city_code,
			Double city_longitude, Double city_latitude) {
		super();
		this.city_id = city_id;
		this.county_name = county_name;
		this.county_code = county_code;
		this.city_name = city_name;
		this.city_code = city_code;
		this.city_longitude = city_longitude;
		this.city_latitude = city_latitude;
	}

	public GeoCityBean(int geoid,int cityid, String cityname, List<PlatformStatBean> platform) {
		this.geoid = geoid;
		this.city_id = cityid;
		this.city_name = cityname;
		this.platform = platform;
	}
	
	public String getCounty_name() {
		return county_name;
	}

	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

	public String getCounty_code() {
		return county_code;
	}

	public void setCounty_code(String county_code) {
		this.county_code = county_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public Double getCity_longitude() {
		return city_longitude;
	}

	public void setCity_longitude(Double city_longitude) {
		this.city_longitude = city_longitude;
	}

	public Double getCity_latitude() {
		return city_latitude;
	}

	public void setCity_latitude(Double city_latitude) {
		this.city_latitude = city_latitude;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the time_zone
	 */
	public String getTime_zone() {
		return time_zone;
	}

	/**
	 * @param time_zone the time_zone to set
	 */
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	/**
	 * @return the isp
	 */
	public String getIsp() {
		return isp;
	}

	/**
	 * @param isp the isp to set
	 */
	public void setIsp(String isp) {
		this.isp = isp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"GeoCityBean [city_id=%s, county_name=%s, county_code=%s, city_name=%s, city_code=%s, city_longitude=%s, city_latitude=%s, time_zone=%s, isp=%s]",
				city_id, county_name, county_code, city_name, city_code, city_longitude, city_latitude, time_zone, isp);
	}

	
     
}
