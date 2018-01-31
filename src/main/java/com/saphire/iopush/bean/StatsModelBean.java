package com.saphire.iopush.bean;

import com.saphire.iopush.utils.Utility;

public class StatsModelBean {
	   int geoid=0;
	   int cityid=0;
	   int platformid=0;
	   int campaignSent;
	   int campaignOpen;
	   int campaignClose;
	   int campaignClick;
	   
	   public StatsModelBean() {
		}
		public StatsModelBean(Object[] result) {
			this.geoid =Utility.intConverter(""+ result[0]);
			this.cityid =Utility.intConverter(""+ result[1]);
			this.platformid =Utility.intConverter(""+ result[2]);
			this.campaignSent =Utility.intConverter(""+ result[3]);
			this.campaignOpen =Utility.intConverter(""+ result[4]);
			this.campaignClick =Utility.intConverter(""+ result[5]);
			this.campaignClose =Utility.intConverter(""+ result[6]);
		}
		
		
		/**
		 * @param geoid
		 * @param cityid
		 * @param platformid
		 * @param campaignSent
		 * @param campaignOpen
		 * @param campaignClick
		 * @param campaignClose
		 */
		public StatsModelBean(int geoid, int cityid, int platformid, int campaignSent, int campaignOpen, int campaignClick,
				int campaignClose) {
			this.geoid = geoid;
			this.cityid = cityid;
			this.platformid = platformid;
			this.campaignSent = campaignSent;
			this.campaignOpen = campaignOpen;
			this.campaignClick = campaignClick;
			this.campaignClose = campaignClose;
		}
		/**
		 * @return the geoid
		 */
		public int getGeoid() {
			return geoid;
		}
		/**
		 * @return the cityid
		 */
		public int getCityid() {
			return cityid;
		}
		/**
		 * @return the platformid
		 */
		public int getPlatformid() {
			return platformid;
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
		 * @param geoid the geoid to set
		 */
		public void setGeoid(int geoid) {
			this.geoid = geoid;
		}
		/**
		 * @param cityid the cityid to set
		 */
		public void setCityid(int cityid) {
			this.cityid = cityid;
		}
		/**
		 * @param platformid the platformid to set
		 */
		public void setPlatformid(int platformid) {
			this.platformid = platformid;
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
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format(
					"StatsModelBean [geoid=%s, cityid=%s, platformid=%s, campaignSent=%s, campaignOpen=%s, campaignClose=%s, campaignClick=%s]",
					geoid, cityid, platformid, campaignSent, campaignOpen, campaignClose, campaignClick);
		}
		
		
		
	}
	