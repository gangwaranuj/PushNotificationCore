package com.saphire.iopush.daoImpl;

import java.text.ParseException;
import java.util.ArrayList;
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

import com.saphire.iopush.dao.IPaypalDao;
import com.saphire.iopush.model.IopushPackagePlan;
import com.saphire.iopush.model.IopushPayPalPayment;
import com.saphire.iopush.model.IopushPayPalPlanSelected;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushPaypalTransactionLog;
import com.saphire.iopush.model.IopushPlan;
import com.saphire.iopush.model.IopushRenewalConfig;
import com.saphire.iopush.model.IopushRenewalCustomer;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.model.IopushUserplan;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.Utility;

@Repository
@Transactional
public class PaypalDaoImpl implements IPaypalDao{

	@Autowired HibernateTemplate hibernateTemplate;


	@Override
	public Response findPlanDetail(int planId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPlan.class);
		criteria.add(Restrictions.eq("planId", planId));
		List<IopushPlan> listIopushUserplan = (List<IopushPlan>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listIopushUserplan.isEmpty()))
		{
			response.setData(listIopushUserplan);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;

	}


	@Override
	public Response savePayment(IopushPayPalPayment iopushPayPalPayment) {

		int i=0;
		Response response = new Response();
		i = (Integer) hibernateTemplate.save(iopushPayPalPayment);

		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;
	}


	@Override
	public Response saveTransction(IopushPaypalTransactionLog iopushPaypalTransactionLog) {

		Response response = new Response();
		int i=0;
		i = (Integer) hibernateTemplate.save(iopushPaypalTransactionLog);
		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;

	}


	@Override
	public Response savePlanSelectInfo(IopushPayPalPlanSelected iopushPayPalPlanSelectedTable) {

		Response response = new Response();
		int i=0;
		i = (Integer) hibernateTemplate.save(iopushPayPalPlanSelectedTable);
		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;

	}




	@Override
	public Response findSelectedUserPlan( int paypalPaymentId) {


		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayPalPlanSelected.class);
		criteria.createAlias("iopushPayPalPayment", "PayPalPayment");
		criteria.add(Restrictions.eq("PayPalPayment.payment_id", paypalPaymentId));

		List<IopushPayPalPlanSelected> iopushUserplan = (List<IopushPayPalPlanSelected>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushUserplan.isEmpty()))
		{
			response.setData(iopushUserplan);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response updateUsercategory(IopushUsercategory iopushUsercategory) {

		Response response = new Response();
		this.hibernateTemplate.update(iopushUsercategory);
		response.setStatus(true);
		return response;

	}


	@Override
	public Response findUsercategory( int prodID) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUsercategory.class);
		criteria.add(Restrictions.eq("product_id", prodID));
		List<IopushUsercategory> iopushUsercategory = (List<IopushUsercategory>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushUsercategory.isEmpty()))
		{
			response.setData(iopushUsercategory);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response findExecuteURL(String token, int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("tpToken", token));
		criteria.add(Restrictions.eq("fkProductId", productId));
		List<IopushPayment> listIopushpayment = (List<IopushPayment>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listIopushpayment.isEmpty()))
		{
			response.setData(listIopushpayment);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;


	}

	@Override
	public Response updatePaypalPaymentDetail(IopushPayPalPayment iopushPayPalPayment) {

		Response response = new Response();
		this.hibernateTemplate.update(iopushPayPalPayment);
		response.setStatus(true);
		return response;
	}




	@Override
	public Response findUserPlanById(int planId) {
		// TODO Auto-generated method stub


		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUserplan.class);
		criteria.add(Restrictions.eq("planId", planId));
		List<IopushUserplan> iopushUserplan = (List<IopushUserplan>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushUserplan.isEmpty()))
		{
			response.setData(iopushUserplan);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response fetchPlanInfo(int prodId, String agreementId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("fkProductId", prodId));
		criteria.add(Restrictions.eq("agreementId", agreementId));
		List<IopushPayment> listIopushUserplan = (List<IopushPayment>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listIopushUserplan.isEmpty()))
		{
			response.setScalarResult(listIopushUserplan.get(0));
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public List<Object[]> getIncompletePaymentUserInfo() {

		Session session = null;
		List<Object[]> results = new ArrayList<Object[]>();
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction();
			String hql = Utility.createIncompletePaymentQuery();
			results = session.createSQLQuery(hql).getResultList();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
		} finally {
			session.close();
		}
		return results;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Object[] getCurrentUserPlan(int productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.groupProperty("fkPackageId"));
		projection.add(Projections.max("fkPlanId"));
		projection.add(Projections.groupProperty("paymentId"));
		
		criteria.add(Restrictions.isNotNull("nextPaymentdate"));
		criteria.add(Restrictions.eq("tpAcknowledgement", 1));
		criteria.add(Restrictions.eq("fkProductId", productId));
		criteria.addOrder(Order.desc("paymentId"));
		criteria.setProjection(projection);
		
		List<Object[]> listPayment = (List<Object[]>) hibernateTemplate.findByCriteria(criteria);
		if(!listPayment.isEmpty())
		{
			return listPayment.get(0);
		}
		return null;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPackages(Integer[] fkPlanId, int fkPackageId) {
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.packagesList(fkPlanId, fkPackageId);
			results = session.createQuery(hql).getResultList();
		}finally {
			session.close();
		}
		return results;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getPlanWithHighPreference(int fkPlanId) {
		Session session =null;
		List<Integer> results=new ArrayList<Integer>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.planList(fkPlanId);
			results = session.createQuery(hql).getResultList();
		}finally {
			session.close();
		}
		return results;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllPlans() {
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.allPackagesList();
			results = session.createQuery(hql).getResultList();
		}finally {
			session.close();
		}
		return results;
	}


	@Override
	public Response updatePaypalPaymentDetails(IopushPayment iopushPayment) {
		Response response = new Response();
		this.hibernateTemplate.update(iopushPayment);
		response.setStatus(true);
		return response;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getPackageDetails(int fkPackageId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPackagePlan.class);
		criteria.add(Restrictions.eq("packageId", fkPackageId));
		List<IopushPackagePlan> listPackagePlan = (List<IopushPackagePlan>) hibernateTemplate.findByCriteria(criteria);
		if(!listPackagePlan.isEmpty())
		{
			response.setStatus(true);
			response.setScalarResult(listPackagePlan.get(0));
		}
		else
		{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response saveTransaction(IopushPaypalTransactionLog iopushTransactionLog) {
		Response response = new Response();
		int i=0;
		i = (Integer) hibernateTemplate.save(iopushTransactionLog);
		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;
	}





	@Override
	public Response findUserByProdID(List<Integer> prodId) {


		Response res = new Response();

		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.in("iopushProduct.productID", prodId));

		List<IopushUser> list = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);

		if(!list.isEmpty()){
			res.setStatus(true);
			res.setData(list);
		}
		else{
			res.setStatus(false);
		}
		return res;
	}


	@Override
	public Response getRenewConfig() {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRenewalConfig.class);
		List<IopushRenewalConfig> iopushRenewalConfig= (List<IopushRenewalConfig>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushRenewalConfig.isEmpty()))
		{
			response.setData(iopushRenewalConfig);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;

	}


	@Override
	public Response savePayment(IopushPayment iopushPayment) {

		int i=0;
		Response response = new Response();
		i = (Integer) hibernateTemplate.save(iopushPayment);


		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;
	}

	@Override
	public Response getRenewalInfo(Date startDate, Date endDate) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);

		criteria.add(Restrictions.ilike("status", "Active"));
		criteria.add(Restrictions.between("nextPaymentdate", startDate, endDate));
		criteria.addOrder(Order.asc("nextPaymentdate"));
		List<IopushPayment> iopushPayment= (List<IopushPayment>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushPayment.isEmpty()))
		{
			response.setData(iopushPayment);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;

	}

	@Override
	public Response saveRenewInfo(IopushRenewalCustomer renew) {


		int i=0;
		Response response = new Response();
		i = (Integer) hibernateTemplate.save(renew);

		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);
		}
		else
			response.setStatus(false);
		return response;
	}

	@Override
	public Response getUserInfo(int prodID) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		//iopushProduct productID
		criteria.createAlias("iopushProduct", "product");

		criteria.add(Restrictions.eq("product.productID", prodID));
		List<IopushUser> iopushUser= (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushUser.isEmpty()))
		{
			response.setData(iopushUser);
			response.setStatus(true);
		}else{
			response.setStatus(false);

		}
		return response;

	}

	@Override
	public void  deleteRenewCostumer(IopushRenewalCustomer deleteObject) {
		
		this.hibernateTemplate.delete(deleteObject);

	}

	@Override
	public Response findPackageDetail(int planId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPackagePlan.class);
		criteria.add(Restrictions.eq("packageId", planId));
		List<IopushPackagePlan> listIopushUserplan = (List<IopushPackagePlan>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listIopushUserplan.isEmpty()))
		{
			response.setData(listIopushUserplan);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;

	}

	@Override
	public Response findPaymentDetails(int paymentID) {
		// TODO Auto-generated method stub

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("paymentId", paymentID));
		criteria.addOrder(Order.desc("modifiedOn"));
		List<IopushPayment> listIopushpaymentURL = (List<IopushPayment>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listIopushpaymentURL.isEmpty()))
		{
			response.setData(listIopushpaymentURL);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;


	}

	@Override
	public Response fetchRenewals(Date date) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushRenewalCustomer.class);
		criteria.add(Restrictions.lt("nextRenewDate",date));
		List<IopushRenewalCustomer> iopushRenew= (List<IopushRenewalCustomer>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushRenew.isEmpty()))
		{
			response.setData(iopushRenew);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}
	@Override
	public List<Object[]> fetchPackageInfo(int packageId, int planId) {

		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.createPackageInfoQuery(planId,packageId);
			results = session.createSQLQuery(hql).getResultList();

		}finally {
			session.close();
		}
		return results;
	}


	@Override
	public Response updatePayment(IopushPayment paymentInfo) {

		Response response = new Response();
		this.hibernateTemplate.update(paymentInfo);
		response.setStatus(true);
		return response;

	}

	@Override
	public Response getPaymentInfo(String agreementID) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("agreementId",agreementID));
		criteria.addOrder(Order.desc("modifiedOn"));
//		criteria.add(Restrictions.ne("status",Constants.PAYAL_AGREEMENT_STATUS_CANCEL));
		List<IopushRenewalCustomer> iopushRenew= (List<IopushRenewalCustomer>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(iopushRenew.isEmpty()))
		{
			response.setData(iopushRenew);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response findRenewalUserdetails(String agreementStatus) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Object[]> getUserInfoForAgreementId(String agreementID) {
		
		Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.getUserInfoQuery(agreementID);
			results = session.createSQLQuery(hql).getResultList();

		}finally {
			session.close();
		}
		return results;
	}


	@Override
	public List<Object[]> getUserInfoForAgreementIdArray(List<String> failedAgreementIDList) {
		
		/*Session session =null;
		List<Object[]> results=new ArrayList<Object[]>();
		try{
			session=hibernateTemplate.getSessionFactory().openSession();
			session.beginTransaction() ;
			String hql=Utility.getUserInfoQuery(agreementID);
			results = session.createSQLQuery(hql).getResultList();

		}finally {
			session.close();*/
		
		return null;
	}


	@Override
	public Response getUserInfo(Object[] array) {
		
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.in("iopushProduct.productID",array));
		List<IopushUser> userList= (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(userList.isEmpty()))
		{
			response.setData(userList);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response getPaymentInfo(Object[] array) {

		
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.in("fkProductId",array));
		criteria.addOrder(Order.desc("modifiedOn"));
		List<IopushUser> userList= (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(userList.isEmpty()))
		{
			response.setScalarResult(userList.get(0));
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}


	@Override
	public Response getPaymentInfo(int prodID) {

		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushPayment.class);
		criteria.add(Restrictions.eq("fkProductId",prodID));
		criteria.addOrder(Order.desc("modifiedOn"));
		List<IopushUser> userList= (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(userList.isEmpty()))
		{
			response.setScalarResult(userList.get(0));
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}

}


