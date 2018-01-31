package com.saphire.iopush.service;
import com.saphire.iopush.bean.SegmentTypeBean;
import com.saphire.iopush.bean.SegmentationBean;
import com.saphire.iopush.model.IopushSegmentType;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.utils.ComboBoxOption;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface ISegmentService {
	
	JsonResponse<IopushSegmentation> saveSegmentation(SegmentationBean segmentationBean, Integer productId);
	
	ResponseMessage deleteSegmentation(SegmentationBean segmentationBean, Integer productId);
	
	JsonResponse<SegmentationBean> listSegmentation(SegmentationBean segmentationBean, Integer attribute);
	
	JsonResponse<IopushSegmentType> saveSegmentType(SegmentTypeBean segmentTypeBean, Integer productId);
	
	JsonResponse<ComboBoxOption> listSegmentType(Integer pid);
	
	JsonResponse<ComboBoxOption> listSegment(String segmentType_id,Integer pid);
	
	JsonResponse<SegmentationBean> fetchSegment(SegmentationBean segmentationBean, Integer attribute);
	
	JsonResponse<String> subscriberCount(Integer sid,Integer pid);
	
	ResponseMessage checkSegmentName(SegmentationBean segmentationBean , Integer integer);
}