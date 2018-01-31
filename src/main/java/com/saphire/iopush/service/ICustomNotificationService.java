package com.saphire.iopush.service;

import com.saphire.iopush.bean.CustomNotificationBean;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface ICustomNotificationService {
	
	ResponseMessage saveCustomNotification(CustomNotificationBean customNotificationBean, Integer productId);

	
	JsonResponse<CustomNotificationBean> autofillCustomNotification(Integer attribute,String hash,String deviceType);
	
	
	
}
