package com.saphire.iopush.daoImpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.saphire.iopush.dao.IUserDao;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.SecurityUtils;
import com.saphire.iopush.utils.Utility;

public class UserDaoImpl implements IUserDao {
	HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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
	public UserDetails findUserById(int userId) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
			criteria.add(Restrictions.eq("userId", userId));

			List<IopushUser> theUserList = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);

			if (theUserList.isEmpty()) {
				return null;
			}
			return (UserDetails) theUserList.iterator().next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//
	
	
	
	@Transactional()
	public Response updateUser(IopushUser iopushUser) {
		Response response = new Response();
		this.hibernateTemplate.setCheckWriteOperations(false);
		this.hibernateTemplate.update(iopushUser);
		this.hibernateTemplate.flush();
		response.setStatus(true);
		return response;
	}

	@SuppressWarnings("unchecked")
	public Response findUserByMail(String mailId) {
		Response response = new Response();
		DetachedCriteria criteria = DetachedCriteria.forClass(IopushUser.class);
		criteria.add(Restrictions.eq("emailId", mailId));
		List<IopushUser> userList = (List<IopushUser>) this.hibernateTemplate.findByCriteria(criteria);
		if (userList.size() > 0) {
			response.setScalarResult(userList.get(0));
			response.setStatus(true);

		} else {
			response.setStatus(false);
		}
		return response;
	}

	
	
	
}
