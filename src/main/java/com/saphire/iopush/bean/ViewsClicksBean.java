package com.saphire.iopush.bean;

public class ViewsClicksBean {

	private int clickHits;
    private int viewHits;
    private String date;
    
    
    
	public ViewsClicksBean(String date, int clickHits, int viewHits ) {
		super();
		this.clickHits = clickHits;
		this.viewHits = viewHits;
		this.date = date;
	}
	
	
	/**
	 * @return the clickHits
	 */
	public int getClickHits() {
		return clickHits;
	}
	/**
	 * @param clickHits the clickHits to set
	 */
	public void setClickHits(int clickHits) {
		this.clickHits = clickHits;
	}
	/**
	 * @return the viewHits
	 */
	public int getViewHits() {
		return viewHits;
	}
	/**
	 * @param viewHits the viewHits to set
	 */
	public void setViewHits(int viewHits) {
		this.viewHits = viewHits;
	}
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViewsClicksBean [clickHits=" + clickHits + ", viewHits=" + viewHits + ", date=" + date + "]";
	}

	
	
    
	
}
