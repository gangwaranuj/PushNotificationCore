package com.saphire.iopush.service;

import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.bean.WelcomeStatsBean;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface IWelcomeService {

	JsonResponse<WelcomeBean> pendingWelcome(WelcomeBean welcomeBean, Integer productId);
	
	JsonResponse<WelcomeBean> draftWelcome(WelcomeBean welcomeBean, Integer productId);

	ResponseMessage deleteWelcome(WelcomeBean welcomeBean, Integer attribute);

	ResponseMessage changeStatus(WelcomeBean welcomeBean, Integer attribute);

	JsonResponse<WelcomeBean> listWelcome(WelcomeBean welcomeBean, Integer attribute);
	

	JsonResponse<WelcomeBean> fetchWelcome(WelcomeBean welcomeBean, Integer attribute);

	ResponseMessage expireWelcome(int welcomeId, Integer attribute);

	ResponseMessage checkWelcomeName(String welcomeName,Integer welcomeId, Integer attribute);
	
	ResponseMessage autoExpireWelcome();
	
	ResponseMessage autolaunchWelcome();

	ResponseMessage launchWelcome(IopushWelcome iopushWelcome, WelcomeBean welcomeBean, Integer productId);
	
	ResponseMessage autoStartWelcome();
	
	JsonResponse<WelcomeStatsBean> listwelcomeStats(WelcomeStatsBean welcomeStatsBean);

}
