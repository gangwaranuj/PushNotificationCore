package com.saphire.iopush.dao;

import java.io.Serializable;
import java.util.List;

/**
* Interface to provide common DAO methods
* 
*/

public interface IGenericDao<E>
{

	Serializable save(E entity);
	
	void delete( E entity );
	
	List<E> findAll();
	
//	List<E> findAllByExample( E entity );
	
	E findById( Serializable id );
	
	void clear();
	
	void flush();

}