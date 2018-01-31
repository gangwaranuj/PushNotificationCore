
package com.saphire.iopush.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.bean.WelcomeStatsBean;
import com.saphire.iopush.cache.Cache;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.dao.ISegmentDao;
import com.saphire.iopush.dao.IWelcomeDao;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.service.IWelcomeService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;


@Service
public class WelcomeServiceImpl implements IWelcomeService{

	@Autowired IWelcomeDao iWelcomeDao;
	@Autowired ICampaignDao iCampaignDao;
	@Autowired Cache cache;
	@Autowired JmsTemplate jmsTemplate;
	@Autowired Properties myProperties;
	@Autowired ISegmentDao iSegmentDao;


	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public JsonResponse<WelcomeBean> pendingWelcome(WelcomeBean welcomeBean, Integer productId ) {
		//	productId = 1;
		logger.info("pendingWelcome welcomeBean content [ "+welcomeBean.toString()+" ]"); 
		JsonResponse<WelcomeBean> jResponse = new JsonResponse<WelcomeBean>(); ;
		Response response = new Response();
		IopushWelcome iopushWelcome = new IopushWelcome();
		IopushProduct iopushProduct = new IopushProduct();
		int diffstart=0,diffend=0;  
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		logger.info("pendingWelcome Recieved Request for product Id[" + productId + "]");
		try{
			int country_id = Utility.parseCountry(welcomeBean.getCountries());
			if(country_id>0)
			{
				response = iCampaignDao.findCountryTimeZone(country_id);
				String timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				Date modificationDate = (new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS)).parse(dtf.print(dt));
				iopushWelcome.setCreationDate(modificationDate);
				iopushWelcome.setModificationDate(modificationDate);
			}
			else
			{
				iopushWelcome.setCreationDate(new Date());
				iopushWelcome.setModificationDate(new Date());				
			}
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(welcomeBean.getImagePath());
			String filename = System.currentTimeMillis() + "." + ext;
			String base64Image = welcomeBean.getImagePath().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();


			//			product 
			iopushProduct.setProductID(productId);
			iopushWelcome.setIopushProduct(iopushProduct);

			iopushWelcome.setImagePath(readnotificationPath + filename);
			welcomeBean.setImagePath(iopushWelcome.getImagePath());
			iopushWelcome.setSource(welcomeBean.getSource());
			iopushWelcome.setGeneric(welcomeBean.getGeneric());
			iopushWelcome.setWelcomeName(welcomeBean.getWelcomeName());
			iopushWelcome.setWelcomeStatus(Constants.PENDING);
			iopushWelcome.setForwardUrl(welcomeBean.getForwardUrl());
			iopushWelcome.setSegmented(welcomeBean.isSegmented());
			iopushWelcome.setDescription(welcomeBean.getDescription());
			iopushWelcome.setUnlimited(welcomeBean.isUnlimited());
			iopushWelcome.setCreatedBy(Utility.getUserName());
			iopushWelcome.setModificedBy(Utility.getUserName());
			iopushWelcome.setTitle(welcomeBean.getTitle());
			iopushWelcome.setWelcomeId(welcomeBean.getWelcomeId());
			iopushWelcome.setActive(welcomeBean.isActive());
			//			rules part
			iopushWelcome.setCountries(welcomeBean.getCountries());
			iopushWelcome.setPlatform(welcomeBean.getPlatform());
			iopushWelcome.setIsps(welcomeBean.getIsps());
			iopushWelcome.setCities(welcomeBean.getCities());
			iopushWelcome.setSegmentSelected(welcomeBean.getSegmentSelected());
			iopushWelcome.setSubscribed(welcomeBean.getSubscribed());
			iopushWelcome.setSegment_id(welcomeBean.getSegments());
			iopushWelcome.setSegmentType_id(welcomeBean.getSegmentTypes());
			iopushWelcome.setLargeImage(welcomeBean.isLargeImage());
			
			//			Banner Image
			if(welcomeBean.isLargeImage())
			{
				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(welcomeBean.getBannerImage());
				filename = iopushWelcome.getWelcomeName() + "_banner." + banner;
				base64Image = welcomeBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushWelcome.setBannerImage(readBannerPath+filename);
				
			}


			//			dates
			if(!welcomeBean.isUnlimited())
			{
				iopushWelcome.setWelcomeEndDate(welcomeBean.getWelcomeEndDate());
				iopushWelcome.setWelcomeScheduleDate(welcomeBean.getWelcomeScheduleDate());
				diffstart = (int) Utility.diff(welcomeBean.getWelcomeCurrentDate(),welcomeBean.getWelcomeScheduleDate(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				diffend = (int) Utility.diff(welcomeBean.getWelcomeCurrentDate(),welcomeBean.getWelcomeEndDate(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				iopushWelcome.setWelcomeScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
				iopushWelcome.setWelcomeEndDateInEdt(Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));
				
			}
			else
			{
				iopushWelcome.setWelcomeEndDate("");
				iopushWelcome.setWelcomeScheduleDate(welcomeBean.getWelcomeCurrentDate());
				diffstart = (int) Utility.diff(welcomeBean.getWelcomeCurrentDate(),iopushWelcome.getWelcomeScheduleDate(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				iopushWelcome.setWelcomeScheduleDateInEdt(Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS));

			}
			if(welcomeBean.getWelcomeId() > 0)
			{
				iopushWelcome.setWelcomeId(welcomeBean.getWelcomeId());
				response = this.iWelcomeDao.update(iopushWelcome);
			}
			else
			{
				response = this.iWelcomeDao.save(iopushWelcome);
				welcomeBean.setWelcomeId(response.getIntegerResult());
			}

			// data has been saved in welcome table
			Date currentTimeServerCountry = Utility.addMin(15, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
			if(welcomeBean.isUnlimited() || (currentTimeServerCountry.after(iopushWelcome.getWelcomeScheduleDateInEdt()) && currentTimeServerCountry.before(iopushWelcome.getWelcomeEndDateInEdt())) || currentTimeServerCountry.equals(iopushWelcome.getWelcomeScheduleDateInEdt()) )
			{
				if(welcomeBean.isActive())
				{
					welcomeBean.setBannerImage(iopushWelcome.getBannerImage());
					
					launchWelcome(iopushWelcome, welcomeBean, productId);
				}	
			}
			if(response.getStatus())
			{
				logger.info("pendingWelcome Message saved successfully");
				jResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS,welcomeBean);
			}
		}
		catch (Exception e) {
			logger.error("An Error occurred while save/update a welcome message",e);
			jResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, "Failed to save or update records");
		}
		return jResponse;

	}


	@Override
	public JsonResponse<WelcomeBean> draftWelcome(WelcomeBean welcomeBean, Integer productId ) {
		//	productId = 1;
		logger.info("draftWelcome welcomeBean content [ "+welcomeBean.toString()+" ]"); 
		JsonResponse<WelcomeBean> jResponse = new JsonResponse<WelcomeBean>() ;
		Response response = new Response();
		IopushWelcome iopushWelcome = new IopushWelcome();
		IopushProduct iopushProduct = new IopushProduct();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		logger.info("pendingWelcome Recieved Request for product Id[" + productId + "]");
		try{
			
			int country_id = Utility.parseCountry(welcomeBean.getCountries());
			if(country_id>0)
			{
				response = iCampaignDao.findCountryTimeZone(country_id);
				String timeZone = ((IopushTimeZone) response.getScalarResult()).getTimezone();
				DateTime dt = new DateTime().withZone(DateTimeZone.forID(timeZone));
				DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
				Date modificationDate = (new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS)).parse(dtf.print(dt));
				iopushWelcome.setCreationDate(modificationDate);
				iopushWelcome.setModificationDate(modificationDate);
			}
			else
			{
				iopushWelcome.setCreationDate(new Date());
				iopushWelcome.setModificationDate(new Date());				
			}
			String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
			String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
			FileOutputStream fos = null;
			String ext = Utility.getExtension(welcomeBean.getImagePath());
			String filename = System.currentTimeMillis() + "." + ext;
			String base64Image = welcomeBean.getImagePath().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			fos = new FileOutputStream(new File(notificationPath + filename));
			fos.write(imageBytes);
			fos.close();


			//			product 
			iopushProduct.setProductID(productId);
			iopushWelcome.setIopushProduct(iopushProduct);
			iopushWelcome.setImagePath(readnotificationPath + filename);
			iopushWelcome.setSource(welcomeBean.getSource());
			iopushWelcome.setGeneric(welcomeBean.getGeneric());
			iopushWelcome.setWelcomeName(welcomeBean.getWelcomeName());
			iopushWelcome.setWelcomeStatus(Constants.DRAFT);
			iopushWelcome.setForwardUrl(welcomeBean.getForwardUrl());
			iopushWelcome.setSegmented(welcomeBean.isSegmented());
			iopushWelcome.setUnlimited(welcomeBean.isUnlimited());
			iopushWelcome.setCreatedBy("");
			iopushWelcome.setModificedBy("");
			iopushWelcome.setTitle(welcomeBean.getTitle());
			iopushWelcome.setWelcomeId(welcomeBean.getWelcomeId());
			iopushWelcome.setDescription(welcomeBean.getDescription());
			iopushWelcome.setActive(welcomeBean.isActive());
			//			rules part
			iopushWelcome.setCountries(welcomeBean.getCountries());
			iopushWelcome.setPlatform(welcomeBean.getPlatform());
			iopushWelcome.setIsps(welcomeBean.getIsps());
			iopushWelcome.setCities(welcomeBean.getCities());
			iopushWelcome.setSegmentSelected(welcomeBean.getSegmentSelected());
			iopushWelcome.setWelcomeEndDate("");
			iopushWelcome.setWelcomeScheduleDate("");
			iopushWelcome.setSubscribed(welcomeBean.getSubscribed());
			iopushWelcome.setSegment_id(welcomeBean.getSegments());
			iopushWelcome.setSegmentType_id(welcomeBean.getSegmentTypes());
			iopushWelcome.setLargeImage(welcomeBean.isLargeImage());
			//			Banner Image
			if(welcomeBean.isLargeImage())
			{
				String bannerPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readBannerPath = Constants.NOTIFICATION_FOLDER_READ;
				fos = null;
				String banner = Utility.getExtension(welcomeBean.getBannerImage());
				filename = iopushWelcome.getWelcomeName() + "_banner." + banner;
				base64Image = welcomeBean.getBannerImage().split(",")[1];
				imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(bannerPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushWelcome.setBannerImage(readBannerPath+filename);
			}
			if(welcomeBean.getWelcomeId() > 0)
			{
				response = this.iWelcomeDao.update(iopushWelcome);
				if(response.getStatus())
				{
					logger.info("pendingWelcome Message saved successfully");
					jResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, welcomeBean);
				}
			}
			else
			{
				response = this.iWelcomeDao.save(iopushWelcome);
				if(response.getStatus())
				{
					welcomeBean.setWelcomeId(response.getIntegerResult());
					logger.info("pendingWelcome Message saved successfully");
					jResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, welcomeBean);
				}
			}

		}
		catch (Exception e) {
			logger.error("An Error occurred while save/update a welcome message",e);
			jResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, "Failed to save or update records");
		}
		return jResponse;

	}

	@Override
	public ResponseMessage deleteWelcome(WelcomeBean welcomeBean, Integer productId) {
		//productId = 1;
		logger.info("deleteWelcome [ "+welcomeBean.toString()+" ]");
		ResponseMessage rResponse = new ResponseMessage();
		int welcomeId = welcomeBean.getWelcomeId();
		try {
			Response response = iWelcomeDao.deleteWelcome(welcomeId, productId);
			if (response.getIntegerResult() > 0) {
				rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Deleted");
				logger.info("deleteWelcome welcomeId [ " + welcomeId + " ] Successfully Deleted");
			} else {
				rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Deletion Failed");
				logger.info("deleteWelcome welcomeId [ " + welcomeId + " ] Deletion Failed");
			}
		} catch (Exception e) {
			rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("deleteWelcome welcomeId [ " + welcomeId + " ] " ,  e);
		}
		return rResponse;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage changeStatus(WelcomeBean welcomeBean, Integer productId) {

		ResponseMessage rMessage = new ResponseMessage();

		Response response = new Response();
		Response response1 = new Response();
		int welcomeId = welcomeBean.getWelcomeId();
		int status = welcomeBean.getWelcomeStatus();
		StringBuilder segmentid=new StringBuilder();
		try {

			response = iWelcomeDao.welcomeStatusAndFlag(welcomeId,status, welcomeBean.isActive(), productId);

			if (response.getIntegerResult() > 0) {
				if(status == Constants.LIVE)
				{

					IopushWelcome iopushWelcome = (IopushWelcome)response.getScalarResult() ;

					//     JsonResponse<WelcomeBean> result = fetchWelcome(welcomeBean,productId);

					// cache implemetation
					HashMap<String, String> innerMap = new HashMap<String, String>();    
					String[] Countryarr = iopushWelcome.getCountries().split(",");
					Integer[] country = new Integer[Countryarr.length];
					for(int i=0;i<Countryarr.length;i++)
					{
						country[i] = Utility.intConverter(Countryarr[i]);
						innerMap.put("Countries_"+Countryarr[i], null);
					}

					if(Countryarr.length <= 1)
					{

						if(iopushWelcome.getCities()!=null && !iopushWelcome.getCities().isEmpty() )
						{
							innerMap.put("citiesExists", null);
							String[] Cityarr = iopushWelcome.getCities().split(",");
							for(int i=0;i<Cityarr.length;i++)
							{
								innerMap.put("Cities_"+Cityarr[i], null);
							}
						}
					}
					
					/*if(iopushWelcome.getSegment_id()!=null && !iopushWelcome.getSegment_id().isEmpty() )
					{
						String[] Segmentsarr =iopushWelcome.getSegment_id().split(",");
						for(int i=0;i<Segmentsarr.length;i++)
						{
							innerMap.put("Segments_"+Segmentsarr[i], null);
						}
					}*/
					
					//fetch segment in case of no segments but have segment types
					if(iopushWelcome.getSegment_id().equals("")  && !iopushWelcome.getSegmentType_id().equals(""))
					{

						response1 = iSegmentDao.listSegment(iopushWelcome.getSegmentType_id(),productId);
						logger.info("in camoaidihlistSegmentType response status is [ "+ response.getStatus()+" ]" );
						List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
						if (response1.getStatus()) {

							for (IopushSegmentation iopushSegment : listIopushSegment) {
								segmentid.append(iopushSegment.getSegmentId()+",");
							}
							logger.info("segmentid=="+segmentid.append("0"+","));
							iopushWelcome.setSegment_id(segmentid.substring(0, segmentid.lastIndexOf(",")));
						}
					}


					if(iopushWelcome.getSegment_id()!=null && !iopushWelcome.getSegment_id().isEmpty() )
					{
						String[] Segmentsarr =iopushWelcome.getSegment_id().split(",");
						for(int i=0;i<Segmentsarr.length;i++)
						{
							innerMap.put("Segments_"+Segmentsarr[i], null);
						}
					}
					if (iopushWelcome.getPlatform()!=null && !iopushWelcome.getPlatform().isEmpty() )
					{
						String[] Platformarr =iopushWelcome.getPlatform().split(",");
						for(int i=0;i<Platformarr.length;i++)
						{
							innerMap.put("Platform_"+Platformarr[i], null);
						}
					}
					if( iopushWelcome.getIsps()!=null && !iopushWelcome.getIsps().isEmpty() )
					{

						String[] Ispsarr = iopushWelcome.getIsps().split(",");
						for(int i=0;i<Ispsarr.length;i++)
						{
							innerMap.put("Isps_"+Ispsarr[i], null);
						}
					}
					else
					{
						response = this.iWelcomeDao.fetchIsps(country);
						List<IopushIsp> listIsp = (List<IopushIsp>) response.getData();
						for(IopushIsp isp : listIsp)
						{
							innerMap.put("Isps_"+isp.getIspId(), null);
						}
					}

					if(iopushWelcome.isLargeImage())
					{
						innerMap.put("banner_", iopushWelcome.getBannerImage());
					}
					else
					{
						innerMap.put("banner_", "");
					}

					innerMap.put("product_" + productId,null);
					logger.info("changestatus, cache size before adding element is [{}] ",cache.size());
					WelcomeBean welcomeCacheBean = new WelcomeBean(iopushWelcome);
					cache.put(welcomeCacheBean, innerMap);
					logger.info("changestatus, cache size after adding element is [{}] ",cache.size());
					logger.info(welcomeCacheBean + " launchWelcome cache content ---------------------- "+cache.get(welcomeCacheBean));
					/*String env = myProperties.getProperty("ENVIORAMENT") + ".";
		     HashMap<String,Object> hmap = new HashMap<String,Object>() ;
		     hmap.put("description", welcomeBean.getDescription());
		     hmap.put("title", welcomeBean.getTitle());
		     hmap.put("icon",   myProperties.getProperty(env + "IMAGEURL") +  welcomeBean.getImagePath()) ;
		     hmap.put("url", welcomeBean.getForwardUrl()) ;
		     hmap.put("welcome_name", String.valueOf(welcomeBean.getWelcomeName())) ;
		     hmap.put("productId",productId);
		     //hmap.put("fcm_token",welcomeBean.get);

		     logger.info("Data for Welcome " + hmap );
		    jmsTemplate.convertAndSend(myProperties.getProperty("SUBSQUEUE","SUBSCRIPTION_QUEUE"),hmap);*/

					//cache.put("IOPUSH", outerMap);
					/*cache.remove(welcomeBean);
		      logger.info("cache valuesdfsfsdfdsf="+cache.get(welcomeBean));*/


				}
				else if(status == Constants.PENDING)
				{
					logger.info("Before removing Cache size [" + cache.size() + "]");
					cache.remove(welcomeBean);
					logger.info("Cache size after removing [" + cache.size() + "]");
				}
				rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Changed The Status");
				logger.info("changeStatus welcomeId [ " + welcomeId + " ] Successfully Changed The Status");
			} else {
				rMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Status cannot be updated.");
				logger.info("changeStatus welcomeId [ " + welcomeId + " ] Status cannot be updated.");
			}
		} catch (Exception e) {
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("changeStatus welcomeId [ " + welcomeId + " ] " ,  e);
		}
		return rMessage;

	}



	@Override
	public JsonResponse<WelcomeBean> listWelcome(WelcomeBean welcomeBean, Integer productId) {
		logger.info("listWelcome [ " + welcomeBean.toString()+" ]");
		JsonResponse<WelcomeBean> jsonResponse = new JsonResponse<WelcomeBean>();
		Integer[] welcomestatus = Utility.getWelcomeStatus(welcomeBean);
		boolean flag = Utility.checkCampStatus(welcomestatus);
		boolean analytics = false;
		String searchCampignName = welcomeBean.getWelcomeName();
		int startIndex = welcomeBean.getStartIndex();
		int pagesize = welcomeBean.getPageSize();
		String welcome_date1 = welcomeBean.getWelcomeScheduleDate();
		String welcome_date2 = welcomeBean.getWelcomeEndDate();
		
		//welcomeBean.setColumnForOrdering("platform")
		String order = welcomeBean.getRequiredOrder();
		
		int count=iWelcomeDao.countWelcome(welcomestatus, searchCampignName, startIndex, pagesize, flag, analytics, welcome_date1, welcome_date2,productId);
		
		if ("platform".equals(welcomeBean.getColumnForOrdering())) {

			try {
				// int count=iWelcomeDao.countWelcome(welcomestatus,
				// searchCampignName, startIndex, pagesize, flag, analytics,
				// welcome_date1, welcome_date2,productId);
				List<Object[]> resnew = iWelcomeDao.listManageWelcomePlatform(welcomestatus, searchCampignName,
						startIndex, pagesize, flag, productId);

//				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				// String platformsConfiguration = myProperties.getProperty(env
				// + "platforms","1,2,3,4,5");
				// String platforms =null;

				if (!resnew.isEmpty()) {
					List<WelcomeBean> listWelcomebean = new ArrayList<WelcomeBean>();
					List<WelcomeBean> listWelcomeBeanFinal = new ArrayList<>();

					for (Object[] object : resnew) {
						WelcomeBean welcome = new WelcomeBean(object);
						if (!welcome.getWelcomeScheduleDate().isEmpty()) {
							String date = new SimpleDateFormat("dd/MM/yyyy")
									.format((new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)
											.parse(welcome.getWelcomeScheduleDate())));
							welcome.setWelcomeScheduleDate(date);
						}
						String platform = Utility.convertIntToStr(welcome.getPlatform());
						platform = Utility.convertStrToSortedInt(platform);
						welcome.setPlatform(platform);
						listWelcomebean.add(welcome);
					}

					// //testing code
					// Iterator<WelcomeBean> itr1 = listWelcomebean.iterator();
					// while(itr1.hasNext()){
					// WelcomeBean bean = itr1.next();
					// System.out.println("welcome id::" + bean.getWelcomeId()+
					// "welcome_platforms::"+bean.getPlatform());
					// }

					if ("asc".equals(order))
						Collections.sort(listWelcomebean, new PlatformComparator());
					else
						Collections.sort(listWelcomebean, new PlatformComparator().reversed());

					// //testing code after sorting
					// Iterator<WelcomeBean> itr3 = listWelcomebean.iterator();
					// while(itr3.hasNext()){
					// WelcomeBean bean = itr3.next();
					// System.out.println("welcome id::" + bean.getWelcomeId()+
					// "sorted welcome_platforms::"+bean.getPlatform());
					// }

					List<WelcomeBean> finalFormatList = new ArrayList<>();
					List<WelcomeBean> oneList = new ArrayList<>();
					List<WelcomeBean> twoList = new ArrayList<>();
					List<WelcomeBean> threeList = new ArrayList<>();
					List<WelcomeBean> fourList = new ArrayList<>();
					List<WelcomeBean> fiveList = new ArrayList<>();
					Iterator<WelcomeBean> itrList = listWelcomebean.iterator();

					while (itrList.hasNext()) {
						WelcomeBean bean = itrList.next();
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
						if (!oneList.isEmpty()){
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
				
					ArrayList<WelcomeBean> finalList = new ArrayList<>();
				
					int counter = 0;
					if(finalFormatList.size() > (startIndex + pagesize))
					    counter = startIndex + pagesize;
					else counter = finalFormatList.size();
						
					for (int i = startIndex; i < counter; i++) {

						finalList.add(finalFormatList.get(i));
					}

					Iterator<WelcomeBean> itr = finalList.iterator();

					while (itr.hasNext()) {
						WelcomeBean bean = itr.next();
						String platform = Utility.convertStringPlatformSorted(bean.getPlatform());
						bean.setPlatform(platform);
						logger.debug("Welcome Platform based sorted list [welcome_id:: " + bean.getWelcomeId()
								+ ",platforms:: ]" + bean.getPlatform());
						listWelcomeBeanFinal.add(bean);
					}

					jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, listWelcomeBeanFinal, count);

					logger.info("listrssStats Fetch Total Size [ " + listWelcomeBeanFinal.size() + " ]");
				} else {
					jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, "NO DATA FOUND");
					logger.info("listWelcome NO DATA FOUND");
				}
			} catch (Exception e) {
				jsonResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, e.getMessage());
				logger.error("listWelcome ", e);
				e.printStackTrace();
			}

		} else {

			try {
				// int count=iWelcomeDao.countWelcome(welcomestatus,
				// searchCampignName, startIndex, pagesize, flag, analytics,
				// welcome_date1, welcome_date2,productId);
				List<Object[]> resnew = iWelcomeDao.listManageWelcome(welcomestatus, searchCampignName, startIndex,
						pagesize, flag, productId, welcomeBean.getColumnForOrdering(), welcomeBean.getRequiredOrder());
				if (!resnew.isEmpty()) {
					List<WelcomeBean> listWelcomebean = new ArrayList<WelcomeBean>();
					for (Object[] object : resnew) {
						WelcomeBean welcome = new WelcomeBean(object);
						if (!welcome.getWelcomeScheduleDate().isEmpty()) {
							String date = new SimpleDateFormat("dd/MM/yyyy")
									.format((new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS)
											.parse(welcome.getWelcomeScheduleDate())));
							welcome.setWelcomeScheduleDate(date);
						}
						listWelcomebean.add(welcome);
					}
					jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, listWelcomebean, count);

					logger.info("listrssStats Fetch Total Size [ " + listWelcomebean.size() + " ]");
				} else {
					jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, "NO DATA FOUND");
					logger.info("listWelcome NO DATA FOUND");
				}
			} catch (Exception e) {
				jsonResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, e.getMessage());
				logger.error("listWelcome ", e);
				e.printStackTrace();
			}

		}
		
		return jsonResponse;
	}





	@Override
	public JsonResponse<WelcomeBean> fetchWelcome(WelcomeBean welcomeBean, Integer productId) {
		//	productId = 1;
		logger.info("Inside fetchWelcome method welcomeBean Id is [ "+welcomeBean.getWelcomeId()+" ]");
		JsonResponse<WelcomeBean> jsonResponse = new JsonResponse<WelcomeBean>();
		Response response = new Response(); 
		int welcomeId =welcomeBean.getWelcomeId();
		try
		{
			response = iWelcomeDao.getWelcomeData(welcomeId, productId);
			if(response.getStatus())
			{
				IopushWelcome IopushWelcome = (IopushWelcome) response.getScalarResult();
				welcomeBean.setWelcomeName(IopushWelcome.getWelcomeName());
				welcomeBean.setTitle(IopushWelcome.getTitle());
				welcomeBean.setDescription(IopushWelcome.getDescription());
				welcomeBean.setImagePath(IopushWelcome.getImagePath());
				welcomeBean.setForwardUrl(IopushWelcome.getForwardUrl());
				welcomeBean.setActive(IopushWelcome.isActive());
				welcomeBean.setWelcomeEndDate(IopushWelcome.getWelcomeEndDate());
				welcomeBean.setUnlimited(IopushWelcome.isUnlimited());
				welcomeBean.setSource(IopushWelcome.getSource());
				welcomeBean.setGeneric(IopushWelcome.getGeneric());
				welcomeBean.setWelcomeStatus(IopushWelcome.getWelcomeStatus());
				welcomeBean.setSegmented(IopushWelcome.isSegmented());
				welcomeBean.setSegmentSelected(IopushWelcome.getSegmentSelected());
				welcomeBean.setSubscribed(IopushWelcome.getSubscribed());
				welcomeBean.setBannerImage(IopushWelcome.getBannerImage());
				welcomeBean.setLargeImage(IopushWelcome.isLargeImage());
				if(!IopushWelcome.isUnlimited())
				{
					welcomeBean.setWelcomeScheduleDate(IopushWelcome.getWelcomeScheduleDate());
					welcomeBean.setWelcomeEndDate(IopushWelcome.getWelcomeEndDate());
				}
				if(IopushWelcome.isSegmented())
				{
					welcomeBean.setCities(IopushWelcome.getCities());
					welcomeBean.setCountries(IopushWelcome.getCountries());
					welcomeBean.setIsps(IopushWelcome.getIsps());
					welcomeBean.setPlatform(IopushWelcome.getPlatform());
					welcomeBean.setSegments(IopushWelcome.getSegment_id());
					welcomeBean.setSegmentTypes(IopushWelcome.getSegmentType_id());
				}
				else
				{
					welcomeBean.setCities("");
					welcomeBean.setCountries("");
					welcomeBean.setIsps("");
					welcomeBean.setPlatform("");
					welcomeBean.setSegments("");
					welcomeBean.setSegmentTypes("");
				}
				jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, welcomeBean, 1);
				logger.info("fetchWelcome data successfully retrieved. Here is the welcomeBean data [ "+welcomeBean.toString()+" ]");
			}
			else
			{
				jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS, "Data not found.");
				logger.info("fetchWelcome data not found corresponding to welcomeId [ "+welcomeId+" ]");
			}
		}
		catch (Exception e) {
			jsonResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, "Oops! something went wrong.");
		}
		return jsonResponse;
	}


	@SuppressWarnings({ "unused" })
	@Override
	public ResponseMessage expireWelcome(int welcomeId, Integer productId) {
		//productId = 1;
		logger.info("expireWelcome welcomeId [ "+welcomeId+" ] productId [ "+productId+" ]"); 
		ResponseMessage rMessage = new ResponseMessage();
		WelcomeBean welcomeBean = new WelcomeBean();
		welcomeBean.setWelcomeId(welcomeId);
		try
		{
			IopushWelcome iopushWelcome = new IopushWelcome();
			iopushWelcome.setWelcomeId(welcomeId);
			iopushWelcome.setWelcomeStatus(Constants.EXPIRE);
			IopushProduct iopushProduct = new IopushProduct();
			iopushProduct.setProductID(productId);
			iopushWelcome.setIopushProduct(iopushProduct);
			Response response = iWelcomeDao.updateWelcomeEntity(iopushWelcome);
			cache.remove(welcomeBean);
			rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Notification expired successfully.");
			logger.info("expireWelcome Notification expired successfully."); 
		}
		catch (Exception e) {
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Oops! something went wrong.");
			logger.error("expireWelcome welcomeId [ " + welcomeId + " ] " ,  e);
		}
		return rMessage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage launchWelcome(IopushWelcome iopushWelcome, WelcomeBean welcomeBean, Integer productId) {
		//productId = 1;
		logger.info("launchWelcome welcomeId [ "+welcomeBean.getWelcomeId()+" ] productId [ "+productId+" ]"); 
		ResponseMessage rMessage = new ResponseMessage();
		Response response1 = new Response();
		StringBuilder segmentid=new StringBuilder();
		try
		{
			Response response = null;
			//change status 
			iopushWelcome.setWelcomeId(welcomeBean.getWelcomeId());
			iopushWelcome.setWelcomeStatus(Constants.LIVE);
			IopushProduct iopushProduct = new IopushProduct();
			iopushProduct.setProductID(productId);
			iopushWelcome.setIopushProduct(iopushProduct);
			response = iWelcomeDao.updateWelcomeEntity(iopushWelcome);

			// error Part
			if (response.getIntegerResult() > 0) {

				// cache implemetation
				HashMap<String, String> innerMap = new HashMap<String, String>();    

				String[] Countryarr = welcomeBean.getCountries().split(",");
				Integer[] country = new Integer[Countryarr.length];
				for(int i=0;i<Countryarr.length;i++)
				{
					country[i] = Utility.intConverter(Countryarr[i]);
					innerMap.put("Countries_"+Countryarr[i], null);
				}

				if(Countryarr.length <= 1)
				{

					if(welcomeBean.getCities()!=null && !welcomeBean.getCities().isEmpty() )
					{
						innerMap.put("citiesExists", null);
						String[] Cityarr = welcomeBean.getCities().split(",");
						for(int i=0;i<Cityarr.length;i++)
						{
							innerMap.put("Cities_"+Cityarr[i], null);
						}
					}
				}

				if(welcomeBean.getPlatform()!=null && !welcomeBean.getPlatform().isEmpty() )
				{
					String[] Platformarr =welcomeBean.getPlatform().split(",");
					for(int i=0;i<Platformarr.length;i++)
					{
						innerMap.put("Platform_"+Platformarr[i], null);
					}
				}

				if(welcomeBean.getSegments().equals("")  && !welcomeBean.getSegmentTypes().equals(""))
				{

					response1 = iSegmentDao.listSegment(welcomeBean.getSegmentTypes(),productId);
					logger.info("in camoaidihlistSegmentType response status is [ "+ response.getStatus()+" ]" );
					List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
					if (response1.getStatus()) {

						for (IopushSegmentation iopushSegment : listIopushSegment) {
							segmentid.append(iopushSegment.getSegmentId()+",");
						}
						logger.info("segmentid=="+segmentid.append("0"+","));
						welcomeBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
					}
				}


				if(welcomeBean.getSegments()!=null && !welcomeBean.getSegments().isEmpty() )
				{
					String[] Segmentsarr =welcomeBean.getSegments().split(",");
					for(int i=0;i<Segmentsarr.length;i++)
					{
						innerMap.put("Segments_"+Segmentsarr[i], null);
					}
				}

				if(welcomeBean.getIsps()!=null && !welcomeBean.getIsps().isEmpty() )
				{

					String[] Ispsarr = welcomeBean.getIsps().split(",");
					for(int i=0;i<Ispsarr.length;i++)
					{
						innerMap.put("Isps_"+Ispsarr[i], null);
					}
				}
				else
				{
					response = this.iWelcomeDao.fetchIsps(country);
					List<IopushIsp> listIsp = (List<IopushIsp>) response.getData();
					for(IopushIsp isp : listIsp)
					{
						innerMap.put("Isps_"+isp.getIspId(), null);
					}
				}
				innerMap.put("product_" + productId,null);
				logger.info("launchWelcome, cache size before adding element is [{}] ",cache.size());
				cache.put(welcomeBean, innerMap);
				logger.info("launchWelcome, cache size after adding element is [{}] ",cache.size());
				// cache.put("IOPUSH", outerMap);
				rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Launch");
			}
		}
		catch (Exception e) {
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Oops! something went wrong.");
			logger.error("expireWelcome welcomeId [ " + welcomeBean.getWelcomeId() + " ] " ,  e);
		}
		return rMessage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage autoExpireWelcome() {

		ResponseMessage rMessage = new ResponseMessage();
		Response response = iWelcomeDao.listEligibleForExpireWelcome();
		try
		{
			if(response.getStatus())
			{
				List<IopushWelcome> listWelcome = (List<IopushWelcome>) response.getData();
				for(IopushWelcome iopushWelcome : listWelcome)
				{
					int welcomeId = iopushWelcome.getWelcomeId();
					Date currentTimeServerCountry = Utility.addMin(0, Constants.DATE_FORMAT);
					if(currentTimeServerCountry.after(iopushWelcome.getWelcomeEndDateInEdt()) || currentTimeServerCountry.equals(iopushWelcome.getWelcomeEndDateInEdt()))
					{
						rMessage = expireWelcome(welcomeId, iopushWelcome.getIopushProduct().getProductID());
					}
				}
			}
		}
		catch(Exception e)
		{
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to autoexpire notification");
			logger.error("Failed to launch notification with the exception trace ",  e); 
		}

		return rMessage;
	}


	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage autolaunchWelcome() {
		ResponseMessage rMessage = new ResponseMessage();
		Response response = iWelcomeDao.listEligibleForLaunchWelcome();
		try
		{
			if(response.getStatus())
			{
				List<IopushWelcome> listWelcome = (List<IopushWelcome>) response.getData();
				for(IopushWelcome iopushWelcome : listWelcome)
				{
					WelcomeBean welcomeBean = new WelcomeBean();
					if(!iopushWelcome.isUnlimited())
					{
						welcomeBean.setWelcomeId(iopushWelcome.getWelcomeId());
						Date currentTimeServerCountry = Utility.addMin(0, Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
						/*int diffstart = (int) Utility.diff(LocalDateTime.now().toString(),welcomeBean.getWelcomeScheduleDate(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
						int diffend = (int) Utility.diff(LocalDateTime.now().toString(),welcomeBean.getWelcomeEndDate(), Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
						Date scheduledate = Utility.addMin(diffstart, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
						Date enddate = Utility.addMin(diffend, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
						 */if( (currentTimeServerCountry.after(iopushWelcome.getWelcomeScheduleDateInEdt()) && currentTimeServerCountry.before(iopushWelcome.getWelcomeEndDateInEdt())) || currentTimeServerCountry.equals(iopushWelcome.getWelcomeScheduleDateInEdt())  )
						 {
							 rMessage = launchWelcome(iopushWelcome, welcomeBean, welcomeBean.getIopushProduct().getProductID());
						 }
					}
					/*else
					{
						rMessage = launchWelcome(new WelcomeBean(welcomeBean), welcomeBean.getIopushProduct().getProductID());
					}*/
				}
			}
		}
		catch(Exception e)
		{
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to autoexpire notification");
			logger.error("Failed to launch notification with the exception trace ",  e); 
		}

		return rMessage;
	}


	@Override
	public ResponseMessage checkWelcomeName(String welcomeName,Integer welcomeId, Integer attribute) {
		//attribute = 1;
		logger.info("checkWelcomeName->" + welcomeName+welcomeId);
		ResponseMessage rm = null;
		try {
			Response response = iWelcomeDao.findWelcome(welcomeName,welcomeId,attribute);
			if (response.getStatus()) {
				rm = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Welcome " + welcomeName + " already exists");
				logger.info("checkWelcomeName welcomeName "+welcomeName+" alresdy exists");
			} else {
				rm = new ResponseMessage(Constants.SUCCESS_CODE, "Welcome " + welcomeName + " does not exist");
				logger.info("checkWelcomeName welcomeName "+welcomeName+" does not exist");
			}
		} catch (Exception e) {
			rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("checkWelcomeName[ " , e+" ]");
			e.printStackTrace();
		}
		logger.info("welcomeName Response[ " + rm+" ]");
		return rm;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage autoStartWelcome() {
		ResponseMessage rMessage = new ResponseMessage();
		Response response = iWelcomeDao.listEligibleForCacheWelcome();
		Response response1 = new Response();
		StringBuilder segmentid = new StringBuilder();
		try
		{
			logger.info("Welcome status [" + response.getStatus() + " and elements [" + response.getData());
			if(response.getStatus())
			{
				List<IopushWelcome> listWelcome = (List<IopushWelcome>) response.getData();
				for(IopushWelcome welcomeBean : listWelcome)
				{

					HashMap<String, String> innerMap = new HashMap<String, String>();    

					String[] Countryarr = welcomeBean.getCountries().split(",");
					Integer[] country = new Integer[Countryarr.length];
					for(int i=0;i<Countryarr.length;i++)
					{
						country[i] = Utility.intConverter(Countryarr[i]);
						innerMap.put("Countries_"+Countryarr[i], null);
					}

					if(Countryarr.length <= 1)
					{

						if( welcomeBean.getCities()!=null && !welcomeBean.getCities().isEmpty() )
						{
							innerMap.put("citiesExists", null);
							String[] Cityarr = welcomeBean.getCities().split(",");
							for(int i=0;i<Cityarr.length;i++)
							{
								innerMap.put("Cities_"+Cityarr[i], null);
							}
						}
					}

					if(welcomeBean.getSegment_id().equals("")  && !welcomeBean.getSegmentType_id().equals(""))
					{

						response1 = iSegmentDao.listSegment(welcomeBean.getSegmentType_id(),welcomeBean.getIopushProduct().getProductID());
						logger.info("in camoaidihlistSegmentType response status is [ "+ response.getStatus()+" ]" );
						List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
						if (response1.getStatus()) {

							for (IopushSegmentation iopushSegment : listIopushSegment) {
								segmentid.append(iopushSegment.getSegmentId()+",");
							}
							logger.info("segmentid=="+segmentid.append("0"+","));
							welcomeBean.setSegment_id(segmentid.substring(0, segmentid.lastIndexOf(",")));
						}
					}

					if(welcomeBean.getSegment_id()!=null && !welcomeBean.getSegment_id().isEmpty() )
					{
						String[] Segmentsarr =welcomeBean.getSegment_id().split(",");
						for(int i=0;i<Segmentsarr.length;i++)
						{
							innerMap.put("Segments_"+Segmentsarr[i], null);
						}
					}
					if( welcomeBean.getPlatform()!=null && !welcomeBean.getPlatform().isEmpty() )
					{
						String[] Platformarr =welcomeBean.getPlatform().split(",");
						for(int i=0;i<Platformarr.length;i++)
						{
							innerMap.put("Platform_"+Platformarr[i], null);
						}
					}
					if(welcomeBean.getIsps()!=null && !welcomeBean.getIsps().isEmpty() )
					{

						String[] Ispsarr = welcomeBean.getIsps().split(",");
						for(int i=0;i<Ispsarr.length;i++)
						{
							innerMap.put("Isps_"+Ispsarr[i], null);
						}
					}
					else
					{
						response = this.iWelcomeDao.fetchIsps(country);
						List<IopushIsp> listIsp = (List<IopushIsp>) response.getData();
						for(IopushIsp isp : listIsp)
						{
							innerMap.put("Isps_"+isp.getIspId(), null);
						}
					}

					innerMap.put("product_" + welcomeBean.getIopushProduct().getProductID(),null);
					cache.put(new WelcomeBean(welcomeBean), innerMap);
					
					rMessage = new ResponseMessage(Constants.SUCCESS_CODE, " pushed cache in autostart ");
				}
				logger.info("cache populated [" + cache.lRUMap + "]");
				logger.info("autoStartUp, size of cache is {}", cache.size());
			}
		}
		catch(Exception e)
		{
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to push cache in autostart ");
			logger.error("Failed to push cache in autostart  with the exception trace ",  e); 
		}

		return rMessage;}


	@Override
	public JsonResponse<WelcomeStatsBean> listwelcomeStats(WelcomeStatsBean welcomeFeedBean) {
		JsonResponse<WelcomeStatsBean> jsonResponse = new JsonResponse<WelcomeStatsBean>();
		boolean analytics = true;
		boolean flag =false;
		int startIndex = welcomeFeedBean.getStartIndex();
		int pagesize = welcomeFeedBean.getPageSize();
		String welcomeName = welcomeFeedBean.getWelcomeName();
		String welcome_date1 = welcomeFeedBean.getStartDate();
		String welcome_date2 = welcomeFeedBean.getEndDate();
		try {
			logger.info("listwelcomeStats Fetching the record for productid [" + welcomeFeedBean.getProductId() + "]");

			int count = iWelcomeDao.countWelcome(startIndex, pagesize, analytics, welcomeName, welcome_date1, welcome_date2,welcomeFeedBean.getProductId());
			List<Object[]> resnew = iWelcomeDao.listWelcomeStats(startIndex, pagesize, flag,analytics, welcomeName, welcome_date1, welcome_date2,welcomeFeedBean.getProductId(), welcomeFeedBean.getColumnForOrdering(), welcomeFeedBean.getRequiredOrder());
			List<WelcomeStatsBean> listWelcomebean = new ArrayList<WelcomeStatsBean>();

			logger.info("listwelcomeStats response size=="+resnew.size());
			if(resnew.size()>0){
				
				int welcomestatus;
				int welcomeid;
				String welcomename;
				Date date;
				int sent;
				int click;			
				int open;
				int welcomeStatsId;

				for (Object[] obj : resnew) {
					welcomename = (String) obj[0];
					welcomeid = (int) obj[1];
					click = (int) obj[2];
					date = (Date) obj[3];
					open = (int) obj[4];
					sent = (int) obj[6];
					welcomeStatsId = (int) obj[7];
					welcomestatus = (int) obj[8];
					listWelcomebean.add(new WelcomeStatsBean(welcomestatus, welcomeid, welcomename , date,sent,click,open,welcomeStatsId));
					jsonResponse = new JsonResponse<WelcomeStatsBean>("Success", listWelcomebean,count);
				} 
			}else {
				jsonResponse = new JsonResponse<WelcomeStatsBean>(Constants.SUCCESS, "NO DATA FOUND");
				logger.info("listwelcomeStats NO DATA FOUND");
			}

		} catch (Exception e) {
			jsonResponse = new JsonResponse<WelcomeStatsBean>(Constants.ERROR, e.getMessage());
			e.printStackTrace();
			logger.error("listwelcomeStats error" , e);
		}
		return jsonResponse;

	}
	
	


	static class PlatformComparator implements Comparator<WelcomeBean> {

		@Override
		public int compare(WelcomeBean o1, WelcomeBean o2) {
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
	protected String[] getVersionParts(String version) {
		return version.split("\\.");
	}

	
	static class IdComparator implements Comparator<WelcomeBean> {

		@Override
		public int compare(WelcomeBean o1, WelcomeBean o2) {

			int id1 = o1.getWelcomeId();
			int id2 = o2.getWelcomeId();
			if (o1.getPlatform().equals(o2.getPlatform())) {
				if (id1 > id2) {
					return 1;
				} else if (id1 < id2) {
					return -1;
				} else {
					return 0;
				}
			} else
				return 0;

		}

	}
}
