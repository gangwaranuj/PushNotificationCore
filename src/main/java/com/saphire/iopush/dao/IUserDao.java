package com.saphire.iopush.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;


public interface IUserDao {

	   UserDetails findUserByName(String paramString)throws UsernameNotFoundException;
	   
	   UserDetails findUserById(int userId);
	   
	   Response updateUser(IopushUser iopushUser);
	   
	   Response findUserByMail(String mailId);	
	  
}
