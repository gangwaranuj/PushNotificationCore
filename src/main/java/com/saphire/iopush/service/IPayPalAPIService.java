package com.saphire.iopush.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.auth.AuthenticationException;

import com.saphire.iopush.bean.IoPushPlanInfoBean;
import com.saphire.iopush.bean.PlanDetailsBean;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;

public interface IPayPalAPIService {



	

	ResponseMessage paymentExecute(String reqUrl, int integer) throws AuthenticationException, com.maxmind.geoip2.exception.AuthenticationException, IOException;

	
	 ResponseMessage sendEmailReport();

	JsonResponse<PlanDetailsBean> fetchUserPlan(int prodId);

//	String cancelPlan(int prodId);
	
	ResponseMessage sendRenewalStatusMail(Map<Integer, Boolean> renewalStatusMap);

	Response createPayment(int productID, int limit, int planID, int amount, int packageId)			throws AuthenticationException;


	void checkRenewals();

	Response updateRenewalStatus();
	
	Response findCustomPaypalResponse(String paymentId);

	JsonResponse<IoPushPlanInfoBean> fetchPaymenInfo(int prodId, String paymentId);


	void checkIPN(HttpServletRequest request);


	Response sendupgradepackageMail(int prodID);
	
	
	Response sendPurchaseConfirmationMail(int prod);

}
