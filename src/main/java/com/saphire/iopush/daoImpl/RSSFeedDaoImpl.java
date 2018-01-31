package com.saphire.iopush.daoImpl;

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

import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSFeedResponseBean;
import com.saphire.iopush.dao.IRSSFeedDao;
import com.saphire.iopush.model.IopushRSSStats;
import com.saphire.iopush.model.IopushRouteManager;
import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.model.IopushRssFeedSchedular;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.SecurityUtils;
import com.saphire.iopush.utils.Utility;

@Transactional
@Repository
public class RSSFeedDaoImpl implements IRSSFeedDao {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Override
	public Response deleteRSSFeedSchedularConfig(int rssfeedid) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.RSSFEED_SCHEDULAR_DELETE_QUERY, rssfeedid));
		return res;
	}

	@Override
	public Response saveDataSchedular(RSSFeedResponseBean data, int rssfeedid) {
		Response response = new Response();
		IopushRssFeedSchedular iopushRssFeedSchedular = new IopushRssFeedSchedular();
		iopushRssFeedSchedular.setArticleid(data.getArticleid());
		iopushRssFeedSchedular.setContent(
				data.getContent().length() > 1000 ? data.getContent().substring(0, 995) : data.getContent());
		iopushRssFeedSchedular.setDescription(data.getDescription().length() > 1000
				? data.getDescription().substring(0, 995) : data.getDescription());
		iopushRssFeedSchedular.setEnclosure(data.getEnclosure());
		iopushRssFeedSchedular.setGuid(data.getGuid());
		iopushRssFeedSchedular.setLink(data.getLink());
		iopushRssFeedSchedular.setPubDate(data.getPubDate());
		iopushRssFeedSchedular.setReqDate(new Date());
		iopushRssFeedSchedular.setRssfeedid(rssfeedid);
		iopushRssFeedSchedular.setThumbnail(data.getThumbnail());
		iopushRssFeedSchedular.setProductid(1);
		iopushRssFeedSchedular
		.setTitle(data.getTitle().length() > 1000 ? data.getTitle().substring(0, 995) : data.getTitle());
		int schId = (int) hibernateTemplate.save(iopushRssFeedSchedular);
		iopushRssFeedSchedular.setId(schId);
		response.setScalarResult(iopushRssFeedSchedular);
		response.setStatus(true);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response fetchRssFeedSchedule() {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRssFeedSchedular.class);
		List<IopushRssFeedSchedular> data = (List<IopushRssFeedSchedular>) this.hibernateTemplate
				.findByCriteria(criteria);
		response.setStatus(data.size() > 0 ? true : false);
		response.setData(data);
		return response;
	}

	@Override
	public Response updateDataSchedular(RSSFeedResponseBean data, int rssfeedid, int schid) {
		Response response = new Response();
		IopushRssFeedSchedular iopushRssFeedSchedular = new IopushRssFeedSchedular();
		iopushRssFeedSchedular.setId(schid);
		iopushRssFeedSchedular.setArticleid(data.getArticleid());
		iopushRssFeedSchedular.setContent(
				data.getContent().length() > 1000 ? data.getContent().substring(0, 995) : data.getContent());
		iopushRssFeedSchedular.setDescription(data.getDescription().length() > 1000
				? data.getDescription().substring(0, 995) : data.getDescription());
		iopushRssFeedSchedular.setEnclosure(data.getEnclosure());
		iopushRssFeedSchedular.setGuid(data.getGuid());
		iopushRssFeedSchedular.setLink(data.getLink());
		iopushRssFeedSchedular.setPubDate(data.getPubDate());
		iopushRssFeedSchedular.setReqDate(new Date());
		iopushRssFeedSchedular.setRssfeedid(rssfeedid);
		iopushRssFeedSchedular.setThumbnail(data.getThumbnail());
		iopushRssFeedSchedular.setProductid(1);
		iopushRssFeedSchedular
		.setTitle(data.getTitle().length() > 1000 ? data.getTitle().substring(0, 995) : data.getTitle());
		hibernateTemplate.merge(iopushRssFeedSchedular);
		response.setScalarResult(iopushRssFeedSchedular);
		response.setStatus(true);
		return response;
	}

	@Override
	public Response statusrssfeed(int id, int active) {
		Response res = new Response();
		res.setIntegerResult(
				this.hibernateTemplate.bulkUpdate(Constants.UPDATE_RSSFEEDCONFIG_STATUS_Query, active, id));
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response rssfeed(int id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRssFeedConfig.class);
		criteria.add(Restrictions.eq("id", id));
		List<IopushRssFeedConfig> rssfeedconfig = (List<IopushRssFeedConfig>) this.hibernateTemplate.findByCriteria(criteria);
		if (rssfeedconfig.size() > 0) {
			response.setStatus(true);
			response.setScalarResult(rssfeedconfig.get(0));
		} else {
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response isExistRSSFeedName(String name) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRssFeedConfig.class);
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		List<IopushRssFeedConfig> rssfeedconfig = (List<IopushRssFeedConfig>) this.hibernateTemplate.findByCriteria(criteria);
		if (rssfeedconfig.size() > 0) {
			response.setStatus(true);
		} else {
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response saveRSSFeedConfig(RSSFeedBean rssfeedBean) {
		Response response = new Response();
		IopushRssFeedConfig iopushRssFeedConfig = new IopushRssFeedConfig();
		iopushRssFeedConfig.setCampaign(rssfeedBean.getCampaign());
		iopushRssFeedConfig.setGeneric(rssfeedBean.getGeneric());
		iopushRssFeedConfig.setSource(rssfeedBean.getSource());
		iopushRssFeedConfig.setUrl(rssfeedBean.getUrl());
		iopushRssFeedConfig.setName(rssfeedBean.getName());
		iopushRssFeedConfig.setCountries(rssfeedBean.getCountries());
		iopushRssFeedConfig.setCities(rssfeedBean.getCities());
		iopushRssFeedConfig.setIsps(rssfeedBean.getIsps());
		iopushRssFeedConfig.setPlatform(rssfeedBean.getPlatform());
		iopushRssFeedConfig.setProducts(rssfeedBean.getProducts());
		iopushRssFeedConfig.setCreatedBy(SecurityUtils.getCurrentLogin());
		iopushRssFeedConfig.setCreationDate(new Date());
		iopushRssFeedConfig.setModificationDate(new Date());
		iopushRssFeedConfig.setModificedBy(SecurityUtils.getCurrentLogin());
		iopushRssFeedConfig.setNotification(rssfeedBean.getNotification() ? 1 : 0);
		iopushRssFeedConfig.setAutomatedImage(rssfeedBean.isUseAutomatedImage()?1:0);
		iopushRssFeedConfig.setLogo_path(rssfeedBean.getLogo());
		iopushRssFeedConfig.setSegments(rssfeedBean.getSegments());
		iopushRssFeedConfig.setSegmentTypes(rssfeedBean.getSegmentTypes());
		iopushRssFeedConfig.setLogo_path(rssfeedBean.getLogo());
		iopushRssFeedConfig.setSubscribedFrom(rssfeedBean.getSubscribedFrom());
		
		
		int id = (int) this.hibernateTemplate.save(iopushRssFeedConfig);

		iopushRssFeedConfig.setId(id);
		response.setScalarResult(iopushRssFeedConfig);
		return response;
	}

	@Override
	public Response updateRSSFeedConfig(RSSFeedBean rssfeedBean) {
		Response response = new Response();
		IopushRssFeedConfig iopushRssFeedConfig = new IopushRssFeedConfig();
		iopushRssFeedConfig.setId(rssfeedBean.getId());
		iopushRssFeedConfig.setCampaign(rssfeedBean.getCampaign());
		iopushRssFeedConfig.setGeneric(rssfeedBean.getGeneric());
		iopushRssFeedConfig.setSource(rssfeedBean.getSource());
		iopushRssFeedConfig.setUrl(rssfeedBean.getUrl());
		iopushRssFeedConfig.setName(rssfeedBean.getName());
		iopushRssFeedConfig.setCountries(rssfeedBean.getCountries());
		iopushRssFeedConfig.setCities(rssfeedBean.getCities());
		iopushRssFeedConfig.setIsps(rssfeedBean.getIsps());
		iopushRssFeedConfig.setPlatform(rssfeedBean.getPlatform());
		iopushRssFeedConfig.setProducts(rssfeedBean.getProducts());
		iopushRssFeedConfig.setCreatedBy(SecurityUtils.getCurrentLogin());
		iopushRssFeedConfig.setCreationDate(new Date());
		iopushRssFeedConfig.setModificationDate(new Date());
		iopushRssFeedConfig.setModificedBy(SecurityUtils.getCurrentLogin());
		iopushRssFeedConfig.setNotification(rssfeedBean.getNotification() ? 1 : 0);
		iopushRssFeedConfig.setAutomatedImage(rssfeedBean.isUseAutomatedImage()?1:0);
		iopushRssFeedConfig.setLogo_path(rssfeedBean.getLogo());
		iopushRssFeedConfig.setSegments(rssfeedBean.getSegments());
		iopushRssFeedConfig.setSegmentTypes(rssfeedBean.getSegmentTypes());
		this.hibernateTemplate.merge(iopushRssFeedConfig);
		response.setScalarResult(iopushRssFeedConfig);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listRssfeed(int startlimit, int pageSize,int pid, String name, int id, String columnForOrdering, String requiredOrder,Integer[] status) {
		Response response = new Response();

		DetachedCriteria criteria1 = DetachedCriteria.forClass(IopushRssFeedConfig.class);
		criteria1.add(Restrictions.in("notification", status));
		criteria1.add(Restrictions.eq("products", String.valueOf(pid))) ;
		criteria1.setProjection(Projections.rowCount());

		List<Long> rssfeedconfig = (List<Long>) this.hibernateTemplate.findByCriteria(criteria1);

		if (rssfeedconfig.size() > 0) {
			response.setStatus(true);
			response.setIntegerResult(rssfeedconfig.get(0).intValue());
		} else {
			response.setStatus(false);
		}
		return response;
	}
	
	@Override
	public List<Object[]> listRssfeedNew(  Integer[] status,String campaign_name,int startIndex,int pagesize,boolean flag, boolean analytics,String start_date, String end_date, int pid,  int id, String columnForOrdering,String requiredOrder) {


		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.listRssfeedQuery(status, campaign_name, startIndex, pagesize, flag, analytics, start_date, end_date, pid, columnForOrdering, requiredOrder);
			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pagesize).getResultList();
		}finally {
			session.close();
		}
		return results;
		
		
	}
	
	

	@Override
	public Response deleteRSSFeedConfig(int id) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.RSSFEED_DELETE_QUERY, id));
		return res;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public List<IopushRSSStats> listRSS(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid, String columnForOrdering, String requiredOrdering) {
		List<IopushRSSStats> listRSS = new ArrayList<>();
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushRSSStats.class);

			if(!rssName.isEmpty())
			{
				criteria.add(Restrictions.eq("rssName", rssName));
			}
			if(rss_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date rssDate1 = sdf.parse(rss_date1);
				Date rssDate2 = sdf.parse(rss_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rssDate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);

				criteria.add(Restrictions.between("date", rssDate1, calendar.getTime()));
			}
			
			criteria.add(Restrictions.eq("pid", pid)) ;
			if(requiredOrdering.equalsIgnoreCase("desc"))
			{
				criteria.addOrder(Order.desc(columnForOrdering));
			}
			else
			{
				criteria.addOrder(Order.asc(columnForOrdering));
			}
			
			listRSS = (List<IopushRSSStats>) hibernateTemplate.findByCriteria(criteria,startIndex,pagesize);
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
		return listRSS;

	}
	
//	
//	@Override
//	public List<Object[]> listRSSAnalyticnew(int startIndex, int pagesize, boolean analytics, String rssName,
//			String rss_date1, String rss_date2, int pid, String columnForOrdering, String requiredOrder) {
//		
//		
//		Session session =null;
//		List<Object[]> results=new ArrayList<Object[]>();
//		try{
//			session=hibernateTemplate.getSessionFactory().openSession();
//			session.beginTransaction() ;
//			String hql=Utility.listAnalyticsRssfeedQuery(rssName, analytics,rss_date1,rss_date2,pid,columnForOrdering,requiredOrder);
//			results = session.createQuery(hql).setFirstResult(startIndex).setMaxResults(pagesize).getResultList();
//
//		}finally {
//			session.close();
//		}
//		return results;
//	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=false)
	public int countRSS(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid) {
		int count = 0;
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushRSSStats.class);
			if(!rssName.isEmpty())
			{
				criteria.add(Restrictions.eq("rssName", rssName));
			}
			if(rss_date1!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date rssDate1 = sdf.parse(rss_date1);
				Date rssDate2 = sdf.parse(rss_date2);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rssDate2);
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE,59);
				calendar.set(Calendar.SECOND, 59);

				criteria.add(Restrictions.between("date", rssDate1, calendar.getTime()));
			}
			criteria.add(Restrictions.eq("pid", pid)) ;
			ProjectionList projection = Projections.projectionList();
			projection.add(Projections.rowCount());
			List<Object[]> list = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
			count = list.size();

		}
		catch(Exception e)
		{

		}

		return count;

	}

	@Override
	public Response saveRouterInfo(IopushRouteManager routeManager) {
		Response response = new Response();
		int id = (int) hibernateTemplate.save(routeManager);
		if(id > 0)
		{
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
	public Response isRSSNameNew(String name, int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRssFeedConfig.class);
		
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.eq("products", String.valueOf(productId)));
		List<IopushRssFeedConfig> rssfeedconfig = (List<IopushRssFeedConfig>) this.hibernateTemplate.findByCriteria(criteria);
		if (rssfeedconfig.size() > 0) {
			response.setStatus(true);
		} else {
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response activateRSS(int id, int active) {
		Response res = new Response();
		res.setIntegerResult(
				this.hibernateTemplate.bulkUpdate(Constants.UPDATE_RSSFEEDCONFIG_STATUS_Query, active, id));
		return res;
	}

	@Override
	public Response updateRoute(int id, int active) {
		Response res = new Response();
		res.setIntegerResult(
				this.hibernateTemplate.bulkUpdate(Constants.UPDATE_ROUTE_MANAGER_STATUS_Query, active, id));
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response isRSSUrlUnique(String url, int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRssFeedConfig.class);
		criteria.add(Restrictions.eq("url", url).ignoreCase());
		criteria.add(Restrictions.eq("products", String.valueOf(productId)));
		List<IopushRssFeedConfig> rssfeedconfig = (List<IopushRssFeedConfig>) this.hibernateTemplate.findByCriteria(criteria);
		if (rssfeedconfig.size() > 0) {
			response.setIntegerResult(rssfeedconfig.get(0).getId());
			response.setStatus(true);
			
		} else {
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response deleteRSSStat(String name, int pid) {
		Response res = new Response();
		res.setIntegerResult(this.hibernateTemplate.bulkUpdate(Constants.RSS_STATS_DELETE_QUERY, name, pid));
		return res;
	}

	
}
