package com.saphire.iopush.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.utils.Utility;

public class CampaignBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	int campaign_id=0;
	String campaign_name="";
	String source;
	String generic;
	int timezone_id=1;
	String timezone;
	String campaign_schedule_date;
	String campaign_current_date;
	String campaign_end_date;
	String modification_date;
	int productId=0 ;
	int campaign_status=0;
	int eligiblecount=0;
	String campaign;
	Boolean isSegmented;
	String campaign_modification_date;
	long expirationTime;
	boolean requireInteraction;
	//Search
	int live=0;
	int pending=0;
	int expired=0;
	int draft=0;
	//Paging
	int startIndex=0;
	int pageSize=0;
	//Criteria
	String countries="";
	String cities="";
	String isps="";
	String platform="";
	String subscribed="";
	String segments="";
	String segmentTypes="";
	// Order criteria
	String columnForOrdering="";
	String requiredOrder  = "";
	//Stats
	int sent;
	int open;
	int close;
	int click;
	String conversionRate="";
	//Notification Info
	private String forwardUrl;
	private String description;
	private String imagePath;
	private String title ;
	String file="";
//  Banner Image Fields
	private boolean largeImage;
	private String bannerImage="";
	
	
	/**
	 * @return the segments
	 */
	public String getSegments() {
		return segments;
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

	/**
	 * @param segments the segments to set
	 */
	public void setSegments(String segments) {
		this.segments = segments;
	}


	
	public CampaignBean(Object[] resnew) {
		this.campaign_id = Utility.intConverter(""+ resnew[0]);
		this.campaign_name =""+ resnew[1];
		this.campaign_schedule_date = (""+ resnew[2]).replace("T", " ");
		this.campaign_current_date =  (""+ resnew[3]).replace("T", " ");
		this.campaign_status = Utility.intConverter(""+ resnew[4]);
	}
	
	public CampaignBean(Object[] resnew,String platform) throws ParseException{
		this.campaign_id = Utility.intConverter(""+ resnew[0]);
		this.campaign_name =""+ resnew[1];
		this.campaign_schedule_date = (""+ resnew[2]).replace("T", " ");
		this.campaign_current_date =  (""+ resnew[3]).replace("T", " ");
		this.campaign_status = Utility.intConverter(""+ resnew[4]);
		this.platform=platform;
		this.timezone_id = Utility.intConverter(""+ resnew[6]);
		this.timezone = ""+ resnew[5];
		this.campaign_end_date = (""+ resnew[7]).replace("T", " ");
		this.isSegmented =  (Boolean) resnew[8];
		this.modification_date =  ""+resnew[9];
	}
	

	public CampaignBean(int campaign_id , String CampaignName, String CampaignScheduleDate, int CampaignStatus, int Campaignclick, int Campaignsent ,int Campaignopen) throws ParseException{
		this.campaign_id = campaign_id;
		this.campaign_name = CampaignName;
		this.campaign_schedule_date =CampaignScheduleDate;
		this.campaign_status = CampaignStatus;
		this.click = Campaignclick;
		this.sent = Campaignsent;
		this.open = Campaignopen;
	}
	
	
	public CampaignBean(IopushCampaign iopushCampaign) {
		this.campaign_id = iopushCampaign.getCampaignId();
		this.campaign_name =iopushCampaign.getCampaignName();
		this.timezone_id = iopushCampaign.getIopushTimeZone().getTimezoneID();
		this.timezone = iopushCampaign.getIopushTimeZone().getTimezone();
		this.campaign_schedule_date = iopushCampaign.getCampaignScheduleDate().replace("T", " ");
		this.campaign_current_date = iopushCampaign.getCampaignCurrentDate().replace("T", " ");
		this.campaign_end_date = iopushCampaign.getCampaignEndDate().replace("T", " ");
		this.modification_date = ""+iopushCampaign.getModificationDate();
		this.campaign_status = iopushCampaign.getCampaignStatus();
		this.eligiblecount = iopushCampaign.getEligiblecount();
		this.source=iopushCampaign.getSource();
		this.generic=iopushCampaign.getGeneric();
		this.sent=iopushCampaign.getCampaignsent();
		this.open=iopushCampaign.getCampaignopen();
		this.close=iopushCampaign.getCampaignclose();
		this.click=iopushCampaign.getCampaignclick();
		 if(this.open>0){
			   this.conversionRate=(Math.round((float)this.click * 100/this.open))+"%";
			   }else{
			    this.conversionRate="0%"; 
			   }
	}
	
	
	public CampaignBean(IopushCampaign iopushCampaign,Map<String,String> ruleMap, String url) {
		this.campaign_id = iopushCampaign.getCampaignId();
		this.campaign_name =iopushCampaign.getCampaignName();
		this.timezone_id = iopushCampaign.getIopushTimeZone().getTimezoneID();
		this.timezone = iopushCampaign.getIopushTimeZone().getTimezone();
		this.campaign_schedule_date = iopushCampaign.getCampaignScheduleDate().replace("T", " ");
		this.campaign_current_date = iopushCampaign.getCampaignCurrentDate().replace("T", " ");
		this.campaign_end_date = iopushCampaign.getCampaignEndDate().replace("T", " ");
		this.modification_date = ""+iopushCampaign.getModificationDate();
		this.campaign_status = iopushCampaign.getCampaignStatus();
		this.eligiblecount = iopushCampaign.getEligiblecount();
		this.source=iopushCampaign.getSource();
		this.generic=iopushCampaign.getGeneric();
		this.isSegmented = iopushCampaign.isSegmented();
		this.countries = ruleMap.getOrDefault("country", "");
		this.cities = ruleMap.getOrDefault("city", "");
		this.platform=ruleMap.getOrDefault("platform", "");
		this.subscribed = ruleMap.getOrDefault("subscribed", "");
		this.isps=ruleMap.getOrDefault("ISP", "");
		this.segments = ruleMap.getOrDefault("segments", "");
		this.segmentTypes = ruleMap.getOrDefault("segmentTypes", "");
		this.forwardUrl = iopushCampaign.getForwardUrl();
		this.description = iopushCampaign.getDescription();
		this.title = iopushCampaign.getTitle();
		this.imagePath = url+iopushCampaign.getImagePath();
		this.campaign = iopushCampaign.getCampaign();
		this.productId = iopushCampaign.getIopushProduct().getProductID() ;
		this.requireInteraction = iopushCampaign.isRequireInteraction();
		this.largeImage = iopushCampaign.isLargeImage();
		this.bannerImage = iopushCampaign.getBannerImage();
	}

	
	

	public CampaignBean() {
		// TODO Auto-generated constructor stub
	}


	
	public int getCampaign_id() {
		return campaign_id;
	}


	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}


	public String getCampaign_name() {
		return campaign_name;
	}


	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getGeneric() {
		return generic;
	}


	public void setGeneric(String generic) {
		this.generic = generic;
	}


	public int getTimezone_id() {
		return timezone_id;
	}


	public void setTimezone_id(int timezone_id) {
		this.timezone_id = timezone_id;
	}


	public String getTimezone() {
		return timezone;
	}


	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}


	public String getCampaign_schedule_date() {
		return campaign_schedule_date;
	}


	public void setCampaign_schedule_date(String campaign_schedule_date) {
		this.campaign_schedule_date = campaign_schedule_date;
	}


	public String getCampaign_current_date() {
		return campaign_current_date;
	}


	public void setCampaign_current_date(String campaign_current_date) {
		this.campaign_current_date = campaign_current_date;
	}


	public Object getModification_date() {
		return modification_date;
	}


	public void setModification_date(Date modification_date) {
		this.modification_date = ""+modification_date;
	}


	public int getCampaign_status() {
		return campaign_status;
	}


	public void setCampaign_status(int campaign_status) {
		this.campaign_status = campaign_status;
	}


	public int getEligiblecount() {
		return eligiblecount;
	}


	public void setEligiblecount(int eligiblecount) {
		this.eligiblecount = eligiblecount;
	}


	public int getLive() {
		return live;
	}


	public void setLive(int live) {
		this.live = live;
	}


	public int getPending() {
		return pending;
	}


	public void setPending(int pending) {
		this.pending = pending;
	}


	public int getExpired() {
		return expired;
	}


	public void setExpired(int expired) {
		this.expired = expired;
	}


	public int getDraft() {
		return draft;
	}


	public void setDraft(int draft) {
		this.draft = draft;
	}


	public int getStartIndex() {
		return startIndex;
	}


	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public String getCountries() {
		return countries;
	}


	public void setCountries(String countries) {
		this.countries = countries;
	}


	public String getCities() {
		return cities;
	}


	public void setCities(String cities) {
		this.cities = cities;
	}


	public String getIsps() {
		return isps;
	}


	public void setIsps(String isps) {
		this.isps = isps;
	}


	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String devices) {
		this.platform = devices;
	}


	public String getSubscribed() {
		return subscribed;
	}


	public void setSubscribed(String subscribed) {
		this.subscribed = subscribed;
	}


	public int getSent() {
		return sent;
	}


	public void setSent(int sent) {
		this.sent = sent;
	}


	public int getOpen() {
		return open;
	}


	public void setOpen(int open) {
		this.open = open;
	}


	public int getClose() {
		return close;
	}


	public void setClose(int close) {
		this.close = close;
	}


	public int getClick() {
		return click;
	}


	public void setClick(int click) {
		this.click = click;
	}


	public String getConversionRate() {
		return conversionRate;
	}


	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}


	public String getForwardUrl() {
		return forwardUrl;
	}


	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public String getCampaign() {
		return campaign;
	}


	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}


	
	public Boolean getIsSegmented() {
		return isSegmented;
	}

	public void setIsSegmented(Boolean isSegmented) {
		this.isSegmented = isSegmented;
	}

	public String getCampaign_end_date() {
		return campaign_end_date;
	}

	public void setCampaign_end_date(String campaign_end_date) {
		this.campaign_end_date = campaign_end_date;
	}

	
	
	public String getCampaign_modification_date() {
		return campaign_modification_date;
	}

	public void setCampaign_modification_date(String campaign_modification_date) {
		this.campaign_modification_date = campaign_modification_date;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public boolean isRequireInteraction() {
		return requireInteraction;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
	

	
	public void setRequireInteraction(boolean requireInteraction) {
		this.requireInteraction = requireInteraction;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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
		/**
	 * @return the largeImage
	 */
	public boolean isLargeImage() {
		return largeImage;
	}

	/**
	 * @param largeImage the largeImage to set
	 */
	public void setLargeImage(boolean largeImage) {
		this.largeImage = largeImage;
	}

	/**
	 * @return the bannerImage
	 */
	public String getBannerImage() {
		return bannerImage;
	}

	/**
	 * @param bannerImage the bannerImage to set
	 */
	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CampaignBean [campaign_id=" + campaign_id + ", campaign_name=" + campaign_name + ", source=" + source
				+ ", generic=" + generic + ", timezone_id=" + timezone_id + ", timezone=" + timezone
				+ ", campaign_schedule_date=" + campaign_schedule_date + ", campaign_current_date="
				+ campaign_current_date + ", campaign_end_date=" + campaign_end_date + ", modification_date="
				+ modification_date + ", productId=" + productId + ", campaign_status=" + campaign_status
				+ ", eligiblecount=" + eligiblecount + ", campaign=" + campaign + ", isSegmented=" + isSegmented
				+ ", campaign_modification_date=" + campaign_modification_date + ", expirationTime=" + expirationTime
				+ ", requireInteraction=" + requireInteraction + ", live=" + live + ", pending=" + pending
				+ ", expired=" + expired + ", draft=" + draft + ", startIndex=" + startIndex + ", pageSize=" + pageSize
				+ ", countries=" + countries + ", cities=" + cities + ", isps=" + isps + ", platform=" + platform
				+ ", subscribed=" + subscribed + ", segments=" + segments + ", segmentTypes=" + segmentTypes
				+ ", columnForOrdering=" + columnForOrdering + ", requiredOrder=" + requiredOrder + ", sent=" + sent
				+ ", open=" + open + ", close=" + close + ", click=" + click + ", conversionRate=" + conversionRate
				+ ", forwardUrl=" + forwardUrl + ", description=" + description + ", title=" + title + ", file=" + file
				+ ", largeImage=" + largeImage + "]";
	}

	/*@Override
	public int compareTo(CampaignBean o) {
		
		if (this.getCampaign_id() > o.getCampaign_id()) {
			return 1;
		} else if (this.getCampaign_id() < o.getCampaign_id()) {
			return -1;
		} else {
			return 0;
		}
	}*/



	
	}

		
	