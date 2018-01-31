
package com.saphire.iopush.utils;


import com.saphire.iopush.model.IopushActivesubscribers;
import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.model.IopushRSSStats;
import com.saphire.iopush.model.IopushRenewalCustomer;
import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.model.IopushRssFeedSchedular;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.model.IopushSubCampaign;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushViewsClicks;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.model.IopushWelcomeStats;

public interface Constants {
	String DATE_FORMAT = "yyyy-MM-dd";
	String SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS= "yyyy-MM-dd HH:mm:ss";
	String SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS="yyyy-MM-dd'T'HH:mm:ss";
	String OUTPUT_DATE_FORMAT = "dd MMM";
	String ERROR = "ERROR";
	String URLERROR = "INVALID";
	String OK = "OK";
	String SUCCESS = "Success";

	String UPN_QUERY = "select * from";
	String UTF_8 = "UTF-8";
	String INSTALLED_FROM = "INSTALLED_FROM";
	String BROWSER_ID = "BROWSER_ID";
	String COUNTRY_ID = "COUNTRY_ID";
	String PLATFORM_ID = "PLATFORM_ID";
	String CITY_ID = "CITY_ID";
	String DEVICES = "DEVICES_ID";
	String SEGMENTS = "SEGMENTS_ID";
	String EXTENSION_ID = "EXTENSION_ID";
	String ISP_ID = "ISP_ID";
	String NOT_CLICKED = "NOT_CLICKED";
	String ACCESS_WEBSITES = "ACCESS_WEBSITES";
	String IOPUSH_SUBSCRIBERS = "iopush_subscribers";
	String ICMP_SITE_REGISTRATION = "icmp_site_registration";
	String SUBSCRIPTION_RANGE = "SUBSCRIPTION_RANGE";
	String SEGMENTTYPES = "SEGMENT_TYPES";
	int LIVE = 1;
	int PENDING = 2;
	int EXPIRE = 3;
	int DRAFT = 4;

	int ACTIVATED=1;
	 
	 String NOTIFICATION_FOLDER_READ = "/notification/";
	 String USER_IMAGE_FOLDER="/user/";
	 String RSSFEED_FOLDER_READ = "/rssfeed/";
	 
	
/*	 String CREATE_CAMPAIGN_QUERY = "create table $tableName$ (record_id int4 not null, created_by varchar(50), creation_date timestamp, modification_date timestamp not null, modificed_by varchar(50), notification_status int4 not null, pull_notification boolean, upn_id varchar(100), primary key (record_id));";
	 String CREATE_CAMPAIGN_SEQUENCE_QUERY = "create sequence $sequenceName$;";*/
	 String SELECT_EXTENSION_UPN = "select fk_geo_id,fk_browser_id,fk_city_id,extension_upn_id,%s, %s from icmp_upn_detail where upn_status =1 and upn_id in(";
	  String INSERT_INTO_DYNAMIC_SUBSCRIBER_CAMPAIGN_TABLE_QUERY = "insert into iopush_activesubscribers (iopush_token,fcm_token,fk_geoid,fk_platformid,fk_cityid,fk_segmentId,fk_campaign_id, status) " ;
	  
	  String CAMPAIGN_DELETE_QUERY = "DELETE FROM " + IopushCampaign.class.getName() + " WHERE campaignId=?";
	int GENERIC_ERROR=402;
	int ERROR_CODE_INVALID = 206;

	// int REGISTRATION_SUCCESS_EMAIL_FAILURE=207;

	int EMAIL_FAILURE = 207;

	int LIMITATION_EXCEED=705;
	int ERROR_CODE_UNKNOWN = 520;
	int SUCCESS_CODE = 200;
	int DUPLICATE_LINK=100;
	int UNAUTHORIZE_USER_CODE = 401;
	String UNAUTHORIZE_USER = "Invalid User Please Relogin";
	String INVALID_DATEFORMAT = "Invalid DateFormat ";
	String CAMPAIGN_EXPIRED_QUERY = "UPDATE " + IopushCampaign.class.getName()+ " SET campaignStatus = 3 WHERE campaignStatus != 3 and date(campaignEndDateInEDT) < '$currentDate$' ";
    
    String MOVE_FROM_ACTIVATED_EXPIRED="insert into icmp_expiredUPNCampaign "
    		+ "(campaignclick, campaignclose, campaignopen, campaignsent, cityid, geoid, platformid, type, upnid,campaign_id )"
    		+ "(select campaignclick, campaignclose, campaignopen, campaignsent, fk_cityid, fk_geoid, fk_platformid, type, upnid, fk_campaign_id from icmp_activatedupnlist where campaignsent=1 and fk_campaign_id=:campaign_id )";
    String MOVE_FROM_EXPIRED_STATS="insert into icmp_campaign_stats "
    		+ "(campaign_id,platformid,geoid,cityid,campaignsent,campaignopen,campaignclose,campaignclick )"
    		+ "(select campaign_id,platformid,geoid,cityid,sum(campaignsent) as campaignsent,sum(campaignopen) as campaignopen,sum(campaignclose) as campaignclose,sum(campaignclick) as campaignclick "
    		+ " from icmp_expiredUPNCampaign where campaign_id=:campaign_id group by 1,2,3,4 )";

    String UPDATECampaign_STATS_Query = "UPDATE "+IopushCampaign.class.getName()+" SET campaignclick=? ,campaignclose=?,campaignopen=? ,campaignsent=? WHERE campaign_id=?";
    
    String CAMPAIGN_RULE_DELETE_QUERY = "DELETE FROM " + IopushRules.class.getName() + " WHERE iopushCampaign.campaignId=?";
    
    String SUB_CAMPAIGN_DELETE_QUERY = "DELETE FROM " + IopushSubCampaign.class.getName() + " WHERE iopushCampaign.campaignId=?";
	
    String CAMPAIGN_LAUNCH_QUERY = "UPDATE " + IopushCampaign.class.getName()	+ " SET campaignStatus=<STATUS> ,modificationDate=NOW() WHERE campaignId=?";
	String CAMPAIGN_EXPIRE_QUERY = "UPDATE " + IopushCampaign.class.getName()	+ " SET campaignStatus=<STATUS> WHERE campaignId=?";
    
    
	String VIEWSCLICKS_RULE_DELETE_QUERY = "DELETE FROM " + IopushViewsClicks.class.getName() + " WHERE iopushCampaign.campaignId=?";
	
	String UPDATE_SUBSCRIBERLIST_CLICK_STATS_Query = "UPDATE "+IopushActivesubscribers.class.getName()+" SET campaignclick=1  WHERE iopush_token=? and fk_campaign_id=?";
    String UPDATE_SUBSCRIBERLIST_CLOSE_STATS_Query = "UPDATE "+IopushActivesubscribers.class.getName()+" SET campaignclose=1 WHERE iopush_token=? and fk_campaign_id=? ";
    String UPDATE_SUBSCRIBERLIST_OPEN_STATS_Query = "UPDATE "+IopushActivesubscribers.class.getName()+" SET campaignopen=1  WHERE  iopush_token=? and fk_campaign_id=?";
    String UPDATE_SUBSCRIBERLIST_CLICK_OPEN_STATS_Query="UPDATE "+IopushActivesubscribers.class.getName()+" SET campaignclick=1 ,campaignopen=1 WHERE iopush_token=? and  fk_campaign_id=?";
//	String NOTIFICATION_DELETE_QUERY = "DELETE FROM " + IopushNotification.class.getName() + " WHERE iopushCampaign.campaignId=?";
	String SELECT_SUBSCRIBERS = "select iopush_token,fcm_token,fk_geo_id,fk_platform_id,fk_city_id,fk_segmentId,%s, 0 from iopush_subscribers where  subscribers_id in(";
	String ACTIVAT_SUBSCRIBERS_DELETE_QUERY = "DELETE FROM "+IopushActivesubscribers.class.getName()+" WHERE fkCampaignId=?";
	String RSSFEED_DELETE_QUERY = "DELETE FROM "+IopushRssFeedConfig.class.getName()+" WHERE id=?";
	String FIND_CAMPAIGN_COUNTRY_STATS="select geoid,cityid,platformid,sum(campaignsent) as campaignsent,sum(campaignopen) as campaignopen,sum(campaignclick) as campaignclick,sum(campaignclose) as campaignclose from iopush_campaign_stats where fk_campaign_id=:campaign_id group by 1,2,3";
    String FIND_CAMPAIGN_COUNTRY_PLATFORM_STATS="select platformid,sum(campaignsent) as campaignsent,sum(campaignopen) as campaignopen,sum(campaignclick) as campaignclick,sum(campaignclose) as campaignclose from iopush_campaign_stats where fk_campaign_id=:campaign_id and geoid=:geoid group by 1";
    
	
	String SUB_CAMPAIGN_LAUNCH_QUERY = "UPDATE " + IopushSubCampaign.class.getName() + " SET campaignStatus=<STATUS> WHERE iopushCampaign.campaignId=? and geo_id = ?";
	String SUB_CAMPAIGN_CHECK_STATUS_QUERY = "Select campaign_status from" + IopushSubCampaign.class.getName() + "  WHERE iopushCampaign.campaignId=? and geo_id = ?";
	String ACTIVAT_SUBSCRIBERS_SUBCAMPAIGN_DELETE_QUERY = "DELETE FROM "+IopushActivesubscribers.class.getName()+" WHERE fk_campaign_id=? and fk_geoid=? ";

	String RSSFEED_SCHEDULAR_DELETE_QUERY= "DELETE FROM "+IopushRssFeedSchedular.class.getName()+" WHERE rssfeedid=?";
	String INSERT_INTO_SUBSCRIBER_RSSFEED_TABLE_QUERY = "insert into iopush_rssfeed_activesubscribers (iopush_token,fcm_token,fk_geo_id,fk_platform_id,fk_city_id,fk_article_id, status) " ;
	String UPDATE_RSSFEEDCONFIG_STATUS_Query = "UPDATE "+IopushRssFeedConfig.class.getName()+" SET notification=?  WHERE   id=?";
	String PRODUCT_ID = "PRODUCT_ID";
	String UPDATE_CAMPAIGN_LIVE_COUNT_QUERY = "UPDATE " + IopushCampaign.class.getName()	+ " SET live = ? ,modificationDate=NOW() WHERE campaignId=?";
	
	String WELCOME_DELETE_QUERY = "DELETE FROM " + IopushWelcome.class.getName() + " WHERE welcomeId=? and iopushProduct.productID=?";
	String WELCOME_STATS_DELETE_QUERY = "DELETE FROM " + IopushWelcomeStats.class.getName() + " where fk_welcome_id=?" ;
	String WELCOME_STATUS_CHANGE_QUERY = "UPDATE " + IopushWelcome.class.getName()	+ " SET welcomeStatus=? ,modificationDate=NOW() WHERE welcomeId=? and iopushProduct.productID=?";
	String WELCOME_STATUS_FLAG_CHANGE_QUERY = "UPDATE " + IopushWelcome.class.getName()	+ " SET welcomeStatus=? ,isActive=?, modificationDate=NOW() WHERE welcomeId=? and iopushProduct.productID=?";
	String UPDATE_ROUTE_MANAGER_STATUS_Query = "UPDATE "+IopushRssFeedConfig.class.getName()+" SET active=?  WHERE   options LIKE ''rss_id='+? '%'  ";
	String RSS_STATS_DELETE_QUERY = "DELETE FROM "+IopushRSSStats.class.getName()+" WHERE rssName = ? and pid = ?";
	String PREVIEW_LAST_NOTIFICATION = "Select campaignId, title, description, forwardUrl, imagePath from " + IopushCampaign.class.getName() + "  WHERE iopushProduct.productID=? ORDER BY campaignStatus ASC, modificationDate desc ";
	
	String SEGMENT_DELETE_QUERY = "DELETE FROM " + IopushSegmentation.class.getName() + " WHERE segmentId=? and iopushProduct.productID=?";
	 String SUBSCRIBER_UPDATE_QUERY = "UPDATE " + IopushSubscribers.class.getName() + " SET iopushSegmentation.segment_id=0 where iopushSegmentation.segment_id=? ";
	 String RENEW_DELETE_QUERY = "DELETE FROM " +IopushRenewalCustomer.class.getName() + " WHERE Customer =?";

	String PAYPAL_REQUEST_INTENT = "sale";
	String PAYPAL_REQUEST_PAYMENT_METHOD="paypal";
	String PAYPAL_REQUEST_CURRENCY = "EUR";
	String PAYPAL_REQUEST_BLANK = "0.00";
	String PAYPAL_REQUEST_BLANK_STRING="";
	int PAYPAL_REQUEST_ITEM_QUANTITY=1;
	String PAYAL_AGREEMENT_STATUS_CANCEL="Cancelled";
			String PAYAL_AGREEMENT_STATUS_ACTIVE="Active";
			
			int PAYAL_CANCEL_AGREEMENT__STATUS_CODE=204;
	//
	String PAYPAL_REQUEST_ALLOWED_PAYMENT_METHOD="INSTANT_FUNDING_SOURCE";
	
	String PAYPAL_REQUEST_NOTE_TO_PAYER ="Contact us for any questions on your order.";
/*	String PAYPAL_REQUEST_CANCEL_URL = "http://www.amazon.com";
	String PAYPAL_REQUEST_RETURN_URL = "http://www.hawaii.com";*/
	String CREATED = "created";
	String PAYPAL_REQUEST_SUCCESSFULLY_CREATED = "payment request created ";
	String PAYPAL_REQUEST_UNSUCCESSFULL= "Payment request Invalid";
	int PAYPAL_CREATE_PAYMENT_REQUEST_SUCCESSFULL = 1;
	int PAYPAL_PAYMENT_UNAUTHORISED_REQUEST = 401;
	int PAYPAL_CREATE_PAYMENT_REQUEST_FAILED = 0;
	int PAYPAL_API_SUCCESSCODE = 201;
	int PAYPAL_API_FAILED_REQUEST_CODE = 402;
	
	 String PAYPAL_PAYMENT_EXCEUTE_REASON="Paypal Payment Successfully Executed";
	   String PAYPAL_PAYMENT_FAIL_REASON="Paypal Payment  Execution Failed";
	 int PAYPAL_PAYMENT_SUCCESS=2;
	 int PAYPAL_PAYMENT_FAILED=3;
	 int BAD_REQUEST=400;
	 
	 String PAYPAL_PLAN_YES = "YES";
	 String PAYPAL_PLAN_CANCEL = "Cancel";
	String PAYPAL_REQUEST_DESCRIPTION = "Subscription Agreement";
	 String PAYPAL_PLAN_TYPE = "INFINITE";
	 String PAYPAL_PAYMENT_DEFINETION = "Regular payment";
	 String PAYPAL_PAYMENT_TYPE = "REGULAR";
	 String PAYPAL_TRANSACTION_TYPE_INSTANT = "Instant";
	String PAYPAL_TRANSACTION_TYPE_RENEW = "Renew";
	String SYSTEM = "System";

	int LIMIT_EXCEED=1;
	int LIMIT_NOT_EXCEED=0;
	int LIMIT_CANCEL=2;
	int LIMIT_RENEWAL_FALSE = 3;
	
}
