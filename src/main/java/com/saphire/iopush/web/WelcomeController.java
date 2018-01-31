package com.saphire.iopush.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.bean.WelcomeStatsBean;
import com.saphire.iopush.service.IWelcomeService;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping("welcomeapi")
public class WelcomeController {

	@Autowired IWelcomeService iWelcomeService;

	@RequestMapping(value={"/pendingwelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<WelcomeBean> pendingWelcome(@RequestBody String requestJson, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		JsonResponse<WelcomeBean> result = this.iWelcomeService.pendingWelcome(welcomeBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/draftwelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<WelcomeBean> draftWelcome(@RequestBody String requestJson, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		JsonResponse<WelcomeBean> result = this.iWelcomeService.draftWelcome(welcomeBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/deletewelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage deleteWelcome(@RequestBody String requestJson, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		ResponseMessage result = this.iWelcomeService.deleteWelcome(welcomeBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/changestatus"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage changeStatus(@RequestBody String requestJson, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		ResponseMessage result = this.iWelcomeService.changeStatus(welcomeBean, (Integer)session.getAttribute("productId"));
		return result;
	}


	/*
	@CrossOrigin(origins={"*"})
	  @RequestMapping(value={"/launchWelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	  public JsonResponse<WelcomeBean> launchCampign(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	  {
		  Gson gson = new Gson();
		  WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
			 response.setHeader("Access-Control-Allow-Origin", "*");
			 response.setHeader("Access-Control-Allow-Credentials", "true");
			 JsonResponse<WelcomeBean> jResponse = this.iWelcomeService.launchWelcome(welcomeId, productId)(welcomeBean);
	    return jResponse;
	  }
	 */
	@RequestMapping(value={"/fetchwelcomebyid"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<WelcomeBean> fetchWelcome(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		JsonResponse<WelcomeBean> result = this.iWelcomeService.fetchWelcome(welcomeBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/expirewelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage expireWelcome(@RequestBody String requestJson, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
		ResponseMessage result = this.iWelcomeService.expireWelcome(welcomeBean.getWelcomeId(), (Integer)session.getAttribute("productId"));
		return result;
	}

	
	@RequestMapping(value={"/checkWelcomeName/{welcomeName}/{welcomeId}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	 public ResponseMessage checkWelcomeName(@PathVariable String welcomeName,@PathVariable Integer welcomeId,HttpServletResponse response , HttpSession session) throws Exception
	 {
	  ResponseMessage result = this.iWelcomeService.checkWelcomeName(welcomeName,welcomeId, (Integer)session.getAttribute("productId"));
	  return result;
	 }
	
	
	 @RequestMapping(value={"/listWelcomeStats"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<WelcomeStatsBean> listWelcomeStats(@RequestBody String requestJson,HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			WelcomeStatsBean welcomeStatsBean = gson.fromJson(requestJson, WelcomeStatsBean.class) ;
			welcomeStatsBean.setProductId((Integer)session.getAttribute("productId"));
			JsonResponse<WelcomeStatsBean> jsonResponse  = this.iWelcomeService.listwelcomeStats(welcomeStatsBean) ;
			return jsonResponse;
		} 
		@RequestMapping(value={"/listwelcome"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<WelcomeBean> listWelcome(@RequestBody String requestJson, HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			WelcomeBean welcomeBean = gson.fromJson(requestJson, WelcomeBean.class) ;
			JsonResponse<WelcomeBean> jResponse = this.iWelcomeService.listWelcome(welcomeBean, (Integer)session.getAttribute("productId"));
			return jResponse ;
		}


}
