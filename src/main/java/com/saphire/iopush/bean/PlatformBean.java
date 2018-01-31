package com.saphire.iopush.bean;

import java.util.HashMap;

public class PlatformBean implements Comparable<PlatformBean> {
	String platformName = "";
	int count = 0;
	
	int platformId =0;
	
	public int getCampaignSent() {
		return campaignSent;
	}


	public int getCampaignOpen() {
		return campaignOpen;
	}


	public int getCampaignClose() {
		return campaignClose;
	}


	public int getCampaignClick() {
		return campaignClick;
	}


	public void setCampaignSent(int campaignSent) {
		this.campaignSent = campaignSent;
	}


	public void setCampaignOpen(int campaignOpen) {
		this.campaignOpen = campaignOpen;
	}


	public void setCampaignClose(int campaignClose) {
		this.campaignClose = campaignClose;
	}


	public void setCampaignClick(int campaignClick) {
		this.campaignClick = campaignClick;
	}

	private int campaignSent=0;
	private int campaignOpen=0;
	private int campaignClose=0;
	private int campaignClick=0;
	
	public String getPlatformName() {
		return platformName;
	}


	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	



	public PlatformBean(String platformName) {
		super();
		this.platformName = platformName;
	}


	public PlatformBean(String platformName, int count)
	{
		this.platformName = platformName;
		this.count = count;
	}


	public int getCount() { return this.count; }

	public void setCount(int count) {
		this.count = count;
	}

	public int compareTo(PlatformBean o)
	{
		return this.platformName.compareTo(o.platformName);
	}

	public PlatformBean(int platformId,HashMap<Integer,String> platform) {
		this.platformId =platformId;
		this.platformName =platform.getOrDefault(platformId,"");
		this.campaignClick =0;
		this.campaignClose =0;
		this.campaignOpen =0;
		this.campaignSent =0;
	}
	
}