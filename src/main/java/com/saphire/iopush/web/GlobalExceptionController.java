package com.saphire.iopush.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionController {

	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
	
/*	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> failedAuth() {
		Response response = new Response();
		response.setStatus(false);
		response.setStatusCode(com.saphire.iopush.utils.Constants.PAYPAL_API_FAILED_REQUEST_CODE);
		
		Gson gson = new Gson();
		return new ResponseEntity(gson.toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
	}*/

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> Error(Exception e) {
		
		ResponseMessage response = new ResponseMessage();
		logger.error("Inside the globalExceptionController exception is ",e);
		response.setResponseCode(Constants.GENERIC_ERROR);
		response.setResponseDescription("Some exception occured");
		//response.setStatus(false);
		//response.setStatusCode(com.saphire.iopush.utils.Constants.ERROR_CODE_INVALID);	
		Gson gson = new Gson();
		e.printStackTrace();
		return new ResponseEntity(gson.toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
