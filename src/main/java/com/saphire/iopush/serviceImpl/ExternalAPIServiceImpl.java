package com.saphire.iopush.serviceImpl;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CancelAgreementBean;
import com.saphire.iopush.bean.GeoCityBean;
import com.saphire.iopush.bean.GetInTouchBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.SubscriptionBean;
import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.cache.Cache;
import com.saphire.iopush.dao.ICustomNotificationDao;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.dao.IPaypalDao;
import com.saphire.iopush.dao.IUserCategoryDao;
import com.saphire.iopush.model.IopushCityDetails;
import com.saphire.iopush.model.IopushCustomNotification;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushPlatformCategory;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.model.IopushUserplan;
import com.saphire.iopush.service.IExternalAPIService;
import com.saphire.iopush.service.IWelcomeService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.PaypalResponse;
import com.saphire.iopush.utils.PaypalUtility;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;
import com.sun.syndication.io.impl.Base64;

@Service
@Transactional(readOnly = false)
public class ExternalAPIServiceImpl implements IExternalAPIService {

	@Autowired IExternalAPIDao iExternalAPIDao;
	@Autowired Properties myProperties;
	@Autowired IWelcomeService iWelcomeService;
	//	@Autowired JavaMailSender mailSender;

	@Autowired JavaMailSender mailSender_service;
	@Autowired JavaMailSender mailSender_support;
	@Autowired Cache cache ;
	@Autowired JmsTemplate jmsTemplate;
	@Autowired IUserCategoryDao iUserCategoryDao;

	@Autowired ICustomNotificationDao iCustomNotificationDao;
	@Autowired IPaypalDao iPaypalDao;

	private Logger logger = LoggerFactory.getLogger(ExternalAPIServiceImpl.class); 

	Logger subscription_logger = LoggerFactory.getLogger("subscription");

	@SuppressWarnings("unchecked")
	@Override
	//@Transactional(propagation = Propagation.REQUIRED)
	public JsonResponse<WelcomeBean> subscriptionApi(SubscriptionBean subscriptionBean) {
		subscription_logger.debug("subscriptionApi, subscriptionBean content [ {} ] ",subscriptionBean.toString()); 	
		JsonResponse<WelcomeBean> jsonResponse = new JsonResponse<WelcomeBean>();
		IopushProduct iopushProduct = new IopushProduct();
		int ispID=0, geo_id = 0, city_id=0, productID = 0,segment_id=0;
		try
		{
			String ip = subscriptionBean.getIp();
			int category = subscriptionBean.isMobile()? 1 : 2;
			GeoCityBean geoCityBean = Utility.getDetails(ip, myProperties);
			List<IopushPlatformDetails> listIopushPlatformDetails = new ArrayList<IopushPlatformDetails>();
			// if request is coming from desktop then only we need to check for browser name
			if(category == 2)
			{
				// check whether browser is present in the list or not 
				
				Response checkPlatformResponse = this.iExternalAPIDao.findPlatformName(subscriptionBean.getBrowser_name());
				if(!checkPlatformResponse.getStatus()){
					subscription_logger.debug("subscriptionApi browser name [ {} ]", subscriptionBean.getBrowser_name() );
					jsonResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, "Invalid Browser name");
					return jsonResponse;
				}
				listIopushPlatformDetails = (List<IopushPlatformDetails>) checkPlatformResponse.getData() ;
			}
			
			//check whether the product is already registered or not 
			
			Response productDetailsResponse = this.iExternalAPIDao.findProductID(subscriptionBean.getPid());
			if(productDetailsResponse.getStatus())
			{
				iopushProduct = (IopushProduct) productDetailsResponse.getScalarResult();
				productID = iopushProduct.getProductID();
				subscription_logger.debug("subscriptionApi productID is [ {} ]", productID);
			}
			else
			{
				subscription_logger.error("subscriptionApi productID [ {} ] not found.", productID );
				jsonResponse = new JsonResponse<WelcomeBean>(Constants.ERROR, "invalid product name");
				return jsonResponse;
			}
			//find segment id
			segment_id = getSegmentID(subscriptionBean);
			subscription_logger.debug("subscriptionApi segment ID is [ {} ]", segment_id );
			
			//fetch geo details			
			geo_id = getGeoId(geoCityBean);
			subscription_logger.debug("subscriptionApi geo_id is [ {} ]", geo_id);
			
			// fetch city details
			city_id = getCityID(geoCityBean, geo_id);
			subscription_logger.debug("subscriptionApi city_id is [ {} ] ", city_id);
			
			// fetch isp id
			ispID = getISPID(geoCityBean, geo_id);
			subscription_logger.debug("subscriptionApi,IspID is [ {} ]", ispID );
			
			Response saveSubscriberResponse = saveSubscriber(geo_id, city_id, ispID, category, segment_id, productID, iopushProduct, subscriptionBean, listIopushPlatformDetails.get(0));
			subscription_logger.debug("subscriptionApi, response status while saving the subscriber is [ {} ] ", saveSubscriberResponse.getStatus());
			if(saveSubscriberResponse.getStatus())
			{
				subscription_logger.info("subscriptionApi , subscriber data successfully saved.");
				//update userCategory table				
				this.updateUserCategory(productID);
				
				IopushSubscribers iopushSubscribers = (IopushSubscribers) saveSubscriberResponse.getScalarResult();
				

				List<Object> welcomeMessagesList = new ArrayList<Object>();
				subscription_logger.debug("cache data [ {} ]",cache.entrySet().size());

				for(Map.Entry<Object, HashMap<String,String>> welcomeEntry: cache.entrySet())
				{
					welcomeMessagesList = addWelcomeInList(welcomeMessagesList, welcomeEntry, iopushSubscribers, subscriptionBean, productID);
				}
				
				jsonResponse = new JsonResponse(Constants.SUCCESS,welcomeMessagesList);
			}
		}
		catch (Exception e) {
			jsonResponse = new JsonResponse<WelcomeBean>(Constants.SUCCESS,"Oops! something went wrong.");
			subscription_logger.error("An error occured while subsribe user[ {} ]",e);
			return jsonResponse;

		}
		return jsonResponse;
	}


	private Response saveSubscriber(int geo_id, int city_id, int ispID, int category, int segment_id, int productID, IopushProduct iopushProduct, SubscriptionBean subscriptionBean, IopushPlatformDetails iopushPlatformDetails) {
		IopushSubscribers iopushSubscribers = new IopushSubscribers();
		
		IopushGeoDetails iopushGeoDetails = new IopushGeoDetails();
		iopushGeoDetails.setGeoId(geo_id);
		
		IopushCityDetails iopushCityDetails = new IopushCityDetails();
		iopushCityDetails.setCityId(city_id);
		
		IopushPlatformCategory iopushPlatformCategory = new IopushPlatformCategory();
		iopushPlatformCategory.setCategoryID(category);
		
		IopushIsp iopushIsp = new IopushIsp();
		iopushIsp.setIspId(ispID);
		
		iopushSubscribers.setFcmToken(subscriptionBean.getCurrentToken());
		iopushSubscribers.setInstallationDate(new Date());
		iopushSubscribers.setIopushCityDetails(iopushCityDetails);
		iopushSubscribers.setIopushGeoDetails(iopushGeoDetails);
		iopushSubscribers.setIopushIsp(iopushIsp);
		iopushSubscribers.setIopushPlatformcategory(iopushPlatformCategory);
		iopushSubscribers.setIopushSegmentation(segment_id);
		iopushSubscribers.setIp((subscriptionBean.getIp().isEmpty())? "" : subscriptionBean.getIp());
		
		// if request is coming from the mobile then platform is android only
		if(category == 1)
		{
			IopushPlatformDetails platformDetails= new IopushPlatformDetails();
			platformDetails.setPlatformID(5);
			iopushSubscribers.setIopushPlatformDetails(platformDetails);
			iopushSubscribers.setOsName("Android");
		}
		else
		{
			iopushSubscribers.setIopushPlatformDetails(iopushPlatformDetails);
			iopushSubscribers.setOsName(subscriptionBean.getOs_name());
		}
		iopushProduct.setProductID(productID);
		iopushSubscribers.setIopushProduct(iopushProduct);
		iopushSubscribers.setIopushToken(subscriptionBean.getIopushToken());
		iopushSubscribers.setOsLanguage(subscriptionBean.getOs_language());
		
		subscription_logger.debug("Subscriber record - ISP ID[ " + iopushSubscribers.getIopushIsp().getIspId() + "], Country ID [" 
				+ iopushSubscribers.getIopushGeoDetails().getGeoId() + "], PlatformId [" +  iopushSubscribers.getIopushPlatformDetails().getPlatformID()
				+"], ProductId [" + productID + "] , SegmentID ["+segment_id +"]");
		return this.iExternalAPIDao.saveSubscriber(iopushSubscribers);
	}


	private int getSegmentID(SubscriptionBean subscriptionBean) {
		int segment_id = 0;
		if(this.iExternalAPIDao.findSegmentID(subscriptionBean.getSegment_id()).getStatus())
		{
			segment_id = this.iExternalAPIDao.findSegmentID(subscriptionBean.getSegment_id()).getIntegerResult();
		}
		else
		{
			segment_id = 0;
		}
		return segment_id;
	}


	private int getISPID(GeoCityBean geoCityBean, int geo_id) {
		int ispID = 0;
		Response ispDetails = this.iExternalAPIDao.findIspID(geoCityBean.getIsp(), geo_id);
		if(!ispDetails.getStatus())
		{
			ispID = saveISPDetails(geoCityBean.getIsp(),geo_id);
		}
		else
		{
			ispID = ispDetails.getIntegerResult();
		}
		return ispID;
	}


	private int getCityID(GeoCityBean geoCityBean, int geo_id) {
		int city_id = 0;
		Response cityDetails = this.iExternalAPIDao.findCityCode(geoCityBean.getCity_code(), geo_id);
		if(cityDetails.getStatus()){
			city_id=((IopushCityDetails)cityDetails.getScalarResult()).getCityId();
		}else{
			city_id = saveCityDetails(geoCityBean,geo_id);
		}
		return city_id;
	}


	private int getGeoId(GeoCityBean geoCityBean) {
		int geo_id = 0;
		Response geoDetails = this.iExternalAPIDao.findGeoCode(geoCityBean.getCounty_code().toLowerCase());
		if(geoDetails.getStatus())
		{
			geo_id = ((IopushGeoDetails) geoDetails.getScalarResult()).getGeoId();				
		}
		else
		{
			geo_id = saveGeoDetails(geoCityBean);
			if(geo_id>0){
				IopushTimeZone iopushTimeZone=new IopushTimeZone(geo_id,geoCityBean.getCounty_name(),geoCityBean.getTime_zone());
				geoDetails = this.iExternalAPIDao.saveTimezone(iopushTimeZone);
			}
		}
		return geo_id;
	}


	private List<Object> addWelcomeInList(List<Object> welcomeMessagesList,Entry<Object, HashMap<String, String>> welcomeEntry, IopushSubscribers iopushSubscribers, SubscriptionBean subscriptionBean, int productID) {
		Map<String,String> welcomeData = welcomeEntry.getValue();
		WelcomeBean welcomeBean = (WelcomeBean)welcomeEntry.getKey() ;
		subscription_logger.debug("Data for Welcome [" + welcomeBean + "]");
		//check whether the subscriber is eligible for welcome notifications					
		boolean isEligibleForWelcome = welcomeMessagesPresent(welcomeBean, welcomeData, iopushSubscribers, productID);
		subscription_logger.debug("isEligible value is {}", isEligibleForWelcome);
		subscription_logger.info("Welcome map criteria [{}]", welcomeData) ;
		if(isEligibleForWelcome){	
			HashMap<String,Object> welcomeMessage = setWelcomeData(welcomeBean,subscriptionBean, productID );
			welcomeMessagesList.add(welcomeMessage);
			subscription_logger.info("welcome message send successfully");
		}
		return welcomeMessagesList;
	}


	private HashMap<String, Object> setWelcomeData(WelcomeBean welcomeBean, SubscriptionBean subscriptionBean, int productID) {
		HashMap<String,Object> welcomeMessage = new HashMap<>();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		welcomeMessage.put("icon",   myProperties.getProperty(env + "IMAGEURL") +  welcomeBean.getImagePath()) ;
		welcomeMessage.put("url", welcomeBean.getForwardUrl());
		welcomeMessage.put("welcome_name", String.valueOf(welcomeBean.getWelcomeName())) ;
		welcomeMessage.put("productId",productID);
		welcomeMessage.put("fcm_token",subscriptionBean.getCurrentToken());
		welcomeMessage.put("welcome_id", welcomeBean.getWelcomeId());
		subscription_logger.info("welcomeBean title{}", welcomeBean.getTitle());
		if(welcomeBean.isLargeImage())
		{
			welcomeMessage.put("banner", welcomeBean.getBannerImage());
			subscription_logger.debug("adding image into map [ {} ]",welcomeBean.getBannerImage());
		}
		if(subscriptionBean.isMobile())
		{
			welcomeMessage.put("description", StringEscapeUtils.escapeJava(welcomeBean.getDescription()));
			welcomeMessage.put("title", StringEscapeUtils.escapeJava(welcomeBean.getTitle()));
			logger.info("subscriptionApi :subscriptionBean.isMobile()[{}]",subscriptionBean.isMobile(),welcomeMessage);
			jmsTemplate.convertAndSend(myProperties.getProperty("SUBSQUEUE","subscription_fcm_queue"),welcomeMessage);
		}
		else
		{

			jmsTemplate.convertAndSend(myProperties.getProperty("SUBSQUEUE","SUBSCRIPTION_QUEUE"),welcomeMessage);
		}
		welcomeMessage.put("description", welcomeBean.getDescription());
		welcomeMessage.put("title", welcomeBean.getTitle());
		logger.info("subscriptionApi ,:::[{}]",welcomeMessage);
		return welcomeMessage;

	}


	private boolean welcomeMessagesPresent(WelcomeBean welcomeBean, Map<String,String> welcomeData, IopushSubscribers iopushSubscribers, int productID) {
		boolean isEligible=false;
		if(!welcomeBean.isSegmented() && welcomeData.containsKey("product_" + productID)){
			isEligible=true;
		}else{
			if((welcomeData.containsKey("Isps_" + iopushSubscribers.getIopushIsp().getIspId()) && 
					welcomeData.containsKey("Countries_" + iopushSubscribers.getIopushGeoDetails().getGeoId()) &&
					welcomeData.containsKey("Platform_" + iopushSubscribers.getIopushPlatformDetails().getPlatformID())  && welcomeData.containsKey("Segments_"+iopushSubscribers.getIopushSegmentation())
					&& welcomeData.containsKey("product_" + productID))){
				isEligible  = true ;
				if(welcomeData.containsKey("citiesExists")){
					if(welcomeData.containsKey("Cities_" + iopushSubscribers.getIopushCityDetails().getCityId()))
						isEligible  = true ;	
					else
						isEligible  = false ;
				}

			}
		}

		return isEligible;
	}




	private int saveGeoDetails(GeoCityBean geoCityBean) {
		IopushGeoDetails iopushGeoDetails = new IopushGeoDetails();
		iopushGeoDetails.setGeoCode(geoCityBean.getCounty_code().toLowerCase());
		iopushGeoDetails.setGeoLatitude(geoCityBean.getCity_latitude());
		iopushGeoDetails.setGeoLongititude(geoCityBean.getCity_longitude());
		iopushGeoDetails.setGeoName(geoCityBean.getCounty_name());
		iopushGeoDetails.setCreatedBy("anonymousUser");
		iopushGeoDetails.setCreationDate(new Date());
		iopushGeoDetails.setModificationDate(new Date());
		iopushGeoDetails.setModificedBy("anonymousUser");
		Response response = this.iExternalAPIDao.saveGeoDetail(iopushGeoDetails);
		subscription_logger.debug("subscriptionApi response status received while saving iopushGeoDetails is [ "+response.getStatus()+" ]");
		if(response.getStatus())
		{
			return response.getIntegerResult();
		}
		return 0;
	}

	private int saveCityDetails(GeoCityBean geoCityBean, int geo_id) {
		int city_id = 0;
		IopushGeoDetails iopushGeoDetails = new IopushGeoDetails();
		iopushGeoDetails.setGeoId(geo_id);
		IopushCityDetails iopushCityDetails=new IopushCityDetails();
		iopushCityDetails.setIopushGeoDetails(iopushGeoDetails);
		iopushCityDetails.setCityCode(geoCityBean.getCity_code());
		iopushCityDetails.setCityName(geoCityBean.getCity_name());
		iopushCityDetails.setCityLongititude(geoCityBean.getCity_longitude());
		iopushCityDetails.setCityLatitude(geoCityBean.getCity_latitude());
		iopushCityDetails.setCreatedBy("anonymousUser");
		iopushCityDetails.setCreationDate(new Date());
		iopushCityDetails.setModificationDate(new Date());
		iopushCityDetails.setModificedBy("");
		city_id=this.iExternalAPIDao.saveCityCode(iopushCityDetails).getIntegerResult();
		subscription_logger.debug("subscriptionApi response status received while saving iopushCityDetails is [ "+this.iExternalAPIDao.saveCityCode(iopushCityDetails).getStatus()+" ] ");
		return city_id;
	}


	private int saveISPDetails(String isp, int geo_id) {
		IopushIsp iopushIsp = new IopushIsp();
		iopushIsp.setIspName(isp);
		iopushIsp.setCountryId(geo_id);
		return this.iExternalAPIDao.saveIsp(iopushIsp).getIntegerResult();
	}


	@Override
	public ResponseMessage campStatApi( String iopush_token, int campaign_id, int action) {
		logger.info("inside campStatApi ");
		logger.info("campStatApi data passed is 1) iopush_token [ " +iopush_token+" ] 2) campaign_id ["+campaign_id+" ] 3) action [ "+action+" ]");
		ResponseMessage rmMessage=null;
		Response res=null;
		try{
			res=this.iExternalAPIDao.updateActivateSubscriberStat(iopush_token, campaign_id,action);
			if(res.getIntegerResult()>0){
				rmMessage=new ResponseMessage(Constants.SUCCESS_CODE,Constants.SUCCESS); 
				logger.info("campStatApi table data successfully updated ");
			}else{
				rmMessage=new ResponseMessage(Constants.ERROR_CODE_INVALID," Campaign Id NOT AVAILABLE"); 
				logger.info("campStatApi campaign with given campaign_Id [ "+campaign_id+" ] does not exist  ");
			}
		}catch(Exception e){
			rmMessage=new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,e.getMessage());
			logger.error("campStatApi Exception iopush_token[ " + iopush_token +" ]",e);
		}
		logger.info("CampStatApi Response[ "+rmMessage+" ]");
		return rmMessage;
	}



	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResponseMessage registrationAPI(UserBean userBean, HttpServletRequest request) {
		logger.info("registrationEmail userBean data is [ " + userBean.toString() + " ]");
		ResponseMessage rMessage = new ResponseMessage();
		Response response = new Response();
		IopushUser iopushUser = new IopushUser();
		int userID=0;
		try {
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			int noOfCAPSAlpha = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_CAPS_ALPHA", "1"));
			int noOfDigits = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_DIGITS", "1"));
			int noOfSplChars = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_SPL_CHARACTER", "1"));
			int minLen = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_MIN_LENGTH", "8"));
			int maxLen = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_MAX_LENGTH", "12"));
			String password = new String(Utility.generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars));
			//			String encrytPassword = Utility.md5Encryption(password);

			// get the access token
			//String env = myProperties.getProperty("ENVIORAMENT") + ".";
			String freebiesTotal = myProperties.getProperty(env + "FREEBIES_TOTAL","750");
			String freebiesUsed = myProperties.getProperty(env + "FREEBIES_USED","0");
			String freebiesPlanId = myProperties.getProperty(env + "FREEBIES_PLAN_ID","3");
			String freebiesbal = myProperties.getProperty(env + "FREEBIES_BALANCE","750");
			// setting encrytPassword to user so it could be used in email api
			userBean.setPassword(password);

			IopushProduct iopushProduct = new IopushProduct();
			iopushProduct.setHash(Utility.generateUniqueId());
			iopushProduct.setProductName(userBean.getSubDomain());
			iopushUser.setEmailId(userBean.getEmailId());
			//iopushUser.setPassword(encrytPassword);
			//iopushUser.setLastPassword(encrytPassword);
			iopushUser.setPassword(password);
			iopushUser.setLastPassword(password);
			iopushUser.setUsername(userBean.getEmailId());

			iopushUser.setPhoneNumber(userBean.getPhoneNumber());
			iopushUser.setCompany(userBean.getCompany());
			iopushUser.setFirstName(userBean.getFirstName().trim());
			iopushUser.setTargetVisitors("");

			iopushUser.setWebsiteUrl(userBean.getWebsiteUrl());
			iopushUser.setImagePath("");
			iopushUser.setCountryCode("");
			iopushUser.setIsActive(false);
			iopushUser.setLoginAttempts((short) 0);
			iopushUser.setSalutation("");
			iopushUser.setIopushProduct(iopushProduct);
			iopushUser.setEmailFlag(0);
			iopushUser.setCreatedOn(new java.util.Date());
			iopushUser.setCreatedBy(Utility.getUserName());
			//			iopushUser.setModifiedOn(new java.util.Date());

			// check email 
			Response mailresponse=this.iExternalAPIDao.findUserByEmailId(userBean.getEmailId());
			if(mailresponse.getStatus())
			{
				// check subdomain
				Response checkmailDaoResponse = iExternalAPIDao.checkEmailandDomain(userBean.getSubDomain());
				if (checkmailDaoResponse.getStatus()) {

					// this is the first db call here we save the iopush_user data
					response = this.iExternalAPIDao.saveRegistrationDetails(iopushUser);

					logger.info("registrationEmail save user details db response is {}.", response.getStatus());

					if (response.getStatus()) {
						userBean.setUserId(response.getIntegerResult());
						String subDomain = myProperties.getProperty(env + "FOLDER", "/var/www/html/") + "/"
								+ userBean.getSubDomain();
						Path path = Paths.get(subDomain);

						logger.info("Is directory created [" + Files.createDirectory(path) + "]");
						String content = new String(Files.readAllBytes(Paths.get(myProperties
								.getProperty(env + "SUBSCRIPTION_INDEX_HTML", "/var/www/html/iopush_conf/index.html"))));
						if (content != null) {
							content = content.replace("%company_name%", userBean.getCompany());
							content = content.replace("%pid%", iopushProduct.getHash());
						}
						Files.write(Paths.get(subDomain + "/index.html"), content.getBytes());
						logger.info("copied index.html file");
						String subscription_content = new String(Files.readAllBytes(Paths.get(myProperties
								.getProperty(env + "SUBSCRIPTION_INDEX_JS", "/var/www/html/iopush_conf/subscription.js"))));
						if (subscription_content != null) {
							subscription_content = subscription_content.replace("%hash%", iopushProduct.getHash());
						}
						Files.write(Paths.get(subDomain + "/subscription.js"), subscription_content.getBytes());
						logger.info("copied subscription.js file");

						logger.info("Going to insert in iopushUserCategory !");
						// insertSubscriptionLimit(iopushProduct);
						IopushUsercategory userCategory = new IopushUsercategory();
						Response response1 = new Response();

						userCategory.setBalance(Integer.parseInt(freebiesbal));
						userCategory.setTotal(Integer.parseInt(freebiesTotal));
						userCategory.setUsed(Integer.parseInt(freebiesUsed));
						userCategory.setCreatedOn(new java.util.Date());
						userCategory.setCreatedBy(Utility.getUserName());
						userCategory.setModifiedOn(new java.util.Date());
						userCategory.setPlanId(Integer.parseInt(freebiesPlanId));
						userCategory.setProduct_id(iopushProduct.getProductID());
						userCategory.setLimitExceed(Constants.LIMIT_NOT_EXCEED);

						response1 = this.iUserCategoryDao.insertUserCategoryDetails(userCategory);

						if (response1.getStatus()) {
							logger.info("Done insertion in iopushUserCategory !!");

							logger.info("Going to insert in iopushCustomNotification for device type desktop!");
							IopushCustomNotification iopushCustomNotification = new IopushCustomNotification();
							Response response2 = new Response();
							iopushCustomNotification.setAllowBtnBackgroundColor("#eeeeee");
							iopushCustomNotification.setAllowBtnColor("#28282d");
							iopushCustomNotification.setAllowText("");
							iopushCustomNotification.setButtonType("");
							iopushCustomNotification.setCheckFlag(false);
							iopushCustomNotification.setDelayTime(0);
							iopushCustomNotification.setDontAllowBtnBackgroundColor("#eeeeee");
							iopushCustomNotification.setDontAllowBtnColor("#28282d");
							iopushCustomNotification.setDontAllowText("");
							iopushCustomNotification.setLogoPath("");
							iopushCustomNotification.setMessage("");
							iopushCustomNotification.setOptIn(true);
							iopushCustomNotification.setPopupBackgroundColor("#ffffff");
							iopushCustomNotification.setPopupColor("28282d");
							iopushCustomNotification.setProduct_id(iopushProduct.getProductID());
							iopushCustomNotification.setTitle("");
							iopushCustomNotification.setType("native");
							iopushCustomNotification.setWebsiteUrl(userBean.getWebsiteUrl());
							iopushCustomNotification.setDeviceType("desktop");


							response2 = this.iCustomNotificationDao.insertCustomNoitifcationDetails(iopushCustomNotification);

							if (response2.getStatus()) {

								logger.info("Going to insert in iopushCustomNotification for mobile device type !");

								iopushCustomNotification = new IopushCustomNotification();
								Response response3 = new Response();
								iopushCustomNotification.setAllowBtnBackgroundColor("#eeeeee");
								iopushCustomNotification.setAllowBtnColor("#28282d");
								iopushCustomNotification.setAllowText("");
								iopushCustomNotification.setButtonType("");
								iopushCustomNotification.setCheckFlag(false);
								iopushCustomNotification.setDelayTime(0);
								iopushCustomNotification.setDontAllowBtnBackgroundColor("#eeeeee");
								iopushCustomNotification.setDontAllowBtnColor("#28282d");
								iopushCustomNotification.setDontAllowText("");
								iopushCustomNotification.setLogoPath("");
								iopushCustomNotification.setMessage("");
								iopushCustomNotification.setOptIn(true);
								iopushCustomNotification.setPopupBackgroundColor("#ffffff");
								iopushCustomNotification.setPopupColor("28282d");
								iopushCustomNotification.setProduct_id(iopushProduct.getProductID());
								iopushCustomNotification.setTitle("");
								iopushCustomNotification.setType("native");
								iopushCustomNotification.setWebsiteUrl(userBean.getWebsiteUrl());
								iopushCustomNotification.setDeviceType("mobile");
								response3 = this.iCustomNotificationDao.insertCustomNoitifcationDetails(iopushCustomNotification);

								if (response3.getStatus()){
									rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Registration is Successful!");
									rMessage.setResponseCode(Constants.SUCCESS_CODE);
									rMessage.setData(iopushUser);
								}
								logger.info("Going to insert in iopushCustomNotification !");
							}

						}
						// insertIopushCustomNotification(iopushProduct,userBean.getWebsiteUrl());
					}
				}
				else {
					logger.info("An account with this subdoamin already exists.");
					rMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "An account with this subdomain already exists.");
					//				return rMessage;
				}
			}
			else{
				logger.info("user email  already exist ");
				rMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "An account with this email address already exists.");

			}
		}
		catch (Exception e) {
			logger.error("registrationEmail an error occured. ", e);
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "some exception while user registration");
			//			return rMessage;
		}
		return rMessage;
	}

	@Async
	@Override
	public Response sendRegistrationMail(IopushUser iopushUser) {
		logger.info("sendRegistrationMail sending mail...");
		Response response = new Response();
		String errorMessage = "";
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String host = myProperties.getProperty(env + "IMAGEURL");
		byte[] username_encodedBytes = Base64.encode(iopushUser.getUsername().getBytes());
		byte[] password_encodedBytes = Base64.encode(iopushUser.getPassword().getBytes());
		try {

			String encrypt_username = URLEncoder.encode(new String(username_encodedBytes), "UTF-8");
			String encrypt_password = URLEncoder.encode(new String(password_encodedBytes), "UTF-8");
			String link =host+"/iopush_notify/authenticate.html?flag="+encrypt_username+"&auth="+encrypt_password;
			String htmlMsg = String.format(myProperties.getProperty(env + "REGISTRATION_BODY"), iopushUser.getFirstName(),iopushUser.getEmailId(), iopushUser.getPassword(),link);
			MimeMessage mimeMessage = mailSender_service.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(iopushUser.getEmailId());
			//			helper.setBcc(myProperties	.getProperty(env + "BCC_MAIL", "salgotra.jagdish@thinksys.com,maya.unger@iosis.io,yakirs@iosis.io")	.split(","));
			helper.setBcc(myProperties	.getProperty(env + "BCC_MAIL", "salgotra.jagdish@thinksys.com")	.split(","));
			errorMessage = myProperties.getProperty(env + "EMAIL_ERROR",	"User Registration is successful, some error while sending mail");
			helper.setSubject(myProperties.getProperty(env+"MAIL_SUBJECT", "Registration successful"));
			mailSender_service.send(mimeMessage);
			response.setStatus(true);

			logger.info("sendRegistrationMail message sent successfully[{}]",iopushUser.getEmailId());

			iopushUser.setEmailFlag(1);
			iopushUser.setIsActive(true);
			this.iExternalAPIDao.updateEmailFlag(iopushUser);
			logger.info("sendRegistrationMail email flag updated successfully.");

		} catch (Exception e) {
			errorMessage = myProperties.getProperty(env + "EMAIL_ERROR","You have successfully registered to ioPush. There was an unexpected error and we were unable to send you an automatic email with your login information. We will contact you shortly and provide you all the information");
			response.setStatus(false);
			response.setMessage(errorMessage);
			logger.error("sendRegistrationMail some error occured.", e);
		}
		return response;
	}












	private void insertSubscriptionLimit(IopushProduct iopushProduct ){

		// insertion of the records in the usercategory table for the plans
		// related details
		try{
			IopushUsercategory userCategory = new IopushUsercategory();
			Response response1 = new Response();
			userCategory.setBalance(500);
			userCategory.setTotal(500);
			userCategory.setUsed(0);
			userCategory.setCreatedOn(new java.util.Date());
			userCategory.setCreatedBy(Utility.getUserName());
			userCategory.setModifiedOn(new java.util.Date());
			userCategory.setPlanId(3);
			userCategory.setProduct_id(iopushProduct.getProductID());

			response1 = this.iUserCategoryDao.insertUserCategoryDetails(userCategory);

			if (response1.getStatus()) {
				logger.info("Successfully update the userCategory...");
				//System.out.println("Successfully update the userCategory...");

			} else {
				logger.error("Failed upload the UserCategory records. ");
				//System.out.println("Failed upload the UserCategory records.");
			}

		}catch(Exception e){
			logger.error("While inserting IopushUserCategory table");
			e.printStackTrace();

		}

	}

	@Override
	public ResponseMessage sendUnImplementedUserReportMail() {

		logger.info("inside sendUnImplementedUserReportMail.");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");					
		Calendar cal = Calendar.getInstance();
		String date = simpleDateFormat.format(cal.getTime());
		ResponseMessage responseMessage = null;

		String fileName=myProperties.getProperty(env +"UNIMPLEMENTED_USER_FILE")+"unimplementeduser."+date+"_" +System.currentTimeMillis()+".csv";


		List<Object[]> userlist= this.iExternalAPIDao.findUnsubscribedUserdetail();
		List<UserBean> userbeanlist= new ArrayList<UserBean>();
		if(!userlist.isEmpty()){
			for(Object[] object : userlist){
				UserBean userbean= new UserBean();
				String registrationDate = object[5]==null?"":simpleDateFormat.format((Date)object[5]);
				userbean.setUserId((Integer)object[0]);
				userbean.setEmailId((String)object[1]);
				userbean.setUsername((String)object[2]);
				userbean.setWebsiteUrl((String)object[3]);
				userbean.setPhoneNumber((String)object[4]);
				userbean.setRegistrationDate(registrationDate);
				userbeanlist.add(userbean);
			}
			logger.info("sendUnImplementedUserReportMail  unsubscribed user SuccessFully retrieved .  {}",userbeanlist);


			String[] columns = new String[]{"Name","Email","Phone number","Website URL","Registration date"};
			if(Utility.makeImplementedUserReportCsvFile(userbeanlist, fileName,columns)){


				logger.info("sendUnImplementedUserReportMail  csv document is created with file name =  {}",fileName);
				try{
					String htmlMsg = String.format(myProperties.getProperty(env + "UNIMPLEMENTED_USER_MAIL_BODY")+myProperties.getProperty(env + "UNIMPLEMENTED_USER_MAIL_BODY_2"),date);
					MimeMessage mimeMessage = mailSender_support.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

					helper.setTo(myProperties.getProperty(env+"UNIMPLEMENTED_USER_SENT_MAIL_TO","singh.anuj@thinksys.com,gautam.kanika@thinksys.com,pandey.neeraj@thinksys.com,salgotra.jagdish@thinksys.com").split(","));
					helper.setBcc(myProperties.getProperty(env+"UNIMPLEMENTED_USER_BCC_MAIL_TO","singh.anuj@thinksys.com"));

					helper.setSubject(myProperties.getProperty(env +"UNIMPLEMENTED_MAIL_SUBJECT"));
					helper.setText(htmlMsg, true);
					FileSystemResource file = new FileSystemResource(fileName);
					helper.addAttachment(myProperties.getProperty(env+"UNIMPLEMENTED_USER_ATTACHMENT_FILE_NAME"),file);
					mailSender_support.send(mimeMessage);
					logger.info("sendUnImplementedUserReportMail message sent successfully To .{}" ,"singh.anuj@thinksys.com");
					responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Email sent Successfully !");
				}catch (Exception ex){

					logger.error("sendUnImplementedUserReportMail ,an error occured.{} ",ex.toString());
					responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "some error occured while sending email !");
					ex.printStackTrace();
				}
			}
			else{
				logger.info(" sendUnImplementedUserReportMail ,Some error while creating the csv file");
				responseMessage= new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,"Some error while creating the csv file !");
			}
		}
		else{
			logger.info("sendUnImplementedUserReportMail ,unsubscribed user doesn't exist ");
			responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "not implemented HTML codes, user doesn't exist !");
		}

		return responseMessage;
	}




	@Override
	//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResponseMessage getInTouch(GetInTouchBean getInTouchBean,Integer prodID){
		logger.info("getInTouch userBean data is {}",getInTouchBean );
		ResponseMessage rMessage = new ResponseMessage();


		try{
			/*
			IopushGetInTouch iopushgetintouch = new IopushGetInTouch();
			iopushgetintouch.setFullName(getInTouchBean.getFullName());
			iopushgetintouch.setPhoneNumber(getInTouchBean.getPhoneNumber());
			iopushgetintouch.setCompany(getInTouchBean.getCompany());
			iopushgetintouch.setEmailId(getInTouchBean.getEmailId());
			iopushgetintouch.setWebsiteUrl(getInTouchBean.getWebsiteUrl());
			iopushgetintouch.setMonthlyWebsiteVisitors(getInTouchBean.getMonthlyWebsiteVisitors());
			iopushgetintouch.setMessage(getInTouchBean.getMessage());

			if(prodID==null || prodID==0)
			{
				iopushgetintouch.setProduct_id(0);
			}
			else
			{
				iopushgetintouch.setProduct_id(prodID);
			} 
			//iopushgetintouch.setUserId(-1);
			iopushgetintouch.setCustomizePlan(true);

			iopushgetintouch.setCreatedOn(new java.util.Date());
			iopushgetintouch.setModifiedOn(new java.util.Date());
			iopushgetintouch.setCreatedBy(Utility.getUserName());
			iopushgetintouch.setModifiedBy("");
			iopushgetintouch.setSource(getInTouchBean.getSource());*/

			/*	Response response = this.iExternalAPIDao.saveGetInTouch(iopushgetintouch);

			if (response.getStatus()){*/

			Response	response = sendGetInTouchMail(getInTouchBean);

			if (response.getStatus()) {
				response  = sendGetInTouchMailAcknowledgment(getInTouchBean);
				if(response.getStatus())
				{
					rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Get in touch acknowledgment persisted!");
					rMessage.setResponseCode(Constants.SUCCESS_CODE);
					rMessage.setData(getInTouchBean);
				}
				else
				{
					rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,
							"some error occured while sending email !");
					rMessage.setResponseCode(Constants.ERROR_CODE_UNKNOWN);
				}
			} else {
				rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,
						"some error occured while sending email !");
				rMessage.setResponseCode(Constants.ERROR_CODE_UNKNOWN);
			}


			//}

		}catch(Exception e){

			logger.info("issue while saving the get in touch ");
			rMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "issue while persisting in db");
			rMessage.setResponseCode(Constants.ERROR_CODE_INVALID);
			// e.printStackTrace();
			return rMessage;
		}
		return rMessage;
	}


	private Response sendGetInTouchMail(GetInTouchBean getInTouchBean) {
		logger.info("sendGetInTouch sending mail...");
		Response response = new Response();
		String errorMessage = "";
		try {
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			LocalDate date = LocalDate.now();

			String htmlMsg = String.format(myProperties.getProperty(env + "GETINTOUCH_BODY"), getInTouchBean.getFullName(),getInTouchBean.getSource(),
					date, getInTouchBean.getFullName(),getInTouchBean.getEmailId(),getInTouchBean.getPhoneNumber(),getInTouchBean.getWebsiteUrl(),getInTouchBean.getMessage());

			MimeMessage mimeMessage = mailSender_support.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			//mimeMessage.setContent(htmlMsg, "text/html");
			mimeMessage.setContent(htmlMsg, "text/html; charset=UTF-8");
			helper.setTo(myProperties.getProperty(env + "GETINTOUCH_USER_CC_MAIL", "yakirs@iosis.io,maya.unger@iosis.io,laura.schwarz@iosis.io,florian.wachter@iosis.io,operations@iosis.io").split(","));
			if(!"".equals(getInTouchBean.getSource()) && "Backoffice/ioPush".equals(getInTouchBean.getSource().trim()))
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCH_MAIL_SUBJECT_IOPUSH", "Enterprise - custom made ioPush"));
			else if(!"".equals(getInTouchBean.getSource()) && "Iosis Website/ioPush".equals(getInTouchBean.getSource().trim()))
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCH_MAIL_SUBJECT_WEBSITE_IOPUSH", "Enterprise - custom made ioPush"));
			else 
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCH_MAIL_SUBJECT_WEBSITE_CONTACT", "Enterprise - custom made ioPush"));

			mailSender_support.send(mimeMessage);
			response.setStatus(true);
			logger.info("getintouch message sent successfully");
			return response;
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(errorMessage);

			logger.error("sendGetInTouchMail,an error occured.{} ",e.toString());

			e.printStackTrace();
		}
		return response;
	}
	private Response sendGetInTouchMailAcknowledgment(GetInTouchBean getInTouchBean) {
		logger.info("sendGetInTouchMailAcknowledgment sending mail...");
		Response response = new Response();
		String errorMessage = "";
		try {
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			LocalDate date = LocalDate.now();
			String htmlMsg ="";
			if(!"".equals(getInTouchBean.getSource()) && "Backoffice/ioPush".equals(getInTouchBean.getSource().trim())){
				htmlMsg = String.format(myProperties.getProperty(env + "GETINTOUCHACK_BODY"), getInTouchBean.getFullName());
			}
			else
				htmlMsg = String.format(myProperties.getProperty(env + "GETINTOUCHACK_MAIL_BODY_WEBSITE"), getInTouchBean.getFullName());
			MimeMessage mimeMessage = mailSender_support.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(htmlMsg, "text/html; charset=UTF-8");

			helper.setTo(getInTouchBean.getEmailId());
			helper.setBcc(myProperties.getProperty(env+"GETINTOUCHACK_USER_CC_MAIL","singh.anuj@thinksys.com").split(","));
			if(!"".equals(getInTouchBean.getSource()) && "Backoffice/ioPush".equals(getInTouchBean.getSource().trim()))
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCHACK_MAIL_SUBJECT_IOPUSH"));
			else if(!"".equals(getInTouchBean.getSource()) && "Iosis Website/ioPush".equals(getInTouchBean.getSource().trim()))
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCHACK_MAIL_SUBJECT_WEBSITE"));
			else 
				helper.setSubject(myProperties.getProperty(env + "GETINTOUCHACK_MAIL_SUBJECT_IOPUSH"));
			mailSender_support.send(mimeMessage);
			response.setStatus(true);
			logger.info("GETINTOUCHACK message sent successfully");
			return response;
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(errorMessage);

			logger.error("sendGetInTouchMailAck,an error occured.{} ",e.toString());

			e.printStackTrace();
		}
		return response;
	}

	@Override

	public ResponseMessage cancelPaypalAggrement(Integer userid) {

		logger.info("inside cancel Paypal Aggrement");
		int productId=0;
		String status = "active";
		DateTime dt = new DateTime();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		ResponseMessage rm=null;
		try{
			Response res= this.iExternalAPIDao.fetchUserById(userid);
			if(res.getStatus()){

				IopushUser iopushUser = (IopushUser) res.getData().get(0);
				logger.info("cancelPaypalAggrement , userDetail retrieved .details [{}] for user Id:[{}] ,",iopushUser.toString(),userid);
				productId= iopushUser.getIopushProduct().getProductID();
				logger.info("going to fetch paypal pAyment detail.");

				Response responsenew= this.iExternalAPIDao.fetchPaypalPaymentdetail(productId, status);
				if(responsenew.getStatus()){

					IopushPayment iopushPayment =  (IopushPayment) responsenew.getData().get(0);
					logger.info("cancelPaypalAggrement ,IopushPayment detail retrieved.for prodID.[{}],detail :[{}]",productId,iopushPayment.toString());

					/************************************************************/
					/**********cancel aggrement with paypal***********/
					/************************************************************/


					String userName = myProperties.getProperty(env+"clientUserName");
					String secretPassword = myProperties.getProperty(env+"clientSecretPassword");
					String tokenURL = myProperties.getProperty(env+"tokenURL");
					String accessToken = PaypalUtility.getAccesstoken(userName, secretPassword, tokenURL);
					String aggrementId=iopushPayment.getAgreementId();
					String cancelurl= myProperties.getProperty(env+"createAgreementURL")+aggrementId+"/cancel";
					System.out.println(cancelurl);
					CancelAgreementBean cancelAgreementBean = new CancelAgreementBean("Canceling the profile.");
					Gson  gson = new Gson();
					String jsonRequest=gson.toJson(cancelAgreementBean);
					PaypalResponse paypalResponse = PaypalUtility.executePayment(cancelurl, accessToken, jsonRequest);

					/************************************************************/
					/************************************************************/

					if(paypalResponse.getStatusCode()==200){

						logger.info("cancelPaypalAggrement, paypal agreement has SuccessFully canceled .");

						logger.info("going to update IopushPayment detail.");
						iopushPayment.setAgreementStatus("cancel");
						Response resPaymentDetail= this.iExternalAPIDao.updatePaymentDetail(iopushPayment);
						if(resPaymentDetail.getStatus()){

							logger.info("cancelPaypalAggrement , paymentDetail SuccessFully updated. paymentDetail :[{}]",iopushPayment.toString());
							logger.info("going to fetch usercategory.");
							Response resnew =iPaypalDao.findUsercategory(productId);
							if(resnew.getStatus()){

								IopushUsercategory iopushUsercategory= (IopushUsercategory) resnew.getData().get(0);
								logger.info("going to update usercategory.[{}]",iopushUsercategory.toString());
								iopushUsercategory.setTotal(0);
								//								iopushUsercategory.setBalance(0);
								iopushUsercategory.setModifiedOn(dt.toDate());
								Response response=this.iUserCategoryDao.updateUserCategoryDetails(iopushUsercategory);
								if(response.getStatus()){

									logger.info("cancelPaypalAggrement,  userCategory SuccessFully Updated");	
									rm = new ResponseMessage(Constants.SUCCESS_CODE,"Paypal payment Aggrement SuccessFully Updated !");
								}
							}else{
								logger.info("cancelPaypalAggrement,  No Record found in userCategory for prod ID [{}]",productId);	
								rm =new ResponseMessage(Constants.SUCCESS_CODE, " No Record found in userCategory!");
							}
						}
					}
					else{

						logger.info("cancelPaypalAggrement, unable to cancel paypal .paypal return status code : [{}]",paypalResponse.getStatusCode());	
						rm =new ResponseMessage(Constants.SUCCESS_CODE, " unable to cancel paypal agrement.please Try again !");
					}
				}
				else{
					logger.info("cancelPaypalAggrement,iopushPayment detail not found for prductId:[{}] and status :[{}]",productId,status);
					rm =new ResponseMessage(Constants.SUCCESS_CODE, "No active plan found for this user!");
				}
			}
			else{
				logger.info("cancelPaypalAggrement , user doesn't exist .  for user Id:[{}] ,",userid);
				rm = new ResponseMessage(Constants.SUCCESS_CODE,"user doesn't exist !");
			}
		}catch(Exception e){

			logger.error("cancelPaypalAggrement, some error occured.[{}]",e.getMessage());
			rm= new ResponseMessage(Constants.ERROR_CODE_INVALID,e.getMessage());
			e.printStackTrace();
		}
		return rm;
	}


	//	@Override
	//	public ResponseMessage sendFailCustomerRenewalReportMail() throws ParseException {
	//
	//		logger.info("inside sendFailCustomerRenewalReportMail.");
	//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");	
	//
	//		Calendar cal = Calendar.getInstance();
	//		String newdate=sdf.format(cal.getTime());
	//		String date1 = simpleDateFormat.format(cal.getTime());
	//		cal.add(Calendar.DATE, -1);
	//		String date2 = simpleDateFormat.format(cal.getTime());
	//		ResponseMessage responseMessage = null;
	//		/************************/
	//		Date d1=simpleDateFormat.parse(date1);
	//		Date d2 =simpleDateFormat.parse(date2);
	//		/************************/
	//		String env = myProperties.getProperty("ENVIORAMENT") + ".";
	//		String fileName=myProperties.getProperty(env +"FAIL_RENEWAL_CUSTOMER_REPORT")+"FailCustomerRenewal."+date1+"_"  +System.currentTimeMillis()+".csv";
	//
	//		ResponseMessage rm = null;
	//
	//
	//		String[] columns = new String[]{"First Name","Email Id","Phone Number","Renew Amount","Creation Date","Agreement Status","OutStanding Balance"};
	//
	//		List<Object[]>objects= this.iExternalAPIDao.fetchFailCustomerRenewalInfo(date1, date2);
	//		if(!objects.isEmpty()){
	//
	//			if(Utility.makeFailCustomerRenewalReportCsvFile(objects, fileName,columns)){
	//
	//				try{
	//					logger.info("sendFailCustomerRenewalReportMail  csv document is created with file name =  {}",fileName);
	//
	//
	//					String htmlMsg = String.format(myProperties.getProperty(env + "FAIL_RENEWAL_CUSTOMER_REPORT_MAIL_BODY"),newdate);
	//					MimeMessage mimeMessage = mailSender.createMimeMessage();
	//					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	//					helper.setTo(myProperties.getProperty(env+"FAIL_RENEWAL_CUSTOMER_MAIL_TO","singh.anuj@thinksys.com").split(","));
	//					//helper.setBcc(myProperties.getProperty(env+"FAIL_RENEWAL_CUSTOMER_MAIL_BCC_TO","singh.anuj@thinksys.com"));
	//					helper.setSubject(myProperties.getProperty(env +"FAIL_RENEWAL_CUSTOMER_MAIL_SUBJECT"));
	//					helper.setText(htmlMsg, true);
	//					FileSystemResource file = new FileSystemResource(fileName);
	//					helper.addAttachment(String.format(myProperties.getProperty(env+"FAIL_RENEWAL_CUSTOMER_MAIL_ATTACHMENT_FILE"),newdate),file);
	//					mailSender.send(mimeMessage);
	//					responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Email sent Successfully !");
	//
	//					/************** delete csv files  */
	//					File directory = new File(myProperties.getProperty(env +"FAIL_RENEWAL_CUSTOMER_REPORT"));
	//					File files[] = directory.listFiles();
	//					for(int index = 0; index < files.length; index++)
	//					{
	//						boolean wasDeleted = files[index].delete();
	//						if (!wasDeleted)
	//						{
	//							logger.info("sendFailCustomerRenewalReportMail file is not deleted.");
	//						}
	//					}
	//					/************************/
	//
	//				}
	//				catch (Exception ex){
	//
	//					logger.error("sendFailCustomerRenewalReportMail ,an error occured.{} ",ex.toString());
	//					responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "some error occured in  while sending email !");
	//					ex.printStackTrace();
	//				}
	//
	//
	//			}
	//			else{
	//				logger.info(" sendFailCustomerRenewalReportMail ,Some error while creating the csv file");
	//				responseMessage= new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,"Some error while creating the csv file !");
	//			}
	//		}else{
	//			logger.info("sendFailCustomerRenewalReportMail.NO user record found.");
	//		}
	//		return rm;
	//	}
	public ResponseMessage updateActiveStatus(String username) {
		logger.info("inside updateActiveStatus.");
		logger.info("updateActiveStatus, username is {}", username);

		ResponseMessage response = new ResponseMessage();
		response = this.iExternalAPIDao.updateActiveStatus(username);

		return response;
	}

	@Override
	public ResponseMessage linkroute(String username) {
		logger.info("inside linkroute.");
		ResponseMessage response = new ResponseMessage();
		boolean linkrouteval;
		IopushUser iopushUser = (IopushUser) this.iExternalAPIDao.findUserByName(username);
		if(iopushUser!=null)
		{
			linkrouteval = iopushUser.isLinkRoute();
			if(linkrouteval)
			{
				response.setResponseDescription("true");
			}
			else
			{
				response.setResponseDescription("false");
			}
			response.setResponseCode(Constants.SUCCESS_CODE);
		}
		return response;

	}

	/* updation of userCategory record while subscription//
	 Basically decrement in balance, and increment in used subscriber
	 */
	@SuppressWarnings("unchecked")
	private void updateUserCategory(int productID ){
		int balance = 0;
		int used = 0;

		//retrieval based on productId
		Response response = this.iUserCategoryDao.userCategoryDetails(productID);
		if (response.getStatus()) {
			IopushUsercategory iopushUsercategory = (IopushUsercategory) response.getData().get(0);
			// update the userCategory table with updated info
			response = updateUserCategory(iopushUsercategory);
			if (response.getStatus()) {

				logger.info("Balance decreased, in userCategory  balance::{} ,used::{}",balance,used);

			} else {

				logger.info("Unable to update the record in userCategory table");
			}

		} else {
			logger.info("Unable to retrieve the userCategory record for product_id ::"+productID);
			// else not records found
		}
		// update the userCatebgory table with updated info

	}
	private Response updateUserCategory(IopushUsercategory userCategory) {


		subscription_logger.debug("subscriptionApi, in updateUserCategory.");

		int balTemp = 0;
		int usedTemp = 0;
		balTemp = userCategory.getBalance() - 1;
		usedTemp = userCategory.getUsed() + 1;
		int overLimitSubscribersValue=0;

		if (userCategory.isLimitExceed()!=Constants.LIMIT_EXCEED ){

			if(balTemp<0){

				subscription_logger.info("subscriptionApi, in updateUserCategory,limit exceed going to update limitExceed count");
				userCategory.setLimitExceed(Constants.LIMIT_EXCEED);
				overLimitSubscribersValue =userCategory.getOverLimitSubscribersValue()+1;
				userCategory.setOverLimitSubscribersValue(overLimitSubscribersValue);
				userCategory.setUsed(userCategory.getUsed()+1);
			}
			else{

				subscription_logger.info("subscriptionApi, in updateUserCategory, limit not exceed, going to update balance and used count");
				userCategory.setBalance(balTemp);
				userCategory.setUsed(usedTemp);
			}
			return this.iUserCategoryDao.updateUserCategoryDetails(userCategory);
		}
		else
		{
			// if limit exceed value is not zero then we will update the over Limit subscribers
			subscription_logger.info("subscriptionApi, in updateUserCategory, limit exceed going to update overlimit count");
			overLimitSubscribersValue =userCategory.getOverLimitSubscribersValue()+1;
			userCategory.setOverLimitSubscribersValue(overLimitSubscribersValue);
			userCategory.setUsed(userCategory.getUsed()+1);
			return this.iUserCategoryDao.updateUserCategoryDetails(userCategory);
		}
	
}

	@Override
	public ResponseMessage resendRegistrationMail() {
		logger.info("inside resendRegistrationMail.");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");	

		Calendar cal = Calendar.getInstance();
		String date = simpleDateFormat.format(cal.getTime());
		ResponseMessage responseMessage = null;
		String fileName=myProperties.getProperty(env +"RESEND_REGISTRATION_MAIL_FILE")+"resendReg."+date+"_"  +System.currentTimeMillis()+".csv";

		logger.info("filename::[{}]",fileName);
		Response res= this.iExternalAPIDao.checkResendregistrationEmailUser();
		List<UserBean> userbeanlist= new ArrayList<UserBean>();
		List<IopushUser> list=null;
		if(res.getStatus()){
			list =(List<IopushUser>) res.getData();
			IopushUser user= list.get(0);

			for(IopushUser iopushUser : list){
				UserBean userBean= new UserBean();
				String registrationDate = iopushUser.getCreatedOn()==null?"":" " +iopushUser.getCreatedOn();
				String lastname=iopushUser.getLastName()==null?"":iopushUser.getLastName();
				String firstname=iopushUser.getFirstName()==null?"":iopushUser.getFirstName();
				userBean.setUserId(iopushUser.getUserId());
				userBean.setFirstName(iopushUser.getFirstName());
				userBean.setFullName(firstname + " "+ lastname);
				userBean.setEmailId(iopushUser.getEmailId());
				userBean.setPhoneNumber(iopushUser.getPhoneNumber());
				userBean.setPassword(iopushUser.getPassword());
				userBean.setUsername(iopushUser.getUsername());
				userBean.setRegistrationDate(registrationDate);
				userBean.setSubDomain(iopushUser.getIopushProduct().getProductName());
				userbeanlist.add(userBean);
			}
			logger.info("resendRegistrationMail   user SuccessFully retrieved .  {}",userbeanlist);

			String[] columns = new String[]{"Full name","Email","Phone","Domain","User name","Password","Date and time of registration"};
			if(Utility.makeResendRegistrationMailCsvFile(list, fileName,columns)){

				logger.info("resendRegistrationMail  csv document is created with file name =  {}",fileName);
				try{

					String body2= myProperties.getProperty(env+"RESEND_REGISTRATION_MAIL_BODY_2");
					String htmlMsg = String.format(myProperties.getProperty(env + "RESEND_REGISTRATION_MAIL_BODY")+ myProperties.getProperty(env + "RESEND_REGISTRATION_MAIL_BODY_2"),date);
					MimeMessage mimeMessage = mailSender_support.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
					helper.setTo(myProperties.getProperty(env+"RESEND_REGISTRATION_MAIL_TO","singh.anuj@thinksys.com,gautam.kanika@thinksys.com,pandey.neeraj@thinksys.com,salgotra.jagdish@thinksys.com").split(","));
					helper.setBcc(myProperties.getProperty(env+"RESEND_REGISTRATION_BCC_MAIL_TO","singh.anuj@thinksys.com,gautam.kanika@thinksys.com,pandey.neeraj@thinksys.com,salgotra.jagdish@thinksys.com"));
					helper.setSubject(myProperties.getProperty(env +"RESEND_REGISTRATION_MAIL_SUBJECT"));
					helper.setText(htmlMsg, true);
					FileSystemResource file = new FileSystemResource(fileName);
					helper.addAttachment(myProperties.getProperty(env+"RESEND_REGISTRATION_MAIL_ATTACHMENT_FILE_NAME"),file);
					mailSender_support.send(mimeMessage);
					logger.info("resendRegistrationMail message sent successfully To .{}" ,myProperties.getProperty(env+"RESEND_REGISTRATION_MAIL_TO"));
					responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Email sent Successfully !");
				}catch (Exception ex){

					logger.error("resendRegistrationMail ,an error occured.{} ",ex.toString());
					responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "some error occured in  while sending email !");
					ex.printStackTrace();
				}
			}
			else{
				logger.info(" resendRegistrationMail ,Some error while creating the csv file");
				responseMessage= new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,"Some error while creating the csv file !");
			}


			//			logger.info("resendRegistrationMail   user SuccessFully retrieved .  {}",userbeanlist);

			logger.info("resendRegistrationMail ,goiong to send mail to user");		
			for (UserBean userBean:userbeanlist){

				Response emailResponse = this.sendRegistrationMail(user);
				if(emailResponse.getStatus())
				{
					logger.info("resendRegistrationMail ,message sent successfully To.",userBean.getEmailId());					

				}else{

					logger.info("resendRegistrationMail ,some error while sending mail");	
				}
			}
		}
		else{
			logger.info("resendRegistrationMail ,unsubscribed user doesn't exist ");
			responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, ", user dosen't exist who did not receive registration email   !");
		}

		return responseMessage;
	}


	@Override
	public JsonResponse<PlanBean> fetchUserPlan() {
		logger.info("inside fetchUserPlan.");
		JsonResponse<PlanBean> jsonResponse =null;
		Response response = null;
		try{
			response = this.iExternalAPIDao.findUserPlan();
			List<PlanBean> listUserPlan = new ArrayList<>();

			if(response.getStatus()){
				List<IopushUserplan> iopushUserplan = (List<IopushUserplan>) response.getData();
				for (IopushUserplan object : iopushUserplan) 
					listUserPlan.add(new PlanBean( object.getPlanId(), object.getPlanName(),object.getPricing(),object.getSubscriberLimit(),object.getCurrencyName(),object.getSymbol(),object.getDuration()));
				jsonResponse= new JsonResponse<>(Constants.SUCCESS, listUserPlan, listUserPlan.size());
				logger.info("UserPlan Data successfully retrieved. ["+listUserPlan+ ", TotalRecordCount ::"+ listUserPlan.size() +" ]");
			}
			else{
				logger.info("UserPlan table have no data. ["+listUserPlan.isEmpty()+"]");
				jsonResponse= new JsonResponse<>(null, "UserPlan table have no data.");
			}
		}catch(Exception e){
			logger.error("fetchUserPlan some error occured." + e);
			jsonResponse = new JsonResponse<PlanBean>("ERROR", e.getMessage());
		}
		return jsonResponse;
	}


	@Override
	public ResponseMessage sendSubscriptionLimitExceedMail() {

		logger.info("inside sendSubscriptionLimitExceedMail.");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		ResponseMessage rm =null;
		try{
			Response response = this.iExternalAPIDao.fetchLimitExceedUserDetail();
			if(response.getStatus()){

				List<IopushUser> userlist = (List<IopushUser>) response.getData();
				for(IopushUser iopushUser :userlist){

					String lastname=iopushUser.getLastName()==null?"":iopushUser.getLastName();
					String firstname=iopushUser.getFirstName()==null?"":iopushUser.getFirstName();

					String htmlMsg = String.format(myProperties.getProperty(env + "SEND_LIMIT_EXCEED_MAIL_BODY"),firstname +" "+lastname);
					MimeMessage mimeMessage = mailSender_service.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
					helper.setTo(iopushUser.getEmailId());
					helper.setSubject(myProperties.getProperty(env +"SEND_LIMIT_EXCEED_MAIL_SUBJECT","Subscriber limit reached"));
					helper.setText(htmlMsg, true);
					mailSender_service.send(mimeMessage);
					logger.info("sendSubscriptionLimitExceedMail message sent successfully To [{}]" ,iopushUser.getEmailId());
					rm = new ResponseMessage(Constants.SUCCESS_CODE, "Email sent Successfully !");
					logger.info("sendSubscriptionLimitExceedMail going to update IsLimitExceed flag ");
					Response res=this.iExternalAPIDao.updateIsLimitExceedFlag(iopushUser.getIopushProduct().getProductID());
					if(res.getStatus()){
						logger.info("sendSubscriptionLimitExceedMail ,IsLimitExceed flag SuccessFully Updated.");
					}
				}

			}else{
				logger.info("sendSubscriptionLimitExceedMail, No User found.");
				rm=new ResponseMessage(Constants.SUCCESS_CODE,"No User Found");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info("sendSubscriptionLimitExceedMail, some error occured");
			rm=new ResponseMessage(Constants.SUCCESS_CODE,e.getMessage());
		}
		return rm;
	}

}
