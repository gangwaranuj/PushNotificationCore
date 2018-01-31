package com.saphire.iopush.serviceImpl;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CampaignBean;
import com.saphire.iopush.bean.GeoCityBean;
import com.saphire.iopush.bean.GeoStatBean;
import com.saphire.iopush.bean.PlatformStatBean;
import com.saphire.iopush.bean.StatsModelBean;
import com.saphire.iopush.bean.SubCampaignBean;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.dao.ISegmentDao;
import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.model.IopushCityDetails;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.model.IopushSubCampaign;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.service.ICampaignService;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.SecurityUtils;
import com.saphire.iopush.utils.Utility;

@Service
public class CampaignServiceImpl implements ICampaignService {
	@Autowired ICampaignDao iCampaignDao;
	@Autowired Properties myProperties;
	@Autowired IExternalAPIDao iExternalDao;
	@Autowired JmsTemplate jmsTemplate;
	@Autowired ISegmentDao iSegmentDao;
	private Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

	@Override
	public String add() {
		IopushTimeZone iopushTimeZone=new IopushTimeZone();
		iopushTimeZone.setTimezoneID(1);
		String[] excludeProperty={"country_id"};
		Response response =this.iCampaignDao.fetch(iopushTimeZone, excludeProperty);
		if(response.getStatus())
		{
			logger.info("response data : "+response.getData().get(0));
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CampaignBean> pendingcampaign(CampaignBean campignBean, Integer productId) {
		logger.info("inside pendingcampaign method");
		logger.info("pending method campaignBean data [ "+campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		String timeZone = "", current_time_in_campaign_country = "";
		int c_id = 0, subCampaignsCount = 0;

		try {
			String country ;
			Date current_time_in_server_country = new Date(System.currentTimeMillis());
			int diffstart = 0;
			int diffend = 0;
			Response response = null;
			Response response1 = null;
			Response response3 = null;
			IopushTimeZone iopushTimeZone = new IopushTimeZone();

			IopushProduct iopushProduct=new IopushProduct();
			iopushProduct.setProductID(productId);
			IopushCampaign iopushCampaign = new IopushCampaign();
			iopushCampaign.setCampaignName(campignBean.getCampaign_name());
			iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
			iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
			if(campignBean.getCountries()==null || campignBean.getCountries().isEmpty())
			{
				//				int countCountries = 0;
				String subcountry = "";
				Response objResponse = iCampaignDao.listTimezone();
				logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
				if (objResponse.getStatus()) {
					for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
						//						countCountries++;
						c_id = objTimezoneDetail.getCountryId();
						subcountry += (Integer.toString(c_id)).concat(",");
					}
					campignBean.setCountries(subcountry);
					//					subCampaignsCount = countCountries;
				}
			}
			c_id = Utility.parseCountry(campignBean.getCountries());
			logger.info("pendingcampaign countryid-> " + c_id);

			if (c_id > 0 ) {
				subCampaignsCount = 0;
				response = iCampaignDao.findCountryTimeZone(c_id);
				timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
				iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				current_time_in_campaign_country = dtf.print(dt);
				logger.info("pendingcampaign current time in targeted country[ " +timeZone+"   ]["+ current_time_in_campaign_country+" ]");

				diffstart = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_schedule_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_end_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);

				iopushCampaign.setMultiple_country(false);
				iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffstart,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
				iopushCampaign.setCampaignEndDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffend,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setCreationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
				iopushCampaign.setModificationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));

			} else {

				// if countries contain multiple values
				String countries = campignBean.getCountries();
				if(countries.contains(","))
				{
					String[] arr = countries.split(",");
					subCampaignsCount = arr.length;
				}

				diffstart = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_schedule_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_end_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);

				iopushTimeZone.setTimezoneID(Utility.intConverter(myProperties.getProperty("DEFAULT_TIMEZONE_ID")));
				iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
				iopushCampaign.setCampaignEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setMultiple_country(true);
				iopushCampaign.setCreationDate(new Date());
				iopushCampaign.setModificationDate(new Date());

			}

			String forwardUrl = campignBean.getForwardUrl().toLowerCase().startsWith("www") ? "http://" + campignBean.getForwardUrl() : campignBean.getForwardUrl();
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(campignBean.getFile());
			String filename = iopushCampaign.getCampaignName() + "." + ext;
			String base64Image = campignBean.getFile().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();
			logger.info("pendingcampaign You successfully uploaded file=" + filename);



			iopushCampaign.setIopushTimeZone(iopushTimeZone);
			iopushCampaign.setIopushProduct(iopushProduct);
			iopushCampaign.setCampaignCurrentDate(campignBean.getCampaign_current_date());
			iopushCampaign.setCreatedBy(SecurityUtils.getCurrentLogin());

			iopushCampaign.setModificedBy(SecurityUtils.getCurrentLogin());
			iopushCampaign.setCampaignStatus(Constants.PENDING);
			iopushCampaign.setEligiblecount(campignBean.getEligiblecount());
			iopushCampaign.setSource(campignBean.getSource());
			iopushCampaign.setGeneric(campignBean.getGeneric());
			iopushCampaign.setDescription(campignBean.getDescription());
			iopushCampaign.setTitle(campignBean.getTitle());
			iopushCampaign.setForwardUrl(forwardUrl);
			iopushCampaign.setSegmented(campignBean.getIsSegmented());
			iopushCampaign.setImagePath(readnotificationPath + filename);
			iopushCampaign.setCampaign(campignBean.getCampaign());
			iopushCampaign.setRequireInteraction(campignBean.isRequireInteraction());
			iopushCampaign.setSubCampaignsCount(subCampaignsCount);
			iopushCampaign.setLive(0);
			iopushCampaign.setCampaignclick(0);
			iopushCampaign.setCampaignclose(0);
			iopushCampaign.setCampaignopen(0);
			iopushCampaign.setCampaignsent(0);
			iopushCampaign.setLargeImage(campignBean.isLargeImage());
			//			Banner Image
			if(campignBean.isLargeImage())
			{
				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(campignBean.getBannerImage());
				filename = iopushCampaign.getCampaignName() + "_banner." + banner;
				base64Image = campignBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushCampaign.setBannerImage(readBannerPath+filename);
			}

			logger.info("pendingcampaign Data[ " + iopushCampaign.toString()+" ]");
			response = iCampaignDao.saveCampaign(iopushCampaign);

			if (response.getStatus()) {
				campignBean.setCampaign_id(response.getIntegerResult());
				if (c_id > 0) {

				}
				else
				{ 
					if(!campignBean.getIsSegmented())
					{ 
						logger.info("campignBean.getIsSegmented() : "+campignBean.getIsSegmented());
						String subcountry = "";
						Response objResponse = iCampaignDao.listTimezone();
						logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
						if (objResponse.getStatus()) {
							for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
								c_id = objTimezoneDetail.getCountryId();
								subcountry += (Integer.toString(c_id)).concat(",");
							}
							campignBean.setCountries(subcountry);

						}
					}



					country =  campignBean.getCountries();
					if(country.contains(","))
					{
						List<String> countryList = Arrays.asList(country.split(","));
						String country_code;
						for(int i = 0;i<countryList.size();i++)
						{
							response3 = iCampaignDao.findCountryTimeZone(Utility.parseCountry(countryList.get(i)));
							response1 = iCampaignDao.findCountryCode(Utility.parseCountry(countryList.get(i)));
							country_code =  ((IopushGeoDetails) response1.getScalarResult()).getGeoCode();
							IopushSubCampaign iopushsubCampaign = new IopushSubCampaign();
							iopushsubCampaign.setCampaignName(campignBean.getCampaign_name().concat("_").concat(country_code));
							iopushsubCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
							iopushsubCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
							iopushsubCampaign.setCampaignStatus(Constants.PENDING);
							iopushsubCampaign.setIopushCampaign(iopushCampaign);
							iopushsubCampaign.setEligiblecount(campignBean.getEligiblecount());
							iopushsubCampaign.setGeoid(Utility.parseCountry(countryList.get(i)));
							timeZone = ((IopushTimeZone) response3.getScalarResult()).getTimezone();
							iopushTimeZone.setTimezoneID(((IopushTimeZone) response3.getScalarResult()).getTimezoneID());
							DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
							DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);


							///string to date conversion 
							DateFormat dtf1 = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
							Date scheduledate = dtf1.parse(campignBean.getCampaign_schedule_date());
							Date enddate = dtf1.parse(campignBean.getCampaign_end_date());

							iopushsubCampaign.setCampaignScheduleDateInEdt(scheduledate);
							iopushsubCampaign.setCampaignEndDateInEdt(enddate);	
							iopushsubCampaign.setIopushTimeZone(iopushTimeZone);
							//////////




							logger.info("PENDING  CampaignScheduleDateInEdt[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
							logger.info("PENDING campaign CampaignEndDateInEdt[ " + iopushCampaign.getCampaignEndDateInEdt()+" ]");
							Response response2 = null;
							response2 = iCampaignDao.saveSubCampaign(iopushsubCampaign);
						}
					}
				}

				///////// SAVE RULE

				IopushCampaign iopushCampaignbean = new IopushCampaign(); 
				iopushCampaignbean.setCampaignId(response.getIntegerResult());
				List<IopushRules> listIopushRules = new ArrayList<IopushRules>();


				if(campignBean.getIsSegmented())
				{
					if (!campignBean.getPlatform().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "platform", campignBean.getPlatform(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getCountries().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "country", campignBean.getCountries(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getCities().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "city", campignBean.getCities(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getIsps().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "ISP", campignBean.getIsps(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getSubscribed().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "subscribed", campignBean.getSubscribed(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

					if (!campignBean.getSegments().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "segments", campignBean.getSegments(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getSegmentTypes().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "segmentTypes", campignBean.getSegmentTypes(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				}

				logger.info("PENDINGCampaign Iopushrule[ " + listIopushRules.toString()+" ]");

				response = iCampaignDao.saveRules(listIopushRules);
				jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, campignBean);
				logger.info("PENDINGCampaign campaign saved successfully ");
			} else
			{
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, "Insertion Fail");
				logger.info("PENDINGCampaign insertion failed!");
			}
		} catch (Exception ex) {
			Throwable t = ex.getCause();
			if (t instanceof PSQLException) {
				logger.error(
						" PENDINGCampaign updateBrowser -> You are violating the constraint, Object cannot be deleted due to the record is already exist in other entities");
				jsonResponse = new JsonResponse<CampaignBean>("ERROR",
						"You are violating the constraint, Object cannot be deleted due to the record is already exist in other entities");
			} else {
				logger.error("PENDINGCampaign updateBrowser -> Object cannot be deleted due to following reason  " ,ex);
				jsonResponse = new JsonResponse<CampaignBean>("ERROR","Failed to save campaign");
			}
		}
		return jsonResponse;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CampaignBean> draftcampaign(CampaignBean campignBean, Integer productId) {
		logger.info("inside draftcampaign method");
		logger.info("draftcampaign method campaignBean data [ "+campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		String timeZone = "", current_time_in_campaign_country = "";
		int c_id = 0,  subCampaignsCount = 0;
		try {
			String country ;
			Date current_time_in_server_country = new Date(System.currentTimeMillis());
			int diffstart = 0;
			int diffend = 0;
			Response response = null;
			Response response1 = null;
			Response response3 = null;
			IopushTimeZone iopushTimeZone = new IopushTimeZone();

			IopushProduct iopushProduct=new IopushProduct();
			// productId =1;
			iopushProduct.setProductID(productId);
			IopushCampaign iopushCampaign = new IopushCampaign();
			iopushCampaign.setCampaignName(campignBean.getCampaign_name());
			if(campignBean.getCountries()==null || campignBean.getCountries().isEmpty())
			{
				String subcountry = "";
				Response objResponse = iCampaignDao.listTimezone();
				logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
				if (objResponse.getStatus()) {
					for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
						c_id = objTimezoneDetail.getCountryId();
						subcountry += (Integer.toString(c_id)).concat(",");
					}
					campignBean.setCountries(subcountry);

				}
			}
			c_id = Utility.parseCountry(campignBean.getCountries());
			logger.info("draftcampaign camapign countryid-> " + c_id);

			if (c_id > 0 ) {
				subCampaignsCount = 0;
				response = iCampaignDao.findCountryTimeZone(c_id);
				timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
				iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				current_time_in_campaign_country = dtf.print(dt);
				if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
				{
					iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
					iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
					logger.info("draftcampaign current time in targeted country[ " +timeZone+"   ]["+ current_time_in_campaign_country+" ]");
					diffstart = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_schedule_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					diffend = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_end_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					iopushCampaign.setMultiple_country(false);
					iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffstart,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setCampaignEndDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffend,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				}
				else
				{
					iopushCampaign.setCampaignScheduleDate("");
					iopushCampaign.setCampaignEndDate("");
					iopushCampaign.setCampaignScheduleDateInEdt(null);
					iopushCampaign.setCampaignScheduleDateInEdt(null);
				}
				iopushCampaign.setCreationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
				iopushCampaign.setModificationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
			} else {

				String countries = campignBean.getCountries();
				if(countries.contains(","))
				{
					String[] arr = countries.split(",");
					subCampaignsCount = arr.length;
				}
				iopushTimeZone.setTimezoneID(Utility.intConverter(myProperties.getProperty("DEFAULT_TIMEZONE_ID")));
				if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
				{
					diffstart = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_schedule_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					diffend = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_end_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setCampaignEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setMultiple_country(true);
					logger.info("draftcampaign CampaignStartDateInEDT[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
					logger.info("draftcampaign CampaignEndtDateInEDT[ " + iopushCampaign.getCampaignEndDateInEdt()+" ]");
				}
				else
				{
					iopushCampaign.setCampaignScheduleDate("");
					iopushCampaign.setCampaignEndDate("");
					iopushCampaign.setCampaignScheduleDateInEdt(null);
					iopushCampaign.setCampaignScheduleDateInEdt(null);
				}
				iopushCampaign.setCreationDate(new Date());
				iopushCampaign.setModificationDate(new Date());
			}

			String forwardUrl = campignBean.getForwardUrl().toLowerCase().startsWith("www") ? "http://" + campignBean.getForwardUrl() : campignBean.getForwardUrl();
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(campignBean.getFile());
			String filename = iopushCampaign.getCampaignName() + "." + ext;
			String base64Image = campignBean.getFile().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();
			logger.info("draftcampaign You successfully uploaded file=" + filename);
			iopushCampaign.setIopushTimeZone(iopushTimeZone);
			iopushCampaign.setIopushProduct(iopushProduct);
			iopushCampaign.setCampaignCurrentDate(campignBean.getCampaign_current_date());
			iopushCampaign.setCreatedBy("");
			iopushCampaign.setModificedBy("");
			iopushCampaign.setCampaignStatus(Constants.DRAFT);
			iopushCampaign.setEligiblecount(campignBean.getEligiblecount());
			iopushCampaign.setSource(campignBean.getSource());
			iopushCampaign.setGeneric(campignBean.getGeneric());
			iopushCampaign.setDescription(campignBean.getDescription());
			iopushCampaign.setTitle(campignBean.getTitle());
			iopushCampaign.setForwardUrl(forwardUrl);
			iopushCampaign.setSegmented(campignBean.getIsSegmented());
			iopushCampaign.setImagePath(readnotificationPath + filename);
			iopushCampaign.setCampaign(campignBean.getCampaign());
			iopushCampaign.setRequireInteraction(campignBean.isRequireInteraction());
			iopushCampaign.setSubCampaignsCount(subCampaignsCount);
			iopushCampaign.setLive(0);
			iopushCampaign.setCampaignclick(0);
			iopushCampaign.setCampaignclose(0);
			iopushCampaign.setCampaignopen(0);
			iopushCampaign.setCampaignsent(0);
			iopushCampaign.setLargeImage(campignBean.isLargeImage());
			//			Banner Image
			if(campignBean.isLargeImage())
			{

				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(campignBean.getBannerImage());
				filename = iopushCampaign.getCampaignName() + "_banner." + banner;
				base64Image = campignBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushCampaign.setBannerImage(readBannerPath+filename);
			}
			logger.info("draftcampaign Data[ " + iopushCampaign.toString()+" ]");
			response = iCampaignDao.saveCampaign(iopushCampaign);

			if (response.getStatus()) {
				campignBean.setCampaign_id(response.getIntegerResult());
				if (c_id > 0) {

				}
				else
				{ 
					if(!campignBean.getIsSegmented())
					{ 
						logger.info("campignBean.getIsSegmented() : "+campignBean.getIsSegmented());
						String subcountry = "";
						Response objResponse = iCampaignDao.listTimezone();
						logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
						if (objResponse.getStatus()) {
							for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
								c_id = objTimezoneDetail.getCountryId();
								subcountry += (Integer.toString(c_id)).concat(",");
							}
							campignBean.setCountries(subcountry);

						}
					}



					country =  campignBean.getCountries();
					if(country.contains(","))
					{
						List<String> countryList = Arrays.asList(country.split(","));
						String country_code;
						for(int i = 0;i<countryList.size();i++)
						{
							response3 = iCampaignDao.findCountryTimeZone(Utility.parseCountry(countryList.get(i)));
							response1 = iCampaignDao.findCountryCode(Utility.parseCountry(countryList.get(i)));
							country_code =  ((IopushGeoDetails) response1.getScalarResult()).getGeoCode();
							IopushSubCampaign iopushsubCampaign = new IopushSubCampaign();
							iopushsubCampaign.setCampaignName(campignBean.getCampaign_name().concat("_").concat(country_code));
							iopushsubCampaign.setCampaignStatus(Constants.DRAFT);
							iopushsubCampaign.setIopushCampaign(iopushCampaign);
							iopushsubCampaign.setEligiblecount(campignBean.getEligiblecount());
							iopushsubCampaign.setGeoid(Utility.parseCountry(countryList.get(i)));
							timeZone = ((IopushTimeZone) response3.getScalarResult()).getTimezone();
							iopushTimeZone.setTimezoneID(((IopushTimeZone) response3.getScalarResult()).getTimezoneID());
							///string to date conversion 
							if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
							{

								iopushsubCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
								iopushsubCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
								DateFormat dtf1 = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
								Date scheduledate = dtf1.parse(campignBean.getCampaign_schedule_date());
								Date enddate = dtf1.parse(campignBean.getCampaign_end_date());
								iopushsubCampaign.setCampaignScheduleDateInEdt(scheduledate);
								iopushsubCampaign.setCampaignEndDateInEdt(enddate);	
							}
							else
							{
								iopushsubCampaign.setCampaignScheduleDate("");
								iopushsubCampaign.setCampaignEndDate("");
								iopushsubCampaign.setCampaignScheduleDateInEdt(null);
								iopushsubCampaign.setCampaignScheduleDateInEdt(null);
							}
							iopushsubCampaign.setIopushTimeZone(iopushTimeZone);
							iCampaignDao.saveSubCampaign(iopushsubCampaign);
						}
					}
				}

				///////// SAVE RULE

				IopushCampaign iopushCampaignbean = new IopushCampaign(); 
				iopushCampaignbean.setCampaignId(response.getIntegerResult());
				List<IopushRules> listIopushRules = new ArrayList<IopushRules>();


				if(campignBean.getIsSegmented())
				{
					if (!campignBean.getPlatform().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "platform", campignBean.getPlatform(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getCountries().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "country", campignBean.getCountries(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getCities().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "city", campignBean.getCities(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getIsps().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "ISP", campignBean.getIsps(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

					if (!campignBean.getSubscribed().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "subscribed", campignBean.getSubscribed(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getSegments().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "segments", campignBean.getSegments(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
					if (!campignBean.getSegmentTypes().equals(""))
						listIopushRules.add(new IopushRules(iopushCampaignbean, "segmentTypes", campignBean.getSegmentTypes(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				}

				logger.info("draftcampaign Iopushrule[ " + listIopushRules.toString()+" ]");

				response = iCampaignDao.saveRules(listIopushRules);
				jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, campignBean);
				logger.info("draftcampaign campaign saved successfully ");
			} else
			{
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, "Insertion Fail");
				logger.info("draftcampaign insertion failed!");
			}
		} catch (Exception ex) {
			Throwable t = ex.getCause();
			if (t instanceof PSQLException) {
				logger.error(
						" draftcampaign updateBrowser -> You are violating the constraint, Object cannot be deleted due to the record is already exist in other entities");
				jsonResponse = new JsonResponse<CampaignBean>("ERROR",
						"You are violating the constraint, Object cannot be deleted due to the record is already exist in other entities");
			} else {
				logger.error("draftcampaign updateBrowser -> Object cannot be deleted due to following reason  " ,ex);
				jsonResponse = new JsonResponse<CampaignBean>("ERROR","Failed to save campaign");
			}
		}
		return jsonResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CampaignBean> updatedraft(CampaignBean campignBean, Integer productId) {
		logger.info("inside UpdateCampaign method");
		logger.info("updateCampaign CampaignBean data [ "+campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		try {
			String timeZone = "", current_time_in_campaign_country = "";	String country ;
			Date current_time_in_server_country = new Date(System.currentTimeMillis());
			int diffstart = 0;
			int diffend = 0;
			int c_id = 0, subCampaignsCount = 0;
			// Find Time Zone timezoneId
			Response response = null;
			Response response1 = null;
			int camapaign_id = campignBean.getCampaign_id();
			response = iCampaignDao.findCampaignByID(camapaign_id);
			IopushCampaign iopushCampaign = (IopushCampaign) response.getScalarResult();
			IopushTimeZone iopushTimeZone = new IopushTimeZone();
			String forwardUrl = campignBean.getForwardUrl().toLowerCase().startsWith("www") ? "http://" + campignBean.getForwardUrl() : campignBean.getForwardUrl();
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(campignBean.getFile());
			String filename = iopushCampaign.getCampaignName() + "." + ext;
			String base64Image = campignBean.getFile().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();
			logger.info("save campaignYou successfully uploaded file=" + filename);

			if (iopushCampaign.getCampaignStatus() == Constants.LIVE) {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, "Campaign has been already launched");
				logger.info("launchCampaign campignid[" + camapaign_id + "] Campaign has been already launched");
				return jsonResponse;
			}

			if(campignBean.getCountries()==null || campignBean.getCountries().isEmpty())
			{
				String subcountry = "";
				Response objResponse = iCampaignDao.listTimezone();
				logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
				if (objResponse.getStatus()) {
					for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
						c_id = objTimezoneDetail.getCountryId();
						subcountry += (Integer.toString(c_id)).concat(",");
					}
					campignBean.setCountries(subcountry);

				}
			}

			c_id = Utility.parseCountry(campignBean.getCountries());
			logger.debug("update camapign countryid[ " + c_id+" ]");

			if (c_id > 0) {
				subCampaignsCount = 0;
				response = iCampaignDao.findCountryTimeZone(c_id);
				timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
				iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				current_time_in_campaign_country = dtf.print(dt);
				if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
				{

					logger.info("draftcampaign current time in targeted country[ " +timeZone+"   ]["+ current_time_in_campaign_country+" ]");
					diffstart = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_schedule_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					diffend = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_end_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					iopushCampaign.setMultiple_country(false);
					iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffstart,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setCampaignEndDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffend,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					logger.info("draftcampaign CampaignScheduleDateInEdt[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
					logger.info("draftcampaign CampaignEndDateInEdt[ " + iopushCampaign.getCampaignEndDateInEdt()+" ]");
				}
				else
				{
					iopushCampaign.setCampaignScheduleDate("");
					iopushCampaign.setCampaignEndDate("");
					iopushCampaign.setCampaignScheduleDateInEdt(null);
					iopushCampaign.setCampaignEndDateInEdt(null);
				}
				iopushCampaign.setCreationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
				iopushCampaign.setModificationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
				/*subCampaignsCount = 0;
				response = iCampaignDao.findCountryTimeZone(c_id);
				iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
				timeZone=((IopushTimeZone) response.getScalarResult()).getTimezone();

				logger.info("update campaign Campaign_current_timeZone[ " + timeZone+" ]");
					DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				current_time_in_campaign_country = dtf.print(dt);
				logger.info("update campaign current time in targeted country[ " + current_time_in_campaign_country+" ]");
				if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
				{
				iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
				iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
				}
				iopushCampaign.setMultiple_country(false);
				logger.info("update campaign CampaignStartDateInEDT[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");*/

			} else {

				String countries = campignBean.getCountries();
				if(countries.contains(","))
				{
					String[] arr = countries.split(",");
					subCampaignsCount = arr.length;
				}
				iopushTimeZone.setTimezoneID(Utility.intConverter(myProperties.getProperty("DEFAULT_TIMEZONE_ID")));
				if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
				{
					diffstart = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_schedule_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					diffend = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_end_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
					iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setCampaignEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
					iopushCampaign.setMultiple_country(true);
					logger.info("draftcampaign CampaignStartDateInEDT[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
					logger.info("draftcampaign CampaignEndtDateInEDT[ " + iopushCampaign.getCampaignEndDateInEdt()+" ]");
				}
				else
				{
					iopushCampaign.setCampaignScheduleDateInEdt(null);
					iopushCampaign.setCampaignEndDateInEdt(null);
					iopushCampaign.setCampaignScheduleDate("");
					iopushCampaign.setCampaignEndDate("");
				}
				iopushCampaign.setCreationDate(new Date());
				iopushCampaign.setModificationDate(new Date());
				/*String countries = campignBean.getCountries();
				if(countries.contains(","))
				{
					String[] arr = countries.split(",");
					subCampaignsCount = arr.length;
				}
				diffstart = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_schedule_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_end_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);

				iopushTimeZone.setTimezoneID(Utility.intConverter(myProperties.getProperty("DEFAULT_TIMEZONE_ID")));


				iopushCampaign.setMultiple_country(true);


				iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
				//				iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
				//			iopushCampaign.setCampaignEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));


				//		logger.info("save campaign CampaignStartDateInEDT[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
				 */
			}
			IopushProduct iopushProduct=new IopushProduct();
			iopushProduct.setProductID(productId);
			iopushCampaign.setIopushTimeZone(iopushTimeZone);
			iopushCampaign.setCampaignName(campignBean.getCampaign_name());
			iopushCampaign.setIopushProduct(iopushProduct);
			iopushCampaign.setCampaignCurrentDate(campignBean.getCampaign_current_date());
			iopushCampaign.setCreatedBy(SecurityUtils.getCurrentLogin());
			iopushCampaign.setModificedBy(SecurityUtils.getCurrentLogin());

			iopushCampaign.setCampaignStatus(Constants.DRAFT);
			iopushCampaign.setEligiblecount(campignBean.getEligiblecount());
			iopushCampaign.setSource(campignBean.getSource());
			iopushCampaign.setGeneric(campignBean.getGeneric());
			iopushCampaign.setDescription(campignBean.getDescription());
			iopushCampaign.setTitle(campignBean.getTitle());
			iopushCampaign.setForwardUrl(forwardUrl);
			iopushCampaign.setSegmented(campignBean.getIsSegmented());
			iopushCampaign.setImagePath(readnotificationPath + filename);
			iopushCampaign.setCampaign(campignBean.getCampaign());
			iopushCampaign.setRequireInteraction(campignBean.isRequireInteraction());
			iopushCampaign.setSubCampaignsCount(subCampaignsCount);
			iopushCampaign.setLive(0);
			iopushCampaign.setCampaignclick(0);
			iopushCampaign.setCampaignclose(0);
			iopushCampaign.setCampaignopen(0);
			iopushCampaign.setCampaignsent(0);
			iopushCampaign.setLargeImage(campignBean.isLargeImage());
			//			Banner Image
			if(campignBean.isLargeImage())
			{
				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(campignBean.getBannerImage());
				filename = iopushCampaign.getCampaignName() + "_banner." + banner;
				base64Image = campignBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushCampaign.setBannerImage(readBannerPath+filename);
			}

			logger.info("updateCampaign Campaign[ " + campignBean.toString()+" ]");
			response = iCampaignDao.updateCampaign(iopushCampaign);

			iCampaignDao.deleteSubCampaign(camapaign_id);
			if (c_id > 0) {

			}
			else
			{	
				if(!campignBean.getIsSegmented())
				{	
					logger.info("campignBean.getIsSegmented() : "+campignBean.getIsSegmented());
					String subcountry = "";
					Response objResponse = iCampaignDao.listTimezone();
					logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
					if (objResponse.getStatus()) {
						for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
							c_id = objTimezoneDetail.getCountryId();
							subcountry += (Integer.toString(c_id)).concat(",");
						}
						campignBean.setCountries(subcountry);

					}
				}

				country =  campignBean.getCountries();
				if(country.contains(","))
				{
					List<String> countryList = Arrays.asList(country.split(","));
					String country_code;
					for(int i = 0;i<countryList.size();i++)
					{
						response = iCampaignDao.findCountryTimeZone(Utility.parseCountry(countryList.get(i)));
						response1 = iCampaignDao.findCountryCode(Utility.parseCountry(countryList.get(i)));
						country_code =  ((IopushGeoDetails) response1.getScalarResult()).getGeoCode();
						IopushSubCampaign iopushsubCampaign = new IopushSubCampaign();
						iopushsubCampaign.setCampaignName(campignBean.getCampaign_name().concat("_").concat(country_code));
						iopushsubCampaign.setCampaignStatus(Constants.DRAFT);
						iopushsubCampaign.setIopushCampaign(iopushCampaign);
						iopushsubCampaign.setEligiblecount(campignBean.getEligiblecount());
						iopushsubCampaign.setGeoid(Utility.parseCountry(countryList.get(i)));
						timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
						iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
						iopushsubCampaign.setIopushTimeZone(iopushTimeZone);
						if(campignBean.getCampaign_schedule_date()!=null && campignBean.getCampaign_end_date()!=null && !campignBean.getCampaign_schedule_date().isEmpty() && !campignBean.getCampaign_end_date().isEmpty())
						{

							iopushsubCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
							iopushsubCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
							DateFormat dtf1 = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
							Date scheduledate = dtf1.parse(campignBean.getCampaign_schedule_date());
							Date enddate = dtf1.parse(campignBean.getCampaign_end_date());
							iopushsubCampaign.setCampaignScheduleDateInEdt(scheduledate);
							iopushsubCampaign.setCampaignEndDateInEdt(enddate);	
						}
						else
						{
							iopushsubCampaign.setCampaignScheduleDate("");
							iopushsubCampaign.setCampaignEndDate("");
							iopushsubCampaign.setCampaignScheduleDateInEdt(null);
							iopushsubCampaign.setCampaignScheduleDateInEdt(null);
						}
						 iCampaignDao.saveSubCampaign(iopushsubCampaign);
					}
				}
			}



			iCampaignDao.deleteCampaignRule(camapaign_id);

			// Insert Rule
			IopushCampaign iopushCampaignbean = new IopushCampaign();
			iopushCampaignbean.setCampaignId(camapaign_id);

			List<IopushRules> listIopushRules = new ArrayList<IopushRules>();

			if(campignBean.getIsSegmented())
			{
				if (!campignBean.getPlatform().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "platform", campignBean.getPlatform(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getCountries().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "country", campignBean.getCountries(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getCities().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "city", campignBean.getCities(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getIsps().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "ISP", campignBean.getIsps(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

				if (!campignBean.getSubscribed().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "subscribed", campignBean.getSubscribed(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getSegments().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "segments", campignBean.getSegments(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getSegmentTypes().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "segmentTypes", campignBean.getSegmentTypes(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
			}

			//////////////////
			logger.info("updateCampaign insertCampaignRule [ " + campignBean.toString()+" ]");
			response = iCampaignDao.saveRules(listIopushRules);

			jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "Successfully Updated");
			logger.info("updateCampaign Successfully Updated [ " + campignBean.toString()+" ]");
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("updateCampaign Update Error " , e);
		}
		return jsonResponse;



	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CampaignBean> updatepending(CampaignBean campignBean, Integer productId) {
		logger.info("inside UpdateCampaign method");
		logger.info("updateCampaign CampaignBean data [ "+campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		try {
			String timeZone = "", current_time_in_campaign_country = "";	String country ;
			Date current_time_in_server_country = new Date(System.currentTimeMillis());
			int diffstart = 0;
			int diffend = 0;
			int c_id = 0, subCampaignsCount = 0;
			// Find Time Zone timezoneId
			Response response = null;
			Response response1 = null;
			int camapaign_id = campignBean.getCampaign_id();
			response = iCampaignDao.findCampaignByID(camapaign_id);
			IopushCampaign iopushCampaign = (IopushCampaign) response.getScalarResult();
			IopushTimeZone iopushTimeZone = new IopushTimeZone();
			String forwardUrl = campignBean.getForwardUrl().toLowerCase().startsWith("www") ? "http://" + campignBean.getForwardUrl() : campignBean.getForwardUrl();
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(campignBean.getFile());
			String filename = iopushCampaign.getCampaignName() + "." + ext;
			String base64Image = campignBean.getFile().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();
			logger.info("save campaignYou successfully uploaded file=" + filename);

			if (iopushCampaign.getCampaignStatus() == Constants.LIVE) {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, "Campaign has been already launched");
				logger.info("launchCampaign campignid[" + camapaign_id + "] Campaign has been already launched");
				return jsonResponse;
			}

			if(campignBean.getCountries()==null || campignBean.getCountries().isEmpty())
			{
				String subcountry = "";
				Response objResponse = iCampaignDao.listTimezone();
				logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
				if (objResponse.getStatus()) {
					for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
						c_id = objTimezoneDetail.getCountryId();
						subcountry += (Integer.toString(c_id)).concat(",");
					}
					campignBean.setCountries(subcountry);

				}
			}

			c_id = Utility.parseCountry(campignBean.getCountries());
			logger.debug("update camapign countryid[ " + c_id+" ]");

			if (c_id > 0) {
				subCampaignsCount = 0;

				response = iCampaignDao.findCountryTimeZone(c_id);
				iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
				timeZone=((IopushTimeZone) response.getScalarResult()).getTimezone();
				logger.info("update campaign Campaign_current_timeZone[ " + timeZone+" ]");
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				current_time_in_campaign_country = dtf.print(dt);
				logger.info("update campaign current time in targeted country[ " + current_time_in_campaign_country+" ]");

				diffstart = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_schedule_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(current_time_in_campaign_country, campignBean.getCampaign_end_date(),Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);

				iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());

				iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffstart,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
				iopushCampaign.setCampaignEndDateInEdt(Utility.addMinInDate(current_time_in_server_country, diffend,Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
				iopushCampaign.setMultiple_country(false);

				iopushCampaign.setCreationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));
				iopushCampaign.setModificationDate(new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS).parse(current_time_in_campaign_country));

			} else {
				String countries = campignBean.getCountries();
				if(countries.contains(","))
				{
					String[] arr = countries.split(",");
					subCampaignsCount = arr.length;
				}


				diffstart = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_schedule_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(campignBean.getCampaign_current_date(),campignBean.getCampaign_end_date(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);

				iopushTimeZone.setTimezoneID(Utility.intConverter(myProperties.getProperty("DEFAULT_TIMEZONE_ID")));


				iopushCampaign.setMultiple_country(true);


				iopushCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
				iopushCampaign.setCampaignScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
				iopushCampaign.setCampaignEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

				iopushCampaign.setCreationDate(new Date());
				iopushCampaign.setModificationDate(new Date());
			}
			IopushProduct iopushProduct=new IopushProduct();
			iopushProduct.setProductID(productId);
			iopushCampaign.setIopushTimeZone(iopushTimeZone);
			iopushCampaign.setIopushProduct(iopushProduct);
			iopushCampaign.setCampaignCurrentDate(campignBean.getCampaign_current_date());
			iopushCampaign.setCreatedBy(SecurityUtils.getCurrentLogin());
			iopushCampaign.setModificedBy(SecurityUtils.getCurrentLogin());
			iopushCampaign.setCampaignStatus(Constants.PENDING);
			iopushCampaign.setEligiblecount(campignBean.getEligiblecount());
			iopushCampaign.setSource(campignBean.getSource());
			iopushCampaign.setGeneric(campignBean.getGeneric());
			iopushCampaign.setDescription(campignBean.getDescription());
			iopushCampaign.setTitle(campignBean.getTitle());
			iopushCampaign.setForwardUrl(forwardUrl);
			iopushCampaign.setSegmented(campignBean.getIsSegmented());
			iopushCampaign.setImagePath(readnotificationPath + filename);
			iopushCampaign.setCampaign(campignBean.getCampaign());
			iopushCampaign.setRequireInteraction(campignBean.isRequireInteraction());
			iopushCampaign.setSubCampaignsCount(subCampaignsCount);
			iopushCampaign.setLive(0);
			iopushCampaign.setCampaignclick(0);
			iopushCampaign.setCampaignclose(0);
			iopushCampaign.setCampaignopen(0);
			iopushCampaign.setCampaignsent(0);
			iopushCampaign.setLargeImage(campignBean.isLargeImage());
			//			Banner Image
			if(campignBean.isLargeImage())
			{
				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(campignBean.getBannerImage());
				filename = iopushCampaign.getCampaignName() + "_banner." + banner;
				base64Image = campignBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushCampaign.setBannerImage(readBannerPath+filename);
			}

			logger.info("updateCampaign Campaign[ " + campignBean.toString()+" ]");
			response = iCampaignDao.updateCampaign(iopushCampaign);

			iCampaignDao.deleteSubCampaign(camapaign_id);
			if (c_id > 0) {

			}
			else
			{	
				if(!campignBean.getIsSegmented())
				{	
					logger.info("campignBean.getIsSegmented() : "+campignBean.getIsSegmented());
					String subcountry = "";
					Response objResponse = iCampaignDao.listTimezone();
					logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
					if (objResponse.getStatus()) {
						for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
							c_id = objTimezoneDetail.getCountryId();
							subcountry += (Integer.toString(c_id)).concat(",");
						}
						campignBean.setCountries(subcountry);

					}
				}

				country =  campignBean.getCountries();
				if(country.contains(","))
				{
					List<String> countryList = Arrays.asList(country.split(","));
					String country_code;
					for(int i = 0;i<countryList.size();i++)
					{
						response = iCampaignDao.findCountryTimeZone(Utility.parseCountry(countryList.get(i)));
						response1 = iCampaignDao.findCountryCode(Utility.parseCountry(countryList.get(i)));
						country_code =  ((IopushGeoDetails) response1.getScalarResult()).getGeoCode();
						IopushSubCampaign iopushsubCampaign = new IopushSubCampaign();
						iopushsubCampaign.setCampaignName(campignBean.getCampaign_name().concat("_").concat(country_code));
						iopushsubCampaign.setCampaignScheduleDate(campignBean.getCampaign_schedule_date());
						iopushsubCampaign.setCampaignEndDate(campignBean.getCampaign_end_date());
						iopushsubCampaign.setCampaignStatus(Constants.PENDING);
						iopushsubCampaign.setIopushCampaign(iopushCampaign);
						iopushsubCampaign.setEligiblecount(campignBean.getEligiblecount());
						iopushsubCampaign.setGeoid(Utility.parseCountry(countryList.get(i)));
						timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
						iopushTimeZone.setTimezoneID(((IopushTimeZone) response.getScalarResult()).getTimezoneID());
						DateFormat dtf1 = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
						Date scheduledate = dtf1.parse(campignBean.getCampaign_schedule_date());
						Date enddate = dtf1.parse(campignBean.getCampaign_end_date());

						iopushsubCampaign.setCampaignScheduleDateInEdt(scheduledate);
						iopushsubCampaign.setCampaignEndDateInEdt(enddate);	
						iopushsubCampaign.setIopushTimeZone(iopushTimeZone);
						logger.info("save campaign CampaignScheduleDateInEdt[ " + iopushCampaign.getCampaignScheduleDateInEdt()+" ]");
						logger.info("save campaign CampaignEndDateInEdt[ " + iopushCampaign.getCampaignEndDateInEdt()+" ]");
						logger.info("status while save sub campaign [" + iCampaignDao.saveSubCampaign(iopushsubCampaign).getStatus() + "]");
					}
				}
			}



			iCampaignDao.deleteCampaignRule(camapaign_id);

			// Insert Rule
			IopushCampaign iopushCampaignbean = new IopushCampaign();
			iopushCampaignbean.setCampaignId(camapaign_id);

			List<IopushRules> listIopushRules = new ArrayList<IopushRules>();

			if(campignBean.getIsSegmented())
			{
				if (!campignBean.getPlatform().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "platform", campignBean.getPlatform(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getCountries().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "country", campignBean.getCountries(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getCities().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "city", campignBean.getCities(), new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getIsps().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "ISP", campignBean.getIsps(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

				if (!campignBean.getSubscribed().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "subscribed", campignBean.getSubscribed(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

				if (!campignBean.getSegments().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "segments", campignBean.getSegments(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));
				if (!campignBean.getSegmentTypes().equals(""))
					listIopushRules.add(new IopushRules(iopushCampaignbean, "segmentTypes", campignBean.getSegmentTypes(),new Date(),SecurityUtils.getCurrentLogin(), new Date(), SecurityUtils.getCurrentLogin()));

			}

			//////////////////
			logger.info("updateCampaign insertCampaignRule [ " + campignBean.toString()+" ]");
			response = iCampaignDao.saveRules(listIopushRules);

			jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "Successfully Updated");
			logger.info("updateCampaign Successfully Updated [ " + campignBean.toString()+" ]");
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("updateCampaign Update Error " , e);
		}
		return jsonResponse;



	}



	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<CampaignBean> list(CampaignBean campignBean) {

		logger.info("findCampaignList [ " + campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		Integer[] campstatus = Utility.getCampignStatus(campignBean);
		boolean flag = Utility.checkCampStatus(campstatus);
		boolean analytics = false;
		String searchCampignName = campignBean.getCampaign_name();
		//		String username = SecurityUtils.getCurrentLogin();

		if ("platform".equals(campignBean.getColumnForOrdering())) {

			int startIndex = campignBean.getStartIndex();
			int pagesize = campignBean.getPageSize();

			String order = campignBean.getRequiredOrder();

			try {
				long st = System.currentTimeMillis();

				int count = iCampaignDao.countCampaignsNew(campstatus, searchCampignName, startIndex, pagesize, flag,
						analytics, null, null, campignBean.getProductId());

				List<Object[]> listCampaigns = iCampaignDao.listCampaignsPlatforms(campstatus, searchCampignName,
						startIndex, pagesize, flag, analytics, null, null, campignBean.getProductId());
				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				String platformsConfiguration = myProperties.getProperty(env + "platforms", "1,2,3,4,5");
				String platforms = null;

				if (!listCampaigns.isEmpty()) {
					List<CampaignBean> listCampignToParse = new ArrayList<CampaignBean>();
					List<CampaignBean> listCampignFinal = new ArrayList<CampaignBean>();

					logger.info("Campaign List Total time to execute [ " + (System.currentTimeMillis() - st) + " ]");

					for (Object[] objects : listCampaigns) {// one row

						platforms = platformsConfiguration;

						Response response = iCampaignDao.getPlatforms(Utility.intConverter("" + objects[0]));
						if (response.getStatus()) {
							List<IopushRules> listRules = (List<IopushRules>) response.getData();
							for (IopushRules IopushRule : listRules) {
								if (IopushRule.getRuleType().equals("platform")) {
									platforms = IopushRule.getRuleValue();

									break;
								}

							}
						}
						// transform (1,2,3,4,5) to
						// (Chrome,Firefox,Opera,Safari,Android)
						platforms = Utility.convertIntToStr(platforms);
						// providing alphabetics order A-/C-2/F-3/O-4/S-5
						platforms = Utility.convertStrToSortedInt(platforms);
						listCampignToParse.add(new CampaignBean(objects, platforms));

					}

					if (order.equals("asc"))
						Collections.sort(listCampignToParse, new PlatformComparator());
					else
						Collections.sort(listCampignToParse, new PlatformComparator().reversed());

					List<CampaignBean> finalFormatList = new ArrayList<>();
					List<CampaignBean> oneList = new ArrayList<>();
					List<CampaignBean> twoList = new ArrayList<>();
					List<CampaignBean> threeList = new ArrayList<>();
					List<CampaignBean> fourList = new ArrayList<>();
					List<CampaignBean> fiveList = new ArrayList<>();
					Iterator<CampaignBean> itrList = listCampignToParse.iterator();

					while (itrList.hasNext()) {
						CampaignBean bean = itrList.next();
						String parts[] = getVersionParts(bean.getPlatform());
						if (parts.length == 1)
							oneList.add(bean);
						if (parts.length == 2)
							twoList.add(bean);
						if (parts.length == 3)
							threeList.add(bean);
						if (parts.length == 4)
							fourList.add(bean);
						if (parts.length == 5)
							fiveList.add(bean);

					}

					if (order.equals("asc")) {
						if (!oneList.isEmpty()) {
							Collections.sort(oneList, new IdComparator());
							finalFormatList.addAll(oneList);
						}
						if (!twoList.isEmpty()){
							Collections.sort(twoList, new IdComparator());
							finalFormatList.addAll(twoList);
						}
						if (!threeList.isEmpty()){
							Collections.sort(threeList, new IdComparator());
							finalFormatList.addAll(threeList);
						}
						if (!fourList.isEmpty()){
							Collections.sort(fourList, new IdComparator());
							finalFormatList.addAll(fourList);
						}
						if (!fiveList.isEmpty()){
							Collections.sort(fiveList, new IdComparator());
							finalFormatList.addAll(fiveList);
						}
					} else {
						if (!fiveList.isEmpty()){
							Collections.sort(fiveList, new IdComparator().reversed());
							finalFormatList.addAll(fiveList);
						}
						if (!fourList.isEmpty()){
							Collections.sort(fourList, new IdComparator().reversed());
							finalFormatList.addAll(fourList);
						}
						if (!threeList.isEmpty()){
							Collections.sort(threeList, new IdComparator().reversed());
							finalFormatList.addAll(threeList);
						}
						if (!twoList.isEmpty()){
							Collections.sort(twoList, new IdComparator().reversed());
							finalFormatList.addAll(twoList);
						}
						if (!oneList.isEmpty()){
							Collections.sort(oneList, new IdComparator().reversed());
							finalFormatList.addAll(oneList);
						}
					}

					int counter = 0;
					logger.info("lenth of the arrayList :::"+finalFormatList.size()+ " startIndex::"+startIndex+"pageSize:::"+pagesize);

					if(finalFormatList.size() > (startIndex + pagesize))
						counter = startIndex + pagesize;
					else counter = finalFormatList.size();

					ArrayList<CampaignBean> finalList = new ArrayList<>();
					for (int i = startIndex; i < counter; i++) {

						finalList.add(finalFormatList.get(i));
					}

					Iterator<CampaignBean> itr = finalList.iterator();

					while (itr.hasNext()) {
						CampaignBean bean = itr.next();

						String platform = Utility.convertStringPlatformSorted(bean.getPlatform());
						bean.setPlatform(platform);
						logger.debug(
								"campaign_id:::" + bean.getCampaign_id() + "    " + "platform:::" + bean.getPlatform());
						listCampignFinal.add(bean);
					}

					jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, listCampignFinal, count);
					logger.info("findCampaignList Fetch Total Size [ " + listCampignFinal.size() + " ]");
				} else {
					jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "NO DATA FOUND");
					logger.info("findCampaignStatList NO DATA FOUND");
				}
			} catch (Exception e) {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
				logger.error("findCampaignList ", e);
			}

		} else {

			int startIndex = campignBean.getStartIndex();
			int pagesize = campignBean.getPageSize();

			try {

				long st = System.currentTimeMillis();
				int count = iCampaignDao.countCampaignsNew(campstatus, searchCampignName, startIndex, pagesize, flag,
						analytics, null, null, campignBean.getProductId());
				List<Object[]> resnew = iCampaignDao.listCampaignsNew(campstatus, searchCampignName, startIndex,
						pagesize, flag, analytics, null, null, campignBean.getProductId(),
						campignBean.getColumnForOrdering(), campignBean.getRequiredOrder());
				if (resnew.size() > 0) {
					logger.info("Campaign List Total time to execute [ " + (System.currentTimeMillis() - st) + " ]");

					List<CampaignBean> listCampign = new ArrayList<CampaignBean>();

					for (Object[] objects : resnew) {
						String platforms = "";
						Response response = iCampaignDao.getPlatforms(Utility.intConverter("" + objects[0]));
						if (response.getStatus()) {
							List<IopushRules> listRules = (List<IopushRules>) response.getData();
							for (IopushRules IopushRule : listRules) {
								if (IopushRule.getRuleType().equals("platform")) {
									platforms = IopushRule.getRuleValue();
									break;
								}

							}
						}
						listCampign.add(new CampaignBean(objects, platforms));

					}

					jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, listCampign, count);

					logger.info("findCampaignList Fetch Total Size [ " + listCampign.size() + " ]");
				} else {
					jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "NO DATA FOUND");
					logger.info("findCampaignStatList NO DATA FOUND");
				}
			} catch (Exception e) {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
				logger.error("findCampaignList ", e);
			}

		}




		/*}else{
			jsonResponse = new JsonResponse<CampaignBean>(""+Constants.UNAUTHORIZE_USER_CODE,Constants.UNAUTHORIZE_USER);
			logger.info("findCampaignList "+Constants.UNAUTHORIZE_USER);
		}*/
		return jsonResponse;
	}


	@Override
	public JsonResponse<CampaignBean> findCampaignStatList(CampaignBean campignBean) {
		logger.info("findCampaignStatList [ " + campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		Integer[] campstatus = Utility.getCampignStatus(campignBean);
		boolean flag = Utility.checkCampStatus(campstatus);
		boolean analytics = true;
		String searchCampignName = campignBean.getCampaign_name();
		String username = SecurityUtils.getCurrentLogin();
		int startIndex = campignBean.getStartIndex();
		int pagesize = campignBean.getPageSize();
		String campaign_date1 = campignBean.getCampaign_schedule_date();
		String campaign_date2 = campignBean.getCampaign_end_date();
		//if(!username.equalsIgnoreCase("anonymousUser")){
		try {
			logger.info("findCampaignStatList [ " + searchCampignName + " ], startIndex [ " + startIndex + " ] , pagesize [ "
					+ pagesize + " ], flag [ " + flag + " ], username [ " + username+" ]");

			int count=iCampaignDao.countCampaignAnalytics(startIndex, pagesize, analytics, searchCampignName, campaign_date1, campaign_date2,campignBean.getProductId());
			List<Object[]> response=iCampaignDao.campaignsAnalyticsList(campstatus,searchCampignName,startIndex, pagesize, analytics, campaign_date1, campaign_date2,campignBean.getProductId(), campignBean.getColumnForOrdering(), campignBean.getRequiredOrder());
			if(!response.isEmpty()){
				List<CampaignBean> listCampaigns = new ArrayList<CampaignBean>();
				for (Object[] object : response) 
				{
					listCampaigns.add(new CampaignBean(Utility.intConverter(""+object[0]), ""+object[1], ""+object[3], Utility.intConverter(""+object[2]),
							Utility.intConverter(""+object[6]), Utility.intConverter(""+object[4]),Utility.intConverter(""+object[5])));
					//listCampaigns.add(new CampaignBean(object));
				}

				jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, listCampaigns, count);

				logger.info("findCampaignStatList Fetch Total Size [ " + listCampaigns.size()+" ]");
			} else {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "NO DATA FOUND");
				logger.info("findCampaignStatList NO DATA FOUND");
			}
		} catch (Exception e) {
			jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("findCampaignStatList" , e);
		}
		return jsonResponse;
	}



	@Override
	public JsonResponse<CampaignBean> fetchByID(CampaignBean campignBean) {
		logger.info("findCampaignById [ "+campignBean.toString()+" ]");
		Map<String, String> ruleMap = new HashMap<String, String>();
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		//		String username=SecurityUtils.getCurrentLogin();
		int campignid = campignBean.getCampaign_id();
		logger.info("findCampaignById campignid[ " + campignid+" ]");

		try {
			Response response = iCampaignDao.findCampaignByID(campignid);
			IopushCampaign iopushCampaign = (IopushCampaign) response.getScalarResult();

			for(IopushRules iopushRules : iopushCampaign.getIopushRules()){
				ruleMap.put(iopushRules.getRuleType(), iopushRules.getRuleValue());
			}

			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String imageurl = myProperties.getProperty(env + "IMAGEURL");
			/********************************/

			CampaignBean campaignBean =	new CampaignBean(iopushCampaign, ruleMap, imageurl);
			/********************************/
			jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, campaignBean, 1);
			logger.info("findCampaignById  SUCCESS [ " + campaignBean.toString()+" ]");
		} catch (Exception e) {
			jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("findCampaignById " , e);
		}

		return jsonResponse;
	}

	@Override

	public ResponseMessage checkCampaignName(CampaignBean campignBean , Integer product_id) {

		logger.info("checkcampaignname->" + campignBean.toString());
		ResponseMessage rm = null;
		String campignname = campignBean.getCampaign_name();
		int campignid =  campignBean.getCampaign_id();


		/*String username = SecurityUtils.getCurrentLogin();
		  campignname=username.equalsIgnoreCase("admin")?campignname+"_admin":campignname;


		  if(!username.equalsIgnoreCase("anonymousUser")){
		 */try {
			 Response response = iCampaignDao.findCampaign(campignname,campignid,product_id);
			 if (response.getStatus()) {
				 rm = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Campaign " + campignname + " already exists");
				 logger.info("checkcampaignname campaignName "+campignname+" alresdy exists");
			 } else {
				 rm = new ResponseMessage(Constants.SUCCESS_CODE, "Campaign " + campignname + " does not exist");
				 logger.info("checkcampaignname campaignName "+campignname+" does not exist");
			 }
		 } catch (Exception e) {
			 rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			 logger.error("checkcampaignname[ " , e+" ]");
		 }
		 /* }else{
		   rm = new ResponseMessage(Constants.UNAUTHORIZE_USER_CODE,Constants.UNAUTHORIZE_USER);
		   logger.info("checkcampaignname "+Constants.UNAUTHORIZE_USER);
		  }*/
		 logger.info("checkcampaignname Response[ " + rm+" ]");
		 return rm;
	}

	@Override
	public ResponseMessage changeCampaignStatus(CampaignBean campignBean) {
		logger.info("changecampaignStatus [ " + campignBean.toString() +" ]");
		ResponseMessage rResponse = null; 
		try{
			iCampaignDao.changeCampaignStatus(campignBean.getCampaign_id(), Constants.PENDING);
			rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Changed Successfully!");
			logger.info("changecampaignStatus Campaign campignid->" + campignBean.getCampaign_id() + " Campaign Status Changed Successfully!");
		}catch(Exception e){
			rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("changecampaignStatus Campaign campignid->" + campignBean.getCampaign_id() + "  " ,  e); 
		}
		return rResponse;
	}

	@Override
	public JsonResponse<CampaignBean> delete(CampaignBean campignBean) {
		logger.info("deleteCampaign [ "+campignBean.toString()+" ]");
		JsonResponse<CampaignBean> jsonResponse = new JsonResponse<CampaignBean>();
		int campignid = campignBean.getCampaign_id();
		try {
			logger.info("deleteCampaign CampaignRule campignid[" + campignid + "]");
			iCampaignDao.deleteCampaignRule(campignid);
			//			iCampaignDao.deleteNotification(campignid);
			iCampaignDao.deleteViewsClick(campignid);
			iCampaignDao.deleteSubCampaign(campignid);
			Response response = iCampaignDao.deleteCampaign(campignid);
			if (response.getIntegerResult() > 0) {

				jsonResponse = new JsonResponse<CampaignBean>(Constants.SUCCESS, "Successfully Deleted");
				logger.info("deleteCampaign Campaign campignid [ " + campignid + " ] Successfully Deleted");
			} else {
				jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, "Deletion Failed");
				logger.info("deleteCampaign Campaign campignid [ " + campignid + " ] Deletion Failed");
			}
		} catch (Exception e) {
			jsonResponse = new JsonResponse<CampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("deleteCampaign Campaign campignid [ " + campignid + " ] " ,  e);
		}
		return jsonResponse;
	}






	/*@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> listISP() {
		logger.info("inside IspList");

		Response response = iCampaignDao.ispList();
		logger.info("listIsp response status is [ "+ response.getStatus()+" ]" );
		List<IopushIsp> listIopushIsp = (List<IopushIsp>) response.getData();
		List<ComboBoxOption> objListDisplayOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (response.getStatus()) {

			for (IopushIsp iopushIsp : listIopushIsp) {
				objListDisplayOption
				.add(new ComboBoxOption("" + iopushIsp.getIspId(), iopushIsp.getIspName()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListDisplayOption,
					objListDisplayOption.size());
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, response.getErrorMessage());
		}
		return jsonResp;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> listISP(Integer[] countryId) {
		logger.info("inside IspList");

		Response response = iCampaignDao.ispList(countryId);
		logger.info("listIsp response status is [ "+ response.getStatus()+" ]" );
		List<IopushIsp> listIopushIsp = (List<IopushIsp>) response.getData();
		List<ComboBoxOption> objListDisplayOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (response.getStatus()) {

			for (IopushIsp iopushIsp : listIopushIsp) {
				objListDisplayOption
				.add(new ComboBoxOption("" + iopushIsp.getIspId(), iopushIsp.getIspName()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListDisplayOption,
					objListDisplayOption.size());
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, response.getErrorMessage());
		}
		return jsonResp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> listTimezone() {
		logger.info("inside listTimeZone ");
		Response objResponse = iCampaignDao.listTimezone();
		logger.info("listTimeZone response status is [ "+objResponse.getStatus()+" ]");
		List<ComboBoxOption> objListComboBoxOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (objResponse.getStatus()) {
			for (IopushTimeZone objTimezoneDetail : (List<IopushTimeZone>) objResponse.getData()) {
				objListComboBoxOption
				.add(new ComboBoxOption( objTimezoneDetail.getCountryId(), objTimezoneDetail.getTimezone(),objTimezoneDetail.getCountry()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListComboBoxOption,
					objListComboBoxOption.size());
			logger.info("listTimeZone data successfully retreived from the database!");
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, objResponse.getErrorMessage());
			logger.info("listTimeZone there was a problem while retreiving the data ");
		}
		return jsonResp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> fetchCity(int country_id) {
		Response response = new Response();
		JsonResponse<ComboBoxOption> jsonResponse = new JsonResponse<ComboBoxOption>();
		List<ComboBoxOption> listComboBoxOption = new ArrayList<ComboBoxOption>();
		response = iCampaignDao.listCities(country_id);
		if(response.getStatus())
		{
			for(IopushCityDetails objIopushCityDetails : (List<IopushCityDetails>)response.getData())
			{
				listComboBoxOption.add(new ComboBoxOption(objIopushCityDetails.getCityId(), objIopushCityDetails.getCityCode(),objIopushCityDetails.getCityName()));
			}
			jsonResponse = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, listComboBoxOption,
					listComboBoxOption.size());
		} 
		else {
			jsonResponse = new JsonResponse<ComboBoxOption>(Constants.ERROR, response.getErrorMessage());
		}

		return jsonResponse;
	}



	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public ResponseMessage launch(CampaignBean campaignBean, Integer product_id ) {
		//		product_id = 1;
		boolean isAnySubCampaignLive = false;
		ResponseMessage rResponse = new ResponseMessage();
		Map<String, String> ruleMap = new HashMap<String, String>();
		int campignid = campaignBean.getCampaign_id();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		String insertQuery = "";
		Boolean segmented =false;
		int flag =0;
		Response campaignResponse, response;
		Response response1 = new Response();
		String segments ="", segmentTypes="";
		String segmentval="";
		StringBuilder segmentid=new StringBuilder();
		logger.info("launching Campaign campignid[" + campignid + "] for productId [ "+product_id+" ]");
		try
		{
			response = iCampaignDao.findCampaignByID(campignid);

			if(response.getStatus())
			{
				IopushCampaign iopushCampaign = (IopushCampaign) response.getScalarResult();
				String timezone = iopushCampaign.getIopushTimeZone().getTimezone();
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timezone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				Date modificationDate = (new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS)).parse(dtf.print(dt));
				
				if(iopushCampaign.isSegmented())
				{
					segmented = true;
					for(IopushRules iopushRules : iopushCampaign.getIopushRules()){
						ruleMap.put(iopushRules.getRuleType(), iopushRules.getRuleValue());
						if(iopushRules.getRuleType().equals("segments"))
						{
							segments =  iopushRules.getRuleValue();
						}
						if(iopushRules.getRuleType().equals("segmentTypes"))
						{
							segmentTypes = iopushRules.getRuleValue();
						}
					}
				}
				if(segments.equals("")  && !segmentTypes.equals(""))
				{

					response1 = iSegmentDao.listSegment(segmentTypes,product_id);
					logger.info("launch campaign, SegmentType response status is [ "+ response.getStatus()+" ]" );
					List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
					if (response1.getStatus()) {

						for (IopushSegmentation iopushSegment : listIopushSegment) {
							segmentid.append(iopushSegment.getSegmentId()+",");
						}
						segmentval = segmentid.substring(0, segmentid.lastIndexOf(","));
						campaignBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
					}
				}
				if(iopushCampaign.isLargeImage())
				{
					campaignBean.setBannerImage(iopushCampaign.getBannerImage());
				}
				//    iopushCampaign.isMultiple_country() ===================multiple countries code starts here
				if(iopushCampaign.getSubCampaignsCount() > 1)
				{
					int livecampaignsCount = iopushCampaign.getLive();
					response = iCampaignDao.getSubCampaigns(campignid);
					List<IopushSubCampaign> listSubCamnpaign = (List<IopushSubCampaign>) response.getData();
					if(response.getStatus())
					{
						for(IopushSubCampaign subCampaign : listSubCamnpaign)
						{
							String env = myProperties.getProperty("ENVIORAMENT") + ".";
							String imageurl = myProperties.getProperty(env + "IMAGEURL");
							campaignBean = new CampaignBean(iopushCampaign, ruleMap,imageurl);
							int country_id = subCampaign.getGeoid();
							logger.info("segmentval  11111=="+segmentval);
							if(campaignBean.getSegments().equals("") && !campaignBean.getSegmentTypes().equals(""))
							{campaignBean.setSegments(segmentval);}
							insertQuery = Utility.insertQueryBasedOnCriteriaForSubCampaign(campaignBean, segmented, product_id, country_id);
							insertQuery = Constants.INSERT_INTO_DYNAMIC_SUBSCRIBER_CAMPAIGN_TABLE_QUERY + "(" + insertQuery + ")";
							logger.info("launchCampaign insertQuery [" + insertQuery + "]");
							logger.info("launch insert Query "+insertQuery);
							response = iCampaignDao.executeQuery(insertQuery);
							campaignBean.setProductId(product_id);
							logger.info("campaignBean [" + campaignBean + "]");
							HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(campaignBean), myProperties.getProperty(env+"messaging.url"));
							logger.info("launchCampaign httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
							if (response.getStatus()) {
								livecampaignsCount++;
								flag = 1;
								response = iCampaignDao.changeSubCampaignStatus(campignid,country_id, Constants.LIVE);
							}else
								rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
							//							}

						}
						if(flag == 0)
						{
							rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
						}
					}
					if(livecampaignsCount > 0)
					{
						iopushCampaign.setModificationDate(modificationDate);
						iopushCampaign.setCampaignStatus(Constants.LIVE);
						if(iopushCampaign.getCampaignStatus() != 1)
						{
							response = iCampaignDao.updateCampaign(iopushCampaign);
						}
						if(livecampaignsCount != iopushCampaign.getLive())
						{
							iopushCampaign.setLive(livecampaignsCount);
							response = iCampaignDao.updateCampaign(iopushCampaign);
						}
						rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Notification sent Successfully!");
					}
					else
					{
						rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
					}
				}
				else
				{
					
					String env = myProperties.getProperty("ENVIORAMENT") + ".";
					String imageurl = myProperties.getProperty(env + "IMAGEURL");
					campaignBean = new CampaignBean(iopushCampaign, ruleMap,imageurl);
					if(campaignBean.getSegments().equals("") && !campaignBean.getSegmentTypes().equals(""))
					{campaignBean.setSegments(segmentval);}
					insertQuery = Utility.insertQueryBasedOnCriteria(campaignBean, segmented, product_id);
					insertQuery = Constants.INSERT_INTO_DYNAMIC_SUBSCRIBER_CAMPAIGN_TABLE_QUERY + "(" + insertQuery + ")";
					response = iCampaignDao.executeQuery(insertQuery);
					logger.info("Query to be executed [" + insertQuery + "]");
					HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(campaignBean), myProperties.getProperty(env+"messaging.url"));
					logger.info("launchCampaign httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");

					if (response.getStatus()) {
						iopushCampaign.setModificationDate(modificationDate);
						iopushCampaign.setCampaignStatus(Constants.LIVE);
						response = iCampaignDao.updateCampaign(iopushCampaign);
						rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Notification sent Successfully!");
					}else
						rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
				}
				//    ends here
			}
			else if(!response.getStatus())
			{
				rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Campaign not found");
				logger.info("launchCampaign campignid[" + campignid + "] campaign with given id does not exist ");
			} 
			else if (response.getIntegerResult() > 0) {
				rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Launched");
				logger.info("launchCampaign campignid[" + campignid + "] Successfully Launched");
			} else {
				rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to launch campaign");
				logger.info("launchCampaign campignid[" + campignid + "] Launching Failed");
			}
		}
		catch (Exception e) {
			rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("launchCampaign Campaign campignid->" + campignid + "  " ,  e);
		}
		return rResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage autoexpire() {
		ResponseMessage rMessage = new ResponseMessage();
		Response response = this.iCampaignDao.listCampaigns_expire();
		//		SimpleDateFormat sdf =new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
		CampaignBean campaignBean = new CampaignBean();
		logger.info("autoexpire response status is [ "+response.getStatus()+" ]"); 
		try
		{
			if(response.getStatus())
			{
				List<IopushCampaign> listIopushCampaign = (List<IopushCampaign>) response.getData();
				for(IopushCampaign iopushCampaign : listIopushCampaign)
				{
					campaignBean.setCampaign_id(iopushCampaign.getCampaignId());
					if(iopushCampaign.getSubCampaignsCount() > 1)
					{

						rMessage = expire(iopushCampaign.getCampaignId());
					}
					else
					{
						Date currentTimeServerCountry = Utility.addMin(0, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
						logger.info("autoLaunch, current time in server country is [ "+currentTimeServerCountry+" ]");
						/*String timeZone = iopushCampaign.getIopushTimeZone().getTimezone();
						String currentDateInCountry = Utility.changeDateIntotimeZone(new Date(), timeZone); 
						Date result = sdf.parse(currentDateInCountry);*/
						if(currentTimeServerCountry.after(iopushCampaign.getCampaignEndDateInEdt()) || currentTimeServerCountry.equals(iopushCampaign.getCampaignScheduleDateInEdt()) || currentTimeServerCountry.equals(iopushCampaign.getCampaignEndDateInEdt()) )
						{
							rMessage = expire(iopushCampaign.getCampaignId());
						}

					}
				}
			}
			else
			{
				logger.info("autoexpire No Campaign is eligible for launch");
				rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "No campaign is eligible for autoexpire");
			}
		}
		catch(Exception e)
		{
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to autoexpire campaign");
			logger.error("autoexpire Failed to launch campaign with the exception trace ",  e); 
		}
		return rMessage;
	}



	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public ResponseMessage expire(int campaign_id) {
		logger.info("inside expireCampaign, campaign_id [ "+campaign_id+" ]");
		ResponseMessage jsonResponse = null;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
		try {
			// check for campaign is for multiple counties or not
			Response response1 = iCampaignDao.findCampaignByID(campaign_id);
			IopushCampaign iopushCampaign = (IopushCampaign) response1.getScalarResult();
			String timezone = iopushCampaign.getIopushTimeZone().getTimezone();
			DateTime dt = new DateTime().withZone(DateTimeZone.forID(timezone));
			DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
			Date modificationDate = (new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS)).parse(dtf.print(dt));
			logger.info("Campaign has subcampaigns [ "+iopushCampaign.isMultiple_country()+" ]");
			boolean multiple_status = iopushCampaign.isMultiple_country();
			boolean isError = false;
			int campaignExpiredCount = 0;
			if(iopushCampaign.getSubCampaignsCount() > 1)
			{
				Response response2 = iCampaignDao.getSubCampaigns(campaign_id);
				if(response2.getStatus()){
					List<IopushSubCampaign> listSubCamnpaign = (List<IopushSubCampaign>) response2.getData();

					for(IopushSubCampaign subCampaign : listSubCamnpaign)
					{
						String timeZone = subCampaign.getIopushTimeZone().getTimezone();
						String currentDateInCountry = Utility.changeDateIntotimeZone(new Date(), timeZone); 
						Date result = sdf.parse(currentDateInCountry);
						if(result.after(subCampaign.getCampaignEndDateInEdt()) || result.equals(subCampaign.getCampaignScheduleDateInEdt()) || result.equals(subCampaign.getCampaignEndDateInEdt()) )
						{
							campaignExpiredCount++;
							Response response = iCampaignDao.deleteActiveSubscribers_subcampaign(campaign_id, subCampaign.getGeoid());
							response = iCampaignDao.changeSubCampaignStatus(campaign_id, subCampaign.getGeoid(), Constants.EXPIRE);

						}
					}
				}
				else if(iopushCampaign.isSegmented() == false)
				{
					String timeZone = iopushCampaign.getIopushTimeZone().getTimezone();
					String currentDateInCountry = Utility.changeDateIntotimeZone(new Date(), timeZone); 
					Date result = sdf.parse(currentDateInCountry);
					if(result.after(iopushCampaign.getCampaignEndDateInEdt()) || result.equals(iopushCampaign.getCampaignScheduleDateInEdt()) || result.equals(iopushCampaign.getCampaignEndDateInEdt()) )
					{
						iopushCampaign.setCampaignStatus(Constants.EXPIRE);
						iopushCampaign.setModificationDate(modificationDate);
						Response response = iCampaignDao.deleteActiveSubscribers(campaign_id);
						Response response3 = iCampaignDao.updateCampaign(iopushCampaign);
						jsonResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "This campaign can not be launched because the start date is in the past. This campaign is now expired.");
						logger.info("expireCampaign, data successfully deleted from activeSubscribers also campaign status changed ");
					}
				}
				else
				{
					isError=true ;
					jsonResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "This campaign can not be expired because it is still eligible for atleast one country.");
					logger.info("expireCampaign, Campaign is not eligible for expiration");
				}
				Thread.sleep(2000);
				if(campaignExpiredCount == iopushCampaign.getSubCampaignsCount() && !isError)
				{
					iopushCampaign.setCampaignStatus(Constants.EXPIRE);
					iopushCampaign.setModificationDate(modificationDate);
					Response response3 = iCampaignDao.updateCampaign(iopushCampaign);
					jsonResponse = new ResponseMessage(Constants.SUCCESS_CODE, "This campaign can not be launched because the start date is in the past. This campaign is now expired.");
					logger.info("expireCampaign, data successfully deleted from activeSubscribers also campaign status changed ");
				}


			}
			else
			{
				iopushCampaign.setCampaignStatus(Constants.EXPIRE);
				iopushCampaign.setModificationDate(modificationDate);
				Response response = iCampaignDao.deleteActiveSubscribers(campaign_id);
				response = iCampaignDao.updateCampaign(iopushCampaign);
				jsonResponse = new ResponseMessage(Constants.SUCCESS_CODE, "This campaign can not be launched because the start date is in the past. This campaign is now expired.");
				logger.info("expireCampaign, data successfully deleted from activeSubscribers also campaign status changed ");  
			}
		}
		catch (Exception e) {
			jsonResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("expireCampaign, exception occured while moving data  ", e);
		}
		return jsonResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage sentNotification(CampaignBean campaignBean) {
		ResponseMessage responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Notification sent successfully") ;
		try{
			Response response = iExternalDao.listSubscribers() ;
			if(response.getStatus()){
				List<IopushSubscribers> listSubscribers = (List<IopushSubscribers>) response.getData() ;
				int successCount=0;
				for(IopushSubscribers iopushSubscriber:listSubscribers)
					successCount += Utility.sendNotification(campaignBean.getTitle(), campaignBean.getDescription(), campaignBean.getImagePath(), campaignBean.getForwardUrl(), iopushSubscriber.getFcmToken(),myProperties);
				if(successCount>0)
					responseMessage.setResponseDescription(successCount + " Notification sent successfully");
				else
					responseMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID,"No subscriber available to sent Notification");
			}

		}catch (Exception e) {
			logger.error("An Error Occurred while throwing exception",e);
			responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,"An Error Occurred while sending a message");
		}
		return responseMessage;
	}





	@Override
	public JsonResponse<String> criteriaList(HashMap<String, String> criteriaList) {
		logger.info("inside criteriaList");
		String subSelectQuery = "select count(subscribers_id) from " + Constants.IOPUSH_SUBSCRIBERS + " where fk_product_id="+criteriaList.get(Constants.PRODUCT_ID);
		String masterQuery = "";
		/*
		if (criteriaList.containsKey(Constants.SUBSCRIPTION_RANGE) || criteriaList.containsKey(Constants.COUNTRY_ID)
				|| criteriaList.containsKey(Constants.CITY_ID) 
				|| criteriaList.containsKey(Constants.DEVICES) || criteriaList.containsKey(Constants.ISP_ID)) {
		}
		 */
		if (criteriaList.getOrDefault(Constants.SUBSCRIPTION_RANGE, "").length() > 0) {
			subSelectQuery += " and date(installation_date) >= "
					+"'"+ criteriaList.get(Constants.SUBSCRIPTION_RANGE)+"'";
		}

		if (criteriaList.getOrDefault(Constants.COUNTRY_ID, "").length() > 0) {
			subSelectQuery += " and fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
		}

		if (criteriaList.getOrDefault(Constants.CITY_ID, "").length() > 0) {
			subSelectQuery += " and fk_city_id in(" + criteriaList.get(Constants.CITY_ID) + ")";
		}

		if (criteriaList.getOrDefault(Constants.DEVICES, "").length() > 0) {
			subSelectQuery += " and fk_platform_id in(" + criteriaList.get(Constants.DEVICES) + ")";
		}

		if (criteriaList.getOrDefault(Constants.ISP_ID, "").length() > 0) {
			subSelectQuery += " and fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
		}

		if (criteriaList.getOrDefault(Constants.SEGMENTS, "").length() > 0) {
			subSelectQuery += " and fk_segmentid in(" + criteriaList.get(Constants.SEGMENTS) + ")";
		}

		String query = masterQuery.length() == 0 ? subSelectQuery : masterQuery ;
		logger.info("Count Query [" + query  + "]");
		Response response = iCampaignDao
				.findSUBSCRIBERSCountByCriteira(query);
		JsonResponse<String> jsonResponse = new JsonResponse<String>();
		if (response.getStatus()) {
			jsonResponse.setTotalRecordCount(((BigInteger) response.getScalarResult()).intValue());
			jsonResponse.setResult(Constants.SUCCESS);
		}
		return jsonResponse;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> listPlatform() {
		logger.info("inside listPlatform");
		Response objResponse = iCampaignDao.listplatform();
		List<ComboBoxOption> objListComboBoxOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (objResponse.getStatus()) {
			for (IopushPlatformDetails objPlatformDetail : (List<IopushPlatformDetails>) objResponse.getData()) {
				objListComboBoxOption
				.add(new ComboBoxOption( objPlatformDetail.getPlatformID(),objPlatformDetail.getPlatformName(),objPlatformDetail.getPlatformName()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListComboBoxOption,
					objListComboBoxOption.size());
			logger.info("listPlatform data successfully retreived from database.");
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, objResponse.getErrorMessage());
			logger.info("listPlatform there wwas a problem while retreiving data from database.");
		}
		return jsonResp;
	}




	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<SubCampaignBean> fetchSubCampaignList(CampaignBean campaignBean) {
		logger.info("findCampaignList [ " + campaignBean.toString()+" ]");
		JsonResponse<SubCampaignBean> jsonResponse = new JsonResponse<SubCampaignBean>();
		Response response = new Response();
		try
		{
			int campaignId = campaignBean.getCampaign_id();
			response = iCampaignDao.listSubCampaign(campaignId);
			if(response.getStatus() )
			{
				List<SubCampaignBean> listSubCampaign = (List<SubCampaignBean>) response.getData(); 
				jsonResponse = new JsonResponse<SubCampaignBean>(Constants.SUCCESS, listSubCampaign, listSubCampaign.size());
			}
			else
			{
				jsonResponse = new JsonResponse<SubCampaignBean>(Constants.SUCCESS, "Campaign Is Created For Single Country");
			}
		}
		catch (Exception e) {
			jsonResponse = new JsonResponse<SubCampaignBean>(Constants.ERROR, e.getMessage());
			logger.error("findCampaignList " , e);
		}
		return jsonResponse;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage autoLaunch() {
		ResponseMessage rMessage = new ResponseMessage();
		Response response = this.iCampaignDao.listCampaignAutoLaunch();
		//		SimpleDateFormat sdf =new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		CampaignBean campaignBean = new CampaignBean();
		logger.info("autoLaunch, response status is [ "+response.getStatus()+" ]"); 
		try
		{
			if(response.getStatus())
			{
				List<IopushCampaign> listIopushCampaign = (List<IopushCampaign>) response.getData();
				logger.info("autoLaunch number of campaigns found [ "+listIopushCampaign.size()+" ]");
				for(IopushCampaign iopushCampaign : listIopushCampaign)
				{
					campaignBean.setCampaign_id(iopushCampaign.getCampaignId());
					if(iopushCampaign.getSubCampaignsCount() > 1)
					{
						rMessage = launchCampaigns(campaignBean, iopushCampaign.getIopushProduct().getProductID());
					}
					else
					{
						Date currentTimeServerCountry = Utility.addMin(0, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
						logger.info("autoLaunch, current time in server country is [ "+currentTimeServerCountry+" ]");
						/*String timeZone = iopushCampaign.getIopushTimeZone().getTimezone();
						String currentDateInCountry = Utility.changeDateIntotimeZone(new Date(), timeZone,Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS); 
						Date result = sdf.parse(currentDateInCountry);*/
						if( (currentTimeServerCountry.after(iopushCampaign.getCampaignScheduleDateInEdt()) && currentTimeServerCountry.before(iopushCampaign.getCampaignEndDateInEdt())) || currentTimeServerCountry.equals(iopushCampaign.getCampaignScheduleDateInEdt()) || currentTimeServerCountry.equals(iopushCampaign.getCampaignEndDateInEdt())  )
						{
							rMessage = launchCampaigns(campaignBean, iopushCampaign.getIopushProduct().getProductID());
						}

					}
				}

			}
			else
			{
				logger.info("autoLaunch No Campaign is eligible for launch");
				rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "No campaign is eligible for launch");
			}
		}
		catch(Exception e)
		{
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to launch campaign");
			logger.error("autoLaunch Failed to launch campaign with the exception trace ",  e); 
		}
		return rMessage;
	}




	@SuppressWarnings("unchecked")
	public JsonResponse<ComboBoxOption> listProduct() {
		logger.info("inside listProduct");
		Response objResponse = iCampaignDao.listProduct();
		List<ComboBoxOption> objListComboBoxOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (objResponse.getStatus()) {
			for (IopushProduct objProduct : (List<IopushProduct>) objResponse.getData()) {
				objListComboBoxOption
				.add(new ComboBoxOption( objProduct.getProductID(),objProduct.getProductName(),objProduct.getProductName()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListComboBoxOption,
					objListComboBoxOption.size());
			logger.info("listProduct data successfully retreived from database.");
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, objResponse.getErrorMessage());
			logger.info("listProduct there was a problem while retreiving data from database.");
		}
		return jsonResp;
	}


	@SuppressWarnings("unchecked")
	private ResponseMessage launchCampaigns(CampaignBean campaignBean, int product_id) {
		//		product_id = 1;
		boolean isAnySubCampaignLive = false;
		ResponseMessage rResponse = new ResponseMessage();
		Map<String, String> ruleMap = new HashMap<String, String>();
		int campignid = campaignBean.getCampaign_id();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		String insertQuery = "";
		Boolean segmented =false;
		int flag =0;
		Response campaignResponse, response;
		Response response1;
		String segments="",segmentTypes="";
		StringBuilder segmentid=new StringBuilder();
		logger.info("launching Campaign campignid[" + campignid + "] for productId [ "+product_id+" ]");
		try
		{
			response = iCampaignDao.findCampaignByID(campignid);

			if(response.getStatus())
			{
				IopushCampaign iopushCampaign = (IopushCampaign) response.getScalarResult();
				String timezone = iopushCampaign.getIopushTimeZone().getTimezone();
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timezone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				Date modificationDate = (new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS)).parse(dtf.print(dt));
				if(iopushCampaign.isSegmented())
				{
					segmented = true;
					for(IopushRules iopushRules : iopushCampaign.getIopushRules()){
						ruleMap.put(iopushRules.getRuleType(), iopushRules.getRuleValue());
						if(iopushRules.getRuleType().equals("segments"))
						{
							segments =  iopushRules.getRuleValue();
						}
						if(iopushRules.getRuleType().equals("segmentTypes"))
						{
							segmentTypes = iopushRules.getRuleValue();
						}
					}
				}
				logger.info("11"+segmentTypes);
				logger.info("22"+segments);
				if(segments.equals("")  && !segmentTypes.equals(""))
				{

					response1 = iSegmentDao.listSegment(segmentTypes,product_id);
					logger.info("in campaignlistSegmentType response status is [ "+ response.getStatus()+" ]" );
					List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
					if (response1.getStatus()) {

						for (IopushSegmentation iopushSegment : listIopushSegment) {
							segmentid.append(iopushSegment.getSegmentId()+",");
						}
						campaignBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")-1));
					}
				}
				if(iopushCampaign.isLargeImage())
				{
					campaignBean.setBannerImage(iopushCampaign.getBannerImage());
				}
				//    iopushCampaign.isMultiple_country() ===================multiple countries code starts here
				if(iopushCampaign.getSubCampaignsCount() > 1)
				{
					int livecampaignsCount = iopushCampaign.getLive();
					response = iCampaignDao.getSubCampaigns(campignid);
					List<IopushSubCampaign> listSubCamnpaign = (List<IopushSubCampaign>) response.getData();
					if(response.getStatus())
					{
						for(IopushSubCampaign subCampaign : listSubCamnpaign)
						{
							String timeZone = subCampaign.getIopushTimeZone().getTimezone();
							String currentDateInCountry = Utility.changeDateIntotimeZone(new Date(), timeZone, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS); 
							logger.info("Launch current time in "+timeZone+" is "+currentDateInCountry); 
							Date now = sdf.parse(currentDateInCountry);
							Date result = Utility.addMinInDate(now, 15, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
							if( subCampaign.getCampaignStatus()!=1 && (result.before(subCampaign.getCampaignEndDateInEdt()) && result.after(subCampaign.getCampaignScheduleDateInEdt())) || result.equals(subCampaign.getCampaignScheduleDateInEdt()) || result.equals(subCampaign.getCampaignEndDateInEdt()) )
							{
								flag = 1;
								String env = myProperties.getProperty("ENVIORAMENT") + ".";
								String imageurl = myProperties.getProperty(env + "IMAGEURL");
								campaignBean = new CampaignBean(iopushCampaign, ruleMap,imageurl);
								if(campaignBean.getSegments().equals("") && !campaignBean.getSegmentTypes().equals(""))
									/*{campaignBean.setSegments(segmentval);}	*/
									campaignBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
								logger.info("here ======"+campaignBean.getSegments());
								int country_id = subCampaign.getGeoid();
								insertQuery = Utility.insertQueryBasedOnCriteriaForSubCampaign(campaignBean, segmented, product_id, country_id);
								insertQuery = Constants.INSERT_INTO_DYNAMIC_SUBSCRIBER_CAMPAIGN_TABLE_QUERY + "(" + insertQuery + ")";
								logger.info("launchCampaign insertQuery [" + insertQuery + "]");
								logger.info("launch insert Query "+insertQuery);
								response = iCampaignDao.executeQuery(insertQuery);
								campaignBean.setProductId(product_id);
								HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(campaignBean), myProperties.getProperty(env+"messaging.url"));
								logger.info("launchCampaign httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
								if (response.getStatus()) {
									livecampaignsCount++;
									//									isAnySubCampaignLive = true;
									response = iCampaignDao.changeSubCampaignStatus(campignid,country_id, Constants.LIVE);
								}else
									rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
							}

						}
						if(flag == 0)
						{
							rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
						}
					}
					if(livecampaignsCount > 0)
					{
						iopushCampaign.setCampaignStatus(Constants.LIVE);
						iopushCampaign.setModificationDate(modificationDate);
						if(iopushCampaign.getCampaignStatus() != 1)
						{
							response = iCampaignDao.updateCampaign(iopushCampaign);
						}
						if(livecampaignsCount != iopushCampaign.getLive())
						{
							iopushCampaign.setLive(livecampaignsCount);
							response = iCampaignDao.updateCampaign(iopushCampaign);
						}
						rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Notification sent Successfully!");
					}
					else
					{
						rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
					}
				}
				else
				{
					if(iopushCampaign.getCampaignStatus()!=1)
					{
						String env = myProperties.getProperty("ENVIORAMENT") + ".";
						String imageurl = myProperties.getProperty(env + "IMAGEURL");
						campaignBean = new CampaignBean(iopushCampaign, ruleMap,imageurl);
						if(campaignBean.getSegments().equals("") && !campaignBean.getSegmentTypes().equals(""))
						{campaignBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));}
						logger.info("here11="+campaignBean.getSegments());
						insertQuery = Utility.insertQueryBasedOnCriteria(campaignBean, segmented, product_id);
						insertQuery = Constants.INSERT_INTO_DYNAMIC_SUBSCRIBER_CAMPAIGN_TABLE_QUERY + "(" + insertQuery + ")";
						response = iCampaignDao.executeQuery(insertQuery);
						logger.info("Query to be executed [" + insertQuery + "]");
						campaignBean.setProductId(product_id);
						HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(campaignBean), myProperties.getProperty(env+"messaging.url"));
						logger.info("launchCampaign httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");

						if (response.getStatus()) {
							iopushCampaign.setCampaignStatus(Constants.LIVE);
							iopushCampaign.setModificationDate(modificationDate);
							response = iCampaignDao.updateCampaign(iopushCampaign);
							rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Notification sent Successfully!");
						}else
							rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
					}
				}
				//    ends here
			}
			else if(!response.getStatus())
			{
				rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID , "Campaign not found");
				logger.info("launchCampaign campignid[" + campignid + "] campaign with given id does not exist ");
			} 
			else if (response.getIntegerResult() > 0) {
				rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Launched");
				logger.info("launchCampaign campignid[" + campignid + "] Successfully Launched");
			} else {
				rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to launch campaign");
				logger.info("launchCampaign campignid[" + campignid + "] Launching Failed");
			}
		}
		catch (Exception e) {
			rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("launchCampaign Campaign campignid->" + campignid + "  " ,  e);
		}
		return rResponse;

	}


	@Override
	public ResponseMessage campaignAnalytics(int campaign_id,int type,int geo_id,int platform_id,int city_id,int product_id) {
		logger.info("campaignAnalytics campaign_id [ "+campaign_id+" ] type [ "+type+" ] platform_id [ "+platform_id+" ] city_id [ "+city_id+" ]" + "] product_id [ " + product_id +" ]" );
		ResponseMessage response1 = new ResponseMessage();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String queue = myProperties.getProperty(env + "CAMPAIGN_CLICK_OPEN_QUEUE");

		map.put("geo_id",geo_id);
		map.put("platform_id",platform_id);
		map.put("city_id",city_id);
		map.put("campaign_id", campaign_id);
		map.put("product_id", product_id);
		if(type == 1)
		{
			jmsTemplate.convertAndSend("CAMPAIGN_CLICK_QUEUE",map);
			response1 = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully updated records");
		}
		else
		{
			jmsTemplate.convertAndSend("CAMPAIGN_OPEN_QUEUE",map);
			response1 = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully updated records");

		}


		return  response1;
	}



	@Override
	public JsonResponse<GeoStatBean> campaignStatsCountry(CampaignBean campignBean) {
		JsonResponse<GeoStatBean> jsonResponse = new JsonResponse<GeoStatBean>();

		Response response = iCampaignDao.listplatform();
		List<IopushPlatformDetails> listbrowser = (List<IopushPlatformDetails>) response.getData();
		HashMap<Integer, String> mapBrowser = new HashMap<Integer, String>();
		Set<Integer> setBrowser = new HashSet<Integer>();
		for (IopushPlatformDetails icmpBrowserDetails : listbrowser) {
			mapBrowser.put(icmpBrowserDetails.getPlatformID(), icmpBrowserDetails.getPlatformName());
			setBrowser.add(icmpBrowserDetails.getPlatformID());
		}
		logger.info("campaignStatsCountry  fetch listBrowsers [ " + mapBrowser+" ]");
		response = iCampaignDao.listTimezone();
		List<IopushTimeZone> listgeo = (List<IopushTimeZone>) response.getData();
		HashMap<Integer, String> mapGeo = new HashMap<Integer, String>();
		HashMap<Integer, IopushCityDetails> mapCity = new HashMap<Integer, IopushCityDetails>();
		for (IopushTimeZone icmpGeo : listgeo) {
			mapGeo.put(icmpGeo.getCountryId(), icmpGeo.getCountry());

		}
		logger.info("campaignStatsCountry  fetch listgeo [ " + mapGeo+" ]");
		response = iCampaignDao.listCities();
		List<IopushCityDetails> listcity = (List<IopushCityDetails>) response.getData();

		for (IopushCityDetails icmpCity : listcity) {
			mapCity.put(icmpCity.getCityId(), icmpCity);
		}
		logger.info("campaignStatsCountry  fetch listcity [ " + mapCity.size()+" ]");
		response = iCampaignDao.campaignStatsCountry(campignBean.getCampaign_id());
		List<GeoStatBean> objGeoStatBean = new ArrayList<GeoStatBean>();
		List<StatsModelBean> liststats = (List<StatsModelBean>) response.getData();
		logger.info("campaignStatsCountry campaignStatsCountry[ " + liststats.toString()+" ]");
		objGeoStatBean = getGeoStats(liststats, mapBrowser, setBrowser, mapGeo, mapCity, campignBean.getCampaign_id());
		jsonResponse = new JsonResponse<GeoStatBean>(Constants.SUCCESS, objGeoStatBean, objGeoStatBean.size());
		return jsonResponse;

	}

	public List<GeoStatBean> getGeoStats(List<StatsModelBean> liststats, HashMap<Integer, String> mapBrowser,
			Set<Integer> setBrowser, HashMap<Integer, String> mapGeo, HashMap<Integer, IopushCityDetails> mapCity,
			int campaign_id) {
		Set<Integer> setGeo = new HashSet<Integer>();
		Set<Integer> setCity = new HashSet<Integer>();
		for (StatsModelBean statsModelBean : liststats) {
			setGeo.add(statsModelBean.getGeoid());
			setCity.add(statsModelBean.getCityid());
		}
		List<GeoCityBean> listCityStatsBean = new ArrayList<GeoCityBean>();
		List<GeoStatBean> listGeoStatBean = new ArrayList<GeoStatBean>();

		for (int cityid : setCity) {
			List<PlatformStatBean> listPlatformStatBean = new ArrayList<PlatformStatBean>();
			for (StatsModelBean smb : liststats) {
				if (cityid == smb.getCityid()) {
					listPlatformStatBean.add(new PlatformStatBean(smb, mapBrowser));
				}
			}
			for (int browserid : setBrowser) {
				boolean flag = Utility.containsBrowserId(listPlatformStatBean, browserid);
				if (!flag) {
					listPlatformStatBean.add(new PlatformStatBean(browserid, mapBrowser));
				}
			}
			Collections.sort(listPlatformStatBean);
			listCityStatsBean.add(new GeoCityBean(mapCity.get(cityid).getIopushGeoDetails().getGeoId(), cityid,
					mapCity.get(cityid).getCityName(), listPlatformStatBean));
		}
		logger.info("getGeoStats listCityStatsBean [ " + listCityStatsBean+" ]");
		listGeoStatBean = getStats(listCityStatsBean, setGeo, mapGeo, setBrowser, mapBrowser, campaign_id);
		return listGeoStatBean;
	}

	public List<GeoStatBean> getStats(List<GeoCityBean> listCityStatsBean, Set<Integer> setGeo,
			HashMap<Integer, String> mapGeo, Set<Integer> setBrowser, HashMap<Integer, String> mapBrowser,
			int campaign_id) {
		List<GeoStatBean> listGeoStatBean = new ArrayList<GeoStatBean>();
		List<PlatformStatBean> listPlatformStatBean = null;
		List<GeoCityBean> listCityStatsBean1 = null;
		for (int geoid : setGeo) {
			listCityStatsBean1 = new ArrayList<GeoCityBean>();
			for (GeoCityBean cityStatsBean : listCityStatsBean) {
				if (geoid == cityStatsBean.getGeoid()) {
					listCityStatsBean1.add(cityStatsBean);
				}
			}
			Response response = iCampaignDao.campaignCountryPlatformStats(campaign_id, geoid, mapBrowser);

			listPlatformStatBean = (List<PlatformStatBean>) response.getData();
			for (int browserid : setBrowser) {
				boolean flag = Utility.containsBrowserId(listPlatformStatBean, browserid);
				if (!flag) {
					listPlatformStatBean.add(new PlatformStatBean(browserid, mapBrowser));
				}
			}
			Collections.sort(listPlatformStatBean);
			listGeoStatBean.add(new GeoStatBean(geoid, mapGeo.get(geoid), listPlatformStatBean, listCityStatsBean1));
		}
		logger.info("getStats listGeoStatBean [ " + listGeoStatBean+" ]");
		return listGeoStatBean;
	}



	protected String[] getVersionParts(String version) {
		return version.split("\\.");
	}

	static class PlatformComparator implements Comparator<CampaignBean> {

		@Override
		public int compare(CampaignBean o1, CampaignBean o2) {
			// Split version into parts
			String parts1[] = getVersionParts(o1.getPlatform()), parts2[] = getVersionParts(o2.getPlatform());

			// Go through common prefix left to right, first part which is
			// higher indicates
			// higher version (4.2.1 > 4.2.0 > 3.9.9)
			for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
				int partComparison = compareVersionPart(parts1[i], parts2[i]);
				if (partComparison != 0) {
					return partComparison;
				}
			}

			// Common prefix is the same; longer value means higher version
			// (3.2.1 > 3.2)
			if (parts1.length > parts2.length) {
				return 1;
			} else if (parts1.length < parts2.length) {
				return -1;
			} else {
				return 0;
			}

		}

		protected String[] getVersionParts(String version) {
			return version.split("\\.");
		}

		protected int compareVersionPart(String part1, String part2) {
			int versionPart1 = Integer.parseInt(part1), versionPart2 = Integer.parseInt(part2);

			if (versionPart1 > versionPart2) {
				return 1;
			} else if (versionPart1 < versionPart2) {
				return -1;
			} else {
				return 0;
			}
		}

	}


	static class IdComparator implements Comparator<CampaignBean> {

		@Override
		public int compare(CampaignBean o1, CampaignBean o2) {

			int id1 = o1.getCampaign_id();
			int id2 = o2.getCampaign_id();
			if (o1.getPlatform().equals(o2.getPlatform())) {
				if (id1 > id2) {
					return 1;
				} else if (id1 < id2) {
					return -1;
				} else {
					return 0;
				}
			}else return 0;

		}

	}

}

