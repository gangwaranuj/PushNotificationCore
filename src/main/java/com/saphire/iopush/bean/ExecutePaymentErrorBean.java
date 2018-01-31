package com.saphire.iopush.bean;

import java.util.Arrays;

public class ExecutePaymentErrorBean {



	private String name;
	private String message;
	private String information_link;
	private String debug_id;
	private ExecutePaymentDetailBean[] details;


	public ExecutePaymentErrorBean(String name, String message, String information_link, String debug_id) {
		super();
		this.name = name;
		this.message = message;
		this.information_link = information_link;
		this.debug_id = debug_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getInformation_link() {
		return information_link;
	}
	public void setInformation_link(String information_link) {
		this.information_link = information_link;
	}
	public String getDebug_id() {
		return debug_id;
	}
	public void setDebug_id(String debug_id) {
		this.debug_id = debug_id;
	}
	
	
	
	public ExecutePaymentDetailBean[] getDetails() {
		return details;
	}
	public void setDetails(ExecutePaymentDetailBean[] details) {
		this.details = details;
	}
	@Override
	public String toString() {
		final int maxLen = 10;
		return "ExecutePaymentErrorBean [" + (name != null ? "name=" + name + ", " : "")
				+ (message != null ? "message=" + message + ", " : "")
				+ (information_link != null ? "information_link=" + information_link + ", " : "")
				+ (debug_id != null ? "debug_id=" + debug_id + ", " : "") + (details != null
						? "details=" + Arrays.asList(details).subList(0, Math.min(details.length, maxLen)) : "")
				+ "]";
	}
	


}
