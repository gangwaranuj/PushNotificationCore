package com.saphire.iopush.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSStatsBean;
import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.service.IRSSFeedService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping("rssapi")
public class RSSController {

	@Autowired IRSSFeedService iRSSFeedService ;

	@RequestMapping(value={"saverssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<IopushRssFeedConfig> saverssfeed(@RequestBody String requestJson,HttpSession session) throws Exception
	{
//		(Integer)session.getAttribute("productId")
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		JsonResponse<IopushRssFeedConfig> jsonResponse  = this.iRSSFeedService.saveRSSFEED(rssfeedBean,(Integer)session.getAttribute("productId")) ;
		return jsonResponse;
	} 

	@RequestMapping(value={"rssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<IopushRssFeedConfig> rssfeed(@RequestBody String requestJson) throws Exception
	{
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		JsonResponse<IopushRssFeedConfig> jsonResponse  = this.iRSSFeedService.rssfeed(rssfeedBean) ;
		return jsonResponse;
	} 


	@RequestMapping(value={"updaterssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<IopushRssFeedConfig> updaterssfeed(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		JsonResponse<IopushRssFeedConfig> jsonResponse  = this.iRSSFeedService.updaterssfeed(rssfeedBean) ;
		return jsonResponse;
	}

	@RequestMapping(value={"deleterssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage deleterssfeed(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		ResponseMessage rmResponse  = this.iRSSFeedService.deleterssfeed(rssfeedBean) ;
		return rmResponse;
	}

	@RequestMapping(value={"statusrssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage statusrssfeed(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		ResponseMessage rmResponse  = this.iRSSFeedService.statusrssfeed(rssfeedBean) ;
		return rmResponse;
	}

	@RequestMapping(value={"/urlValidApi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public ResponseMessage urlValidApi(@RequestParam String url,HttpServletResponse httpresponse) throws Exception
	{
		String uri = java.net.URLDecoder.decode(url, Constants.UTF_8);
		ResponseMessage response = this.iRSSFeedService.urlValid(uri);
		return response;
	}
	
	 @RequestMapping(value={"sendnotification"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
     public ResponseMessage sendnotification(@RequestBody String requestJson,HttpServletResponse response) throws Exception
     {
	     Gson gson = new Gson();
	     RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
	     ResponseMessage jsonResponse  = this.iRSSFeedService.sendNotification(rssfeedBean) ;
	     return jsonResponse;
     }
	 
	 /**********/
	 @RequestMapping(value={"listrssstats"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<RSSStatsBean> listRSSStats(@RequestBody String requestJson,HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			RSSStatsBean rssStatsBean = gson.fromJson(requestJson, RSSStatsBean.class) ;
			rssStatsBean.setProductId((Integer)session.getAttribute("productId"));
			JsonResponse<RSSStatsBean> jsonResponse  = this.iRSSFeedService.listrssStats(rssStatsBean) ;
			return jsonResponse;
		} 
	 /**********/
	 

	 /**********/
	@RequestMapping(value={"listrssfeed"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<RSSFeedBean> listrssfeed(@RequestBody String requestJson,HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		RSSFeedBean rssfeedBean = gson.fromJson(requestJson, RSSFeedBean.class) ;
		rssfeedBean.setProductId((Integer)session.getAttribute("productId"));
		JsonResponse<RSSFeedBean> jsonResponse  = this.iRSSFeedService.listrssfeed(rssfeedBean) ;
		return jsonResponse;
	} 
	 /**********/
	 @RequestMapping(value={"/isurlvalid"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public ResponseMessage isUrlValid(@RequestParam String url,HttpServletResponse httpresponse) throws Exception
		{
			String uri = java.net.URLDecoder.decode(url, Constants.UTF_8);
			ResponseMessage response = this.iRSSFeedService.isUrlValid(uri);
			return response;
		}
	 
}
