package com.saphire.iopush.bean;

import java.util.HashMap;

import com.saphire.iopush.utils.Utility;



public class PlatformStatBean implements Comparable<PlatformStatBean>{
	int platformId =0;
	String platformName = "";
	private int campaignSent=0;
	private int campaignOpen=0;
	private int campaignClose=0;
	private int campaignClick=0;

	/**	
	 * 
	 */
	public PlatformStatBean() {
	}
	public PlatformStatBean(Object[] result,HashMap<Integer,String> platform) {
		this.platformId =Utility.intConverter(""+ result[0]);
		this.platformName =platform.getOrDefault(Utility.intConverter(""+ result[0]),"");
		this.campaignClick =Utility.intConverter(""+ result[3]);
		this.campaignClose =Utility.intConverter(""+ result[4]);
		this.campaignOpen =Utility.intConverter(""+ result[2]);
		this.campaignSent =Utility.intConverter(""+ result[1]);
	}
	
	public PlatformStatBean(StatsModelBean statsModelBean,HashMap<Integer,String> platform) {
		this.platformId =statsModelBean.getPlatformid();
		this.platformName =platform.getOrDefault(statsModelBean.getPlatformid(),"");
		this.campaignClick =statsModelBean.getCampaignClick();
		this.campaignClose =statsModelBean.getCampaignClose();
		this.campaignOpen =statsModelBean.getCampaignOpen();
		this.campaignSent =statsModelBean.getCampaignSent();
	}
	
	public PlatformStatBean(int platformId,HashMap<Integer,String> platform) {
		this.platformId =platformId;
		this.platformName =platform.getOrDefault(platformId,"");
		this.campaignClick =0;
		this.campaignClose =0;
		this.campaignOpen =0;
		this.campaignSent =0;
	}
	/**
	 * @return the campaignSent
	 */
	public int getCampaignSent() {
		return campaignSent;
	}
	/**
	 * @return the campaignOpen
	 */
	public int getCampaignOpen() {
		return campaignOpen;
	}
	/**
	 * @return the campaignClose
	 */
	public int getCampaignClose() {
		return campaignClose;
	}
	/**
	 * @return the campaignClick
	 */
	public int getCampaignClick() {
		return campaignClick;
	}
	/**
	 * @param campaignSent the campaignSent to set
	 */
	public void setCampaignSent(int campaignSent) {
		this.campaignSent = campaignSent;
	}
	/**
	 * @param campaignOpen the campaignOpen to set
	 */
	public void setCampaignOpen(int campaignOpen) {
		this.campaignOpen = campaignOpen;
	}
	/**
	 * @param campaignClose the campaignClose to set
	 */
	public void setCampaignClose(int campaignClose) {
		this.campaignClose = campaignClose;
	}
	/**
	 * @param campaignClick the campaignClick to set
	 */
	public void setCampaignClick(int campaignClick) {
		this.campaignClick = campaignClick;
	}
	/**
	 * @return the platformId
	 */
	public int getPlatformId() {
		return platformId;
	}
	/**
	 * @return the platformName
	 */
	public String getPlatformName() {
		return platformName;
	}
	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	/**
	 * @param platformName the platformName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public int compareTo(PlatformStatBean o)
	{
	return this.platformName.compareTo(o.platformName);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"PlatformStatBean [platformId=%s, platformName=%s, campaignSent=%s, campaignOpen=%s, campaignClose=%s, campaignClick=%s]",
				platformId, platformName, campaignSent, campaignOpen, campaignClose, campaignClick);
	}
	
}
