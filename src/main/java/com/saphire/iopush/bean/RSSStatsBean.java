package com.saphire.iopush.bean;

import java.util.Date;

public class RSSStatsBean {

	private int IopushRSSStatsId;
	private String rssName;
	private Date date;
	private String rssURL;
	private int sent;
	private int click;
	private int productId ;

	// Order criteria
	String columnForOrdering="";
	String requiredOrder  = "";

	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}
	private int startIndex=0;
	private int pageSize=0;


	private String startDate;
	private String endDate;
	public RSSStatsBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public RSSStatsBean(int iopushRSSStatsId, String rssName, Date date, String rssURL, int sent, int click,
			int startIndex, int pageSize, String startDate, String endDate) {
		super();
		IopushRSSStatsId = iopushRSSStatsId;
		this.rssName = rssName;
		this.date = date;
		this.rssURL = rssURL;
		this.sent = sent;
		this.click = click;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
	}





	public RSSStatsBean(String rssName, Date date, String rssURL, int sent, int click,int RSSStatsId) {
		this.rssName = rssName;
		this.date = date;
		this.rssURL = rssURL;
		this.sent = sent;
		this.click = click;
		this.IopushRSSStatsId = RSSStatsId;
	}



	public int getIopushRSSStatsId() {
		return IopushRSSStatsId;
	}
	public String getRssName() {
		return rssName;
	}
	public Date getDate() {
		return date;
	}
	public String getRssURL() {
		return rssURL;
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



	public void setIopushRSSStatsId(int iopushRSSStatsId) {
		IopushRSSStatsId = iopushRSSStatsId;
	}
	public void setRssName(String rssName) {
		this.rssName = rssName;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setRssURL(String rssURL) {
		this.rssURL = rssURL;
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


	@Override
	public String toString() {
		return "RSSStatsBean [IopushRSSStatsId=" + IopushRSSStatsId + ", rssName=" + rssName + ", date=" + date
				+ ", rssURL=" + rssURL + ", sent=" + sent + ", click=" + click + ", productId=" + productId
				+ ", columnForOrdering=" + columnForOrdering + ", requiredOrder=" + requiredOrder + ", startIndex="
				+ startIndex + ", pageSize=" + pageSize + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}


	
}
