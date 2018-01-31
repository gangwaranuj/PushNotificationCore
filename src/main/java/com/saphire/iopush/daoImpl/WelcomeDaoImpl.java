package com.saphire.iopush.daoImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.bean.WelcomeStatsBean;
import com.saphire.iopush.dao.IWelcomeDao;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.model.IopushWelcomeStats;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.Utility;


@Repository
@Transactional(readOnly=false)
public class WelcomeDaoImpl implements IWelcomeDao{

	@Autowired HibernateTemplate hibernateTemplate;

	@Override
	public Response save(IopushWelcome iopushWelcome) {
		Response response = new Response();

		int schId = (int)hibernateTemplate.save(iopushWelcome);
		response.setStatus(true);
		response.setIntegerResult(schId);
		return response;

	}
	@Override
	public Response update(IopushWelcome iopushWelcome) {
		Response response = new Response();
		hibernateTemplate.saveOrUpdate(iopushWelcome);
		response.setStatus(true);
		return response;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getWelcomeEntity(Integer productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.add(Restrictions.eq("productId", productId));
		List<IopushWelcome> listIopushWelcome = (List<IopushWelcome>)this.hibernateTemplate.findByCriteria(criteria);
		if(!listIopushWelcome.isEmpty())
		{
			response.setStatus(true);
			response.setScalarResult(listIopushWelcome.get(0));
		}
		else
		{
			response.setStatus(false);
		}

		return response;
	}


	@Override
	public Response updateWelcomeEntity(IopushWelcome iopushWelcome) {
		Response response = new Response();
		hibernateTemplate.update(iopushWelcome);
		response.setStatus(true);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listCountries(List<String> listCountries) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushTimeZone.class);
		criteria.add(Restrictions.in("countryId", listCountries));
		List<IopushTimeZone> listTimeZone = (List<IopushTimeZone>) hibernateTemplate.findByCriteria(criteria);
		if(!listTimeZone.isEmpty())
		{
			response.setStatus(true);
			response.setData(listTimeZone);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response deleteWelcome(int welcomeId, int productId) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.WELCOME_STATS_DELETE_QUERY, welcomeId));
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.WELCOME_DELETE_QUERY, welcomeId, productId));
		return res;
	}


	@Override
	public Response welcomeStatusChange(int welcomeId, int status, int productId) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.WELCOME_STATUS_CHANGE_QUERY, status, welcomeId, productId));
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly=false)
	public int countWelcome(Integer[] status,String welcome_name,int startIndex,int pageSize, boolean flag,boolean analytics,String welcome_date1,String welcome_date2,int pid){
		int count = 0;
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
			criteria.createAlias("iopushProduct", "product");
			if(!welcome_name.isEmpty())
			{
				criteria.add(Restrictions.eq("welcomeName", welcome_name));
			}
			if(welcome_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date rssDate1 = sdf.parse(welcome_date1);
				Date rssDate2 = sdf.parse(welcome_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rssDate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);

				criteria.add(Restrictions.between("date", rssDate1, calendar.getTime()));
			}
			criteria.add(Restrictions.eq("product.productID", pid));
			criteria.add(Restrictions.in("welcomeStatus", (Integer[])status));
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
	public  List<IopushWelcome> listWelcome(Integer[] status,String welcome_name,int startIndex,int pageSize, boolean flag,boolean analytics,String welcome_date1,String welcome_date2,int pid
			,String columnForOrdering, String requiredOrder){
		List<IopushWelcome> listWelcome = new ArrayList<>();
		try
		{
			
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
			criteria.createAlias("iopushProduct", "product");
			if(!welcome_name.isEmpty())
			{
				criteria.add(Restrictions.eq("welcomeName", welcome_name));
			}
			if(welcome_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date rssDate1 = sdf.parse(welcome_date1);
				Date rssDate2 = sdf.parse(welcome_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rssDate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);

				criteria.add(Restrictions.between("date", rssDate1, calendar.getTime()));
			}
			if(requiredOrder.equalsIgnoreCase("DESC"))
			{
				criteria.addOrder(Order.desc(columnForOrdering));
			}
			else
			{
				criteria.addOrder(Order.asc(columnForOrdering));
			}
			criteria.add(Restrictions.eq("product.productID", pid));
			criteria.add(Restrictions.in("welcomeStatus", (Integer[])status));
			listWelcome = (List<IopushWelcome>) hibernateTemplate.findByCriteria(criteria,startIndex,pageSize);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		return listWelcome;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getWelcomeData(int welcomeId, int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("welcomeId", welcomeId));
		criteria.add(Restrictions.eq("product.productID", productId));
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
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
	public Response findWelcome(String welcomename,int welcomeId,int product_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("welcomeName", welcomename).ignoreCase());
		if(welcomeId != 0)
		{
			criteria.add(Restrictions.ne("welcomeId", welcomeId));
		}
		criteria.add(Restrictions.eq("product.productID", product_id));
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
		if(listWelcome.size()>0)
			response.setStatus(true);
		else
			response.setStatus(false); 
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listEligibleForExpireWelcome() {
		Response response = new Response();
		List<Integer> status = new ArrayList<>();
		status.add(Constants.PENDING);
		status.add(Constants.LIVE);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.add(Restrictions.in("welcomeStatus", status));
		criteria.add(Restrictions.eq("unlimited",false)) ;
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
		if(listWelcome.size() > 0)
		{
			response.setData(listWelcome);
			response.setStatus(true);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response listEligibleForLaunchWelcome() {
		Response response = new Response();
		List<Integer> status = new ArrayList<>();
		status.add(Constants.PENDING);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.add(Restrictions.in("welcomeStatus", status));
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
		if(listWelcome.size() > 0)
		{
			response.setData(listWelcome);
			response.setStatus(true);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response listEligibleForCacheWelcome() {
		Response response = new Response();
		List<Integer> status = new ArrayList<>();
		status.add(Constants.LIVE);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.add(Restrictions.in("welcomeStatus", status));
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
		if(listWelcome.size() > 0)
		{
			response.setData(listWelcome);
			response.setStatus(true);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response welcomeStatusAndFlag(int welcomeId, int status, boolean active, int productId) {
		Response res = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcome.class);
		criteria.add(Restrictions.eq("welcomeId", welcomeId)); 
		List<IopushWelcome> listWelcome = (List<IopushWelcome>) this.hibernateTemplate.findByCriteria(criteria);
		if(!listWelcome.isEmpty())
		{
			IopushWelcome iopushWelcome = listWelcome.get(0);
			iopushWelcome.setActive(active);
			iopushWelcome.setWelcomeStatus(status);
			hibernateTemplate.update(iopushWelcome);
 			res.setIntegerResult(1);
 			res.setScalarResult(iopushWelcome);
		}
		return res;
	}

	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> listWelcomeStats(int startIndex, int pagesize, boolean flag,boolean analytics, String welcomeName, String welcome_startdate, String welcome_enddate,int pid, String columnForOrdering, String requiredOrder) throws ParseException {

		
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.welcomeAnalyticsQuery(columnForOrdering, requiredOrder,welcomeName,startIndex,pagesize,flag,analytics,welcome_startdate,welcome_enddate,pid);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pagesize).getResultList();

		}finally {
			session.close();
		}
		return results;
		
		//		List<WelcomeStatsBean> listWelcome = new ArrayList<>();
//		try
//		{
//			DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcomeStats.class);
//			
//			if(!welcomeName.isEmpty())
//			{
//				criteria.add(Restrictions.eq("welcomeName", welcomeName));
//			}
//			if(welcome_date1!=null)
//			{
//				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
//				Date welcomeDate1 = sdf.parse(welcome_date1);
//				Date welcomeDate2 = sdf.parse(welcome_date2);
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(welcomeDate2);
//				calendar.set(Calendar.HOUR, 23);
//				calendar.set(Calendar.MINUTE,59);
//				calendar.set(Calendar.SECOND, 59);
//
//				criteria.add(Restrictions.between("date", welcomeDate1, calendar.getTime()));
//			}
//			
//			criteria.add(Restrictions.eq("productid", pid)) ;
//			criteria.createAlias("iopushWelcome", "iopushwelcome");
//			ProjectionList projList = Projections.projectionList();
//			projList.add(Projections.property("iopushwelcome.welcomeStatus")) ;
//			projList.add(Projections.property("iopushwelcome.welcomeId")) ;
//			projList.add(Projections.property("welcomeName")) ;
//			projList.add(Projections.property("date")) ;
//			projList.add(Projections.property("sent")) ;
//			projList.add(Projections.property("click")) ;
//			projList.add(Projections.property("open")) ;
//			projList.add(Projections.property("welcomeStatsId")) ;
//			
//			criteria.setProjection(projList);
//			
//			if(requiredOrder.equalsIgnoreCase("desc"))
//			{
//				criteria.addOrder(Order.desc(columnForOrdering));
//			}
//			else
//			{
//				criteria.addOrder(Order.asc(columnForOrdering));
//			}
//			listWelcome = (List<WelcomeStatsBean>) hibernateTemplate.findByCriteria(criteria,startIndex,pagesize);
//			
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace(); 
//		}
//		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public int countWelcome(int startIndex, int pagesize, boolean analytics, String welcomeName, String welcome_date1, String welcome_date2,int pid) {
		int count = 0;
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushWelcomeStats.class);
			
			if(!welcomeName.isEmpty())
			{
				criteria.add(Restrictions.eq("welcomeName", welcomeName));
			}
			if(welcome_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date welcomeDate1 = sdf.parse(welcome_date1);
				Date welcomeDate2 = sdf.parse(welcome_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(welcomeDate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);

				criteria.add(Restrictions.between("date", welcomeDate1, calendar.getTime()));
			}
			criteria.add(Restrictions.eq("productid", pid)) ;
			ProjectionList projection = Projections.projectionList();
			projection.add(Projections.rowCount());
			criteria.setProjection(projection);
			
			List<Long> list = (List<Long>) hibernateTemplate.findByCriteria(criteria);
			count = list.get(0).intValue();

		}
		catch(Exception e)
		{

		}

		return count;

	}

	
	@SuppressWarnings("unchecked")
	 @Override
	 public Response fetchIsps(Integer[] country) {
	  Response response = new Response();
	  DetachedCriteria criteria = DetachedCriteria.forClass(IopushIsp.class);
	  criteria.add(Restrictions.in("countryId", country)); 
	  List<IopushIsp> listWelcome = (List<IopushIsp>) hibernateTemplate.findByCriteria(criteria);
	  if(!listWelcome.isEmpty())
	  {
	   response.setStatus(true);
	   response.setData(listWelcome);
	  }
	  else
	  {
	   response.setStatus(false);
	  }
	  return response;
	 }
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> listManageWelcome(Integer[] status,String welcomeName,int startIndex,int pageSize, boolean flag,
			int pid, String columnForOrdering, String requiredOrder){
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.manageWelcomeQuery(status, welcomeName, flag,pid, columnForOrdering,requiredOrder);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
		}finally {
			session.close();
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> listManageWelcomePlatform(Integer[] status,String welcomeName,int startIndex,int pageSize, boolean flag,
			int pid){
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.manageWelcomeQueryPlatform(status, welcomeName, flag,pid);
			results = session.createQuery(hql).getResultList();
		}finally {
			session.close();
		}
		return results;
	}
}
