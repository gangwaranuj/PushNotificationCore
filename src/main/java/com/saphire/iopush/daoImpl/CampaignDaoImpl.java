package com.saphire.iopush.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.bean.PlatformStatBean;
import com.saphire.iopush.bean.StatsModelBean;
import com.saphire.iopush.dao.ICampaignDao;
import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.model.IopushCampaignStats;
import com.saphire.iopush.model.IopushCityDetails;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushSubCampaign;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.serviceImpl.CampaignServiceImpl;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.Utility;

@Transactional()
@Repository
public class CampaignDaoImpl implements ICampaignDao{

	@Autowired HibernateTemplate hibernateTemplate;

	private Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Response fetch(IopushTimeZone iopushTimeZone, String[] excludeProperty) {
		Response response= new Response();
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(IopushTimeZone.class);
		detachedCriteria.setProjection(Projections.projectionList().add(Projections.property("timezoneID"), "timezone_id").add(Projections.property("timezone"), "time_zone").add(Projections.property("country"), "country"));

		List<IopushTimeZone> output=(List<IopushTimeZone>)hibernateTemplate.findByCriteria(detachedCriteria);
		if(output.size()>0)
		{
			response.setData(output);
			response.setStatus(true);
		}

		return response;
	}


	@Override
	public Response saveCampaign(IopushCampaign campaign) {

		Response response = new Response();
		int i=0;
		SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		Session session =sessionFactory.openSession() ;
		try{
			session.beginTransaction() ;
			i=(Integer)session.save(campaign) ;
			session.getTransaction().commit();
		}finally{
			session.close();
		}
		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;
	}

	@Override
	public Response saveSubCampaign(IopushSubCampaign campaign) {

		Response response = new Response() ;
		this.hibernateTemplate.setCheckWriteOperations(false);

		this.hibernateTemplate.save(campaign);

		hibernateTemplate.flush();
		return response ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listCampaigns_expire() {
		  Response response = new Response();
		  List<Integer> status = new ArrayList<>();
		  status.add(Constants.EXPIRE);
		  status.add(Constants.LIVE);
		  DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
		  criteria.add(Restrictions.in("campaignStatus", status));
		  List<IopushCampaign> listCampaign = (List<IopushCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		  if(listCampaign.size() > 0)
		  {
		   response.setData(listCampaign);
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
	public Response findActiveSubscribers(int campaign_id) {
		  Response response = new Response();
		  		  DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaignStats.class);
		  criteria.add(Restrictions.eq("iopushCampaign", campaign_id));
		  List<IopushCampaignStats> listActiveSubscribers = (List<IopushCampaignStats>) this.hibernateTemplate.findByCriteria(criteria);
		  if(listActiveSubscribers.size() > 0)
		  {
		   response.setData(listActiveSubscribers);
		   response.setStatus(true);
		  }
		  else
		  {
		   response.setStatus(false);
		  }
		  return response;
		 }
	
	
	
	
	@Override
	public Response updateCampaign(IopushCampaign iopushCampaign) {

		Response response = new Response();
		int i=0;
		SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		Session session =sessionFactory.openSession() ;
		try{
			session.beginTransaction() ;
			session.merge(iopushCampaign) ;
			session.getTransaction().commit();
		}finally{
			session.close();
		}
		if(i>0){
			response.setStatus(true);
		}
		else
			response.setStatus(false);
		return response;
	}



	/*@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> listCampaignsNew(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean flag,boolean analytics,
			String campaign_date1,String campaign_date2,int pid){
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.listCampaignQuery(status, campaign_name, startIndex, pageSize, flag, analytics, campaign_date1, campaign_date2,pid);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			results = query.list();
		}finally {
			session.close();
		}
		return results;
	}*/
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly=false)
	public int countCampaignsNew(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean flag,boolean analytics,String campaign_date1,String campaign_date2,int pid){
		Session session =null;
		int count=0;
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.countCampaignQuery(status, campaign_name, startIndex, pageSize, flag, analytics, campaign_date1, campaign_date2,pid);
			Query query = session.createQuery(hql);
			count = ((Long)query.uniqueResult()).intValue();
		}finally {
			session.close();
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response findCampaignByID(int camapaign_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
		criteria.add(Restrictions.eq("campaignId", camapaign_id));
		List<IopushCampaign> listCampaign = (List<IopushCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCampaign.size()>0)
			response.setScalarResult(listCampaign.get(0));
		response.setStatus(true);
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findCountryTimeZone(int c_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushTimeZone.class);
		criteria.add(Restrictions.eq("countryId", c_id));
		List<IopushTimeZone> listIcmpTimeZone = (List<IopushTimeZone>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIcmpTimeZone.size()>0){
			response.setStatus(true);
			response.setScalarResult(listIcmpTimeZone.get(0));
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response findCountryCode(int c_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushGeoDetails.class);
		criteria.add(Restrictions.eq("geoId", c_id));
		List<IopushGeoDetails> listIopushGeoDetails = (List<IopushGeoDetails>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIopushGeoDetails.size()>0){
			response.setStatus(true);
			response.setScalarResult(listIopushGeoDetails.get(0));
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response deleteCampaignRule(int camapaign_id) {

		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.CAMPAIGN_RULE_DELETE_QUERY, camapaign_id));
		return res;

	}

	@Override
	public Response deleteSubCampaign(int camapaign_id) {

		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.SUB_CAMPAIGN_DELETE_QUERY, camapaign_id));
		return res;

	}

	@Override
	public Response saveRules(List<IopushRules> listIopushRules) {
		Response response = new Response() ;
		this.hibernateTemplate.setCheckWriteOperations(false);
		for(IopushRules icmpRule : listIopushRules)
		{
			this.hibernateTemplate.save(icmpRule);
		}
		hibernateTemplate.flush();
		return response ;
	}

	@Override
	public Response listCampaignByCriteria(DetachedCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response deleteCampaign(IopushCampaign iopushCampaign) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response listCampaign(int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findSUBSCRIBERSCountByCriteira(String query) {
		Response response = new Response();
		Session session=null;
		try{
			session = this.hibernateTemplate.getSessionFactory().openSession() ;
			response.setScalarResult(session.createNativeQuery(query).getSingleResult()) ;
			response.setStatus(true);
		}catch(Exception ex){
			response.setErrorMessage(ex.getMessage());
			response.setException(ex);
			response.setStatus(false);
		}finally{
			session.close();
		}


		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response findCampaign(String campignname,int campaign_id,int product_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("campaignName", campignname).ignoreCase());
		if(campaign_id != 0)
		{
		criteria.add(Restrictions.ne("campaignId", campaign_id));
		}
		criteria.add(Restrictions.eq("product.productID", product_id));
		/* criteria.createAlias("product", "p").add(Restrictions.eqOrIsNull("p.productId", product_id));*/
		List<IopushCampaign> listCampaign = (List<IopushCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCampaign.size()>0)
			response.setStatus(true);
		else
			response.setStatus(false); 
		return response;
	}



	@Override
	public Response changeCampaignStatus(int campaignID, int status) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.CAMPAIGN_EXPIRE_QUERY.replaceAll("<STATUS>", ""+status), campaignID));
		return res;
	}


	@Override
	public Response deleteNotification(int campignid) {
		Response res = new Response();
		//		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.NOTIFICATION_DELETE_QUERY, campignid));
		return res;
	}


	@Override
	public Response deleteViewsClick(int campignid) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.VIEWSCLICKS_RULE_DELETE_QUERY, campignid));
		return res;
	}


	@Override
	public Response deleteCampaign(int campaignid) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.CAMPAIGN_DELETE_QUERY, campaignid));
		return res;
	}


	/*@SuppressWarnings("unchecked")
	@Override
	public Response ispList() {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushIsp.class);
		List<IopushIsp> listIopushIsp = (List<IopushIsp>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listIopushIsp);
		response.setStatus(true);
		return response;
	}*/

	@SuppressWarnings("unchecked")
	 @Override
	 public Response ispList(Object countryList[]) {
	  Response response = new Response();
	  DetachedCriteria criteria = DetachedCriteria.forClass(IopushIsp.class);
	  if(countryList[0] != (Object)0)
	   criteria.add(Restrictions.in("countryId", countryList)) ;
	  List<IopushIsp> listIopushIsp = (List<IopushIsp>) this.hibernateTemplate.findByCriteria(criteria);
	  response.setData(listIopushIsp);
	  response.setStatus(true);
	  return response;
	 }

	@SuppressWarnings("unchecked")
	@Override
	public Response listTimezone() {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushTimeZone.class);
		List<IopushTimeZone> listTimezone = (List<IopushTimeZone>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listTimezone);
		response.setStatus(true);
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response listCities(int country_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCityDetails.class);
		criteria.createAlias("iopushGeoDetails", "country");

		criteria.add(Restrictions.eq("country.geoId", country_id));
		List<IopushCityDetails> listCities = (List<IopushCityDetails>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCities.size()>0)
		{
			response.setStatus(true);
			response.setData(listCities);
		}else{
			response.setStatus(false);
		}
		return response;
	}
	@SuppressWarnings("unchecked")
	 @Override
	 public Response listCities() {
	  Response response = new Response();
	   DetachedCriteria criteria = DetachedCriteria.forClass(IopushCityDetails.class);
	            List<IopushCityDetails> listCities = (List<IopushCityDetails>) this.hibernateTemplate.findByCriteria(criteria);
	   response.setData(listCities);
	   response.setStatus(true);
	  return response;
	 }

	@SuppressWarnings("unchecked")
	@Override
	public Response getPlatforms(int campaignId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRules.class);
		criteria.createAlias("iopushCampaign", "campaign");
		criteria.add(Restrictions.eq("campaign.campaignId", campaignId));
		List<IopushRules> listRules = (List<IopushRules>) this.hibernateTemplate.findByCriteria(criteria);
		if(listRules.size()>0)
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


	@Override
	public Response executeQuery(String insertQuery) {
		Response response = new Response();
		Session session = null;
		try
		{
			session = this.hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction();

			SQLQuery sqlCreateCampaign = session.createSQLQuery(insertQuery);
			sqlCreateCampaign.executeUpdate();
			session.getTransaction().commit();
			response.setStatus(true);
		}
		catch (Exception e)
		{
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}finally{
			session.close();
		}
		return response;
	}


	@Override
	public Response deleteActiveSubscribers(int campaign_id) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.ACTIVAT_SUBSCRIBERS_DELETE_QUERY, campaign_id));
		return res;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response findCampaign(int camapaign_id, int product_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
		criteria.createAlias("iopushProduct", "product");

		criteria.add(Restrictions.eq("campaignId", camapaign_id));
		criteria.add(Restrictions.eq("product.productID", product_id));
		List<IopushCampaign> listCampaign = (List<IopushCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCampaign.size()>0)
		{
			response.setScalarResult(listCampaign.get(0));
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
	public Response getSubCampaigns(int campaignid) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSubCampaign.class);
		criteria.createAlias("iopushCampaign", "campaign");
		criteria.add(Restrictions.eq("campaign.campaignId", campaignid));
		List<IopushSubCampaign> listSubCampaign = (List<IopushSubCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listSubCampaign.size()>0)
		{
			response.setStatus(true);
			response.setData(listSubCampaign);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}

	public Response listProduct() {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushProduct.class);
		List<IopushProduct> listProduct = (List<IopushProduct>) this.hibernateTemplate.findByCriteria(criteria);
		response.setData(listProduct);
		response.setStatus(true);

		return response;
	}


	@Override

	public Response changeSubCampaignStatus(int campignid, int status) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.SUB_CAMPAIGN_LAUNCH_QUERY.replaceAll("<STATUS>", ""+status), campignid));
		return res;
	}

	@Override
	public Response changeSubCampaignStatus(int campignid,int geo_id, int status) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.SUB_CAMPAIGN_LAUNCH_QUERY.replaceAll("<STATUS>", ""+status), campignid , geo_id));
		return res;
	}


	@Override
	public Response deleteActiveSubscribers_subcampaign(int campaign_id, int geo_id) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.ACTIVAT_SUBSCRIBERS_SUBCAMPAIGN_DELETE_QUERY, campaign_id,geo_id));
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listSubCampaign(int campaignId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSubCampaign.class);
		criteria.createAlias("iopushCampaign", "campaign");
		criteria.add(Restrictions.eq("campaign.campaignId", campaignId));
		List<IopushSubCampaign> listSubCampaigns = (List<IopushSubCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listSubCampaigns.size() > 0)
		{
			response.setData(listSubCampaigns);
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
	public Response listCampaignAutoLaunch() {
		Response response = new Response();
		List<Integer> status = new ArrayList<>();
		status.add(Constants.PENDING);
		status.add(Constants.LIVE);
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
		criteria.add(Restrictions.in("campaignStatus", status));
		//criteria.add(Restrictions.neProperty("subCampaignsCount", "live"));
		List<IopushCampaign> listCampaign = (List<IopushCampaign>) this.hibernateTemplate.findByCriteria(criteria);
		if(listCampaign.size() > 0)
		{
			response.setData(listCampaign);
			response.setStatus(true);
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}

	public Response statusrssfeed(int id, int active) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.UPDATE_RSSFEEDCONFIG_STATUS_Query, active,id));
		return res;
	}


	@Override
	public Response updateLiveCount(int campignid, int livecampaignsCount) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.UPDATE_CAMPAIGN_LIVE_COUNT_QUERY, livecampaignsCount, campignid));
		return res;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<IopushCampaign> listCampaignAnalytics(int startIndex, int pagesize, boolean analytics, String campaignName, String campaign_date1, String campaign_date2,int pid, String columnForOrdering, String requiredOrder) {
		List<IopushCampaign> listCampaign = new ArrayList<>();
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);

			if(!campaignName.isEmpty())
			{
				criteria.add(Restrictions.eq("campaignName", campaignName));
			}
			if(campaign_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date campaigndate1 = sdf.parse(campaign_date1);
				Date campaigndate2 = sdf.parse(campaign_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(campaigndate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);
				criteria.add(Restrictions.between("campaignScheduleDateInEdt", campaigndate1, calendar.getTime()));
			}
			criteria.add(Restrictions.eq("iopushProduct.productID",pid)) ;
			criteria.add(Restrictions.eq("campaignStatus",Constants.LIVE)) ;
			if(requiredOrder.equalsIgnoreCase("DESC"))
			{
				criteria.addOrder(Order.desc(columnForOrdering));
			}
			else
			{
				criteria.addOrder(Order.asc(columnForOrdering));
			}
			listCampaign = (List<IopushCampaign>) hibernateTemplate.findByCriteria(criteria,startIndex,pagesize);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return listCampaign;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> campaignsAnalyticsList(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean analytics,
			String campaign_date1,String campaign_date2,int pid, String columnForOrdering, String requiredOrder){
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.listAnalyticsCampaignQuery(campaign_name,analytics
					,campaign_date1,campaign_date2,pid, columnForOrdering, requiredOrder);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
		}finally {
			session.close();
		}
		return results;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public int countCampaignAnalytics(int startIndex, int pagesize, boolean analytics, String campaignName, String campaign_date1, String campaign_date2,int pid) {
		int count = 0;
		try
		{
			Integer[] status = {1,3};
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushCampaign.class);
			if(!campaignName.isEmpty())
			{
				criteria.add(Restrictions.eq("campaignName", campaignName));
			}
			if(campaign_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date campaigndate1 = sdf.parse(campaign_date1);
				Date campaigndate2 = sdf.parse(campaign_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(campaigndate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);
				criteria.add(Restrictions.between("campaignScheduleDateInEdt", campaigndate1, calendar.getTime()));
			}
			criteria.add(Restrictions.eq("iopushProduct.productID",pid)) ;
			criteria.add(Restrictions.in("campaignStatus",status));
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
	
	
	@Override
	public Response campaignStatsCountry(int campaign_id) {
		Response response = new Response();
		Session session = null;
		try
		{
			List<StatsModelBean> listStatsModelBean=new ArrayList<StatsModelBean>();
			session = this.hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction();
            logger.info("findCampaignStats::"+Constants.FIND_CAMPAIGN_COUNTRY_STATS);
			SQLQuery sqlFetchCampaignStatsResult = session.createSQLQuery(Constants.FIND_CAMPAIGN_COUNTRY_STATS);
			sqlFetchCampaignStatsResult.setParameter("campaign_id", campaign_id);
			List<Object[]> listCampaignCountryStats = sqlFetchCampaignStatsResult.list();
			for (Object[] objects : listCampaignCountryStats) {
				listStatsModelBean.add(new StatsModelBean(objects));
			}
			if(listStatsModelBean.size()>0){
                response.setStatus(true);				
				response.setData(listStatsModelBean);
			}else{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : ",e);
		}finally {
			session.close();
		}
		return response;
	}
	
	@Override
	public Response campaignCountryPlatformStats(int campaign_id,int geoid,HashMap<Integer,String> platform) {
		Response response = new Response();
		Session session = null;
		try
		{
			List<PlatformStatBean> listPlatformStatBean=new ArrayList<PlatformStatBean>();
			session = this.hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction();
            logger.info("findCampaigncountryplatformStats::"+Constants.FIND_CAMPAIGN_COUNTRY_PLATFORM_STATS);
			SQLQuery sqlFetchCampaignStatsResult = session.createSQLQuery(Constants.FIND_CAMPAIGN_COUNTRY_PLATFORM_STATS);
			sqlFetchCampaignStatsResult.setParameter("campaign_id", campaign_id);
			sqlFetchCampaignStatsResult.setParameter("geoid", geoid);
			List<Object[]> listCampaignCountryStats = sqlFetchCampaignStatsResult.list();
			for (Object[] objects : listCampaignCountryStats) {
				listPlatformStatBean.add(new PlatformStatBean(objects,platform));
			}
			if(listPlatformStatBean.size()>0){
                response.setStatus(true);				
				response.setData(listPlatformStatBean);
			}else{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			this.logger.error("Exception : ",e);
		}finally {
			session.close();
		}
		return response;
	}
	
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
	@Transactional(readOnly=false)
	public List<Object[]> listCampaignsNew(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String campaign_date1, String campaign_date2, int productId, String columnForOrdering,
			String requiredOrder) {
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.listCampaignQuery(campstatus, searchCampignName, startIndex, pagesize, flag, analytics, campaign_date1, campaign_date2,productId, columnForOrdering,requiredOrder);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pagesize).getResultList();

			/*query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			results = query.list();*/
		}finally {
			session.close();
		}
		return results;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<Object[]> listCampaignsPlatforms(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String campaign_date1, String campaign_date2, int productId) {
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.listCampaignQueryPlatform(campstatus, searchCampignName, startIndex, pagesize, flag, analytics, campaign_date1, campaign_date2,productId);
			results = session.createQuery(hql).getResultList();

			
		}finally {
			session.close();
		}
		return results;
	}
}
