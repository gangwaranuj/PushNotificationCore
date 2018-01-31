package com.saphire.iopush.service;

import java.text.ParseException;

import java.util.Properties;


import javax.servlet.http.HttpServletRequest;

import com.saphire.iopush.bean.GetInTouchBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.SubscriptionBean;
import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;

public interface IExternalAPIService {

	JsonResponse<WelcomeBean> subscriptionApi(SubscriptionBean subscriptionBean);

	ResponseMessage campStatApi(String iopush_token, int campaign_id, int action);


	ResponseMessage registrationAPI(UserBean userBean, HttpServletRequest request);

	//	JsonResponse<PlanBean> fetchUserPlan();

	Response sendRegistrationMail(IopushUser iopushUser);

	ResponseMessage sendUnImplementedUserReportMail() throws ParseException;


	//ResponseMessage getInTouch(GetInTouchBean getInTouchBean, int prodId);

	ResponseMessage getInTouch(GetInTouchBean getInTouchBean, Integer prodID);

	ResponseMessage cancelPaypalAggrement(Integer pid);

//	ResponseMessage sendFailCustomerRenewalReportMail() throws ParseException;



	ResponseMessage updateActiveStatus(String username);

	ResponseMessage linkroute(String username);

	ResponseMessage resendRegistrationMail();

	JsonResponse<PlanBean> fetchUserPlan();

	ResponseMessage sendSubscriptionLimitExceedMail();

}
