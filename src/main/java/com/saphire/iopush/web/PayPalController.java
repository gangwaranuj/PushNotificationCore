package com.saphire.iopush.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CreatePaymentBean;
import com.saphire.iopush.bean.IoPushPlanInfoBean;
import com.saphire.iopush.bean.PlanDetailsBean;
import com.saphire.iopush.service.IPayPalAPIService;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;

@Controller
@RequestMapping("paypalapi")
public class PayPalController {

	@Autowired IPayPalAPIService iPayPalAPIService;
	private Logger logger = LoggerFactory.getLogger(PayPalController.class);


	@ResponseBody
	@CrossOrigin("*")
	@RequestMapping(value={"/createPayment"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public Response createPayment(@RequestBody String requestJson,HttpSession session) throws AuthenticationException{

		Gson gson = new Gson();
		CreatePaymentBean createPaymentBean = gson.fromJson(requestJson, CreatePaymentBean.class) ;
		int prodId=(Integer)session.getAttribute("productId");
		Response response=this.iPayPalAPIService.createPayment(prodId,createPaymentBean.getLimit(),createPaymentBean.getPlanId(),createPaymentBean.getAmount(),createPaymentBean.getPackageId());
		return response;
	}

	@RequestMapping(value={"/execute"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String paymentExecute(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
	{
		String queryString=request.getQueryString();
		int prodId=(Integer) session.getAttribute("productId");
		//int prodId=1;
		ResponseMessage rm=  this.iPayPalAPIService.paymentExecute(queryString,prodId);;
		if(rm.getResponseCode()==200){
			if(rm.getData()!=null)
			{
				this.iPayPalAPIService.sendupgradepackageMail((int)rm.getData());
				this.iPayPalAPIService.sendPurchaseConfirmationMail(prodId);
			}
		}
		return "redirect:"+rm.getResponseDescription();
	}


	@ResponseBody
	@CrossOrigin("*")
	@RequestMapping(value={"/fetchPaymentInfo/{paymentId}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<IoPushPlanInfoBean> fetchPaymentInfo(@PathVariable String paymentId,HttpSession session) throws AuthenticationException{

		// this has to be get from session
		int prodId=(Integer) session.getAttribute("productId");
		JsonResponse<IoPushPlanInfoBean> planInfo=this.iPayPalAPIService.fetchPaymenInfo(prodId,paymentId);
		return planInfo;
	}




	/*****************************************/
	/*****For display higher plan***********/ 
	/*****************************************/ 
	@ResponseBody
	@CrossOrigin("*")
	@RequestMapping(value={"/fetchuserplan"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<PlanDetailsBean> fetchUserPlans(HttpSession session){


		int prodId=(Integer) session.getAttribute("productId");  
		//	int prodId=1;

		JsonResponse<PlanDetailsBean> jsonResponse= this.iPayPalAPIService.fetchUserPlan(prodId);
		return jsonResponse;
	}
	/*****************************************/
	/*************!!!!!!******************/
	/*****************************************/

	//		@CrossOrigin("*")
	//		@RequestMapping(value={"/cancelplan"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	//		public String cancelPlan(HttpSession session){
	//	
	//			int prodId=(Integer) session.getAttribute("productId");  
	//			String jsonResponse= this.iPayPalAPIService.cancelPlan(prodId);
	//			return jsonResponse;
	//		}




}
