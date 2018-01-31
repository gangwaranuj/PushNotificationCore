package com.saphire.iopush.daoImpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.dao.ICustomNotificationDao;
import com.saphire.iopush.model.IopushCustomNotification;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.utils.Response;

@Repository
@Transactional
public class CustomNotificationDaoImpl implements ICustomNotificationDao {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Override
	public Response save(IopushCustomNotification iopushCustomNotification) {
		Response response = new Response();
		hibernateTemplate.saveOrUpdate(iopushCustomNotification);
		response.setStatus(true);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response autofillNotification(Integer productId,String deviceType) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCustomNotification.class);
		//criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("product_id", productId)).add(Restrictions.eq("deviceType", deviceType));
		
		
		List<IopushCustomNotification> listCustomNotification = (List<IopushCustomNotification>) this.hibernateTemplate
				.findByCriteria(criteria);
		if (listCustomNotification.size() > 0) {
			response.setStatus(true);
			response.setScalarResult(listCustomNotification.get(0));
		} else {
			response.setStatus(false);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getProduct(Integer productId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushProduct.class);
		criteria.add(Restrictions.eq("productID", productId));

		List<IopushProduct> listProduct = (List<IopushProduct>) this.hibernateTemplate.findByCriteria(criteria);
		if (listProduct.size() > 0) {
			response.setStatus(true);
			response.setScalarResult(listProduct.get(0));
		} else {
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response insertCustomNoitifcationDetails(IopushCustomNotification iopushCustomNotification) {
		Response response = new Response();

		int i = 0;
		i = (int) hibernateTemplate.save(iopushCustomNotification);
		//hibernateTemplate.flush();
		if (i > 0) {
			response.setStatus(true);
			response.setIntegerResult(i);

		} else {
			response.setStatus(false);
		}
		return response;
	}

//	@Override
//	public Response autofillNotificationBYDeviceType(Integer productId, String deviceType) {
//
////		Response response = new Response();
////		DetachedCriteria criteria = DetachedCriteria.forClass(IopushCustomNotification.class);
////		//criteria.add(Restrictions.eq("productId", productId));
////		criteria.add(Restrictions.eq("product_id", productId)).add(Restrictions.eq("deviceType", deviceType));
////		
////		
////		List<IopushCustomNotification> listCustomNotification = (List<IopushCustomNotification>) this.hibernateTemplate
////				.findByCriteria(criteria);
////		if (listCustomNotification.size() > 0) {
////			response.setStatus(true);
////			response.setScalarResult(listCustomNotification.get(0));
////		} else {
////			response.setStatus(false);
////		}
////		return response;
//	}

}
