package com.saphire.iopush.dao;

import com.saphire.iopush.model.IopushCustomNotification;
import com.saphire.iopush.utils.Response;

public interface ICustomNotificationDao {
	
	Response save(IopushCustomNotification iopushCustomNotification);
	
//	Response autofillNotification(Integer productId);

	Response autofillNotification(Integer productId,String deviceType);
	
	Response getProduct(Integer productId);
	
	Response insertCustomNoitifcationDetails(IopushCustomNotification iopushCustomNotification);
	
}
