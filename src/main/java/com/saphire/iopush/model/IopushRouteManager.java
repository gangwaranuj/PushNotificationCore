package com.saphire.iopush.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="iopush_route_manager")
public class IopushRouteManager implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 @Id   
	    @SequenceGenerator(name="iopush_messaging_route_manager_seq", sequenceName="iopush_messaging_route_manager_seq",allocationSize=1)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_messaging_route_manager_seq")
	 @Column(name = "route_id", unique = true, nullable = false,columnDefinition = "serial")
	private int routeId;
	 
	 @Column(name="route_name", length = 100)
	private String routeName;
	 
	 @Column(name="options", length = 100)
	private String options;
	 
	 @Column(name="active")
	 private int active;
	 
	public IopushRouteManager() {
		// TODO Auto-generated constructor stub
	}

	public IopushRouteManager(int routeId, String routeName, String options) {
		super();
		this.routeId = routeId;
		this.routeName = routeName;
		this.options = options;
	}
	public int getRouteId() {
		return routeId;
	}
	public String getRouteName() {
		return routeName;
	}
	public String getOptions() {
		return options;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
	
}
