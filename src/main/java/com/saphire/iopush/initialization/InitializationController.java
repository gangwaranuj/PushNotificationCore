package com.saphire.iopush.initialization;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.saphire.iopush.service.IWelcomeService;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
public class InitializationController {

	@Autowired IWelcomeService iWelcomeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@PostConstruct
	public void init() throws Exception
	  {
			logger.info("In init method of auto initialization");
			 ResponseMessage jResponse = this.iWelcomeService.autoStartWelcome();
			 logger.info("Response after fetching from init method of initialization method is: "+jResponse);
	  }
}
