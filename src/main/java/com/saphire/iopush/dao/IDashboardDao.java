package com.saphire.iopush.dao;

import java.util.Date;

import com.saphire.iopush.utils.Response;

public interface IDashboardDao {

	Response platformDetails(int objects);
	
	Response findIspDetails(int product);

	
	Response findGeo(int productID);

	Response findPlatformDetails();

	Response getViews(Date d1, Date d2, int productID);

	Response getClicks(Date d1, Date d2, int productID);

	Response getHourlyViews(Date d1, Date d2, int productID);

	Response getHourlyClicks(Date d1, Date d2, int productID);

	Response getDateWiseTrendDetails(Date d1, Date d2, int productID);

	Response getHourWiseTrendDetails(Date start_date, Date end_date, int productID);

	Response PlanDetails(String use,int prodId);

	Response lastNotificationDetails(int productID);

	Response previewLastNotificationDetails(int productID);

	Response findBrowsers();

	Response findGeoCode(String country_code);

	Response findCityNew(int country_id, int product);

	Response fetchRules(int campaignId);

	Response listplatform();


}
 