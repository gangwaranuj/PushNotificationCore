package com.saphire.iopush.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.saphire.iopush.bean.SegmentTypeBean;
import com.saphire.iopush.bean.SegmentationBean;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.dao.ISegmentDao;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSegmentType;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.service.ISegmentService;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;

@Service
public class SegmentServiceImpl implements ISegmentService{

	@Autowired Properties myProperties;
	@Autowired ISegmentDao iSegmentDao;
	@Autowired IExternalAPIDao iExternalApiDao;
	@Autowired ICampaignDao iCampaignDao;
	@Autowired JmsTemplate jmsTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Override
	public JsonResponse<IopushSegmentation> saveSegmentation(SegmentationBean segmentationBean, Integer productId) {
		//productId=1;
		logger.info("save segmentationBean content [ "+segmentationBean.toString()+" ]"); 
		JsonResponse<IopushSegmentation> jResponse = new JsonResponse<IopushSegmentation>(); 
		Response response = new Response();
		IopushSegmentation iopushSegment = new IopushSegmentation();
		IopushProduct iopushProduct = new IopushProduct();
		IopushSegmentType iopushSegmentType = new IopushSegmentType();
		logger.info("pendingWelcome Recieved Request for product Id[" + productId + "]");
		try{

			//			product 
			iopushProduct.setProductID(productId);
			iopushSegment.setIopushProduct(iopushProduct);
			iopushSegment.setHash(Utility.generateUniqueId());
			iopushSegment.setSegmentName(segmentationBean.getSegmentName());
			iopushSegmentType.setSegmentTypeId(segmentationBean.getSegmentTypeID());
			iopushSegment.setSegmentType(iopushSegmentType);
			iopushSegment.setCreatedBy(Utility.getUserName());
			iopushSegment.setCreatedOn(new Date());
			if(segmentationBean.getSegmentId() > 0)
			{
				iopushSegment.setSegmentId(segmentationBean.getSegmentId());
				iopushSegment.setModifiedBy(Utility.getUserName());
				iopushSegment.setModifiedOn(new Date());
				response = this.iSegmentDao.save(iopushSegment);
			}
			else
			{
				iopushSegment.setModifiedBy(Utility.getUserName());
				iopushSegment.setModifiedOn(new Date());
				response = this.iSegmentDao.save(iopushSegment); 
			}

			// data has been saved in welcome table
			if(response.getStatus())
			{
				logger.info("segmentation is saved successfully");
				jResponse.setRecord(iopushSegment);
				jResponse.setResult(Constants.SUCCESS);
			}
	}catch(Exception e)
		{
		logger.error("An Error occurred while save/update a segment",e);
			jResponse.setMessage("Records could not be saved/updated.");
			jResponse.setResult(Constants.ERROR_CODE_INVALID + "");
		}
		return jResponse;

	
}
	
	@Override
	public ResponseMessage deleteSegmentation(SegmentationBean segmentationBean, Integer productId) {
		//productId = 1;
		logger.info("deletesegmentationBean [ "+segmentationBean.toString()+" ]");
		ResponseMessage rResponse = new ResponseMessage();
		int segmentId = segmentationBean.getSegmentId();
		try {
			Response response = iSegmentDao.deleteSegment(segmentId, productId);
			if (response.getIntegerResult() > 0) {
				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				String update_Subscribers = myProperties.getProperty(env + "UPDATE_SUBSCRIBERS_QUEUE");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("segmentId",segmentId);
					jmsTemplate.convertAndSend("UPDATE_SUBSCRIBERS_QUEUE", map);
				
			//	Response res = iSegmentDao.updateSubscribers(segmentId);
				rResponse = new ResponseMessage(Constants.SUCCESS_CODE, "Successfully Deleted");
				logger.info("deleteWelcome segmentId [ " + segmentId + " ] Successfully Deleted");
			} else {
				rResponse = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Deletion Failed");
				logger.info("deleteWelcome segmentId [ " + segmentId + " ] Deletion Failed");
			}
		} catch (Exception e) {
			rResponse = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			logger.error("deleteWelcome segmentId [ " + segmentId + " ] " ,  e);
		}
		return rResponse;
	}
	
	@Override
	public JsonResponse<SegmentationBean> listSegmentation(SegmentationBean segmentationBean, Integer productId) {
	//	productId = 1;
		logger.info("SegmentationBean [ " + segmentationBean.toString()+" ]");
		JsonResponse<SegmentationBean> jsonResponse = new JsonResponse<SegmentationBean>();
	
		int startIndex = segmentationBean.getStartIndex();
		int pagesize = segmentationBean.getPageSize();
		
		try {
			int count=iSegmentDao.countSegment(startIndex, pagesize, productId);
			List<IopushSegmentation> resnew=iSegmentDao.listSegmentation(startIndex, pagesize,productId,segmentationBean.getColumnForOrdering(), segmentationBean.getRequiredOrder());
			if(!resnew.isEmpty()){
				List<SegmentationBean> listSegment = new ArrayList<SegmentationBean>();
					Iterator itr = resnew.iterator();
					int segmentId;
					String segmentName;
					Date createdOn;
					Date modifiedOn;
					String hash;
					int segmentTypeID;
					String createdBy;
					String segmentTypeName;
					while(itr.hasNext()){
					   Object[] obj = (Object[]) itr.next();
					   segmentId = (int) obj[0];
					   segmentName = (String) obj[1];
					   createdOn = (Date) obj[2];
					   modifiedOn = (Date) obj[3];
					   hash = (String) obj[4];
					   segmentTypeID = (int) obj[5];
					   createdBy = (String) obj[6];
					   segmentTypeName = (String) obj[7];
					   listSegment.add(new SegmentationBean(segmentId, segmentName, createdOn , modifiedOn,hash,segmentTypeID,createdBy,segmentTypeName,productId));
					   
					   
				/*	   //now you have one array of Object for each row
					   String welcomename = String.valueOf(obj[0]); // don't know the type of column CLIENT assuming String 
					   Integer service = Integer.parseInt(String.valueOf(obj[1])); //SERVICE assumed as int
					   //same way for all obj[2], obj[3], obj[4]
	*/				}
				
				
				
				
				jsonResponse = new JsonResponse<SegmentationBean>(Constants.SUCCESS, listSegment, count);

				logger.info("SegmentationBean Fetch Total Size [ " + listSegment.size()+" ]");
			} else {
				jsonResponse = new JsonResponse<SegmentationBean>(Constants.SUCCESS, "NO DATA FOUND");
				logger.info("SegmentationBean NO DATA FOUND");
			}
		} catch (Exception e) {
			jsonResponse = new JsonResponse<SegmentationBean>(Constants.ERROR, "CAUGHT EXCEPTION");
			logger.error("findSegmentList " , e);
		}
		return jsonResponse;
	}

	@Override
	public JsonResponse<IopushSegmentType> saveSegmentType(SegmentTypeBean segmentTypeBean, Integer productId) {
	//	productId=1;
		logger.info("save segmentTypeBean content [ "+segmentTypeBean.toString()+" ]"); 
		JsonResponse<IopushSegmentType> jResponse = new JsonResponse<IopushSegmentType>(); 
		ResponseMessage responseMessage = new ResponseMessage();
		Response response = new Response();
		IopushSegmentType iopushSegmentType = new IopushSegmentType();
		IopushProduct iopushProduct = new IopushProduct();
		try{
			//			product 
			iopushSegmentType.setSegmentTypeName(segmentTypeBean.getSegmentTypeName());
			iopushSegmentType.setProductId(productId);
			iopushSegmentType.setSegmentTypeId(segmentTypeBean.getSegmentTypeId());
			response = this.iSegmentDao.saveSegmentType(iopushSegmentType);
			// data has been saved in welcome table
			jResponse.setRecord(iopushSegmentType);
			jResponse.setMessage("Records saved/updated");
			
		}catch(Exception e)
		{
			logger.error("An Error occurred while save/update a segment",e);
			jResponse.setMessage("Records could not be saved/updated.");
			jResponse.setResult(Constants.ERROR);
		}
		return jResponse;

	
}
	
	@SuppressWarnings("unchecked")
	@Override
	public JsonResponse<ComboBoxOption> listSegmentType(Integer pid) {
		logger.info("inside ListSegmentType");
		//pid =1;
		Response response = iSegmentDao.listSegmentType(pid);
		logger.info("listSegmentType response status is [ "+ response.getStatus()+" ]" );
		List<IopushSegmentType> listIopushSegmentType = (List<IopushSegmentType>) response.getData();
		List<ComboBoxOption> objListDisplayOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (response.getStatus()) {

			for (IopushSegmentType iopushSegmentType : listIopushSegmentType) {
				objListDisplayOption
				.add(new ComboBoxOption("" + iopushSegmentType.getSegmentTypeId(), iopushSegmentType.getSegmentTypeName()));
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
	public JsonResponse<ComboBoxOption> listSegment(String segmentType_id , Integer pid) {
		logger.info("inside ListSegment"+segmentType_id);
		//pid =1;
		Response response = iSegmentDao.listSegment(segmentType_id,pid);
		logger.info("listSegmentType response status is [ "+ response.getStatus()+" ]" );
		List<IopushSegmentation> listIopushSegment = (List<IopushSegmentation>) response.getData();
		List<ComboBoxOption> objListDisplayOption = new ArrayList<ComboBoxOption>();
		JsonResponse<ComboBoxOption> jsonResp;
		if (response.getStatus()) {

			for (IopushSegmentation iopushSegment : listIopushSegment) {
				objListDisplayOption
				.add(new ComboBoxOption("" + iopushSegment.getSegmentId(), iopushSegment.getSegmentName()));
			}
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.SUCCESS, objListDisplayOption,
					objListDisplayOption.size());
		} else {
			jsonResp = new JsonResponse<ComboBoxOption>(Constants.ERROR, response.getErrorMessage());
		}
		return jsonResp;
	}
	
	@Override
	public JsonResponse<SegmentationBean> fetchSegment(SegmentationBean segmentationBean, Integer productId) {
			//productId = 1;
		logger.info("Inside fetchSegment method segmentationBean Id is [ "+segmentationBean.getSegmentId()+" ]");
		JsonResponse<SegmentationBean> jsonResponse = new JsonResponse<SegmentationBean>();
		Response response = new Response(); 
		int segmentId =segmentationBean.getSegmentId();
		try
		{
			response = iSegmentDao.getSegmentData(segmentId, productId);
			if(response.getStatus())
			{
				IopushSegmentation iopushSegmentation = (IopushSegmentation) response.getScalarResult();
				segmentationBean.setSegmentName(iopushSegmentation.getSegmentName());
				segmentationBean.setSegmentTypeID(iopushSegmentation.getSegmentType().getSegmentTypeId());
				segmentationBean.setCreatedBy(iopushSegmentation.getCreatedBy());
				segmentationBean.setCreatedOn(iopushSegmentation.getCreatedOn());
				segmentationBean.setModifiedBy(iopushSegmentation.getModifiedBy());
				segmentationBean.setModifiedOn(iopushSegmentation.getModifiedOn());
				segmentationBean.setHash(iopushSegmentation.getHash());
				segmentationBean.setProductId(iopushSegmentation.getIopushProduct().getProductID());
				
				
				jsonResponse = new JsonResponse<SegmentationBean>(Constants.SUCCESS, segmentationBean, 1);
				logger.info("fetchSegment data successfully retrieved. Here is the segmentationBean data [ "+segmentationBean.toString()+" ]");
			}
			else
			{
				jsonResponse = new JsonResponse<SegmentationBean>(Constants.SUCCESS, "Data not found.");
				logger.info("fetchSegment data not found corresponding to segmentId [ "+segmentId+" ]");
			}
		}
		catch (Exception e) {
			jsonResponse = new JsonResponse<SegmentationBean>(Constants.ERROR, "Oops! something went wrong.");
		}
		return jsonResponse;
	}
	
	@Override
	public JsonResponse<String> subscriberCount(Integer sid,Integer pid) {
		logger.info("inside subscriberCount");
		String subSelectQuery = "select count(subscribers_id) from " + Constants.IOPUSH_SUBSCRIBERS + " where fk_product_id="+pid +" and fk_segmentid="+sid;
		String masterQuery = "";
		
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
	
	@Override

	public ResponseMessage checkSegmentName(SegmentationBean segmentationBean , Integer product_id) {
		//product_id =1;
		logger.info("checksegmentname->" + segmentationBean.toString());
		ResponseMessage rm = null;
		String segmentName = segmentationBean.getSegmentName();
		int segmentid =  segmentationBean.getSegmentId();

		 try {
			 Response response = iSegmentDao.findSegment(segmentName,segmentid,product_id);
			 if (response.getStatus()) {
				 rm = new ResponseMessage(Constants.SUCCESS_CODE, "Campaign " + segmentName + " already exists");
				 logger.info("checkSegmentName segmentName "+segmentName+" alresdy exists");
			 } else {
				 rm = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Campaign " + segmentName + " does not exist");
				 logger.info("checkSegmentName segmentName "+segmentName+" does not exist");
			 }
		 } catch (Exception e) {
			 rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
			 logger.error("checkSegmentName[ " , e+" ]");
		 }
		 logger.info("checkSegmentName Response[ " + rm+" ]");
		 return rm;
	}


}
