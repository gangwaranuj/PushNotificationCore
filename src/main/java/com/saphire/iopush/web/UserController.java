package com.saphire.iopush.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.service.IUserService;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping({ "user" })
public class UserController {
	
	@Autowired
	IUserService userService;

	
	@RequestMapping(value = { "failedAuthentication" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseEntity<String> failedAuth() {

		return new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
	}

	// Change Password
	@RequestMapping(value = { "changePassword" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseMessage changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, HttpServletResponse response) {
		ResponseMessage responseMessage = userService.changePassword(oldPassword, newPassword);
		return responseMessage;

	}

	// updatePhoto
	@RequestMapping(value = { "updateImage" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public JsonResponse<UserBean> updateImage(@RequestParam("file") String imagefile,
			@RequestParam("userId") int userId, HttpServletResponse response) {
		JsonResponse<UserBean> Jsonresponse = userService.uploadImage(imagefile, userId);
		return Jsonresponse;

	}

	// update Profile
	@RequestMapping(value = { "updateProfile" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseMessage updateProfile(@RequestBody String requestJson) {
		Gson gson = new Gson();
		UserBean userBean = gson.fromJson(requestJson, UserBean.class);
		ResponseMessage responseMessage = userService.updateProfile(userBean);
		return responseMessage;

	}

	 //remove image 
	  @RequestMapping(value={"removeImage"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	  public ResponseMessage removeImage(@RequestParam("userId") int userId)
	  {
	   
	   ResponseMessage responseMessage=userService.deleteImage(userId);
	   return responseMessage;
	   
	  }
	  
	 
	
}
