package com.saphire.iopush.service;

import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface IUserService {

	ResponseMessage deleteImage(int userId) ;
	
	ResponseMessage changePassword(String currentPassword,String newPassword);
	
	JsonResponse<UserBean> uploadImage(String imageData,int userId);
	
	ResponseMessage updateProfile(UserBean user);
	
	ResponseMessage forgetpassword(String username);
}
