package com.saphire.iopush.bean;

import java.util.Date;
import java.util.Objects;

import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.utils.Utility;

public class WelcomeBean {

	int welcomeId=0;

	private IopushProduct iopushProduct;
	private Integer welcomeclick;
	private Integer welcomeclose;
	private String welcomeCurrentDate;
	private String welcomeName; 
	private String source;
	private String generic;
	private String welcome;
	private Integer welcomeopen;
	private Integer welcomesent;
	private String welcomeScheduleDate;
	private Date welcomeScheduleDateInEdt;
	private String welcomeEndDate;
	private Date welcomeEndDateInEdt;
	private int welcomeStatus;
	private String createdBy;
	private Date creationDate;
	private Date modificationDate;
	private String modificedBy;
	private String file;
	private String title;
	private String description;
	private String forwardUrl;
	private String imagePath;
	private boolean unlimited;
	private boolean segmented;
	private String segments="";
	private String segmentTypes="";
	// Order criteria
	String columnForOrdering="";
	String requiredOrder  = "";
	//  Banner Image Fields
	private boolean largeImage;
	private String bannerImage="";

	
	
	
	

	public WelcomeBean(int welcomeId, IopushProduct iopushProduct, Integer welcomeclick, Integer welcomeclose,
			String welcomeCurrentDate, String welcomeName, String source, String generic, String welcome,
			Integer welcomeopen, Integer welcomesent, String welcomeScheduleDate, Date welcomeScheduleDateInEdt,
			String welcomeEndDate, Date welcomeEndDateInEdt, int welcomeStatus, String createdBy, Date creationDate,
			Date modificationDate, String modificedBy, String file, String title, String description, String forwardUrl,
			String imagePath, boolean unlimited, boolean segmented, String segments, String segmentTypes,
			String columnForOrdering, String requiredOrder, boolean largeImage, String bannerImage,
			boolean multiple_country, String platform, String countries, String cities, String isps, String subscribed,
			int subCampaignsCount, boolean active, int live, int pending, int expired, int draft, int productId,
			String segmentSelected, int pageSize, int startIndex) {
		super();
		this.welcomeId = welcomeId;
		this.iopushProduct = iopushProduct;
		this.welcomeclick = welcomeclick;
		this.welcomeclose = welcomeclose;
		this.welcomeCurrentDate = welcomeCurrentDate;
		this.welcomeName = welcomeName;
		this.source = source;
		this.generic = generic;
		this.welcome = welcome;
		this.welcomeopen = welcomeopen;
		this.welcomesent = welcomesent;
		this.welcomeScheduleDate = welcomeScheduleDate;
		this.welcomeScheduleDateInEdt = welcomeScheduleDateInEdt;
		this.welcomeEndDate = welcomeEndDate;
		this.welcomeEndDateInEdt = welcomeEndDateInEdt;
		this.welcomeStatus = welcomeStatus;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.modificedBy = modificedBy;
		this.file = file;
		this.title = title;
		this.description = description;
		this.forwardUrl = forwardUrl;
		this.imagePath = imagePath;
		this.unlimited = unlimited;
		this.segmented = segmented;
		this.segments = segments;
		this.segmentTypes = segmentTypes;
		this.columnForOrdering = columnForOrdering;
		this.requiredOrder = requiredOrder;
		this.largeImage = largeImage;
		this.bannerImage = bannerImage;
		this.multiple_country = multiple_country;
		this.platform = platform;
		this.countries = countries;
		this.cities = cities;
		this.isps = isps;
		this.subscribed = subscribed;
		this.subCampaignsCount = subCampaignsCount;
		this.active = active;
		this.live = live;
		this.pending = pending;
		this.expired = expired;
		this.draft = draft;
		this.productId = productId;
		this.segmentSelected = segmentSelected;
		this.pageSize = pageSize;
		this.startIndex = startIndex;
	}



	public boolean isActive() {
		return active;
	}
	
	

	/**
	 * @return the requireInteraction
	 */
	/*	public boolean isRequireInteraction() {
		return requireInteraction;
	}

	 *//**
	 * @param requireInteraction the requireInteraction to set
	 *//*
	public void setRequireInteraction(boolean requireInteraction) {
		this.requireInteraction = requireInteraction;
	}*/

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
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

	private boolean multiple_country = false;
	private String platform;
	private String countries;
	private String cities;
	private String isps;
	private String subscribed;
	private int subCampaignsCount;

	private boolean active;
	/**
	 * @return the segmentSelected
	 */
	public String getSegmentSelected() {
		return segmentSelected;
	}

	/**
	 * @param segmentSelected the segmentSelected to set
	 */
	public void setSegmentSelected(String segmentSelected) {
		this.segmentSelected = segmentSelected;
	}

	private int live=0;
	private int pending=0;
	private int expired=0;
	private int draft=0;
	private int productId=0;
	private String segmentSelected;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the pid
	 */

	public int getPending() {
		return pending;
	}


	public int getExpired() {
		return expired;
	}


	public int getDraft() {
		return draft;
	}


	public void setPending(int pending) {
		this.pending = pending;
	}


	public void setExpired(int expired) {
		this.expired = expired;
	}


	public void setDraft(int draft) {
		this.draft = draft;
	}

	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}

	private int pageSize;
	private int startIndex;

	public WelcomeBean() {

	}





	/**
	 * @return the welcomeId
	 */
	public int getWelcomeId() {
		return welcomeId;
	}


	/**
	 * @param welcomeId the welcomeId to set
	 */
	public void setWelcomeId(int welcomeId) {
		this.welcomeId = welcomeId;
	}




	/**
	 * @return the iopushProduct
	 */
	public IopushProduct getIopushProduct() {
		return iopushProduct;
	}


	/**
	 * @param iopushProduct the iopushProduct to set
	 */
	public void setIopushProduct(IopushProduct iopushProduct) {
		this.iopushProduct = iopushProduct;
	}


	/**
	 * @return the welcomeclick
	 */
	public Integer getWelcomeclick() {
		return welcomeclick;
	}


	/**
	 * @param welcomeclick the welcomeclick to set
	 */
	public void setWelcomeclick(Integer welcomeclick) {
		this.welcomeclick = welcomeclick;
	}


	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}


	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}


	/**
	 * @return the countries
	 */
	public String getCountries() {
		return countries;
	}


	/**
	 * @param countries the countries to set
	 */
	public void setCountries(String countries) {
		this.countries = countries;
	}


	/**
	 * @return the cities
	 */
	public String getCities() {
		return cities;
	}


	/**
	 * @param cities the cities to set
	 */
	public void setCities(String cities) {
		this.cities = cities;
	}


	/**
	 * @return the welcomeclose
	 */
	public Integer getWelcomeclose() {
		return welcomeclose;
	}


	/**
	 * @param welcomeclose the welcomeclose to set
	 */
	public void setWelcomeclose(Integer welcomeclose) {
		this.welcomeclose = welcomeclose;
	}


	/**
	 * @return the welcomeCurrentDate
	 */
	public String getWelcomeCurrentDate() {
		return welcomeCurrentDate;
	}


	/**
	 * @param welcomeCurrentDate the welcomeCurrentDate to set
	 */
	public void setWelcomeCurrentDate(String welcomeCurrentDate) {
		this.welcomeCurrentDate = welcomeCurrentDate;
	}


	/**
	 * @return the welcomeName
	 */
	public String getWelcomeName() {
		return welcomeName;
	}


	/**
	 * @param welcomeName the welcomeName to set
	 */
	public void setWelcomeName(String welcomeName) {
		this.welcomeName = welcomeName;
	}


	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}


	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * @return the generic
	 */
	public String getGeneric() {
		return generic;
	}


	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}


	/**
	 * @return the welcomeopen
	 */
	public Integer getWelcomeopen() {
		return welcomeopen;
	}


	/**
	 * @param welcomeopen the welcomeopen to set
	 */
	public void setWelcomeopen(Integer welcomeopen) {
		this.welcomeopen = welcomeopen;
	}


	/**
	 * @return the welcomesent
	 */
	public Integer getWelcomesent() {
		return welcomesent;
	}


	/**
	 * @param welcomesent the welcomesent to set
	 */
	public void setWelcomesent(Integer welcomesent) {
		this.welcomesent = welcomesent;
	}


	/**
	 * @return the welcomeScheduleDate
	 */
	public String getWelcomeScheduleDate() {
		return welcomeScheduleDate;
	}


	/**
	 * @param welcomeScheduleDate the welcomeScheduleDate to set
	 */
	public void setWelcomeScheduleDate(String welcomeScheduleDate) {
		this.welcomeScheduleDate = welcomeScheduleDate;
	}


	/**
	 * @return the welcomeScheduleDateInEdt
	 */
	public Date getWelcomeScheduleDateInEdt() {
		return welcomeScheduleDateInEdt;
	}


	/**
	 * @param welcomeScheduleDateInEdt the welcomeScheduleDateInEdt to set
	 */
	public void setWelcomeScheduleDateInEdt(Date welcomeScheduleDateInEdt) {
		this.welcomeScheduleDateInEdt = welcomeScheduleDateInEdt;
	}


	/**
	 * @return the welcomeEndDate
	 */
	public String getWelcomeEndDate() {
		return welcomeEndDate;
	}


	/**
	 * @param welcomeEndDate the welcomeEndDate to set
	 */
	public void setWelcomeEndDate(String welcomeEndDate) {
		this.welcomeEndDate = welcomeEndDate;
	}


	/**
	 * @return the welcomeEndDateInEdt
	 */
	public Date getWelcomeEndDateInEdt() {
		return welcomeEndDateInEdt;
	}


	/**
	 * @param welcomeEndDateInEdt the welcomeEndDateInEdt to set
	 */
	public void setWelcomeEndDateInEdt(Date welcomeEndDateInEdt) {
		this.welcomeEndDateInEdt = welcomeEndDateInEdt;
	}


	/**
	 * @return the welcomeStatus
	 */
	public int getWelcomeStatus() {
		return welcomeStatus;
	}


	/**
	 * @param welcomeStatus the welcomeStatus to set
	 */
	public void setWelcomeStatus(int welcomeStatus) {
		this.welcomeStatus = welcomeStatus;
	}


	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}


	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}





	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}


	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}


	/**
	 * @return the modificedBy
	 */
	public String getModificedBy() {
		return modificedBy;
	}


	/**
	 * @param modificedBy the modificedBy to set
	 */
	public void setModificedBy(String modificedBy) {
		this.modificedBy = modificedBy;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the forwardUrl
	 */
	public String getForwardUrl() {
		return forwardUrl;
	}


	/**
	 * @param forwardUrl the forwardUrl to set
	 */
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}


	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}


	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	/**
	 * @return the welcome
	 */
	public String getWelcome() {
		return welcome;
	}


	/**
	 * @param welcome the welcome to set
	 */
	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}


	/**
	 * @return the segmented
	 */
	public boolean isSegmented() {
		return segmented;
	}


	/**
	 * @param segmented the segmented to set
	 */
	public void setSegmented(boolean segmented) {
		this.segmented = segmented;
	}


	/**
	 * @return the multiple_country
	 */
	public boolean isMultiple_country() {
		return multiple_country;
	}


	/**
	 * @param multiple_country the multiple_country to set
	 */
	public void setMultiple_country(boolean multiple_country) {
		this.multiple_country = multiple_country;
	}


	/**
	 * @return the platform_id
	 */


	/**
	 * @return the isps
	 */
	public String getIsps() {
		return isps;
	}


	/**
	 * @param isps the isps to set
	 */
	public void setIsps(String isps) {
		this.isps = isps;
	}


	/**
	 * @return the subscribed
	 */
	public String getSubscribed() {
		return subscribed;
	}


	/**
	 * @param subscribed the subscribed to set
	 */
	public void setSubscribed(String subscribed) {
		this.subscribed = subscribed;
	}


	/**
	 * @return the subCampaignsCount
	 */
	public int getSubCampaignsCount() {
		return subCampaignsCount;
	}


	/**
	 * @param subCampaignsCount the subCampaignsCount to set
	 */
	public void setSubCampaignsCount(int subCampaignsCount) {
		this.subCampaignsCount = subCampaignsCount;
	}


	/**
	 * @return the live
	 */
	public int getLive() {
		return live;
	}


	/**
	 * @param live the live to set
	 */
	public void setLive(int live) {
		this.live = live;
	}




	public boolean isUnlimited() {
		return unlimited;
	}


	public void setUnlimited(boolean unlimited) {
		this.unlimited = unlimited;
	}



	public int getPageSize() {
		return pageSize;
	}


	public int getStartIndex() {
		return startIndex;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */



	/*
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	 */

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
	public int hashCode()
	{
		return Objects.hashCode(welcomeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WelcomeBean other = (WelcomeBean) obj;
		if (welcomeId != other.welcomeId)
			return false;
		return true;
	}
	public boolean isLargeImage() {
		return largeImage;
	}

	public void setLargeImage(boolean largeImage) {
		this.largeImage = largeImage;
	}

	public String getBannerImage() {
		return bannerImage;
	}

	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}

	public WelcomeBean(IopushWelcome object) {
		this.welcomeId = object.getWelcomeId();
		this.welcomeName =object.getWelcomeName();
		this.welcomeScheduleDate = (""+object.getWelcomeScheduleDate()).replace("T", " ");
		this.welcomeEndDate = (""+object.getWelcomeEndDate()).replace("T", " ");
		this.modificationDate = object.getModificationDate();
		this.welcomeStatus = object.getWelcomeStatus();
		this.source=object.getSource();
		this.title = object.getTitle();
		this.description = object.getDescription() ;
		this.forwardUrl = object.getForwardUrl() ;
		this.generic=object.getGeneric();
		this.imagePath = object.getImagePath() ;
		this.welcomesent=object.getWelcomesent();
		this.welcomeopen=object.getWelcomeopen();
		this.welcomeclose=object.getWelcomeclose();
		this.welcomeclick=object.getWelcomeclick();
		this.cities=object.getCities();
		this.segmented = object.isSegmented() ;
		this.countries = object.getCountries();
		this.platform = object.getPlatform();
		this.productId = object.getIopushProduct().getProductID() ;
		this.active = object.isActive();
		this.segments = object.getSegment_id();
		this.segmentTypes = object.getSegmentType_id();
		this.bannerImage = object.getBannerImage();
		this.largeImage = object.isLargeImage();
	}
	
	public WelcomeBean(Object[] object) {
		this.welcomeId = Utility.intConverter(""+object[0]);
		this.welcomeName =""+object[1];
		this.welcomeScheduleDate = (""+object[2]).replace("T", " ");
		this.welcomeEndDate = (""+object[3]).replace("T", " ");
		this.modificationDate = (Date)object[4];
		this.welcomeStatus = Utility.intConverter(""+object[5]);
		this.title = ""+object[6];
		this.description = ""+object[7] ;
		this.forwardUrl = ""+object[8] ;
		this.imagePath = ""+object[9] ;
		this.cities=""+object[10];
		this.segmented = (boolean)object[11] ;
		this.countries = ""+object[12];
		this.platform = ""+object[13];
		this.active = (boolean)object[14];
		this.segments = ""+object[15];
		this.segmentTypes = ""+object[16];
		this.productId =(int)object[17] ;
	}
	

	@Override
	public String toString() {
		return "WelcomeBean [welcomeId=" + welcomeId + ", iopushProduct=" + iopushProduct + ", welcomeclick="
				+ welcomeclick + ", welcomeclose=" + welcomeclose + ", welcomeCurrentDate=" + welcomeCurrentDate
				+ ", welcomeName=" + welcomeName + ", source=" + source + ", generic=" + generic + ", welcome="
				+ welcome + ", welcomeopen=" + welcomeopen + ", welcomesent=" + welcomesent + ", welcomeScheduleDate="
				+ welcomeScheduleDate + ", welcomeScheduleDateInEdt=" + welcomeScheduleDateInEdt + ", welcomeEndDate="
				+ welcomeEndDate + ", welcomeEndDateInEdt=" + welcomeEndDateInEdt + ", welcomeStatus=" + welcomeStatus
				+ ", createdBy=" + createdBy + ", creationDate=" + creationDate + ", modificationDate="
				+ modificationDate + ", modificedBy=" + modificedBy + ", title=" + title
				+ ", description=" + description + ", forwardUrl=" + forwardUrl 
				+ ", unlimited=" + unlimited + ", segmented=" + segmented + ", segments=" + segments + ", segmentTypes="
				+ segmentTypes + ", columnForOrdering=" + columnForOrdering + ", requiredOrder=" + requiredOrder
				+ ", multiple_country=" + multiple_country + ", platform=" + platform + ", countries=" + countries
				+ ", cities=" + cities + ", isps=" + isps + ", subscribed=" + subscribed + ", subCampaignsCount="
				+ subCampaignsCount + ", active=" + active + ", live=" + live + ", pending=" + pending + ", expired="
				+ expired + ", draft=" + draft + ", productId=" + productId + ", segmentSelected=" + segmentSelected
				+ ", pageSize=" + pageSize + ", startIndex=" + startIndex + "]";
	}



}
