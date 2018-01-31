package com.saphire.iopush.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.CampaignBean;
import com.saphire.iopush.bean.GeoStatBean;
import com.saphire.iopush.bean.SubCampaignBean;
import com.saphire.iopush.service.ICampaignService;
import com.saphire.iopush.service.ISegmentService;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;


/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping("campaignapi")
public class CampaignController {

	@Autowired ICampaignService iCampaignService;
	@Autowired ISegmentService segmentService;
	@RequestMapping(value={"/draftcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> saveCampign(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.draftcampaign(campignBean, (Integer)session.getAttribute("productId"));
		return result;
	}
	
	@RequestMapping(value={"/pendingcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> pendingcampaign(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.pendingcampaign(campignBean, (Integer)session.getAttribute("productId"));
		return result;
	}
	
	@RequestMapping(value={"/campaignlist"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> getCampignList(@RequestBody String requestJson,HttpSession httpSession) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		campignBean.setProductId((Integer)httpSession.getAttribute("productId"));
		JsonResponse<CampaignBean> result = this.iCampaignService.list(campignBean);
		return result;
	}

	@RequestMapping(value={"/campaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> getCampignListbyId(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.fetchByID(campignBean);
		return result;
	} 

	@RequestMapping(value={"/checkcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})

	 public ResponseMessage checkcampaign(@RequestBody String requestJson,HttpServletResponse response , HttpSession session) throws Exception
	 {
	  Gson gson = new Gson();
	  CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
	  ResponseMessage result = this.iCampaignService.checkCampaignName(campignBean, (Integer)session.getAttribute("productId"));
	  return result;
	 }


	@RequestMapping(value={"/changecampaignStatus"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage changecampaignStatus(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		ResponseMessage result = this.iCampaignService.changeCampaignStatus(campignBean);
		return result;
	}

	@RequestMapping(value={"/updatedraftcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> updateDraftCampign(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.updatedraft(campignBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/updatependingcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> updatePendingCampign(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.updatepending(campignBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	
	@RequestMapping(value={"/deletecampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<CampaignBean> deleteCampign(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		JsonResponse<CampaignBean> result = this.iCampaignService.delete(campignBean);
		return result;
	}

	/*@RequestMapping(value={"/listisp"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ComboBoxOption> listIsp(HttpServletResponse response) throws Exception
	{
		JsonResponse<ComboBoxOption> result = this.iCampaignService.listISP();
		return result;
	}*/

	@RequestMapping(value={"/listisp"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	 public JsonResponse<ComboBoxOption> listIsp(HttpServletResponse response,@RequestParam(required=false,defaultValue="0") Integer[] countryId) throws Exception
	 {
	  JsonResponse<ComboBoxOption> result = this.iCampaignService.listISP(countryId);
	  return result;
	 }
	
	@RequestMapping(value={"/listTimezone"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ComboBoxOption> listTimezone(HttpServletResponse response) throws Exception
	{
		JsonResponse<ComboBoxOption> result = this.iCampaignService.listTimezone();
		return result;
	}

	@RequestMapping(value={"/listCity/{country_id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ComboBoxOption> listCity(@PathVariable int country_id,HttpServletResponse httpresponse) throws Exception
	{
		JsonResponse<ComboBoxOption> response = this.iCampaignService.fetchCity(country_id);
		return response;
	}

	@RequestMapping(value={"/launchcampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage launchCampign(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		ResponseMessage result = this.iCampaignService.launch(campignBean, (Integer)session.getAttribute("productId"));
		return result;
	}

	@RequestMapping(value={"/expirecampaign"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseMessage expireCampaign(@RequestBody String requestJson,HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		ResponseMessage result = this.iCampaignService.expire(campignBean.getCampaign_id());
		return result;
	}

	
	
	@RequestMapping(value={"/campaigncriteria"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public JsonResponse<String> campaignCriteria(HttpServletResponse response, @RequestParam(defaultValue="") String countryId,@RequestParam(defaultValue="") String cityId, @RequestParam(defaultValue="") String isps,@RequestParam(defaultValue="") String subscribed, @RequestParam(defaultValue="") String devices, @RequestParam(defaultValue="") String segments,@RequestParam(defaultValue="") String segmentTypes,HttpSession session) throws Exception
	{



		HashMap<String, String> criteriaList = new HashMap<String,String>();
		String segmentid ="";
		if(!subscribed.isEmpty()){
			//String[] subscribed_dates = subscribed.split(",") ;
			criteriaList.put(Constants.SUBSCRIPTION_RANGE, subscribed) ;
		}

		if(!isps.isEmpty()){
			criteriaList.put(Constants.ISP_ID, isps);
		}

		if(!countryId.isEmpty()){
			criteriaList.put(Constants.COUNTRY_ID, countryId);
		}

		if(!cityId.isEmpty()){
			criteriaList.put(Constants.CITY_ID, cityId);
		}

		if(!devices.isEmpty()){
			criteriaList.put(Constants.DEVICES, devices);
		}
		if(!segments.isEmpty()){
			criteriaList.put(Constants.SEGMENTS, segments);
		}
		if(!segmentTypes.isEmpty()){
			criteriaList.put(Constants.SEGMENTTYPES, segmentTypes);
			if(segments.isEmpty())
			{
				JsonResponse<ComboBoxOption> result= this.segmentService.listSegment(segmentTypes,(Integer)session.getAttribute("productId"));
				for(int i = 0;i<result.getRecords().size();i++)
				{
					System.out.println("segment value="+result.getRecords().get(i).getValue());
					segmentid = result.getRecords().get(i).getValue()+","+segmentid;
				}
				criteriaList.put(Constants.SEGMENTS, segmentid.substring(0, segmentid.length()-1));
			}
		}
		criteriaList.put(Constants.PRODUCT_ID, String.valueOf(session.getAttribute("productId"))) ;

		return iCampaignService.criteriaList(criteriaList);
	}

	

	@RequestMapping(value = "/customernotification", method = RequestMethod.POST)
	public ResponseMessage customerNotification(@RequestBody String requestJson) {

		Gson gson = new Gson();
		CampaignBean campaignBean = gson.fromJson(requestJson, CampaignBean.class) ;

		return iCampaignService.sentNotification(campaignBean) ;
	}

	@RequestMapping(value={"/listplatform"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	public JsonResponse<ComboBoxOption> listPlatform(HttpServletResponse response) throws Exception
	{
		JsonResponse<ComboBoxOption> result = this.iCampaignService.listPlatform();
		return result;
	}

		@RequestMapping(value={"/subcampaignlist"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		  public JsonResponse<SubCampaignBean> getSubCampignListbyId(@RequestBody String requestJson,HttpServletResponse response) throws Exception
		  {
		   Gson gson = new Gson();
		   CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
		   JsonResponse<SubCampaignBean> result = this.iCampaignService.fetchSubCampaignList(campignBean);
		   return result;
		  }
		
		
		
		
		
		

		@RequestMapping(value={"/listproduct"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public JsonResponse<ComboBoxOption> listProduct(HttpServletResponse response) throws Exception
		{
			JsonResponse<ComboBoxOption> result = this.iCampaignService.listProduct();
			return result;
		}
		
		 @CrossOrigin(origins={"*"})
		  @RequestMapping(value={"/campaignstatslist"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		  public JsonResponse<CampaignBean> getCampignStatList(@RequestBody String requestJson,HttpSession httpSession) throws Exception
		  {
			 Gson gson = new Gson();
			 CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
			 campignBean.setProductId((Integer)httpSession.getAttribute("productId"));
		    JsonResponse<CampaignBean> result = this.iCampaignService.findCampaignStatList(campignBean);
		    return result;
		  }
		 @CrossOrigin(origins={"*"})
		  @RequestMapping(value={"campaignstatscountry"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		  public JsonResponse<GeoStatBean> campaignStatsCountry(@RequestBody String requestJson,HttpServletResponse response) throws Exception
		  {
			 Gson gson = new Gson();
			 CampaignBean campignBean = gson.fromJson(requestJson, CampaignBean.class) ;
			 response.setHeader("Access-Control-Allow-Origin", "*");
			 response.setHeader("Access-Control-Allow-Credentials", "true");
			 
		    JsonResponse<GeoStatBean> result = this.iCampaignService.campaignStatsCountry(campignBean);
		    return result;
		  }
		

}

