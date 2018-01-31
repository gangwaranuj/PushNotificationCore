package com.saphire.iopush.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.model.IopushCityDetails;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushGetInTouch;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushPlatformDetails;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSegmentation;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.model.IopushUserplan;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;
@Transactional
@Repository
public class ExternalAPIDaoImpl implements IExternalAPIDao{

	@Autowired HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public Response findGeoCode(String country_code) {
		Response response = new Response();
		try
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushGeoDetails.class);
			criteria.add(Restrictions.eq("geoCode", country_code).ignoreCase());
			List<IopushGeoDetails> listCountries = (List<IopushGeoDetails>) this.hibernateTemplate.findByCriteria(criteria);
			if(listCountries.size()>0)
			{
				response.setStatus(true);
				response.setScalarResult(listCountries.get(0));
			}
			else
			{
				response.setStatus(false);
			}
		}
		catch (Exception e)
		{
			response.setErrorMessage(e.getMessage());
			response.setException(e);
			response.setStatus(false);
		}
		return response;
	}



	@Override
	public Response saveGeoDetail(IopushGeoDetails iopushGeoDetails) {
		Response response = new Response();
		int geo_id = 0;
		geo_id = (int) hibernateTemplate.save(iopushGeoDetails);
		if(geo_id>0){
			response.setStatus(true);
			response.setIntegerResult(geo_id);
		}
		else
			response.setStatus(false);
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findCityCode(String city_code, int geo_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCityDetails.class);
		criteria.createAlias("iopushGeoDetails", "geo");
		
		criteria.add(Restrictions.eq("cityCode", city_code));
		criteria.add(Restrictions.eq("geo.geoId", geo_id));
		List<IopushCityDetails> listIopushCityDetails = (List<IopushCityDetails>) hibernateTemplate.findByCriteria(criteria);
		if(! listIopushCityDetails.isEmpty())
		{
			response.setStatus(true);
			response.setScalarResult(listIopushCityDetails.get(0));
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}



	@Override
	public Response saveCityCode(IopushCityDetails iopushCityDetails) {
		Response response = new Response();
		int city_id = 0;
		city_id = (int)hibernateTemplate.save(iopushCityDetails);
		if(city_id>0){
			response.setStatus(true);
			response.setIntegerResult(city_id);
		}
		else
			response.setStatus(false);

		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findPlatformName(String platformName) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPlatformDetails.class);
		criteria.add(Restrictions.eq("platformName", platformName));
		List<IopushPlatformDetails> listIopushPlatformDetails = (List<IopushPlatformDetails>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIopushPlatformDetails.size()>0)
		{
			response.setData(listIopushPlatformDetails);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findProductID(String hash) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushProduct.class);
		criteria.add(Restrictions.eq("hash", hash));
		List<IopushProduct> listIopushProduct = (List<IopushProduct>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIopushProduct.size()>0)
		{
			response.setIntegerResult(listIopushProduct.get(0).getProductID());
			response.setScalarResult(listIopushProduct.get(0));
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response findSegmentID(String hash) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushSegmentation.class);
		criteria.add(Restrictions.eq("hash", hash));
		List<IopushSegmentation> listIopushSegmentation = (List<IopushSegmentation>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIopushSegmentation.size()>0)
		{
			response.setIntegerResult(listIopushSegmentation.get(0).getSegmentId());
			response.setScalarResult(listIopushSegmentation.get(0));
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response saveSubscriber(IopushSubscribers iopushSubscribers) {
		Response response = new Response();
		int subscriber_id = 0;
		subscriber_id = (int)hibernateTemplate.save(iopushSubscribers);
		if(subscriber_id >0){
			response.setStatus(true);
			response.setIntegerResult(subscriber_id );
			response.setScalarResult(iopushSubscribers);
			iopushSubscribers.setSubscriberId(subscriber_id);
		}
		else
			response.setStatus(false);

		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response findIspID(String isp, int geo_id) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushIsp.class);
		criteria.add(Restrictions.eq("ispName", isp));
		criteria.add(Restrictions.eq("countryId", geo_id));
		List<IopushIsp> listIopushIsp = (List<IopushIsp>) this.hibernateTemplate.findByCriteria(criteria);
		if(listIopushIsp.size()>0)
		{
			response.setIntegerResult(listIopushIsp.get(0).getIspId());
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response listSubscribers() {
		Response response = new Response();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IopushSubscribers.class);
		//listing the subscriber only for demo
		detachedCriteria.addOrder(Order.desc("subscriberId") );
		List<IopushSubscribers> listSubscribers = (List<IopushSubscribers>) this.hibernateTemplate.findByCriteria(detachedCriteria,0,5);
		if(listSubscribers.size()>0)
		{
			response.setStatus(true);
			response.setData(listSubscribers);
		}
		return response;
	}



	@Override
	public Response saveIsp(IopushIsp isp) {
		Response response = new Response();
		int isp_id = 0;
		isp_id = (int) hibernateTemplate.save(isp);

		if(isp_id >0){
			response.setStatus(true);
			response.setIntegerResult(isp_id);
		}
		else
			response.setStatus(false);

		return response;
	}



	@Override
	public Response updateActivateSubscriberStat(String iopush_token, int campaign_id, int action) {
		String query="";
		if(action==1) query=Constants.UPDATE_SUBSCRIBERLIST_OPEN_STATS_Query;
		else if(action==2) query=Constants.UPDATE_SUBSCRIBERLIST_CLOSE_STATS_Query;
		else if(action==3) query=Constants.UPDATE_SUBSCRIBERLIST_CLICK_STATS_Query;
		else query=Constants.UPDATE_SUBSCRIBERLIST_CLICK_OPEN_STATS_Query;
		Response response = new Response();
		response.setIntegerResult(this.hibernateTemplate.bulkUpdate(query, iopush_token,campaign_id));
		return response;
	}

	public Response updateUserCategory(IopushUsercategory iopushUserCategory){
		Response response = new Response();
		this.hibernateTemplate.update(iopushUserCategory);
		response.setStatus(true);
		return response ;
	}



	@Override
	public Response saveRegistrationDetails(IopushUser iopushUser) {
		Response response = new Response();
		int user_id = 0;
		user_id =  (int) this.hibernateTemplate.save(iopushUser);
		//this.hibernateTemplate.flush();
		if(user_id > 0)
		{
			response.setStatus(true);
			response.setIntegerResult(user_id);
		}
		else
		{
			response.setStatus(false);
		}
		return response ;
	}



	@Override
	public Response saveProduct(IopushProduct iopushProduct) {
		Response response = new Response();
		int product_id = 0;
		product_id =  (int) this.hibernateTemplate.save(iopushProduct);
		//this.hibernateTemplate.flush();
		if(product_id > 0)
		{
			response.setStatus(true);
			response.setIntegerResult(product_id);
		}
		else
		{
			response.setStatus(false);
		}
		return response ;
	}




	@Override
	public Response saveTimezone(IopushTimeZone iopushTimeZone) {
		Response response = new Response();
		int timezone_id = 0;
		timezone_id = (int) hibernateTemplate.save(iopushTimeZone);
		if(timezone_id>0){
			response.setStatus(true);
			response.setIntegerResult(timezone_id);
		}
		else
			response.setStatus(false);
		return response;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Response findUserPlan() {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUserplan.class);

		List<IopushUserplan> userPlan = (List<IopushUserplan>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(userPlan.isEmpty())){

			response.setStatus(true);
			response.setData(userPlan);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response checkEmailandDomain( String subDomain) {

		Response response = new Response();

		DetachedCriteria criteria = DetachedCriteria.forClass(IopushProduct.class);
		criteria.add(Restrictions.eq("productName", subDomain));
		List<IopushUser> userPlan = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);

		if ((userPlan.isEmpty())) {

			response.setStatus(true);

		} else {
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public List<Object[]> findUnsubscribedUserdetail() {


		Response res= null;
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.QueryForFindUserdetail();
			results = session.createSQLQuery(hql).getResultList();


		}finally {
			session.close();
		}

		return results;
	}

	@Override
	public Response updateEmailFlag(IopushUser iopushUser) {

		Response res=new Response();
		this.hibernateTemplate.update(iopushUser);
		res.setStatus(true);
		return res;
	}



	@Override
	public Response checkResendregistrationEmailUser() {

		int flag=0;
		Response res = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.eq("emailFlag", flag));
		criteria.add(Restrictions.eq("isActive", false));
		List<IopushUser> iopushUser = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if(!iopushUser.isEmpty()){
			res.setData(iopushUser);
			res.setStatus(true);
		}
		else{
			res.setStatus(false);
		}
		return res;
	}


	@SuppressWarnings("unchecked")
	public UserDetails findUserByName(String username) throws UsernameNotFoundException {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
			criteria.add(Restrictions.eq("username", username));

			List<IopushUser> theUserList = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);

			if (theUserList.isEmpty()) {
				throw new UsernameNotFoundException("User Not Found");
			}
			return (UserDetails) theUserList.iterator().next();
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User Not Found");
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=false)
	public ResponseMessage updateActiveStatus(String username)
	{	
		int count=0;
		ResponseMessage responseMessage = new ResponseMessage();
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
			criteria.add(Restrictions.eq("username", username));
			List<IopushUser> listUser = (List<IopushUser>) hibernateTemplate.findByCriteria(criteria);
			if(!listUser.isEmpty())
			{
				IopushUser iopushUser = listUser.get(0);
				iopushUser.setIsActive(true);
				iopushUser.setLinkRoute(true);
				hibernateTemplate.update(iopushUser);
				responseMessage.setResponseDescription("Success");
				responseMessage.setResponseCode(Constants.SUCCESS_CODE);
			}
			else
			{
				responseMessage.setResponseDescription("Error");
				responseMessage.setResponseCode(Constants.ERROR_CODE_INVALID);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseMessage;
	}



	@Override
	public Response updatePaymentDetail(IopushPayment iopushPayment) {

		Response res = new Response();
		this.hibernateTemplate.update(iopushPayment);
		res.setStatus(true);
		return res;
	}




	@Override
	public Response fetchPaypalPaymentdetail(int pid, String status) {

		Response res = new  Response();


		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("fkProductId", pid));
		criteria.add(Restrictions.eq("status",status));
		criteria.addOrder(Order.desc("modifiedOn"));
		List<IopushPayment> list = (List<IopushPayment>) this.hibernateTemplate.findByCriteria(criteria);		

		if(!list.isEmpty())
		{
			res.setData(list);
			res.setStatus(true);

		}
		else{
			res.setStatus(false);
		}
		return res;
	}


	@Override
	public  List<Object[]> fetchFailCustomerRenewalInfo(String d1,String d2) {

		Session session =null;


		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.queryForFindRenewalFailCustomerInfo(d1,d2);
			results = session.createSQLQuery(hql).getResultList();

		}finally {
			session.close();
		}
		return results;

	}


	@Override
	public Response saveGetInTouch(IopushGetInTouch iopushgetintouch) {
		Response response = new Response();
		int i=0;
		i=(int) hibernateTemplate.save(iopushgetintouch);

		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);	

		}
		else{
			response.setStatus(false);
		}
		return response;

	}

	@Override
	public Response fetchUserById(int userId) {

		Response res = new  Response();


		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.eq("userId", userId));
		List<IopushUser> list = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);		

		if(!list.isEmpty())
		{
			res.setData(list);
			res.setStatus(true);
		}
		else{
			res.setStatus(false);
		}
		return res;
	}

	@Override
	public Response fetchUserByProdId(int prodId) {

		Response res = new  Response();


		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.createAlias("iopushProduct", "product");
		criteria.add(Restrictions.eq("product.productID", prodId));
		List<IopushUser> list = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);		

		if(!list.isEmpty())
		{
			res.setScalarResult(list.get(0));
			res.setStatus(true);
		}
		else{
			res.setStatus(false);
		}
		return res;
	}


	@Override
	public Response findUserByEmailId(String email) {

		Response res = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.eq("emailId", email));
		List<IopushUser> list = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);		
		if(list.isEmpty())
		{
			res.setStatus(true);
		}
		else{
			res.setStatus(false);
		}
		return res;
	}



	@Override
	public Response fetchLimitExceedUserDetail() {

		Response res = new Response();

		DetachedCriteria userCategorycriteria= DetachedCriteria.forClass(IopushUsercategory.class);
		userCategorycriteria.add(Restrictions.eq("limitExceed", Constants.LIMIT_EXCEED));
		List<IopushUsercategory> list = (List<IopushUsercategory>) this.hibernateTemplate.findByCriteria(userCategorycriteria);
		if(!list.isEmpty()){
			Integer[] productIds = list.stream().map(p -> (Integer)p.getProduct_id() ).toArray(Integer[]::new);
			DetachedCriteria usercriteria= DetachedCriteria.forClass(IopushUser.class);
			usercriteria.add(Restrictions.in("iopushProduct.productID", productIds));
			List<IopushUser> userrlist= (List<IopushUser>) this.hibernateTemplate.findByCriteria(usercriteria);
			if(!userrlist.isEmpty()){

				res.setStatus(true);
				res.setData(userrlist);

			}
			else{

				res.setStatus(false);
			}

		}
		else{

			res.setStatus(false);
		}
		return res;
	}



	@Override
	public Response updateIsLimitExceedFlag(int prodId) {

		Response res = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUsercategory.class);
		criteria.add(Restrictions.eq("product_id", prodId));
		List<IopushUsercategory> list= (List<IopushUsercategory>) this.hibernateTemplate.findByCriteria(criteria);

		if(!list.isEmpty()){
			IopushUsercategory iopushUsercategory=list.get(0);
			iopushUsercategory.setLimitExceed(Constants.LIMIT_NOT_EXCEED);
			this.hibernateTemplate.update(iopushUsercategory);
			res.setStatus(true);
		}
		else {
			res.setStatus(false);
		}
		return res;
	}

}
