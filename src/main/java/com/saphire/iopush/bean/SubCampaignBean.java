package com.saphire.iopush.bean;

import java.util.Date;
import java.util.Map;

import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.model.IopushSubCampaign;

public class SubCampaignBean {

	private int subcampaignId;
	private IopushCampaign iopushCampaign;
	private String campaignName; 
	private String campaignScheduleDate;
	private Date campaignScheduleDateInEdt;
	private String campaignEndDate;
	private Date campaignEndDateInEdt;
	private int campaignStatus;
	private int eligiblecount;
	
	
	
	public SubCampaignBean() {
		// TODO Auto-generated constructor stub
	}



	public SubCampaignBean(String campaignName,
			String campaignScheduleDate, Date campaignScheduleDateInEdt, String campaignEndDate,
			Date campaignEndDateInEdt, int campaignStatus, int eligiblecount) {
		this.campaignName = campaignName;
		this.campaignScheduleDate = campaignScheduleDate;
		this.campaignScheduleDateInEdt = campaignScheduleDateInEdt;
		this.campaignEndDate = campaignEndDate;
		this.campaignEndDateInEdt = campaignEndDateInEdt;
		this.campaignStatus = campaignStatus;
		this.eligiblecount = eligiblecount;
	}



	public SubCampaignBean(IopushSubCampaign subCampaign, Map<String, String> ruleMap, String imageurl) {
		
	}



	public int getSubcampaignId() {
		return subcampaignId;
	}



	public IopushCampaign getIopushCampaign() {
		return iopushCampaign;
	}



	public String getCampaignName() {
		return campaignName;
	}



	public String getCampaignScheduleDate() {
		return campaignScheduleDate;
	}



	public Date getCampaignScheduleDateInEdt() {
		return campaignScheduleDateInEdt;
	}



	public String getCampaignEndDate() {
		return campaignEndDate;
	}



	public Date getCampaignEndDateInEdt() {
		return campaignEndDateInEdt;
	}



	public int getCampaignStatus() {
		return campaignStatus;
	}



	public int getEligiblecount() {
		return eligiblecount;
	}



	public void setSubcampaignId(int subcampaignId) {
		this.subcampaignId = subcampaignId;
	}



	public void setIopushCampaign(IopushCampaign iopushCampaign) {
		this.iopushCampaign = iopushCampaign;
	}



	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}



	public void setCampaignScheduleDate(String campaignScheduleDate) {
		this.campaignScheduleDate = campaignScheduleDate;
	}



	public void setCampaignScheduleDateInEdt(Date campaignScheduleDateInEdt) {
		this.campaignScheduleDateInEdt = campaignScheduleDateInEdt;
	}



	public void setCampaignEndDate(String campaignEndDate) {
		this.campaignEndDate = campaignEndDate;
	}



	public void setCampaignEndDateInEdt(Date campaignEndDateInEdt) {
		this.campaignEndDateInEdt = campaignEndDateInEdt;
	}



	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}



	public void setEligiblecount(int eligiblecount) {
		this.eligiblecount = eligiblecount;
	}



	@Override
	public String toString() {
		return "SubCampaignBean [subcampaignId=" + subcampaignId + ", iopushCampaign=" + iopushCampaign
				+ ", campaignName=" + campaignName + ", campaignScheduleDate=" + campaignScheduleDate
				+ ", campaignScheduleDateInEdt=" + campaignScheduleDateInEdt + ", campaignEndDate=" + campaignEndDate
				+ ", campaignEndDateInEdt=" + campaignEndDateInEdt + ", campaignStatus=" + campaignStatus
				+ ", eligiblecount=" + eligiblecount + "]";
	}
	
	
	
	
}
