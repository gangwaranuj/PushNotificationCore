package com.saphire.iopush.service;

import com.saphire.iopush.bean.CityDashBoardBean;
import com.saphire.iopush.bean.GeoBean;
import com.saphire.iopush.bean.ISPBean;
import com.saphire.iopush.bean.LastNotificationBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.PlatformBean;
import com.saphire.iopush.bean.PreviewLastNotificationBean;
import com.saphire.iopush.bean.TrendsBean;
import com.saphire.iopush.bean.ViewsClicksBean;
import com.saphire.iopush.utils.JsonResponse;

public interface IDashboardService {

	JsonResponse<PlatformBean> platformsApi(Integer attribute);

	JsonResponse<ViewsClicksBean> viewClicksApi(String startdate, String enddate, int action, Integer attribute);

	JsonResponse<GeoBean> geoApi(Integer attribute);

	JsonResponse<ISPBean> ispApi(Integer attribute);

	JsonResponse<TrendsBean> trendsApi(String startdate, String enddate, int action, Integer attribute);

	JsonResponse<ViewsClicksBean> viewClicksHourApi(String startdate, String enddate, int action,  Integer attribute);

	JsonResponse<TrendsBean> trendsHourApi(String startdate, String enddate, int action, Integer attribute);

	JsonResponse<PlanBean> planApi(String currentUser, int prodId);

	JsonResponse<LastNotificationBean> lastNotificationApi(Integer attribute);

	JsonResponse<PreviewLastNotificationBean> previewLastNotificationApi(Integer attribute);

	JsonResponse<CityDashBoardBean> fetchCityApiNew(String lowerCase);



	
}
