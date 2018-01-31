package com.saphire.iopush.bean;

import java.util.Date;

import com.saphire.iopush.model.IopushWelcome;

public class WelcomeStatsBean {

	private int IopushWelcomeStatsId;
	private String welcomeName;
	private Date date;
	private int sent;
	private int click;
	private int open;
	private int welcome_id;
	private int status;
	
	// Order criteria
			String columnForOrdering="";
			String requiredOrder  = "";
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}


	/**
	 * @return the open
	 */
	public int getOpen() {
		return open;
	}


	/**
	 * @param open the open to set
	 */
	public void setOpen(int open) {
		this.open = open;
	}


	/**
	 * @return the welcomeName
	 */
	public String getWelcomeName() {
		return welcomeName;
	}
	private int productId ;
	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}
	private int startIndex=0;
	private int pageSize=0;
	
	
	/**
	 * @return the iopushWelcome
	 */
	
	private String startDate;
	/**
	 * @return the welcome_id
	 */
	public int getWelcome_id() {
		return welcome_id;
	}


	/**
	 * @param welcome_id the welcome_id to set
	 */
	public void setWelcome_id(int welcome_id) {
		this.welcome_id = welcome_id;
	}
	private String endDate;
	public WelcomeStatsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public WelcomeStatsBean(int iopushWelcomeStatsId, String welcomeName, Date date,  int sent, int click,int open,
			int startIndex, int pageSize, String startDate, String endDate) {
		super();
		IopushWelcomeStatsId = iopushWelcomeStatsId;
		this.welcomeName = welcomeName;
		this.date = date;
	
		this.sent = sent;
		this.open = open;
		this.click = click;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
	}





	public WelcomeStatsBean(String welcomeName, Date date,  int sent, int click,int open,IopushWelcome iopushwelcome) {
		this.welcomeName = welcomeName;
		this.date = date;
		this.welcome_id = iopushwelcome.getWelcomeId();
		this.status = iopushwelcome.getWelcomeStatus();
		this.sent = sent;
		this.click = click;
		this.open = open;
	}

	
	public WelcomeStatsBean(int status , int welcomeid,String welcomeName, Date date,  int sent, int click,int open,int welcomeStatsId) {
		this.welcomeName = welcomeName;
		this.date = date;
		this.welcome_id = welcomeid;
		this.status = status;
		this.sent = sent;
		this.click = click;
		this.open = open;
		this.IopushWelcomeStatsId = welcomeStatsId;
		
	}

	


	public int getIopushWelcomeStatsId() {
		return IopushWelcomeStatsId;
	}
		public Date getDate() {
		return date;
	}

	public int getSent() {
		return sent;
	}
	public int getClick() {
		return click;
	}
	
	public int getStartIndex() {
		return startIndex;
	}



	public int getPageSize() {
		return pageSize;
	}


		public String getStartDate() {
		return startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public void setIopushWelcomeStatsId(int iopushwelcomeStatsId) {
		IopushWelcomeStatsId = iopushwelcomeStatsId;
	}
	public void setWelcomeName(String welcomeName) {
		this.welcomeName = welcomeName;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setSent(int sent) {
		this.sent = sent;
	}
	public void setClick(int click) {
		this.click = click;
	}


	public String getColumnForOrdering() {
		return columnForOrdering;
	}


	public String getRequiredOrder() {
		return requiredOrder;
	}


	public void setColumnForOrdering(String columnForOrdering) {
		this.columnForOrdering = columnForOrdering;
	}


	public void setRequiredOrder(String requiredOrder) {
		this.requiredOrder = requiredOrder;
	}
	
	
}
