package com.saphire.iopush.service;

import java.util.HashMap;

import com.saphire.iopush.bean.CampaignBean;
import com.saphire.iopush.bean.GeoStatBean;
import com.saphire.iopush.bean.SubCampaignBean;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface ICampaignService {

	String add();

	JsonResponse<CampaignBean> draftcampaign(CampaignBean campignBean, Integer integer);
	
	JsonResponse<CampaignBean> pendingcampaign(CampaignBean campignBean, Integer integer);

	JsonResponse<CampaignBean> list(CampaignBean campignBean);

	JsonResponse<CampaignBean> fetchByID(CampaignBean campignBean);

	JsonResponse<CampaignBean> updatedraft(CampaignBean campignBean, Integer integer);
	
	JsonResponse<CampaignBean> updatepending(CampaignBean campignBean, Integer integer);

	JsonResponse<CampaignBean> delete(CampaignBean campignBean);

	ResponseMessage changeCampaignStatus(CampaignBean campignBean);

	ResponseMessage checkCampaignName(CampaignBean campignBean , Integer integer);

	JsonResponse<ComboBoxOption> listISP(Integer[] countryId);

	JsonResponse<ComboBoxOption> listTimezone();

	JsonResponse<ComboBoxOption> fetchCity(int country_id);

	ResponseMessage launch(CampaignBean campignBean, Integer integer);

	ResponseMessage expire(int campaign_id);
	ResponseMessage campaignAnalytics(int campaign_id,int type,int geo_id,int platform_id,int city_id,int product_id);
	

	ResponseMessage sentNotification(CampaignBean campaignBean);
	
	 
	JsonResponse<String> criteriaList(HashMap<String, String> criteriaList);

	JsonResponse<ComboBoxOption> listPlatform();



	JsonResponse<SubCampaignBean> fetchSubCampaignList(CampaignBean campignBean);

	

	JsonResponse<ComboBoxOption> listProduct();
	
	ResponseMessage autoexpire();

	ResponseMessage autoLaunch();

	
	JsonResponse<CampaignBean> findCampaignStatList(CampaignBean campignBean);
	JsonResponse<GeoStatBean> campaignStatsCountry(CampaignBean campignBean);

	
}
