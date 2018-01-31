
package com.saphire.iopush.bean;

import java.text.ParseException;
import java.util.Date;

import org.hibernate.query.criteria.internal.Renderable;

import com.saphire.iopush.utils.Utility;

public class RSSFeedBean {
	int id=0;
	String name;
	String url;
	String campaign="";
	String source="";
	String generic="";
	String countries="";
	String cities="";
	String isps="";
	String query="";
	boolean activateRSS;
	int productId;
	String segments ="";
	String segmentTypes= "";
	String logo;
	String logo_path;

	String products="";
		Boolean notification=true;

	private int notificationCode;
	private String createdBy;
	private String creationDate;
	private Date modificationDate;
	private String modificedBy;
	private int automatedImage;
	private String subscribedFrom;
	// Order criteria

		String columnForOrdering="";
		String requiredOrder  = "";
	





	public RSSFeedBean(Object[] resnew) throws ParseException{
		this.id = Utility.intConverter(""+ resnew[0]);
		this.campaign=""+resnew[1];
		this.cities=""+resnew[2];
		this.countries=""+resnew[3];
		this.createdBy="" +resnew[4];
		this.creationDate= (""+ resnew[5]).replace("T", " ");
		this.generic=""+resnew[6];
		this.isps=""+resnew[7];
		this.modificedBy=""+resnew[9];
		this.name =""+ resnew[10];
		this.notificationCode=(int) resnew[11];
		this.notification=( (int)resnew[11]==1) ?true:false;
		this.products=""+resnew[12];
		this.source=""+resnew[13];
		this.url=""+resnew[14];
		this.automatedImage=(int) resnew[15];
		this.logo_path=""+resnew[16];
		this.segmentTypes=""+resnew[17];
		this.segments=""+resnew[18];
		this.modificationDate=(Date)resnew[8];
	}


	/**
	 * 
	 * 
	 * @return the segments
	 */
	public String getSegments() {
		return segments;
	}
	/**
	 * @param segments the segments to set
	 */
	public void setSegments(String segments) {
		this.segments = segments;
	}
	/**
	 * @return the segmentTypes
	 */
	public String getSegmentTypes() {
		return segmentTypes;
	}
	/**
	 * @param segmentTypes the segmentTypes to set
	 */
	public void setSegmentTypes(String segmentTypes) {
		this.segmentTypes = segmentTypes;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	String platform="";
	boolean useAutomatedImage=false;
	public boolean isUseAutomatedImage() {
		return useAutomatedImage;
	}
	public void setUseAutomatedImage(boolean useAutomatedImage) {
		this.useAutomatedImage = useAutomatedImage;
	}

	//Paging
	int startIndex=0;
	int pageSize=0;
	//Search
	int active=0;
	int inactive=0;
	/**
	 * @return the rssfeedid
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the countries
	 */
	public String getCountries() {
		return countries;
	}
	/**
	 * @return the cities
	 */
	public String getCities() {
		return cities;
	}
	/**
	 * @return the isps
	 */
	public String getIsps() {
		return isps;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param rssfeedid the rssfeedid to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param countries the countries to set
	 */
	public void setCountries(String countries) {
		this.countries = countries;
	}
	/**
	 * @param cities the cities to set
	 */
	public void setCities(String cities) {
		this.cities = cities;
	}
	
	
	public String getLogo_path() {
		return logo_path;
	}

	public void setLogo_path(String logo_path) {
		this.logo_path = logo_path;
	}

	/**
	 * @param isps the isps to set
	 */
	public void setIsps(String isps) {
		this.isps = isps;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the notification
	 */
//	public Boolean getNotification() {
//		return notification;
//	}
//	/**
//	 * @param notification the notification to set
//	 */
//	public void setNotification(Boolean notification) {
//		this.notification = notification;
//	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}



	/**
	 * @return the products
	 */
	public String getProducts() {
		return products;
	}
	/**
	 * @param products the products to set
	 */
	public void setProducts(String products) {
		this.products = products;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the campaign
	 */
	public String getCampaign() {
		return campaign;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @return the generic
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getActive() {
		return active;
	}
	public int getInactive() {
		return inactive;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public void setInactive(int inactive) {
		this.inactive = inactive;
	}


	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public boolean isActivateRSS() {
		return activateRSS;
	}
	public void setActivateRSS(boolean activateRSS) {
		this.activateRSS = activateRSS;
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


	

	public Boolean getNotification() {
		return notification;
	}

	public void setNotification(Boolean notification) {
		this.notification = notification;
	}


	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreationDate() {
		return creationDate;
	}

	
	public int getNotificationCode() {
		return notificationCode;
	}

	public void setNotificationCode(int notificationCode) {
		this.notificationCode = notificationCode;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificedBy() {
		return modificedBy;
	}

	public void setModificedBy(String modificedBy) {
		this.modificedBy = modificedBy;
	}

	public int getAutomatedImage() {
		return automatedImage;
	}

	public void setAutomatedImage(int automatedImage) {
		this.automatedImage = automatedImage;
	}

	public String getSubscribedFrom() {
		return subscribedFrom;
	}

	public void setSubscribedFrom(String subscribedFrom) {
		this.subscribedFrom = subscribedFrom;
	}


	@Override
	public String toString() {
		return "RSSFeedBean [id=" + id + ", name=" + name + ", url=" + url + ", campaign=" + campaign + ", source="
				+ source + ", generic=" + generic + ", countries=" + countries + ", cities=" + cities + ", isps=" + isps
				+ ", query=" + query + ", activateRSS=" + activateRSS + ", productId=" + productId + ", segments="
				+ segments + ", segmentTypes=" + segmentTypes + ", logo=" + logo + ", logo_path=" + logo_path
				+ ", products=" + products + ", notification=" + notification + ", notificationCode=" + notificationCode
				+ ", createdBy=" + createdBy + ", creationDate=" + creationDate + ", modificationDate="
				+ modificationDate + ", modificedBy=" + modificedBy + ", automatedImage=" + automatedImage
				+ ", subscribedFrom=" + subscribedFrom + ", columnForOrdering=" + columnForOrdering + ", requiredOrder="
				+ requiredOrder + ", platform=" + platform + ", useAutomatedImage=" + useAutomatedImage
				+ ", startIndex=" + startIndex + ", pageSize=" + pageSize + ", active=" + active + ", inactive="
				+ inactive + "]";
	}


	
	
	


}

