package com.saphire.iopush.web;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CustomNotificationBean;
import com.saphire.iopush.bean.GetInTouchBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.SubscriptionBean;
import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.service.ICampaignService;
import com.saphire.iopush.service.ICustomNotificationService;
import com.saphire.iopush.service.IExternalAPIService;
import com.saphire.iopush.service.IPayPalAPIService;
import com.saphire.iopush.service.IRSSFeedService;
import com.saphire.iopush.service.IUserService;
import com.saphire.iopush.service.IWelcomeService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping("externalapi")
public class ExternalAPIController {

	@Autowired	IExternalAPIService iExternalAPIService;
	@Autowired	IUserService iUserService;
	@Autowired	ICampaignService iCampaignService;
	@Autowired	ICustomNotificationService iCustomNotificationService;
	@Autowired	IRSSFeedService iRSSFeedService;
	@Autowired	IWelcomeService iWelcomeService;
	@Autowired  IPayPalAPIService iPayPalAPIService;



	private Logger logger = LoggerFactory.getLogger(ExternalAPIController.class);

	@CrossOrigin("*")
	@RequestMapping(value = { "/subscriptionapi" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public JsonResponse<WelcomeBean> subscriptionApi(@RequestBody String requestJson, HttpSession session) {
		Gson gson = new Gson();
		SubscriptionBean subscriptionBean = gson.fromJson(requestJson, SubscriptionBean.class);
		JsonResponse<WelcomeBean> response = this.iExternalAPIService.subscriptionApi(subscriptionBean);
		return response;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "forgetpassword" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseMessage forgetpassword(@RequestBody String requestJson, HttpServletResponse response)
			throws Exception {
		Gson gson = new Gson();
		UserBean userBean = gson.fromJson(requestJson, UserBean.class);
		ResponseMessage jsonResponse = iUserService.forgetpassword(userBean.getUsername());
		return jsonResponse;
	}

	// 1-open 2-close 3-click

	// type =1|2 (1==open and 2==click)
	@CrossOrigin("*")
	@RequestMapping(value = { "/welcomeanalytics/{welcome_name}/{date}/{type}/{product_id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage welcomeAnalytics(@PathVariable String welcome_name, @PathVariable String date,
			@PathVariable int type, @PathVariable int product_id) throws Exception {
		ResponseMessage result = this.iRSSFeedService.welcomeAnalytics(welcome_name, date, type, product_id);
		return result;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "/rssanalytics/{rssname}/{date}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage rssAnalytics(@PathVariable String rssname, @PathVariable String date) throws Exception {
		ResponseMessage result = this.iRSSFeedService.rssAnalytics(rssname, date);
		return result;
	}

	@CrossOrigin("*")
	@RequestMapping(value = {"/campaignanalytics/{campaign_id}/{type}/{geo_id}/{platform_id}/{city_id}/{product_id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage campaignAnalytics(@PathVariable int campaign_id, @PathVariable int type,
			@PathVariable int geo_id, @PathVariable int platform_id, @PathVariable int city_id,
			@PathVariable int product_id) throws Exception {
		ResponseMessage result = this.iCampaignService.campaignAnalytics(campaign_id, type, geo_id, platform_id,
				city_id, product_id);
		return result;
	}



	@CrossOrigin("*")
	@RequestMapping(value = { "registrationemail" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseMessage registrationEmail(@RequestBody String requestJson, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Gson gson = new Gson();

		ResponseMessage registrationemail = new ResponseMessage();
		UserBean userBean = gson.fromJson(requestJson, UserBean.class);
		ResponseMessage jsonResponse = iExternalAPIService.registrationAPI(userBean, request);
		if (jsonResponse.getResponseCode() == 200) {
			Response emailResponse = iExternalAPIService.sendRegistrationMail((IopushUser) jsonResponse.getData());
			if (emailResponse.getStatus()) {
				registrationemail.setResponseCode(Constants.SUCCESS_CODE);
				registrationemail.setResponseDescription("User Registration successful,RegistrationMail sent successfully");
			} else {

				
				registrationemail.setResponseCode(Constants.EMAIL_FAILURE);
				registrationemail.setResponseDescription(emailResponse.getMessage());
			}
		} else {
			if (jsonResponse.getResponseCode() == 206) {
				registrationemail.setResponseCode(Constants.ERROR_CODE_INVALID);
				registrationemail.setResponseDescription(jsonResponse.getResponseDescription());
			} else {
				registrationemail.setResponseCode(Constants.ERROR_CODE_UNKNOWN);
				registrationemail.setResponseDescription("User Registration unsuccessful,");
			}

		}
		return registrationemail;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "/autoexpire" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage autoexpire(HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		ResponseMessage jsonResponse = this.iCampaignService.autoexpire();
		return jsonResponse;
	}

	@CrossOrigin("*")

	@RequestMapping(value="/autofillnotification/{devicetype}", method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<CustomNotificationBean> listCustomNotification(HttpSession session,@RequestParam(value="hash",required=false,defaultValue="")String hash,@PathVariable String devicetype){

		JsonResponse<CustomNotificationBean> result= this.iCustomNotificationService.autofillCustomNotification((Integer)session.getAttribute("productId"),hash,devicetype);
		return result;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "/autolaunch" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage autoLaunch(HttpServletResponse response, HttpSession session) throws Exception {
		ResponseMessage result = this.iCampaignService.autoLaunch();
		return result;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "rssfeedschedule" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage rssfeedschedule() throws Exception {
		ResponseMessage jsonResponse = this.iRSSFeedService.rssfeedschedule();
		return jsonResponse;
	}

	@CrossOrigin("*")
	@RequestMapping(value = { "/autoexpirewelcome" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public ResponseMessage autoexpireWelcome(HttpServletResponse response) throws Exception {
		ResponseMessage jsonResponse = this.iWelcomeService.autoExpireWelcome();
		return jsonResponse;
	}



//	@CrossOrigin("*")
//	@RequestMapping(value = { "/fetchuser" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
//	public ResponseMessage fetchUser() throws ParseException {
//
//		ResponseMessage jsonResponse = this.iExternalAPIService.sendUnImplementedUserReportMail();
//		return jsonResponse;
//	}



	@CrossOrigin("*")
	@RequestMapping(value = { "/getintouch" }, method = {org.springframework.web.bind.annotation.RequestMethod.POST })
	public ResponseMessage getInTouch(@RequestBody String requestJson, HttpServletResponse response,	HttpServletRequest request,HttpSession session) throws Exception {

		Gson gson = new Gson();
		Integer prodId=(Integer)session.getAttribute("productId");
		//prodId=null;
		//Re sponseMessage getIntouchMail= new ResponseMessage();
		GetInTouchBean getInTouchBean = gson.fromJson(requestJson, GetInTouchBean.class);
		ResponseMessage jsonResponse = iExternalAPIService.getInTouch(getInTouchBean,prodId);
		return jsonResponse;
	}

	

	@CrossOrigin("*")
	  @RequestMapping(value={"/autolaunchWelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public ResponseMessage launchCampign(HttpServletResponse response) throws Exception
	  {
			 ResponseMessage jResponse = this.iWelcomeService.autolaunchWelcome();
	    return jResponse;
	  }
	   
//	  @CrossOrigin("*")
//	  @RequestMapping(value={"/fetchuserplan"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
//	  public JsonResponse<PlanBean> fetchUserPlans(){
//
//	   JsonResponse<PlanBean> jsonResponse= this.iExternalAPIService.fetchUserPlan();
//	    return jsonResponse;
//	  }

	  @CrossOrigin("*")
	  @RequestMapping(value={"/UnImplementedUserReportMail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public ResponseMessage UnImplementedUserReportMail() throws ParseException{

	 ResponseMessage jsonResponse= this.iExternalAPIService.sendUnImplementedUserReportMail();
	    return jsonResponse;
	  }

	  @CrossOrigin("*")
		@RequestMapping(value = { "/resendregistrationemail" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
		public ResponseMessage reSendRegistrationEmail(){
			
			ResponseMessage res=this.iExternalAPIService.resendRegistrationMail();
			return res;
		}
	  @CrossOrigin("*")
		@RequestMapping(value = { "/updateActiveStatus/{username}" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
		public ResponseMessage updateActiveStatus(@PathVariable String username){
			
			ResponseMessage res=this.iExternalAPIService.updateActiveStatus(username);
			return res;
		}
		
		@CrossOrigin("*")
		@RequestMapping(value={"/linkroute/{username}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public ResponseMessage linkroute(@PathVariable String username) throws Exception
		{
			ResponseMessage res=this.iExternalAPIService.linkroute(username);
			return res;
		}
		
		@CrossOrigin("*")
		@RequestMapping(value={"/islimitexceed"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public ResponseMessage islimitexceedMail() throws Exception
		{
			ResponseMessage res=this.iExternalAPIService.sendSubscriptionLimitExceedMail();
			return res;
		}
		

		
		

		
		
		
		
		/*****************************************/
		/* Paypal  API Controller  */
		/*****************************************/

		@CrossOrigin("*")
		@RequestMapping(value = { "/paypalCancelAggrement/{userid}" }, method = {org.springframework.web.bind.annotation.RequestMethod.GET })
		public ResponseMessage paypalCancelApi(@PathVariable int userid){

			ResponseMessage res =this.iExternalAPIService.cancelPaypalAggrement(userid);;
			return res;
		}


//		@CrossOrigin("*")
//		@RequestMapping(value={"/sendFailRenewalInfo"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
//		public ResponseMessage sendFailCustomerRenewalReportMailApi() throws ParseException{
//
//			ResponseMessage rm = this.iExternalAPIService.sendFailCustomerRenewalReportMail();
//			return rm;
//		}

		// related to IPN for paypal callback
		@ResponseBody
		@CrossOrigin("*")
		@RequestMapping(value={"/IPN"}, method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
		public void  callBackAPI(HttpServletRequest request){
			
			
			this.iPayPalAPIService.checkIPN(request);
		}
		
		
		//*******************************For IOP-384 ********************
//		@ResponseBody
		@CrossOrigin("*")
		@RequestMapping(value={"/sendEmailReport"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public ResponseMessage sendEmailReport(HttpSession session) throws AuthenticationException{

			ResponseMessage response=this.iPayPalAPIService.sendEmailReport();
			return response;
		}

		
//		@ResponseBody
		@CrossOrigin("*")
		@RequestMapping(value={"/checkUpcomingRenewal"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public void  checkUpcomingRenewal(HttpSession session) throws AuthenticationException{

			this.iPayPalAPIService.checkRenewals();

		}	

		@CrossOrigin("*")
		@RequestMapping(value={"/renewalStatusAPI"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public void  renewalStatusAPI(HttpSession session) throws AuthenticationException{

			Response response=this.iPayPalAPIService.updateRenewalStatus();
		//	Response response = new Response();
		//	HashMap<Integer,Boolean> map= new HashMap<Integer,Boolean>();
		//	map.put(1, true);
		//	map.put(325, false);
		//	map.put(326, false);
		//	map.put(327, true);
	//		response.setScalarResult(map);
			if(response!=null && ((response.getScalarResult()!=null)))
			{
				Map<Integer,Boolean> renewalStatusMap=(Map<Integer, Boolean>) response.getScalarResult();
			this.iPayPalAPIService.sendRenewalStatusMail(renewalStatusMap);
			}

		}
		
		
		//@ResponseBody
		@CrossOrigin("*")
		@RequestMapping(value={"/paypalcustomapi/{paymentid}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public String  paypalcustomapi(HttpSession session,@PathVariable String paymentid) throws AuthenticationException{
		
			Response response = this.iPayPalAPIService.findCustomPaypalResponse(paymentid) ;
			return response.getMessage() ;

		}
		
		/*****************************************/
		/* !***********! */
		/*****************************************/

		
		
		
		
		
		
		
		
}
