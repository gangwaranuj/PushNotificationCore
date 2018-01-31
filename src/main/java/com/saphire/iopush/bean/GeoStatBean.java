package com.saphire.iopush.bean;

import java.util.List;

public class GeoStatBean {
 int countryId=0;
 String countryName="";
 List<PlatformStatBean> platformStat;
 List<GeoCityBean> cityStat;
 
/**
 * 
 */
public GeoStatBean() {
}

/**
 * @param countryId
 * @param countryName
 * @param platformStat
 * @param cityStat
 */
public GeoStatBean(int countryId, String countryName, List<PlatformStatBean> platformStat,List<GeoCityBean> cityStat) {
	this.countryId = countryId;
	this.countryName = countryName;
	this.platformStat = platformStat;
	this.cityStat = cityStat;
}

/**
 * @param countryId
 * @param countryName
 * @param cityStat
 */
public GeoStatBean(int countryId, String countryName, List<GeoCityBean> cityStat) {
	this.countryId = countryId;
	this.countryName = countryName;
	this.cityStat = cityStat;
}

public int getCountryId() {
	return countryId;
}

public void setCountryId(int countryId) {
	this.countryId = countryId;
}

public String getCountryName() {
	return countryName;
}

public void setCountryName(String countryName) {
	this.countryName = countryName;
}

public List<PlatformStatBean> getPlatformStat() {
	return platformStat;
}

public void setPlatformStat(List<PlatformStatBean> platformStat) {
	this.platformStat = platformStat;
}

public List<GeoCityBean> getCityStat() {
	return cityStat;
}

public void setCityStat(List<GeoCityBean> cityStat) {
	this.cityStat = cityStat;
}

@Override
public String toString() {
	return "GeoStatBean [countryId=" + countryId + ", countryName=" + countryName + ", platformStat=" + platformStat
			+ ", cityStat=" + cityStat + "]";
}



}
