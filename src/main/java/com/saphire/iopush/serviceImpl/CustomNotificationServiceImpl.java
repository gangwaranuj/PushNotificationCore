package com.saphire.iopush.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saphire.iopush.bean.CustomNotificationBean;
import com.saphire.iopush.dao.ICustomNotificationDao;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.model.IopushCustomNotification;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.service.ICustomNotificationService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;

@Service
public class CustomNotificationServiceImpl implements ICustomNotificationService{

	@Autowired Properties myProperties;
	@Autowired ICustomNotificationDao iCustomNotificationDao;
	@Autowired IExternalAPIDao iExternalApiDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseMessage saveCustomNotification(CustomNotificationBean customNotificationBean, Integer productId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Response response =new Response();
		try
		{

			response = this.iCustomNotificationDao.getProduct(productId);
			IopushProduct iopushProduct = (IopushProduct) response.getScalarResult();
			
			
			
			
			IopushCustomNotification iopushCustomNotification = new IopushCustomNotification();
			iopushCustomNotification.setTitle(customNotificationBean.getTitle());
			iopushCustomNotification.setMessage(customNotificationBean.getMessage());
			if(!(customNotificationBean.getLogoPath()).equals(""))
			{
				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				String notificationPath = myProperties.getProperty(env + "NOTIFICATION_FOLDER");
				String readnotificationPath = Constants.NOTIFICATION_FOLDER_READ;
				FileOutputStream fos = null;
				String ext = Utility.getExtension(customNotificationBean.getLogoPath());
				String filename = System.currentTimeMillis() + "." + ext;
				String base64Image = customNotificationBean.getLogoPath().split(",")[1];
				byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				fos = new FileOutputStream(new File(notificationPath + filename));
				fos.write(imageBytes);
				fos.close();
				iopushCustomNotification.setLogoPath(readnotificationPath + filename);
			}
			else
			{
				iopushCustomNotification.setLogoPath("");
			}	

			iopushCustomNotification.setOptIn(true);
			//iopushCustomNotification.setProductId(productId);
			iopushCustomNotification.setProduct_id(productId);
			iopushCustomNotification.setPopupBackgroundColor(customNotificationBean.getPopupBackgroundColor()==null?"":customNotificationBean.getPopupBackgroundColor());
			iopushCustomNotification.setPopupColor(customNotificationBean.getPopupColor()==null?"":customNotificationBean.getPopupColor());
			iopushCustomNotification.setAllowBtnBackgroundColor(customNotificationBean.getAllowBtnBackgroundColor()==null?"":customNotificationBean.getAllowBtnBackgroundColor());
			iopushCustomNotification.setAllowBtnColor(customNotificationBean.getAllowBtnColor()==null?"":customNotificationBean.getAllowBtnColor());
			iopushCustomNotification.setDontAllowBtnBackgroundColor(customNotificationBean.getDontAllowBtnBackgroundColor()==null?"":customNotificationBean.getDontAllowBtnBackgroundColor());
			iopushCustomNotification.setDontAllowBtnColor(customNotificationBean.getDontAllowBtnColor()==null?"":customNotificationBean.getDontAllowBtnColor());
			iopushCustomNotification.setCheckFlag(customNotificationBean.isCheckFlag());

			if(customNotificationBean.isCheckFlag())
			{
				iopushCustomNotification.setDelayTime(customNotificationBean.getDelayTime());
			}
			else
			{
				iopushCustomNotification.setDelayTime(0);
			}
			iopushCustomNotification.setType(customNotificationBean.getType()); 
			iopushCustomNotification.setButtonType(customNotificationBean.getButtonType()); 
			
			/*********/
			iopushCustomNotification.setDeviceType(customNotificationBean.getDeviceType());
			
			/*********/

			iopushCustomNotification.setAllowText(customNotificationBean.getAllowText()==null?"":customNotificationBean.getAllowText());
			iopushCustomNotification.setDontAllowText(customNotificationBean.getDontAllowText()==null?"":customNotificationBean.getDontAllowText());
			iopushCustomNotification.setWebsiteUrl("https://" + iopushProduct.getProductName()+".iopushtech.com/" + iopushProduct.getProductName() + "/index.html");
			if(customNotificationBean.getCustomNotificationId() > 0)
				iopushCustomNotification.setCustomNotificationId(customNotificationBean.getCustomNotificationId());
			response = this.iCustomNotificationDao.save(iopushCustomNotification);
			
			if(response.getStatus())
			{
				logger.info("saveCustomNotification Records saved/updated.");
				responseMessage = new ResponseMessage(Constants.SUCCESS_CODE, "Records saved/updated.");
			}
		}
		catch (Exception e) {
			logger.error("saveCustomNotification Failed to save or update records."+e);
			responseMessage = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to save or update records.");
			e.printStackTrace();
		}
		return responseMessage;
	}

	@Override
	public JsonResponse<CustomNotificationBean> autofillCustomNotification(Integer productId,String hash,String deviceType) {
		JsonResponse<CustomNotificationBean> jsonResponse = new JsonResponse<CustomNotificationBean>();
		Response response = new Response();
		CustomNotificationBean customNotificationBean = new CustomNotificationBean();
		logger.info("autofillCustomNotification, productId is {}", productId);
		logger.info("autofillCustomNotification, hash is {}", hash);
		if(hash!=null && !hash.isEmpty()){
			response = this.iExternalApiDao.findProductID(hash);
			productId = ((IopushProduct)response.getScalarResult()).getProductID();
		}

		response = this.iCustomNotificationDao.autofillNotification(productId,deviceType);

		if(response.getStatus()) 
		{
			IopushCustomNotification customNotification = (IopushCustomNotification) response.getScalarResult();
			customNotificationBean.setCustomNotificationId(customNotification.getCustomNotificationId());
			customNotificationBean.setTitle(customNotification.getTitle());
			customNotificationBean.setMessage(customNotification.getMessage());
			customNotificationBean.setLogoPath(customNotification.getLogoPath());
			customNotificationBean.setOptIn(customNotification.isOptIn());
			customNotificationBean.setAllowText(customNotification.getAllowText());
			customNotificationBean.setDontAllowText(customNotification.getDontAllowText());
			customNotificationBean.setWebsiteUrl(customNotification.getWebsiteUrl());

			customNotificationBean.setPopupBackgroundColor(customNotification.getPopupBackgroundColor());
			customNotificationBean.setPopupColor(customNotification.getPopupColor());
			customNotificationBean.setAllowBtnBackgroundColor(customNotification.getAllowBtnBackgroundColor());
			customNotificationBean.setAllowBtnColor(customNotification.getAllowBtnColor());
			customNotificationBean.setDontAllowBtnBackgroundColor(customNotification.getDontAllowBtnBackgroundColor());
			customNotificationBean.setDontAllowBtnColor(customNotification.getDontAllowBtnColor());
			customNotificationBean.setCheckFlag(customNotification.isCheckFlag());
			customNotificationBean.setDelayTime(customNotification.getDelayTime());
			customNotificationBean.setType(customNotification.getType());
			customNotificationBean.setButtonType(customNotification.getButtonType());
			customNotificationBean.setDeviceType(customNotification.getDeviceType());

			jsonResponse = new JsonResponse<CustomNotificationBean>(Constants.SUCCESS, customNotificationBean);
			logger.info("autofillCustomNotification record successfully fetched."  );
			logger.debug("autofillCustomNotification data is [ "+customNotificationBean.toString()+" ]");
		}
		else
		{
			logger.info("autofillCustomNotification Record is not available.");
			jsonResponse = new JsonResponse<CustomNotificationBean>(Constants.SUCCESS, "Record is not available.");
		}
		return jsonResponse;
	}

//	@Override
//	public JsonResponse<CustomNotificationBean> autofillCustomNotificationByDeviceID(Integer productId, String hash,String deviceType) {
//		
//		logger.info("inside autofillCustomNotificationByDeviceID.");
//		JsonResponse<CustomNotificationBean> jsonResponse = new JsonResponse<CustomNotificationBean>();
//		Response response = new Response();
//		CustomNotificationBean customNotificationBean = new CustomNotificationBean();
//
//		if(hash!=null && !hash.isEmpty()){
//			response = this.iExternalApiDao.findProductID(hash);
//			productId = ((IopushProduct)response.getScalarResult()).getProductID();
//		}
//
//		response = this.iCustomNotificationDao.autofillNotificationBYDeviceType(productId,deviceType);
//
//		if(response.getStatus()) 
//		{
//			IopushCustomNotification customNotification = (IopushCustomNotification) response.getScalarResult();
//			customNotificationBean.setCustomNotificationId(customNotification.getCustomNotificationId());
//			customNotificationBean.setTitle(customNotification.getTitle());
//			customNotificationBean.setMessage(customNotification.getMessage());
//			customNotificationBean.setLogoPath(customNotification.getLogoPath());
//			customNotificationBean.setOptIn(customNotification.isOptIn());
//			customNotificationBean.setAllowText(customNotification.getAllowText());
//			customNotificationBean.setDontAllowText(customNotification.getDontAllowText());
//			customNotificationBean.setWebsiteUrl(customNotification.getWebsiteUrl());
//
//			customNotificationBean.setPopupBackgroundColor(customNotification.getPopupBackgroundColor());
//			customNotificationBean.setPopupColor(customNotification.getPopupColor());
//			customNotificationBean.setAllowBtnBackgroundColor(customNotification.getAllowBtnBackgroundColor());
//			customNotificationBean.setAllowBtnColor(customNotification.getAllowBtnColor());
//			customNotificationBean.setDontAllowBtnBackgroundColor(customNotification.getDontAllowBtnBackgroundColor());
//			customNotificationBean.setDontAllowBtnColor(customNotification.getDontAllowBtnColor());
//			customNotificationBean.setCheckFlag(customNotification.isCheckFlag());
//			customNotificationBean.setDelayTime(customNotification.getDelayTime());
//			customNotificationBean.setType(customNotification.getType());
//			customNotificationBean.setButtonType(customNotification.getButtonType());
//			customNotificationBean.setDeviceType(customNotification.getDeviceType());
//
//			jsonResponse = new JsonResponse<CustomNotificationBean>(Constants.SUCCESS, customNotificationBean);
//			logger.info("autofillCustomNotificationByDeviceID record successfully fetched.");
//			logger.debug("autofillCustomNotificationByDeviceID data is [ "+customNotificationBean.toString()+" ]");
//		}
//		else
//		{
//			logger.info("autofillCustomNotificationByDeviceID Record is not available.");
//			jsonResponse = new JsonResponse<CustomNotificationBean>(Constants.SUCCESS, "Record is not available.");
//		}
//		return jsonResponse;
//	}

}
