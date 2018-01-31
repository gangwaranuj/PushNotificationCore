package com.saphire.iopush.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saphire.iopush.bean.CityDashBoardBean;
import com.saphire.iopush.bean.GeoBean;
import com.saphire.iopush.bean.ISPBean;
import com.saphire.iopush.bean.LastNotificationBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.PlatformBean;
import com.saphire.iopush.bean.PreviewLastNotificationBean;
import com.saphire.iopush.bean.TrendsBean;
import com.saphire.iopush.bean.ViewsClicksBean;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.dao.IDashboardDao;
import com.saphire.iopush.dao.IUserDao;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushPlan;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.service.IDashboardService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.Utility;

@Service
public class DashboardServiceImpl implements IDashboardService {

	@Autowired IDashboardDao iDashboardDao;
	@Autowired ICampaignDao iCampaignDao;
	@Autowired IUserDao userDetailsDao;
	@Autowired Properties myProperties;

	@Autowired 
	private HttpSession httpSession;

	private Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<PlatformBean> platformsApi(Integer productID) {
		JsonResponse<PlatformBean> jsonResponse = new JsonResponse<PlatformBean>();
		Response response =new Response();
		try
		{
			response = this.iDashboardDao.platformDetails(productID);
			List<Object[]> results = (List<Object[]>) response.getData();
			List<PlatformBean> listPlatform = new ArrayList<PlatformBean>();
			for (Object[] object : results) 
				listPlatform.add(new PlatformBean(String.valueOf(object[0]), (int)object[1]));
			jsonResponse = new JsonResponse<PlatformBean>("Success", listPlatform, listPlatform.size());
			logger.debug("platformsApi Data successfully retrieved.");
		} catch (Exception e) {
			jsonResponse = new JsonResponse<PlatformBean>("ERROR", e.getMessage());
			logger.error("platformsApi error while retrieving data ", e);
		}
		return jsonResponse;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public JsonResponse<ViewsClicksBean> viewClicksApi(String start_date,String end_date,int action, Integer productID) {
		JsonResponse<ViewsClicksBean> jsonResponse = new JsonResponse<ViewsClicksBean>();
		try {
			if (action == 1) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -2);
				end_date = Utility.getDate(Constants.DATE_FORMAT, -1);
			} else if (action == 2) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -6);
				end_date = Utility.getDate(Constants.DATE_FORMAT, 0);
			}
			else if ((!Utility.isThisDateValid(start_date, Constants.DATE_FORMAT)) || (!Utility.isThisDateValid(end_date, Constants.DATE_FORMAT))) {
				if(this.logger.isDebugEnabled())
					this.logger.debug("totalViewClickApi, start_date->[" + start_date + "], end_date->[" + end_date + "], INVALID DATE Format");
				jsonResponse = new JsonResponse<ViewsClicksBean>("ERROR", "INVALID DATE Format");
			}

			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date d1 = format.parse(start_date);
			Date d2 = format.parse(end_date);

			List<ViewsClicksBean> listViewClickBean = new ArrayList<ViewsClicksBean>();
			Response totalViews=this.iDashboardDao.getViews(d1, d2, productID);
			Response totalClicks=this.iDashboardDao.getClicks(d1, d2, productID);
			List<ViewsClicksBean> listViewClickBean1=new ArrayList<>();
			LinkedHashMap<String, ArrayList<Integer>> finalmap=Utility.datesInRangeForViewsClicks(start_date, end_date);
			if(totalViews.getStatus() && totalClicks.getStatus())
			{
				listViewClickBean = Utility.mergeClickView(totalClicks, totalViews, action);

				for(int i=0;i<listViewClickBean.size();i++)
				{
					finalmap.put(listViewClickBean.get(i).getDate(), new ArrayList<Integer>(Arrays.asList(listViewClickBean.get(i).getViewHits(), listViewClickBean.get(i).getClickHits())));

				}

			}	
			ArrayList<Integer> arrayList=new ArrayList<>();
			Iterator itr=finalmap.entrySet().iterator();
			while(itr.hasNext())
			{
				@SuppressWarnings("unchecked")
				Map.Entry<String, ArrayList<Integer>> me=(Entry<String, ArrayList<Integer>>) itr.next();
				arrayList=me.getValue();
				listViewClickBean1.add(new ViewsClicksBean(me.getKey(), arrayList.get(0), arrayList.get(1)));

			}
			jsonResponse = new JsonResponse<ViewsClicksBean>("Success", listViewClickBean1, listViewClickBean1.size());
		} catch (Exception e) {
			this.logger.error("ViewsClicksApi, error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<ViewsClicksBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public JsonResponse<ViewsClicksBean> viewClicksHourApi(String start_date, String end_date, int action,  Integer productID) {
		JsonResponse<ViewsClicksBean> jsonResponse = new JsonResponse<ViewsClicksBean>();
		try {
			if (action == 1) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -2);
				end_date = Utility.getDate(Constants.DATE_FORMAT, -1);
			} else if (action == 2) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -6);
				end_date = Utility.getDate(Constants.DATE_FORMAT, 0);
			}
			else if ((!Utility.isThisDateValid(start_date, Constants.DATE_FORMAT)) || (!Utility.isThisDateValid(end_date, Constants.DATE_FORMAT))) {
				if(this.logger.isDebugEnabled())
					this.logger.debug("totalViewClickHourWiseApi start_date->[" + start_date + "], end_date->[" + end_date + "], INVALID DATE Format");
				jsonResponse = new JsonResponse<ViewsClicksBean>("ERROR", "INVALID DATE Format");
			}
			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date d1 = format.parse(start_date);
			Date d2 = format.parse(end_date);
			List<ViewsClicksBean> listViewClickBean = new ArrayList<ViewsClicksBean>();
			Response totalViews=this.iDashboardDao.getHourlyViews(d1, d2, productID);
			Response totalClicks=this.iDashboardDao.getHourlyClicks(d1, d2, productID);
			List<ViewsClicksBean> listViewClickBean1=new ArrayList<>();
			LinkedHashMap<String, ArrayList<Integer>> finalmap=Utility.hourWise();
			listViewClickBean = Utility.mergeClickView(totalClicks, totalViews, 0);

			for(int i=0;i<listViewClickBean.size();i++)
			{
				finalmap.put(listViewClickBean.get(i).getDate(), new ArrayList<Integer>(Arrays.asList(listViewClickBean.get(i).getViewHits(), listViewClickBean.get(i).getClickHits())));

			}

			Iterator itr=finalmap.entrySet().iterator();
			ArrayList<Integer> arrayList=new ArrayList<>();
			while(itr.hasNext())
			{
				@SuppressWarnings("unchecked")
				Map.Entry<String, ArrayList<Integer>> me=(Entry<String, ArrayList<Integer>>) itr.next();
				arrayList=me.getValue();
				listViewClickBean1.add(new ViewsClicksBean(me.getKey(), arrayList.get(0), arrayList.get(1)));

			}
			jsonResponse = new JsonResponse<ViewsClicksBean>("Success", listViewClickBean1, listViewClickBean1.size());
		} catch (Exception e) {
			this.logger.error("ViewsClicksHourWiseApi, error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<ViewsClicksBean>("ERROR", e.getMessage());
		}
		return jsonResponse;

	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<GeoBean> geoApi(Integer productID) {
		JsonResponse<GeoBean> jsonResponse = null;
		try
		{ 
			Response response = this.iDashboardDao.findGeo(productID);
			Response platformResponse = this.iDashboardDao.findPlatformDetails();
			List<Object[]> platforms = (List<Object[]>) platformResponse.getData();
			Set<String> mapPlatforms = new HashSet<String>();
			for (Object[] objects : platforms) {
				mapPlatforms.add(String.valueOf(objects[1]));
			}
			Object listGeoBean = Utility.populateData(response, mapPlatforms);
			Collections.sort((List<GeoBean>)listGeoBean);
			jsonResponse = new JsonResponse<GeoBean>("Success", (List<GeoBean>)listGeoBean, ((List<GeoBean>)listGeoBean).size());
		} catch (Exception e) {
			this.logger.error("GeoApi, error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<GeoBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ISPBean> ispApi(Integer productID) {
		JsonResponse<ISPBean> jsonResponse = null;
		try
		{ 
			Response response = this.iDashboardDao.findIspDetails(productID);
			Response platformResponse = this.iDashboardDao.findPlatformDetails();
			List<Object[]> platforms = (List<Object[]>) platformResponse.getData();
			Set<String> mapPlatforms = new HashSet<String>();
			for (Object[] objects : platforms) {
				mapPlatforms.add(String.valueOf(objects[1]));
			}

			Object listGeoBean = Utility.populateISPData(response, mapPlatforms);
			Collections.sort((List<ISPBean>)listGeoBean);
			jsonResponse = new JsonResponse<ISPBean>("Success", (List<ISPBean>)listGeoBean, ((List<ISPBean>)listGeoBean).size());
		} catch (Exception e) {
			this.logger.error("IspApi, error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<ISPBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<TrendsBean> trendsApi(String start_date,String end_date,int action, Integer productID) {
		JsonResponse<TrendsBean> jsonResponse = new JsonResponse<TrendsBean>();
		Response response= new Response();
		try {
			if (action == 1) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -2);
				end_date = Utility.getDate(Constants.DATE_FORMAT, -1);
			} else if (action == 2) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -6);
				end_date = Utility.getDate(Constants.DATE_FORMAT, 0);
			}
			else if ((!Utility.isThisDateValid(start_date, Constants.DATE_FORMAT)) || (!Utility.isThisDateValid(end_date, Constants.DATE_FORMAT))) {
				if(this.logger.isDebugEnabled())
					this.logger.debug("trendsApi start_date->[" + start_date + "], end_date->[" + end_date + "], INVALID DATE Format");
				jsonResponse = new JsonResponse<TrendsBean>("ERROR", "INVALID DATE Format");
			}			
			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date d1 = format.parse(start_date);
			Date d2 = format.parse(end_date);

			response=this.iDashboardDao.getDateWiseTrendDetails(d1, d2, productID);
			List<Object[]> results = (List<Object[]>) response.getData();
			HashMap<String, TrendsBean> hm = Utility.fillTrend(start_date, end_date , format);

			List<TrendsBean> listTrendsBean = new ArrayList<>();
			this.logger.debug("trendsApi, response status is "+ response.getStatus() +" .");
			if(response.getStatus())
			{
				for (Object[] object : results) 
				{
					String date = format.format(object[0]);
					if(hm.containsKey(date))
					{
						if(String.valueOf(object[1]).equalsIgnoreCase("android"))
						{
							hm.get(date).setAndroid((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("chrome"))
						{
							hm.get(date).setChrome((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("opera"))
						{
							hm.get(date).setOpera((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("firefox"))
						{
							hm.get(date).setFirefox((int)object[2]);
						}
						else
						{
							hm.get(date).setSafari((int)object[2]);
						}
					}
					else
					{
						if(String.valueOf(object[1]).equalsIgnoreCase("android"))
						{
							hm.put(date, new TrendsBean(date,(int)object[2],0,0,0, 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("chrome"))
						{
							hm.put(date, new TrendsBean(date,0,(int)object[2],0,0, 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("opera"))
						{
							hm.put(date, new TrendsBean(date,0,0,0,(int)object[2], 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("firefox"))
						{
							hm.put(date, new TrendsBean(date,0,0,0, 0,(int)object[2]));
						}
						else
						{
							hm.put(date, new TrendsBean(date,0,0,(int)object[2],0, 0));
						}
					}
				}
			}
			for (Map.Entry<String, TrendsBean> entry : hm.entrySet())
			{
				listTrendsBean.add(new TrendsBean(entry.getKey(), entry.getValue().getAndroid(), entry.getValue().getChrome(), entry.getValue().getSafari(), entry.getValue().getOpera(), entry.getValue().getFirefox() ) );
			}

			logger.debug("TrendsApi listTrendBean size is [ "+listTrendsBean.size()+ " ]"); 
			logger.debug("TrendsApi listTrendBean content is [ "+ listTrendsBean+ " ]");
			jsonResponse = new JsonResponse<TrendsBean>("Success", listTrendsBean, listTrendsBean.size());		
		}
		catch (Exception e) {
			this.logger.error("TrendsApi, an error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<TrendsBean>("ERROR", e.getMessage());
		}
		return jsonResponse;

	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<TrendsBean> trendsHourApi(String start_date, String end_date, int action, Integer productID) {
		JsonResponse<TrendsBean> jsonResponse = new JsonResponse<TrendsBean>();
		Response response= new Response();
		try {
			if (action == 1) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -2);
				end_date = Utility.getDate(Constants.DATE_FORMAT, -1);
			} else if (action == 2) {
				start_date = Utility.getDate(Constants.DATE_FORMAT, -6);
				end_date = Utility.getDate(Constants.DATE_FORMAT, 0);
			}
			else if ((!Utility.isThisDateValid(start_date, Constants.DATE_FORMAT)) || (!Utility.isThisDateValid(end_date, Constants.DATE_FORMAT))) {
				if(this.logger.isDebugEnabled())
					this.logger.debug("totalViewClickApi start_date->[" + start_date + "], end_date->[" + end_date + "], INVALID DATE Format");
				jsonResponse = new JsonResponse<TrendsBean>("ERROR", "INVALID DATE Format");
			}
			DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date d1 = format.parse(start_date);
			Date d2 = format.parse(end_date);

			response = this.iDashboardDao.getHourWiseTrendDetails(d1, d2, productID);
			this.logger.debug("trendsHourApi, response status is "+ response.getStatus() +" .");
			List<Object[]> results = (List<Object[]>) response.getData();
			HashMap<String, TrendsBean> hm =Utility.hourWiseTrend();
			List<TrendsBean> listTrendsBean = new ArrayList<>();
			if(response.getStatus())
			{
				for (Object[] object : results) 
				{
					if(hm.containsKey(object[0].toString()))
					{
						if(String.valueOf(object[1]).equalsIgnoreCase("android"))
						{
							hm.get(object[0].toString()).setAndroid((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("chrome"))
						{
							hm.get(object[0].toString()).setChrome((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("opera"))
						{
							hm.get(object[0].toString()).setOpera((int)object[2]);
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("firefox"))
						{
							hm.get(object[0].toString()).setFirefox((int)object[2]);
						}
						else
						{
							hm.get(object[0].toString()).setSafari((int)object[2]);
						}
					}
					else
					{
						if(String.valueOf(object[1]).equalsIgnoreCase("android"))
						{
							hm.put(object[0].toString(), new TrendsBean(object[0].toString(),(int)object[2],0,0,0, 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("chrome"))
						{
							hm.put(object[0].toString(), new TrendsBean(object[0].toString(),0,(int)object[2],0,0, 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("opera"))
						{
							hm.put(object[0].toString(), new TrendsBean(object[0].toString(),0,0,0,(int)object[2], 0));
						}
						else if(String.valueOf(object[1]).equalsIgnoreCase("firefox"))
						{
							hm.put(object[0].toString(), new TrendsBean(object[0].toString(),0,0,0, 0,(int)object[2]));
						}
						else
						{
							hm.put(object[0].toString(), new TrendsBean(object[0].toString(),0,0,(int)object[2],0, 0));
						}
					}
				}
			}
			for (Map.Entry<String, TrendsBean> entry : hm.entrySet())
			{
				listTrendsBean.add(new TrendsBean(entry.getKey(), entry.getValue().getAndroid(), entry.getValue().getChrome(), entry.getValue().getSafari(), entry.getValue().getOpera(), entry.getValue().getFirefox() ) );
			}

			jsonResponse = new JsonResponse<TrendsBean>("Success", listTrendsBean, listTrendsBean.size());			
		}
		catch (Exception e) {
			this.logger.error("TrendsHourWiseApi, an error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<TrendsBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<PlanBean> planApi(String  user, int prodId) {
		this.logger.info("inside planApi"); 

	  JsonResponse<PlanBean> jsonResponse = new JsonResponse<PlanBean>();
	  Response response =new Response();
	  try
	  {
	   List<PlanBean> listPreviewLastNotification = new ArrayList<PlanBean>();
	   response = this.iDashboardDao.PlanDetails(user, prodId);
	   this.logger.debug("planApi, response status is "+ response.getStatus() +" .");
	   if(response.getStatus())
	   {
	     Object[] obj =  (Object[]) response.getScalarResult() ;
	     IopushUsercategory  iopushUsercategory = (IopushUsercategory) obj[0] ;
	     IopushPlan userPlan=(IopushPlan) obj[1];
	     int balance =0;
	     if(iopushUsercategory.getBalance() < 0)
	    	  balance = 0;
	     else balance = iopushUsercategory.getBalance()  ;
	     listPreviewLastNotification.add(new PlanBean(userPlan.getPlanName(), iopushUsercategory.getTotal(), balance, iopushUsercategory.getUsed()));

	   }
	   jsonResponse = new JsonResponse<PlanBean>("Success", listPreviewLastNotification, listPreviewLastNotification.size());
	  } catch (Exception e) {
	   this.logger.error("planApi, an error occured while retrieving data. " , e);
	   jsonResponse = new JsonResponse<PlanBean>("ERROR", e.getMessage());
	  }
	  return jsonResponse;
	 }
	 
	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<LastNotificationBean> lastNotificationApi(Integer productID) {
		JsonResponse<LastNotificationBean> jsonResponse = new JsonResponse<LastNotificationBean>();
		Response response =new Response();
		try
		{
			response = this.iDashboardDao.lastNotificationDetails(productID);
			this.logger.debug("lastNotificationApi, response status is "+ response.getStatus() +" .");
			List<LastNotificationBean> listPreviewLastNotification = new ArrayList<LastNotificationBean>();
			if(response.getStatus())
			{
				List<Object[]> results = (List<Object[]>) response.getData();

				for (Object[] object : results) 
					listPreviewLastNotification.add(new LastNotificationBean(String.valueOf(object[0]), String.valueOf(object[1]), (object[2]==null?0:(int)object[2]), (object[3]==null?0:(int)object[3]),(object[4]==null?0:(int)object[4])));

			}
			else
			{
				listPreviewLastNotification.add(new LastNotificationBean("", "", 0, 0, 0));
			}
			jsonResponse = new JsonResponse<LastNotificationBean>("Success", listPreviewLastNotification, listPreviewLastNotification.size());
		} catch (Exception e) {
			this.logger.error("lastNotificationApi, an error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<LastNotificationBean>("ERROR", e.getMessage());
		}
		return jsonResponse;

	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<PreviewLastNotificationBean> previewLastNotificationApi(Integer productID) {
		JsonResponse<PreviewLastNotificationBean> jsonResponse = new JsonResponse<PreviewLastNotificationBean>();
		Response response =new Response();
		String platform = "";
		int campaignId= 0;
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String imageurl = myProperties.getProperty(env + "IMAGEURL");
		try
		{
			response = this.iDashboardDao.previewLastNotificationDetails(productID);
			this.logger.debug("previewLastNotificationApi, response status is "+ response.getStatus() +" .");
			List<PreviewLastNotificationBean> listPreviewLastNotification = new ArrayList<PreviewLastNotificationBean>();
			if(response.getStatus())
			{
				List<Object[]> result = (List<Object[]>) response.getData();
				for (Object[] object : result) 
				{

					campaignId = (int)object[4];
					if(campaignId > 0)
					{
						response = this.iDashboardDao.fetchRules(campaignId);
						if(response.getStatus())
						{
							for(IopushRules iopushRules : (List<IopushRules>)response.getData()){
								if(iopushRules.getRuleType().equals("platform"))
								{
									platform = iopushRules.getRuleValue();
								}
							}
						}
						response = null;
					}
					if(platform.isEmpty())
					{

						List<IopushPlatformDetails> listPlatform = (List<IopushPlatformDetails>) iDashboardDao.listplatform().getData();
						for(IopushPlatformDetails iopushPlatformDetails : listPlatform)
						{
							platform += iopushPlatformDetails.getPlatformID()+",";

						}
						platform = platform.substring(0,platform.length()-1);
					}
					listPreviewLastNotification.add(new PreviewLastNotificationBean(String.valueOf(object[0]), String.valueOf(object[1]), String.valueOf(object[2]), imageurl+String.valueOf(object[3]),platform));
				}
			}
			else
			{
				listPreviewLastNotification.add(new PreviewLastNotificationBean("","","", "",""));
			}
			jsonResponse = new JsonResponse<PreviewLastNotificationBean>("Success", listPreviewLastNotification, 1);
		} catch (Exception e) {
			this.logger.error("previewLastNotificationApi, an error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<PreviewLastNotificationBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CityDashBoardBean> fetchCityApiNew(String country_code) {
		this.logger.info("fetchCityApiNew, geo code is [ "+country_code+" ]");
		JsonResponse<CityDashBoardBean> jsonResponse=null;
		int product = (int) httpSession.getAttribute("productId") ;

		Response browserresponse = this.iDashboardDao.findBrowsers();
		List<IopushPlatformDetails> browsers = (List<IopushPlatformDetails>) browserresponse.getData();
		Set<String> mapBrowser = new HashSet<String>();
		for (IopushPlatformDetails objects : browsers) {
			mapBrowser.add(objects.getPlatformName());
		}


		int country_id=0;
		try{
			Response response=iDashboardDao.findGeoCode(country_code);
			this.logger.debug("fetchCityApiNew, findGeocode response status is [ "+response.getStatus() +" ]");
			if(response.getStatus()){
				country_id=((IopushGeoDetails)response.getScalarResult()).getGeoId();
				response=this.iDashboardDao.findCityNew(country_id,product);
				boolean flag=country_code.equalsIgnoreCase("US")?true:false;
				List<CityDashBoardBean> listCityDashBoardBean=Utility.populateCityData(response,mapBrowser,flag);

				jsonResponse = new JsonResponse<CityDashBoardBean>(Constants.SUCCESS,listCityDashBoardBean,listCityDashBoardBean.size()); 
			}else{
				jsonResponse = new JsonResponse<CityDashBoardBean>(Constants.ERROR, "Geo Code Not Available");  
			}
		}catch(Exception e){
			this.logger.error("fetchCityApiNew, an error occured while retrieving data. " , e);
			jsonResponse = new JsonResponse<CityDashBoardBean>("ERROR", e.getMessage()+"  country_id::"+country_id);
		}
		return jsonResponse;
	}





}
