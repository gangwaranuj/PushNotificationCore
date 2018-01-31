package com.saphire.iopush.daoImpl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.saphire.iopush.dao.IGenericDao;

public abstract class AbstractGenericDao<E> implements IGenericDao<E>{

	
	private final Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractGenericDao() {
		this.entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}
	
	@Autowired
	private HibernateTemplate hibernateTemplate;


	@Override
	public E findById(final Serializable id) {
		return (E) hibernateTemplate.get(this.entityClass, id);
	}

	@Override
	public Serializable save(E entity) {
		return hibernateTemplate.save(entity);
	}


	@Override
	public void delete(E entity) {
		hibernateTemplate.delete(entity);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		return (List<E>) hibernateTemplate.findByExample(this.entityClass);
	}

/*	@Override
	public List<E> findAllByExample(E entity) {
		Example example = Example.create(entity).ignoreCase().enableLike().excludeZeroes();
		return hibernateTemplate.createCriteria(this.entityClass).add(example).list();
	}*/

	@Override
	public void clear() {
		hibernateTemplate.clear();

	}

	@Override
	public void flush() {
		hibernateTemplate.flush();

	}
}
