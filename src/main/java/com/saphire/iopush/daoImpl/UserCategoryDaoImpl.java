package com.saphire.iopush.daoImpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.dao.IUserCategoryDao;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Response;



@Transactional
@Repository
public class UserCategoryDaoImpl implements IUserCategoryDao {
	@Autowired HibernateTemplate hibernateTemplate;
	

	@Override
	public Response updateSubscriberBalanceAndUsed(int product_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response userCategoryDetails(int product_id) {
			
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUsercategory.class);
		criteria.add(Restrictions.eq("product_id", product_id));
		List<IopushUsercategory> listUserCategory = (List<IopushUsercategory>) this.hibernateTemplate.findByCriteria(criteria);
		if(!(listUserCategory.isEmpty()))
		{
			response.setData(listUserCategory);
			response.setStatus(true);
		}else{
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public Response updateUserCategoryDetails(IopushUsercategory UserCategory) {
		Response response = new Response();
		hibernateTemplate.saveOrUpdate(UserCategory);
		response.setStatus(true);
		return response;
		
	}

	@Override
	public Response insertUserCategoryDetails(IopushUsercategory UserCategory) {
		Response response = new Response();
		int i=0;
		i=(int) hibernateTemplate.save(UserCategory);
		this.hibernateTemplate.flush();
		if(i>0){
			response.setStatus(true);
			response.setIntegerResult(i);	

		}
		else{
			response.setStatus(false);
		}
		return response;
		
	}

	
	

}
