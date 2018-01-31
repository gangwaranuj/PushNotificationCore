package com.saphire.iopush.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CustomNotificationBean;
import com.saphire.iopush.service.ICustomNotificationService;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping("custom")
public class CustomNotificationController {

	@Autowired ICustomNotificationService customNotificationService;
	
		@RequestMapping(value="modifycustomnotification", method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public ResponseMessage modifyCustomNotification(@RequestBody String requestJson, HttpSession session){
			
//			
			Gson gson = new Gson();
			CustomNotificationBean customNotificationBean = gson.fromJson(requestJson, CustomNotificationBean.class) ;
			ResponseMessage result = this.customNotificationService.saveCustomNotification(customNotificationBean, (Integer)session.getAttribute("productId"));
			return result;
		}
		
		@RequestMapping(value="/autofillnotification/{devicetype}", method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public JsonResponse<CustomNotificationBean> listCustomNotification(HttpSession session,@RequestParam(value="hash",required=false,defaultValue="")String hash,@PathVariable String devicetype){

			JsonResponse<CustomNotificationBean> result= this.customNotificationService.autofillCustomNotification((Integer)session.getAttribute("productId"),hash,devicetype);
			return result;
		}
}
