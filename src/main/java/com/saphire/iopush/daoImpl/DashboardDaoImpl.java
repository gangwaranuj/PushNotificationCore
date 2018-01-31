package com.saphire.iopush.daoImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.saphire.iopush.dao.IDashboardDao;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushPlan;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.model.IopushUserplan;
import com.saphire.iopush.model.IopushViewsClicks;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.Utility;

@Repository
public class DashboardDaoImpl implements IDashboardDao {

	@Autowired HibernateTemplate hibernateTemplate;
	private Logger logger = LoggerFactory.getLogger(DashboardDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response platformDetails(int productID) {
		Response response=new Response();
		try
		{
			DetachedCriteria criteria=DetachedCriteria.forClass(IopushSubscribers.class);

			criteria.createAlias("iopushPlatformDetails", "Platforms");
			criteria.createAlias("iopushProduct", "Product");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("Platforms.platformName"));
			projectionList.add(Projections.sqlProjection("Cast(Count(subscribers_id) as Integer) count",
					new String[]{"count"},
					new Type[]{StandardBasicTypes.INTEGER}));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("Product.productID", productID));
			
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_platform_criteria");
			List<Object[]> listBrowser = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			response.setData(listBrowser);
		}
		catch (Exception e)
		{
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findGeo(int productID) {
		Response response = new Response();
		try
		{

			DetachedCriteria criteria = DetachedCriteria.forClass(IopushSubscribers.class);
			criteria.createAlias("iopushGeoDetails", "geoDetails") ;
			criteria.createAlias("iopushPlatformDetails", "platformDetails") ;
			criteria.createAlias("iopushProduct", "productDetails") ;


			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("geoDetails.geoName")) ;
			projectionList.add(Projections.groupProperty("geoDetails.geoCode")) ;
			projectionList.add(Projections.groupProperty("geoDetails.geoLatitude")) ;
			projectionList.add(Projections.groupProperty("geoDetails.geoLongititude")) ;
			projectionList.add(Projections.groupProperty("platformDetails.platformName"));

			projectionList.add(Projections.groupProperty("platformDetails.platformID"));
			projectionList.add(Projections.groupProperty("geoDetails.geoId")) ;


			projectionList.add(Projections.rowCount());

			criteria.setProjection(projectionList).add(Restrictions.eq("iopushProduct.productID",  productID));
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_geo_criteria");
			List<Object[]> listGeo = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);


			response.setData(listGeo);
			if(logger.isDebugEnabled()){
				for (Object[] objects : listGeo) {
					this.logger.debug("geo_name : " + String.valueOf(objects[0].toString()));
					this.logger.debug("geo_code : " + String.valueOf(objects[1].toString()));
					this.logger.debug("geo_latitude : " + String.valueOf(objects[2].toString()));
					this.logger.debug("geo_longititude :" + String.valueOf(objects[3].toString()));
					this.logger.debug("platformName : " + String.valueOf(objects[4].toString()));
					this.logger.debug("Count : " + String.valueOf(objects[8].toString()));
				}
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response findPlatformDetails()
	{
		Response response = new Response();
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushPlatformDetails.class);

			this.hibernateTemplate.setCacheQueries(true);
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.groupProperty("platformName"));

			criteria.setProjection(projectionList);
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_find_platform_criteria");
			List<Object[]> listBrowser = (List<Object[]>) this.hibernateTemplate.findByCriteria(criteria);
			response.setData(listBrowser);
			if(logger.isDebugEnabled()){
				for (Object[] objects : listBrowser) {
					this.logger.debug("platformName : " + String.valueOf(objects[1].toString()));
					this.logger.debug("Count : " + String.valueOf(objects[0].toString()));
				}
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getViews(Date start_date, Date end_date, int productID) {
		Response response = new Response();
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushViewsClicks.class);
			criteria.createAlias("productID", "product_id");

			ProjectionList projectionList = Projections.projectionList();


			projectionList.add(Projections.sqlGroupProjection( "date(date) as viewdate", 
					"viewdate", 
					new String[]{"viewdate"}, 
					new Type[] {TimestampType.INSTANCE}));
			projectionList.add(Projections.sum("viewHits"));
			criteria.setProjection(projectionList);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(end_date);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);
			
			criteria.add(Restrictions.eq("product_id.productID", productID)); 
			criteria.add(Restrictions.between("date", start_date, calendar.getTime()));

			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_views_datewise_criteria");
			List<Object[]> listSent = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			if(listSent.size()>0)
			{
				response.setStatus(true);
				response.setData(listSent);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getHourlyViews(Date d1, Date d2, int productID) {
		Response response = new Response();
		try
		{

			DetachedCriteria icmp_hourlyviews_criteria = DetachedCriteria.forClass(IopushViewsClicks.class) ;

			icmp_hourlyviews_criteria.createAlias("productID", "product_id");

			ProjectionList projectionList = Projections.projectionList() ;
			projectionList.add(Projections.sqlGroupProjection(
					"extract(hour from this_.date) as monthly", 
					"monthly", 
					new String[]{"monthly"}, 
					new Type[] {IntegerType.INSTANCE}));
			//projectionList.add(Projections.rowCount()) ;
			projectionList.add(Projections.sum("viewHits"));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d2);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);

			icmp_hourlyviews_criteria.setProjection(projectionList).add(Restrictions.between("date", d1, calendar.getTime())).add(Restrictions.eq("product_id.productID",  productID))  ;
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_views_hourly_criteria");
			List<Object[]> listTotalViews = (List<Object[]>) hibernateTemplate.findByCriteria(icmp_hourlyviews_criteria) ;

			response.setData(listTotalViews);

		}
		catch (Exception e)
		{
			logger.error("Exception : ",e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response getClicks(Date start_date, Date end_date, int productID) {
		Response response = new Response();
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushViewsClicks.class);
			criteria.createAlias("productID", "product_id");

			ProjectionList projectionList = Projections.projectionList();

			projectionList.add(Projections.sqlGroupProjection( "date(date) as clickDate", 
					"clickDate", 
					new String[]{"clickDate"}, 
					new Type[] {TimestampType.INSTANCE}));
			projectionList.add(Projections.sum("clickHits"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(end_date);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("product_id.productID", productID)); 
			criteria.add(Restrictions.between("date", start_date, calendar.getTime()));
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("icmp_topsites_criteria");
			List<Object[]> listClicks = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			if(listClicks.size()>0)
			{
				response.setStatus(true);
				response.setData(listClicks);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}




	@SuppressWarnings("unchecked")
	@Override
	public Response getHourlyClicks(Date d1, Date d2, int productID) {
		Response response = new Response();
		try
		{

			DetachedCriteria icmp_hourlyclickss_criteria = DetachedCriteria.forClass(IopushViewsClicks.class);
			icmp_hourlyclickss_criteria.createAlias("productID", "product_id");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sqlGroupProjection(
					"extract(hour from date) as monthly", 
					"monthly", 
					new String[]{"monthly"}, 
					new Type[] {IntegerType.INSTANCE}));
			projectionList.add(Projections.sum("clickHits"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d2);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);

			icmp_hourlyclickss_criteria.setProjection(projectionList).add(Restrictions.between("date", d1, calendar.getTime())).add(Restrictions.eq("product_id.productID",  productID)) ;
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_hourly_clicks_criteria");
			List<Object[]> listTotalClicks  = (List<Object[]>) this.hibernateTemplate.findByCriteria(icmp_hourlyclickss_criteria);

			response.setData(listTotalClicks);
		}
		catch (Exception e)
		{
			logger.error("Exception : ",e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response findIspDetails(int productID) {

		Response response = new Response();
		try
		{

			DetachedCriteria criteria = DetachedCriteria.forClass(IopushSubscribers.class);
			criteria.createAlias("iopushIsp", "isp") ;
			criteria.createAlias("iopushPlatformDetails", "platformDetails") ;
			criteria.createAlias("iopushProduct", "productDetails") ;


			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("isp.ispName")) ;
			projectionList.add(Projections.groupProperty("platformDetails.platformName"));

			projectionList.add(Projections.groupProperty("platformDetails.platformID"));
			projectionList.add(Projections.groupProperty("isp.ispId")) ;


			projectionList.add(Projections.rowCount());

			criteria.setProjection(projectionList).add(Restrictions.eq("iopushProduct.productID",  productID));
			hibernateTemplate.setCacheQueries(true);
			List<Object[]> listGeo = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);


			response.setData(listGeo);
			if(logger.isDebugEnabled()){
				for (Object[] objects : listGeo) {
					this.logger.debug("isp_name : " + String.valueOf(objects[0].toString()));
					this.logger.debug("platformName : " + String.valueOf(objects[1].toString()));
					this.logger.debug("Count : " + String.valueOf(objects[4].toString()));
				}
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response getDateWiseTrendDetails(Date d1, Date d2, int productID) {
		Response response= new Response();
		try{
			DetachedCriteria criteria= DetachedCriteria.forClass(IopushSubscribers.class);
			criteria.createAlias("iopushPlatformDetails","Platform");
			criteria.createAlias("iopushProduct", "product");

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sqlGroupProjection( "date(installation_date) as daily", 
				    "daily", 
				    new String[]{"daily"}, 
				    new Type[] {TimestampType.INSTANCE}));
			projectionList.add(Projections.groupProperty("Platform.platformName"));
			projectionList.add(Projections.sqlProjection("Cast(Count(subscribers_id) as Integer) count",
					new String[]{"count"},
					new Type[]{StandardBasicTypes.INTEGER}));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("product.productID", productID));
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d2);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);
			
			criteria.add(Restrictions.between("installationDate", d1, calendar.getTime()));
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_datewise_trend_criteria");
			List<Object[]> listTrends = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			if(listTrends.size()>0)
			{
				response.setStatus(true);
				response.setData(listTrends);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}


		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response getHourWiseTrendDetails(Date d1, Date d2, int productID) {
		Response response= new Response();
		try{
			DetachedCriteria criteria= DetachedCriteria.forClass(IopushSubscribers.class);
			criteria.createAlias("iopushPlatformDetails","Platform");
			criteria.createAlias("iopushProduct", "product");

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sqlGroupProjection(
					"extract(hour from installation_date) as daily", 
					"daily", 
					new String[]{"daily"}, 
					new Type[] {IntegerType.INSTANCE}));
			
			projectionList.add(Projections.groupProperty("Platform.platformName"));
			projectionList.add(Projections.sqlProjection("Cast(Count(subscribers_id) as Integer) count",
					new String[]{"count"},
					new Type[]{StandardBasicTypes.INTEGER}));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("product.productID", productID));
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d2);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);
			criteria.add(Restrictions.between("installationDate", d1, calendar.getTime()));
			
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_hourwise_trends_criteria");
			List<Object[]> listTrends = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			if(listTrends.size()>0)
			{
				response.setStatus(true);
				response.setData(listTrends);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}


		return response;
	}


	
	
	 
	@SuppressWarnings("unchecked")
	@Override
 public Response PlanDetails(String  user, int prodId) {
	  
	  Response response = new Response();
	  try
	  {
	    DetachedCriteria criteria = DetachedCriteria.forClass(IopushUsercategory.class).add(Restrictions.eq("product_id", prodId));
	   
	  
	    List<IopushUsercategory> iopushUsercategorylist = (List<IopushUsercategory>) hibernateTemplate.findByCriteria(criteria);
	    Object[] o = new Object[2];
	    if(iopushUsercategorylist.size() > 0)
	    {
	     IopushUsercategory iopushUsercategory=iopushUsercategorylist.get(0);
	     int planid=iopushUsercategory.getPlanId();
	     o[0] = iopushUsercategory ;
	     
	     IopushPlan userPlan = hibernateTemplate.get(IopushPlan.class,planid);
	     o[1] = userPlan ;
	     response.setScalarResult(o);
	     response.setStatus(true);
	    }
	    else{
	     response.setStatus(false);
	    }
	  }
	  catch (Exception e)
	  {
	   this.logger.error("Exception : " , e);
	   response.setErrorMessage(e.getMessage());
	   response.setException(e);
	   response.setStatus(false);
	  }
	  return response;
	 }

	@SuppressWarnings("unchecked")
	@Override
	public Response lastNotificationDetails(int productID) {
		Response response = new Response();
		try
		{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Object[]> listNotification = session.createQuery(Utility.QueryForLastNotification(productID)).setFirstResult(0).setMaxResults(1).getResultList();
			/*DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
			
			criteria.createAlias("iopushProduct", "Product");
			
			Integer[] status = new Integer[2];
			status[0] = 1;
			status[1] = 3;
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("creationDate"));
			projectionList.add(Projections.property("forwardUrl"));
			projectionList.add(Projections.property("campaignopen"));
			projectionList.add(Projections.property("campaignclick"));
			projectionList.add(Projections.property("campaignsent"));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("Product.productID", productID));
			criteria.add(Restrictions.in("campaignStatus", status));
			criteria.addOrder(Order.desc("campaign_id"));*/
			/*hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_lastnotification_criteria");*/
//			List<Object[]> listNotification = (List<Object[]>) hibernateTemplate.findByCriteria(criteria, 0, 1);
			if(listNotification.size() > 0)
			{
				response.setStatus(true);
				response.setData(listNotification);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response previewLastNotificationDetails(int productID) {
		
		Response response = new Response();
		try
		{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<Object[]> listpreview = session.createQuery(Utility.QueryForPreviewLastNotification(productID)).setFirstResult(0).setMaxResults(1).getResultList();
			/*Integer[] status = new Integer[2];
			status[0] = 1;
			status[1] = 3;
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
			criteria.createAlias("iopushProduct", "product");
			criteria.add(Restrictions.eq("product.productID", productID));
			criteria.add(Restrictions.in("campaignStatus", status));
			criteria.addOrder(Order.desc("campaign_id"));
			
			List<IopushCampaign> listpreview = (List<IopushCampaign>) hibernateTemplate.findByCriteria(criteria, 0, 1);
			*/if(listpreview.size() > 0)
			{
				response.setStatus(true);
				response.setData(listpreview);
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findBrowsers() {
		Response response = new Response();
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushPlatformDetails.class);
			hibernateTemplate.setCacheQueries(true);
			hibernateTemplate.setQueryCacheRegion("iopush_browsers_criteria");
			List<IopushPlatformDetails> listBrowser = (List<IopushPlatformDetails>) this.hibernateTemplate.findByCriteria(criteria);
			response.setData(listBrowser);
			
		}
		catch (Exception e)
		{
			this.logger.error("Exception : " , e);
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findGeoCode(String country_code) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushGeoDetails.class);
		criteria.add(Restrictions.eq("geoCode", country_code));
		hibernateTemplate.setCacheQueries(true);
		hibernateTemplate.setQueryCacheRegion("iopush_geocode_criteria");
		List<IopushGeoDetails> listCountries = (List<IopushGeoDetails>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCountries.size()>0)
		{
			response.setStatus(true);
			response.setScalarResult(listCountries.get(0));
		}else{
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findCityNew(int country_id, int product) {
		Response response = new Response();
		   try
		   {
				
				DetachedCriteria icmp_newcity_criteria = DetachedCriteria.forClass(IopushSubscribers.class);
				icmp_newcity_criteria.createAlias("iopushCityDetails", "cityDetails") ;
				icmp_newcity_criteria.createAlias("iopushPlatformDetails", "browserDetails") ;
				icmp_newcity_criteria.createAlias("iopushGeoDetails", "geoDetails") ;
				icmp_newcity_criteria.createAlias("iopushProduct", "productDetails") ;
				
				
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.groupProperty("cityDetails.cityLongititude")) ;
				projectionList.add(Projections.groupProperty("cityDetails.cityLatitude")) ;
				projectionList.add(Projections.groupProperty("cityDetails.cityName")) ;
				projectionList.add(Projections.groupProperty("browserDetails.platformName"));
				//projectionList.add(Projections.groupProperty("cityDetails.geoCode")) ;

				projectionList.add(Projections.groupProperty("cityDetails.cityId")) ;
				projectionList.add(Projections.groupProperty("browserDetails.platformID"));
				
				
				projectionList.add(Projections.rowCount());
				
				icmp_newcity_criteria.setProjection(projectionList).add(Restrictions.eq("geoDetails.geoId", country_id)).add(Restrictions.eq("productDetails.productID",  product)) ;
				
				/*hibernateTemplate.setCacheQueries(true);
				hibernateTemplate.setQueryCacheRegion("icmp_newcity_criteria");
				*/
				hibernateTemplate.setCacheQueries(true);
				hibernateTemplate.setQueryCacheRegion("iopush_citynew_criteria");
				 List<Object[]> listcity = (List<Object[]>) hibernateTemplate.findByCriteria(icmp_newcity_criteria);

		     response.setData(listcity);

		   }
		   catch (Exception e)
		   {
		     this.logger.error("Exception : " , e);
		     response.setErrorMessage(e.getMessage());
		     response.setException(e);
		     response.setStatus(false);
		   }
		   return response;
		 }



	@SuppressWarnings("unchecked")
	@Override
	public Response fetchRules(int campaignId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRules.class);
		criteria.createAlias("iopushCampaign", "campaign");
		criteria.add(Restrictions.eq("campaign.campaignId", campaignId));
		List<IopushRules> listRules = (List<IopushRules>) hibernateTemplate.findByCriteria(criteria);
		if(!listRules.isEmpty())
		{
			response.setStatus(true);
			response.setData(listRules);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response listplatform() {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPlatformDetails.class);
		List<IopushPlatformDetails> listPlatform = (List<IopushPlatformDetails>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listPlatform);
		response.setStatus(true);
		return response;
	}
	}




