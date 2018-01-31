

package com.saphire.iopush.serviceImpl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSFeedResponseBean;
import com.saphire.iopush.bean.RSSStatsBean;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.dao.IRSSFeedDao;
import com.saphire.iopush.dao.ISegmentDao;
import com.saphire.iopush.model.IopushRSSStats;
import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.model.IopushRssFeedSchedular;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.service.IRSSFeedService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;

@Service
public class RSSFeedServiceImpl implements IRSSFeedService {

	@Autowired
	IRSSFeedDao iRSSFeedDao;
	@Autowired
	ICampaignDao iCampaignDao;
	@Autowired
	Properties myProperties;
	@Autowired JmsTemplate jmsTemplate;
	@Autowired ISegmentDao iSegmentDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseMessage statusrssfeed(RSSFeedBean rssfeedBean) {
		ResponseMessage rMessage = null;
		Response response = new Response();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		try {
			this.iRSSFeedDao.statusrssfeed(rssfeedBean.getId(), rssfeedBean.getActive());
			/*rMessage = new ResponseMessage(Constants.SUCCESS_CODE,
					rssfeedBean.getActive() == 1 ? "Successfully Resumed" : "Successfully Paused");*/

			response = this.iRSSFeedDao.rssfeed(rssfeedBean.getId());
			IopushRssFeedConfig iopushRssFeedConfig = (IopushRssFeedConfig) response.getScalarResult();
			rssfeedBean.setCampaign(iopushRssFeedConfig.getCampaign());
			rssfeedBean.setGeneric(iopushRssFeedConfig.getGeneric());
			rssfeedBean.setSource(iopushRssFeedConfig.getSource());
			rssfeedBean.setUrl(iopushRssFeedConfig.getUrl());
			rssfeedBean.setName(iopushRssFeedConfig.getName());
			rssfeedBean.setCountries(iopushRssFeedConfig.getCountries());
			rssfeedBean.setCities(iopushRssFeedConfig.getCities());
			rssfeedBean.setIsps(iopushRssFeedConfig.getIsps());
			rssfeedBean.setPlatform(iopushRssFeedConfig.getPlatform());
			rssfeedBean.setProducts(iopushRssFeedConfig.getProducts());
			rssfeedBean.setUseAutomatedImage(iopushRssFeedConfig.getAutomatedImage()==0?false:true);
			rssfeedBean.setLogo(iopushRssFeedConfig.getLogo_path());
			rssfeedBean.setProductId(Utility.intConverter(iopushRssFeedConfig.getProducts()) );
			rssfeedBean.setSegments(iopushRssFeedConfig.getSegments());
			rssfeedBean.setSegmentTypes(iopushRssFeedConfig.getSegmentTypes());
			rssfeedBean.setSubscribedFrom(iopushRssFeedConfig.getSubscribedFrom());
			if((rssfeedBean.getSegments()==null ||rssfeedBean.getSegments().equals("")) && (rssfeedBean.getSegmentTypes()!=null && !rssfeedBean.getSegmentTypes().equals("")))
			{
			 Response response1 = iSegmentDao.listSegment(rssfeedBean.getSegmentTypes(),rssfeedBean.getProductId());
			    logger.info("listSegmentType response status is [ "+ response.getStatus()+" ]" );
			    StringBuilder segmentid=new StringBuilder();
			    List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
			    if (response1.getStatus()) {

			     for (IopushSegmentation iopushSegment : listIopushSegment) {
			      segmentid.append(iopushSegment.getSegmentId()+",");
			     }
			     rssfeedBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
			     
			    }
			}
			
			if(rssfeedBean.getActive()==1)
			{
				rssfeedBean.setActivateRSS(true);
				String insertQuery = Utility.insertQueryBasedOnCriteriaForRSS(rssfeedBean);
				rssfeedBean.setQuery(insertQuery);
				HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(rssfeedBean), myProperties.getProperty(env+"rssfeed.url"));
				logger.info(" statusRSS, httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
				if(httpResponse.getStatusLine().getStatusCode() == 200)
				{
					response = this.iRSSFeedDao.activateRSS(((IopushRssFeedConfig)response.getScalarResult()).getId() , 1);
					rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "RSS Activated Succesfully!");
				}
				else
				{
					rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Oops! Something Went Wrong.");
				}

			}
			else
			{
				rssfeedBean.setActivateRSS(false);
				HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(rssfeedBean), myProperties.getProperty(env+"rssfeed.url"));
				logger.info(" statusRSS, httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
				if(httpResponse.getStatusLine().getStatusCode() == 200)
				{
					response = this.iRSSFeedDao.activateRSS(((IopushRssFeedConfig)response.getScalarResult()).getId() , 0);
					rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "RSS Paused Succesfully!");
				}
				else
				{
					rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Oops! Something Went Wrong.");
				}
			}

		} catch (Exception e) {
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("Exception occurred while deleting rss feed ", e);
		}
		return rMessage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage rssfeedschedule() {
		ResponseMessage responseMessage = new ResponseMessage();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String message = "";
		try {
			// Step 1 Get data from Rssfeed scheduler

			logger.info("Rssfeedschedule Start " + LocalDateTime.now());
			String rssfeedurl = "";
			int rssfeedid = 0;
			String articleid = "" + System.currentTimeMillis();

			Response response = this.iRSSFeedDao.fetchRssFeedSchedule();
			if (response.getStatus()) {
				List<IopushRssFeedSchedular> listiopushRssFeedScheduler = (List<IopushRssFeedSchedular>) response
						.getData();
				for (IopushRssFeedSchedular iopushRssFeedSchedular : listiopushRssFeedScheduler) {
					rssfeedid = iopushRssFeedSchedular.getRssfeedid();
					response = this.iRSSFeedDao.rssfeed(rssfeedid);
					IopushRssFeedConfig iopushRssFeedConfig = (IopushRssFeedConfig) response.getScalarResult();
					logger.info("iopushRssFeedConfig , " + iopushRssFeedConfig);
					rssfeedurl = iopushRssFeedConfig.getUrl();
					/*if (iopushRssFeedConfig.getNotification() == 1) {
						// Step 2 Hit Rssfeed URL
						Long millis = System.currentTimeMillis();
						RSSFeedResponseBean data = Utility.checkRssFeeds(rssfeedurl, rssfeedid, myProperties, articleid,
								iopushRssFeedConfig.getLogo_path(), iopushRssFeedSchedular.getLink().trim());

						logger.debug("Rssfeedschedule RssUrl Response [" + data + "]");
						logger.info("Total time taken to proccess [" +(System.currentTimeMillis() - millis) +"]");

						if (data.getStatus() == Constants.SUCCESS_CODE) {
							logger.info("Rssfeedschedule Compare Link id :: New Link " + data.getLink() + " \nOld Link "
									+ iopushRssFeedSchedular.getLink());

							logger.info("Rssfeedschedule Different Link send push Notification");
							String insertQuery = Utility.insertQueryBasedOnCriteria(iopushRssFeedConfig,
									data.getArticleid());
							insertQuery = Constants.INSERT_INTO_SUBSCRIBER_RSSFEED_TABLE_QUERY + "(" + insertQuery
									+ ")";
							logger.debug("Rssfeedschedule insertQuery [" + insertQuery + "]");
							// Save data in active subscriber
							logger.debug("Rssfeedschedule Save data in active subscriber ");
							response = this.iCampaignDao.executeQuery(insertQuery);
							logger.debug("Rssfeedschedule Save data in active subscriber :: " + response.getStatus());
							// Update Schedular Table
							logger.debug("Rssfeedschedule Update data in schedular ");
							response = this.iRSSFeedDao.updateDataSchedular(data, rssfeedid,
									iopushRssFeedSchedular.getId());
							logger.info("Rssfeedschedule Update data in schedular :: " + response.getStatus());
							// Send notification
							data.setCampaign(iopushRssFeedConfig.getCampaign());
							data.setGeneric(iopushRssFeedConfig.getGeneric());
							data.setSource(iopushRssFeedConfig.getSource());
							data.setName(iopushRssFeedConfig.getName());
							data.setUrl(rssfeedurl);

							logger.info("[Rssfeedschedule] Hit Send Notification :: " + data.toString()
							+ " And Messaging URL :: "
							+ myProperties.getProperty(env + "rssfeedmessaging.url"));
							message += "[" + rssfeedid + "]" + rssfeedurl + ",";
							HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(data),
									myProperties.getProperty(env + "rssfeedmessaging.url"));
							logger.info("Rssfeedschedule httpResponse code [ "
									+ httpResponse.getStatusLine().getStatusCode() + " ]");
						} else if (data.getStatus() == Constants.DUPLICATE_LINK)
							logger.info("Duplicate Link found, No need to send notification");
						else
							logger.info("Unknown response from Rss Feed");
					} else {
						logger.info("Rssfeedschedule Inactive status Rssfeed Url :: " + rssfeedurl + "   rssfeedid :: "
								+ rssfeedid);
					}*/
				}
				responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, message);
			} else {
				logger.info("Rssfeedschedule No Data for scheduler");
			}
		} catch (Exception e) {
			responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("Exception occurred, ", e);
		}
		logger.info("Rssfeedschedule Final Response :: " + responseMessage);

		return responseMessage;
	}

	@Override
	public ResponseMessage urlValid(String uri) {
		logger.info("urlValid uri [ " + uri + " ]");
		ResponseMessage rm = null;
		if (uri.startsWith("http")) {
			rm = Utility.isURLValid(uri);
		} else {
			rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Protocol Missing");
		}
		logger.info("urlValid Response [ " + rm + " ]");
		return rm;
	}


	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<RSSFeedBean> listrssfeed(RSSFeedBean rssfeedBean) {

		Response response = new Response();

		Integer[] rssfeedstatus = new Integer[3];//0-inActive , 1- Active , 2- Paused
		if (rssfeedBean.getActive() == rssfeedBean.getInactive()) {
			rssfeedstatus =	new Integer[]{0,1,2};
		} else if (rssfeedBean.getActive() == 1) {
			rssfeedstatus =	new Integer[]{1};
		} else if (rssfeedBean.getInactive() == 1) {
			rssfeedstatus =	new Integer[]{0,2};
		}

		boolean flag = Utility.checkRssfeedStatus(rssfeedstatus);
		boolean analytics = false;
		int startIndex = rssfeedBean.getStartIndex();
		int pagesize = rssfeedBean.getPageSize();
		JsonResponse<RSSFeedBean> jsonResponse = null;
		List<RSSFeedBean> listRssfeed = new ArrayList<RSSFeedBean>();
		try {
			long st=System.currentTimeMillis();

			response = this.iRSSFeedDao.listRssfeed(startIndex,pagesize,rssfeedBean.getProductId(), rssfeedBean.getName(), rssfeedBean.getId(), rssfeedBean.getColumnForOrdering(), rssfeedBean.getRequiredOrder(),rssfeedstatus);
			List<Object[]> resnew=iRSSFeedDao.listRssfeedNew(rssfeedstatus,rssfeedBean.getName(),startIndex,pagesize,flag,analytics,null,null, rssfeedBean.getProductId(),  rssfeedBean.getId(), rssfeedBean.getColumnForOrdering(), rssfeedBean.getRequiredOrder());
			if(resnew.size()>0){
				logger.info("RssList Total time to execute [ "+(System.currentTimeMillis()-st)+ " ]");
				for (Object[] objects : resnew) {
					listRssfeed.add(new RSSFeedBean(objects));
				}
				jsonResponse = new JsonResponse<RSSFeedBean>("SUCCESS", listRssfeed,response.getIntegerResult());
			}
			else{
				jsonResponse = new JsonResponse<RSSFeedBean>("SUCCESS", "NO DATA FOUND");
				logger.info("listrssfeed NO DATA FOUND");
			}

		} catch (Exception e) {
			jsonResponse = new JsonResponse<RSSFeedBean>("ERROR", e.getMessage());
			logger.error(" listrssfeed ,Exception occurred, ", e);
		}
		return jsonResponse;
	}




	// @SuppressWarnings("unchecked")
	// @Override
	// public JsonResponse<IopushRssFeedConfig> listrssfeed(RSSFeedBean rssfeedBean) {
	// 	Response response = new Response();
	// 	Integer[] rssfeedstatus = new Integer[3];//0-inActive , 1- Active , 2- Paused
	// 	if (rssfeedBean.getActive() == rssfeedBean.getInactive()) {
	// 		rssfeedstatus =	new Integer[]{0,1,2};
	// 	} else if (rssfeedBean.getActive() == 1) {
	// 		rssfeedstatus =	new Integer[]{1};
	// 	} else if (rssfeedBean.getInactive() == 1) {
	// 		rssfeedstatus =	new Integer[]{0,2};
	// 	}
	// 	JsonResponse<IopushRssFeedConfig> data = null;
	// 	try {
	// 		response = this.iRSSFeedDao.listRssfeed(rssfeedBean.getStartIndex(),
	// 				rssfeedBean.getPageSize(),rssfeedBean.getProductId(), rssfeedBean.getName(), rssfeedBean.getId(), rssfeedBean.getColumnForOrdering(), rssfeedBean.getRequiredOrder(),rssfeedstatus);
	// 		data = new JsonResponse<IopushRssFeedConfig>("SUCCESS", (List<IopushRssFeedConfig>) response.getData(),
	// 				response.getIntegerResult());
	// 	} catch (Exception e) {
	// 		data = new JsonResponse<IopushRssFeedConfig>("ERROR", e.getMessage());
	// 		logger.error("Exception occurred, ", e);
	// 	}
	// 	return data;
	// }

	@Override
	public JsonResponse<IopushRssFeedConfig> rssfeed(RSSFeedBean rssfeedBean) {
		Response response = new Response();
		JsonResponse<IopushRssFeedConfig> data = null;
		try {
			response = this.iRSSFeedDao.rssfeed(rssfeedBean.getId());
			data = new JsonResponse<IopushRssFeedConfig>("SUCCESS", (IopushRssFeedConfig) response.getScalarResult());
		} catch (Exception e) {
			data = new JsonResponse<IopushRssFeedConfig>("ERROR", e.getMessage());
			logger.error("Exception occurred, ", e);
		}
		return data;
	}

	@Override
	public JsonResponse<IopushRssFeedConfig> updaterssfeed(RSSFeedBean rssfeedBean) {
		Response response = new Response();
		JsonResponse<IopushRssFeedConfig> data = null;
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		try {
			response = this.iRSSFeedDao.isRSSUrlUnique(rssfeedBean.getUrl(),Utility.intConverter(rssfeedBean.getProducts()));
			if(response.getStatus() && response.getIntegerResult()!=rssfeedBean.getId())
			{
				
				data = new JsonResponse<IopushRssFeedConfig>("ERROR", "RSS details with the same url already exist.");
			}
			else
			{
				if (rssfeedBean.isUseAutomatedImage())
					rssfeedBean.setLogo(Utility.getRSSLogo(rssfeedBean.getUrl()));
				else {

					String notificationPath = myProperties.getProperty(env + "FOLDER")
							+ myProperties.getProperty(env + "RSSFEED_NOTIFICATION_DIR");
					String ext = Utility.getExtension(rssfeedBean.getLogo());
					String filename = rssfeedBean.getName() + "." + ext;
					String base64Image = rssfeedBean.getLogo().split(",")[1];
					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

					Files.deleteIfExists(Paths.get(notificationPath + filename));

					Files.write(Paths.get(notificationPath + filename), imageBytes);
					rssfeedBean.setLogo(myProperties.getProperty(env + "IMAGEURL")
							+ myProperties.getProperty(env + "RSSFEED_NOTIFICATION_DIR") + filename);
					logger.info("File Path : " + notificationPath + filename);
				}
				response = this.iRSSFeedDao.updateRSSFeedConfig(rssfeedBean);
				data = new JsonResponse<IopushRssFeedConfig>("SUCCESS", (IopushRssFeedConfig) response.getScalarResult());
				rssfeedBean.setProductId(Utility.intConverter(rssfeedBean.getProducts()));
				if((rssfeedBean.getSegments()==null ||rssfeedBean.getSegments().equals("")) && (rssfeedBean.getSegmentTypes()!=null && !rssfeedBean.getSegmentTypes().equals("")))
				{
				 Response response1 = iSegmentDao.listSegment(rssfeedBean.getSegmentTypes(),rssfeedBean.getProductId());
				    logger.info("listSegmentType response status is [ "+ response.getStatus()+" ]" );
				    StringBuilder segmentid=new StringBuilder();
				    List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
				    if (response1.getStatus()) {

				     for (IopushSegmentation iopushSegment : listIopushSegment) {
				      segmentid.append(iopushSegment.getSegmentId()+",");
				     }
				     rssfeedBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
				     
				    }
				}
				if(rssfeedBean.isActivateRSS() && rssfeedBean.getProductId()>0)
				{
					String insertQuery = Utility.insertQueryBasedOnCriteriaForRSS(rssfeedBean);
					rssfeedBean.setQuery(insertQuery);
					rssfeedBean.setId( ((IopushRssFeedConfig)response.getScalarResult()).getId() );
					logger.info("saveRSSFEED, insert query is "+insertQuery); 
					HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(rssfeedBean), myProperties.getProperty(env+"rssfeed.url"));
					logger.info("launchCampaign httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
					if(httpResponse.getStatusLine().getStatusCode() == 200)
					{
						this.iRSSFeedDao.activateRSS(((IopushRssFeedConfig)response.getScalarResult()).getId() , 1);
						data = new JsonResponse<IopushRssFeedConfig>("SUCCESS",(IopushRssFeedConfig) response.getScalarResult());
					}
					else
					{
						data = new JsonResponse<IopushRssFeedConfig>("ERROR","Error Occurred while activating RSS Feed.");
					}
				}
			}
		} catch (Exception e) {
			data = new JsonResponse<IopushRssFeedConfig>("ERROR", e.getMessage());
			logger.error("Exception occurred, ", e);
		}
		return data;
	}

	@Override
	public JsonResponse<IopushRssFeedConfig> saveRSSFEED(RSSFeedBean rssfeedBean, int productId) {
		Response saveRSSresponse = new Response();
		JsonResponse<IopushRssFeedConfig> data = null;
		try {
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			Response isRSSNameNewresponse = this.iRSSFeedDao.isRSSNameNew(rssfeedBean.getName(),productId);
			if (isRSSNameNewresponse.getStatus()) {
				data = new JsonResponse<IopushRssFeedConfig>("ERROR", "RSS details with the same name already exist.");
			} else {
				Response isRSSUrlUniqueresponse = this.iRSSFeedDao.isRSSUrlUnique(rssfeedBean.getUrl(),productId);
				if(isRSSUrlUniqueresponse.getStatus())
				{
					data = new JsonResponse<IopushRssFeedConfig>("ERROR", "RSS details with the same url already exist.");
				}
				else
				{
					if (rssfeedBean.isUseAutomatedImage())
						rssfeedBean.setLogo(Utility.getRSSLogo(rssfeedBean.getUrl()));
					else {
						String notificationPath = myProperties.getProperty(env + "FOLDER")
								+ myProperties.getProperty(env + "RSSFEED_NOTIFICATION_DIR");
						String ext = Utility.getExtension(rssfeedBean.getLogo());
						String filename = rssfeedBean.getName() + "." + ext;
						String base64Image = rssfeedBean.getLogo().split(",")[1];
						byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

						Files.write(Paths.get(notificationPath + filename), imageBytes);
						rssfeedBean.setLogo(myProperties.getProperty(env + "IMAGEURL")
								+ myProperties.getProperty(env + "RSSFEED_NOTIFICATION_DIR") + filename);
						logger.info("File Path : " + notificationPath + filename);
					}
					
					rssfeedBean.setProducts(String.valueOf(productId));
					rssfeedBean.setProductId(productId);
//					rssfeedBean.setSubscribedFrom(subscribedFrom);
					saveRSSresponse = this.iRSSFeedDao.saveRSSFeedConfig(rssfeedBean);
					data = new JsonResponse<IopushRssFeedConfig>("SUCCESS",(IopushRssFeedConfig) saveRSSresponse.getScalarResult());
					logger.info("RSS Feed Configuration save successfully [" + rssfeedBean.getName() + "]");
					if((rssfeedBean.getSegments()==null ||rssfeedBean.getSegments().equals("")) && (rssfeedBean.getSegmentTypes()!=null && !rssfeedBean.getSegmentTypes().equals("")))
					{
					 Response response1 = iSegmentDao.listSegment(rssfeedBean.getSegmentTypes(),rssfeedBean.getProductId());
					    logger.info("listSegmentType response status is [ "+ saveRSSresponse.getStatus()+" ]" );
					    StringBuilder segmentid=new StringBuilder();
					    List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response1.getData();
					    if (response1.getStatus()) {

					     for (IopushSegmentation iopushSegment : listIopushSegment) {
					      segmentid.append(iopushSegment.getSegmentId()+",");
					     }
					     rssfeedBean.setSegments(segmentid.substring(0, segmentid.lastIndexOf(",")));
					     
					    }
					}
					if(rssfeedBean.isActivateRSS() && rssfeedBean.getProductId()>0)
					{
						String insertQuery = Utility.insertQueryBasedOnCriteriaForRSS(rssfeedBean);
						rssfeedBean.setQuery(insertQuery);
						rssfeedBean.setId( ((IopushRssFeedConfig)saveRSSresponse.getScalarResult()).getId() );
						logger.info("saveRSSFEED, insert query is "+insertQuery); 
						HttpResponse httpResponse = Utility.httpPost(new Gson().toJson(rssfeedBean), myProperties.getProperty(env+"rssfeed.url"));
						logger.info(" saveRSSFEED, httpResponse code [ "+httpResponse.getStatusLine().getStatusCode()+" ]");
						if(httpResponse.getStatusLine().getStatusCode() == 200)
						{
							this.iRSSFeedDao.activateRSS(((IopushRssFeedConfig)saveRSSresponse.getScalarResult()).getId() , 1);
							data = new JsonResponse<IopushRssFeedConfig>("SUCCESS",(IopushRssFeedConfig) saveRSSresponse.getScalarResult());
						}
						else
						{
							data = new JsonResponse<IopushRssFeedConfig>("ERROR","Error Occurred while activating RSS Feed.");
						}
					}
				}
			}
		} catch (Exception e) {
			data = new JsonResponse<IopushRssFeedConfig>("ERROR", e.getMessage());
			logger.error("Exception occurred, ", e);
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public ResponseMessage deleterssfeed(RSSFeedBean rssfeedBean) {
		ResponseMessage rMessage = null;
		try {
			Response response = this.iRSSFeedDao.rssfeed(rssfeedBean.getId());
			IopushRssFeedConfig iopushRssFeedConfig = (com.saphire.iopush.model.IopushRssFeedConfig) response.getScalarResult();
			this.iRSSFeedDao.deleteRSSFeedConfig(rssfeedBean.getId());
			this.iRSSFeedDao.deleteRSSStat(iopushRssFeedConfig.getName(), Utility.intConverter(iopushRssFeedConfig.getProducts()));
			this.iRSSFeedDao.deleteRSSFeedSchedularConfig(rssfeedBean.getId());

			rMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Deleted");
		} catch (Exception e) {
			rMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("Exception occurred while deleting rss feed ", e);
		}
		return rMessage;
	}

	@Override
	public ResponseMessage sendNotification(RSSFeedBean rssfeedBean) {
		// Step 1 Find IopushRssFeedConfig according to id
//		String articleid = "" + System.currentTimeMillis();
		logger.info("sendNotification [" + rssfeedBean.getId() + "]");
		ResponseMessage responseMessage = new ResponseMessage();
		try {
//			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			Response response = this.iRSSFeedDao.rssfeed(rssfeedBean.getId());
			IopushRssFeedConfig iopushRssFeedConfig = (IopushRssFeedConfig) response.getScalarResult();
			if (iopushRssFeedConfig.getNotification() == 0) {
				logger.info("rss feed url [" + iopushRssFeedConfig.getUrl() + "]");
				logger.info("rss feed name[" + iopushRssFeedConfig.getName() + "]");
				responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Success");
			} else {
				responseMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Already Active");
			}
		} catch (Exception e) {
			responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("Exception ", e);
		}
		logger.info("Rssfeed Final Response, " + responseMessage);
		return responseMessage;
	}


	@Override
	public ResponseMessage rssAnalytics(String rssname, String date) {
		logger.info("rssAnalytics rssName is [ "+rssname+ " ] date is [ "+date+" ]");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String rssClickQueue = myProperties.getProperty(env + "RSS_CLICK_QUEUE","rss_clickQ");
//		String queue = myProperties.getProperty(env + "RSS_CLICK_QUEUE");
		logger.info("rssAnalytics queue name is [{}] ",rssClickQueue);
		Map<String, Object> rssDataMap = new HashMap<String, Object>();
		rssDataMap.put("rssName",rssname);
		rssDataMap.put("date",date);
		jmsTemplate.convertAndSend(rssClickQueue, rssDataMap);

		return new ResponseMessage(Constants.SUCCESS_CODE, "Successfully updated records");
	}

		@Override
	public JsonResponse<RSSStatsBean> listrssStats(RSSStatsBean rssStatsBean) {
		JsonResponse<RSSStatsBean> jsonResponse = new JsonResponse<RSSStatsBean>();
		boolean analytics = true;
		int startIndex = rssStatsBean.getStartIndex();
		int pagesize = rssStatsBean.getPageSize();
		String rssName = rssStatsBean.getRssName();
		String rss_date1 = rssStatsBean.getStartDate();
		String rss_date2 = rssStatsBean.getEndDate();
		try {
			logger.info("listrssStats, Fetching the record for productid [{}] ",rssStatsBean.getProductId() );
			int count = iRSSFeedDao.countRSS(startIndex, pagesize, analytics, rssName, rss_date1, rss_date2,rssStatsBean.getProductId());

			List<IopushRSSStats> rssListResponse = iRSSFeedDao.listRSS(startIndex, pagesize, analytics, rssName, rss_date1, rss_date2,rssStatsBean.getProductId(), rssStatsBean.getColumnForOrdering(), rssStatsBean.getRequiredOrder());
			if(!rssListResponse.isEmpty()){
				List<RSSStatsBean> listRSS = new ArrayList<RSSStatsBean>();
				for (IopushRSSStats object : rssListResponse) 
				{
					listRSS.add(new RSSStatsBean(object.getRssName(), object.getDate(), object.getRssURL(), object.getSent(), object.getClick(),object.getIopushRSSStatsId()));
				}
				jsonResponse = new JsonResponse<RSSStatsBean>(Constants.SUCCESS, listRSS, count);
				logger.info("listrssStats Fetch Total Size [ " + listRSS.size()+" ]");
			} else {
				jsonResponse = new JsonResponse<RSSStatsBean>(Constants.SUCCESS, "NO DATA FOUND");
				logger.info("listrssStats NO DATA FOUND");
			}
		} catch (Exception e) {
			jsonResponse = new JsonResponse<RSSStatsBean>(Constants.ERROR, e.getMessage());
			logger.error("listrssStats" , e);
		}
		return jsonResponse;
	}



	// @Override
	// public JsonResponse<RSSStatsBean> listrssStats(RSSStatsBean rssStatsBean) {
	// 	JsonResponse<RSSStatsBean> jsonResponse = new JsonResponse<RSSStatsBean>();
	// 	boolean analytics = true;
	// 	int startIndex = rssStatsBean.getStartIndex();
	// 	int pagesize = rssStatsBean.getPageSize();
	// 	String rssName = rssStatsBean.getRssName();
	// 	String rss_date1 = rssStatsBean.getStartDate();
	// 	String rss_date2 = rssStatsBean.getEndDate();
	// 	try {
	// 		logger.info("Fetching the record for productid [" + rssStatsBean.getProductId() + "]");
	// 		int count = iRSSFeedDao.countRSS(startIndex, pagesize, analytics, rssName, rss_date1, rss_date2,rssStatsBean.getProductId());
	// 		List<IopushRSSStats> response = iRSSFeedDao.listRSS(startIndex, pagesize, analytics, rssName, rss_date1, rss_date2,rssStatsBean.getProductId(), rssStatsBean.getColumnForOrdering(), rssStatsBean.getRequiredOrder());
	// 		if(!response.isEmpty()){
	// 			List<RSSStatsBean> listRss = new ArrayList<RSSStatsBean>();
	// 			for (IopushRSSStats object : response) 
	// 			{
	// 				listRss.add(new RSSStatsBean(object.getRssName(), object.getDate(), object.getRssURL(), object.getSent(), object.getClick(),object.getIopushRSSStatsId()));
	// 			}
	// 			jsonResponse = new JsonResponse<RSSStatsBean>(Constants.SUCCESS, listRss, count);

	// 			logger.info("listrssStats Fetch Total Size [ " + listRss.size()+" ]");
	// 		} else {
	// 			jsonResponse = new JsonResponse<RSSStatsBean>(Constants.SUCCESS, "NO DATA FOUND");
	// 			logger.info("listrssStats NO DATA FOUND");
	// 		}
	// 	} catch (Exception e) {
	// 		jsonResponse = new JsonResponse<RSSStatsBean>(Constants.ERROR, e.getMessage());
	// 		logger.error("listrssStats" , e);
	// 	}
	// 	return jsonResponse;
	// }

	@Override
	public ResponseMessage isUrlValid(String rssURL) {
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RSSFeedResponseBean rssData = Utility.checkRssUrl(rssURL, myProperties);
			if (rssData.getStatus() == 200) {
				responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Url Is Valid");
			} else {
				responseMessage = new ResponseMessage(rssData.getStatus(), "Please Enter Valid Url.");
			}

		} catch (Exception e) {
			responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Please Enter Valid Url.");
			logger.error(" isUrlValid Exception ", e);
		}
		logger.info("isUrlValid Final Response, " + responseMessage);
		return responseMessage;

	}

	@Override
	public ResponseMessage welcomeAnalytics(String welcomename, String date, int type,int product_id) {
		logger.info("welcomeAnalytics welcomeName is [ "+welcomename+ " ] date is [ "+date+" ]");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String click_queue = myProperties.getProperty(env + "WELCOME_CLICK_QUEUE");
		String open_queue = myProperties.getProperty(env + "WELCOME_OPEN_QUEUE");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("welcomeName",welcomename);
		map.put("date",date);
		map.put("productId",product_id);
		if(type==1)//open
			jmsTemplate.convertAndSend("WELCOME_OPEN_QUEUE", map);
		else
			jmsTemplate.convertAndSend("WELCOME_CLICK_QUEUE", map);

		return new ResponseMessage(Constants.SUCCESS_CODE, "Successfully updated records");
	}



}



