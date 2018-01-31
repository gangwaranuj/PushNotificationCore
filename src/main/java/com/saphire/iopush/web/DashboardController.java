package com.saphire.iopush.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saphire.iopush.bean.CityDashBoardBean;
import com.saphire.iopush.bean.GeoBean;
import com.saphire.iopush.bean.ISPBean;
import com.saphire.iopush.bean.LastNotificationBean;
import com.saphire.iopush.bean.PlanBean;
import com.saphire.iopush.bean.PlatformBean;
import com.saphire.iopush.bean.PreviewLastNotificationBean;
import com.saphire.iopush.bean.TrendsBean;
import com.saphire.iopush.bean.ViewsClicksBean;
import com.saphire.iopush.service.IDashboardService;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.SecurityUtils;

@RestController
public class DashboardController {

	@Autowired IDashboardService iDashboardService;

	@RequestMapping(value={"/platformapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<PlatformBean> platformApi(HttpSession session)
	{
		JsonResponse<PlatformBean> response =  this.iDashboardService.platformsApi((Integer)session.getAttribute("productId"));
		return response;
	}

	@RequestMapping(value={"/trendsapi/{startdate}/{enddate}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<TrendsBean> trendsApi(@PathVariable String startdate, @PathVariable String enddate, @PathVariable int action, HttpSession session)
	{
		JsonResponse<TrendsBean> response =  this.iDashboardService.trendsApi(startdate,enddate,action, (Integer)session.getAttribute("productId"));
		return response;
	}

	@RequestMapping(value={"/trendshourapi/{startdate}/{enddate}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<TrendsBean> trendsHourApi(@PathVariable String startdate, @PathVariable String enddate, @PathVariable int action, HttpSession session)
	{
		JsonResponse<TrendsBean> response =  this.iDashboardService.trendsHourApi(startdate,enddate,action, (Integer)session.getAttribute("productId"));
		return response;
	}

	@RequestMapping(value={"/totalViewClickApi/{startdate}/{enddate}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ViewsClicksBean> viewClicksApi(@PathVariable String startdate, @PathVariable String enddate, @PathVariable int action, HttpSession session)
	{
		JsonResponse<ViewsClicksBean> response =  this.iDashboardService.viewClicksApi(startdate,enddate,action, (Integer)session.getAttribute("productId"));
		return response;
	}

	@RequestMapping(value={"/totalViewClickHourApi/{startdate}/{enddate}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ViewsClicksBean> viewClicksHourApi(@PathVariable String startdate, @PathVariable String enddate, @PathVariable int action, HttpSession session)
	{
		JsonResponse<ViewsClicksBean> response =  this.iDashboardService.viewClicksHourApi(startdate,enddate,action, (Integer)session.getAttribute("productId"));
		return response;
	}

	@RequestMapping(value={"/geoapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<GeoBean> geoApi(HttpSession session){
		JsonResponse<GeoBean> response = this.iDashboardService.geoApi((Integer)session.getAttribute("productId"));
		return response ;
	}

	@RequestMapping(value={"/ispapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ISPBean> ispApi(HttpSession session){
		JsonResponse<ISPBean> response = this.iDashboardService.ispApi((Integer)session.getAttribute("productId"));
		return response ;
	}

	 @RequestMapping(value={"/planapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	 public JsonResponse<PlanBean> planApi(HttpSession session){
	  String currentUser = SecurityUtils.getCurrentLogin() ;
	   int prodId=(Integer) session.getAttribute("productId");
	  JsonResponse<PlanBean> response = this.iDashboardService.planApi(currentUser,prodId);
	  return response ;
	 }
	
	@RequestMapping(value={"/lastnotificationapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<LastNotificationBean> lastNotificationApi(HttpSession session){
		JsonResponse<LastNotificationBean> response = this.iDashboardService.lastNotificationApi((Integer)session.getAttribute("productId"));
		return response ;
	}

	
	@RequestMapping(value={"/previewlastnotificationapi"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<PreviewLastNotificationBean> previewLastNotificationApi(HttpSession session){
		JsonResponse<PreviewLastNotificationBean> response = this.iDashboardService.previewLastNotificationApi((Integer)session.getAttribute("productId"));
		return response ;
	}
	
	  @RequestMapping(value={"/fetchCityApi/{country_code}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  public JsonResponse<CityDashBoardBean> fetchCityApi(@PathVariable String country_code,HttpServletResponse httpresponse) throws Exception
	  {
	   JsonResponse<CityDashBoardBean> response = this.iDashboardService.fetchCityApiNew(country_code.toLowerCase());
	   return response;
	  }
}
