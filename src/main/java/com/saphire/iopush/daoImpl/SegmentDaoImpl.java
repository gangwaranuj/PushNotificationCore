package com.saphire.iopush.daoImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.dao.ISegmentDao;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSegmentType;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;

@Repository
@Transactional
public class SegmentDaoImpl implements ISegmentDao{

	@Autowired HibernateTemplate hibernateTemplate;
	
	@Override
	public Response save(IopushSegmentation iopushSegmentation) {
		Response response = new Response();
		hibernateTemplate.saveOrUpdate(iopushSegmentation);
		response.setIntegerResult(iopushSegmentation.getSegmentId());
		response.setStatus(true);
		return response;
	}

	
	@Override
	public Response saveSegmentType(IopushSegmentType iopushSegmentType) {
		Response response = new Response();
		hibernateTemplate.saveOrUpdate(iopushSegmentType);
		response.setIntegerResult(iopushSegmentType.getSegmentTypeId());
		response.setStatus(true);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response getProduct(Integer productId) {
		Response response = new  Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushProduct.class);
		criteria.add(Restrictions.eq("productID", productId));
		
		List<IopushProduct> listProduct = (List<IopushProduct>) this.hibernateTemplate.findByCriteria(criteria);
		if(listProduct.size() > 0)
		{
			response.setStatus(true);
			response.setScalarResult(listProduct.get(0)); 
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}

	
	@Override
	public Response deleteSegment(int segmentId, int productId) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.SEGMENT_DELETE_QUERY, segmentId, productId));
		return res;
	}
	
	@Override
	public Response updateSubscribers(int productId) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.SUBSCRIBER_UPDATE_QUERY, productId));
		return res;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly=false)
	public int countSegment(int startIndex,int pageSize,int pid){
		int count = 0;
		try
		{
			List<Integer> pids = new ArrayList<>();
			  pids.add(0);
			  pids.add(pid);
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
			criteria.createAlias("iopushProduct", "product");
				criteria.add(Restrictions.in("product.productID", pids));
	
			ProjectionList projection = Projections.projectionList();
			projection.add(Projections.rowCount());
			criteria.setProjection(projection) ;
			
			List<Long> list = (List<Long>) hibernateTemplate.findByCriteria(criteria);

			count = list.get(0).intValue();

		}
		catch(Exception e)
		{
			e.getStackTrace();
		}

		return count;

	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public  List<IopushSegmentation> listSegmentation(int startIndex,int pageSize,int pid,String columnForOrdering, String requiredOrder){
		List<IopushSegmentation> listSegmentation = new ArrayList<>();
		try
		{
			List<Integer> pids = new ArrayList<>();
			  pids.add(0);
			  pids.add(pid);
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
			criteria.createAlias("segmentType", "segmentType");
			criteria.createAlias("iopushProduct", "product");
			criteria.add(Restrictions.in("product.productID", pids));
			criteria.add(Restrictions.ne("segmentId", 0));
			ProjectionList projList = Projections.projectionList();
			
			projList.add(Projections.property("segmentId")) ;
			projList.add(Projections.property("segmentName")) ;
			projList.add(Projections.property("createdOn")) ;
			projList.add(Projections.property("modifiedOn")) ;
			projList.add(Projections.property("hash")) ;
			projList.add(Projections.property("segmentType.segmentTypeId")) ;
			projList.add(Projections.property("createdBy")) ;
			projList.add(Projections.property("segmentType.segmentTypeName")) ;
			
			criteria.setProjection(projList);
			if(requiredOrder.equalsIgnoreCase("DESC"))
			{
				criteria.addOrder(Order.desc(columnForOrdering));
			}
			else
			{
				criteria.addOrder(Order.asc(columnForOrdering));
			}
			listSegmentation = (List<IopushSegmentation>) hibernateTemplate.findByCriteria(criteria,startIndex,pageSize);
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
		return listSegmentation;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Response listSegmentType(int pid) {
		Response response = new Response();
		List<Integer> pids = new ArrayList<>();
		  pids.add(0);
		  pids.add(pid);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentType.class);
		criteria.add(Restrictions.in("productId", pids));
		List<IopushSegmentType> listIopushSegmentType = (List<IopushSegmentType>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listIopushSegmentType);
		response.setStatus(true);
		return response;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Response listSegment(String segmentType_id, int pid) {
		Response response = new Response();
		
		
		
		int[] segemntTypeid = Arrays.asList(segmentType_id.split(","))
                .stream()
                .map(String::trim)
                .mapToInt(Integer::parseInt).toArray();
		List<Integer> segmenttypeid = new ArrayList<Integer>();
		for(int i=0;i<segemntTypeid.length;i++)
		{
			segmenttypeid.add(segemntTypeid[i]);
		}
		
		List<Integer> pids = new ArrayList<>();
		  pids.add(0);
		  pids.add(pid);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.createAlias("segmentType", "segmentType");
		criteria.add(Restrictions.in("product.productID", pids));
		criteria.add(Restrictions.in("segmentType.segmentTypeId", segmenttypeid));
		List<IopushSegmentation> listIopushSegment= (List<IopushSegmentation>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listIopushSegment);
		response.setStatus(true);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response getSegmentData(int segmentId, int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("segmentId", segmentId));
		criteria.add(Restrictions.eq("product.productID", productId));
		List<IopushSegmentation> listWelcome = (List<IopushSegmentation>) this.hibernateTemplate.findByCriteria(criteria);
		if(!listWelcome.isEmpty())
		{
			response.setStatus(true);
			response.setScalarResult(listWelcome.get(0));
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response findSegment(String segmentname,int segment_id,int product_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("segmentName", segmentname).ignoreCase());
		if(segment_id != 0)
		{
		criteria.add(Restrictions.ne("segmentId", segment_id));
		}
		criteria.add(Restrictions.eq("product.productID", product_id));
		List<IopushSegmentation> listSegment = (List<IopushSegmentation>) this.hibernateTemplate.findByCriteria(criteria);
		if(listSegment.size()>0)
			response.setStatus(true);
		else
			response.setStatus(false); 
		return response;
	}
}
