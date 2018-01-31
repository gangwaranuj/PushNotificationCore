package com.saphire.iopush.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.saphire.iopush.bean.SegmentTypeBean;
import com.saphire.iopush.bean.SegmentationBean;
import com.saphire.iopush.model.IopushSegmentType;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.service.ISegmentService;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

@RestController
@RequestMapping("segment")
public class SegmentController {

	@Autowired ISegmentService segmentService;
	
		@RequestMapping(value="saveSegmentation", method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<IopushSegmentation> saveSegmentation(@RequestBody String requestJson, HttpSession session){
			
			Gson gson = new Gson();
			SegmentationBean segmentationBean = gson.fromJson(requestJson, SegmentationBean.class) ;
			JsonResponse<IopushSegmentation> jResponse = this.segmentService.saveSegmentation(segmentationBean, (Integer)session.getAttribute("productId"));
			return jResponse;
		}
		@RequestMapping(value={"/deleteSegmentation"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public ResponseMessage deleteSegmentation(@RequestBody String requestJson, HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			SegmentationBean segmentationBean = gson.fromJson(requestJson, SegmentationBean.class) ;
			ResponseMessage result = this.segmentService.deleteSegmentation(segmentationBean, (Integer)session.getAttribute("productId"));
			return result;
		}
		
		@RequestMapping(value={"/listSegmentation"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<SegmentationBean> listSegmentation(@RequestBody String requestJson, HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			SegmentationBean segmentationBean = gson.fromJson(requestJson, SegmentationBean.class) ;
			JsonResponse<SegmentationBean> jResponse = this.segmentService.listSegmentation(segmentationBean, (Integer)session.getAttribute("productId"));
			return jResponse ;
		}
		@RequestMapping(value="saveSegmentType", method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<IopushSegmentType> saveSegmentType(@RequestBody String requestJson, HttpSession session){
			
			Gson gson = new Gson();
			SegmentTypeBean segmentTypeBean = gson.fromJson(requestJson, SegmentTypeBean.class) ;
			JsonResponse<IopushSegmentType> jResponse = this.segmentService.saveSegmentType(segmentTypeBean, (Integer)session.getAttribute("productId"));
			return jResponse;
		}
		@RequestMapping(value={"/listSegmentType"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public JsonResponse<ComboBoxOption> listSegmentType(HttpServletResponse response, HttpSession session) throws Exception
		{
			JsonResponse<ComboBoxOption> result = this.segmentService.listSegmentType((Integer)session.getAttribute("productId"));
			return result;
		}
		
		@RequestMapping(value={"/listSegment/{segmentType_id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		public JsonResponse<ComboBoxOption> listSegment(@PathVariable String segmentType_id,HttpServletResponse response, HttpSession session) throws Exception
		{
			JsonResponse<ComboBoxOption> result = this.segmentService.listSegment(segmentType_id,(Integer)session.getAttribute("productId"));
			return result;
		}
		
		@RequestMapping(value={"/fetchsegmentbyid"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<SegmentationBean> fetchSegment(@RequestBody String requestJson,HttpServletResponse response, HttpSession session) throws Exception
		{
			Gson gson = new Gson();
			SegmentationBean segmentationBean = gson.fromJson(requestJson, SegmentationBean.class) ;
			JsonResponse<SegmentationBean> result = this.segmentService.fetchSegment(segmentationBean, (Integer)session.getAttribute("productId"));
			return result;
		}
		
		@RequestMapping(value={"/subscribercount"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		public JsonResponse<String> subscriberCount(HttpServletResponse response, @RequestParam int segmentId,HttpSession session) throws Exception
		{
			return segmentService.subscriberCount(segmentId,(Integer)session.getAttribute("productId"));
		}

		@RequestMapping(value={"/checkSegmentName"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
		 public ResponseMessage checkSegmentName(@RequestBody String requestJson,HttpServletResponse response , HttpSession session) throws Exception
		 {
		  Gson gson = new Gson();
		  SegmentationBean segmentationBean = gson.fromJson(requestJson, SegmentationBean.class) ;
		  ResponseMessage result = this.segmentService.checkSegmentName(segmentationBean, (Integer)session.getAttribute("productId"));
		  return result;
		 }
		
		
}
