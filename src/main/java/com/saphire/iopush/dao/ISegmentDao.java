package com.saphire.iopush.dao;

import java.util.List;

import com.saphire.iopush.model.IopushSegmentType;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.utils.Response;

public interface ISegmentDao {
	Response save(IopushSegmentation iopushSegmentation);

	Response deleteSegment(int segmentId, int productId);

	Response getProduct(Integer productId);
	
	Response saveSegmentType(IopushSegmentType iopushSegmentType);
	
	int countSegment( int startIndex, int pagesize,int pid);
	
	List<IopushSegmentation> listSegmentation(int startIndex, int pagesize,int pid, String string, String string2);
	
	Response listSegmentType(int pid);
	
	Response updateSubscribers(int productId);
	
	Response listSegment(String segmentType_id, int pid);
	
	Response getSegmentData(int segmentId, int productId);
	
	Response findSegment(String segmentname,int segmentid, int product_id);
}
